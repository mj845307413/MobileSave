package service;

import java.util.ArrayList;
import java.util.List;

import com.example.mj_mobileserver.EnterPWDActivity;

import db.query.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class WatchDog extends Service {
	private ActivityManager activityManager;
	private boolean flag;
	private AppLockDao appLockDao;
	private innerReceiver receiver;
	private LockScreen lockScreen;
	private List<String> tempstoppackname;
	private boolean flag1;
	private List<String> protectList;
	private DataChangeReceiver dataChangeReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class LockScreen extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			tempstoppackname = null;
			System.out.println("清空tempstop");
		}

	}

	private class innerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			tempstoppackname.add(intent.getStringExtra("packname"));
		}

	}

	private class DataChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("数据库的内容变化了。。。");
			protectList = appLockDao.findall();
		}
	}

	@Override
	public void onCreate() {
		tempstoppackname = new ArrayList<String>();
		appLockDao = new AppLockDao(this);
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		receiver = new innerReceiver();
		registerReceiver(receiver, new IntentFilter("stop_watch_dog"));
		lockScreen = new LockScreen();
		dataChangeReceiver = new DataChangeReceiver();
		registerReceiver(dataChangeReceiver, new IntentFilter("applockchange"));
		registerReceiver(lockScreen, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		protectList = appLockDao.findall();
		System.out.println("watchDog开始运行了");
		final Intent intent = new Intent(WatchDog.this, EnterPWDActivity.class);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		flag = true;
		flag1 = false;
		new Thread() {
			public void run() {
				while (flag) {
					List<RunningTaskInfo> infos = activityManager
							.getRunningTasks(1);
					String packnameString = infos.get(0).topActivity
							.getPackageName();

					flag1 = false;
					if (protectList.contains(packnameString)) {
						for (String nameString : tempstoppackname) {
							if (packnameString.equals(nameString)) {
								flag1 = true;
							}
						}
						if (flag1) {

						} else {
							intent.putExtra("packname", packnameString);

							startActivity(intent);
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		flag = false;
		unregisterReceiver(receiver);
		unregisterReceiver(lockScreen);
		receiver = null;
		lockScreen = null;
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
