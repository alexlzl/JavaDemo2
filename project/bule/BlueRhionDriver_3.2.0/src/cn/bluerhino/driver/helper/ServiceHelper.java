package cn.bluerhino.driver.helper;

import java.util.Set;
import com.bluerhino.library.utils.ToastUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.controller.activity.LoginActivity;
import cn.bluerhino.driver.controller.service.LocationService;
import cn.bluerhino.driver.utils.ConstantsManager;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.bluerhino.driver.utils.UploadErrorMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class ServiceHelper {
	private static final String TAG = ServiceHelper.class.getName();
	private static Intent LocService = null;
	private static Context mContext;
	private static final int MSG_SET_ALIAS = 1001;
	private static int count;

	/**
	 * 初始化极光推送服务相关
	 * 
	 * @param context
	 */
	public static void initJPushService(Context context, String alias,
			boolean isDebug) {
		JPushInterface.setDebugMode(isDebug);
		JPushInterface.init(context);
		mContext = context;
		if (StringUtil.isEmpty(alias)) {
			Toast.makeText(context, "注册推送失败，请重新登录", Toast.LENGTH_SHORT).show();
			return;
		}

		// 调用 Handler 来异步设置别名
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	private final static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				LogUtil.d(TAG, "Set alias in handler.");
				// 调用 JPush 接口来设置别名。
				JPushInterface.setAliasAndTags(mContext, (String) msg.obj,
						null, mAliasCallback);
				break;
			default:
				LogUtil.d(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};
	private final static TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				count = 0;
				logs = "Set tag and alias success";
				LogUtil.d(TAG, logs);
				if (ApplicationController.testSwitch) {
					ToastUtil.showToast(mContext, "注册推送成功" + "code==" + code
							+ "alias==" + alias, 1000);
				} else {
					ToastUtil.showToast(mContext, "注册推送成功", 1000);
				}
				break;
			case 6002:
				// 将推送的错误发送到服务端
				if (count == 0) {
					new UploadErrorMessage().uploadErrorMessage(
							ConstantsManager.PUSH_ERROR, alias
									+ "pushErrorCode" + code);
				}
				if (count == 3 || count > 3) {
					count = 0;
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				} else {
					count++;
					logs = "Failed to set alias and tags due to timeout. Try again after 5s.";
					LogUtil.d(TAG, logs + "次数==" + count);
					if (ApplicationController.testSwitch) {
						ToastUtil.showToast(mContext, "注册推送失败，请重新登录" + "code=="
								+ code + "alias==" + alias, 1000);
					} else {
						ToastUtil.showToast(mContext, "注册推送失败，请重新登录", 1000);
					}
					// 延迟 5 秒来调用 Handler 设置别名
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 3);
				}
				break;
			default:
				// 将推送的错误发送到服务端
				if (count == 0) {
					new UploadErrorMessage().uploadErrorMessage(
							ConstantsManager.PUSH_ERROR, alias
									+ "pushErrorCode" + code);
				}
				if (count == 3 || count > 3) {
					count = 0;
					mContext.startActivity(new Intent(mContext,
							LoginActivity.class));
				} else {
					count++;
					logs = "Failed with errorCode = " + code;
					LogUtil.e(TAG, logs);
					// 延迟 5 秒来调用 Handler 设置别名
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 3);
				}

			}
		}
	};

	/**
	 * 开启定位
	 * 
	 * @param context
	 */
	public static void startLoc(Context context) {
		if (LocService == null) {
			LocService = new Intent(context, LocationService.class);
		}
		LocService.setAction(LocationService.ACTION_START_LOCATION);
		context.startService(LocService);
	}

	/**
	 * 停止定位
	 * 
	 * @param context
	 */
	public static void stopLoc(Context context) {
		if (LocService == null) {
			LocService = new Intent(context, LocationService.class);
		}
		LocService.setAction(LocationService.ACTION_STOP_LOCATION);
		context.startService(LocService);
	}
}
