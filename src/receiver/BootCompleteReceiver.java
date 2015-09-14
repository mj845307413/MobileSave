package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {
private SharedPreferences sharedPreferences=null;
private TelephonyManager telephonyManager=null;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//广播要这么写
		sharedPreferences=context.getSharedPreferences("config",Context.MODE_PRIVATE);
             String simString= sharedPreferences.getString("sim", null);
             telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
             String realSim=telephonyManager.getSimSerialNumber();
             if (TextUtils.isEmpty(realSim)) {
            	 realSim="12345678";
			}
             if (realSim.equals(simString)) {
				
			} else {
Toast.makeText(context, "SIM卡变更", 5).show();
			}
	}

}
