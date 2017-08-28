package cn.bluerhino.driver.utils;

import java.util.Iterator;
import java.util.Stack;

import com.umeng.analytics.MobclickAgent;

import cn.bluerhino.driver.ApplicationController;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * Describe:Activity管理类
 * 
 * Date:2015-7-27
 * 
 * Author:liuzhouliang
 */
public class AppManager {
	private Context mContext;
	private static AppManager appManager = new AppManager();
	private static Stack<Activity> activityStack = new Stack<Activity>();;

	private AppManager() {
	}

	public static AppManager getAppManager() {

		return appManager;
	}

	/**
	 * 
	 * Describe:添加Activity到堆栈
	 * 
	 * Date:2015-7-27
	 * 
	 * Author:liuzhouliang
	 */
	public void addActivity(Activity activity) {

		activityStack.add(activity);
	}

	/**
	 * 
	 * Describe:获取当前Activity（堆栈中最后一个压入的）
	 * 
	 * Date:2015-7-27
	 * 
	 * Author:liuzhouliang
	 */
	public Activity currentActivity() {
		if (activityStack == null)
			return null;
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 
	 * Describe:结束当前Activity（堆栈中最后一个压入的）
	 * 
	 * Date:2015-7-27
	 * 
	 * Author:liuzhouliang
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 
	 * Describe:结束指定的Activity
	 * 
	 * Date:2015-7-27
	 * 
	 * Author:liuzhouliang
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 
	 * Describe:结束指定类名的Activity
	 * 
	 * Date:2015-7-27
	 * 
	 * Author:liuzhouliang
	 */
	public void finishActivity(Class<?> cls) {
		if (activityStack == null)
			return;
		Iterator<Activity> iterator = activityStack.iterator();
		while (iterator.hasNext()) {
			Activity activity = iterator.next();
			if (activity.getClass().equals(cls)) {
				iterator.remove();
				activity.finish();
				activity = null;
			}
		}

	}

	/**
	 * 
	 * Describe:结束所有Activity
	 * 
	 * Date:2015-7-27
	 * 
	 * Author:liuzhouliang
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				if (!activityStack.get(i).isFinishing()) {
					activityStack.get(i).finish();
				}

			}
		}
		activityStack.clear();
	}

	/**
	 * 
	 * Describe:判断某个类的Activity是否存在
	 * 
	 * Date:2015-7-27
	 * 
	 * Author:liuzhouliang
	 */
	public boolean isExistActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Describe:退出应用程序
	 * 
	 * Date:2015-7-27
	 * 
	 * Author:liuzhouliang
	 */
	public void exitApp(Context context) {
		mContext = context;
		try {
			SharedPreferencesUtil.writeSharedPreferencesString(mContext,
					"loginfo", "sessionID", "");
			finishAllActivity();
			ApplicationController.getInstance().setLoginStatus(false);
			// ActivityManager activityMgr = (ActivityManager) context
			// .getSystemService(Context.ACTIVITY_SERVICE);
			// activityMgr.restartPackage(context.getPackageName());
			MobclickAgent.onKillProcess(mContext);
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);

		} catch (Exception e) {
		}
	}

}