package ui;

import com.example.mj_mobileserver.R;
import com.example.mj_mobileserver.R.string;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingClickView extends RelativeLayout {
	public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		iniView(context);
	}

	
	private String setting_title=null;
	private String description_off=null;
	/**
	 * 一般在布局文件里面实现的为两个参数的构造方法。。。。。。
	 * 
	 * @param context
	 * @param attrs
	 */
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		// TODO Auto-generated constructor stub
		setting_title= attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mj_mobileserver","setting_title");
		description_off= attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mj_mobileserver","description_off");
		textTitle.setText(setting_title);
		textdesc.setText(description_off);


	}
public void setTextdesc(int which) {
	final String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
	textdesc.setText(items[which]);

}
	public SettingClickView(Context context) {
		super(context);
		iniView(context);
		// TODO Auto-generated constructor stub

	}

	/**
	 * @param context
	 */
	private ImageView imageView = null;
	private TextView textdesc = null;
    private TextView textTitle=null;
	private void iniView(Context context) {
		// TODO Auto-generated method stub
		View.inflate(context, R.layout.setting_click_view, this);
		imageView = (ImageView) findViewById(R.id.setting_click_view_image);
		textdesc = (TextView) findViewById(R.id.setting_click_view_textview2);
		textTitle=(TextView)findViewById(R.id.setting_click_view_textview1);
	}

	
}
