package com.example.mj_mobileserver;

import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;

public class TrafficManagerActivity extends Activity {
	private PackageManager packageManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_manager);
		packageManager = getPackageManager();
		List<ApplicationInfo> applicationInfos = packageManager
				.getInstalledApplications(0);
		for (ApplicationInfo applicationInfo : applicationInfos) {
			int uid = applicationInfo.uid;
			long tx = TrafficStats.getUidTxBytes(uid);
			long rx = TrafficStats.getUidRxBytes(uid);
		}
		long total_rx = TrafficStats.getTotalRxBytes();
		long total_tx = TrafficStats.getTotalTxBytes();
		long mobile_tx = TrafficStats.getMobileTxBytes();
		long mobile_rx = TrafficStats.getMobileRxBytes();
	}
}
