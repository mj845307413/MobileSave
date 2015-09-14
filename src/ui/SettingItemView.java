package ui;

import com.example.mj_mobileserver.R;
import com.example.mj_mobileserver.R.string;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {
	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		iniView(context);
	}

	
	private String setting_title=null;
	private String description_on=null;
	private String description_off=null;
	/**
	 * 一般在布局文件里面实现的为两个参数的构造方法。。。。。。
	 * 
	 * @param context
	 * @param attrs
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		// TODO Auto-generated constructor stub
		setting_title= attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mj_mobileserver","setting_title");
		description_on= attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mj_mobileserver","description_on");
		description_off= attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mj_mobileserver","description_off");
		textTitle.setText(setting_title);
		textdesc.setText(description_off);

	}

	public SettingItemView(Context context) {
		super(context);
		iniView(context);
		// TODO Auto-generated constructor stub

	}

	/**
	 * @param context
	 */
	private CheckBox checkBox = null;
	private TextView textdesc = null;
    private TextView textTitle=null;
	private void iniView(Context context) {
		// TODO Auto-generated method stub
		View.inflate(context, R.layout.setting_item, this);
		checkBox = (CheckBox) findViewById(R.id.setting_checkbox);
		textdesc = (TextView) findViewById(R.id.setting_textview2);
		textTitle=(TextView)findViewById(R.id.setting_textview1);
	}

	public boolean isCheck() {
		return checkBox.isChecked();

	}

	public void setChecked(boolean checked) {
		if (checked) {
			textdesc.setText(description_on);

		} else {
			textdesc.setText(description_off);

		}
		checkBox.setChecked(checked);
	}
}
