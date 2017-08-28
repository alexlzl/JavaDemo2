package com.minsheng.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * 
 * @describe:存储管理
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:30:52
 * 
 */

public class SharedPreferencesUtil {

	public static final String APP_EXCEPTION = "AppException";
	public static final String APP_EXCEPTION_KEY = "app_exception_key";
	public static final String APP_EXCEPTION_EXIST = "AppExceptionExist";
	public static final String APP_EXCEPTION_EXIST_KEY = "app_exception_exist_key";
	public static final String ADVERTISING_INFO_FILE_NAME = "advertising_info_file";// 宣传图文件名
	public static final String ADVERTISING_PICURL_KEY = "advertising_picurl";// 宣传图url
	// 小区ID
	public static final String COMMUNITY_ID_KEY = "community_id_key";
	public static final String COMMUNITY_ID_SP = "community_id_sp";
	public static final String COMMUNITY_NAME_SP = "community_name_sp";
	public static final String COMMUNITY_NAME_KEY = "community_name_key";
	public static final String COMMUNITY_AREA_SP = "community_area_sp";
	public static final String COMMUNITY_AREA_KEY = "community_area_key";

	// 城市ID
	public static final String CITY_ID_SP = "city_id_sp";
	public static final String CITY_ID_KEY = "city_id_key";
	// 登陆Token
	public static final String LOGIN_TOKEN_SP = "login_token_sp";
	public static final String LOGIN_TOKEN_KEY = "login_token_key";
	// 定位城市
	public static final String LOCATION_CITY_SP = "location_city_sp";
	public static final String LOCATION_CITY_KEY = "location_city_key";
	/**
	 * 应用是否为打开状态
	 */
	public static final String ISAPPCLOSED_INFO_FILE_NAME = "isappopened_info";
	public static final String ISAPPCLOSED_KEY = "isappopened";
	/**
	 * 推送开关
	 */
	public static final String PUSH_ON_OFF = "push_on_off";
	/**
	 * 推送状态标识 默认为true首次启动
	 */
	public static final String PUSH_STATE = "push_state";
	public static final String PUSH_INFO_FILE_NAME = "push_info";

	public static void writeSharedPreferencesBoolean(Context context,
			String spName, String key, boolean value) {
		if (null == context || null == spName || null == key) {
			return;
		}
		SharedPreferences user = context.getSharedPreferences(spName,
				Context.MODE_PRIVATE);
		Editor editor = user.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getSharedPreferencesBoolean(Context context,
			String spName, String key, boolean defValue) {
		if (null == context || null == spName || null == key) {
			return defValue;
		}
		SharedPreferences user = context.getSharedPreferences(spName,
				Context.MODE_PRIVATE);
		return user.getBoolean(key, defValue);
	}

	public static void writeSharedPreferencesString(Context context,
			String spName, String key, String value) {
		if (null == context || null == spName || null == key) {
			return;
		}
		SharedPreferences user = context.getSharedPreferences(spName,
				Context.MODE_PRIVATE);
		Editor editor = user.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getSharedPreferencesString(Context context,
			String spName, String key, String defValue) {
		if (null == context || null == spName || null == key) {
			return defValue;
		}
		SharedPreferences user = context.getSharedPreferences(spName,
				Context.MODE_PRIVATE);
		return user.getString(key, defValue);
	}

	public static void writeSharedPreferencesInt(Context context,
			String spName, String key, int value) {
		if (null == context || null == spName || null == key) {
			return;
		}
		SharedPreferences user = context.getSharedPreferences(spName,
				Context.MODE_PRIVATE);
		Editor editor = user.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void writeSharedPreferencesDouble(Context context,
			String spName, String key, double value) {
		if (null == context || null == spName || null == key) {
			return;
		}
		SharedPreferences user = context.getSharedPreferences(spName,
				Context.MODE_PRIVATE);
		Editor editor = user.edit();
		editor.putFloat(key, (float) value);
		editor.commit();
	}

	public static double getSharedPreferencesDouble(Context context,
			String spName, String key, double value) {
		if (null == context || null == spName || null == key) {
			return (float) value;
		}
		SharedPreferences user = context.getSharedPreferences(spName,
				Context.MODE_PRIVATE);
		return user.getFloat(key, (float) value);
	}

	public static int getSharedPreferencesInt(Context context, String spName,
			String key, int value) {
		if (null == context || null == spName || null == key) {
			return value;
		}
		SharedPreferences user = context.getSharedPreferences(spName,
				Context.MODE_PRIVATE);
		return user.getInt(key, value);
	}

	public static void ClearSharedPreferences(Context context, String spName) {
		if (null == context || null == spName) {
			return;
		}
		SharedPreferences user = context.getSharedPreferences(spName,
				Context.MODE_PRIVATE);
		Editor editor = user.edit();
		editor.clear();
		editor.commit();
	}

}
