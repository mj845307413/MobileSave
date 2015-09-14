package com.example.mj_mobileserver;

import utils.MD5utils;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPWDActivity extends Activity {
	private EditText editText;
	private SharedPreferences sharedPreferences;
	private String packname;
	private PackageManager packageManager;
	private TextView tv_name;
	private ImageView iv_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pwd);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		editText = (EditText) findViewById(R.id.et_password);
		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		Intent intent = getIntent();
		packname = intent.getStringExtra("packname");
		packageManager = getPackageManager();
		ApplicationInfo applicationInfo;
		try {
			applicationInfo = packageManager.getApplicationInfo(packname, 0);
			iv_icon.setImageDrawable(applicationInfo.loadIcon(packageManager));
			tv_name.setText(applicationInfo.loadLabel(packageManager));
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void onBackPressed() {
		//回桌面。
//        <action android:name="android.intent.action.MAIN" />
//        <category android:name="android.intent.category.HOME" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <category android:name="android.intent.category.MONKEY"/>
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
		//所有的activity最小化 不会执行ondestory 只执行 onstop方法。
		
	}
@Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	finish();
}
	public void click(View view) {
		String pswString = editText.getText().toString().trim();
		// String MD5String = MD5utils.MD5Password(pswString);
		//
		// String password = sharedPreferences.getString("password", null);
		if (TextUtils.isEmpty(pswString)) {
			Toast.makeText(EnterPWDActivity.this, "你他妈倒是输密码啊", 1).show();
			return;
		}
		if (pswString.endsWith("123")) {
			Intent intent = new Intent();
			intent.setAction("stop_watch_dog");
			intent.putExtra("packname", packname);
			sendBroadcast(intent);
			finish();
			return;
		} else {
			Toast.makeText(EnterPWDActivity.this, "密码输错了", 1).show();
			return;
		}
	}
}
