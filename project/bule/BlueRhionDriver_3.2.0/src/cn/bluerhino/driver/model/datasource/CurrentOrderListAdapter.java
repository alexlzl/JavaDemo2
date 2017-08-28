package cn.bluerhino.driver.model.datasource;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.TimeUtil;

public class CurrentOrderListAdapter extends ShowTimeOrderInfoListAdapter {
	private final String TAG = getClass().getName();
	private boolean isPlay;
	// 存储倒计时差
	private List<Long> countDownTimeList;
	private List<OrderInfo> orderInfosList;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (!isPlay)
				return;
			handler.postDelayed(this, 1000);
			if (countDownTimeList != null && countDownTimeList.size() > 0) {
				int size = countDownTimeList.size();
				for (int i = 0; i < size; i++) {
					if (countDownTimeList.get(i) > 0) {
						countDownTimeList.set(i,
								countDownTimeList.get(i) - 1000);
					}
				}

			}

			notifyDataSetChanged();
		}
	};

	/**
	 * 
	 * Describe:启动倒计时
	 * 
	 * Date:2015-8-22
	 * 
	 * Author:liuzhouliang
	 */
	public void startCountDownTime() {
		isPlay = true;
		runnable.run();
	}

	public void stopCountDownTime() {
		isPlay = false;
	}

	public CurrentOrderListAdapter(Context context, List<OrderInfo> list) {
		super(context, list);

		initCountDownTime();
	}

	/**
	 * 
	 * Describe:刷新数据
	 * 
	 * Date:2015-8-22
	 * 
	 * Author:liuzhouliang
	 */

	public void setNewData(List<OrderInfo> list) {
		orderInfosList = list;
		initCountDownTime();

	}

	/**
	 * 
	 * Describe:初始化倒计时时间
	 * 
	 * Date:2015-8-21
	 * 
	 * Author:liuzhouliang
	 */
	public void initCountDownTime() {
		/**
		 * 处理倒计时
		 */
		if (orderInfosList != null) {
			int size = orderInfosList.size();
			if (size > 0) {
				LogUtil.d(TAG, "订单信息==" + orderInfosList.toString());
				if (countDownTimeList != null) {
					countDownTimeList.clear();
				}
				countDownTimeList = new ArrayList<Long>();
				for (int i = 0; i < size; i++) {

					long appointTime = orderInfosList.get(i).getTransTime() * 1000;
					long currentTime = System.currentTimeMillis();
					long gapTime = appointTime - currentTime;
					if (gapTime > 0) {
						countDownTimeList.add(i, gapTime);
					} else {
						countDownTimeList.add(i, 0L);
					}
				}
				LogUtil.d(TAG, "倒计时信息==" + countDownTimeList.toString());
			}
		}

	}

	@Override
	protected void setOrderStatusAndReason(Button orderState_button,
			TextView orderinfo_item_killmeters_change, OrderInfo orderInfo,
			int position) {
		setOrderStatus(orderState_button, orderInfo,orderinfo_item_killmeters_change);
		setOrderReason(orderinfo_item_killmeters_change, orderInfo, position);
	}

	/**
	 * 设置按钮样式
	 * 
	 * @param orderStateView
	 * @param orderInfo
	 */
	private void setOrderStatus(Button orderStateView, OrderInfo orderInfo,
			TextView orderinfo_item_killmeters_change) {
		int orderState = orderInfo.getFlag();
		int confirmed = orderInfo.getConfirmed(); // 待用户选择 or 待用户抢单
		orderStateView.setOnClickListener(null);
		orderStateView.setTextColor(Color.parseColor("#00A7E3"));
		orderStateView.setBackgroundColor(Color.parseColor("#ffffff"));
		LayoutParams params = orderStateView.getLayoutParams();
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		orderStateView.setLayoutParams(params);
		LinearLayout orderStateParentView = (LinearLayout) orderStateView
				.getParent();
		LinearLayout.LayoutParams parentParams = (LinearLayout.LayoutParams) orderStateParentView
				.getLayoutParams();
		parentParams.gravity = Gravity.CENTER_VERTICAL;
		orderStateParentView.setLayoutParams(parentParams);

		if (isChangeOrder(orderInfo.getOrderId())) {
			/**
			 * 需要处理显示已经取消或者改派的订单
			 */
			orderStateView.setText("该订单已经被取消");
			orderinfo_item_killmeters_change.setVisibility(View.GONE);
			LogUtil.d(TAG, "状态更改订单");

		} else {
			LogUtil.d(TAG, "非状态更改订单");
			switch (orderState) {
			case OrderInfo.OrderState.USERAFFIRM: // 等待用户确认
				orderStateView.setText(R.string.orderstate_waitaffirm);
				break;
			case OrderInfo.OrderState.NEEDDRIVER2: // 等待司机抢单
				orderStateView.setText(R.string.orderstate_needdelivery);
				break;
			case OrderInfo.OrderState.NEEDDRIVER3: // 抢单提交成功
				if (confirmed == 1) {
					// 待用户选择
					orderStateView
							.setText(R.string.orderstate_requestcommitsucceed);
				} else {
					// 待用户抢单
					orderStateView.setText(R.string.orderstate_needdelivery);
				}
				break;
			case OrderInfo.OrderState.WAITRESPONSE:// 等待响应
				orderStateView.setText(R.string.orderstate_waitresponse);
				break;
			case OrderInfo.OrderState.WAITSERVICE:// 等待服务
				orderStateView.setText(R.string.orderstate_waitservice);
				break;
			case OrderInfo.OrderState.DELIVERDEPARTURE:// 司机出发
				orderStateView.setText(R.string.orderstate_deliverdeparture);
				break;
			case OrderInfo.OrderState.REACHSHIPADDRESS:// 到达发货地
				orderStateView.setText(R.string.orderstate_reachshipaddress);
				break;
			case OrderInfo.OrderState.DEPARTURESHIPADDRESS:// 从发货地出发
				orderStateView
						.setText(R.string.orderstate_deliver_calculate_waittime);
				break;
			case OrderInfo.OrderState.REACHPLACEOFRECEIPT:// 到达收货地
				orderStateView
						.setText(R.string.orderstate_departureshipaddress);
				break;
			case OrderInfo.OrderState.DEPARTURERECEIPT:// 从收货地出发
				orderStateView
						.setText(R.string.orderstate_take_calculate_waittime);
				break;
			case OrderInfo.OrderState.SERVICEFINISH:// 服务结束
				orderStateView.setText(R.string.orderstate_servicefinish);
				break;
			default:
				if (orderState >= OrderInfo.OrderState.ORDERCANCEL) {
					orderStateView.setText(R.string.orderstate_ordercance);
				} else {
					orderStateView.setText("");
				}
				break;
			}
		}

	}

	/**
	 * 按钮底部文字描述
	 */
	private void setOrderReason(TextView orderinfo_item_killmeters_change,
			OrderInfo orderInfo, int position) {
		if (orderInfo.getServeType() != 200
				&& orderInfo.getOrderFlag() <= OrderInfo.OrderState.WAITSERVICE) {
			// long diffTime = orderInfo.getTransTime() * 1000
			// - System.currentTimeMillis();
			if (countDownTimeList.size() == 0) {
				return;
			}
			long diffTime = countDownTimeList.get(position);
			if (diffTime < 0) {
			} else if (diffTime <= 1000 * 60 * 60 * 24) {
				String[] timeUnit = TimeUtil.printDifference(diffTime).split(
						":");
				String hour = timeUnit[1];
				String min = timeUnit[2];
				if (min.length() == 1) {
					min = '0' + min;
				}
				SpannableString spannableString = new SpannableString(
						new StringBuilder("距服务开始还有").append(hour).append("小时")
								.append(min).append("分").toString());
				spannableString.setSpan(
						new ForegroundColorSpan(Color.parseColor("#FD6138")),
						7, spannableString.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				orderinfo_item_killmeters_change.setText(spannableString);
			} else if (diffTime <= 1000 * 60 * 60 * 24 * 365) {
				String[] timeUnit = TimeUtil.printDifference(diffTime).split(
						":");
				String day = timeUnit[0];
				String hour = timeUnit[1];
				SpannableString spannableString = new SpannableString(
						new StringBuilder("距服务开始还有").append(day).append("天")
								.append(hour).append("小时").toString());
				spannableString.setSpan(
						new ForegroundColorSpan(Color.parseColor("#FD6138")),
						7, spannableString.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				orderinfo_item_killmeters_change.setText(spannableString);
			}
		} else {
			orderinfo_item_killmeters_change.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 * Describe:判断是否是状态改变过的订单
	 * 
	 * Date:2015-9-8
	 * 
	 * Author:liuzhouliang
	 */
	private boolean isChangeOrder(int orderNum) {
		List<OrderInfo> cacheOrderList = ApplicationController.getInstance()
				.getmCacheOrderList();
		if (cacheOrderList != null) {
			LogUtil.d(TAG, "取缓存中的当前订单" + cacheOrderList.toString());
		} else {
			LogUtil.d(TAG, "取缓存中的当前订单为空");
		}

		if (cacheOrderList != null && cacheOrderList.size() > 0) {
			for (OrderInfo order : cacheOrderList) {
				int cacheOrderNum = order.getOrderId();
				if (orderNum == cacheOrderNum) {
					return true;
				}
			}
		} else {
			return false;
		}
		return false;

	}

}
