package com.minsheng.app.util;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 
 * 
 * @describe:工具类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:38:10
 * 
 */
public class ToolUtil {
	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public static boolean isAppOnForeground(Activity activity) {
		ActivityManager activityManager = (ActivityManager) activity
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = activity.getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (null != packageName
					&& null != appProcess
					&& packageName.equals(appProcess.processName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取metaData
	 * 
	 * @param context
	 * @param name
	 * @param def
	 * @return
	 */
	public static String getMetaDataValue(Context context, String name,
			String def) {
		String value = getMetaDataValue(context, name);
		return (value == null) ? def : value;
	}

	private static String getMetaDataValue(Context context, String name) {
		Object value = null;
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo;
		try {
			applicationInfo = packageManager.getApplicationInfo(
					context.getPackageName(), 128);
			if (applicationInfo != null && applicationInfo.metaData != null) {
				value = applicationInfo.metaData.get(name);
			}
		} catch (NameNotFoundException e) {
			LogUtil.e("ToolUtil getMetaDataValue Could not read the name in the manifest file e = "
					+ e);
		} catch (Exception e) {
			LogUtil.e("ToolUtil getMetaDataValue  e = " + e);
		}

		if (value == null) {
			LogUtil.e("ToolUtil getMetaDataValue name is not defined in the manifest file's meta data");
		}
		return value.toString();
	}

}
