package com.example.mj_mobileserver;

import java.util.ArrayList;
import java.util.List;

import utils.DensityUtil;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import db.domain.APPInfo;
import db.query.AppLockDao;
import engine.AppInfoProvider;

public class AppManagerActivity extends Activity implements OnClickListener {
	private static final String Tag = "AppManagerActivity";
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	private ListView lv_app_manager;
	private LinearLayout ll_loading;
	private List<APPInfo> appInfos;
	private APPManegerAdapter adapter;
	private List<APPInfo> userAppInfos;
	private List<APPInfo> systemAppInfos;
	private TextView tv_status;
	private PopupWindow popupWindow;
	private LinearLayout ll_uninstall;
	private LinearLayout ll_start;
	private LinearLayout ll_share;
	private APPInfo appInfo_globle;
	private AppLockDao appLockDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		appLockDao = new AppLockDao(this);
		tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		tv_status = (TextView) findViewById(R.id.tv_status);
		long sdAvialableSize = getAvailableSpace(Environment
				.getExternalStorageDirectory().getAbsolutePath());
		long romAvialableSize = getAvailableSpace(Environment
				.getDataDirectory().getAbsolutePath());
		tv_avail_rom.setText("内存可用空间："
				+ Formatter.formatFileSize(this, romAvialableSize));
		tv_avail_sd.setText("sd卡可用空间："
				+ Formatter.formatFileSize(this, sdAvialableSize));
		filldata();
		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					return;
				} else if (position == userAppInfos.size() + 1) {
					return;
				} else if (position <= userAppInfos.size()) {
					int newposition = position - 1;
					appInfo_globle = userAppInfos.get(newposition);

				} else {
					int newposition = position - 1 - userAppInfos.size() - 1;

					appInfo_globle = systemAppInfos.get(newposition);
				}
				dismissPopupWindow();
				View contentView = View.inflate(getApplicationContext(),
						R.layout.popup_app_item, null);
				ll_uninstall = (LinearLayout) contentView
						.findViewById(R.id.ll_uninstall);// 注意了是在view内部的
				ll_start = (LinearLayout) contentView
						.findViewById(R.id.ll_start);
				ll_share = (LinearLayout) contentView
						.findViewById(R.id.ll_share);
				// ll_uninstall.setOnClickListener(new UninstallLintener());
				// ll_start.setOnClickListener(new StartLintener() );
				// ll_share.setOnClickListener(new ShareLintener());
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				ll_start.setOnClickListener(AppManagerActivity.this);
				ll_share.setOnClickListener(AppManagerActivity.this);
				popupWindow = new PopupWindow(contentView, -2, -2);// -2表示的是wrap，而-1则是填充父窗体
				// popupWindow.setBackgroundDrawable(new
				// ColorDrawable(R.drawable.call_locate_gray));
				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int dip = 60;
				int px = DensityUtil.dip2px(getApplicationContext(), dip);
				System.out.println("px=" + px);
				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,
						px, location[1]);
				System.out.println("location[1]=" + location[1]);
				AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1f);
				alphaAnimation.setDuration(1000);
				ScaleAnimation scaleAnimation = new ScaleAnimation(0.3f, 1f,
						0.3f, 1f, Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				scaleAnimation.setDuration(1000);
				AnimationSet animationSet = new AnimationSet(false);
				animationSet.addAnimation(alphaAnimation);
				animationSet.addAnimation(scaleAnimation);
				contentView.startAnimation(animationSet);
			}
		});
		lv_app_manager
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0) {
							return true;
						} else if (position == userAppInfos.size() + 1) {
							return true;
						} else if (position <= userAppInfos.size()) {
							int newposition = position - 1;
							appInfo_globle = userAppInfos.get(newposition);

						} else {
							int newposition = position - 1
									- userAppInfos.size() - 1;

							appInfo_globle = systemAppInfos.get(newposition);
						}
						ViewHolder holder = (ViewHolder) view.getTag();
						System.out.println("长点机了。。。。。。。");
						if (appLockDao.find(appInfo_globle.getPackageString())) {
							appLockDao.delete(appInfo_globle.getPackageString());
							holder.iv_status
									.setImageResource(R.drawable.unlock);
						} else {
							appLockDao.add(appInfo_globle.getPackageString());
							holder.iv_status.setImageResource(R.drawable.lock);

						}
						return true;
					}
				});
		lv_app_manager.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopupWindow();
				// TODO Auto-generated method stub
				if (userAppInfos != null || systemAppInfos != null) {

					if (firstVisibleItem > userAppInfos.size()) {
						tv_status.setText("系统程序：" + systemAppInfos.size() + "个");
					} else {
						tv_status.setText("用户程序：" + userAppInfos.size() + "个");
					}
				}
			}
		});
	}

	private void filldata() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				appInfos = AppInfoProvider.getAppInfos(getApplicationContext());
				userAppInfos = new ArrayList<APPInfo>();
				systemAppInfos = new ArrayList<APPInfo>();
				for (APPInfo appInfo : appInfos) {
					if (appInfo.isUserApp()) {
						userAppInfos.add(appInfo);
					} else {
						systemAppInfos.add(appInfo);
					}
				}
				System.out.println(appInfos.size());
				runOnUiThread(new Runnable() {
					public void run() {
						if (adapter == null) {
							adapter = new APPManegerAdapter();
							lv_app_manager.setAdapter(adapter);

						} else {
							adapter.notifyDataSetChanged();
						}
						ll_loading.setVisibility(View.INVISIBLE);

					}
				});
			}
		}.start();
	}

	private class APPManegerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userAppInfos.size() + systemAppInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View view;
			ViewHolder holder;
			APPInfo appInfo = new APPInfo();
			// 1.减少内存中view对象创建的个数
			if (position == 0) {
				TextView textView = new TextView(getApplicationContext());
				textView.setText("用户程序" + userAppInfos.size());
				return textView;
			} else if (position == (userAppInfos.size() + 1)) {
				TextView textView = new TextView(getApplicationContext());
				textView.setText("系统应用程序" + systemAppInfos.size());
				return textView;
			} else if (position <= userAppInfos.size()) {
				appInfo = userAppInfos.get(position - 1);

			} else {
				appInfo = systemAppInfos
						.get(position - userAppInfos.size() - 2);
			}
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();// 5%

			} else {
				// 把一个布局文件转化成 view对象。
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_appinfo, null);
				// 2.减少子孩子查询的次数 内存中对象的地址。
				holder = new ViewHolder();
				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				holder.tv_app_location = (TextView) view
						.findViewById(R.id.tv_app_location);
				holder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.iv_status = (ImageView) view
						.findViewById(R.id.iv_status);
				// 当孩子生出来的时候找到他们的引用，存放在记事本，放在父亲的口袋
				view.setTag(holder);

			}
			if (appInfo.isInRom()) {
				holder.tv_app_location.setText("手机内存" + "  uid"
						+ appInfo.getUid());
			} else {
				holder.tv_app_location.setText("外部内存" + "  uid"
						+ appInfo.getUid());
			}
			if (appLockDao.find(appInfo.getPackageString())) {
				holder.iv_status.setImageResource(R.drawable.lock);
			} else {
				holder.iv_status.setImageResource(R.drawable.unlock);

			}
			holder.tv_app_name.setText(appInfo.getName());
			holder.iv_app_icon.setImageDrawable(appInfo.getIconDrawable());
			return view;
		}

	}

	static class ViewHolder {
		TextView tv_app_name;
		TextView tv_app_location;
		ImageView iv_app_icon;
		ImageView iv_status;
	}

	private long getAvailableSpace(String path) {
		StatFs statFs = new StatFs(path);
		long number = statFs.getAvailableBlocks();
		long size = statFs.getBlockSize();
		return number * size;

	}

	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dismissPopupWindow();
		super.onDestroy();
	}

	/**
	 * 弹出窗体添加的点击事件 屌的不行
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dismissPopupWindow();
		switch (v.getId()) {
		case R.id.ll_uninstall:
			if (appInfo_globle.isUserApp()) {
				Log.i(Tag, "卸载" + appInfo_globle.getPackageString());
				uninstall();

			} else {
				Toast.makeText(AppManagerActivity.this, "系统程序不能被卸载", 1);
			}

			break;

		case R.id.ll_start:
			Log.i(Tag, "启动" + appInfo_globle.getPackageString());
			startApplication();
			break;
		case R.id.ll_share:
			Log.i(Tag, "分享" + appInfo_globle.getPackageString());
			shareAppliacation();
			break;

		default:
			break;
		}
	}

	private void shareAppliacation() {
		// TODO Auto-generated method stub
		// Intent { act=android.intent.action.SEND typ=text/plain flg=0x3000000
		// cmp=com.android.mms/.ui.ComposeMessageActivity (has extras) } from
		// pid 256
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		// intent.addFlags(0x3000000);
		intent.putExtra(Intent.EXTRA_TEXT, "骏哥牌手机卫士，啊哈哈哈哈哈哈，这个软件叫"
				+ appInfo_globle.getName());
		startActivity(intent);
	}

	private void uninstall() {
		// TODO Auto-generated method stub
		// <action android:name="android.intent.action.VIEW" />
		// <action android:name="android.intent.action.DELETE" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <data android:scheme="package" />
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + appInfo_globle.getPackageString()));
		startActivityForResult(intent, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		filldata();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startApplication() {
		// TODO Auto-generated method stub
		PackageManager packageManager = getPackageManager();
		// Intent intent=new Intent();
		// intent.setAction("android.intent.action.MAIN");
		// intent.addCategory("android.intent.category.LAUNCHER");
		// List<ResolveInfo> infos=packageManager.queryIntentActivities(intent,
		// PackageManager.GET_INTENT_FILTERS);
		Intent intent = packageManager.getLaunchIntentForPackage(appInfo_globle
				.getPackageString());
		if (intent != null) {
			startActivity(intent);
		} else {
			Toast.makeText(AppManagerActivity.this, "卧槽，程序打不开。。。。", 1).show();
		}
	}

}
