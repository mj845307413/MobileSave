package com.example.mj_mobileserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.stream.JsonReader;

import utils.StreamTools;

import android.support.v7.app.ActionBarActivity;
import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends ActionBarActivity {
	TextView textView = null;
	TextView textViewprograss = null;
	protected static final String tag = "SplashActivity";
	private static final int ENTER_HOME = 0;
	private static final int SHOW_UPDATE_DIALOGE = 1;
	private static final int JSON_ERROR = 2;
	private static final int NETWORK_ERROR = 3;
	private static final int URL_ERROR = 4;
	private String descriptionString;
	private String versionString;
	private String apkurlString;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		textView = (TextView) findViewById(R.id.textView1);
		textView.setText(getVersionName());
		copyDB();
		copyAntiVirusDB();
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		installShortCut();

		boolean update = sharedPreferences.getBoolean("update", false);
		
		if (update) {
			checkUpdate();
		} else {
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					enterHome();
				}
			}, 2000);
		}
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
		alphaAnimation.setDuration(1000);
		findViewById(R.id.splash).startAnimation(alphaAnimation);
		textViewprograss = (TextView) findViewById(R.id.prograssBarView);
	}

	private void installShortCut() {
		// TODO Auto-generated method stub
		boolean shortCut = sharedPreferences.getBoolean("shortcur", false);
		if (shortCut) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "骏哥小卫士");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(getResources(), R.drawable.ic_launcher));
		Intent shortCutIntent = new Intent();
		shortCutIntent.setAction("android.intent.action.MAIN");
		shortCutIntent.addCategory("android.intent.category.LAUNCHER");
		shortCutIntent.setClassName(getPackageName(),
				"com.example.mj_mobileserver.SplashActivity ");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("shortcut", true);
		editor.commit();
		sendBroadcast(intent);
	}
	private void copyAntiVirusDB() {
		try {

			File file = new File(getFilesDir(), "antivirus.db");
			if (file.exists() && file.length() > 0) {
				Log.i(tag, "antivirus有了，不拷贝");

			} else {
				InputStream inputStream = getAssets().open("antivirus.db");
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				byte[] bs = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(bs)) != -1) {
					fileOutputStream.write(bs, 0, len);
				}
				inputStream.close();
				fileOutputStream.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void copyDB() {
		try {

			File file = new File(getFilesDir(), "address.db");
			if (file.exists() && file.length() > 0) {
				Log.i(tag, "address有了，不拷贝");

			} else {
				InputStream inputStream = getAssets().open("address.db");
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				byte[] bs = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(bs)) != -1) {
					fileOutputStream.write(bs, 0, len);
				}
				inputStream.close();
				fileOutputStream.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_UPDATE_DIALOGE:
				showUpdateDialog();
				break;

			case ENTER_HOME:
				enterHome("ENTER_HOME");
				break;
			case URL_ERROR:
				enterHome("URL_ERROR");
				break;
			case JSON_ERROR:
				enterHome("JSON_ERROR");
				break;
			case NETWORK_ERROR:
				enterHome("NETWORK_ERROR");
				break;
			default:
				break;
			}
		}

	};

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("升级");
		builder.setMessage("升级啦，么么哒");
		// builder.setCancelable(false);
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				enterHome();
			}
		});
		builder.setPositiveButton("yes", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// afnal
				FinalHttp finalHttp = new FinalHttp();
				finalHttp.download(apkurlString, Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/mj_mobileSafe", new AjaxCallBack<File>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						t.printStackTrace();
						Toast.makeText(getApplicationContext(), "下载失败", 1)
								.show();
						super.onFailure(t, errorNo, strMsg);
					}

					@Override
					public void onLoading(long count, long current) {
						// TODO Auto-generated method stub
						int prograss = (int) (current * 100 / count);
						textViewprograss.setVisibility(View.VISIBLE);
						textViewprograss.setText(prograss);
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(File t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						installAPK(t);
					}

					private void installAPK(File t) {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setDataAndType(Uri.fromFile(t),
								"application/vnd.android.package-archive");

						startActivity(intent);
					}

				});
			}
		});
		builder.setNegativeButton("no", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				enterHome();
			}
		});
	}

	private void enterHome(String string) {
		Toast toast = Toast.makeText(SplashActivity.this, string, 0);
		toast.show();
		final Intent intent = new Intent();
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				intent.setClass(SplashActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		};
		timer.schedule(timerTask, 1000);
	}

	private void enterHome() {
		final Intent intent = new Intent();
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				intent.setClass(SplashActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		};
		timer.schedule(timerTask, 1000);
	}

	/**
	 * 检查版本更新
	 */
	private void checkUpdate() {
		System.out.println("36");
		Thread thread = new Thread(checkUpdateThread());
		thread.start();
		System.out.println("39");
	}

	private Runnable checkUpdateThread() {
		Message message = Message.obtain();
		try {
			URL url = new URL(getString(R.string.serverurl));
			System.out.print(url);
			try {
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("GET");

				connection.setConnectTimeout(4000);
				int code = connection.getResponseCode();
				if (code == 200) {
					InputStream inputStream = connection.getInputStream();
					String string = StreamTools.readFromStream(inputStream);
					System.out.println(string);
					// json解析
					try {
						/*
						 * JSONObject jsonObject=new JSONObject(string);
						 * versionString=(String) jsonObject.get("version");
						 * descriptionString=(String)
						 * jsonObject.get("description");
						 * apkurlString=(String)jsonObject.get("apkurl");
						 * System.
						 * out.println(versionString+descriptionString+apkurlString
						 * );
						 */
						JsonReader reader = new JsonReader(new StringReader(
								string));
						reader.beginArray();
						while (reader.hasNext()) {
							reader.beginObject();
							while (reader.hasNext()) {
								String tagName = reader.nextName();
								if (tagName.equals("version")) {
									versionString = reader.nextString();
								} else if (tagName.equals("description")) {
									descriptionString = reader.nextString();

								} else {
									apkurlString = reader.nextString();

								}
							}
							reader.endObject();
						}
						reader.endArray();
						if (getVersionName().equals(versionString)) {

							message.what = ENTER_HOME;

						} else {
							message.what = SHOW_UPDATE_DIALOGE;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						message.what = JSON_ERROR;
					}
					// Log.i(tag, string);
					// System.out.print(string);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// System.out.println("有错误1");
				e.printStackTrace();
				message.what = NETWORK_ERROR;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.what = URL_ERROR;
			// System.out.println("有错误2");

		} finally {
			handler.sendMessage(message);

		}
		return null;

	}

	/**
	 * 得到程序版本名
	 */
	private String getVersionName() {
		try {
			PackageManager manager = getPackageManager();
			// manager.getPackageInfo("com.example.mj_mobileserver",0);
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
