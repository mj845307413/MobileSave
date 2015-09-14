package service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import db.query.BlackNumberIncrease;
import android.R.string;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class CallSmsSafeService extends Service {
	private InnerSmsReceiver innerSmsReceiver;
	private BlackNumberIncrease blackNumberChange;
	private TelephonyManager telephonyManager;
	private TelephoneListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		blackNumberChange = new BlackNumberIncrease(this);
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new TelephoneListener();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		innerSmsReceiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(1000);
		registerReceiver(innerSmsReceiver, filter);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(innerSmsReceiver);
		innerSmsReceiver = null;
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

	private class TelephoneListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String result = blackNumberChange
						.blackNumberFindMode(incomingNumber);
				if (result.equals("3") || result.equals("1")) {
					System.out.println("不接电话");
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true,
							new CallLogObserver(incomingNumber, new Handler()));
					endCall();
				}
				break;
			default:
				break;
			}
		}
	}

	private class CallLogObserver extends ContentObserver {
		String number = null;

		public CallLogObserver(String numberString, Handler handler) {
			super(handler);
			number = numberString;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			deleteCalllog(number);
			getContentResolver().unregisterContentObserver(this);
		}

	}

	private void deleteCalllog(String incomingNumber) {
		// TODO Auto-generated method stub
		ContentResolver contentResolver = getContentResolver();
		Uri uri = Uri.parse("content://call_log/calls");
		contentResolver
				.delete(uri, "number=?", new String[] { incomingNumber });

	}

	private void endCall() {
		// TODO Auto-generated method stub
		try {
			// 加载servicemanager的字节码
			Class clazz = CallSmsSafeService.class.getClassLoader().loadClass(
					"android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(ibinder).endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class InnerSmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
				String address = message.getOriginatingAddress();
				if (blackNumberChange.blackNumberFindNumber(address)) {
					String modeString = blackNumberChange
							.blackNumberFindMode(address);
					if ("2".equals(modeString) || "3".equals(modeString)) {
						abortBroadcast();
					}
				}
			}
		}

	}
}
