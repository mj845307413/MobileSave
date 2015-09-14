package engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import db.domain.APPInfo;

public class AppInfoProvider {
	public static List<APPInfo> getAppInfos(Context context) {
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> packageInfos = manager.getInstalledPackages(0);
		List<APPInfo> appInfos = new ArrayList<APPInfo>();
		for (PackageInfo packageInfo : packageInfos) {
			APPInfo appInfo = new APPInfo();
			String packname = packageInfo.packageName;
			int uid = packageInfo.applicationInfo.uid;
			//File sendFile=new File("/proc/uid_stat/"+uid+"/tcp_snd");
			//File rcvFile=new File("/proc/uid_stat/"+uid+"/tcp_rcv");
			Drawable drawable = packageInfo.applicationInfo.loadIcon(manager);
			String nameString = (String) packageInfo.applicationInfo
					.loadLabel(manager);
			int flags = packageInfo.applicationInfo.flags;
			if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				appInfo.setUserApp(true);
			} else {
				appInfo.setUserApp(false);
			}
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				appInfo.setInRom(true);
			} else {
				appInfo.setInRom(false);
			}
			appInfo.setPackageString(packname);
			appInfo.setName(nameString);
			appInfo.setIconDrawable(drawable);
			appInfo.setUid(uid);
			appInfos.add(appInfo);
		}
		return appInfos;
	}
}
