package com.minsheng.app.module.appointmentorder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
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
import com.minsheng.app.module.orderdetail.AppointmentOrderDetail;
import com.minsheng.app.module.ordermap.MsMapView;
import com.minsheng.app.util.HtmlUtil;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.TimeUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 
 * @describe:预约订单列表数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-16下午8:57:08
 * 
 */
public class AppointmentOrderAdapter extends BaseListAdapter<OrderObject> {
	private static final String TAG = "AppointmentOrderAdapter";
	private boolean isPlay;
	protected List<String> itemType;
	private ModifyState modifyStateBean;
	private Handler handler = new Handler();
	private int changePosition;
	// private Double latitudeD = null;
	// private Double longitudeD = null;
	// 存储倒计时差，秒
	private List<Long> gapTimeList = new ArrayList<Long>();
	private List<Boolean> isNewOrderList;
	private int i = 0;
	private long days; // 天
	private long diffTime; // 倒计时时间差
	private long diffStartTime; // 活动刚开始倒计时到活动结束时间差(用于开始前倒计时到开始倒计时的时间设置)
	private long hours; // 小时
	private long minutes; // 分
	private long seconds; // 秒
	private CourierStateChangeListener mlistener;
	/**
	 * 定时任务刷新UI
	 */
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

	public AppointmentOrderAdapter(Context context, List<OrderObject> list) {
		super(list, context);
		setOrderItemType(list);

	}

	@Override
	public void setNewData(List<OrderObject> newData) {
		// TODO Auto-generated method stub
		super.setNewData(newData);
		setOrderItemType(newData);
	}

