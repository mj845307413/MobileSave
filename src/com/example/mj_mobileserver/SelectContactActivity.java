package com.example.mj_mobileserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectContactActivity extends Activity {
	private ListView listView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selector_contact);
		listView = (ListView) findViewById(R.id.list_select_contact);
		final List<Map<String, String>> data = getContact();
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.contact_item_view, new String[] { "name", "phone" },
				new int[] { R.id.name, R.id.phone });
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Map<String, String> map=data.get(position);
				String phoneString=map.get("phone");
				Intent intent=new Intent();
				intent.putExtra("phone", phoneString);
				intent.setClass(SelectContactActivity.this, Setup3Activity.class);
setResult(0, intent);
finish();
			}
			
		});
	}

	private List<Map<String, String>> getContact() {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ContentResolver contentResolver = getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uridate = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = contentResolver.query(uri,
				new String[] { "contact_id" }, null, null, null);
		while (cursor.moveToNext()) {
			String contact_id = cursor.getString(0);
			if (!TextUtils.isEmpty(contact_id)) {
				Map<String, String> map = new HashMap<String, String>();
				Cursor datacursor = contentResolver.query(uridate,
						new String[] { "data1", "mimetype" }, "contact_id=?",
						new String[] { contact_id }, null);
				while (datacursor.moveToNext()) {
					String data1 = datacursor.getString(0);
					String mimetypeString = datacursor.getString(1);
					if ("vnd.android.cursor.item/name".equals(mimetypeString)) {
						// 联系人的姓名
						map.put("name", data1);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetypeString)) {
						// 联系人的电话号码
						map.put("phone", data1);
					}
					
				}
				list.add(map);
				datacursor.close();
			}
		}
		cursor.close();
		return list;
	}
}
