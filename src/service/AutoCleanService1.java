package service;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoCleanService1 extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private ActivityManager activityManager;
	private cleanRecever recever;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("cb_clean_change2");
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		recever = new cleanRecever();
		registerReceiver(recever, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(recever);
		recever = null;
		super.onDestroy();
	}

	private class cleanRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			List<RunningAppProcessInfo> appProcessInfos = activityManager
					.getRunningAppProcesses();
			System.out.println("cb_clean_change3");
			for (RunningAppProcessInfo runningAppProcessInfo : appProcessInfos) {
				activityManager
						.killBackgroundProcesses(runningAppProcessInfo.processName);
				System.out.println(runningAppProcessInfo.processName + "±ª«Â¿Ì");
			}
		}

	}
}
