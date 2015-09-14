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
		// 1.读取sd卡上的xml文件
		// Xml.newPullParser();
		File f = new File(android.os.Environment.getExternalStorageDirectory()
				+ "/backup.xml");
		String path = f.getAbsolutePath();
		File myFile = new File(path);
		if (myFile.exists()) {
			Toast.makeText(context, "已经存在文件", 2).show();
		}
		XmlPullParser parser = Xml.newPullParser();
		FileInputStream fileInputStream = new FileInputStream(myFile);
		parser.setInput(fileInputStream, "utf-8");

		// 2.读取max
		// 3.读取每一条短信信息，body date type address

		// 4.把短信插入到系统短息应用。
		ContentValues values = null;
		int type = parser.getEventType();
		int currentcount = 0;
		while (type != XmlPullParser.END_DOCUMENT) {// 如果没有到文件的结尾 则一直执行
			switch (type) {
			case XmlPullParser.START_TAG:// 起始节点
				if ("count".equals(parser.getName())) {
					// pd.setMax(Integer.parseInt(parser.nextText()));//给对话框设置最大值
				} else if ("sms".equals(parser.getName())) {// 但是信息节点的时候新建一个contentValues
					values = new ContentValues();
				} else if ("_id".equals(parser.getName())) {// 获取节点的名称 如果为 id
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
				if ("sms".equals(parser.getName())) {// 如果节点是sms
					ContentResolver resolver = context.getContentResolver();
					resolver.insert(uri, values);// 先数据库中插入数据
					values = null;//

					currentcount++;
					// pd.setProgress(currentcount);//设置进度
					break;
				}
			}
			default:
				break;
			}
			type = parser.next();// 不要忘了给type重新赋值
		}

	}

	/*
	 * ContentValues values = new ContentValues(); values.put("body",
	 * "woshi duanxin de neirong"); values.put("date", "1395045035573");
	 * values.put("type", "1"); values.put("address", "5558");
	 */
}
