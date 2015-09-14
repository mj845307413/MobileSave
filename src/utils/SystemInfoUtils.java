package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class SystemInfoUtils {
	public static int getRunningProcessNumber(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = activityManager
				.getRunningAppProcesses();
		return infos.size();

	}
	public static long getAvaiableMemory(Context context) {
		ActivityManager activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo=new MemoryInfo();
		activityManager.getMemoryInfo(outInfo);
		return outInfo.availMem;
		
	}
	public static long getAllMemory(Context context) {
//		ActivityManager activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo=new MemoryInfo();
//		activityManager.getMemoryInfo(outInfo);
//		return outInfo.totalMem;
		File file=new File("/proc/meminfo/");
		try {
			FileInputStream fileInputStream=new FileInputStream(file);
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream));
			String lineString=bufferedReader.readLine();
			StringBuilder builder=new StringBuilder();
			for (char c:lineString.toCharArray()) {
				if (c>='0'&&c<='9') {
					builder.append(c);
				}
			}
			return Long.parseLong(builder.toString())*1024;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	
	}

}
