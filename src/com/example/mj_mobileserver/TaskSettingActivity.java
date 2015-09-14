package com.example.mj_mobileserver;

import service.AutoCleanService;
import service.AutoCleanService1;
import utils.ServiceUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskSettingActivity extends Activity {
	private CheckBox cb_show_system;
	private CheckBox cb_auto_clean;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_setting);
		cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
		cb_auto_clean = (CheckBox) findViewById(R.id.cb_auto_clean);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		boolean show_system_boolean = sharedPreferences.getBoolean(
				"cb_show_system", false);
		cb_show_system.setChecked(show_system_boolean);
		System.out.println("creat cb_show");
		cb_auto_clean.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TaskSettingActivity.this,
						AutoCleanService1.class);
				if (isChecked) {
					startService(intent);
					System.out.println("cb_clean_change1");
				}else {
					stopService(intent);
				}
			}
		});
		cb_show_system
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor editor = sharedPreferences.edit();
						editor.putBoolean("cb_show_system", isChecked);

						editor.commit();

						// TODO Auto-generated method stub
					}
				});
//		CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
//
//			@Override
//			public void onTick(long millisUntilFinished) {
//				// TODO Auto-generated method stub
//				System.out.println(millisUntilFinished);
//			}
//
//			@Override
//			public void onFinish() {
//				// TODO Auto-generated method stub
//				System.out.println("finish");
//			}
//		};
//		countDownTimer.start();
	}
@Override
protected void onStart() {
	// TODO Auto-generated method stub
	
	super.onStart();
}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		boolean isServiceRun = ServiceUtils.isServiceRunning(
				this, "service.AutoCleanService1");
			cb_auto_clean.setChecked(isServiceRun);
		super.onResume();
	}
}
