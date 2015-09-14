package com.example.mj_mobileserver;

import com.example.mj_mobileserver.R.anim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Setup1Activity extends BaseSetupActivity {
	private Button button=null;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.setup1);
	button=(Button)findViewById(R.id.nextButton1);
	button.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showNext() ;
			
		}
	});
}



@Override
public void showNext() {
	// TODO Auto-generated method stub
	Intent intent=new Intent(Setup1Activity.this, Setup2Activity.class);
	startActivity(intent);
	finish();
	overridePendingTransition(anim.trans_out, anim.trans_in);
}
@Override
public void showPre() {
	// TODO Auto-generated method stub
}
}
