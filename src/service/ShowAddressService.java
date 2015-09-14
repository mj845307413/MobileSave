package service;

import android.app.Service;
import android.app.usage.UsageEvents.Event;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mj_mobileserver.R;

import db.query.NumberAddressQueryUtil;

public class ShowAddressService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private WindowManager windowManager = null;
	private View view = null;

	private TelephonyManager telephonyManager = null;
	private MyPhoneListener phoneListener = null;
	private OutCallReceiver receiver = null;
	private WindowManager.LayoutParams params;
	private SharedPreferences sharedPreferences=null;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		phoneListener = new MyPhoneListener();
		sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
		telephonyManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		
	}

	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String address = NumberAddressQueryUtil
						.queryNumber(incomingNumber);
				//Toast.makeText(getApplicationContext(), address, 1).show();
				MyToast(address);
				
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (view != null) {
					windowManager.removeView(view);

				}

				break;
			default:
				break;
			}
		}

	}
long []mHits=new long[2];
	public void MyToast(String address) {
		// TODO Auto-generated method stub
		view = View.inflate(this, R.layout.address_show,
				null);
		TextView textView = (TextView) view.findViewById(R.id.address_view);
		textView.setText(address);
		int[] ids = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.arraycopy(mHits, 1, mHits,0,mHits.length-1);
				mHits[mHits.length-1]=SystemClock.uptimeMillis();
				if (mHits[0]>=(SystemClock.uptimeMillis()-500)) {
					params.x=(windowManager.getDefaultDisplay().getWidth()-view.getWidth())/2;
					params.y=(windowManager.getDefaultDisplay().getHeight()-view.getHeight())/2;
					Editor editor=sharedPreferences.edit();
					editor.putInt("startx",params.x);
					editor.putInt("starty",params.y);
					editor.commit();
				}
			}
		});
		view.setOnTouchListener(new OnTouchListener() {
			int startx ;
			int starty ;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					startx=(int) event.getRawX();
					starty=(int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newx=(int) event.getRawX();
					int newy=(int) event.getRawY();
					int dx=newx-startx;
					int dy=newy-starty;
					params.x+=dx;
					params.y+=dy;
					if (params.x<0) {
						params.x=0;
					}
					if (params.y<0) {
						params.y=0;
					}
					if (params.x>(windowManager.getDefaultDisplay().getWidth()-view.getWidth())) {
						params.x=windowManager.getDefaultDisplay().getWidth()-view.getWidth();
					}
					if (params.y>(windowManager.getDefaultDisplay().getHeight()-view.getHeight())) {
						params.y=windowManager.getDefaultDisplay().getHeight()-view.getHeight();
					}
					startx=newx;
					starty=newy;
					windowManager.updateViewLayout(view, params);
					break;
				case MotionEvent.ACTION_UP:
					Editor editor=sharedPreferences.edit();
					editor.putInt("startx",params.x);
					editor.putInt("starty",params.y);
					System.out.println("startx"+startx+"starty"+starty+"params.x"+params.x+"params.y"+params.y);
					editor.commit();
					break;
				default:
					break;
				}
				return false;
			}
		});
		textView.setBackgroundResource(ids[sharedPreferences.getInt("which", 0)]);
		// 窗体的参数
		
	    params = new WindowManager.LayoutParams();
	    
	    params.x= sharedPreferences.getInt("startx", 100);
		params.y=sharedPreferences.getInt("starty", 100);
		System.out.println("params.x"+params.x+"params.y"+params.y);
		params.gravity = Gravity.TOP + Gravity.LEFT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;

		params.flags =
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

		windowManager.addView(view, params);
	}

	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String phoneString = getResultData();
			String address = NumberAddressQueryUtil.queryNumber(phoneString);
			//Toast.makeText(getApplicationContext(), address, 1).show();
			 MyToast(address);
			 new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					windowManager.removeView(view);

				}
			}, 10000);

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
		phoneListener = null;
		unregisterReceiver(receiver);
		receiver = null;
	}
}
