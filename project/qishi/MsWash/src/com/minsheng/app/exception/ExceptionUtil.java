package com.minsheng.app.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import com.minsheng.app.util.SharedPreferencesUtil;
import android.content.Context;

/**
 * 
 * 
 * @describe:异常工具类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-3下午3:31:43
 * 
 */

public class ExceptionUtil {

	public static final int EXCEPTION_STATE = 0;
	public static final int EXCEPTION_REQUEST_CODE = 0;
	public static final int RESTART_PENDING_TIME = 1000;

	/**
	 * 
	 * 
	 * @describe:获取异常信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午3:39:59
	 * 
	 */
	public static String getErrorInfo(Throwable t) {
		if (null == t) {
			return "";
		}
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		if (null != pw) {
			t.printStackTrace(pw);
			pw.close();
		}

		String error = "";
		if (null != writer) {
			error = writer.toString();
		}
		return error;
	}

	/**
	 * 
	 * 
	 * @describe:获取上次保存的异常信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午3:39:34
	 * 
	 */
	public static String getSavedErrorInfo(Context context) {
		String lastErrorInfo = SharedPreferencesUtil
				.getSharedPreferencesString(context,
						SharedPreferencesUtil.APP_EXCEPTION,
						SharedPreferencesUtil.APP_EXCEPTION_KEY, "");
		return lastErrorInfo;
	}

	/**
	 * 
	 * 
	 * @describe:保存异常信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午3:38:52
	 * 
	 */
	public static void saveErrorInfo(Context context, String content) {
		if (null == context) {
			return;
		}

		SharedPreferencesUtil.writeSharedPreferencesString(context,
				SharedPreferencesUtil.APP_EXCEPTION,
				SharedPreferencesUtil.APP_EXCEPTION_KEY, content);
	}

	/**
	 * 
	 * 
	 * @describe:清空异常信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午3:39:14
	 * 
	 */
	public static void clearSavedErrorInfo(Context context) {
		if (null == context) {
			return;
		}
		SharedPreferencesUtil.ClearSharedPreferences(context,
				SharedPreferencesUtil.APP_EXCEPTION);
	}

}
