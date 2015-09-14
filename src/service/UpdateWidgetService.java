package service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import receiver.MyWidget;
import utils.SystemInfoUtils;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.mj_mobileserver.R;

public class UpdateWidgetService extends Service {
	private ScreenOffReceiver offreceiver;
	private ScreenOnReceiver onreceiver;
	private ActivityManager activityManager;

	private class ScreenOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Timerisnull();
		}
	}

	private class ScreenOnReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			startWidget();
		}
	}

	protected static final String Tag = "UpdateWidgetService";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;

	}

	private AppWidgetManager appWidgetManager;
	private Timer timer;
	private TimerTask timerTask;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		offreceiver = new ScreenOffReceiver();
		onreceiver = new ScreenOnReceiver();
		registerReceiver(offreceiver,
				new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(onreceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		appWidgetManager = AppWidgetManager.getInstance(this);
		startWidget();
	}

	private void startWidget() {
		if (timer == null && timerTask == null) {

			timer = new Timer();
			timerTask = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ComponentName provider = new ComponentName(
							UpdateWidgetService.this, MyWidget.class);
					RemoteViews remoteViews = new RemoteViews(getPackageName(),
							R.layout.process_widget);
					remoteViews
							.setTextViewText(
									R.id.process_count,
									"正在运行的程序："
											+ SystemInfoUtils
													.getRunningProcessNumber(getApplicationContext())
											+ "个");
					remoteViews
							.setTextViewText(
									R.id.process_memory,
									"可用的内存为"
											+ Formatter
													.formatFileSize(
															getApplicationContext(),
															SystemInfoUtils
																	.getAvaiableMemory(getApplicationContext()))
											+ "个");
					Intent intent = new Intent();
					intent.setAction("killall_process");
					PendingIntent pendingIntent = PendingIntent.getBroadcast(
							getApplicationContext(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					remoteViews.setOnClickPendingIntent(R.id.btn_clear,
							pendingIntent);
					appWidgetManager.updateAppWidget(provider, remoteViews);
					System.out.println("mj_更新widget");
				}
			};
			timer.schedule(timerTask, 0, 3000);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(onreceiver);
		unregisterReceiver(offreceiver);
		onreceiver = null;
		offreceiver = null;
		Timerisnull();
	}

	private void Timerisnull() {
		if (timer != null && timerTask != null) {
			timer.cancel();
			timerTask.cancel();
			timer = null;
			timerTask = null;
		}
	}

}
