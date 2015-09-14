package com.example.mj_mobileserver;

import java.util.List;

import db.domain.BlackNumberInfo;
import db.query.BlackNumberIncrease;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CallMSNActivity extends Activity {
	ListView listView = null;
	private BlackNumberIncrease blackNumberchange;
	List<BlackNumberInfo> blackNumberInfos;
	private listAdapter adapter;
	private int offset = 0;
	private int maxnumber = 15;
	private LinearLayout smsLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_scm_safe);
		smsLayout = (LinearLayout) findViewById(R.id.scm_linearlayout);
		listView = (ListView) findViewById(R.id.call_scm_safe_listview);
		blackNumberchange = new BlackNumberIncrease(this);
		fillNumber();
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int position = listView.getLastVisiblePosition();
					if (position == (blackNumberInfos.size() - 1)) {
						offset += maxnumber;
						fillNumber();
					}
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					break;
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void fillNumber() {
		smsLayout.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (blackNumberInfos == null) {
					blackNumberInfos = blackNumberchange.blackNumberFindPartNumber(offset, maxnumber);

				} else {
					blackNumberInfos.addAll(blackNumberchange.blackNumberFindPartNumber(offset, maxnumber));
				}

				runOnUiThread(new Runnable() {
					public void run() {
						smsLayout.setVisibility(View.INVISIBLE);
						if (adapter == null) {
							adapter = new listAdapter();
							listView.setAdapter(adapter);

						} else {
							adapter.notifyDataSetChanged();
						}
					}
				});
			}
		}).start();
	}

	private class listAdapter extends BaseAdapter {

		private ViewHolder holder;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return blackNumberInfos.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_sms, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) view
						.findViewById(R.id.tv_black_number);
				holder.tv_mode = (TextView) view
						.findViewById(R.id.tv_block_mode);
				holder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);
				view.setTag(holder);
			}

			holder.tv_number
					.setText(blackNumberInfos.get(position).getNumber());
			String modeString = blackNumberchange
					.blackNumberFindMode(blackNumberInfos.get(position)
							.getNumber());
			if ("1".equals(modeString)) {
				holder.tv_mode.setText("电话拦截");
			} else if ("2".equals(modeString)) {
				holder.tv_mode.setText("短信拦截");
			} else if ("3".equals(modeString)) {
				holder.tv_mode.setText("全部拦截");
			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new Builder(
							CallMSNActivity.this);
					builder.setTitle("删除");
					builder.setMessage("是否要删除");
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									blackNumberchange
											.blackNumberDecrease(blackNumberInfos
													.get(position).getNumber());
									blackNumberInfos.remove(position);
									adapter.notifyDataSetChanged();
								}

							});
					builder.setNegativeButton("no", null);
					builder.show();
				}
			});
			return view;
		}

		class ViewHolder {
			TextView tv_number;
			TextView tv_mode;
			ImageView iv_delete;
		}
	}

	private AlertDialog alertDialog;
	private Button cancelButton;
	private Button oKButton;
	private CheckBox phoneBox;
	private CheckBox smsBox;
	private EditText editText;

	public void addBlackNumber(View v) {
		AlertDialog.Builder builder = new Builder(CallMSNActivity.this);
		View view = View.inflate(CallMSNActivity.this,
				R.layout.dialog_add_blacknumber, null);
		editText = (EditText) view.findViewById(R.id.et_blacknumber);
		phoneBox = (CheckBox) view.findViewById(R.id.cb_phone);
		smsBox = (CheckBox) view.findViewById(R.id.cb_sms);
		oKButton = (Button) view.findViewById(R.id.ok);
		cancelButton = (Button) view.findViewById(R.id.cancel);
		alertDialog = builder.create();
		alertDialog.setView(view, 0, 0, 0, 0);

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		oKButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String numberString = editText.getText().toString().trim();
				String modeString = null;
				if (TextUtils.isEmpty(numberString)) {
					Toast.makeText(CallMSNActivity.this, "不能为空啊，哥哥", 2).show();
					return;
				} else if (phoneBox.isChecked() && !smsBox.isChecked()) {
					modeString = "1";
				} else if (!phoneBox.isChecked() && smsBox.isChecked()) {
					modeString = "2";
				} else if (phoneBox.isChecked() && smsBox.isChecked()) {
					modeString = "3";
				} else {
					Toast.makeText(CallMSNActivity.this, "选择模式", 2).show();
					return;
				}
				blackNumberchange.blackNumberIncrease(numberString, modeString);
				BlackNumberInfo info = new BlackNumberInfo();
				info.setNumber(numberString);
				info.setMode(modeString);
				blackNumberInfos.add(info);
				adapter.notifyDataSetChanged();
				alertDialog.dismiss();
			}
		});

		alertDialog.show();

	}
}
