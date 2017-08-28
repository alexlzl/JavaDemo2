package cn.bluerhino.driver.model.datasource.orderflow;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRURL;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.OrderFlowActivity;
import cn.bluerhino.driver.model.DeliverInfo;
import cn.bluerhino.driver.model.LocationInfo;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.utils.AppRunningInfor;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.view.LoadingDialog;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bluerhino.library.network.framework.BRErrorListener;
import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import com.bluerhino.library.network.framework.BRJsonRequest.JsonBuilder;
import com.bluerhino.library.network.framework.BRRequestHead;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.network.framework.BRResponseError;
import com.bluerhino.library.utils.ToastUtil;
import com.bluerhino.library.utils.UserAgentUtils;
import com.bluerhino.library.utils.WeakHandler;

/**
 * 
 * Describe:抽象订单层
 * 
 * Date:2015-6-26
 * 
 * Author:liuzhouliang
 */
public abstract class OrderState implements OnClickListener {

	protected static final String TAG = OrderState.class.getSimpleName();
	protected static final String EXCUTEORDER = BRURL.EXECUTE_ORDER_POST_URL;
	//支付金额
	protected static final String PAYORDER = BRURL.ORDERPAY_INFO_POST_URL;
	protected static final String REQUEST_TAG = OrderFlowActivity.class
			.getSimpleName();
	protected static OrderStateMachineContext mMachineContext;
	protected String mNormalNoticeFormat;
	protected String mUnfinishFormat;
	protected String mAgaindepartureFormat;
	protected String mPlayInfoFormatForDeliver;
	protected String mPlayInfoFormatForPickup;
	protected JsonBuilder mBuilder;
	protected AlertDialog mExecuteDialog;
	protected static Context mContext;
	private LoadingDialog mLoadingDialog;
	private DialogPositiveClickListener mPositiveClick;
	private CancleClickListener mCancleClickListener;
	private static final int DELAY_TIME = 1000;
	// 即使订单，模拟司机出发任务
	private Timer immediatelyDriverStartTimer;
	private TimerTask immediatelyDriverStartTimerTask;
	// 预约订单司机出发模拟任务
	private Timer appointOrderTimer;
	private TimerTask appointOrderTimerTask;
	// 从发货地离开模拟任务
	private Timer deliverStartTimer;
	private TimerTask deliverStartTimerTask;
	// 从收货地出发模拟任务
	private Timer takeDeliverStartTimer;
	private TimerTask takeDeliverStartTimerTask;
	// 到达收货地模拟任务
	private Timer arriveTakeDeliverTimer;
	private TimerTask arriveTakeDeliverTimerTask;
	// 模拟预约订单，响应服务任务
	private Timer appointResponseServerTimer;
	private TimerTask appointResponseServerTimerTask;
	LatLng secondLocation = null;
	String executeDialogMessage;
	private StateMessageHandler messageHandler;
	long startTime = 0;
	long endTime = 0;
	LatLng locationOneLatLng;
	byte[] obj = new byte[] {};
	private View currentView;

	/**
	 * for test=====
	 */
	protected static class StateMessageHandler extends WeakHandler<OrderState> {

