package com.minsheng.app.module.orderdetail;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
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
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.appointmentorder.CompleteOrder;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.TimeUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:预约订单详情页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-6下午4:46:52
 * 
 */
public class AppointmentOrderDetail extends BaseActivity {
	private static final String TAG = "AppointmentOrderDetail";
	private String orderType;
	private Button bt;
	private int orderId;
	private OrderDetail orderDetailInfor;
	private TextView tvName, tvPhoneNum, tvAddress, tvOrderNum,
			tvGetClothesTime, tvSendClothesTiem, tvRemark;
	private ImageView ivUserHead;
	private ModifyState modifyStateBean;
	private ModifyState changeOrderTime;
	private ModifyState cancelChangeOrderTime;
	// private int orderState;
	private boolean isBackreFreshData;
	private int businessId;
	private int orderStateDetail;
	private String orderMobile;
	private TextView tvChangeTime;
	private int changeTimeType;
	private TextView tvOrderPrice;
	private TextView mtvRemark;
	private  TextView tvShopName;
	private TextView tvShopSn;
	private TextView tvCancelOrder;
	private TextView tvRemarkDriver;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.appointment_order_detail);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		orderType = getIntent().getStringExtra(MsConfiguration.ORDER_TYPE);
		orderMobile = getIntent().getStringExtra(
				MsConfiguration.MOBILE_PHONE_KEY);
		bt = (Button) findViewById(R.id.appointment_order_detail_bt);
		orderId = getIntent().getIntExtra(MsConfiguration.ORDER_ID_KEY, 0);
		// orderState = getIntent()
		// .getIntExtra(MsConfiguration.ORDER_STATE_KEY, 0);
		businessId = getIntent()
				.getIntExtra(MsConfiguration.BUSINESS_ID_KEY, 0);
		ivUserHead = (ImageView) findViewById(R.id.appointment_order_detail_customericon);
		tvName = (TextView) findViewById(R.id.appointment_order_detail_customername_content);
		tvPhoneNum = (TextView) findViewById(R.id.appointment_order_detail_customerphone_content);
		tvAddress = (TextView) findViewById(R.id.appointment_order_detail_address);
		tvOrderNum = (TextView) findViewById(R.id.appointment_order_detail_ordernum);
		tvGetClothesTime = (TextView) findViewById(R.id.appointment_order_detail_getclothes_time);
		tvSendClothesTiem = (TextView) findViewById(R.id.appointment_order_detail_sendclothes_time);
		tvRemark = (TextView) findViewById(R.id.appointment_order_detail_remark);
		tvChangeTime = (TextView) findViewById(R.id.appointment_order_detail_changetime);
		tvOrderPrice = (TextView) findViewById(R.id.appointment_order_detail_price);
		mtvRemark = (TextView) findViewById(R.id.appointment_order_detail_order_remark);
		tvShopName=(TextView) findViewById(R.id.appointment_order_detail_shopname);
		tvShopSn=(TextView) findViewById(R.id.appointment_order_detail_ordersn);
		tvRemarkDriver=(TextView) findViewById(R.id.appointment_order_detail_order_remark_driver);
		LogUtil.d(TAG, "businessId==" + businessId + "orderId==" + orderId);
		tvCancelOrder=(TextView) findViewById(R.id.appointment_order_detail_cancelorder);
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
						LogUtil.d(TAG, "onSuccess==" + arg2);
						dismissRoundProcessDialog();
					}

					@Override
					protected OrderDetail parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
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

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		tvChangeTime.setOnClickListener(this);
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
					LogUtil.d(TAG, "详情页订单状态" + orderStateDetail);
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

	/**
	 * 
	 * 
	 * @describe:绑定控件数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-21下午5:24:45
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
		// 标记已经成功进入详情页，返回首页可以刷新未读数据
		if (MsApplication.orderListChangeListener != null) {
			MsApplication.orderListChangeListener.orderListChange();
		}

		/**
		 * 初始化底部按钮显示状态
		 */
		if (MsConfiguration.COMPLETE_ORDER.equals(orderType)) {
			bt.setBackgroundResource(R.drawable.order_list_button_bg_yellow);
			bt.setText("完善订单");
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					final Dialog dialog = new AlertDialog.Builder(
							AppointmentOrderDetail.this).create();
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
			bt.setBackgroundResource(R.drawable.order_list_button_bg_green);
			bt.setText("我在路上");
			bt.setOnClickListener(this);
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					final Dialog dialog = new AlertDialog.Builder(
							AppointmentOrderDetail.this).create();
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
							changeOrderState();
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
			bt.setBackgroundResource(R.drawable.bt_gray_bg);
			bt.setText("待用户确认");
		}
		tvShopSn.setText(orderDetailInfor.getInfo().getOrderDetail().getWashOrderNumber()+"(店)");
		tvShopName.setText(orderDetailInfor.getInfo().getOrderDetail().getBusinessName());
		mtvRemark.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getWashRemark());
		tvOrderPrice.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getOrderAmountD()
				+ "元");
		tvName.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getConsignee());
		tvPhoneNum.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getOrderMobile());
		tvAddress.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getAddress());
		tvOrderNum.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getOrderSn());
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
	 * @describe:改变订单状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-21下午5:59:26
	 * 
	 */
	public void changeOrderState() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		map.put("changeStatusType", MsConfiguration.ZERO);
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
						ViewUtil.dismissRoundProcessDialog();
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
					isBackreFreshData = true;
					orderStateDetail = modifyStateBean.getInfo()
							.getWashStatus();
					LogUtil.d(TAG, "修改订单状态成功" + "新的订单状态" + orderStateDetail);
					bt.setBackgroundResource(R.drawable.order_list_button_bg_yellow);
					bt.setText("完善订单");
					bt.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							final Dialog dialog = new AlertDialog.Builder(
									AppointmentOrderDetail.this).create();
							dialog.show();
							dialog.setContentView(R.layout.change_time_dialog);
							Window window = dialog.getWindow();
							window.setWindowAnimations(R.style.WindowAnimation);
							WindowManager.LayoutParams wm = window
									.getAttributes();
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
									Intent intent = new Intent(baseContext,
											CompleteOrder.class);
									intent.putExtra(
											MsConfiguration.BUSINESS_ID_KEY,
											businessId);
									intent.putExtra(
											MsConfiguration.ORDER_ID_KEY,
											orderId);
									intent.putExtra(
											MsConfiguration.ORDER_STATE_KEY,
											orderStateDetail);
									MsStartActivity.startActivity(baseActivity,
											intent);
									MsStartActivity.startActivity(
											(Activity) baseContext, intent);
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("isFreshData", isBackreFreshData);
		// 设置结果，并进行传送
		setResult(MsConfiguration.APPOINT_ORDER_TO_DETAIL, intent);
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.appointment_order_detail_changetime:
			if (changeTimeType == 1) {
				MsToast.makeText(AppointmentOrderDetail.this, "请受理完成后再进行变更")
						.show();
				return;
			}
			showChangeTimeDialog();
			break;
		case R.id.appointment_order_detail_cancelorder:
			/**
			 * 取消订单
			 */
			showChangeDialog(ButtonType.CANCEL_ORDER);
			break;
		case R.id.appointment_order_detail_order_remark_driver:
			/**
			 * 骑士备注
			 */
			showChangeDialog(ButtonType.DRIVER_REMARK);
			break;
		}
	}
	public  enum ButtonType{
		DRIVER_REMARK,CANCEL_ORDER
		
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
		switch(type){
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
				
				switch(type){
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
	 * @describe:显示时间变更对话框
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-5-4上午11:26:31
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
				/**
				 * 取消变更
				 */
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
						ViewUtil.dismissRoundProcessDialog();
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
					tvChangeTime.setText("取消修改");
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
						ViewUtil.dismissRoundProcessDialog();
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
