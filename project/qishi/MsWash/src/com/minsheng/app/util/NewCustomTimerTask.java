package com.minsheng.app.util;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.minsheng.app.util.CustomTimerTask.TimeEvent;

/**
 * 
 * @describe:
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-28下午2:53:03
 * 
 */
public class NewCustomTimerTask {
	private TextView mTvTime;
	private long hours, minutes, seconds;
	// 计时器变化的间隔时间，毫秒
	private int intervalTime;
	// 需要倒计时的总时间，毫秒
	private long totalTaskTime;
	private CountDownTimer timer;

	private TimeEvent event;
	private int mposition;

	public interface TimeEvent {
		public void complete();
	}

	public NewCustomTimerTask(TextView tvTime, long totalTime, int interval,
			int position) {
		super();
		// TODO Auto-generated constructor stub
		mTvTime = tvTime;
		intervalTime = interval;
		totalTaskTime = totalTime;
		mposition = position;
	}

	public void setEvent(TimeEvent event) {
		this.event = event;
	}

	public void startTimer() {
		timer = new CountDownTimer(totalTaskTime, intervalTime) {
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				if (((Integer) mTvTime.getTag()) == mposition) {
					mTvTime.setText("00:00:00");
				}

				if (event != null) {
					event.complete();
				}
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if (((Integer) mTvTime.getTag()) == mposition) {
					// 获取小时值
					hours = millisUntilFinished / (60 * 60 * 1000);
					// 获取分值
					minutes = (millisUntilFinished - hours * (60 * 60 * 1000))
							/ (60 * 1000);
					// 获取秒值
					seconds = (millisUntilFinished - hours * (60 * 60 * 1000) - minutes
							* (60 * 1000)) / 1000;
					StringBuilder sb = new StringBuilder();
					if (hours < 10) {
						// tvHour.setText("0" + hours);
						sb.append("0" + hours + ":");
					} else
						// tvHour.setText(hours + "");
						sb.append(hours + ":");
					if (minutes < 10) {
						// tvMinute.setText("0" + minutes);
						sb.append("0" + minutes + ":");
					} else
						// tvMinute.setText(minutes + "");
						sb.append(minutes + ":");
					if (seconds < 10) {
						// tvSeconds.setText("0" + seconds);
						sb.append("0" + seconds + ":");
					} else
						// tvSeconds.setText(seconds + "");
						sb.append(seconds + "");
					mTvTime.setText(sb.toString());
				}

			}
		};
		timer.start();
	}

	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
}
