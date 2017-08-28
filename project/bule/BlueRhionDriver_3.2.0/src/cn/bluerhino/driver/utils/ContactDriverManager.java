package cn.bluerhino.driver.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class ContactDriverManager {

	private static final Intent DRIVER_MANAGER_INTENT = new Intent(Intent.ACTION_CALL,
	        Uri.parse("tel:4006785966"));

	private static final ContactDriverManager INSTANCE = new ContactDriverManager();

	private ContactDriverManager() {
		super();
		DRIVER_MANAGER_INTENT.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public synchronized static ContactDriverManager getIntance() {
		return INSTANCE;
	}

	public void contactDriverManager(Context context) {
		context.startActivity(DRIVER_MANAGER_INTENT);
	}
	
}
