package receiver;

import service.GPSService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.mj_mobileserver.R;

public class SMSreceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";
	private SharedPreferences preferences = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		preferences = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
			String senderString = smsMessage.getOriginatingAddress();
			String safeNumber = preferences.getString("phone", "");
			String body = smsMessage.getMessageBody();
			if (senderString.contains(safeNumber)) {

				if ("#*location*#".equals(body)) {
					Log.i(TAG, "得到手机的GPS");
					Intent intent2=new Intent(context, GPSService.class);
					context.startService(intent2);
					SharedPreferences sharedPreferences=context.getSharedPreferences("config",context.MODE_PRIVATE);
					String lastLocation=sharedPreferences.getString("location", null);
					if (TextUtils.isEmpty(lastLocation)) {
						SmsManager.getDefault().sendTextMessage(senderString, null,"正在GET LOCATION", null, null);
					}else {
						SmsManager.getDefault().sendTextMessage(senderString, null,lastLocation, null, null);

					}
					abortBroadcast();
				} else if ("#*alarm*#".equals(body)) {
					Log.i(TAG, "播放报警信息");
					abortBroadcast();
					MediaPlayer mediaPlayer = MediaPlayer.create(context,
							R.raw.jzqs);
					mediaPlayer.setLooping(false);
					mediaPlayer.setVolume(1, 1);
					mediaPlayer.start();
				} else if ("#*wipedata*#".equals(body)) {
					Log.i(TAG, "远程清除数据");
					abortBroadcast();

				} else if ("#*locksceen*#".equals(body)) {
					Log.i(TAG, "远程锁屏");
					abortBroadcast();

				}
			}
		}
	}

}
