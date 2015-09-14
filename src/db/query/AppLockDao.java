package db.query;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppLockDao {
	private AppLockDB appLockDB;
private Context context;
	public AppLockDao(Context context) {
		appLockDB = new AppLockDB(context);
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	public boolean add(String name) {
		SQLiteDatabase sqLiteDatabase = appLockDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", name);
		sqLiteDatabase.insert("applock", null, values);
		sqLiteDatabase.close();
		Intent intent = new Intent();
		intent.setAction("applockchange");
		context.sendBroadcast(intent);
		return true;
	}

	public boolean delete(String name) {
		SQLiteDatabase sqLiteDatabase = appLockDB.getWritableDatabase();
		sqLiteDatabase.delete("applock", "packname=?", new String[] { name });
		sqLiteDatabase.close();
		Intent intent = new Intent();
		intent.setAction("applockchange");
		context.sendBroadcast(intent);
		return true;
	}

	public boolean find(String name) {
		boolean result = false;
		SQLiteDatabase sqLiteDatabase = appLockDB.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.query("applock", null, "packname=?",
				new String[] { name }, null, null, null);
		if (cursor.moveToNext()) {
			result = true;
		}
		sqLiteDatabase.close();
		cursor.close();
		return result;

	}

	public List<String> findall() {
		List<String> protectList = new ArrayList<String>();
		SQLiteDatabase sqLiteDatabase = appLockDB.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.query("applock",
				new String[] { "packname" }, null, null, null, null, null);
		if (cursor.moveToNext()) {
			protectList.add(cursor.getString(0));
		}
		sqLiteDatabase.close();
		cursor.close();
		return protectList;

	}

}