		public StateMessageHandler(OrderState reference) {
			super(reference);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				Bundle dataBundle1001 = msg.getData();
				String contentString1001 = dataBundle1001.getString("1001");
				ToastUtil.showToast(mContext, "实时订单，模拟司机出发距离=="
						+ contentString1001, 100);
				break;
			case 2002:
				Bundle dataBundle2002 = msg.getData();
				String contentString2002 = dataBundle2002.getString("2002");
				ToastUtil.showToast(mContext, "司机距离发货地距离" + contentString2002,
						100);
				break;
			case 2003:
				Bundle dataBundle2003 = msg.getData();
				String contentString2003 = dataBundle2003.getString("2003");
				ToastUtil.showToast(mContext, "判断司机是否静止" + contentString2003,
						100);
				break;
			case 2004:
				Bundle dataBundle2004 = msg.getData();
				String contentString2004 = dataBundle2004.getString("2004");
				ToastUtil.showToast(mContext, "司机距离收货地距离" + contentString2004,
						100);
				break;
			case 2005:
				Bundle dataBundle2005 = msg.getData();
				String contentString2005 = dataBundle2005.getString("2005");
				ToastUtil.showToast(mContext, contentString2005, 100);
				break;
			case 2006:
				Bundle dataBundle2006 = msg.getData();
				String contentString2006 = dataBundle2006.getString("2006");
				ToastUtil.showToast(mContext, contentString2006, 100);
				break;

			case 2007:
				if (!AppRunningInfor.isAPPRunningForeground(
						mContext.getPackageName(), mContext)) {
					/**
					 * 运行在后台
					 */
				} else {
					/**
					 * 运行前台
					 */
					if (AppRunningInfor
							.isActivityRunningForeground(mContext,
									"cn.bluerhino.driver.controller.activity.OrderFlowActivity")) {
						OrderState orderState = getReference();
						if (orderState != null) {
							orderState.mLoadingDialog.show();
						}

					}

				}

				break;
			default:
				break;
			}
		}

	}

	protected interface DialogPositiveClickListener {
		public void onClick();
	}

	public interface CancleClickListener {
		public void onCancleClick();
	}

	protected void setCancleListener(CancleClickListener listener) {
		mCancleClickListener = listener;
	}

	/**
	 * 默认实现确认响应事件监听接口：执行更新订单状态
	 */
	private final DialogPositiveClickListener DEFAULT_POSITIVE_CLICK = new DialogPositiveClickListener() {
		@Override
		public void onClick() {
			/**
			 * 更新到下一个订单状态
			 */
			executeRequest();
		}
	};

	public OrderState(OrderStateMachineContext machineContext) {
		mMachineContext = machineContext;

		create();
	}

	private void create() {
		messageHandler = new StateMessageHandler(this);
		mContext = mMachineContext.getContext();
		mLoadingDialog = new LoadingDialog(mContext);
		createExecuteMessageFormat(mContext);
		BRRequestHead head = new BRRequestHead();
		head.put("PhoneAgent", UserAgentUtils.getUserAgent(mContext));
		mBuilder = (JsonBuilder) new JsonBuilder()
				.setSucceedListener(SUCCEED_LISTENER)
				.setErrorListener(ERROR_LISTENER).setRequestTag(REQUEST_TAG)
				.setHeaders(head).setUrl(getUrl()).setParams(getParams());
		mPositiveClick = DEFAULT_POSITIVE_CLICK;
		/**
		 * 对话框，确定响应事件
		 */
		DialogInterface.OnClickListener positiveClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mPositiveClick.onClick();
			}
		};
		/**
		 * 取消事件
		 */
		DialogInterface.OnClickListener cancleClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mCancleClickListener != null) {
					mCancleClickListener.onCancleClick();
				}

			}

		};
		/**
		 * 点击响应窗口
		 */
		mExecuteDialog = new AlertDialog.Builder(mContext)
				.setPositiveButton("确定", positiveClick)
				.setNegativeButton("取消", cancleClick).create();

		setOnClickListener(this);

		Log.d(TAG, "create=当前已经完成状态==" + getValue() + "下一个订单状态"
				+ getNextValue() + "订单信息0"
				+ mMachineContext.getOrderInfo().toString());
		updateOrderState(getValue());
	}

	/**
	 * 
	 * Describe:初始化消息提示资源
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	private void createExecuteMessageFormat(Context context) {
		Resources res = context.getResources();
		// 你确定%s吗?
		mNormalNoticeFormat = res.getString(R.string.order_deal_normal_nitice);
		// 请注意：此订单预计%1$d个收货地，您已服务 %2$d个收货地，
		// 现在完成，行程与订单不符，是客户要求变更吗?变更后，将以实际行驶里程计费，请予客户知晓。
		mUnfinishFormat = res.getString(R.string.order_deal_unfinish_deliver);
		// 请注意：此订单只有 %1$d 个收货地，
		// 再次出发，行程与订单不符，是客户要求变更吗?变更后，将以实际行驶里程计费，请予客户知晓。
		mAgaindepartureFormat = res
				.getString(R.string.order_deal_againdeparture);
		// 请您注意：该订单需要向收货人收款%1$s元。收款后才可以进行下一步操作。现在去收款吗?
		mPlayInfoFormatForDeliver = res
				.getString(R.string.order_deal_payinfo_deliver);
		// 请您注意：该订单需要向发货人收款%1$s元。收款后才可以进行下一步操作。现在去收款吗?
		mPlayInfoFormatForPickup = res
				.getString(R.string.order_deal_payinfo_pickup);
	}

	/**
	 * 
	 * Describe:设置确认按钮监听
	 * 
	 * Date:2015-7-3
	 * 
	 * Author:liuzhouliang
	 */
	protected void setDialogPositiveListener(
			DialogPositiveClickListener clickListener) {
		mPositiveClick = clickListener;
	}

	/**
	 * 
	 * Describe:设置对话框确定操作，默认响应为执行新订单状态请求
	 * 
	 * Date:2015-7-7
	 * 
	 * Author:liuzhouliang
	 */
	protected void setDialogPositiveDefalutListener() {
		setDialogPositiveListener(DEFAULT_POSITIVE_CLICK);
	}

	public OrderInfo getOrderInfo() {
		return mMachineContext.getOrderInfo();
	}

	protected Button getLeftTab() {
		return mMachineContext.getLeftTab();
	}

	protected Button getRightTab() {
		return mMachineContext.getRightTab();
	}

	/**
	 * 
	 * Describe:获取等待时间视图
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	protected View getWaitTime() {
		return mMachineContext.getWaitTimeView();
	}

	/**
	 * 
	 * Describe:获取订单状态显示视图
	 * 
	 * Date:2015-7-1
	 * 
	 * Author:liuzhouliang
	 */
	protected static TextView getOrderStateTv() {
		return mMachineContext.getOrderStateTv();
	}

	/**
	 * 
	 * Describe:获取显示订单状态父视图
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	protected static LinearLayout getOrderStateParent() {
		return mMachineContext.getOrderStateParentView();
	}

	/**
	 * 
	 * Describe:设置左边，右边控件监听事件
	 * 
	 * Date:2015-6-26
	 * 
	 * Author:liuzhouliang
	 */
	protected void setOnClickListener(OnClickListener clickListener) {
		getLeftTab().setOnClickListener(clickListener);
		getRightTab().setOnClickListener(clickListener);
	}

	@Override
	public void onClick(View v) {
		currentView = v;
		showExecuteDialog();
	}

	/**
	 * 
	 * Describe:显示提示窗口
	 * 
	 * Date:2015-6-26
	 * 
	 * Author:liuzhouliang
	 */
	protected void showExecuteDialog() {

		executeDialogMessage = getExecuteDialogMessage();
		if (TextUtils.isEmpty(executeDialogMessage)) {
			return;
		}
		if (mContext != null) {
			if (AppRunningInfor.isRunningForeground(mContext)) {
				mExecuteDialog.setTitle(getExecuteDialogTitle());
				mExecuteDialog.setMessage(executeDialogMessage);
				
				if (AppRunningInfor
						.isActivityRunningForeground(mContext,
								"cn.bluerhino.driver.controller.activity.OrderFlowActivity")) {
					mExecuteDialog.show();
				}
			}
		}

	}

	/**
	 * 
	 * Describe:更新订单状态:跳转到下一个状态
	 * 
	 * Date:2015-6-26
	 * 
	 * Author:liuzhouliang
	 */
	protected void buildNextState() {
		mMachineContext.setOrderState(mMachineContext
				.getOrderState(getNextValue()));
		mMachineContext.buildView();

	}

	/**
	 * 
	 * Describe:执行任务请求
	 * 
	 * Date:2015-6-26
	 * 
	 * Author:liuzhouliang
	 */
	protected void executeRequest() {

		setButtonUnClickAble();

		LogUtil.d(TAG, "执行executeRequest" + "url==" + getUrl() + "当前已经完成状态"
				+ getValue() + "下一个状态" + getNextValue());
		mBuilder.setUrl(getUrl());
		mBuilder.setParams(getParams());
		BRJsonRequest request = mBuilder.build();
		mMachineContext.execute(request);
		// Message message = Message.obtain();
		// message.what = 2007;
		// messageHandler.sendMessage(message);
	}

	protected String getUrl() {
		return EXCUTEORDER;
	}

	/**
	 * 
	 * Describe:请求参数
	 * 
	 * Date:2015-6-26
	 * 
	 * Author:liuzhouliang
	 */
	protected BRRequestParams getParams() {
		ApplicationController appInstance = ApplicationController.getInstance();
		LocationInfo locationinfo = appInstance.getLastLocationInfo();
		BRRequestParams params = new BRRequestParams(appInstance.getLoginfo()
				.getSessionID());
		params.setDeviceId(appInstance.getDeviceId());
		params.setVerCode(appInstance.getVerCode());
		params.put("orderId", getOrderInfo().getOrderId()); // 订单号
		params.put("OrderFlag", getNextValue());// 订单的下一个状态
		params.put("lat", String.valueOf(locationinfo.getLatitude()));
		params.put("lng", String.valueOf(locationinfo.getLongitude()));
		LogUtil.d(TAG, "请求参数==" + params.toString() + "url==" + getUrl());
		return params;
	}

	/**
	 * 
	 * Describe:请求失败回调信息
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	protected void callOnErrorResponse(VolleyError error) {
		setButtonClickAble();
		LogUtil.d(TAG, "更新状态回调失败===" + error.getMessage());
		stopAllTimerTask();
		if (null != error) {
			String msg = null;
			if (error instanceof ServerError) {
				msg = "服务器错误";
			} else if (error instanceof TimeoutError) {
				msg = "连接超时";
			} else if (error instanceof NetworkError) {
				msg = "网络错误";
			} else if (error instanceof ParseError) {
				msg = "解析错误";
			} else if (error instanceof BRResponseError) {
				msg = error.getMessage();
			} else {
				msg = "未知错误";
			}
			if (!TextUtils.isEmpty(msg)) {
				Toast.makeText(mMachineContext.getContext(), msg,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 
	 * Describe:成功响应，重置订单状态
	 * 
	 * Date:2015-6-26
	 * 
	 * Author:liuzhouliang
	 */
	protected void callOnResponse(JSONObject response) {
		LogUtil.d(TAG, "返回订单状态信息====" + response.toString());
		setButtonClickAble();
		stopAllTimerTask();
		try {

			mMachineContext.getOrderInfo().setOrderFlag(
					response.getInt("OrderFlag"));
			mMachineContext.getOrderInfo().setArriveCount(
					response.getInt("ArriveCount"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.d(TAG, e.toString());
		}
		LogUtil.d(TAG, "callOnResponse ===新订单信息"
				+ mMachineContext.getOrderInfo().toString());
		refreshOrderState();
		buildNextState();

	}

	/**
	 * 
	 * Describe:让控件获取点击事件
	 * 
	 * Date:2015-8-22
	 * 
	 * Author:liuzhouliang
	 */
	protected void setButtonClickAble() {
		if (currentView != null) {
			currentView.setClickable(true);
		}
	}

	/**
	 * 
	 * Describe:设置控件不可点击
	 * 
	 * Date:2015-8-22
	 * 
	 * Author:liuzhouliang
	 */
	protected void setButtonUnClickAble() {
		if (currentView != null) {
			currentView.setClickable(false);
		}
	}

	/**
	 * 
	 * Describe:刷新订单状态文本显示
	 * 
	 * Date:2015-7-3
	 * 
	 * Author:liuzhouliang
	 */

	protected void refreshOrderState() {
		/**
		 * 更新文本显示状态
		 */
		LogUtil.d(TAG, "refreshOrderState" + "已经完成状态==" + getValue()
				+ "已经到达发货地" + getOrderInfo().getArriveCount());
		switch (getValue()) {
		case OrderInfo.OrderState.WAITSERVICE:
			showOrderStateInfor("当前司机出发");
			break;

		case OrderInfo.OrderState.DELIVERDEPARTURE:
			/**
			 * 当前状态为到达发货地
			 */
			showOrderStateInfor("当前抵达发货地，正在计算等待时间");
			break;

		case OrderInfo.OrderState.REACHSHIPADDRESS:
			/**
			 * 当前状态为从发货地出发
			 */
			showOrderStateInfor("司机从发货地出发");
			break;
		case OrderInfo.OrderState.DEPARTURESHIPADDRESS:
			/**
			 * 当前状态为到达收货地
			 */
			if (isSingleOrMultipleDestination()) {
				/**
				 * 如果是多个收货地
				 */
				int arriveNum = getOrderInfo().getArriveCount();
				showOrderStateInfor("已到达收货地" + (arriveNum + 1) + "，正在计算等待时间");
			} else {
				/**
				 * 单个收货地
				 */
				showOrderStateInfor("已到达收货地，正在计算等待时间");
			}

		case OrderInfo.OrderState.REACHPLACEOFRECEIPT:
			/**
			 * 当前已经完成状态：4600
			 */
			if (isSingleOrMultipleDestination()) {
				/**
				 * 如果是多个收货地
				 */

				if (ApplicationController.getInstance().isMultipleStartAgain) {
					ApplicationController.getInstance().isMultipleStartAgain = false;
					showOrderStateInfor("从收货地出发");
				} else {
					int arriveNum = getOrderInfo().getArriveCount();
					showOrderStateInfor("已到达收货地" + (arriveNum) + "，正在计算等待时间");
				}

			} else {
				/**
				 * 单个收货地
				 */

				if (ApplicationController.getInstance().isSingleStartAgain) {
					ApplicationController.getInstance().isSingleStartAgain = false;
					showOrderStateInfor("从收货地出发");
				} else {
					showOrderStateInfor("已到达收货地" + "正在计算等待时间");
				}

			}

			break;
		case OrderInfo.OrderState.DEPARTURERECEIPT:
			/**
			 * 当前完成状态4800
			 */
			if (isSingleOrMultipleDestination()) {
				/**
				 * 如果是多个收货地
				 */
				int arriveNum = getOrderInfo().getArriveCount();
				showOrderStateInfor("已到达收货地" + (arriveNum) + "，正在计算等待时间");
			} else {
				/**
				 * 单个收货地
				 */
				showOrderStateInfor("已到达收货地"
						+ (getOrderInfo().getArriveCount()) + "正在计算等待时间");
			}
			break;
		}
	}

	/**
	 * 
	 * Describe:在更新订单状态成功后，停止当前状态下的定时任务
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */

	protected void stopCurrentTask(int currentOrderFlag) {
		switch (currentOrderFlag) {
		case OrderInfo.OrderState.WAITSERVICE:// 3000

			/**
			 * 停止模拟响应服务任务
			 */
			LogUtil.d(TAG, "终止任务==模拟响应服务 ");
			if (appointResponseServerTimer != null) {

				appointResponseServerTimer.cancel();
				appointResponseServerTimer = null;
			}

			if (appointResponseServerTimerTask != null) {
				appointResponseServerTimerTask.cancel();
				appointResponseServerTimerTask = null;
			}

			break;
		case OrderInfo.OrderState.DELIVERDEPARTURE:// 3300
			/**
			 * 即时订单===，模拟司机出发任务
			 */

			if (immediatelyDriverStartTimer != null) {
				LogUtil.d(TAG, "终止任务==即时订单，模拟司机出发任务");
				immediatelyDriverStartTimer.cancel();
				immediatelyDriverStartTimer = null;
			}

			if (immediatelyDriverStartTimerTask != null) {
				immediatelyDriverStartTimerTask.cancel();
				immediatelyDriverStartTimerTask = null;
			}
			/**
			 * 预约订单====，模拟司机出发任务
			 */

			if (appointOrderTimer != null) {
				LogUtil.d(TAG, "终止任务==预约订单，模拟司机出发任务");
				appointOrderTimer.cancel();
				appointOrderTimer = null;
			}

			if (appointOrderTimerTask != null) {
				appointOrderTimerTask.cancel();
				appointOrderTimerTask = null;
			}
			break;
		case OrderInfo.OrderState.DEPARTURESHIPADDRESS:// 4150
			/**
			 * 模拟离开发货地任务
			 */

			if (deliverStartTimer != null) {
				LogUtil.d(TAG, "终止任务==模拟离开发货地任务");
				deliverStartTimer.cancel();
				deliverStartTimer = null;
			}

			if (deliverStartTimerTask != null) {
				deliverStartTimerTask.cancel();
				deliverStartTimerTask = null;
			}
			break;

		case OrderInfo.OrderState.REACHPLACEOFRECEIPT:// 4600
			/**
			 * 模拟到达收货地任务
			 */

			if (arriveTakeDeliverTimer != null) {
				LogUtil.d(TAG, "终止任务==模拟到达收货地任务");
				arriveTakeDeliverTimer.cancel();
				arriveTakeDeliverTimer = null;
			}

			if (arriveTakeDeliverTimerTask != null) {
				arriveTakeDeliverTimerTask.cancel();
				arriveTakeDeliverTimerTask = null;
			}
			break;

		case OrderInfo.OrderState.DEPARTURERECEIPT:// 4800
			/**
			 * 从收货地出发，模拟司机从收货地出发
			 */

			if (takeDeliverStartTimer != null) {
				LogUtil.d(TAG, "终止任务==模拟从收货地出发任务");
				takeDeliverStartTimer.cancel();
				takeDeliverStartTimer = null;
			}

			if (takeDeliverStartTimerTask != null) {
				takeDeliverStartTimerTask.cancel();
				takeDeliverStartTimerTask = null;
			}
			break;
		}

	}

	/**
	 * 
	 * Describe:在订单状态发生改变后，判断新的订单状态是否需要自动触发
	 * 
	 * Date:2015-7-1
	 * 
	 * Author:liuzhouliang
	 */
	private void updateOrderState(int currentOrderFlag) {
		switch (currentOrderFlag) {
		case OrderInfo.OrderState.WAITRESPONSE:
			/**
			 * 后台定向分单后，当前状态2600，用户手动触发
			 */
			break;
		case OrderInfo.OrderState.WAITSERVICE:
			/**
			 * 3000====完成等待服务状态==========下一个状态模拟司机出发
			 */

			if (200 == getOrderType()) {

				/**
				 * 实时订单================司机在成功接单后，5分钟的最后3分钟，每分钟坐标轨迹会发生变动，即判定司机已经出发
				 */

				simulationImmediatelyOrderStart();

			} else {
				/**
				 * 预约订单====================司机在预约订单服务前25分钟，处于发货地范围10公里即判定司机已经出发
				 */
				simulationAppointOrderStart();

			}

			break;
		case OrderInfo.OrderState.DELIVERDEPARTURE:
			/**
			 * 3300===已经完成状态：司机出发，下一个状态到达发货地，手动触发
			 */
			if (ApplicationController.getInstance().isAutomaticTo4000) {
				/**
				 * 3000状态下，点击到达发货地，连续执行：3300，4000状态
				 */
				executeRequest();
				ApplicationController.getInstance().isAutomaticTo4000 = false;

			}

			break;
		case OrderInfo.OrderState.REACHSHIPADDRESS:// 4000
			/**
			 * 已经完成到达发货地状态，自动模拟从发货地出发
			 */
			simulationDriverDeliverStart();
			break;
		case OrderInfo.OrderState.DEPARTURESHIPADDRESS:// 4150
			/**
			 * 已经完成从发货地出发状态
			 */
			if (ApplicationController.getInstance().isMultiAutomaticTo4600) {
				/**
				 * 如果在4000状态下点击：到达收货地，执行从发货地出发--到达收货地
				 */
				LogUtil.d(TAG, "isMultiAutomaticTo4600===executeRequest()");
				executeRequest();
				ApplicationController.getInstance().isMultiAutomaticTo4600 = false;
				return;
			}

			if (ApplicationController.getInstance().isAutomaticTo5000) {
				/**
				 * 如果在4000状态下点击：完成服务，执行从发货地出发--到达收货地--完成
				 */
				LogUtil.d(TAG, "isAutomaticTo5000===executeRequest()");
				executeRequest();
				return;
			}
			if (isSingleOrMultipleDestination()) {
				/**
				 * 多个收货地，手动出发到达收货地
				 */
				return;
			} else {
				/**
				 * 一个收货地，模拟到达收货地
				 */
				LogUtil.d(TAG, "simulationArriveTakeDeliver()");
				if (ApplicationController.getInstance().isStartAgainAutomaticTo4600) {
					executeRequest();
				} else {
					simulationArriveTakeDeliver();
				}

			}

			break;

		case OrderInfo.OrderState.REACHPLACEOFRECEIPT:// 4600
			/**
			 * 当前已经完成状态：到达收货地 4600
			 */
			if (isSingleOrMultipleDestination()) {

				if (mMachineContext.getOrderInfo().getArriveCount() < mMachineContext
						.getOrderInfo().getDeliver().size()) {
					/**
					 * 还有未到达的收货地，默认从收货地出发
					 */
					simulationDriverTakeDeliverStart(getOrderInfo()
							.getArriveCount());
				}

				return;
			}
			if (ApplicationController.getInstance().isStartAgainAutomaticTo4600) {
				executeRequest();

			}
			if (ApplicationController.getInstance().isAutomaticTo5000) {
				executeRequest();
				ApplicationController.getInstance().isAutomaticTo5000 = false;
				return;
			}
			break;

		case OrderInfo.OrderState.DEPARTURERECEIPT:// 4800
			/**
			 * 当前已经完成状态：从收货地出发 4800，下一个状态：到达收货地
			 */
			if (ApplicationController.getInstance().isStartAgainAutomaticTo4600) {
				ApplicationController.getInstance().isStartAgainAutomaticTo4600 = false;
			}
			if (isSingleOrMultipleDestination()) {
				/**
				 * 多个收货地
				 */
				if (mMachineContext.getOrderInfo().getArriveCount() == mMachineContext
						.getOrderInfo().getDeliver().size()) {
					/**
					 * 当前已经完成状态：从收货地出发，且已经到达的收货地数目等于总共的收货地数目，进入下一个状态4600，
					 * 显示完成服务和再次出发
					 */
					if (ApplicationController.getInstance().isStartAgainApp) {
						ApplicationController.getInstance().isStartAgainApp = false;
						LogUtil.d(TAG, "多个收货地===再次出发");
						return;
					}
					executeRequest();
					LogUtil.d(TAG, "更新到下一个状态==4600");
				}
				if (ApplicationController.getInstance().isAutomaticToNextDestination) {
					executeRequest();
					ApplicationController.getInstance().isAutomaticToNextDestination = false;
				}

			}
			break;

		case OrderInfo.OrderState.SERVICEFINISH:// 5000
			/**
			 * 服务完成状态
			 */
			break;
		}

	}

	/**
	 * 
	 * Describe:单个收货地，模拟到达收货地任务，1.当司机进入到收货地（用户填写收货地）范围内700m范围内时
	 * 
	 * 2.司机静止超过2分钟
	 * 
	 * 当满足条件1后，开始检测条件2，条件2实现时，触发“点击以到达收货地”操作
	 * 
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	private void simulationArriveTakeDeliver() {
		LogUtil.d(TAG, "模拟到达收货地=====");
		if (arriveTakeDeliverTimer == null) {
			arriveTakeDeliverTimer = new Timer();
		}

		if (arriveTakeDeliverTimerTask == null) {
			arriveTakeDeliverTimerTask = new TimerTask() {
				@Override
				public void run() {
					double dis = 0;
					// 获取最新的地理位置信息
					LocationInfo lastLocationInfo = ApplicationController
							.getInstance().getLastLocationInfo();
					LatLng currentLocation = new LatLng(
							lastLocationInfo.getLatitude(),
							lastLocationInfo.getLongitude());
					LatLng takeDeliverLocation = null;
					// 获取收货地地址位置信息
					List<DeliverInfo> takeDeliverList = mMachineContext
							.getOrderInfo().getDeliver();

					int arriveNum = getOrderInfo().getArriveCount();

					if (takeDeliverList != null && takeDeliverList.size() > 0) {
						if (arriveNum < takeDeliverList.size()) {
							/**
							 * 收货地存在，到达的收货地数量不大于总共的收货地数量，属于非再次出发状态
							 */
							LogUtil.d(TAG,
									"收货地信息" + "arriveNum=" + arriveNum
											+ " takeDeliverList.size()="
											+ takeDeliverList.size()
											+ takeDeliverList.toString());
							takeDeliverLocation = new LatLng(
									Double.parseDouble(takeDeliverList.get(
											arriveNum).getDeliverAddressLat()),
									Double.parseDouble(takeDeliverList.get(
											arriveNum).getDeliverAddressLng()));
							dis = DistanceUtil.getDistance(currentLocation,
									takeDeliverLocation);
						} else {
							/**
							 * 收货地存在，到达的收货地数量大于总共的收货地数量，属于再次出发状态
							 */
							LogUtil.d(TAG,
									"收货地信息" + "arriveNum=" + arriveNum
											+ " takeDeliverList.size()="
											+ takeDeliverList.size()
											+ takeDeliverList.toString());
							takeDeliverLocation = new LatLng(
									Double.parseDouble(takeDeliverList.get(
											takeDeliverList.size() - 1)
											.getDeliverAddressLat()),
									Double.parseDouble(takeDeliverList.get(
											takeDeliverList.size() - 1)
											.getDeliverAddressLng()));
							dis = DistanceUtil.getDistance(currentLocation,
									takeDeliverLocation);
						}

					} else {
						LogUtil.d(TAG, "暂无收货地信息");
					}

					if (dis < 700 && dis > 0) {

						LogUtil.d(TAG, "进入收货地700m内" + "距离收货地距离" + dis);
						if (startTime == 0) {

							startTime = System.currentTimeMillis();
							LocationInfo inforOne = ApplicationController
									.getInstance().getLastLocationInfo();
							locationOneLatLng = new LatLng(
									inforOne.getLatitude(),
									inforOne.getLongitude());
							LogUtil.d(TAG, "开始判断是否静止startTime==" + startTime);
						} else {

							endTime = System.currentTimeMillis();
							LogUtil.d(TAG, "判断是否静止，第二个时间点endTime==" + endTime);
						}
						LogUtil.d(TAG, "判断是否静止，两个时间点的时间差=="
								+ (endTime - startTime) / 1000 + "秒");
						if ((endTime - startTime) / 1000 > 60 * 2) {
							/**
							 * 大于2分钟后取第二个坐标
							 */
							Double isMotionlessDis = null;
							LocationInfo inforTwo = ApplicationController
									.getInstance().getLastLocationInfo();
							LatLng locationTwoLatLng = new LatLng(
									inforTwo.getLatitude(),
									inforTwo.getLongitude());
							isMotionlessDis = DistanceUtil.getDistance(
									locationOneLatLng, locationTwoLatLng);
							LogUtil.d(TAG, "两分钟后坐标距离===" + isMotionlessDis);
							if (ApplicationController.testSwitch) {
								Message message = Message.obtain();
								message.what = 2003;
								Bundle bundle = new Bundle();
								bundle.putString("2003", "两分钟后坐标距离==="
										+ isMotionlessDis + "==静止时间差=="
										+ (endTime - startTime) / 1000 + "秒");
								message.setData(bundle);
								messageHandler.sendMessage(message);
							}
							if (isMotionlessDis < 50.0) {
								/**
								 * 判断为静止
								 */
								executeRequest();
								startTime = 0;
							} else {
								startTime = 0;
							}
						}
						/**
						 * test=================
						 */
						if (ApplicationController.testSwitch) {
							Message message = Message.obtain();
							message.what = 2003;
							Bundle bundle = new Bundle();
							bundle.putString("2003", "距离收货地的距离==" + dis
									+ "==静止时间差==" + (endTime - startTime)
									/ 1000 + "秒");
							message.setData(bundle);
							messageHandler.sendMessage(message);
						}

					} else {
						LogUtil.d(TAG, "距离收货地距离" + dis);
						/**
						 * test=================================
						 */
						if (ApplicationController.testSwitch) {
							Message message = Message.obtain();
							message.what = 2005;
							Bundle dataBundle = new Bundle();
							dataBundle.putString("2005", "距离收货地距离" + dis);
							message.setData(dataBundle);
							messageHandler.sendMessage(message);
						}

					}
				}
			};
			if (arriveTakeDeliverTimer != null
					&& arriveTakeDeliverTimerTask != null)
				arriveTakeDeliverTimer.schedule(arriveTakeDeliverTimerTask,
						1000, 1000 * 2);
		}

	}

	/**
	 * 
	 * Describe:模拟司机从收货地出发
	 * 
	 * Date:2015-7-5
	 * 
	 * Author:liuzhouliang
	 */
	private void simulationDriverTakeDeliverStart(final int count) {
		LogUtil.d(TAG, "模拟司机从收货地出发=====" + (count));
		if (takeDeliverStartTimer == null) {
			takeDeliverStartTimer = new Timer();
		}

		if (takeDeliverStartTimerTask == null) {
			takeDeliverStartTimerTask = new TimerTask() {
				@Override
				public void run() {
					double dis = 0;
					// 获取最新的地理位置信息
					LocationInfo lastLocationInfo = ApplicationController
							.getInstance().getLastLocationInfo();
					LatLng currentLocation = new LatLng(
							lastLocationInfo.getLatitude(),
							lastLocationInfo.getLongitude());
					// 获取当前收货地地址位置信息
					List<DeliverInfo> takeDeliverList = mMachineContext
							.getOrderInfo().getDeliver();
					LatLng takeDeliverLocation;
					if (takeDeliverList != null && takeDeliverList.size() > 0) {
						/**
						 * 收货地存在
						 */
						if (count > takeDeliverList.size()) {
							/**
							 * 如果收货地都已经走完，司机点击再次出发，count值大于一共的收货地数目
							 */
							takeDeliverLocation = new LatLng(
									Double.parseDouble(takeDeliverList.get(
											takeDeliverList.size() - 1)
											.getDeliverAddressLat()),
									Double.parseDouble(takeDeliverList.get(
											takeDeliverList.size() - 1)
											.getDeliverAddressLng()));
							dis = DistanceUtil.getDistance(currentLocation,
									takeDeliverLocation);
						} else {
							takeDeliverLocation = new LatLng(
									Double.parseDouble(takeDeliverList.get(
											count - 1).getDeliverAddressLat()),
									Double.parseDouble(takeDeliverList.get(
											count - 1).getDeliverAddressLng()));
							dis = DistanceUtil.getDistance(currentLocation,
									takeDeliverLocation);
						}

					} else {
						LogUtil.d(TAG, "暂无收货地信息");
					}
					LogUtil.d(TAG, "当前位置离收货地" + (count) + "距离" + dis);
					/**
					 * test================
					 */
					if (ApplicationController.testSwitch) {
						Message message = Message.obtain();
						message.what = 2004;
						Bundle bundle = new Bundle();
						bundle.putString("2004", dis + "");
						message.setData(bundle);
						messageHandler.sendMessage(message);
					}

					if (dis < 3000 && dis > 300) {
						/**
						 * 检测到司机已经离收货地
						 */
						LogUtil.d(TAG, "司机已经离开收货地" + (count));
						executeRequest();
					} else {
						LogUtil.d(TAG, "司机未离开收货地" + (count));
					}
				}
			};

			if (takeDeliverStartTimer != null
					&& takeDeliverStartTimerTask != null)
				takeDeliverStartTimer.schedule(takeDeliverStartTimerTask, 1000,
						1000 * 2);
		}

	}

	/**
	 * 
	 * Describe:模拟司机从发货地出发
	 * ，司机点击“到达发货地”时，如果该坐标点A在发货地（用户填写的）范围内3km时，判断司机正确处于“到达发货地”状态，并记录坐标点A
	 * 
	 * 满足上述条件时，当gps检测到司机离开A范围300m时，触发“从发货地出发”操作请求
	 * 
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	private void simulationDriverDeliverStart() {
		LogUtil.d(TAG, "模拟司机从发货地出发=====");
		if (deliverStartTimer == null) {
			deliverStartTimer = new Timer();
		}

		if (deliverStartTimerTask == null) {
			deliverStartTimerTask = new TimerTask() {
				@Override
				public void run() {
					double dis = 0;
					// 获取最新的地理位置信息
					LocationInfo lastLocationInfo = ApplicationController
							.getInstance().getLastLocationInfo();
					LatLng currentLocation = new LatLng(
							lastLocationInfo.getLatitude(),
							lastLocationInfo.getLongitude());
					LogUtil.d(TAG, "最新地理位置信息==" + currentLocation.toString());
					// 获取发货地地址位置信息
					LocationInfo startLocationInfo = ApplicationController
							.getInstance().deliverGoodsLocationInfo;
					if (startLocationInfo != null) {
						LatLng startLocation = new LatLng(
								startLocationInfo.getLatitude(),
								startLocationInfo.getLongitude());
						LogUtil.d(TAG,
								"最终确认发货地理位置信息==" + startLocationInfo.toString());
						dis = DistanceUtil.getDistance(currentLocation,
								startLocation);
					}

					LogUtil.d(TAG, "当前位置离发货地距离" + dis);
					/**
					 * test=========
					 */
					if (ApplicationController.testSwitch) {
						Message message = Message.obtain();
						message.what = 2002;
						Bundle bundle = new Bundle();
						bundle.putString("2002", dis + "");
						message.setData(bundle);
						messageHandler.sendMessage(message);
					}

					if (dis < 1000 * 3 && dis > 300) {
						/**
						 * 检测到司机已经离开发货地
						 */
						LogUtil.d(TAG, "司机已经离开发货地");
						executeRequest();
					} else {
						LogUtil.d(TAG, "司机未离开发货地");
					}
				}
			};
			if (deliverStartTimer != null && deliverStartTimerTask != null)
				deliverStartTimer.schedule(deliverStartTimerTask, 1000,
						1000 * 2);
		}

	}

	/**
	 * 
	 * Describe:预约订单模拟司机出发==================
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	private void simulationAppointOrderStart() {
		LogUtil.d(TAG, "预约订单===模拟司机出发");
		if (appointOrderTimer == null) {
			appointOrderTimer = new Timer();
		}

		if (appointOrderTimerTask == null) {
			appointOrderTimerTask = new TimerTask() {
				@Override
				public void run() {

					boolean isStart = appointmentOrderStart();
					if (isStart) {
						/**
						 * 预约订单司机已经出发，更新订单状态
						 */
						LogUtil.d(TAG, "预约订单司机已经出发====更新订单状态");
						executeRequest();

					} else {
						LogUtil.d(TAG, "预约订单===司机未出发");
					}
				}
			};
			if (appointOrderTimer != null && appointOrderTimerTask != null)
				appointOrderTimer.schedule(appointOrderTimerTask, 0, 1000 * 2);
		}

	}

	/**
	 * 
	 * Describe:判断预约状态下的订单司机是否已经出发，司机在预约订单服务前25分钟，处于发货地范围10公里即判定司机已经出发
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	protected boolean appointmentOrderStart() {
		double dis;
		boolean isStart = false;
		// 预约时间
		long appointmentTime = mMachineContext.getOrderInfo().getTransTime();
		// 当前时间
		long currentTime = System.currentTimeMillis() / 1000;
		LogUtil.d(TAG, "预约时间" + appointmentTime + "当前时间" + currentTime);
		// 时间差:分
		long diffTime = (appointmentTime - currentTime) / 60;
		// 获取最新的地理位置信息
		LocationInfo lastLocationInfo = ApplicationController.getInstance()
				.getLastLocationInfo();
		LatLng currentLocation = new LatLng(lastLocationInfo.getLatitude(),
				lastLocationInfo.getLongitude());
		// 获取发货地地址位置信息
		LatLng startLocation = new LatLng(Double.parseDouble(mMachineContext
				.getOrderInfo().getPickupAddressLat()),
				Double.parseDouble(mMachineContext.getOrderInfo()
						.getPickupAddressLng()));

		dis = DistanceUtil.getDistance(currentLocation, startLocation);
		LogUtil.d(TAG, "距离预约时间差" + diffTime + "距离发货地距离" + dis);
		/**
		 * test==============
		 */
		if (ApplicationController.testSwitch) {
			Message message = Message.obtain();
			message.what = 2006;
			Bundle bundle = new Bundle();
			bundle.putString("2006", "预约订单===距离预约时间" + diffTime + "距离发货地距离"
					+ dis);
			message.setData(bundle);
			messageHandler.sendMessage(message);
		}

		if (diffTime < 25.0 && dis < 1000 * 10) {
			/**
			 * 判断为司机已经出发
			 */
			isStart = true;
			return isStart;
		}
		return isStart;
	}

	/**
	 * 
	 * Describe:获取当前订单类型：预约订单，即时服务订单
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	protected int getOrderType() {
		int type = mMachineContext.getOrderInfo().getServeType();
		return type;

	}

	/**
	 * 
	 * Describe:实时订单模拟司机出发：一分钟内的坐标，首尾坐标距离差值达到50m
	 * 
	 * Date:2015-7-1
	 * 
	 * Author:liuzhouliang
	 */
	private void simulationImmediatelyOrderStart() {
		LogUtil.d(TAG, "实时订单模拟司机出发=====");
		if (immediatelyDriverStartTimer == null) {
			immediatelyDriverStartTimer = new Timer();
		}

		if (immediatelyDriverStartTimerTask == null) {
			immediatelyDriverStartTimerTask = new TimerTask() {
				@Override
				public void run() {
					List<LatLng> locationList;
					synchronized (obj) {
						locationList = ApplicationController.getInstance()
								.getLocationList();
					}

					if (locationList != null && locationList.size() > 0) {
						int size = locationList.size();
						LatLng locationOneLatLng = locationList.get(0);
						LatLng locationTwoLatLng = locationList.get(size - 1);
						Double dis = null;
						dis = DistanceUtil.getDistance(locationOneLatLng,
								locationTwoLatLng);
						LogUtil.d(TAG, "首尾坐标点距离" + dis);
						/**
						 * test=====================
						 */
						if (ApplicationController.testSwitch) {
							Message message = Message.obtain();
							message.what = 1001;
							Bundle dataBundle = new Bundle();
							dataBundle.putString("1001", dis + "");
							message.setData(dataBundle);
							messageHandler.sendMessage(message);
						}

						if (dis > 50) {
							/**
							 * 司机已经出发
							 */
							LogUtil.d(TAG, "司机已经出发");
							executeRequest();
						} else {
							/**
							 * 司机未出发
							 */
							LogUtil.d(TAG, "司机未出发");
						}
					} else {
						LogUtil.d(TAG, "获取坐标为空");
					}
				}
			};
			if (immediatelyDriverStartTimer != null
					&& immediatelyDriverStartTimerTask != null)
				immediatelyDriverStartTimer.schedule(
						immediatelyDriverStartTimerTask, DELAY_TIME, 1000 * 2);
		}

	}

	/**
	 * 
	 * Describe:停止所有定时任务
	 * 
	 * Date:2015-7-7
	 * 
	 * Author:liuzhouliang
	 */
	public void stopAllTimerTask() {
		/**
		 * 停止模拟响应服务任务
		 */
		if (appointResponseServerTimer != null) {
			LogUtil.d(TAG, "终止任务==模拟响应服务 ");
			appointResponseServerTimer.cancel();
			appointResponseServerTimer = null;
		}

		if (appointResponseServerTimerTask != null) {
			appointResponseServerTimerTask.cancel();
			appointResponseServerTimerTask = null;
		}
		/**
		 * 即时订单，模拟司机出发任务
		 */

		if (immediatelyDriverStartTimer != null) {
			LogUtil.d(TAG, "定时终止任务==即时订单，模拟司机出发任务");
			immediatelyDriverStartTimer.cancel();
			immediatelyDriverStartTimer = null;
		}

		if (immediatelyDriverStartTimerTask != null) {
			immediatelyDriverStartTimerTask.cancel();
			immediatelyDriverStartTimerTask = null;
		}
		/**
		 * 预约订单，模拟司机出发任务
		 */
		if (appointOrderTimer != null) {
			LogUtil.d(TAG, "定时终止任务==预约订单，模拟司机出发任务");
			appointOrderTimer.cancel();
			appointOrderTimer = null;
		}

		if (appointOrderTimerTask != null) {
			appointOrderTimerTask.cancel();
			appointOrderTimerTask = null;
		}
		/**
		 * 模拟离开发货地任务
		 */
		if (deliverStartTimer != null) {
			LogUtil.d(TAG, "定时终止任务==模拟离开发货地任务");
			deliverStartTimer.cancel();
			deliverStartTimer = null;
		}

		if (deliverStartTimerTask != null) {
			deliverStartTimerTask.cancel();
			deliverStartTimerTask = null;
		}
		/**
		 * 模拟到达收货地任务
		 */
		if (arriveTakeDeliverTimer != null) {
			LogUtil.d(TAG, "定时终止任务==模拟到达收货地任务");
			arriveTakeDeliverTimer.cancel();
			arriveTakeDeliverTimer = null;
		}

		if (arriveTakeDeliverTimerTask != null) {
			arriveTakeDeliverTimerTask.cancel();
			arriveTakeDeliverTimerTask = null;
		}
		/**
		 * 模拟从收货地出发任务
		 */
		if (takeDeliverStartTimer != null) {
			LogUtil.d(TAG, "定时终止任务==模拟从收货地出发任务");
			takeDeliverStartTimer.cancel();
			takeDeliverStartTimer = null;
		}

		if (takeDeliverStartTimerTask != null) {
			takeDeliverStartTimerTask.cancel();
			takeDeliverStartTimerTask = null;
		}

	}

	/**
	 * 
	 * Describe:更新订单状态显示文本
	 * 
	 * Date:2015-7-7
	 * 
	 * Author:liuzhouliang
	 */
	private void showOrderStateInfor(String msg) {
		getOrderStateParent().setVisibility(View.VISIBLE);
		getOrderStateTv().setText(msg);

	}

	protected String getExecuteDialogTitle() {
		return "提示";
	}

	/**
	 * 请求失败回调
	 */
	private final BRErrorListener ERROR_LISTENER = new BRErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			mLoadingDialog.dismiss();
			callOnErrorResponse(error);
		}
	};
	/**
	 * 请求成功回调
	 */
	private final BRJsonResponse SUCCEED_LISTENER = new BRJsonResponse() {
		@Override
		public void onResponse(JSONObject response) {
			mLoadingDialog.dismiss();
			callOnResponse(response);
		}
	};

	/**
	 * 
	 * Describe:判断是否是过个收货地,true，多个，false，单个
	 * 
	 * Date:2015-7-3
	 * 
	 * Author:liuzhouliang
	 */
	protected boolean isSingleOrMultipleDestination() {
		boolean isSingleOrMultiple = false;
		OrderInfo orderInfo = getOrderInfo();
		List<DeliverInfo> takeDeliverList = orderInfo.getDeliver();
		int size = 0;
		if (takeDeliverList != null && takeDeliverList.size() > 0) {
			size = takeDeliverList.size();
		}
		if (size == 1) {
			/**
			 * 一个收货地
			 */
			isSingleOrMultiple = false;
			return isSingleOrMultiple;
		}
		if (size > 1) {
			/**
			 * 多个收货地
			 */
			isSingleOrMultiple = true;
			return isSingleOrMultiple;
		}
		return isSingleOrMultiple;
	}

	public abstract String getExecuteDialogMessage();

	public abstract void buildView();

	public abstract int getValue();

	public abstract int getNextValue();
}
