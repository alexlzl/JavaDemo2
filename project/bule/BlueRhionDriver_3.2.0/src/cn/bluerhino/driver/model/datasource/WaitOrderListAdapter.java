package cn.bluerhino.driver.model.datasource;

import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.ordermanager.CountdownOrderInfo;
import cn.bluerhino.driver.controller.ordermanager.CountdownOrderInfoUtil;
import cn.bluerhino.driver.helper.OrderInfoHelper;
import cn.bluerhino.driver.model.LocationInfo;
import cn.bluerhino.driver.model.OrderExecuteResponse;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.OrderInfo.OrderState;
import cn.bluerhino.driver.network.ExecuteOrderRequest;
import cn.bluerhino.driver.network.listener.BrErrorListener;
import cn.bluerhino.driver.view.LoadingDialog;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRResponseError;
import com.bluerhino.library.network.framework.BRModelRequest.BRModelResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * Describe:抢单列表数据适配器
 * 
 * Date:2015-8-12
 * 
 * Author:liuzhouliang
 */
public class WaitOrderListAdapter extends ShowTimeOrderInfoListAdapter {

	private BRRequestParams params;
	private AlertDialog mAlertDialog;
	private Window mDialogWindow;
	private RequestQueue mRequestQueue;
	private LoadingDialog mLoadingDialog;
	private NetWorkListener mNetListener;

	/**
	 * 
	 * Describe:点击抢单后接口回调刷新当前页面显示
	 * 
	 * Date:2015-8-12
	 * 
	 * Author:liuzhouliang
	 */
	public static interface NetWorkListener {
		/**
		 * 更新该条订单的信息
		 * @param orderInfo
		 */
		void getRefresh(CountdownOrderInfo orderInfo);
	}

	public void setNetWorkListener(NetWorkListener l) {
		this.mNetListener = l;
	}

	public WaitOrderListAdapter(Context context, List<OrderInfo> list) {
		super(context, list);
		mLoadingDialog = new LoadingDialog(mContext, R.string.wait_order_ing);
		initGrabOrderSuccessDialog();
		initGrabOrderRequest();
	}

	/**
	 * Describe:初始化抢单成功后显示对话框
	 * 
	 * Date:2015-8-12
	 * 
	 * Author:liuzhouliang
	 */
	private void initGrabOrderSuccessDialog() {
		mAlertDialog = new AlertDialog.Builder(mContext).setCancelable(false)
				.create();
		mDialogWindow = mAlertDialog.getWindow();
	}

	/**
	 * Describe:初始化抢单请求
	 * 
	 * Date:2015-8-12
	 * 
	 * Author:liuzhouliang
	 */
	private void initGrabOrderRequest() {
		mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		params = new BRRequestParams();
		params.setDeviceId(ApplicationController.getInstance().getDeviceId());
		params.setVerCode(ApplicationController.getInstance().getVerCode());
	}

