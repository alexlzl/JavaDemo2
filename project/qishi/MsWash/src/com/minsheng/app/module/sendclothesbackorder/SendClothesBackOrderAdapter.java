package com.minsheng.app.module.sendclothesbackorder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
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
import com.minsheng.app.module.orderdetail.UniversalOrderDetail;
import com.minsheng.app.module.ordermap.MsMapView;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.TimeUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:送衣订单数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-14下午4:44:04
 * 
 */
public class SendClothesBackOrderAdapter extends BaseListAdapter<OrderObject> {
	private static final String TAG = "SendClothesBackOrderAdapter";
	private ModifyState modifyStateBean;
	private boolean isPlay;
	protected List<String> orderType;
	private OrderChangeListener mlistener;
	// private Double latitudeD = null;
	// private Double longitudeD = null;
	private List<Long> gapTimeList = new ArrayList<Long>();
	private List<Boolean> isNewOrderList;
	private int changePosition;
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (!isPlay)
				return;
			handler.postDelayed(this, 1000);
			if (gapTimeList != null && gapTimeList.size() > 0) {
				for (int i = 0; i < gapTimeList.size(); i++) {
					if (gapTimeList.get(i) > 0) {
						gapTimeList.set(i, gapTimeList.get(i) - 1);
					}
				}

			}
			notifyDataSetChanged();
		}
	};

	private Handler handler = new Handler();

	public SendClothesBackOrderAdapter(Context context, List<OrderObject> list) {
		super(list, context);
		setDataType(list);

	}

	private void setDataType(List<OrderObject> list) {
		if (list != null) {
			orderType = new ArrayList<String>();
			int lenght = list.size();
			for (int i = 0; i < lenght; i++) {
				int orderState = list.get(i).getWashStatus();
				int payStatus = list.get(i).getPayStatus();
				switch (orderState) {
				case 7:
					/**
					 * 我在派送
					 */
					orderType.add(i,MsConfiguration.IS_SENDDING);
					break;

				case 8:
					if (payStatus == 0) {
						/**
						 * 未支付状态，显示返厂重新，收取现金
						 */
						orderType.add(i,MsConfiguration.NOT_PAY);
					}
					if (payStatus == 1) {
						/*
						 * 支付完成
						 */

					}

					break;
				}
			}
		}

		/**
		 * 处理倒计时
		 */
		if (gapTimeList != null && gapTimeList.size() > 0) {
			/**
			 * 分页数据
			 */
			LogUtil.d(TAG, "分页数据");
			for (int i = gapTimeList.size(); i < list.size(); i++) {
				String subTime = null;
				if (!StringUtil.isEmpty(list.get(i).getGiveTime())) {
					int index = list.get(i).getGiveTime().indexOf("-");
					subTime = list.get(i).getGiveTime().substring(0, index);
				}
				String appointDate = TimeUtil.changeTimeStempToDate(list.get(i)
						.getGiveDate()) + " " + subTime;
				/**
				 * 日期转换成时间戳
				 */
				LogUtil.d(TAG, "预约时间" + appointDate + "时间段" + subTime);
				long appointTime = TimeUtil.getTimeStemp(appointDate,
						"yyyy-MM-dd HH:mm");
				long currentTime = list.get(i).getIntervalTime();
				long gapTime = appointTime - currentTime;
				if (gapTime > 0) {
					gapTimeList.add(i, gapTime);
					LogUtil.d(TAG, "时间差" + gapTime);
				} else {
					gapTimeList.add(i, 0L);
				}
			}

		} else {
			/**
			 * 初始化数据
			 */
			LogUtil.d(TAG, "初始化数据");
			for (int i = 0; i < list.size(); i++) {
				String subTime = null;
				if (!StringUtil.isEmpty(list.get(i).getGiveTime())) {
					int index = list.get(i).getGiveTime().indexOf("-");
					subTime = list.get(i).getGiveTime().substring(0, index);
				} else {
					subTime = "00:00";
				}

				String appointDate = TimeUtil.changeTimeStempToDate(list.get(i)
						.getGiveDate()) + " " + subTime;
				/**
				 * 日期转换成时间戳
				 */
				LogUtil.d(TAG, "预约送衣时间" + appointDate);
				long appointTime = TimeUtil.getTimeStemp(appointDate,
						"yyyy-MM-dd HH:mm");
				long currentTime = list.get(i).getIntervalTime();
				long gapTime = appointTime - currentTime;
				if (gapTime > 0) {
					gapTimeList.add(i, gapTime);
				} else {
					gapTimeList.add(i, 0L);
				}
			}
		}
		/**
		 * 处理存储是否是新订单数据
		 */

		isNewOrderList = new ArrayList<Boolean>();
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
			LogUtil.d(TAG, "新订单状态" + isNewOrderList.toString());
		}

	}

	@Override
	public void setNewData(List<OrderObject> newData) {
		// TODO Auto-generated method stub
		super.setNewData(newData);
		setDataType(newData);
	}

	public void start() {
		isPlay = true;
		runnable.run();
	}

	public void stop() {
		isPlay = false;
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.sendclothesback_order_item, parent, false);
		}
		TextView tvLocation = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.sendclothesback_order_list_item_location_tv);
		LinearLayout llPhoneParent = (LinearLayout) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_phone_parent);
		final TextView tvPhone = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.sendclothesback_order_item_phone);
		TextView tvDistance = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.sendclothesback_order_item_distance);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.sendclothesback_order_item_name);
		TextView tvDate = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.sendclothesback_order_item_date);
		Button btIsSend = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.sendclothesback_order_item_sending);
		Button btWashAgain = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.sendclothesback_order_item_wash_again);
		Button btGetCash = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.sendclothesback_order_item_getmoney);
		ImageView ivHead = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.sendclothesback_order_item_head);
		ImageView ivIsNewOrder = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.sendclothesback_order_item_is_neworder);
		tvLocation.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvLocation.getPaint().setAntiAlias(true);
		tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvPhone.getPaint().setAntiAlias(true);
		long appointTime = baseListData.get(position).getTakeDate();
		long v = appointTime * 1000L - System.currentTimeMillis();
		Date date = new Date(v);
		final TextView tvTime = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_time);
		LinearLayout tvMap = (LinearLayout) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_location_parent);
		LinearLayout llWaitPay = (LinearLayout) ViewHolderUtil.getItemView(
				convertView, R.id.sendclotheback_order_item_sendover);
		ImageView ivGetToday = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.sendclothesback_order_item_gettoday);
		switch(baseListData.get(position).getTodayGiveFlag()){
		case 1:
			/**
			 * 今日待取
			 */
			ivGetToday.setVisibility(View.VISIBLE);
			break;
		case 0:
			/**
			 * 非今日待取
			 */
			ivGetToday.setVisibility(View.GONE);
			break;
		}
		/**
		 * 绑定数据
		 */
		tvDate.setText(TimeUtil.changeTimeStempToDate(baseListData
				.get(position).getGiveDate())
				+ " "
				+ baseListData.get(position).getGiveTime());
		tvLocation.setText(baseListData.get(position).getAddress());
		tvTime.setText(TimeUtil.getCountTime(gapTimeList.get(position)));
		tvPhone.setText(baseListData.get(position).getOrderMobile());
		tvName.setText(baseListData.get(position).getConsignee());
		if (gapTimeList.get(position) > 0) {
			/**
			 * 未结束
			 */
			tvTime.setVisibility(View.VISIBLE);
			tvTime.setText(TimeUtil.getCountTime(gapTimeList.get(position) * 1000));
			LogUtil.d(TAG, "未结束时间差" + gapTimeList.get(position));
		} else {
			tvTime.setText("00:00:00");
			LogUtil.d(TAG, "已经结束");
		}
		/**
		 * 是否是新订单的icon是否显示
		 */
		if (isNewOrderList.get(position)) {
			ivIsNewOrder.setVisibility(View.VISIBLE);
		} else {
			ivIsNewOrder.setVisibility(View.GONE);
		}
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
		LatLng start = new LatLng(latitudeD, longitudeD);
		LatLng end = new LatLng(MsApplication.latitude, MsApplication.longitude);
		LogUtil.d(TAG, "latitudeD=" + latitudeD + "--" + MsApplication.latitude
				+ "longitudeD=" + longitudeD + "---" + MsApplication.longitude);
		Double distancen = DistanceUtil.getDistance(start, end);
		DecimalFormat df = new DecimalFormat("0.#");
		String distanceb = df.format(distancen / 1000);
		tvDistance.setText(distanceb + "公里");
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
		// if (position == 0 || position == 1) {
		// tvTime.setBackgroundResource(R.color.colorff0000);
		// } else {
		// tvTime.setBackgroundResource(R.color.color999999);
		// }
		if (gapTimeList.get(position) < 60 * 60 * 6
				&& gapTimeList.get(position) > 0) {
			tvTime.setBackgroundResource(R.color.colorff0000);
		} else {
			tvTime.setBackgroundResource(R.color.color999999);
		}

		int orderState = baseListData.get(position).getWashStatus();
		int payStatus = baseListData.get(position).getPayStatus();
		switch (orderState) {
		case 7:
			/**
			 * 我在派送
			 */
			btIsSend.setVisibility(View.VISIBLE);
			llWaitPay.setVisibility(View.GONE);
			btIsSend.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					final Dialog dialog = new AlertDialog.Builder(basecontext)
							.create();
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
							changeOrderState(position);
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
			break;

		case 8:
			if (payStatus == 0) {
				/**
				 * 未支付状态
				 */
				btIsSend.setVisibility(View.GONE);
				llWaitPay.setVisibility(View.VISIBLE);
				/**
				 * 返厂重洗
				 */
				btWashAgain.setOnClickListener(new OnClickListener() {

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
								/**
								 * 进入返厂重新页面
								 */
								Intent intent = new Intent(basecontext,
										UniversalOrderDetail.class);
								intent.putExtra(MsConfiguration.ORDER_TYPE,
										MsConfiguration.WASH_AGAIN);
								intent.putExtra(MsConfiguration.ORDER_ID_KEY,
										baseListData.get(position).getOrderId());
								intent.putExtra("washStatus",
										baseListData.get(position)
												.getWashStatus());
								intent.putExtra(MsConfiguration.PAGE_TYPE_KEY,
										MsConfiguration.PAGE_TYPE_WASH_AGAIN);
								MsStartActivity.startActivity(
										(Activity) basecontext, intent);
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
				/**
				 * 收取现金
				 */
				btGetCash.setOnClickListener(new OnClickListener() {

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
								/**
								 * 收取现金操作
								 */
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
			break;
		}
		return convertView;
	}

	/**
	 * 
	 * 
	 * @describe:更改订单状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-25下午8:06:12
	 * 
	 */
	public void changeOrderState(int position) {
		changePosition=position;
		ViewUtil.showRoundProcessDialog(basecontext);
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", baseListData.get(position).getOrderId());
		map.put("changeStatusType", MsConfiguration.FOUR);
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
						handlerChangeOrderState.sendMessage(message);
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
							handlerChangeOrderState.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerChangeOrderState.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return modifyStateBean;
					}
				});
	}

	/**
	 * 处理修改订单消息返回
	 */
	Handler handlerChangeOrderState = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (modifyStateBean != null && modifyStateBean.getCode() == 0) {
					LogUtil.d(TAG, "修改订单状态成功");
//					mlistener.changeListener();
					baseListData.get(changePosition).setWashStatus(8);
					orderType.set(changePosition, MsConfiguration.NOT_PAY);
					notifyDataSetChanged();

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

	/**
	 * 
	 * 
	 * @describe:收取现金
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-25下午8:07:51
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

	public interface OrderChangeListener {
		public void changeListener();

	}

	public void setListener(OrderChangeListener listener) {
		mlistener = listener;
	}
}
