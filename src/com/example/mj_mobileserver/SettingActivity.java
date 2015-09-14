package com.example.mj_mobileserver;


import service.CallSmsSafeService;
import service.ShowAddressService;
import service.WatchDog;
import ui.SettingClickView;
import ui.SettingItemView;
import utils.ServiceUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	private SettingItemView settingItemView = null;
	private SharedPreferences sharedPreferences = null;
	private SettingItemView settingShowAdress = null;
	private Intent showAddressService = null;
	private Intent showSmsSafeServiceIntent = null;
	private SettingClickView settingClickView = null;
	private SettingItemView app_lock = null;
	private SettingItemView smsSafe = null;
	private Intent watchDog;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		settingItemView = (SettingItemView) findViewById(R.id.SettingItemView);
		settingShowAdress = (SettingItemView) findViewById(R.id.show_address);
		settingClickView = (SettingClickView) findViewById(R.id.setting_window);
				app_lock = (SettingItemView) findViewById(R.id.app_lock);
		smsSafe = (SettingItemView) findViewById(R.id.sms_safe);
		Boolean updateBoolean = sharedPreferences.getBoolean("update", false);
		if (updateBoolean) {
			settingItemView.setChecked(true);
		} else {
			settingItemView.setChecked(false);
		}
		settingItemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sharedPreferences.edit();
				// TODO Auto-generated method stub
				if (settingItemView.isCheck()) {
					settingItemView.setChecked(false);
					editor.putBoolean("update", false);
				} else {
					settingItemView.setChecked(true);
					editor.putBoolean("update", true);

				}
				editor.commit();
			}
		});
		showAddressService = new Intent(this, ShowAddressService.class);
		boolean serviceBoolean = ServiceUtils.isServiceRunning(
				SettingActivity.this, "service.ShowAddressService");
		if (serviceBoolean) {
			settingShowAdress.setChecked(true);
		} else {
			settingShowAdress.setChecked(false);

		}
		settingShowAdress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (settingShowAdress.isCheck()) {
					settingShowAdress.setChecked(false);
					stopService(showAddressService);
				} else {
					settingShowAdress.setChecked(true);
					startService(showAddressService);
				}

			}
		});
		watchDog = new Intent(this, WatchDog.class);
		boolean watchDogBoolean = ServiceUtils.isServiceRunning(
				SettingActivity.this, "service.WatchDog");
		if (watchDogBoolean) {
			app_lock.setChecked(true);
		} else {
			app_lock.setChecked(false);

		}
		app_lock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (app_lock.isCheck()) {
					app_lock.setChecked(false);
					stopService(watchDog);
				} else {
					app_lock.setChecked(true);
					startService(watchDog);
				}

			}
		});

		showSmsSafeServiceIntent = new Intent(this, CallSmsSafeService.class);
		boolean smsSafeBoolean = ServiceUtils.isServiceRunning(
				SettingActivity.this, "service.CallSmsSafeService");
		if (smsSafeBoolean) {
			smsSafe.setChecked(true);
		} else {
			smsSafe.setChecked(false);
		}
		smsSafe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (smsSafe.isCheck()) {
					smsSafe.setChecked(false);
					stopService(showSmsSafeServiceIntent);
				} else {
					smsSafe.setChecked(true);
					startService(showSmsSafeServiceIntent);
				}
			}
		});
		final String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
		final int[] ids = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int whichNumber = sharedPreferences.getInt("which", 0);
		settingClickView.setTextdesc(whichNumber);
		settingClickView.setBackgroundResource(ids[whichNumber]);
		settingClickView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int which = sharedPreferences.getInt("which", 0);

				AlertDialog.Builder builder = new Builder(SettingActivity.this);

				builder.setTitle("归属地提示框风格");
				builder.setSingleChoiceItems(items, which,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								settingClickView
										.setBackgroundResource(ids[which]);
								Editor editor = sharedPreferences.edit();
								editor.putInt("which", which);
								settingClickView.setTextdesc(which);
								editor.commit();
								dialog.dismiss();

							}
						});
				builder.setNegativeButton("cancel", null);
				builder.show();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		boolean serviceBoolean = ServiceUtils.isServiceRunning(
				SettingActivity.this, "service.ShowAddressService");
		if (serviceBoolean) {
			settingShowAdress.setChecked(true);
		} else {
			settingShowAdress.setChecked(false);

		}
		boolean smsSafeBoolean = ServiceUtils.isServiceRunning(
				SettingActivity.this, "service.CallSmsSafeService");
		if (smsSafeBoolean) {
			smsSafe.setChecked(true);
		} else {
			smsSafe.setChecked(false);
		}
		boolean watchDogBoolean = ServiceUtils.isServiceRunning(
				SettingActivity.this, "service.WatchDog");
		if (watchDogBoolean) {
			app_lock.setChecked(true);
		} else {
			app_lock.setChecked(false);

		}

	}
}
