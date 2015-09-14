package com.example.mj_mobileserver;

import com.example.mj_mobileserver.R.anim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {
	private Button nextButton = null;
	private Button preButton = null;
	private Button selectButton = null;
	private EditText phoneEditText = null;
	SharedPreferences sharedPreferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup3);
		System.out.println("activity3");
		nextButton = (Button) findViewById(R.id.nextButton3);
		preButton = (Button) findViewById(R.id.preButton3);
		selectButton = (Button) findViewById(R.id.select_contact);
		phoneEditText = (EditText) findViewById(R.id.phone);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		phoneEditText.setText(sharedPreferences.getString("phone", ""));
		selectButton.setOnClickListener(new selectContact_Listener());
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNext();
				// TODO Auto-generated method stub
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
		String string = phoneEditText.getText().toString().trim();
		if (TextUtils.isEmpty(string)) {
			Toast.makeText(Setup3Activity.this, "»¹Ã»ÓÐºÅÂë", 2).show();
			return;
		}
		Editor editor = sharedPreferences.edit();
		editor.putString("phone", string);
		editor.commit();
		Intent intent = new Intent(Setup3Activity.this, Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(anim.rotate_out, anim.trans_in);

		// TODO Auto-generated method stub

	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Setup3Activity.this, Setup2Activity.class);
		startActivity(intent);
		finish();

	}

	class selectContact_Listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Setup3Activity.this,
					SelectContactActivity.class);
			startActivityForResult(intent, 0);
		}

	}

	private String selectPhoneString;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		} else {
			selectPhoneString = data.getStringExtra("phone");
			phoneEditText.setText(selectPhoneString);
		}
	}

}
