package cn.bluerhino.driver.utils;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.os.Message;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.model.ChangeOrderInfor;

/**
 * Describe:倒计时处理订单状态改变的订单
 * 
 * Date:2015-9-8
 * 
 * Author:liuzhouliang
 */
public class ChangeOrderTimer {
	private static String TAG = ChangeOrderTimer.class.getName();
	private static ChangeOrderTimer changeOrderTimer = new ChangeOrderTimer();
	private Timer mTimer;
	private TimerTask mTask;
	// 订单缓存时间，单位秒
	private static final int CACHE_TIME = 30;

	private ChangeOrderTimer() {

	}

	public static ChangeOrderTimer getInstance() {
		if (changeOrderTimer == null) {
			changeOrderTimer = new ChangeOrderTimer();
		}

		return changeOrderTimer;

	}

	/**
	 * 
	 * Describe:启动倒计时
	 * 
	 * Date:2015-9-8
	 * 
	 * Author:liuzhouliang
	 */
	public void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTask == null) {
			mTask = new TimerTask() {

				@Override
				public void run() {
					updateTime();
				}
			};
			if (mTimer != null && mTask != null) {
				mTimer.schedule(mTask, 1000, 1000 * 2);
			}

		}
	}

	/**
	 * 
	 * Describe:更新缓存时间
	 * 
	 * Date:2015-9-8
	 * 
	 * Author:liuzhouliang
	 */
	private void updateTime() {
		LogUtil.d(TAG, "更新倒计时");
		if (ApplicationController.getInstance().getChangeStateOrderList() != null
				&& ApplicationController.getInstance()
						.getChangeStateOrderList().size() > 0) {
			Iterator<ChangeOrderInfor> iterator = ApplicationController
					.getInstance().getChangeStateOrderList().iterator();
			if (iterator.hasNext()) {
				ChangeOrderInfor changeOrderInfor = iterator.next();
				long starttime = changeOrderInfor.getTime();
				long currentTime = System.currentTimeMillis() / 1000;
				LogUtil.d(TAG, "orderid==" + changeOrderInfor.getOrderId()
						+ "倒计时差==" + (currentTime - starttime));
				if ((currentTime - starttime) > CACHE_TIME) {
					iterator.remove();
					/**
					 * 移除已经超过缓存期限的订单，通知UI刷新
					 */
					sendUpdateMessage(changeOrderInfor.getOrderId());
					return;
				}

			}

		}

	}

	/**
	 * 
	 * Describe:发送更新页面的消息
	 * 
	 * Date:2015-9-8
	 * 
	 * Author:liuzhouliang
	 */
	private void sendUpdateMessage(int orderId) {
		Message messageChange = Message.obtain();
		messageChange.what = 200001;
		Bundle dataBundle = new Bundle();
		dataBundle.putInt("orderId", orderId);
		messageChange.setData(dataBundle);
		if (ApplicationController.getInstance().getGloablHandler() != null) {
			ApplicationController.getInstance().getGloablHandler()
					.sendMessage(messageChange);
		}
	}

}
