package cn.bluerhino.driver.exception;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.controller.activity.LoginActivity;
import cn.bluerhino.driver.utils.LogUtil;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

/**
 * 
 * Describe:全局异常处理
 * 
 * Date:2015-7-29
 * 
 * Author:liuzhouliang
 */
public class CrashHandler implements UncaughtExceptionHandler {

	private static final String TAG = CrashHandler.class.getName();
	private Thread.UncaughtExceptionHandler defaultHandler;
	private static CrashHandler instance = new CrashHandler();
	private Map<String, String> infos = new HashMap<String, String>();
	@SuppressLint("SimpleDateFormat")
	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	@SuppressWarnings("unused")
	private ApplicationController context;
	
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/brDriver/crash/";
    private static final String FILE_NAME_SUFFIX = ".log";

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		if (instance == null) {
			instance = new CrashHandler();
		}
		return instance;
	}

	public void init(ApplicationController ctx) {
		this.context = ctx;
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		handleException(throwable);
		defaultHandler.uncaughtException(thread, throwable);
	}

	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		if (ApplicationController.testSwitch) {
			writeCrashInfoToFile(ex);
		}
		restartApp();
		return true;
	}

	/**
	 * 
	 * Describe:获取设备信息
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
				infos.put("crashTime", formatter.format(new Date()));
			}
		} catch (NameNotFoundException e) {
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 
	 * Describe:异常日志保存
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */

	private void writeCrashInfoToFile(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			writeLog(sb.toString());
		}
	}

	private void writeLog(String log) {
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        long current = System.currentTimeMillis();
        String time = formatter.format(new Date(current));
        //以当前时间创建log文件
        File file = new File(PATH + time + FILE_NAME_SUFFIX);
		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(log);
			bw.newLine();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Describe:重启APP
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */
	private void restartApp() {
		LogUtil.d(TAG, "重启APP====");
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		// }
		Intent intent = new Intent(context.getApplicationContext(),
				LoginActivity.class);
		PendingIntent restartIntent = PendingIntent.getActivity(
				context.getApplicationContext(), 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		AlarmManager mgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
				restartIntent);
	}

}
