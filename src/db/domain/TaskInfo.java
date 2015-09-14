package db.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	private Drawable iconDrawable;
	private String name;
	private String packagename;
	private long memsize;
	private boolean userTask;
	private boolean checked;

	public Drawable getIconDrawable() {
		return iconDrawable;
	}

	public void setIconDrawable(Drawable iconDrawable) {
		this.iconDrawable = iconDrawable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public long getMemsize() {
		return memsize;
	}

	public void setMemsize(long memsize) {
		this.memsize = memsize;
	}

	public boolean isUserTask() {
		return userTask;
	}

	public void setUserTask(boolean userTask) {
		this.userTask = userTask;
	}

	@Override
	public String toString() {
		return "TaskInfo [iconDrawable=" + iconDrawable + ", name=" + name
				+ ", packagename=" + packagename + ", memsize=" + memsize
				+ ", userTask=" + userTask + "]";
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
