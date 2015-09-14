package test;

import java.util.List;
import java.util.Random;

import db.domain.BlackNumberInfo;
import db.query.BlackNumberDB;
import db.query.BlackNumberIncrease;
import android.test.AndroidTestCase;

public class TestBlackNumber extends AndroidTestCase {
	public void testCreateDB() throws Exception {
		BlackNumberDB blackNumberDB = new BlackNumberDB(getContext());
		blackNumberDB.getWritableDatabase();
	}

	public void testAdd() throws Exception {
		BlackNumberIncrease blackNumberIncrease = new BlackNumberIncrease(
				getContext());
		long baseNumber=130000001;
		Random random=new Random();
		for (int i = 0; i <50; i++) {
			blackNumberIncrease.blackNumberIncrease(String.valueOf(baseNumber+i),String.valueOf(random.nextInt(3)+1));
		}
		
	}

	public void testDecrease() throws Exception {
		BlackNumberIncrease blackNumberIncrease = new BlackNumberIncrease(
				getContext());
		blackNumberIncrease.blackNumberDecrease("130000002");

	}

	public void testUpdate() throws Exception {
		BlackNumberIncrease blackNumberIncrease = new BlackNumberIncrease(
				getContext());
		blackNumberIncrease.blackNumberUpdate("123123", "3");

	}

	public void testFind() throws Exception {
		BlackNumberIncrease blackNumberIncrease = new BlackNumberIncrease(
				getContext());
		boolean b =blackNumberIncrease.blackNumberFindNumber("123123");
assertEquals(b, true);
	}

	public void testFindAll() throws Exception {
		BlackNumberIncrease blackNumberIncrease = new BlackNumberIncrease(
				getContext());
List<BlackNumberInfo> blackNumberInfos=blackNumberIncrease.blackNumberFindAllNumber();
	for (BlackNumberInfo blackNumberInfo : blackNumberInfos) {
		System.out.println(blackNumberInfo.toString());
	}
	}
}
