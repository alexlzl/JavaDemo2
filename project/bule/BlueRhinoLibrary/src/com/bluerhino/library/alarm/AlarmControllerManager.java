package com.bluerhino.library.alarm;

import android.app.PendingIntent;
import android.content.Context;

public class AlarmControllerManager {

	private volatile static AlarmController singleton;

	private AlarmControllerManager() {
	}

	public static AlarmController getAlarmController(Context context) {
		if (singleton == null) {
			synchronized (AlarmController.class) {
				if (singleton == null) {
					singleton = new AlarmController.Builder(context).build();
				}
			}
		}
		return singleton;
	}
	
	public static AlarmController getAlarmController(Context context,PendingIntent pendingIntent) {
		if (singleton == null) {
			synchronized (AlarmController.class) {
				if (singleton == null) {
					singleton = new AlarmController
							.Builder(context)
							.setAlarmPendingIntent(pendingIntent)
							.build();
				}
			}
		}
		return singleton;
	}
	
	public static AlarmController getAlarmController(Context context,PendingIntent pendingIntent,long intervalMillis) {
		if (singleton == null) {
			synchronized (AlarmController.class) {
				if (singleton == null) {
					singleton = new AlarmController
							.Builder(context)
							.setAlarmPendingIntent(pendingIntent)
							.setIntervalMillis(intervalMillis)
							.build();
				}
			}
		}
		return singleton;
	}

}
