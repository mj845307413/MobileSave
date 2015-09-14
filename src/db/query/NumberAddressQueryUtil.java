package db.query;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtil {
	private static String path = "data/data/com.example.mj_mobileserver/files/address.db";

	public static String queryNumber(String number) {
		String address = number;
		String locationString = number;
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		if (address.matches("^1[34568]\\d{9}$")) {
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { number.substring(0, 7) });
			while (cursor.moveToNext()) {
				locationString = cursor.getString(0);
			}
			cursor.close();

		} else {
			switch (number.length()) {
			case 4:
				locationString = "模拟器";
				break;
			case 3:
				locationString = "匪警号码";
				break;
			case 5:
				locationString = "客服电话";
				break;
			case 7:
				locationString = "本地号码";
				break;
			case 8:
				locationString = "本地号码";
				break;
			default:
				if (number.length() > 10 && number.startsWith("0")) {
					Cursor cursor = database.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1,3) });
					while (cursor.moveToNext()) {
						String string = cursor.getString(0);
						locationString = string.substring(0,
								string.length() - 2);
					}
					cursor.close();
					cursor = database.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1,4) });
					while (cursor.moveToNext()) {
						String string = cursor.getString(0);
						locationString = string.substring(0,
								string.length() - 2);
					}
					cursor.close();
				}
				break;
			}
		}
		return locationString;

	}
}
