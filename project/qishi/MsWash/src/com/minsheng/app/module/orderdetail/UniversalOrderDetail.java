package com.minsheng.app.module.orderdetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.ModifyState;
import com.minsheng.app.entity.OrderDetail;
import com.minsheng.app.entity.WashAgainParam;
import com.minsheng.app.entity.OrderDetail.Infor.Detail.OrderList;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.appointmentorder.CompleteOrder;
import com.minsheng.app.module.main.HomeActivity;
import com.minsheng.app.module.sendshoporder.OrderInforMatch;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.TimeUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:普通订单详情页面和返厂重洗页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-6下午4:45:28
 * 
 */
public class UniversalOrderDetail extends BaseActivity {
	private static final String TAG = "UniversalOrderDetail";
	private ListView lv;
	private UniversalOrderDetailAdapter adapter;
	private View headView;
	private Button btOne, btTwo;
	public static String orderType;
	private RelativeLayout rlBottom;
	private OrderDetail orderDetailInfor;
	private TextView tvName, tvPhoneNum, tvAddress, tvOrderPrice,
			tvGetClothesTime, tvSendClothesTiem, tvRemark, tvSn;
	private ImageView ivUserHead;
	private List<OrderList> orderList;
	private int orderId;
	// public int orderState;
	public int orderStateDetail;
	public ModifyState washAgainBean;
	private ModifyState changeOrderTime;
	private ModifyState cancelChangeOrderTime;
	public static String pageType;
	private ModifyState modifyStateBean;
	private String orderMobile;
	private TextView tvChangeTime;
	private int changeTimeType;
	private TextView mtvRemark;
	private TextView tvShopName;
	private TextView tvShopSn;
	private TextView tvCancelOrder;
	private TextView tvRemarkDriver;
	private LinearLayout llShopNumParent;
	private LinearLayout llShopNameParent;
	private View viewLine;
	private RelativeLayout rlRemark;
	private RelativeLayout cancelOrderParent;
	private int businessId;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.order_detail);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		rlBottom = (RelativeLayout) findViewById(R.id.wash_again_detail_bottomparent);
		lv = (ListView) findViewById(R.id.order_detail_lv);
		headView = baseLayoutInflater.inflate(R.layout.order_detail_head, null);
		tvChangeTime = (TextView) headView
				.findViewById(R.id.order_detail_changetime);
		btOne = (Button) findViewById(R.id.order_detail_bt_one);
		btTwo = (Button) findViewById(R.id.order_detail_bt_two);
		orderMobile = getIntent().getStringExtra(
				MsConfiguration.MOBILE_PHONE_KEY);
		orderType = getIntent().getStringExtra(MsConfiguration.ORDER_TYPE);
		orderId = getIntent().getIntExtra(MsConfiguration.ORDER_ID_KEY, 0);
		businessId = getIntent()
				.getIntExtra(MsConfiguration.BUSINESS_ID_KEY, 0);
		// orderState = getIntent().getIntExtra("washStatus", 0);
		pageType = getIntent().getStringExtra(MsConfiguration.PAGE_TYPE_KEY);
		LogUtil.d(TAG, "orderType===" + orderType + "===========orderId===="
				+ orderId + "businessId==" + businessId);
		ivUserHead = (ImageView) headView
				.findViewById(R.id.appointment_order_detail_customericon);
		tvName = (TextView) headView
				.findViewById(R.id.appointment_order_detail_customername_content);
		tvPhoneNum = (TextView) headView
				.findViewById(R.id.appointment_order_detail_customerphone_content);
		tvAddress = (TextView) headView
				.findViewById(R.id.order_detail_head_address);
		tvOrderPrice = (TextView) headView
				.findViewById(R.id.order_detail_head_price);
		tvGetClothesTime = (TextView) headView
				.findViewById(R.id.order_detail_head_getclothes_time);
		tvSendClothesTiem = (TextView) headView
				.findViewById(R.id.order_detail_head_sendclothes_time);
		tvRemark = (TextView) headView
				.findViewById(R.id.order_detail_head_remark);
		tvSn = (TextView) headView.findViewById(R.id.order_detail_head_sn);
		mtvRemark = (TextView) headView
				.findViewById(R.id.order_detail_head_order_remark);
		tvShopName = (TextView) headView
				.findViewById(R.id.order_detail_head_shop_name);
		tvShopSn = (TextView) headView
				.findViewById(R.id.order_detail_head_ordersn);
		tvRemarkDriver = (TextView) headView
				.findViewById(R.id.order_detail_head_remark_deriver);
		tvCancelOrder = (TextView) headView
				.findViewById(R.id.order_detail_head_cancelorder);
		llShopNumParent = (LinearLayout) headView
				.findViewById(R.id.appointment_order_detail_ordersn_parent);
		llShopNameParent = (LinearLayout) headView
				.findViewById(R.id.order_detail_head_shop_parent);
		viewLine = headView
				.findViewById(R.id.order_detail_head_shop_bottomline);
		rlRemark = (RelativeLayout) headView
				.findViewById(R.id.order_detail_head_remark_et_parent);
		cancelOrderParent = (RelativeLayout) headView
				.findViewById(R.id.order_detail_head_cancelorder_parent);

	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.ORDER_DETAIL,
				new BaseJsonHttpResponseHandler<OrderDetail>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, OrderDetail arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerDetailInfor.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							OrderDetail arg3) {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
					}

					@Override
					protected OrderDetail parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							orderDetailInfor = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									OrderDetail.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerDetailInfor.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerDetailInfor.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return orderDetailInfor;
					}
				});
	}

	/**
	 * 处理获取订单详情数据消息返回
	 */
	Handler handlerDetailInfor = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (orderDetailInfor != null && orderDetailInfor.getCode() == 0) {
					orderStateDetail = orderDetailInfor.getInfo()
							.getOrderDetail().getWashStatus();
					setViewData();
				} else {
					if (orderDetailInfor != null) {
						MsToast.makeText(baseContext, orderDetailInfor.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "获取数据失败").show();
					}
				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "获取数据失败").show();
				break;
			}
		}

	};

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		tvChangeTime.setOnClickListener(this);
		btOne.setOnClickListener(this);
		btTwo.setOnClickListener(this);
		tvCancelOrder.setOnClickListener(this);
		tvRemarkDriver.setOnClickListener(this);
	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "订单详情";
	}

	@Override
	protected String setRightText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int setLeftImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setMiddleImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setRightImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 
	 * 
	 * @describe:绑定数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-19上午10:06:53
	 * 
	 */
	public void setViewData() {
		/**
		 * 显示更改时间控件
		 */
		changeTimeType = orderDetailInfor.getInfo().getOrderDetail()
				.getApplyStatus();
		switch (changeTimeType) {
		case 0:
			/**
			 * 显示取消变更
			 */
			tvChangeTime.setVisibility(View.VISIBLE);
			tvChangeTime.setText("取消变更");
			break;
		case 1:
			/**
			 * 受理变更中
			 */
			// tvChangeTime.setVisibility(View.GONE);
			tvChangeTime.setVisibility(View.VISIBLE);
			tvChangeTime.setText("受理中");
			break;
		case 2:
			/**
			 * 显示变更时间
			 */
			tvChangeTime.setVisibility(View.VISIBLE);
			tvChangeTime.setText("变更时间");
		}
		/**
		 * 初始化底部按钮显示状态
		 */
		if (MsConfiguration.COMPLETE_ORDER.equals(orderType)) {
			tvCancelOrder.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams lp = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			llShopNameParent.setLayoutParams(lp);
			llShopNumParent.setVisibility(View.GONE);
			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.GONE);
			btOne.setBackgroundResource(R.drawable.order_list_button_bg_yellow);
			btOne.setText("完善订单");
			btOne.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					final Dialog dialog = new AlertDialog.Builder(
							UniversalOrderDetail.this).create();
					dialog.show();
					dialog.setContentView(R.layout.change_time_dialog);
					Window window = dialog.getWindow();
					window.setWindowAnimations(R.style.WindowAnimation);
					WindowManager.LayoutParams wm = window.getAttributes();
					wm.gravity = Gravity.CENTER;
					Button btConfirm = (Button) dialog
							.findViewById(R.id.change_time_dialog_confirm);
					Button btCancle = (Button) dialog
							.findViewById(R.id.change_time_dialog_cancle);
					TextView tvTitle = (TextView) dialog
							.findViewById(R.id.change_time_dialog_title);
					tvTitle.setText("你确定要更改状态吗?");
					btConfirm.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.cancel();
							Intent intent = new Intent(baseActivity,
									CompleteOrder.class);
							intent.putExtra(MsConfiguration.BUSINESS_ID_KEY,
									businessId);
							intent.putExtra(MsConfiguration.ORDER_ID_KEY,
									orderId);
							intent.putExtra(MsConfiguration.ORDER_STATE_KEY,
									orderStateDetail);
							MsStartActivity.startActivity(baseActivity, intent);
						}
					});
					btCancle.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.cancel();
							/**
							 * 取消变更
							 */
						}
					});

				}
			});
		}
		if (MsConfiguration.ON_THE_WAY.equals(orderType)) {
			tvCancelOrder.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams lp = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			llShopNameParent.setLayoutParams(lp);
			llShopNumParent.setVisibility(View.GONE);
			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.GONE);
			btOne.setBackgroundResource(R.drawable.order_list_button_bg_green);
			btOne.setText("我在路上");
			btOne.setOnClickListener(this);
			btOne.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					final Dialog dialog = new AlertDialog.Builder(
							UniversalOrderDetail.this).create();
					dialog.show();
					dialog.setContentView(R.layout.change_time_dialog);
					Window window = dialog.getWindow();
					window.setWindowAnimations(R.style.WindowAnimation);
					WindowManager.LayoutParams wm = window.getAttributes();
					wm.gravity = Gravity.CENTER;
					Button btConfirm = (Button) dialog
							.findViewById(R.id.change_time_dialog_confirm);
					Button btCancle = (Button) dialog
							.findViewById(R.id.change_time_dialog_cancle);
					TextView tvTitle = (TextView) dialog
							.findViewById(R.id.change_time_dialog_title);
					tvTitle.setText("你确定要更改状态吗?");
					btConfirm.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.cancel();
							changeOrderState(MsConfiguration.ZERO);
						}
					});
					btCancle.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.cancel();
							/**
							 * 取消变更
							 */
						}
					});

				}
			});
		}
		if (MsConfiguration.FINISH_ORDER_WAIT_CONFIRM.equals(orderType)) {
			RelativeLayout.LayoutParams lp = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			llShopNameParent.setLayoutParams(lp);
			llShopNumParent.setVisibility(View.GONE);
			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.GONE);
			btOne.setBackgroundResource(R.drawable.bt_gray_bg);
			btOne.setText("待用户确认");
			btOne.setClickable(false);
		}
		/**
		 * 处理详情页面改变时间控件显示状态
		 */
		changeTimeType = orderDetailInfor.getInfo().getOrderDetail()
				.getApplyStatus();
		LogUtil.d(TAG, "更改时间状态值===" + changeTimeType);
		switch (changeTimeType) {
		case 0:
			/**
			 * 显示取消变更
			 */
			tvChangeTime.setVisibility(View.VISIBLE);
			tvChangeTime.setText("取消变更");
			break;
		case 1:
			/**
			 * 不显示任何按钮
			 */
			tvChangeTime.setVisibility(View.VISIBLE);
			tvChangeTime.setText("受理中");
			break;
		case 2:
			/**
			 * 显示变更时间
			 */
			tvChangeTime.setVisibility(View.VISIBLE);
			tvChangeTime.setText("变更时间");
		}
		/**
		 * 已经完成订单====不能进行订单时间更改
		 */
		if (MsConfiguration.ORDER_OVER_CAN_WASH_AGAIN.equals(orderType)
				|| MsConfiguration.ORDER_OVER_CANNOT_WASH_AGAIN
						.equals(orderType)) {
			tvCancelOrder.setVisibility(View.GONE);
			tvRemarkDriver.setVisibility(View.GONE);
			rlRemark.setVisibility(View.GONE);
			tvChangeTime.setVisibility(View.GONE);
		}

		/**
		 * 标记已经成功进入详情页，返回首页可以刷新未读数据
		 */
		if (MsApplication.orderListChangeListener != null) {
			MsApplication.orderListChangeListener.orderListChange();
		}
		/**
		 * 取消订单=============详情不能进行订单时间更改
		 */
		if (MsConfiguration.CANCEL_ORDER.equals(orderType)) {
			/**
			 * 隐藏变更时间==========
			 */
			viewLine.setVisibility(View.GONE);
			cancelOrderParent.setVisibility(View.GONE);
			tvRemarkDriver.setVisibility(View.GONE);
			rlRemark.setVisibility(View.GONE);
			tvChangeTime.setVisibility(View.GONE);
			rlBottom.setVisibility(View.GONE);
			RelativeLayout.LayoutParams lp = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			llShopNameParent.setLayoutParams(lp);
			llShopNumParent.setVisibility(View.GONE);
		}
		/**
		 * 个人中心进入详情页面
		 */
		if (MsConfiguration.NORMAL_ORDER_LIST_TYPE.equals(orderType)) {
			tvRemarkDriver.setVisibility(View.GONE);
			rlRemark.setVisibility(View.GONE);
			tvChangeTime.setVisibility(View.GONE);
			rlBottom.setVisibility(View.GONE);
		}
		/**
		 * 送店订单===========我已送店
		 */
		if (MsConfiguration.SEND_SHOP_ORDER.equals(orderType)) {
			RelativeLayout.LayoutParams lp = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			llShopNameParent.setLayoutParams(lp);
			llShopNumParent.setVisibility(View.GONE);
			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.GONE);
			btOne.setText("我已送店");
			btOne.setBackgroundResource(R.drawable.order_list_button_bg_green);

		}
		/**
		 * 取衣订单========我已取走
		 */
		if (MsConfiguration.GET_CLOTHES_BACK.equals(orderType)) {
			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.GONE);
			btOne.setText("我已取走");
			btOne.setBackgroundResource(R.drawable.order_list_button_bg_green);

		}
		/**
		 * 送衣订单=========我在派送
		 */
		if (MsConfiguration.IS_SENDDING.equals(orderType)) {
			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.GONE);
			btOne.setText("我在派送");
			btOne.setBackgroundResource(R.drawable.order_list_button_bg_green);

		}
		/**
		 * 送衣订单，未支付状态
		 */
		if (MsConfiguration.NOT_PAY.equals(orderType)) {

			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.VISIBLE);
			btOne.setText("返厂重洗");
			btOne.setBackgroundResource(R.drawable.order_list_button_bg_yellow);
			btTwo.setText("收取现金");
			btTwo.setBackgroundResource(R.drawable.order_list_button_bg_green);

		}
		/**
		 * 返厂重新
		 */
		if (MsConfiguration.WASH_AGAIN.equals(orderType)
				|| MsConfiguration.ORDER_OVER_CAN_WASH_AGAIN.equals(orderType)) {
			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.GONE);
			btOne.setText("确认返厂重洗");
			btOne.setBackgroundResource(R.drawable.order_list_button_bg_yellow);
		}
		/**
		 * 重洗完成
		 */
		if (MsConfiguration.WASH_AGAGIN_PAY_OVER.equals(orderType)) {
			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.GONE);
			btOne.setText("重洗完成");
			btOne.setBackgroundResource(R.drawable.order_list_button_bg_green);

		}
		/**
		 * 确认收款
		 */

		if (MsConfiguration.CONFIRM_GET_CASH.equals(orderType)
				|| MsConfiguration.WASH_AGAGIN_NOT_PAY.equals(orderType)) {
			btOne.setVisibility(View.VISIBLE);
			btTwo.setVisibility(View.GONE);
			btOne.setText("确认收款");
			btOne.setBackgroundResource(R.drawable.order_list_button_bg_yellow);

		}
		/**
		 * 洗衣完成，不能返厂重洗
		 */
		if (MsConfiguration.ORDER_OVER_CANNOT_WASH_AGAIN.equals(orderType)) {

			rlBottom.setVisibility(View.GONE);
			tvChangeTime.setVisibility(View.GONE);
		}
		/**
		 * 绑定视图数据
		 */
		tvShopSn.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getWashOrderNumber()
				+ "(店)");
		tvShopName.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getBusinessName());
		mtvRemark.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getWashRemark());
		tvOrderPrice.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getOrderAmountD()
				+ "元");
		orderList = orderDetailInfor.getInfo().getOrderDetail()
				.getDeliveryProductDetails();
		adapter = new UniversalOrderDetailAdapter(orderList, baseContext);
		lv.addHeaderView(headView);
		lv.setAdapter(adapter);
		tvName.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getConsignee());
		tvPhoneNum.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getOrderMobile());
		tvAddress.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getAddress());
		tvOrderPrice.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getOrderAmountD()
				+ "元");
		tvSn.setText(orderDetailInfor.getInfo().getOrderDetail().getOrderSn());
		tvGetClothesTime.setText(TimeUtil.changeTime_M_D(orderDetailInfor
				.getInfo().getOrderDetail().getTakeDate())
				+ " "
				+ orderDetailInfor.getInfo().getOrderDetail().getTakeTime());
		tvSendClothesTiem.setText(TimeUtil.changeTime_M_D(orderDetailInfor
				.getInfo().getOrderDetail().getGiveDate())
				+ " "
				+ orderDetailInfor.getInfo().getOrderDetail().getGiveTime());
		tvRemark.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getWashRemark());
		MsApplication.imageLoader.displayImage(orderDetailInfor.getInfo()
				.getOrderDetail().getHeadPicUrl(), ivUserHead,
				MsApplication.options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * 
	 * 
	 * @describe:返厂重洗
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-25下午8:57:00
	 * 
	 */
	private void washAgain(List<WashAgainParam> washAgainList) {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		map.put("deliveryProductDetails", washAgainList);
		map.put("washStatus", orderStateDetail);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.WASH_AGAIN,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerWashAgain.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ModifyState arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						dismissRoundProcessDialog();
					}

					@Override
					protected ModifyState parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							washAgainBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerWashAgain.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerWashAgain.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return washAgainBean;
					}
				});
	}

	/**
	 * 处理返厂重新消息返回
	 */
	Handler handlerWashAgain = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (washAgainBean != null && washAgainBean.getCode() == 0) {
					Intent intent = new Intent(baseContext, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					MsStartActivity.startActivity(baseActivity, intent);
				} else {
					if (washAgainBean != null) {
						MsToast.makeText(baseContext, washAgainBean.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "返厂重洗失败").show();
					}
				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "返厂重洗失败").show();
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:更新订单状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-26下午3:53:39
	 * 
	 */
	public void changeOrderState(int state) {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		map.put("changeStatusType", state);
		map.put("washStatus", orderStateDetail);
		map.put("orderMobile", orderMobile);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.MODIFY_ORDER_STATE,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerChangeReadState.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ModifyState arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						dismissRoundProcessDialog();
					}

					@Override
					protected ModifyState parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							modifyStateBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerChangeReadState.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerChangeReadState.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return modifyStateBean;
					}
				});
	}

	/**
	 * 处理修改订单消息返回
	 */
	Handler handlerChangeReadState = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (modifyStateBean != null && modifyStateBean.getCode() == 0) {
					orderStateDetail = modifyStateBean.getInfo()
							.getWashStatus();
					LogUtil.d(TAG, "修改订单状态成功" + "新订单状态" + orderStateDetail);
					Intent intent = new Intent(baseContext, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					MsStartActivity.startActivity((Activity) baseContext,
							intent);
				} else {
					if (modifyStateBean != null) {
						LogUtil.d(TAG, modifyStateBean.getMsg());
						MsToast.makeText(baseContext, modifyStateBean.getMsg())
								.show();
					} else {
						LogUtil.d(TAG, "修改订单状态失败");
						MsToast.makeText(baseContext, "修改订单状态失败").show();
					}
				}
				break;
			case MsConfiguration.FAIL:
				LogUtil.d(TAG, "修改订单状态失败");
				MsToast.makeText(baseContext, "修改订单状态失败").show();
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:收取现金
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-26下午4:30:04
	 * 
	 */
	public void getMoney() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		map.put("washStatus", orderStateDetail);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.GET_MONEY,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerGetMoney.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ModifyState arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						dismissRoundProcessDialog();
					}

					@Override
					protected ModifyState parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							modifyStateBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerGetMoney.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerGetMoney.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return modifyStateBean;
					}
				});
	}

	/**
	 * 处理收取现金消息返回
	 */
	Handler handlerGetMoney = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (modifyStateBean != null && modifyStateBean.getCode() == 0) {
					LogUtil.d(TAG, "收取现金态成功");
					Intent intent = new Intent(baseContext, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					MsStartActivity.startActivity((Activity) baseContext,
							intent);
				} else {
					if (modifyStateBean != null) {
						LogUtil.d(TAG, modifyStateBean.getMsg());
						MsToast.makeText(baseContext, modifyStateBean.getMsg())
								.show();
					} else {
						LogUtil.d(TAG, "收取现金失败");
						MsToast.makeText(baseContext, "收取现金失败").show();
					}
				}
				break;
			case MsConfiguration.FAIL:
				LogUtil.d(TAG, "收取现金失败");
				MsToast.makeText(baseContext, "收取现金失败").show();
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.order_detail_changetime:
			/**
			 * 改变订单时间
			 */
			if (changeTimeType == 1) {
				MsToast.makeText(UniversalOrderDetail.this, "请受理完成后再进行变更")
						.show();
				return;
			}
			showChangeTimeDialog();
			break;
		case R.id.order_detail_bt_one:
			/**
			 * 设置左侧控件事件================
			 */
			final Dialog dialog = new AlertDialog.Builder(
					UniversalOrderDetail.this).create();
			dialog.show();
			dialog.setContentView(R.layout.change_time_dialog);
			Window window = dialog.getWindow();
			window.setWindowAnimations(R.style.WindowAnimation);
			WindowManager.LayoutParams wm = window.getAttributes();
			wm.gravity = Gravity.CENTER;
			Button btConfirm = (Button) dialog
					.findViewById(R.id.change_time_dialog_confirm);
			Button btCancle = (Button) dialog
					.findViewById(R.id.change_time_dialog_cancle);
			TextView tvTitle = (TextView) dialog
					.findViewById(R.id.change_time_dialog_title);
			tvTitle.setText("你确定要更改状态吗?");
			btConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.cancel();
					setLeftViewListener();
				}
			});
			btCancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.cancel();
					/**
					 * 取消变更
					 */
				}
			});

			break;
		case R.id.order_detail_bt_two:
			/**
			 * 右侧控件事件=================
			 */
			final Dialog dialogRight = new AlertDialog.Builder(
					UniversalOrderDetail.this).create();
			dialogRight.show();
			dialogRight.setContentView(R.layout.change_time_dialog);
			Window windowRight = dialogRight.getWindow();
			windowRight.setWindowAnimations(R.style.WindowAnimation);
			WindowManager.LayoutParams wmRight = windowRight.getAttributes();
			wmRight.gravity = Gravity.CENTER;
			Button btConfirmRight = (Button) dialogRight
					.findViewById(R.id.change_time_dialog_confirm);
			Button btCancleRight = (Button) dialogRight
					.findViewById(R.id.change_time_dialog_cancle);
			TextView tvTitleRight = (TextView) dialogRight
					.findViewById(R.id.change_time_dialog_title);
			tvTitleRight.setText("你确定要更改状态吗?");
			btConfirmRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialogRight.cancel();

					if (MsConfiguration.NOT_PAY.equals(orderType)) {
						/**
						 * 送衣订单=========显示收取现金
						 */
						getMoney();
					}
				}
			});
			btCancleRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialogRight.cancel();
					/**
					 * 取消变更
					 */
				}
			});

			break;
		case R.id.order_detail_head_cancelorder:
			/**
			 * 取消订单
			 */
			showChangeDialog(ButtonType.CANCEL_ORDER);
			break;
		case R.id.order_detail_head_remark_deriver:
			/**
			 * 骑士备注
			 */
			showChangeDialog(ButtonType.DRIVER_REMARK);
			break;
		}
	}

	public enum ButtonType {
		DRIVER_REMARK, CANCEL_ORDER

	}

	public void showChangeDialog(final ButtonType type) {
		final Dialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		dialog.setContentView(R.layout.change_time_dialog);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.WindowAnimation);
		WindowManager.LayoutParams wm = window.getAttributes();
		wm.gravity = Gravity.CENTER;
		Button btConfirm = (Button) dialog
				.findViewById(R.id.change_time_dialog_confirm);
		Button btCancle = (Button) dialog
				.findViewById(R.id.change_time_dialog_cancle);
		final TextView tvTitle = (TextView) dialog
				.findViewById(R.id.change_time_dialog_title);
		switch (type) {
		case DRIVER_REMARK:
			/**
			 * 骑士备注
			 */
			tvTitle.setText("您确定要加入订单备注吗？");
			break;
		case CANCEL_ORDER:
			/**
			 * 取消订单
			 */
			tvTitle.setText("您确定要取消订单吗？");
			break;
		}
		btConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.cancel();

				switch (type) {
				case DRIVER_REMARK:
					/**
					 * 骑士备注
					 */
					break;
				case CANCEL_ORDER:
					/**
					 * 取消订单
					 */
					break;
				}

			}
		});
		btCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.cancel();
				/**
				 * 取消
				 */
			}
		});
	}

	/**
	 * 
	 * 
	 * @describe:设置左侧控件事件
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-5-7上午11:19:29
	 * 
	 */
	private void setLeftViewListener() {
		/**
		 * 左侧控件事件===============================================
		 */
		if (MsConfiguration.SEND_SHOP_ORDER.equals(orderType)) {
			/**
			 * 送店订单====== 我已送店状态
			 */
			Intent intent = new Intent(baseActivity, OrderInforMatch.class);
			intent.putExtra(MsConfiguration.ORDER_ID_KEY, orderId);
			intent.putExtra("washStatus", orderStateDetail);
			MsStartActivity.startActivity(baseActivity, intent);
		}
		if (MsConfiguration.GET_CLOTHES_BACK.equals(orderType)) {
			/**
			 * 取衣订单============我已取走
			 */
			changeOrderState(MsConfiguration.THREE);
		}
		if (MsConfiguration.IS_SENDDING.equals(orderType)) {
			/**
			 * 送衣订单===========我在派送
			 */
			changeOrderState(MsConfiguration.FOUR);
		}
		if (MsConfiguration.NOT_PAY.equals(orderType)) {
			/**
			 * 送衣订单============返厂重洗，收取现金
			 */
			if (MsConfiguration.PAGE_TYPE_WASH_AGAIN.equals(pageType)) {
				/**
				 * 执行返厂重洗
				 */
				if (adapter.washAgainList != null
						&& adapter.washAgainList.size() > 0) {
					List<WashAgainParam> washAgainListN = new ArrayList<WashAgainParam>();
					for (int i = 0; i < adapter.washAgainList.size(); i++) {
						if (adapter.washAgainList.get(i) != null
								&& !"".equals(adapter.washAgainList.get(i))) {
							washAgainListN.add(adapter.washAgainList.get(i));
						}
					}
					if (washAgainListN != null && washAgainListN.size() > 0) {
						washAgain(washAgainListN);
					} else {
						MsToast.makeText(baseContext, "请先选择重洗衣服").show();
					}

				} else {
					MsToast.makeText(baseContext, "请先选择重洗衣服").show();
				}

			} else {
				/**
				 * 切换为返厂重洗状态
				 */
				pageType = MsConfiguration.PAGE_TYPE_WASH_AGAIN;
				adapter.notifyDataSetChanged();
				btTwo.setVisibility(View.GONE);
			}
		}

		if (MsConfiguration.WASH_AGAIN.equals(orderType)
				|| MsConfiguration.ORDER_OVER_CAN_WASH_AGAIN.equals(orderType)) {
			/**
			 * 可执行返厂重新状态
			 */

			if (MsConfiguration.PAGE_TYPE_WASH_AGAIN
					.equals(UniversalOrderDetail.pageType)) {
				/**
				 * 执行返厂重洗=====================
				 */
				if (adapter.washAgainList != null
						&& adapter.washAgainList.size() > 0) {
					List<WashAgainParam> washAgainList = new ArrayList<WashAgainParam>();
					for (int i = 0; i < adapter.washAgainList.size(); i++) {
						if (adapter.washAgainList.get(i) != null
								&& !"".equals(adapter.washAgainList.get(i))) {
							washAgainList.add(adapter.washAgainList.get(i));
						}
					}
					if (washAgainList != null && washAgainList.size() > 0) {
						washAgain(washAgainList);
					} else {
						MsToast.makeText(baseContext, "请先选择重洗衣服").show();
					}

				} else {
					MsToast.makeText(baseContext, "请先选择重洗衣服").show();
				}
			} else {
				/**
				 * 切换为返厂重洗页面，显示选择框按钮================
				 */
				pageType = MsConfiguration.PAGE_TYPE_WASH_AGAIN;
				adapter.notifyDataSetChanged();
			}

		}
		if (MsConfiguration.WASH_AGAGIN_PAY_OVER.equals(orderType)) {
			/**
			 * 重洗订单=============显示重洗完成
			 */
			changeOrderState(MsConfiguration.FIVE);
		}
		if (MsConfiguration.CONFIRM_GET_CASH.equals(orderType)
				|| MsConfiguration.WASH_AGAGIN_NOT_PAY.equals(orderType)) {
			/**
			 * 显示确认收款
			 */
			getMoney();
		}
		if (MsConfiguration.ORDER_OVER_CANNOT_WASH_AGAIN.equals(orderType)) {
			/**
			 * 已完成订单========无状态显示(不能再次重洗)
			 */
			rlBottom.setVisibility(View.GONE);
			tvChangeTime.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 * 
	 * @describe:显示改变时间窗口
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-5-7上午10:20:57
	 * 
	 */
	public void showChangeTimeDialog() {
		final Dialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		dialog.setContentView(R.layout.change_time_dialog);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.WindowAnimation);
		WindowManager.LayoutParams wm = window.getAttributes();
		wm.gravity = Gravity.CENTER;
		Button btConfirm = (Button) dialog
				.findViewById(R.id.change_time_dialog_confirm);
		Button btCancle = (Button) dialog
				.findViewById(R.id.change_time_dialog_cancle);
		TextView tvTitle = (TextView) dialog
				.findViewById(R.id.change_time_dialog_title);
		if (changeTimeType == 0) {
			tvTitle.setText("您确定要取消时间变更吗？");
		}
		btConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.cancel();
				/**
				 * 执行变更时间
				 */
				switch (changeTimeType) {
				case 0:
					/**
					 * 执行取消变更时间
					 */
					cancelChangeOrderTime();
					break;

				case 2:
					/**
					 * 执行变更时间
					 */
					changeOrderTime();
					break;
				}
			}
		});
		btCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
	}

	/**
	 * 
	 * 
	 * @describe:更改订单时间
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-5-4上午11:12:05
	 * 
	 */
	public void changeOrderTime() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.MODIFY_ORDER_TIME,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerChangeOrderTime.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ModifyState arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
					}

					@Override
					protected ModifyState parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							changeOrderTime = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerChangeOrderTime.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerChangeOrderTime.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return modifyStateBean;
					}
				});
	}

	/**
	 * 处理修改订单消息返回
	 */
	Handler handlerChangeOrderTime = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (changeOrderTime != null && changeOrderTime.getCode() == 0) {
					/**
					 * 修改订单时间成功
					 */
					MsToast.makeText(baseContext, "请求修改订单时间成功").show();
					tvChangeTime.setText("取消变更");
					tvChangeTime.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							changeTimeType = 0;
							showChangeTimeDialog();
						}
					});

				} else {
					if (changeOrderTime != null) {

						LogUtil.d(TAG, changeOrderTime.getMsg());
						MsToast.makeText(baseContext, changeOrderTime.getMsg())
								.show();
					} else {
						LogUtil.d(TAG, "请求修改订单时间失败");
						MsToast.makeText(baseContext, "请求修改订单时间失败").show();
					}
				}
				break;
			case MsConfiguration.FAIL:
				LogUtil.d(TAG, "请求修改订单时间失败");
				MsToast.makeText(baseContext, "请求修改订单时间失败").show();
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:取消时间变更请求
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-5-4上午11:25:00
	 * 
	 */
	public void cancelChangeOrderTime() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.CANCEL_MODIFY_ORDER_TIME,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerCancelChangeOrderTime.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ModifyState arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
					}

					@Override
					protected ModifyState parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							cancelChangeOrderTime = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerCancelChangeOrderTime.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerCancelChangeOrderTime.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return modifyStateBean;
					}
				});
	}

	/**
	 * 处理取消修改订单消息返回
	 */
	Handler handlerCancelChangeOrderTime = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (cancelChangeOrderTime != null
						&& cancelChangeOrderTime.getCode() == 0) {
					/**
					 * 修改订单时间成功
					 */
					MsToast.makeText(baseContext, "请求取消修改订单时间成功").show();
					tvChangeTime.setText("变更时间");
					tvChangeTime.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							changeTimeType = 2;
							showChangeTimeDialog();
						}
					});
				} else {
					if (cancelChangeOrderTime != null) {

						LogUtil.d(TAG, changeOrderTime.getMsg());
						MsToast.makeText(baseContext,
								cancelChangeOrderTime.getMsg()).show();
					} else {
						LogUtil.d(TAG, "请求取消修改订单时间失败");
						MsToast.makeText(baseContext, "请求取消修改订单时间失败").show();
					}
				}
				break;
			case MsConfiguration.FAIL:
				LogUtil.d(TAG, "请求取消修改订单时间失败");
				MsToast.makeText(baseContext, "请求取消修改订单时间失败").show();
				break;
			}
		}

	};
}
