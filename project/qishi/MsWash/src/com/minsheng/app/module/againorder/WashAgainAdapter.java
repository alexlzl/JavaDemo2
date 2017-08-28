package com.minsheng.app.module.againorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.ModifyState;
import com.minsheng.app.entity.OrderListEntity.Infor.OrderObject;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.main.HomeActivity;
import com.minsheng.app.module.ordermap.MsMapView;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:当月已经完成订单数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @2015-3-15下午9:22:36
 * 
 */
public class WashAgainAdapter extends BaseListAdapter<OrderObject> {
	public List<String> orderType;
	private static final String TAG = "WashAgainAdapter";
	private ModifyState modifyStateBean;
	// private Double latitudeD = null;
	// private Double longitudeD = null;
	private List<Boolean> isNewOrderList;

	public WashAgainAdapter(Context context, List<OrderObject> list) {
		super(list, context);
		setItemType(list);
	}

	private void setItemType(List<OrderObject> list) {
		if (list != null) {
			orderType = new ArrayList<String>();
			int length = list.size();
			for (int i = 0; i < length; i++) {

				int orderState = list.get(i).getWashStatus();
				int payStatus = list.get(i).getPayStatus();
				switch (orderState) {
				case 9:
					/**
					 * 重洗
					 */
					if (payStatus == 0) {
						/**
						 * 未支付，对应确认收款
						 */
						orderType.add(i, MsConfiguration.WASH_AGAGIN_NOT_PAY);
					}
					if (payStatus == 1) {
						/**
						 * 已经支付，对应重洗完成
						 */
						orderType.add(i, MsConfiguration.WASH_AGAGIN_PAY_OVER);
					}
					break;

				}

			}
		}
		isNewOrderList = new ArrayList<Boolean>();
		/**
		 * 处理存储是否是新订单数据
		 */
		if (list != null && list.size() > 0) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				int isNewOrder = list.get(i).getDelivery_check_status();
				switch (isNewOrder) {
				case 0:
					/**
					 * 未查看
					 */
					isNewOrderList.add(i, false);
					break;
				case 1:
					/**
					 * 已经查看
					 */
					isNewOrderList.add(i, true);
					break;
				}
			}
		}

	}

	@Override
	public void setNewData(List<OrderObject> newData) {
		// TODO Auto-generated method stub
		super.setNewData(newData);
		setItemType(newData);
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(R.layout.wash_again_order_item,
					parent, false);
		}
		TextView tvLocation = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.wash_again_order_item_location_tv);
		LinearLayout llPhoneParent = (LinearLayout) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_phone_parent);
		final TextView tvPhone = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.wash_again_order_item_phonenum);
		TextView tvWashNum = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.wash_again_order_item_wash_num);
		TextView tvPrice = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.wash_again_order_item_price);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.wash_again_order_item_name);
		ImageView ivHead = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.wash_again_order_item_head);
		tvLocation.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvLocation.getPaint().setAntiAlias(true);
		tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvPhone.getPaint().setAntiAlias(true);
		Button bt = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.wash_again_order_item_bt);
		ImageView ivIsNewOrder = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.wash_again_order_item_is_neworder);
		/**
		 * 绑定数据
		 */
		String latitudeS = baseListData.get(position).getWdLatitude();
		Double latitudeD = 0.0;
		Double longitudeD = 0.0;
		if (!StringUtil.isEmpty(latitudeS)) {
			latitudeD = Double.parseDouble(latitudeS);
		}
		String longitudeS = baseListData.get(position).getWdLongitude();
		if (!StringUtil.isEmpty(longitudeS)) {
			longitudeD = Double.parseDouble(longitudeS);
		}
		// tvWashNum.setText("返洗衣物数量:"
		// + baseListData.get(position).getProductNum() + "件");
		if (!StringUtil.isEmpty(baseListData.get(position).getProductNum())) {
			tvWashNum.setText("返洗衣物数量:"
					+ baseListData.get(position).getProductNum() + "件");
		} else {
			tvWashNum.setText("返洗衣物数量:" + 0 + "件");
		}
		/**
		 * 是否是新订单的icon是否显示
		 */
		if (!isNewOrderList.get(position)) {
			ivIsNewOrder.setVisibility(View.VISIBLE);
		} else {
			ivIsNewOrder.setVisibility(View.GONE);
		}
		tvPrice.setText(baseListData.get(position).getOrderAmountD() + "");
		tvLocation.setText(baseListData.get(position).getAddress());
		tvPhone.setText(baseListData.get(position).getOrderMobile());
		tvName.setText(baseListData.get(position).getConsignee());
		LinearLayout tvMap = (LinearLayout) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_location_parent);
		MsApplication.imageLoader.displayImage(baseListData.get(position)
				.getHeadPicUrl(), ivHead, MsApplication.options,
				new ImageLoadingListener() {

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
		/**
		 * 事件监听
		 */
		tvMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent((Activity) basecontext,
						MsMapView.class);
				String latitudeS = baseListData.get(position).getWdLatitude();
				Double latitudeD = 0.0;
				Double longitudeD = 0.0;
				if (!StringUtil.isEmpty(latitudeS)) {
					latitudeD = Double.parseDouble(latitudeS);
				}
				String longitudeS = baseListData.get(position).getWdLongitude();

				if (!StringUtil.isEmpty(longitudeS)) {
					longitudeD = Double.parseDouble(longitudeS);
				}
				intent.putExtra("latitudeD", latitudeD);
				intent.putExtra("longitudeD", longitudeD);
				intent.putExtra("address", baseListData.get(position)
						.getAddress());
				MsStartActivity.startActivity((Activity) basecontext, intent);
			}
		});
		llPhoneParent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Uri uri = Uri.parse("tel:" + tvPhone.getText().toString());
				// Intent call = new Intent(Intent.ACTION_CALL, uri); // 直接播出电话
				// basecontext.startActivity(call);
				/**
				 * 进行拨号
				 */
				final Dialog dialog = new AlertDialog.Builder(basecontext)
						.create();
				dialog.show();
				dialog.setContentView(R.layout.call_phone_dialog);
				Window window = dialog.getWindow();
				window.setWindowAnimations(R.style.WindowAnimation);
				WindowManager.LayoutParams wm = window.getAttributes();
				wm.gravity = Gravity.CENTER;
				TextView tvTitle = (TextView) dialog
						.findViewById(R.id.call_phone_dialog_title);
				String name= baseListData.get(position).getConsignee();
				name =StringUtil.trimSpaceString(name);
				tvTitle.setText("我要给"
						+ name + "("
						+ baseListData.get(position).getOrderMobile() + ")");
				Button btCallphone = (Button) dialog
						.findViewById(R.id.call_phone_dialog_call);
				Button btSendMsg = (Button) dialog
						.findViewById(R.id.call_phone_dialog_msg);
				Button btCancle = (Button) dialog
						.findViewById(R.id.call_phone_dialog_cancle);
				btCallphone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.cancel();
						Uri uri = Uri.parse("tel:"
								+ baseListData.get(position).getOrderMobile());
						Intent call = new Intent(Intent.ACTION_CALL, uri); // 直接播出电话
						basecontext.startActivity(call);
					}
				});
				btCancle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				btSendMsg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.cancel();
						Intent intent = new Intent();

						// 系统默认的action，用来打开默认的短信界面
						intent.setAction(Intent.ACTION_SENDTO);

						// 需要发短息的号码
						intent.setData(Uri.parse("smsto:"
								+ baseListData.get(position).getOrderMobile()));
						basecontext.startActivity(intent);
					}
				});
			}
		});

		int orderState = baseListData.get(position).getWashStatus();
		int payStatus = baseListData.get(position).getPayStatus();
		switch (orderState) {
		case 9:
			/**
			 * 重洗
			 */
			if (payStatus == 0) {
				/**
				 * 未支付，对应确认收款
				 */
				bt.setBackgroundResource(R.drawable.order_list_button_bg_yellow);
				bt.setText("确认收款");
				bt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						final Dialog dialog = new AlertDialog.Builder(
								basecontext).create();
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
								getMoney(position);
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
			if (payStatus == 1) {
				/**
				 * 已经支付，对应重洗完成
				 */
				bt.setBackgroundResource(R.drawable.order_list_button_bg_green);
				bt.setText("重洗完成");
				bt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						final Dialog dialog = new AlertDialog.Builder(
								basecontext).create();
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
								changeOrderState(position, MsConfiguration.FIVE);
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
			break;

		}
		return convertView;
	}

	/**
	 * 
	 * 
	 * @describe:确认收款
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-26上午11:16:23
	 * 
	 */
	public void getMoney(int position) {
		ViewUtil.showRoundProcessDialog(basecontext);
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", baseListData.get(position).getOrderId());
		map.put("washStatus", baseListData.get(position).getWashStatus());
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(basecontext, paramsIn,
				MsRequestConfiguration.GET_MONEY,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						ViewUtil.dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerGetMoney.sendMessage(message);
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
						ViewUtil.dismissRoundProcessDialog();
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
	 * 确认收款消息返回
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
					Intent intent = new Intent(basecontext, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					MsStartActivity.startActivity((Activity) basecontext,
							intent);
				} else {
					if (modifyStateBean != null) {
						LogUtil.d(TAG, modifyStateBean.getMsg());
						MsToast.makeText(basecontext, modifyStateBean.getMsg())
								.show();
					} else {
						LogUtil.d(TAG, "收取现金失败");
						MsToast.makeText(basecontext, "收取现金失败").show();
					}
				}
				break;
			case MsConfiguration.FAIL:
				LogUtil.d(TAG, "收取现金失败");
				MsToast.makeText(basecontext, "收取现金失败").show();
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:修改订单状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-26下午5:44:17
	 * 
	 */

	public void changeOrderState(int position, int state) {
		ViewUtil.showRoundProcessDialog(basecontext);
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", baseListData.get(position).getOrderId());
		map.put("changeStatusType", state);
		map.put("washStatus", baseListData.get(position).getWashStatus());
		map.put("orderMobile", baseListData.get(position).getOrderMobile());
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(basecontext, paramsIn,
				MsRequestConfiguration.MODIFY_ORDER_STATE,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						ViewUtil.dismissRoundProcessDialog();
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
						ViewUtil.dismissRoundProcessDialog();
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
					LogUtil.d(TAG, "修改订单状态成功");
					Intent intent = new Intent(basecontext, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					MsStartActivity.startActivity((Activity) basecontext,
							intent);
				} else {
					if (modifyStateBean != null) {
						LogUtil.d(TAG, modifyStateBean.getMsg());
						MsToast.makeText(basecontext, modifyStateBean.getMsg())
								.show();
					} else {
						LogUtil.d(TAG, "修改订单状态失败");
						MsToast.makeText(basecontext, "修改订单状态失败").show();
					}
				}
				break;
			case MsConfiguration.FAIL:
				LogUtil.d(TAG, "修改订单状态失败");
				MsToast.makeText(basecontext, "修改订单状态失败").show();
				break;
			}
		}

	};
}
