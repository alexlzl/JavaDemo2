package cn.bluerhino.driver.controller.receiver;

import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.controller.activity.MessageDialogActivity;
import cn.bluerhino.driver.model.ChangeOrderInfor;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.PushInfo;
import cn.bluerhino.driver.network.WaitOrderListRequest;
import cn.bluerhino.driver.utils.ChangeOrderTimer;
import cn.bluerhino.driver.utils.ConstantsManager;
import cn.bluerhino.driver.utils.DeviceUtil;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.view.CommonDialog;
import cn.bluerhino.driver.view.CommonDialog.OnDialogListener;
import cn.jpush.android.api.JPushInterface;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.network.framework.BRModelListRequest.BRModelListResponse;
import com.bluerhino.library.utils.WeakHandler;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = JPushReceiver.class.getName();
	/** 服务器推送消息, 如果该订单正在服务, 关闭该订单 */
	public static final String ACTION_NOTIFICATION_PULLREFRESH = "cn.bluerhno.notification_PULLREFRESH";
	/** 跳转到抢单页并刷新 */
	public static final String ACTION_WAITLIST_UPDATE = "cn.bluerhino.waitList_update";
	/** 当前订单页刷新 */
	public static final String ACTION_CURLIST_UPDATE = "cn.bluerhino.curList_update";
	/** 订单被改派 */
	public static final String ACTION_REARRANGED_ORDER = "cn.bluerhino.rearrange_order";
	public static final String EXTRA_ORDERNUM = "OrderNum";
	public static final String EXTRA_ACTION = "Action";
	public static final String EXTRA_MSG = "Msg";
	private static Context mContext;
	private static PushInfo mPushInfo;
	private PushMessageHandler messageHandler = new PushMessageHandler(this);;
	private static OrderInfo mOrderInfo;

	/**
	 * Describe:处理推送到的消息
	 * 
	 * Date:2015-7-13
	 * 
	 * Author:liuzhouliang
	 */
	private static class PushMessageHandler extends WeakHandler<JPushReceiver> {

		public PushMessageHandler(JPushReceiver reference) {
			super(reference);
		}

		@Override
		public void handleMessage(Message msg) {
			if (mOrderInfo == null) {
				return;
			}
			PushState pushState = PushStateContext.getPushState(mContext,
					mPushInfo, mOrderInfo);
			if (pushState != null) {
				pushState.notifyPushMessage();
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 接收到推送下来的自定义消息
		if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			mContext = context;
			Bundle bundle = intent.getExtras();
			processCustomMessage(context, bundle);
		}
	}

	/**
	 * Describe:处理推送消息
	 * 
	 * Date:2015-7-13
	 * 
	 * Author:liuzhouliang
	 */
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		LogUtil.d(TAG, message);
		try {
			mPushInfo = new Gson().fromJson(message, PushInfo.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return;
		}
		if (mPushInfo == null)
			return;

		// 获取消息类型
		String action = mPushInfo.getAction();
		LogUtil.d(TAG, action+"-----");
		if (PushStateContext.HASAVAILABLEORDER.equals(action)) {
			/**
			 * 需要抢的新订单， 抢单成功，判断是否可以正常抢单============
			 */
			handlePushOrder();
		} else if (PushStateContext.PUSHINFOR_PAGE_MESSAGE.equals(action)) {
			/**
			 * 二代协议，页面跳转推送
			 */
			startMessageDialogtActivity(mPushInfo.getMsg(),
					mPushInfo.getButton_name(), mPushInfo.getProtocol(),false);
		} else if (PushStateContext.SYSTEM_ALERT.equals(action)) {
			/**
			 * 系统警告=====
			 */
			startMessageDialogtActivity(mPushInfo.getMsg(),
					mPushInfo.getButton_name(), mPushInfo.getProtocol(),false);
		} else {
			if (PushStateContext.HASREARRANGEDORDER.equals(action)
					|| PushStateContext.HASCANCLEDORDER.equals(action)) {
				/**
				 * 订单被改派，订单被取消的推送消息，会在当前订单停留5分钟，之后被回收到历史订单中
				 */
				int orderChangeType = 0;
				if (PushStateContext.HASREARRANGEDORDER.equals(action)) {
					/**
					 * 订单改派
					 */
					orderChangeType = 2;
				} else if (PushStateContext.HASCANCLEDORDER.equals(action)) {
					/**
					 * 订单取消
					 */
					orderChangeType = 1;
				}
				ChangeOrderInfor changeOrderInfor = new ChangeOrderInfor();
				changeOrderInfor.setOrderChangeType(orderChangeType);
				changeOrderInfor.setOrderId(Integer.parseInt(mPushInfo
						.getOrderNum()));
				changeOrderInfor.setTime(System.currentTimeMillis() / 1000);
				if (ApplicationController.getInstance()
						.getChangeStateOrderList() != null) {
					if (ApplicationController.getInstance()
							.getChangeStateOrderList().size() == 0) {
						ChangeOrderTimer.getInstance().startTimer();
					}
					ApplicationController.getInstance()
							.getChangeStateOrderList().add(changeOrderInfor);
					LogUtil.d(TAG, "存储缓存订单信息"
							+ ApplicationController.getInstance()
									.getChangeStateOrderList().toString());
				}

				Message messageChange = Message.obtain();
				messageChange.what = 200003;
				Bundle dataBundle = new Bundle();
				dataBundle.putString("orderId", mPushInfo.getOrderNum());
				messageChange.setData(dataBundle);
				if (ApplicationController.getInstance().getGloablHandler() != null) {
					ApplicationController.getInstance().getGloablHandler()
							.sendMessage(messageChange);
				}
			}
			PushState pushState = PushStateContext.getPushState(mContext,
					mPushInfo, null);
			if (pushState != null) {
				pushState.notifyPushMessage();
			}
		}
		DeviceUtil.wakeUpAndUnlock(mContext);
	}

	/**
	 * Describe:打开消息提示页面,activity模式
	 * 
	 * Date:2015-7-8
	 * 
	 * Author:liuzhouliang
	 */
	private void startMessageDialogtActivity(String content, String actionType,
			String url,boolean isNeedPlayVoive) {
		Intent intent = new Intent(mContext, MessageDialogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		intent.putExtra(ConstantsManager.PUSH_MESSAGE_DIALOG_CONTENT, content);
		intent.putExtra(ConstantsManager.PUSH_MESSAGE_DIALOG_ACTION_TYPE,
				actionType);
		intent.putExtra(ConstantsManager.PUSH_MESSAGE_DIALOG_ACTION_URL, url);
		intent.putExtra(ConstantsManager.ISNEEDPLAYVOIVE, isNeedPlayVoive);
		mContext.startActivity(intent);
	}

	/**
	 * Describe:打开消息页面，dialog模式
	 * 
	 * Date:2015-7-8
	 * 
	 * Author:liuzhouliang
	 */
	@SuppressWarnings("unused")
	private void startMessageDialog() {
		CommonDialog dialog = CommonDialog
				.makeText(
						mContext,
						"司机师傅您好！ 您的车号正处于北京市限制行驶的规则内（本日尾号限行1/2），故无法收到订单，请您遵守当地交通法规，以避免不必要的损失",
						new OnDialogListener() {

							@Override
							public void onResult(int result,
									CommonDialog commonDialog) {
								if (OnDialogListener.LEFT == result) {
									commonDialog.dismiss();
								}
							}
						});
		dialog.setCancelable(false);
		dialog.setRightViewHide(true);
		dialog.setLeftText("我知道了");
		dialog.showDialog();
	}

	/**
	 * Describe:处理推送过来的订单信息
	 * 
	 * Date:2015-7-13
	 * 
	 * Author:liuzhouliang
	 */
	private void handlePushOrder() {
		// 使用通知传来的订单号
		String orderId = mPushInfo.getOrderNum();
		LogUtil.d(TAG, "推送orderId==" + orderId);
		// 获得订单号,并进入订单详情页
		BRRequestParams params = new BRRequestParams(ApplicationController
				.getInstance().getLoginfo().getSessionID());
		params.setDeviceId(ApplicationController.getInstance().getDeviceId());
		params.setVerCode(ApplicationController.getInstance().getVerCode());
		params.put(PushState.KEY_ORDERID, orderId);
		BRFastRequest request = new WaitOrderListRequest.Builder()
				.setSucceedListener(new BRModelListResponse<List<OrderInfo>>() {
					@Override
					public void onResponse(List<OrderInfo> modelList) {

						if (modelList != null && modelList.size() == 1) {
							LogUtil.d(TAG, "获取推送订单详情成功" + modelList.toString());
							mOrderInfo = modelList.get(0);
							messageHandler.sendEmptyMessage(0);
						}
					}
				}).setErrorListener(new VolleyErrorListener(mContext) {
					@Override
					public void onErrorResponse(VolleyError error) {
						LogUtil.d(TAG, "获取推送订单详情失败" + error.getMessage());
					}
				}).setParams(params).setRequestTag(TAG).build();
		ApplicationController.getInstance().getRequestQueue().add(request);
	}
}
