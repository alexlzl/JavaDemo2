package cn.bluerhino.driver.db.observer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import android.content.Context;
import android.os.SystemClock;
import cn.bluerhino.driver.db.CurrentOrderHelper;
import cn.bluerhino.driver.db.UnReadNotificationHelper;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.UnReadNotification;
import cn.bluerhino.driver.utils.AudioPlayer;

public class UnReadNotificationObserver extends SQLiteObserver<UnReadNotification> {

//	private static final String TAG = UnReadNotificationObserver.class.getSimpleName();

	private static final int NOTIFY_INTERVALMILLIS_DEFALUT = 10 * 1000;

	private final UnReadNotificationHelper mUnReadNotificationHelper;
	private final AudioPlayer mAudioPlayer;

	private WeakReference<CurrentOrderObserver> mWeakCurrentObserver;
	private List<OrderInfo> mOrerList;

	private ExecutorService mScheduledThreadPool = Executors.newSingleThreadExecutor();
	private Future<?> mNotifyTask;
	private boolean isCancelTask = false;

	public UnReadNotificationObserver(Context context, List<UnReadNotification> tList) {
		super(context, tList);
		mUnReadNotificationHelper = UnReadNotificationHelper.getInstance(context);
		mAudioPlayer = AudioPlayer.getInstance();

		mOrerList = new ArrayList<OrderInfo>();
		mWeakCurrentObserver = new WeakReference<CurrentOrderObserver>(new CurrentOrderObserver(
		        context, mOrerList) {
			@Override
			public List<OrderInfo> onChange() {
				List<OrderInfo> orderList = super.onChange();
				mUnReadNotificationHelper.notifyChanged();
				return orderList;
			}
		});

		registerCurrentObserver(context);
	}

	private void registerCurrentObserver(Context context) {
		CurrentOrderObserver observer = mWeakCurrentObserver.get();
		if (null != observer) {
			CurrentOrderHelper.getInstance(context).registerObserver(observer);
		}
	}

	@Override
	public List<UnReadNotification> onChange() {
		List<UnReadNotification> notificationList = mUnReadNotificationHelper.query();

		if (null != tList && null != notificationList) {
			tList.clear();
			tList.addAll(notificationList);
		}

		if (!tList.isEmpty()) {
			isCancelTask = false;
		}
		// Log.i(TAG, "unRead size = " + tList.size());

		checkUnReadNotificationMessage();

		return notificationList;
	}

	public void checkUnReadNotificationMessage(OrderInfo orderInfo) {
		for (UnReadNotification notification : tList) {
			if (orderInfo.getOrderNum() == notification.getOrderNum()
			        && orderInfo.getFlag() > notification.getFlag()) {
				notification.setFlag(0);
				mUnReadNotificationHelper.update(notification);
			}
		}
	}

	public void checkUnReadNotificationMessage() {
		for (UnReadNotification notification : tList) {
			for (OrderInfo orderInfo : mOrerList) {
				if (orderInfo.getOrderNum() == notification.getOrderNum()
				        && orderInfo.getFlag() > notification.getFlag()) {
					notification.setRead(0);
					mUnReadNotificationHelper.update(notification);
				}
			}
		}
		startUnReadNotification();
	}

	public void startUnReadNotification() {
		if (null != mNotifyTask && (!mNotifyTask.isCancelled() || !mNotifyTask.isDone())) {
			mNotifyTask.cancel(true);
		}
		mNotifyTask = mScheduledThreadPool.submit(mPlayerRunnable);
	}

	Runnable mPlayerRunnable = new Runnable() {
		@Override
		public void run() {
			while (!isCancelTask) {
				int unReadCount = tList.size();
				if (!mAudioPlayer.isPlaying() && unReadCount > 0) {
					// Log.d(TAG, "Play...");
//					mAudioPlayer.play(R.raw.notification, mContext);
					SystemClock.sleep(NOTIFY_INTERVALMILLIS_DEFALUT);
				} else {
					// Log.d(TAG, "Stop...");
					mAudioPlayer.stop();
					isCancelTask = true;
				}
			}
		}
	};
}