	/**
	 * 
	 * 
	 * @describe:绑定订单类型
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-21下午7:16:30
	 * 
	 */
	private void setOrderItemType(List<OrderObject> list) {
		/**
		 * 存储item类型:完善订单，在路上，已经取消
		 */
		itemType = new ArrayList<String>();
		if (list != null) {
			int lenght = list.size();
			for (int i = 0; i < lenght; i++) {
				final int washState = baseListData.get(i).getWashStatus();

				switch (washState) {
				case 1:
					/**
					 * 我在路上
					 */
					itemType.add(i, MsConfiguration.ON_THE_WAY);
					break;
				case 2:
					/**
					 * 完善订单
					 */
					itemType.add(i, MsConfiguration.COMPLETE_ORDER);
					break;
				case 10:
					/**
					 * 已经取消
					 */
					itemType.add(i, MsConfiguration.ORDER_CANCEL);
					break;

				case 3:
					/**
					 * 完善订单待用户确认
					 */
					itemType.add(i, MsConfiguration.FINISH_ORDER_WAIT_CONFIRM);
					break;
				default:
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
				if (!StringUtil.isEmpty(list.get(i).getTakeTime())) {
					int index = list.get(i).getTakeTime().indexOf("-");
					subTime = list.get(i).getTakeTime().substring(0, index);
				} else {
					subTime = "00:00";
				}

				String appointDate = TimeUtil.changeTimeStempToDate(list.get(i)
						.getTakeDate()) + " " + subTime;
				/**
				 * 日期转换成时间戳
				 */
				LogUtil.d(TAG, "预约时间" + appointDate);
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

				if (!StringUtil.isEmpty(list.get(i).getTakeTime())) {
					int index = list.get(i).getTakeTime().indexOf("-");
					subTime = list.get(i).getTakeTime().substring(0, index);
				} else {
					subTime = "00:00";
				}

				String appointDate = TimeUtil.changeTimeStempToDate(list.get(i)
						.getTakeDate()) + " " + subTime;
				/**
				 * 日期转换成时间戳
				 */
				LogUtil.d(TAG, "预约时间" + appointDate);
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
		 * 存储记录是否是新订单的标记
		 */
		isNewOrderList = new ArrayList<Boolean>();
		if (list != null && list.size() > 0) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				int isCheck = list.get(i).getDelivery_check_status();
				switch (isCheck) {
				case 0:
					/**
					 * 未查看
					 */
					isNewOrderList.add(i, false);
					break;
				case 1:
					/**
					 * 查看
					 */
					isNewOrderList.add(i, true);
					break;
				}
			}
			LogUtil.d(TAG, "标记是否新订单" + isNewOrderList.toString());
		}
	}

	public String setRunningTimeText(Long diffTime) {
		String cutDownTime;
		if (null == diffTime) {
			return "00:00:00";
		}

		days = diffTime / (1000 * 60 * 60 * 24);
		hours = (diffTime - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		minutes = (diffTime - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60))
				/ (1000 * 60);
		seconds = (diffTime - days * (1000 * 60 * 60 * 24) - hours
				* (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);

		// LogUtil.d(LogUtil.TAG_TEST, "setRunningTimeText days = "
		// + days + " hours = " + hours + " minutes = " + minutes
		// + " seconds = " + seconds);

		cutDownTime = "剩余" + HtmlUtil.TEXT_RED_HTML_FONT_LEFT + days
				+ HtmlUtil.TEXT_HTML_FONT_RIGHT + "天"
				+ HtmlUtil.TEXT_RED_HTML_FONT_LEFT + hours
				+ HtmlUtil.TEXT_HTML_FONT_RIGHT + "小时"
				+ HtmlUtil.TEXT_RED_HTML_FONT_LEFT + minutes
				+ HtmlUtil.TEXT_HTML_FONT_RIGHT + "分"
				+ HtmlUtil.TEXT_RED_HTML_FONT_LEFT + seconds
				+ HtmlUtil.TEXT_HTML_FONT_RIGHT + "秒";

		return Html.fromHtml(cutDownTime).toString();
	}

	private String getTime(long millisUntilFinished) {
		// 获取小时值
		long hours = millisUntilFinished / (60 * 60 * 1000);
		// 获取分值
		long minutes = (millisUntilFinished - hours * (60 * 60 * 1000))
				/ (60 * 1000);
		// 获取秒值
		long seconds = (millisUntilFinished - hours * (60 * 60 * 1000) - minutes
				* (60 * 1000)) / 1000;
		StringBuilder sb = new StringBuilder();
		if (hours < 10) {
			// tvHour.setText("0" + hours);
			sb.append("0" + hours + ":");
		} else
			// tvHour.setText(hours + "");
			sb.append(hours + ":");
		if (minutes < 10) {
			// tvMinute.setText("0" + minutes);
			sb.append("0" + minutes + ":");
		} else
			// tvMinute.setText(minutes + "");
			sb.append(minutes + ":");
		if (seconds < 10) {
			// tvSeconds.setText("0" + seconds);
			sb.append("0" + seconds + "");
		} else
			// tvSeconds.setText(seconds + "");
			sb.append(seconds + "");
		return sb.toString();
	}

	/**
	 * 
	 * 
	 * @describe:启动定时任务
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-20下午5:45:36
	 * 
	 */
	public void start() {
		isPlay = true;
		runnable.run();
	}

	/**
	 * 
	 * 
	 * @describe:关闭定时任务
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-20下午5:45:47
	 * 
	 */
	public void stop() {
		isPlay = false;
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.appointment_order_list_item, parent, false);
		}
		TextView tvLocation = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_location_tv);
		final LinearLayout llPhoneParent = (LinearLayout) ViewHolderUtil
				.getItemView(convertView,
						R.id.appointment_order_list_item_phone_parent);
		final TextView tvPhone = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_phone_tv);
		TextView tvDate = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.appointment_order_list_item_date);
		TextView tvDistance = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_distance);
		TextView tvCancel = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.appointment_order_list_item_cancel);
		final TextView tvTime = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_time);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.appointment_order_list_item_name);
		ImageView ivHead = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.appointment_order_list_item_head);
		ImageView ivIsNewOrder = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_is_new);
		ImageView ivGetToday = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_gettoday);
		Button bt = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.appointment_order_list_item_bt);
		tvLocation.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvLocation.getPaint().setAntiAlias(true);
		tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvPhone.getPaint().setAntiAlias(true);
		switch(baseListData.get(position).getTodayTakeFlag()){
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
		 * 处理倒计时
		 */

		if (gapTimeList.get(position) > 0) {
			/**
			 * 未结束
			 */
			tvTime.setVisibility(View.VISIBLE);
			tvTime.setText(getTime(gapTimeList.get(position) * 1000));
			LogUtil.d(TAG, "未结束时间差" + gapTimeList.get(position));
		} else {
			tvTime.setText("00:00:00");
			LogUtil.d(TAG, "已经结束");
		}
		final LinearLayout tvMap = (LinearLayout) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_location_parent);
		/**
		 * 绑定数据
		 */
		tvLocation.setText(baseListData.get(position).getAddress());
		tvPhone.setText(baseListData.get(position).getOrderMobile());
		tvDate.setText(TimeUtil.changeTimeStempToDate(baseListData
				.get(position).getTakeDate())
				+ " "
				+ baseListData.get(position).getTakeTime());
		tvName.setText(baseListData.get(position).getConsignee());
		/**
		 * 是否是新订单的icon是否显示
		 */
		if (!isNewOrderList.get(position)) {
			ivIsNewOrder.setVisibility(View.VISIBLE);
		} else {
			ivIsNewOrder.setVisibility(View.GONE);
		}
		/**
		 * 倒计时6个小时内的倒计时标示红色
		 */
		if (gapTimeList.get(position) < 60 * 60 * 6
				&& gapTimeList.get(position) > 0) {
			tvTime.setBackgroundResource(R.color.colorff0000);
		} else {
			tvTime.setBackgroundResource(R.color.color999999);
		}

		/**
		 * public static double getDistance(LatLng p1LL, LatLng p2LL) 返回两个点之间的距离
		 * 参数: p1LL - 起点的百度经纬度坐标 p2LL - 终点的百度经纬度坐标 返回: 两点距离，单位为： 米,转换错误时返回-1.
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
		LatLng start = new LatLng(latitudeD, longitudeD);
		LatLng end = new LatLng(MsApplication.latitude, MsApplication.longitude);
		LogUtil.d(TAG, "latitudeD=" + latitudeD + "--" + MsApplication.latitude
				+ "longitudeD=" + longitudeD + "---" + MsApplication.longitude);
		Double distancen = DistanceUtil.getDistance(start, end);
		DecimalFormat df = new DecimalFormat("0.#");
		String distanceb = df.format(distancen / 1000);
		tvDistance.setText(distanceb + "公里");
		/**
		 * 获取用户图片
		 */
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
		 * 处理按钮显示状态
		 */
		final int washState = baseListData.get(position).getWashStatus();
		LogUtil.d(TAG, "订单状态" + washState);
		switch (washState) {
		case 1:
			/**
			 * 我在路上
			 */
			bt.setEnabled(true);
			tvTime.setVisibility(View.VISIBLE);
			bt.setVisibility(View.VISIBLE);
			bt.setBackgroundResource(R.drawable.order_list_button_bg_green);
			bt.setText("我在路上");
			convertView.setBackgroundResource(R.color.white);
			tvCancel.setText("");
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					/**
					 * 我在路上
					 */
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
		case 2:
			/**
			 * 完善订单
			 */
			bt.setEnabled(true);
			tvTime.setVisibility(View.VISIBLE);
			bt.setVisibility(View.VISIBLE);
			bt.setBackgroundResource(R.drawable.order_list_button_bg_yellow);
			bt.setText("完善订单");
			convertView.setBackgroundResource(R.color.white);
			tvCancel.setText("");
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/**
					 * 完善订单
					 */

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
							// changeOrderState(position);
							Intent intent = new Intent((Activity) basecontext,
									CompleteOrder.class);
							intent.putExtra(MsConfiguration.BUSINESS_ID_KEY,
									baseListData.get(position).getBusinessId());
							intent.putExtra(MsConfiguration.ORDER_ID_KEY,
									baseListData.get(position).getOrderId());
							intent.putExtra(MsConfiguration.ORDER_STATE_KEY,
									baseListData.get(position).getWashStatus());
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
					// Intent intent = new Intent((Activity) basecontext,
					// CompleteOrder.class);
					// intent.putExtra(MsConfiguration.BUSINESS_ID_KEY,
					// baseListData.get(position).getBusinessId());
					// intent.putExtra(MsConfiguration.ORDER_ID_KEY,
					// baseListData
					// .get(position).getOrderId());
					// intent.putExtra(MsConfiguration.ORDER_STATE_KEY,
					// baseListData.get(position).getWashStatus());
					// MsStartActivity.startActivity((Activity) basecontext,
					// intent);

				}
			});
			break;
		case 10:
			/**
			 * 已经取消
			 */
			tvTime.setVisibility(View.GONE);
			bt.setVisibility(View.GONE);
			convertView.setBackgroundResource(R.color.colorf2f2f2);
			tvCancel.setText("订单已取消");
			break;

		case 3:
			/**
			 * 完善订单，待用户确认
			 */
			bt.setEnabled(false);
			tvTime.setVisibility(View.VISIBLE);
			bt.setVisibility(View.VISIBLE);
			bt.setBackgroundResource(R.drawable.bt_gray_bg);
			bt.setText("待用户确认");
			convertView.setBackgroundResource(R.color.white);
			tvCancel.setText("");

			break;
		default:
			break;

		}
		if (washState != 10) {
			/**
			 * 非取消订单，进行事件监听
			 */
			tvMap.setEnabled(true);
			llPhoneParent.setEnabled(true);
			tvMap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent((Activity) basecontext,
							MsMapView.class);
					String latitudeS = baseListData.get(position)
							.getWdLatitude();
					Double latitudeD = 0.0;
					Double longitudeD = 0.0;
					if (!StringUtil.isEmpty(latitudeS)) {
						latitudeD = Double.parseDouble(latitudeS);
					}
					String longitudeS = baseListData.get(position)
							.getWdLongitude();

					if (!StringUtil.isEmpty(longitudeS)) {
						longitudeD = Double.parseDouble(longitudeS);
					}
					intent.putExtra("latitudeD", latitudeD);
					intent.putExtra("longitudeD", longitudeD);
					intent.putExtra("address", baseListData.get(position)
							.getAddress());
					MsStartActivity.startActivity((Activity) basecontext,
							intent);
				}
			});
			llPhoneParent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
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
									+ baseListData.get(position)
											.getOrderMobile());
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
									+ baseListData.get(position)
											.getOrderMobile()));
							basecontext.startActivity(intent);
						}
					});
				}
			});

			convertView.setEnabled(true);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/**
					 * 进入详情页面==============================
					 */

					Intent intent = new Intent((Activity) basecontext,
							AppointmentOrderDetail.class);
					intent.putExtra(MsConfiguration.ORDER_TYPE,
							itemType.get(position));
					intent.putExtra(MsConfiguration.BUSINESS_ID_KEY,
							baseListData.get(position).getBusinessId());
					intent.putExtra(MsConfiguration.ORDER_ID_KEY, baseListData
							.get(position).getOrderId());
					intent.putExtra(MsConfiguration.ORDER_STATE_KEY,
							baseListData.get(position).getWashStatus());
					intent.putExtra(MsConfiguration.MOBILE_PHONE_KEY,
							baseListData.get(position).getOrderMobile());
					((Activity) basecontext).startActivityForResult(intent,
							MsConfiguration.APPOINT_ORDER_TO_DETAIL);

				}
			});

		} else {
			/**
			 * 取消订单，item不可以进入详情页面
			 */
			convertView.setEnabled(false);
			tvMap.setEnabled(false);
			llPhoneParent.setEnabled(false);
		}

		return convertView;
	}

	/**
	 * 
	 * 
	 * @describe:更新订单状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-19下午1:12:47
	 * 
	 */
	public void changeOrderState(int position) {
		changePosition = position;
		ViewUtil.showRoundProcessDialog(basecontext);
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", baseListData.get(position).getOrderId());
		map.put("changeStatusType", MsConfiguration.ZERO);
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
					baseListData.get(changePosition).setWashStatus(2);
					itemType.set(changePosition, MsConfiguration.COMPLETE_ORDER);
					notifyDataSetChanged();
					mlistener.changeListener();
					LogUtil.d(TAG,
							"刷新状态===========================================");
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
	 * @describe:根据两点经纬度获取距离
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-21下午3:39:12
	 * 
	 */
	public double getDistance(LatLng start, LatLng end) {
		double lat1 = (Math.PI / 180) * start.latitude;
		double lat2 = (Math.PI / 180) * end.latitude;
		double lon1 = (Math.PI / 180) * start.longitude;
		double lon2 = (Math.PI / 180) * end.longitude;
		// 地球半径
		double R = 6371;
		// 两点间距离 km，如果想要米的话，结果*1000就可以了
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.cos(lon2 - lon1))
				* R;
		return d * 1000;
	}

	public interface CourierStateChangeListener {

		public void changeListener();

	}

	public void seChangeListener(CourierStateChangeListener listener) {

		mlistener = listener;
	}

}
