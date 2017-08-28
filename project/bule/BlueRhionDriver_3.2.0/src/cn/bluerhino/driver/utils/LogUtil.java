package cn.bluerhino.driver.utils;

import cn.bluerhino.driver.ApplicationController;
import android.util.Log;

/**
 * 日志工具类
 * 
 * @author Administrator
 * 
 */

public class LogUtil {
    private static boolean bLog = ApplicationController.testSwitch ? true
            : false;
    private static final String TAG = "br_log";

    public static void d(String msg) {

        if (bLog) {

            Log.d(TAG, msg);

        }

    }

    public static void d(String Tag, String msg) {

        if (bLog) {

            Log.d(Tag, msg);

        }

    }

    public static void e(String Tag, String msg) {

        if (bLog) {

            Log.d(Tag, msg);

        }

    }

    public static void i(String msg) {

        if (bLog) {

            Log.i(TAG, msg);

        }

    }

    public static void e(String msg) {

        if (bLog) {

            Log.e(TAG, msg);

        }

    }

    public static void v(String msg) {

        if (bLog) {

            Log.v(TAG, msg);

        }

    }

    public static void w(String msg) {

        if (bLog) {

            Log.w(TAG, msg);

        }

    }

}
