package com.bluerhino.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

public class UserAgentUtils {

	public static String getUserAgent(Context context) {
		String userAgent = "";
		String packageName = context.getPackageName();
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(packageName, 0);
			String model = Build.MODEL;
			String version_release = Build.VERSION.RELEASE;
			userAgent = String.format("%s/%s/%s/%s", packageName, info.versionCode, model,
			        version_release);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return userAgent;
	}

}
