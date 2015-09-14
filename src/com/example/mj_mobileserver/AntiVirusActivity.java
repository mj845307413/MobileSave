package com.example.mj_mobileserver;

import java.util.List;

import utils.FileMD5Utils;
import android.R.color;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AntiVirusActivity extends Activity {
	private ImageView iv_scan;
	private static String path = "data/data/com.example.mj_mobileserver/files/antivirus.db";
	private PackageManager packageManager;
	private ProgressBar progressBar;
	private TextView tv_scan_status;
	private LinearLayout ll_container;
	private String string;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		tv_scan_status = (TextView) findViewById(R.id.virus_tv_scan_status);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		progressBar = (ProgressBar) findViewById(R.id.virus_progressBar1);
		ll_container=(LinearLayout) findViewById(R.id.virus_ll_container);
		RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(2000);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		iv_scan.setAnimation(rotateAnimation);
		for (int i = 0; i < 8; i++) {
			TextView child=new TextView(getApplicationContext());
			child.setText("...."+i);
			ll_container.addView(child);
		}
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				TextView textView=new TextView(getApplicationContext());
				switch (msg.arg1) {
				case 0:
					string=msg.obj.toString() + ":无病毒";
					tv_scan_status.setText(string);
					System.out.println(string);
					textView.setText(string);
					ll_container.addView(textView,0);
					break;
				case 1:
					string=msg.obj.toString() + ":有病毒";
					System.out.println(string);
					tv_scan_status.setText(string);
					textView.setText(string);
					ll_container.addView(textView,0);
					break;
				case 2:
					string="扫描完毕";
					iv_scan.clearAnimation();
					System.out.println(string);
					tv_scan_status.setText(string);
					textView.setText(string);
					ll_container.addView(textView,0);
					break;
				default:
					break;
				}

				super.handleMessage(msg);
			}
			
		};
		scanVirus();
	}

	private void scanVirus() {
		// TODO Auto-generated method stub
		tv_scan_status.setText("正在初始化8核杀毒引擎。。。");

		new Thread() {
			public void run() {
				packageManager = getPackageManager();
				List<ApplicationInfo> applicationInfos = packageManager
						.getInstalledApplications(0);
				progressBar.setMax(applicationInfos.size());
				int i = 0;
				int flag = 0;
				progressBar.setProgress(i);
				SQLiteDatabase database = SQLiteDatabase.openDatabase(path,
						null, SQLiteDatabase.OPEN_READONLY);

				for (ApplicationInfo applicationInfo : applicationInfos) {
					flag = 0;
					String fileSource = applicationInfo.sourceDir;
					String fileMD5 = FileMD5Utils.getFileMd5(fileSource);
					Cursor cursor = database.rawQuery(
							"select desc from datable where md5= ?",
							new String[] { fileMD5 });
					while (cursor.moveToNext()) {
						String filedesc = cursor.getString(0);
						flag = 1;
					}
					Message msg = Message.obtain();
					msg.obj = applicationInfo.loadLabel(packageManager)
							.toString();
					msg.arg1 = flag;
					handler.sendMessage(msg);
					cursor.close();
					i++;
					progressBar.setProgress(i);
				}
				database.close();
				Message msg =Message.obtain();
				
				msg.arg1 = 2;
				handler.sendMessage(msg);

			};
			
		}.start();

	}
}
