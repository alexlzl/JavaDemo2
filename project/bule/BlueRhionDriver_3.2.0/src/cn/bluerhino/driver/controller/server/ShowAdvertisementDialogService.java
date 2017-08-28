package cn.bluerhino.driver.controller.server;

import java.util.Timer;
import java.util.TimerTask;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.utils.AppRunningInfor;
import cn.bluerhino.driver.utils.LogUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.WindowManager;

/**
 * Describe:服务中显示对话框
 * 
 * Date:2015-7-22
 * 
 * Author:liuzhouliang
 */
@SuppressLint("HandlerLeak")
public class ShowAdvertisementDialogService extends Service {
	private static String TAG = ShowAdvertisementDialogService.class.getName();
	private Timer isAPPRunningForegroundTimer;
	private TimerTask isAPPRunningForegroundTimerTask;
	private Timer isShowLoadingViewTimer;
	private TimerTask isShowLoadingViewTimerTask;
	/**
	 * 处理显示广告窗口消息
	 */
	Handler showHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				/**
				 * 显示提示窗口
				 */
				showLoadingView();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startIsAppRunningForegroundTimer();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 
	 * Describe:启动定时任务，当APP后台运行超过一段时间后，进入APP会显示广告页面
	 * 
	 * Date:2015-7-21
	 * 
	 * Author:liuzhouliang
	 */
	private void startIsAppRunningForegroundTimer() {
		/**
		 * 定时任务检测APP是否在后台运行
		 */
		if (isAPPRunningForegroundTimer == null) {
			isAPPRunningForegroundTimer = new Timer();

		}
		if (isAPPRunningForegroundTimerTask == null) {
			isAPPRunningForegroundTimerTask = new TimerTask() {

				@Override
				public void run() {
					if (AppRunningInfor
							.isRunningForeground(ShowAdvertisementDialogService.this)) {
						/**
						 * 运行在前台
						 */
						LogUtil.d(TAG, "前台运行");
						ApplicationController.getInstance().isAPPRunningForeground = true;
						if (ApplicationController.getInstance().isShowLoadingView) {
							/**
							 * 展示广告页面
							 */
							LogUtil.d(TAG, "前台运行====显示广告页面");
							Message message = Message.obtain();
							message.what = 1001;
							showHandler.sendMessage(message);
							ApplicationController.getInstance().isShowLoadingView = false;
							if (isShowLoadingViewTimer != null) {
								isShowLoadingViewTimer.cancel();
								isShowLoadingViewTimer = null;
							}
							if (isShowLoadingViewTimerTask != null) {
								isShowLoadingViewTimerTask.cancel();
								isShowLoadingViewTimerTask = null;
							}

						}
					} else {
						/**
						 * 运行在后台
						 */
						LogUtil.d(TAG, "后台运行");
						ApplicationController.getInstance().isAPPRunningForeground = false;
						startShowLoadingViewTimer();
					}

				}
			};
		}
		if (isAPPRunningForegroundTimer != null
				&& isAPPRunningForegroundTimerTask != null) {
			isAPPRunningForegroundTimer.schedule(
					isAPPRunningForegroundTimerTask, 0, 1000);
		}

	}

	/**
	 * 
	 * Describe:启动定时任务，监听是否需要显示对话窗口
	 * 
	 * Date:2015-7-22
	 * 
	 * Author:liuzhouliang
	 */
	private void startShowLoadingViewTimer() {
		/**
		 * 定时任务检测APP是否满足显示广告页面的条件
		 */
		if (isShowLoadingViewTimer == null) {
			isShowLoadingViewTimer = new Timer();

		}
		if (isShowLoadingViewTimerTask == null) {
			isShowLoadingViewTimerTask = new TimerTask() {

				@Override
				public void run() {
					/**
					 * 标记为显示广告页面
					 */
					ApplicationController.getInstance().isShowLoadingView = true;
					LogUtil.d(TAG, "显示广告页面");
					isShowLoadingViewTimer.cancel();
					isShowLoadingViewTimerTask.cancel();
					isShowLoadingViewTimer = null;
					isShowLoadingViewTimerTask = null;
				}
			};
		}
		isShowLoadingViewTimer.schedule(isShowLoadingViewTimerTask, 0, 1000);
	}

	/**
	 * 
	 * Describe:展示广告页面
	 * 
	 * Date:2015-7-22
	 * 
	 * Author:liuzhouliang
	 */
	private void showLoadingView() {

		// ShowAdvertisementDialog.makeDialog(this, R.style.DialogTheme)
		// .showDialog();
		showBox(this);
	}

	private void showBox(final Context context) {
		LogUtil.d(TAG, "showLoadingView");
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("提示");
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage("完成次数: ");
		dialog.setPositiveButton("停止测试", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 点击后跳转到某个Activity

			}
		});
		AlertDialog mDialog = dialog.create();
		mDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);// 设定为系统级警告，关键
		mDialog.show();
	}
}
