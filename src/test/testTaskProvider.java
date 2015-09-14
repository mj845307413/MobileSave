package test;

import java.util.List;

import db.domain.TaskInfo;

import engine.TaskInfoProvider;
import engine.TaskInfoProvider1;
import android.test.AndroidTestCase;

public class testTaskProvider extends AndroidTestCase {
public void testGetTaskInfos() throws Exception {
	TaskInfoProvider1 infoProvider=new TaskInfoProvider1();
	List<TaskInfo> infos=infoProvider.getTaskInfos(getContext());
	for (TaskInfo taskInfo : infos) {
		System.out.println(taskInfo.toString());
	}
}
}
