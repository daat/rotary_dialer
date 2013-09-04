package com.rotarydialer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class RotaryImage extends ImageView {
	
	private int radius, nowNum;
	private float x, y;
	private boolean isTouch, isReturn, isEnd;
	private float sumDegree, preDegree, nowDegree;
	private Bitmap bm;
	private Handler mHandler;
	private Activity act;
	
	public RotaryImage(Context context)
	{
		 this(context, null);
	}
	public RotaryImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
	/*要有建構元才能執行*/
	public RotaryImage (Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mHandler = new Handler();
		isTouch = false;
		isReturn = false;
		sumDegree = 0f;
		nowNum = -1;
		isEnd = false;
	}
	
	public void init(Bitmap bm, Activity act)
	{		
		this.bm = bm;
		this.act = act;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{	/*getWidth()只能在onDraw用*/
		radius = getWidth()/2;
			Matrix m = new Matrix(); 
			Paint p = new Paint();
			/*將圖縮放到與view一樣大*/
			m.preScale(2f*radius/bm.getWidth(), 2f*radius/bm.getHeight());
			/*將旋轉中心設在圖的正中間, 旋轉sumDegree度*/
			m.preRotate(sumDegree, bm.getWidth()/2, bm.getHeight()/2);
			canvas.drawBitmap(bm, m, p);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(isReturn)return true;
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(isTouch)return true;
				x = event.getX() - radius;
				y = event.getY() - radius;
				if((x * x + y * y) > radius * radius || (x * x + y * y) < radius * radius/4)return true;
				
				preDegree = computeDegree(x, y);
				if(preDegree < 135f)
				{
					return true;
				}
				nowNum = 9 - (int)((preDegree - 135f)/22.5f);
				isTouch = true;
				return true;
				
			case MotionEvent.ACTION_MOVE:
				if(!isTouch || isEnd)return true;
				x = event.getX() - radius;
				y = event.getY() - radius;
				nowDegree = computeDegree(x, y);
				Log.d("ttt", Float.toString(nowDegree));
				if(nowDegree >=45f && nowDegree < 135f)
				{
					if(nowNum >=0 && nowNum <= 9)((MainActivity)act).addNum(nowNum);
					isEnd = true;
					return true;
				}
				/*不能逆時針轉*/
				if(nowDegree < preDegree || nowDegree - preDegree > 300f)
				{
					preDegree = nowDegree;
					return true;
				}
				sumDegree += nowDegree - preDegree;
				if(sumDegree >= 360f)sumDegree %= 360;
				else if(sumDegree <= -360f)sumDegree = -((-sumDegree)%360);
				preDegree = nowDegree;
				invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				isTouch = false;
				isReturn = true;
				isEnd = false;
				mHandler.post(returnAnimation);
				return true;
		}
		return false;
	}
	
	private Runnable returnAnimation = new Runnable()
	{
		public void run()
		{
			if(sumDegree - 3f <= 0f)
			{
				sumDegree = 0;
				isReturn = false;
				invalidate();
				return;
			}
			else 
			{
				sumDegree -= 3f;
				invalidate();
				mHandler.postDelayed(returnAnimation, 8);
			}
		}
	};
	
	/*計算手指位置與中心所呈的角度*/
	private float computeDegree(float x, float y)
	{
		double tan;
		double deg;
		if(x == 0)
		{
			if(y > 0)return 90f;
			else if(y < 0)return 270f;
			else return preDegree;
		}
		else if(x > 0)
		{
			tan = (double)Math.abs(y/x);
			deg = Math.atan(tan);//回傳值為徑度
			if(y > 0)return (float)(deg*180/Math.PI);
			else if(y < 0)return 360f - (float)(deg*180/Math.PI);
			else return 0f;
		}
		else
		{
			tan = (double)Math.abs(y/x);
			deg = Math.atan(tan);
			if(y > 0)return (float)(180.0-(deg*180/Math.PI));
			else if(y < 0)return (float)(180.0+(deg*180/Math.PI));
			else return 0f;
		}
		
	}
}
