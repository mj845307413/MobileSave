package utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
	public static boolean isServiceRunning(Context context, String serviceName) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = activityManager.getRunningServices(1000);
		for (RunningServiceInfo runningServiceInfo : infos) {
			String nameString = runningServiceInfo.service.getClassName();
			if (nameString.equals(serviceName)) {
				return true;
			}
		}
		return false;

	}
}
