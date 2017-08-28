package com.minsheng.app.module.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * 
 * @describe:开机广播，启动推送服务
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-3下午5:46:01
 * 
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			Intent startServiceIntent = new Intent(context, PushService.class);
			startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(startServiceIntent);
		}
	}
}
