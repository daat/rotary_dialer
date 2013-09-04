package com.rotarydialer;

import com.daat.usefulkit.BitmapKit;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	RotaryImage iv_rotater;
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iv_rotater = (RotaryImage)findViewById(R.id.rotater);
		iv_rotater.init(BitmapKit.readBitMap(this, R.drawable.rotater), this);
		tv = (TextView)findViewById(R.id.tv);
		
		//iv_rotater.setOnTouchListener(new rotateListener());
		Log.d("ttt", "ok");
		
	}
	
	public void addNum(int n)
	{
		tv.setText(tv.getText() + Integer.toString(n));
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
