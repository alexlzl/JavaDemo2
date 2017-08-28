package com.minsheng.app.util;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 
 * 
 * @describe:倒计时功能
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:17:11
 * 
 */
public class CustomTimerTask {
	private TextView tvHour, tvMinute, tvSeconds;
	private long hours, minutes, seconds;
	// 计时器变化的间隔时间，毫秒
	private int intervalTime;
	// 需要倒计时的总时间，毫秒
	private long totalTaskTime;
	private CountDownTimer timer;
	
	private TimeEvent event;
	
	public interface TimeEvent{
		public void complete();
	}

	public CustomTimerTask(TextView hour, TextView minute, TextView seconds,
			long totalTime, int interval) {
		super();
		// TODO Auto-generated constructor stub
		tvHour = hour;
		tvMinute = minute;
		tvSeconds = seconds;
		intervalTime = interval;
		totalTaskTime = totalTime;
	}
	
	public void setEvent(TimeEvent event) {
		this.event = event;
	}

	public void startTimer() {
		timer = new CountDownTimer(totalTaskTime, intervalTime) {
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				tvHour.setText("00");
				tvMinute.setText("00");
				tvSeconds.setText("00");
				if(event!=null){
					event.complete();
				}
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				// 获取小时值
				hours = millisUntilFinished / (60 * 60 * 1000);
				// 获取分值
				minutes = (millisUntilFinished - hours * (60 * 60 * 1000))
						/ (60 * 1000);
				// 获取秒值
				seconds = (millisUntilFinished - hours * (60 * 60 * 1000) - minutes
						* (60 * 1000)) / 1000;
				if (hours < 10) {
					tvHour.setText("0" + hours);
				} else
					tvHour.setText(hours + "");
				if (minutes < 10) {
					tvMinute.setText("0" + minutes);
				} else
					tvMinute.setText(minutes + "");
				if (seconds < 10) {
					tvSeconds.setText("0" + seconds);
				} else
					tvSeconds.setText(seconds + "");
			}
		};
		timer.start();
	}
	
	public void stopTimer(){
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
}
