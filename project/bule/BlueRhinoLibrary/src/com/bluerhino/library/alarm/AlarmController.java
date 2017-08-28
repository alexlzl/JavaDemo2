package com.bluerhino.library.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * AlarmController
 * 
 * @author chowjee
 * @date 2014-7-3
 */
public class AlarmController {
		
	public static final String ACTION_ALARM = "com.bluerhion.library.ACTION_ALARM";
	
	private Context mContext;
	private PendingIntent mAlarmPendingIntent;
	private long mIntervalMillis;
	
	private AlarmController(Builder builder){
		this.mContext = builder.mContext;
		this.mAlarmPendingIntent = builder.mAlarmPendingIntent;
		this.mIntervalMillis = builder.mIntervalMillis;
	}

	public void startRepeatingAlarm(){		
        long firstTime = SystemClock.elapsedRealtime();        

        // Schedule the alarm!
		AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
				mIntervalMillis, mAlarmPendingIntent);
	}	
	
	public void stopRepeatingAlarm(){        
        // And cancel the alarm.
        AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(mAlarmPendingIntent);
	}
	
	public static class Builder {
		
		private static final long ALARM_TIEM_INTERVALMILLIS_DEFALUT = 60*1000;
		
		private Context mContext;
		private PendingIntent mAlarmPendingIntent;
		private long mIntervalMillis;
		
		public Builder(Context context){
			mContext = context;
			mAlarmPendingIntent = makeDefaultIntent();
			mIntervalMillis = ALARM_TIEM_INTERVALMILLIS_DEFALUT;
			
		}

		public final Builder setAlarmPendingIntent(PendingIntent mAlarmPendingIntent) {
			this.mAlarmPendingIntent = mAlarmPendingIntent;
			return this;
		}	

		public final Builder setIntervalMillis(long intervalMillis) {
			this.mIntervalMillis = intervalMillis;
			return this;
		}
		
		public AlarmController build (){
			return new AlarmController(this);
		}
		
		public PendingIntent makeDefaultIntent() {	
			if (null == mAlarmPendingIntent){
				Intent intent = new Intent(ACTION_ALARM);
				mAlarmPendingIntent = PendingIntent.getBroadcast(mContext,
						0, intent, 0);		
			}		
			return mAlarmPendingIntent;
		}		
	}
	
}
