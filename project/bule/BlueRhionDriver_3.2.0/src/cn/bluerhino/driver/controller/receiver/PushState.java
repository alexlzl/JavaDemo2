package cn.bluerhino.driver.controller.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.PushInfo;

/**
 * 1. 语音 2.通知, 3.sendBroadcast 4.自定义行为
 */
public abstract class PushState {
	public static final String TAG = PushState.class.getName();
	public static final String KEY_ORDERID = "orderId";
	protected Context mContext;
	protected PushInfo mPushInfo;
	/**
	 * 订单类型的消息对应的订单号
	 */
	protected int mOrderNum;
	protected Resources mRes;
	private NotificationManager mNotificationManager;

	public PushState(Context context, PushInfo pushInfo, int orderNum) {
		this.mContext = context;
		this.mPushInfo = pushInfo;
		this.mOrderNum = orderNum;
		this.mRes = context.getResources();
		this.mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * 是否拦截推送并不处理消息
	 * 
	 * @return
	 */
	protected boolean isNeedBlock() {
		return false;
	}

	/**
	 * Describe:通知栏提示
	 * 
	 * Date:2015-7-8
	 * 
	 * Author:liuzhouliang
	 */
	public final void notifyPushMessage() {
		if (this.isNeedBlock())
			return;
		// 1语音
		if (this.isNeedVoice()) {
			if (this.isNeedRingtone()) {
				/**
				 * 先滴滴声再语音播报
				 */
				playVoice();
			} else {
				/**
				 * 直接语音播报
				 */
				ApplicationController.getInstance().getBaiduTts()
						.speak(getVoiceText(), mPushInfo);
			}
		}

		// 2发送广播
		if (this.isNeedNotification()) {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					mContext);

			if (getPendingIntent() != null) {
				builder.setContentIntent(this.getPendingIntent());
			}

			builder.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
					.setWhen(System.currentTimeMillis())
					.setContentTitle(getTitle()).setContentText(getMessage());
			if (!filterDevice()) {
				builder.setVibrate(new long[] { 0, 1000, 1000, 1000, 1000 });
			}
			Notification notification = builder.build();

			mNotificationManager.notify(mOrderNum, notification);

		}

		// 3. sendBroadcast
		Intent broadcastInt = this.getNotifyAction();
		if (broadcastInt != null) {
			ApplicationController.getInstance().getLocalBroadcastManager()
					.sendBroadcast(broadcastInt);
		}

		// 4.自定义行为
		this.customBehavior();
	}

	private boolean filterDevice() {
		String deviceNameString = Build.MODEL;
		deviceNameString = deviceNameString.replaceAll(" +", "");
		if ("OPPOT29".equals(deviceNameString)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Describe:播放语音提示
	 * 
	 * Date:2015-8-13
	 * 
	 * Author:liuzhouliang
	 */
	private void playVoice() {
		MediaPlayer mMediaPlayer = MediaPlayer.create(mContext, R.raw.sfx_success); //
		// 设置音频源
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置流类型
		mMediaPlayer.setLooping(false); // 设置是否循环播放
		mMediaPlayer.start(); // 开始播放
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
				ApplicationController.getInstance().getBaiduTts()
						.speak(getVoiceText(), mPushInfo);
			}
		});
		
		mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				mp.release();
				ApplicationController.getInstance().getBaiduTts()
				.speak(getVoiceText(), mPushInfo);
				return true;
			}
		});
	}

	/**
	 * 1.是否需要语音播放, 默认true
	 */
	protected boolean isNeedVoice() {
		return true;
	}

	/**
	 * 1 判断是否需要先播放'滴滴声音', 默认false (新订单来了，抢单成功，订单被取消，订单被改派提示滴滴声音)
	 */
	protected boolean isNeedRingtone() {
		return false;
	}

	/**
	 * 1获得语音播放内容
	 */
	protected String getVoiceText() {
		return "";
	}

	/**
	 * 2.通知: 是否需要通知, 默认true
	 */
	protected boolean isNeedNotification() {
		return true;
	}

	/**
	 * 2 获得标题
	 * 
	 * @return
	 */
	private String getTitle() {
		return mRes.getString(R.string.jpush_notification_title);
	}

	/**
	 * 2 获得通知正文内容, 如果isNeedNotification为false,则默认不需要配置此项
	 */
	protected String getMessage() {
		return "";
	}

	/**
	 * 2 配置PendingIntent, 如果isNeedNotification为false,则默认不需要配置此项
	 */
	protected PendingIntent getPendingIntent() {
		return null;
	}

	/**
	 * 3.发送广播
	 */
	protected abstract Intent getNotifyAction();

	/**
	 * 4.自定义行为, 启用请复写该方法
	 */
	protected void customBehavior() {
	}
}
