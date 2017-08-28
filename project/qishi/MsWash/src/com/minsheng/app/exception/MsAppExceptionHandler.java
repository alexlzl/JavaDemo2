package com.minsheng.app.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import com.minsheng.app.util.DeviceUtil;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.SharedPreferencesUtil;
import android.content.Context;

/**
 * 
 * 
 * @describe:捕获全局异常
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-3下午3:26:09
 * 
 */

public class MsAppExceptionHandler implements UncaughtExceptionHandler {

	private static MsAppExceptionHandler mAppExceptionHandler = null;
	private static Context mContext = null;

	private MsAppExceptionHandler() {
	}

	public static synchronized MsAppExceptionHandler getInstance(Context context) {
		mContext = context;
		if (null == mAppExceptionHandler) {
			mAppExceptionHandler = new MsAppExceptionHandler();
		}
		return mAppExceptionHandler;
	}

	@Override
	public void uncaughtException(Thread th, Throwable t) {
		String errorInfo = ExceptionUtil.getErrorInfo(t);
		String versionInfo = DeviceUtil.getVersionInfo(mContext);
		String mobileInfo = DeviceUtil.getMobileInfo();
		String errorContent = "\n" + "APP_VERSION=" + versionInfo + "\n"
				+ mobileInfo + "\n" + errorInfo;
		// 记录发生异常
		SharedPreferencesUtil.writeSharedPreferencesBoolean(mContext,
				SharedPreferencesUtil.APP_EXCEPTION_EXIST,
				SharedPreferencesUtil.APP_EXCEPTION_EXIST_KEY, true);
		ExceptionUtil.saveErrorInfo(mContext, errorContent);
		LogUtil.e("ExceptionHandler uncaughtException errorContent = "
				+ errorContent);
		System.exit(ExceptionUtil.EXCEPTION_STATE);
		android.os.Process.killProcess(android.os.Process.myPid());
		// restartApp();
	}

	/**
	 * 
	 * 
	 * @describe:程序进入时，上传日志信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午3:36:32
	 * 
	 */
	public static void uploadLogInfo(Context context) {
		boolean hasError = SharedPreferencesUtil.getSharedPreferencesBoolean(
				context, SharedPreferencesUtil.APP_EXCEPTION_EXIST,
				SharedPreferencesUtil.APP_EXCEPTION_EXIST_KEY, false);
		// String errorContent = ExceptionUtil.getSavedErrorInfo(context);

		if (hasError) {

		}
	}

}
