package com.example.mj_mobileserver;

import com.example.mj_mobileserver.R.anim;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class Setup4Activity extends BaseSetupActivity {
	private Button nextButton = null;
	private Button preButton = null;
	private GestureDetector gestureDetector;
	private SharedPreferences sharedPreferences = null;

	private boolean protecting;
	private CheckBox checkBox=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup4);
		checkBox=(CheckBox)findViewById(R.id.checkbox);
		gestureDetector = new GestureDetector(Setup4Activity.this,
				new LearnGestureListener());
		nextButton = (Button) findViewById(R.id.nextButton4);
		preButton = (Button) findViewById(R.id.preButton4);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		protecting=sharedPreferences.getBoolean("protecting",false);
		if (protecting) {
			checkBox.setText("手机防盗已经开启");
			checkBox.setChecked(true);
		} else {
			checkBox.setText("手机防盗已经关闭");
			checkBox.setChecked(false);


		}
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					checkBox.setText("手机防盗已经开启");
					
				} else {
					checkBox.setText("手机防盗已经关闭");
									}
				Editor editor=sharedPreferences.edit();
				editor.putBoolean("protecting",isChecked);
				editor.commit();

			}
		});
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showNext();			}
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
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		Intent intent = new Intent(Setup4Activity.this,
				LostFindActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(anim.alpha_out, anim.trans_in);


	}
	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Setup4Activity.this,
				Setup3Activity.class);
		startActivity(intent);
		finish();
	}

}
