package com.example.mj_mobileserver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private SharedPreferences sharedPreferences = null;
	private TextView stolenIsOpenTextView = null;
	private ImageView stolenIsOpenImageView = null;
	private TextView safetyNumberTextView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		
		boolean configed = sharedPreferences.getBoolean("configed", false);
				if (configed) {
			setContentView(R.layout.activity_lost_find);
		} else {
			Intent intent = new Intent();
			intent.setClass(LostFindActivity.this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}
		stolenIsOpenImageView = (ImageView) findViewById(R.id.stolenisopen_imageview);
		stolenIsOpenTextView = (TextView) findViewById(R.id.stolenisopen_textview);
		safetyNumberTextView = (TextView) findViewById(R.id.safety_number);
		boolean isOpen = sharedPreferences.getBoolean("protecting", false);
		String phoneString=sharedPreferences.getString("phone",null);
		if (isOpen) {
			stolenIsOpenImageView.setImageResource(R.drawable.lock);
		} else {
			stolenIsOpenImageView.setImageResource(R.drawable.unlock);
		}
		if (TextUtils.isEmpty(phoneString)) {
			
		} else {
safetyNumberTextView.setText(phoneString);
		}
	}

	/**
	 * 给text加入事件
	 * 
	 * @param view
	 */
	public void reEnterSetup(View view) {
		Intent intent = new Intent();
		intent.setClass(LostFindActivity.this, Setup1Activity.class);
		startActivity(intent);
		finish();
	}

}
