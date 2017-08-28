package com.bluerhino.library.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

/**
 * 
 * 
 * @author chowjee
 * @date 2014-7-3
 */
public class RunningActivityInfo {
	
	private Context mContext;
		
	public RunningActivityInfo(Context context) {
	    this.mContext = context;
    }

	public boolean isHome() {  
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);  
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);  
        return getHomes().contains(rti.get(0).topActivity.getPackageName());  
    }  
  
    public List<String> getHomes() {  
        List<String> names = new ArrayList<String>();  
        PackageManager packageManager = mContext.getPackageManager();  
        Intent intent = new Intent(Intent.ACTION_MAIN);  
        intent.addCategory(Intent.CATEGORY_HOME);  
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,  
                PackageManager.MATCH_DEFAULT_ONLY);  
        for (ResolveInfo ri : resolveInfo) {  
            names.add(ri.activityInfo.packageName);  
        }  
        return names;  
    } 
	
}
