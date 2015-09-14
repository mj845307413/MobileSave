package com.example.mj_mobileserver;

import utils.MD5utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private String tag = "HomeActivity";
	private GridView gridView = null;
	private MyAdapter adapter = null;
	private SharedPreferences sharedPreferences = null;
	private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
			"手机杀毒", "缓存清理", "高级工具", "设置中心"

	};
	private static int[] ids = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		adapter = new MyAdapter();
		GridView gridView = (GridView) findViewById(R.id.list_home);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final Intent intent;
				switch (position) {
				case 0:
					showLostDialog();
					break;
				case 1:
					intent = new Intent(HomeActivity.this,
							CallMSNActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(HomeActivity.this,
							AppManagerActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(HomeActivity.this,
							TaskManagerActivity.class);
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(HomeActivity.this,
							TrafficManagerActivity.class);
					startActivity(intent);
					break;
				case 5:
					intent = new Intent(HomeActivity.this,
							AntiVirusActivity.class);
					startActivity(intent);
					break;
				case 6:
					intent = new Intent(HomeActivity.this,
							CleanCacheActivity.class);
					startActivity(intent);
					break;
				case 8:
					intent = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;

				case 7:
					intent = new Intent(HomeActivity.this, AToolActivity.class);
					startActivity(intent);

				default:
					break;
				}
			}
		});
	}

	protected void showLostDialog() {
		// TODO Auto-generated method stub
		if (isSetupPwd()) {
			showEnterDialog();
		} else {

			showSetupPassword();
		}
	}

	private Button setupOKButton = null;
	private Button setupConcelButton = null;
	private EditText passwordEditText = null;
	private EditText confirmEditText = null;
	private AlertDialog alertDialog = null;

	private void showSetupPassword() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_setup_password, null);
		passwordEditText = (EditText) view.findViewById(R.id.et_setup_pwd);
		confirmEditText = (EditText) view.findViewById(R.id.et_setip_confirm);
		setupOKButton = (Button) view.findViewById(R.id.ok);
		setupConcelButton = (Button) view.findViewById(R.id.cancel);
		setupOKButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = passwordEditText.getText().toString().trim();
				String confirm = confirmEditText.getText().toString().trim();
				if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
					Toast.makeText(HomeActivity.this, "密码不能为空", 2).show();
				} else {
					if (password.equals(confirm)) {
						Editor editor = sharedPreferences.edit();
						String MD5String = MD5utils.MD5Password(password);
						editor.putString("password", MD5String);
						editor.commit();
						alertDialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(HomeActivity.this,
								LostFindActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(HomeActivity.this, "两次输入不一样", 2);
						passwordEditText.setText("");
						confirmEditText.setText("");
					}
				}

			}
		});
		setupConcelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		// builder.setView(view);

		alertDialog = builder.create();
		alertDialog.setView(view, 0, 0, 0, 0);
		alertDialog.show();
	}

	private void showEnterDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_enter_password, null);
		passwordEditText = (EditText) view.findViewById(R.id.et_setup_pwd);
		setupOKButton = (Button) view.findViewById(R.id.ok);
		setupConcelButton = (Button) view.findViewById(R.id.cancel);
		setupOKButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = passwordEditText.getText().toString().trim();
				String passwordString = sharedPreferences.getString("password",
						null);
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this, "密码不能为空", 2).show();
					return;
				} else {
					String MD5String = MD5utils.MD5Password(password);
					if (MD5String.equals(passwordString)) {
						alertDialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(HomeActivity.this,
								LostFindActivity.class);
						startActivity(intent);

					} else {
						Toast.makeText(HomeActivity.this, "密码输入错误", 2).show();
						passwordEditText.setText("");

					}
				}

			}
		});
		setupConcelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		// builder.setView(view);
		alertDialog = builder.create();
		alertDialog.setView(view, 0, 0, 0, 0);
		alertDialog.show();
	}

	private boolean isSetupPwd() {
		String password = sharedPreferences.getString("password", null);
		if (TextUtils.isEmpty(password)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * home
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int position) {

			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(HomeActivity.this, R.layout.list_home,
					null);
			ImageView imageView = (ImageView) view.findViewById(R.id.app_item1);
			TextView textView = (TextView) view.findViewById(R.id.app_item2);
			textView.setText(names[position]);
			imageView.setImageResource(ids[position]);
			return view;
		}

	}

}
