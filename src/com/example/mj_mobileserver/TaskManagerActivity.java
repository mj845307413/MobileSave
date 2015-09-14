package com.example.mj_mobileserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import utils.SystemInfoUtils;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import db.domain.TaskInfo;
import engine.TaskInfoProvider1;

public class TaskManagerActivity extends Activity {
	private TextView tv_process_count;
	private TextView tv_mem_info;
	private LinearLayout ll_loading;
	private ListView lv_task_manager;
	private List<TaskInfo> infos;
	private AdapterListener adapter;
	private List<TaskInfo> userInfos;
	private List<TaskInfo> systemInfos;
	private TextView tv_status;
	private int processConut;
	private long aviamem;
	private long totalmem;
	private SharedPreferences sharedPreferences;
private Timer timer;
private TimerTask timerTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		changeProcessNumber();

		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
		tv_status = (TextView) findViewById(R.id.tv_status);
		fillData();
		lv_task_manager.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (userInfos != null || systemInfos != null) {

					if (firstVisibleItem > userInfos.size()) {
						tv_status.setText("系统程序：" + systemInfos.size() + "个");
					} else {
						tv_status.setText("用户程序：" + userInfos.size() + "个");
					}

				}
			}
		});
		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TaskInfo taskInfo = null;
				if (position == 0) {
					return;
				} else if (position == (userInfos.size() + 1)) {
					return;
				} else if (position <= userInfos.size()) {
					taskInfo = userInfos.get(position - 1);

				} else {
					taskInfo = systemInfos.get(position - userInfos.size() - 2);
				}
				if (getPackageName().equals(taskInfo.getPackagename())) {
					return;
				}
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				if (taskInfo.isChecked()) {
					taskInfo.setChecked(false);
					viewHolder.cb_status.setChecked(false);
				} else {
					taskInfo.setChecked(true);
					viewHolder.cb_status.setChecked(true);
				}
			}
		});
	}

	private void changeProcessNumber() {
		processConut = SystemInfoUtils.getRunningProcessNumber(this);
		tv_process_count.setText("运行中的进程数：" + processConut);
		aviamem = SystemInfoUtils.getAvaiableMemory(this);
		totalmem = SystemInfoUtils.getAllMemory(this);
		tv_mem_info.setText("剩余内存/总内存："
				+ Formatter.formatFileSize(this, aviamem) + "/"
				+ Formatter.formatFileSize(this, totalmem));
	}

	private void fillData() {
		// TODO Auto-generated method stub
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				infos = TaskInfoProvider1.getTaskInfos(getApplicationContext());
				userInfos = new ArrayList<TaskInfo>();
				systemInfos = new ArrayList<TaskInfo>();
				System.out.println(infos.size());
				for (TaskInfo info : infos) {
					if (info.isUserTask()) {
						userInfos.add(info);
					} else {
						systemInfos.add(info);
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						if (adapter == null) {
							adapter = new AdapterListener();
							lv_task_manager.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						ll_loading.setVisibility(View.INVISIBLE);
					}
				});
			};
		}.start();
	}

	private class AdapterListener extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			boolean systemIsChecked = sharedPreferences.getBoolean(
					"cb_show_system", false);
			if (systemIsChecked) {
				return userInfos.size() + systemInfos.size() + 2;

			} else {
				return userInfos.size() + 1;
			}
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
			TaskInfo taskInfo = null;
			if (position == 0) {
				TextView textView = new TextView(getApplicationContext());
				textView.setText("用户程序" + userInfos.size());
				return textView;
			} else if (position == (userInfos.size() + 1)) {
				TextView textView = new TextView(getApplicationContext());
				textView.setText("系统应用程序" + systemInfos.size());
				return textView;
			} else if (position <= userInfos.size()) {
				taskInfo = userInfos.get(position - 1);

			} else {
				taskInfo = systemInfos.get(position - userInfos.size() - 2);
			}
			View view = null;
			ViewHolder holder = null;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();// 5%

			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_taskinfo, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_task_icon);
				holder.tv_name = (TextView) view
						.findViewById(R.id.tv_task_name);
				holder.tv_memsize = (TextView) view
						.findViewById(R.id.tv_task_memsize);
				holder.cb_status = (CheckBox) view.findViewById(R.id.cb_status);
				view.setTag(holder);
			}
			holder.iv_icon.setImageDrawable(taskInfo.getIconDrawable());
			holder.tv_memsize.setText(Formatter.formatFileSize(
					getApplicationContext(), taskInfo.getMemsize()));
			holder.tv_name.setText(taskInfo.getName());
			holder.cb_status.setChecked(taskInfo.isChecked());
			if (getPackageName().equals(taskInfo.getPackagename())) {
				holder.cb_status.setVisibility(View.INVISIBLE);
			} else {
				holder.cb_status.setVisibility(View.VISIBLE);

			}

			return view;
		}

	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_memsize;
		CheckBox cb_status;
	}

	public void selectAll(View view) {
		for (TaskInfo info : infos) {
			info.setChecked(true);
			if (getPackageName().equals(info.getPackagename())) {
				continue;
			}
		}
		adapter.notifyDataSetChanged();
	}

	public void selectOppo(View view) {
		for (TaskInfo info : infos) {
			info.setChecked(!info.isChecked());
			if (getPackageName().equals(info.getPackagename())) {
				continue;
			}

		}
		adapter.notifyDataSetChanged();
	}

	public void killAll(View view) {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int count = 0;
		long savedmem = 0;
		for (TaskInfo info : infos) {
			if (getPackageName().equals(info.getPackagename())) {
				continue;
			}
			if (info.isUserTask()) {
				if (info.isChecked()) {

					userInfos.remove(info);
					activityManager.killBackgroundProcesses(info
							.getPackagename());
					savedmem += info.getMemsize();
					count++;
				}
			} else {
				if (info.isChecked()) {

					systemInfos.remove(info);
					activityManager.killBackgroundProcesses(info
							.getPackagename());
					savedmem += info.getMemsize();
					count++;
				}
			}

		}

		adapter.notifyDataSetChanged();
		processConut -= count;
		aviamem += savedmem;
		tv_process_count.setText("运行中的进程数：" + processConut);
		tv_mem_info.setText("剩余内存/总内存："
				+ Formatter.formatFileSize(this, aviamem) + "/"
				+ Formatter.formatFileSize(this, totalmem));

	}

	public void enterSetting(View view) {
		Intent intent = new Intent(this,
				TaskSettingActivity.class);
		startActivityForResult(intent, 0);
	}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	adapter.notifyDataSetChanged();
	super.onActivityResult(requestCode, resultCode, data);
}
}
