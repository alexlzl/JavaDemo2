package cn.bluerhino.driver.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import cn.bluerhino.driver.utils.SharedPreferencesUtil;
import android.content.Context;

/**
 * 异常工具类
 * 
 * @author Administrator
 * 
 */

public class ExceptionUtil {

    public static final int EXCEPTION_STATE = 0;
    public static final int EXCEPTION_REQUEST_CODE = 0;
    public static final int RESTART_PENDING_TIME = 1000;

    /**
     * 
     * Describe:获取异常信息
     * 
     * Date:2015-6-24
     * 
     * Author:liuzhouliang
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
     * Describe:获取上次保存的异常信息
     * 
     * Date:2015-6-24
     * 
     * Author:liuzhouliang
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
     * Describe:保存异常信息
     * 
     * Date:2015-6-24
     * 
     * Author:liuzhouliang
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
     * Describe:清空异常信息
     * 
     * Date:2015-6-24
     * 
     * Author:liuzhouliang
     */
    public static void clearSavedErrorInfo(Context context) {
        if (null == context) {
            return;
        }
        SharedPreferencesUtil.ClearSharedPreferences(context,
                SharedPreferencesUtil.APP_EXCEPTION);
    }

}
