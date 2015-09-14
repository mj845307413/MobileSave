package com.example.mj_mobileserver;


import utils.SmsUtils;
import utils.SmsUtils.BackUpCallBack;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AToolActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);

	}

	public void numberQuery(View view) {
		Intent intent = new Intent(this, NumberAddressQueryActivity.class);
		startActivity(intent);

	}

	public ProgressDialog progressDialog;

	public void smsBackup(View view) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在备份");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.show();
		new Thread() {
			public void run() {
				try {
					SmsUtils.backupSms(getApplicationContext(), new BackUpCallBack() {
						
						@Override
						public void getProgress(int progress) {
							// TODO Auto-generated method stub
							progressDialog.setProgress(progress);
							
						}
						
						@Override
						public void getMax(int max) {
							// TODO Auto-generated method stub
							progressDialog.setMax(max);
						}
					});
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AToolActivity.this, "备份成功", 0).show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AToolActivity.this, "备份失败", 0).show();
						}
					});
				}finally{
					progressDialog.dismiss();
				}
			}
		}.start();

	}

	public void smsRestore(View view) throws Exception {
		SmsUtils.restoreSms(this,true);
		Toast.makeText(this,"复制完成", 2).show();

	}
}
