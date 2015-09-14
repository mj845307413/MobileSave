package com.example.mj_mobileserver;

import com.example.mj_mobileserver.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	private GestureDetector gestureDetector=null;
	protected SharedPreferences sharedPreferences=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
		gestureDetector = new GestureDetector(this,
				new LearnGestureListener());

	}
	
	class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Math.abs(e1.getRawY()-e2.getRawY())>100) {
				Toast.makeText(getApplicationContext(), "哥们你划得方向错了啊，蠢不？", 2).show();
				return true;
			}
			if (Math.abs(velocityX)<200) {
				Toast.makeText(getApplicationContext(), "你划得太慢了，给老子划快点", 2).show();
				return true;
			}
			if (e2.getRawX() - e1.getRawX() > 200) {
				showPre();
			} else if (e1.getRawX() - e2.getRawX() > 200) {
				showNext();
			}
			// TODO Auto-generated method stub
			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}
	public abstract void showNext();
	public abstract void showPre();
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