	@Override
	protected void setOrderStatusAndReason(final Button orderState_button,
			final TextView orderinfo_item_killmeters_change,
			final OrderInfo orderInfo, int position) {
		// 2100等待司机抢单状态
		if (orderInfo.getOrderFlag() == OrderState.NEEDDRIVER2) {
			final CountdownOrderInfo countdownOrderInfo = (CountdownOrderInfo)orderInfo;
			// 2100 且 reason没有 才表示可以抢单
			if (orderInfo.getReason().equals("")) {
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append('\n');
				sBuilder.append(countdownOrderInfo.getSecond());
				sBuilder.append('S');
		        //可以抢单
	        	orderState_button.setText("抢单" + sBuilder.toString());
	        	orderState_button.setBackgroundResource(R.drawable.order_button_selector);
		        this.setBtnUnderText(orderinfo_item_killmeters_change, orderInfo);
		        /**
		         * 抢单事件
		         */
		        orderState_button
		        .setOnClickListener(new Button.OnClickListener() {
		        	@Override
		        	public void onClick(View v) {
		        		// 进行抢单,如果按钮可用的话
		        		BRModelResponse<OrderExecuteResponse> succeedListener = new BRModelResponse<OrderExecuteResponse>() {
		        			@Override
		        			public void onResponse(
		        					OrderExecuteResponse model) {
		        				mLoadingDialog.dismiss();
		        				mAlertDialog.show();
		        				mDialogWindow
		        				.setContentView(R.layout.alertdialog_bg);
		        				View.OnClickListener dialogdismisslistener = new View.OnClickListener() {
		        					@Override
		        					public void onClick(View v) {
		        						if (mAlertDialog.isShowing()) {
		        							mAlertDialog.dismiss();
		        						}
		        					}
		        				};
		        				mDialogWindow.findViewById(R.id.btn)
		        				.setOnClickListener(
		        						dialogdismisslistener);
		        				mDialogWindow.findViewById(R.id.text)
		        				.setOnClickListener(dialogdismisslistener);
		        				
        						if (mNetListener != null) {
        							/**
        							 * 对话框消失后，回调刷新界面
        							 */
        							mNetListener.getRefresh(countdownOrderInfo);
        						}
		        			}
		        		};
		        		ApplicationController appInstance = ApplicationController.getInstance();
		        		LocationInfo locationinfo = appInstance.getLastLocationInfo();
		        		params.setToken((appInstance.getLoginfo().getSessionID()));
		        		params.put("lat", String.valueOf(locationinfo
		        				.getLatitude()));
		        		params.put("lng", String.valueOf(locationinfo
		        				.getLongitude()));
		        		params.put("orderId", orderInfo.getOrderId());
		        		params.put("OrderFlag", OrderState.NEEDDRIVER3);
		        		BRFastRequest request = new ExecuteOrderRequest.Builder()
		        		.setSucceedListener(succeedListener)
		        		.setErrorListener(
		        				new BrErrorListener(mContext) {
		        					@Override
		        					public void onErrorResponse(VolleyError error) {
		        						mLoadingDialog.dismiss();
		        						if (error instanceof BRResponseError) {
//		        							int code = ((BRResponseError) error).getCode();
		        							//{"data":[],"msg":"您来晚了，订单已经被别的司机服务！","code":1002}
//		        							if(code == 1002){
//		        								CountdownOrderInfoUtil.setReasonInNotGrab(countdownOrderInfo);
//		        							}
		        							CountdownOrderInfoUtil.setReasonInNotGrab(countdownOrderInfo);
//		        							ToastUtil.showToast(mContext.getApplicationContext(), "该订单已被抢走啦，下次手一定要快！");
		        							Toast.makeText(mContext.getApplicationContext(), "该订单已被抢走啦，下次手一定要快！",Toast.LENGTH_SHORT).show();
		        						}else{
		        							super.onErrorResponse(error);
		        						}
		        					}
		        				}
		        				)
		        		.setParams(params).build();
		        		mRequestQueue.add(request);
		        		mLoadingDialog.show();
		        		MobclickAgent.onEvent(mContext,
		        				"pagewaitList_btn_Wait");
		        	}
		        });
			} 
			//2100 且 reason有显示, 不可抢单,
			else {
//				if (orderInfo.getReason().equals(
//						mRes.getString(R.string.wait_order_waitfor))) {
//					this.setBtnSuccess(orderState_button,
//							orderinfo_item_killmeters_change, orderInfo);
//				} 
//				else{
				this.setBtnFailure(orderState_button,
						orderinfo_item_killmeters_change, orderInfo);
//				}
			}
		} 
		//非2100状态
		else {
			this.setBtnFailure(orderState_button,
					orderinfo_item_killmeters_change, orderInfo);
		}
	}

	/**
	 * 设置已抢单的样式
	 * @param orderState_button
	 * @param orderinfo_item_killmeters_change
	 * @param orderInfo
	 */
//	private void setBtnSuccess(final Button orderState_button,
//			final TextView orderinfo_item_killmeters_change,
//			final OrderInfo orderInfo) {
//		orderState_button.setOnClickListener(null);
//		orderState_button.setText(R.string.wait_order_already);
//		orderState_button
//				.setBackgroundResource(R.drawable.order_button_bg_pressed);
//		// 抢单后为按钮为以抢单, 底部文字为待用户选择
//		orderinfo_item_killmeters_change.setText(orderInfo.getReason());
//	}
	

	/**
	 * 订单不可抢下写入reason原因的按钮 , 和按钮底部文字显示为""
	 */
	private void setBtnFailure(final Button orderState_button,
			final TextView orderinfo_item_killmeters_change,
			final OrderInfo orderInfo) {
		orderState_button.setOnClickListener(null);
		orderState_button.setText(orderInfo.getReason());
		orderState_button
				.setBackgroundResource(R.drawable.order_button_bg_pressed);
		orderinfo_item_killmeters_change.setText("");
	}
	
	/**
	 * 设置2100 且 reason没有下的(成长可抢单下的底部文字描述)
	 */
	private void setBtnUnderText(TextView orderinfo_item_killmeters_change,
			OrderInfo orderInfo) {
		CharSequence killmeterText = "";
		//实时订单
		if (!OrderInfoHelper.getOrderType(orderInfo)) {
			String mileage = orderInfo.getRobDistance();
			if (TextUtils.isEmpty(mileage)) {
				killmeterText = mRes.getString(R.string.wait_order_empty_distance);
			} 
			else {
				//细化包含公里的文字描述
				if (mileage.contains("公里")) {
					mileage = mileage.split("公里")[0];
					SpannableString spannableString = new SpannableString(
							new StringBuilder("距您公里").insert(2, mileage) .toString());
					spannableString.setSpan(
							new ForegroundColorSpan(Color
									.parseColor("#FD6138")), 2, 2 + mileage
									.length(),
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					killmeterText = spannableString;
				}
			}
		} 
		orderinfo_item_killmeters_change.setText(killmeterText);
	}
	
}
