package com.minsheng.app.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * 
 * 
 * @describe:获取手机的硬件信息
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:19:24
 * 
 */
public class DeviceUtil {
	/**
	 * 
	 * 
	 * @describe:判断应用是否已安装
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:19:39
	 * 
	 */
	public static boolean isAppInstalled(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @describe:获取手机的硬件信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:19:50
	 * 
	 */
	public static String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		// 通过反射获取系统的硬件信息
		try {
			sb.append("OCCUR_TIME"
					+ "="
					+ TimeUtil.changeTimeStempToString((int) (System
							.currentTimeMillis() / 1000)));
			sb.append("\n");
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					String name = field.getName();
					String value = field.get(null).toString();
					if ("TIME".equals(name)) {
						continue;
					}
					sb.append(name + "=" + value);
					sb.append("\n");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * 
	 * @describe:获取应用的版本信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:20:05
	 * 
	 */
	public static String getVersionInfo(Context context) {
		if (null == context) {
			return "";
		}
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * 
	 * @describe:获取设备唯一标识
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:20:17
	 * 
	 */
	public static String getDeviceUniqueId(Context context) {
		String deviceId = "";
		if (null != context) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			/**
			 * 唯一编号（IMEI, MEID, ESN, IMSI） 缺点 Android设备要具有电话功能 其工作不是很可靠 序列号
			 * 当其工作时，该值保留了设备的重置信息（“恢复出厂设置”），从而可以消除当客户删除自己设备上的信息，并把设备转另一个人时发生的错误。
			 */
			deviceId = tm.getDeviceId();
			if (!StringUtil.isEmpty(deviceId)) {
				LogUtil.d("DeviceUtil getDeviceUniqueId get deviceId");
				return deviceId;
			}

			deviceId = tm.getSubscriberId();
			if (!StringUtil.isEmpty(deviceId)) {
				LogUtil.d("DeviceUtil getDeviceUniqueId get subscriberId");
				return deviceId;
			}
		}

		// 序列号 缺点序列号无法在所有Android设备上使用
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			deviceId = (String) (get.invoke(c, "ro.serialno", "unknown"));
			if (!StringUtil.isEmpty(deviceId)) {
				LogUtil.d("DeviceUtil getDeviceUniqueId get serialno");
				return deviceId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * ANDROID_ID 缺点 对于Android 2.2（“Froyo”）之前的设备不是100％的可靠
		 * 此外，在主流制造商的畅销手机中至少存在一个众所周知的错误，每一个实例都具有相同的ANDROID_ID。
		 */
		if (null != context) {
			deviceId = Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.ANDROID_ID);
			if (!StringUtil.isEmpty(deviceId)) {
				LogUtil.d("DeviceUtil getDeviceUniqueId get androidId");
				return deviceId;
			}
		}

		if (StringUtil.isEmpty(deviceId)) {
			deviceId = System.currentTimeMillis() + "";
		}
		LogUtil.d("DeviceUtil getDeviceUniqueId get currentTimeMillis");
		return deviceId;

	}

}
