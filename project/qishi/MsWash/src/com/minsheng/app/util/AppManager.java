package com.minsheng.app.util;

import java.util.Stack;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * 
 * 
 * @describe:Activity管理类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:14:01
 * 
 */
public class AppManager {

	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 
	 * 
	 * @describe:单例模式获取对象
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:15:58
	 * 
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 
	 * 
	 * @describe:添加Activity到堆栈
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:15:49
	 * 
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 
	 * 
	 * @describe:获取当前Activity（堆栈中最后一个压入的）
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:15:40
	 * 
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 
	 * 
	 * @describe:结束当前Activity（堆栈中最后一个压入的）
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:15:20
	 * 
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 
	 * 
	 * @describe:结束指定的Activity
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:15:06
	 * 
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
	 * 
	 * @describe:结束指定类名的Activity
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:14:50
	 * 
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 
	 * 
	 * @describe:结束所有Activity
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:14:40
	 * 
	 */
	public static void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}
	
	public boolean isExistActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @describe:退出应用程序
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:14:30
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
			// android.os.Process.killProcess(android.os.Process.myPid());
			// System.exit(1);

		} catch (Exception e) {
		}
	}
}