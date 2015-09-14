package com.example.mj_mobileserver;

import ui.SettingItemView;

import com.example.mj_mobileserver.R.anim;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Setup2Activity extends BaseSetupActivity {
	private Button nextButton = null;
	private Button preButton = null;
	private SettingItemView sim_setup2_sim = null;
	private TelephonyManager telephonyManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);//相当于执行父类的oncreat方法
		setContentView(R.layout.setup2);
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		sim_setup2_sim = (SettingItemView) findViewById(R.id.sim_setup2_sim);
		String simString = sharedPreferences.getString("sim", null);
		if (TextUtils.isEmpty(simString)) {
			sim_setup2_sim.setChecked(false);
		} else {
			sim_setup2_sim.setChecked(true);
		}
		sim_setup2_sim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editor editor = sharedPreferences.edit();

				if (!sim_setup2_sim.isCheck()) {
					sim_setup2_sim.setChecked(true);
					String sim = telephonyManager.getSimSerialNumber();
					if (TextUtils.isEmpty(sim)) {
						sim = "123456";
						Toast.makeText(Setup2Activity.this, "没有发现SIM卡", 3).show();
					}else {
						Toast.makeText(Setup2Activity.this, "有SIM卡", 3).show();

					}
					editor.putString("sim", sim);
				} else {
					sim_setup2_sim.setChecked(false);
					String sim =null;
					editor.putString("sim", sim);
				}

				editor.commit();
			}
		});
		nextButton = (Button) findViewById(R.id.nextButton2);
		preButton = (Button) findViewById(R.id.preButton2);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showNext();
			}
		});
		preButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPre();

			}
		});

	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		String sim=sharedPreferences.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			Toast.makeText(this,"sim卡没有绑定", 5).show();
			return ;
		}
		Intent intent = new Intent(Setup2Activity.this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(anim.scale_out, anim.trans_in);

	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Setup2Activity.this, Setup1Activity.class);
		startActivity(intent);
		finish();

	}

}
