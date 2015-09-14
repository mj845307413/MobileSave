package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {
	private LocationManager locationManager = null;
	private locationListener listener = null;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locationManager.removeUpdates(listener);
		listener = null;

	}
	class locationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			String longtitudeString = "经度" + location.getLongitude();
			String latitudeString = "纬度" + location.getLatitude();
			String accuracyString = "准确度" + location.getAccuracy();
			InputStream inputStream;
			try {
				inputStream=getAssets().open("axisoffset.dat");
				ModifyOffset modifyOffset=ModifyOffset.getInstance(inputStream);
				PointDouble pointDouble=modifyOffset.s2c(new PointDouble(location.getLongitude(), location.getLatitude()));
				longtitudeString="经度"+pointDouble.x;
				latitudeString="纬度"+pointDouble.y;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SharedPreferences preferences=getSharedPreferences("config",MODE_PRIVATE);
			Editor editor=preferences.edit();
			editor.putString("location", longtitudeString + latitudeString + accuracyString);
			editor.commit();
			/*Toast.makeText(MainActivity.this,
					longtitudeString + latitudeString + accuracyString, 5)
					.show();
			TextView textView = new TextView(MainActivity.this);
			textView.setText(longtitudeString + latitudeString + accuracyString);
			setContentView(textView);
*/		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		List<String> providerList = locationManager.getAllProviders();
		for (String string : providerList) {
			System.out.println(string);
		}
		listener = new locationListener();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
		String string = locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(string, 6000, 15, listener);

	}
}
