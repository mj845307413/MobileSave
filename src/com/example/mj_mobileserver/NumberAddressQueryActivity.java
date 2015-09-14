package com.example.mj_mobileserver;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import db.query.NumberAddressQueryUtil;

public class NumberAddressQueryActivity extends Activity {
	private static final String TAG = "NumberAddressQueryActivity";
	private EditText phoneEditText = null;
	private Button numberQueryButton = null;
	private TextView resultTextView = null;
private Vibrator vibrator=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address);
		phoneEditText = (EditText) findViewById(R.id.edit_phone);
		numberQueryButton = (Button) findViewById(R.id.number_query);
		resultTextView = (TextView) findViewById(R.id.result);
		vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
		phoneEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 3 && s != null) {
					String address = NumberAddressQueryUtil.queryNumber(s
							.toString());
					resultTextView.setText(address);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void numberAddressQuery(View view) {
		String phoneString = phoneEditText.getText().toString().trim();
		if (TextUtils.isEmpty(phoneString)) {
			Toast.makeText(this, "ºÅÂëÎª¿Õ", 2).show();
			long[] pattern={200,200,300,300,400,400,500,500,600,600,700,700,800,800};
			vibrator.vibrate(pattern,-1);
			Animation animation=AnimationUtils.loadAnimation(this,R.anim.shake);
			phoneEditText.setAnimation(animation);
			return;
		} else {
			String string = NumberAddressQueryUtil.queryNumber(phoneString);
			resultTextView.setText(string);
		}
	}
}
