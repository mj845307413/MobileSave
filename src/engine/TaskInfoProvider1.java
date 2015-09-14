package engine;

import java.util.ArrayList;
import java.util.List;

import com.example.mj_mobileserver.R;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import db.domain.TaskInfo;

public class TaskInfoProvider1 {
	public static List<TaskInfo> getTaskInfos(Context context) {
		List<TaskInfo> infos = new ArrayList<TaskInfo>();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcessInfos = activityManager
				.getRunningAppProcesses();
		PackageManager packageManager = context.getPackageManager();
		for (RunningAppProcessInfo runningAppProcessInfo : appProcessInfos) {
			TaskInfo taskInfo = new TaskInfo();
			String packName = runningAppProcessInfo.processName;
			taskInfo.setPackagename(packName);
			MemoryInfo[] memoryInfo = activityManager
					.getProcessMemoryInfo(new int[] { runningAppProcessInfo.pid });
			long memsize = memoryInfo[0].getTotalPrivateDirty() * 1024l;
			taskInfo.setMemsize(memsize);
			ApplicationInfo applicationInfo;
			try {
				applicationInfo = packageManager
						.getApplicationInfo(packName, 0);
				Drawable drawable = applicationInfo.loadIcon(packageManager);
				String nameString = applicationInfo.loadLabel(packageManager)
						.toString();
				taskInfo.setIconDrawable(drawable);
				taskInfo.setName(nameString);
				if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					taskInfo.setUserTask(true);
				} else {
					taskInfo.setUserTask(false);
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				taskInfo.setIconDrawable(context.getResources().getDrawable(R.drawable.lock));
				taskInfo.setName(packName);
			}
			System.out.println(taskInfo.toString());
			infos.add(taskInfo);
		}
		return infos;

	}
}
