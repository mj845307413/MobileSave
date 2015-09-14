package db.query;

import java.util.ArrayList;
import java.util.List;

import db.domain.BlackNumberInfo;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

public class BlackNumberIncrease {
	private BlackNumberDB blackNumberDB;
	List<BlackNumberInfo> result;

	public BlackNumberIncrease(Context context) {

		blackNumberDB = new BlackNumberDB(context);
		// TODO Auto-generated constructor stub

	}

	public boolean blackNumberIncrease(String number, String mode) {
		SQLiteDatabase sqLiteDatabase = blackNumberDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		sqLiteDatabase.insert("blacknumber", null, values);
		sqLiteDatabase.close();
		return true;

	}

	public boolean blackNumberDecrease(String number) {
		SQLiteDatabase sqLiteDatabase = blackNumberDB.getWritableDatabase();
		sqLiteDatabase.delete("blacknumber", "number=?",
				new String[] { number });
		sqLiteDatabase.close();
		return true;

	}

	public boolean blackNumberUpdate(String number, String mode) {
		SQLiteDatabase sqLiteDatabase = blackNumberDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		sqLiteDatabase.update("blacknumber", values, "number=?",
				new String[] { number });
		sqLiteDatabase.close();
		return true;

	}

	public String blackNumberFindMode(String number) {
		String result = null;
		SQLiteDatabase sqLiteDatabase = blackNumberDB.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select mode from blacknumber where number=? ",
				new String[] { number });
		if (cursor.moveToNext()) {
			result = cursor.getString(0);
		}
		sqLiteDatabase.close();
		cursor.close();
		return result;

	}

	public boolean blackNumberFindNumber(String number) {
		boolean result = false;
		SQLiteDatabase sqLiteDatabase = blackNumberDB.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select * from blacknumber where number=? ",
				new String[] { number });
		if (cursor.moveToNext()) {
			result = true;
		}
		sqLiteDatabase.close();
		cursor.close();
		return result;

	}

	public List<BlackNumberInfo> blackNumberFindAllNumber() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * new Handler().postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub
		 * 
		 * }Can't create handler inside thread that has not called
		 * Looper.prepare()
		 * 
		 * }, 5000);
		 */
		result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase sqLiteDatabase = blackNumberDB.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select number,mode from blacknumber order by _id desc", null);
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			String numberString = cursor.getString(0);
			String modeString = cursor.getString(1);
			blackNumberInfo.setNumber(numberString);
			blackNumberInfo.setMode(modeString);
			result.add(blackNumberInfo);
			System.out.println(blackNumberInfo.toString());
		}
		sqLiteDatabase.close();
		cursor.close();
		return result;

	}

	/**
	 * 
	 * 
	 * @param offSet
	 *            从哪里开始
	 * @param maxnumber最大
	 * @return
	 */
	public List<BlackNumberInfo> blackNumberFindPartNumber(int offSet,
			int maxnumber) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * new Handler().postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub
		 * 
		 * }Can't create handler inside thread that has not called
		 * Looper.prepare()
		 * 
		 * }, 5000);
		 */
		result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase sqLiteDatabase = blackNumberDB.getReadableDatabase();
		Cursor cursor = sqLiteDatabase
				.rawQuery(
						"select number,mode from blacknumber order by _id desc  limit ? offset ? ",
						new String[] { String.valueOf(maxnumber),
								String.valueOf(offSet) });
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			String numberString = cursor.getString(0);
			String modeString = cursor.getString(1);
			blackNumberInfo.setNumber(numberString);
			blackNumberInfo.setMode(modeString);
			result.add(blackNumberInfo);
			System.out.println(blackNumberInfo.toString());
		}
		sqLiteDatabase.close();
		cursor.close();
		return result;

	}
}
