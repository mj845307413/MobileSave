package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.example.mj_mobileserver.AToolActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;

public class SmsUtils {
	public interface BackUpCallBack {
		public void getMax(int max);

		public void getProgress(int progress);
	}

	public static void backupSms(Context context, BackUpCallBack back)
			throws Exception {
		ContentResolver contentResolver = context.getContentResolver();

		System.out.println("s1");
		File file = new File(Environment.getExternalStorageDirectory(),
				"backup.xml");
		System.out.println("s2");

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(fileOutputStream, "utf-8");
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");
		System.out.println("s3");
		Uri uri = Uri.parse("content://sms/");
		System.out.println(uri);
		Cursor cursor = contentResolver.query(uri, new String[] { "body",
				"address", "type", "date" }, null, null, null);
		int max = cursor.getCount();
		serializer.attribute(null, "max", max + "");
		back.getMax(max);
		int i = 0;
		back.getProgress(i);
		while (cursor.moveToNext()) {
			serializer.startTag(null, "sms");
			String bodyString = cursor.getString(0);
			String address = cursor.getString(1);
			String typeString = cursor.getString(2);
			String dateString = cursor.getString(3);
			serializer.startTag(null, "body");
			serializer.text(bodyString);
			serializer.endTag(null, "body");
			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");

			serializer.startTag(null, "type");
			serializer.text(typeString);
			serializer.endTag(null, "type");

			serializer.startTag(null, "date");
			serializer.text(dateString);
			serializer.endTag(null, "date");

			serializer.endTag(null, "sms");
			i++;
			back.getProgress(i);
		}
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fileOutputStream.close();
	}

	public static void restoreSms(Context context, boolean flag)
			throws Exception {
		Uri uri = Uri.parse("content://sms/");
		ContentResolver contentResolver = context.getContentResolver();

		if (flag) {
			contentResolver.delete(uri, null, null);
		}
		// 1.��ȡsd���ϵ�xml�ļ�
		// Xml.newPullParser();
		File f = new File(android.os.Environment.getExternalStorageDirectory()
				+ "/backup.xml");
		String path = f.getAbsolutePath();
		File myFile = new File(path);
		if (myFile.exists()) {
			Toast.makeText(context, "�Ѿ������ļ�", 2).show();
		}
		XmlPullParser parser = Xml.newPullParser();
		FileInputStream fileInputStream = new FileInputStream(myFile);
		parser.setInput(fileInputStream, "utf-8");

		// 2.��ȡmax
		// 3.��ȡÿһ��������Ϣ��body date type address

		// 4.�Ѷ��Ų��뵽ϵͳ��ϢӦ�á�
		ContentValues values = null;
		int type = parser.getEventType();
		int currentcount = 0;
		while (type != XmlPullParser.END_DOCUMENT) {// ���û�е��ļ��Ľ�β ��һֱִ��
			switch (type) {
			case XmlPullParser.START_TAG:// ��ʼ�ڵ�
				if ("count".equals(parser.getName())) {
					// pd.setMax(Integer.parseInt(parser.nextText()));//���Ի����������ֵ
				} else if ("sms".equals(parser.getName())) {// ������Ϣ�ڵ��ʱ���½�һ��contentValues
					values = new ContentValues();
				} else if ("_id".equals(parser.getName())) {// ��ȡ�ڵ������ ���Ϊ id
					values.put("_id", parser.nextText());
				} else if ("address".equals(parser.getName())) {
					values.put("address", parser.nextText());
				} else if ("date".equals(parser.getName())) {
					values.put("date", parser.nextText());
				} else if ("type".equals(parser.getName())) {
					values.put("type", parser.nextText());
				} else if ("body".equals(parser.getName())) {
					values.put("body", parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG: {
				if ("sms".equals(parser.getName())) {// ����ڵ���sms
					ContentResolver resolver = context.getContentResolver();
					resolver.insert(uri, values);// �����ݿ��в�������
					values = null;//

					currentcount++;
					// pd.setProgress(currentcount);//���ý���
					break;
				}
			}
			default:
				break;
			}
			type = parser.next();// ��Ҫ���˸�type���¸�ֵ
		}

	}

	/*
	 * ContentValues values = new ContentValues(); values.put("body",
	 * "woshi duanxin de neirong"); values.put("date", "1395045035573");
	 * values.put("type", "1"); values.put("address", "5558");
	 */
}
