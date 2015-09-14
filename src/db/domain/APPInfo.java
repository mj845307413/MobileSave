package db.domain;

import android.R.string;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class APPInfo {
	/**
	 * 
	 * 
	 * h获取所有应用的信息
	 */
	private Drawable iconDrawable;
	private String name;
	private String packageString;
	private boolean inRom;
	private boolean userApp;
private int uid;
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

	public String getPackageString() {
		return packageString;
	}

	public void setPackageString(String packageString) {
		this.packageString = packageString;
	}

	public boolean isInRom() {
		return inRom;
	}

	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}

	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}

	@Override
	public String toString() {
		return "APPInfo [iconDrawable=" + iconDrawable + ", name=" + name
				+ ", packageString=" + packageString + ", inRom=" + inRom
				+ ", userApp=" + userApp + "]";
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

}
