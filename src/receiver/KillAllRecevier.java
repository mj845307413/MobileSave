package receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillAllRecevier extends BroadcastReceiver {
	private ActivityManager activityManager;
	private List<RunningAppProcessInfo> infos;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		activityManager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		infos = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo info : infos) {
			activityManager.killBackgroundProcesses(info.processName);
			System.out.println(info.processName + "±ª«Â¿Ì");
		}
	}

}
