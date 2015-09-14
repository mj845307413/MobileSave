package com.example.mj_mobileserver;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CleanCacheActivity extends Activity {
	private ProgressBar pb;
	private TextView tv_scan_status;
	private PackageManager pm;
	private LinearLayout ll_container;
	private List<PackageInfo> infos;
	private int i= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		pb = (ProgressBar) findViewById(R.id.pb);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		scanCache();
	}

	private void scanCache() {
		// TODO Auto-generated method stub
		pm = getPackageManager();
		new Thread() {
			public void run() {
				Method getPackageSizeInfoMethod = null;
				Method[] methods = PackageManager.class.getMethods();
				for (Method method : methods) {
					if ("getPackageSizeInfo".equals(method.getName())) {
						getPackageSizeInfoMethod = method;
					}
				}
				tv_scan_status.setText("’˝‘⁄…®√Ë");
				infos = pm.getInstalledPackages(0);
				pb.setMax(infos.size());
				pb.setProgress(i);
				for (PackageInfo info : infos) {
					try {
						getPackageSizeInfoMethod.invoke(pm, info.packageName,
								new MyDataObserver());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private class MyDataObserver extends IPackageStatsObserver.Stub {

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			// TODO Auto-generated method stub
			final long cache = pStats.cacheSize;
			long code = pStats.codeSize;
			long data = pStats.dataSize;
			final String packnameString = pStats.packageName;
				runOnUiThread(new Runnable() {
					
					public void run() {
						tv_scan_status.setText(packnameString);
						TextView tv_cache = new TextView(
								getApplicationContext());

						String string="≥Ã–Ú£∫"
								+ packnameString
								+ "   ª∫¥Ê£∫"
								+ Formatter.formatFileSize(
										getApplicationContext(), cache);
						tv_cache.setText(string);
						System.out.println(tv_cache.getText().toString());
						ll_container.addView(tv_cache, 0);
					}
				});
			i++;
			pb.setProgress(i);
			if (i == infos.size()) {
				runOnUiThread(new Runnable() {
					public void run() {
						tv_scan_status.setText("…®√ËÕ£÷π");
					}
				});

			}
		}

	}

	public void clearAll(View view) {
		System.out.println("clean all");
	}
}
