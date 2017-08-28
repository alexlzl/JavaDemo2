package com.minsheng.app.module.finishorder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
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
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.entity.OrderListEntity.Infor.OrderObject;
import com.minsheng.app.module.orderdetail.UniversalOrderDetail;
import com.minsheng.app.module.ordermap.MsMapView;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.TimeUtil;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:当月已完成订单数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-14下午5:17:48
 * 
 */
public class FinishOrderAdapter extends BaseListAdapter<OrderObject> {
	public List<String> orderType;
	private static final String TAG = "FinishOrderAdapter";
	// private Double latitudeD = null;
	// private Double longitudeD = null;
	private List<Boolean> isNewOrderList;

	public FinishOrderAdapter(Context context, List<OrderObject> list) {
		super(list, context);
		setTypeData(list);

	}

	@Override
	public void setNewData(List<OrderObject> newData) {
		// TODO Auto-generated method stub
		super.setNewData(newData);
		setTypeData(newData);
	}

	/**
	 * 
	 * 
	 * @describe:设置ITEM类型
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-30下午1:54:06
	 * 
	 */
	private void setTypeData(List<OrderObject> list) {
		if (list != null) {
			orderType = new ArrayList<String>();
			int lenght = list.size();
			for (int i = 0; i < lenght; i++) {

				int orderState = list.get(i).getWashStatus();
				int isShowAfresh = list.get(i).getIsShowAfresh();
				LogUtil.d(TAG, "位置" + i + "状态" + isShowAfresh);
				switch (orderState) {

				case 11:

					if (isShowAfresh == 1) {
						/**
						 * 可以返厂重洗
						 */
						orderType.add(i,
								MsConfiguration.ORDER_OVER_CAN_WASH_AGAIN);
					}
					if (isShowAfresh == 0) {
						/**
						 * 不可以返厂重洗
						 */
						orderType.add(i,
								MsConfiguration.ORDER_OVER_CANNOT_WASH_AGAIN);
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
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(R.layout.finish_order_item,
					parent, false);
		}
		TextView tvLocation = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.finish_order_item_location);
		LinearLayout llPhoneParent = (LinearLayout) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_phone_parent);
		final TextView tvPhone = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.finish_order_item_phonenum);
		TextView tvDate = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.finish_order_item_date);
		TextView tvPayType = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.finish_order_item_distance);
		ImageView ivHead = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.finish_order_item_head);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.finish_order_item_name);
		Button bt = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.finish_order_item_bt);
		ImageView ivIsNewOrder = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.finish_order_item_is_neworder);
		tvLocation.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvLocation.getPaint().setAntiAlias(true);
		tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvPhone.getPaint().setAntiAlias(true);
		LinearLayout tvMap = (LinearLayout) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_location_parent);
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
				String name = baseListData.get(position).getConsignee();
				name = trimInnerSpaceStr(name);
				tvTitle.setText("我要给" + name + "("
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
		// tvDistance.setText(distanceb + "公里");
		switch (baseListData.get(position).getPayType()) {
		case 0:
			/**
			 * 现金支付
			 */
			tvPayType.setText("现金支付");
			break;
		case 1:
			/**
			 * 线上支付
			 */
			tvPayType.setText("线上支付");
			break;
		}

		/**
		 * 是否是新订单的icon是否显示
		 */
		if (!isNewOrderList.get(position)) {
			ivIsNewOrder.setVisibility(View.VISIBLE);
		} else {
			ivIsNewOrder.setVisibility(View.GONE);
		}
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
		int orderState = baseListData.get(position).getWashStatus();
		int isShowAfresh = baseListData.get(position).getIsShowAfresh();
		switch (orderState) {

		case 11:
			/**
			 * 已完成状态
			 */
			if (isShowAfresh == 1) {
				/**
				 * 可以返厂重洗
				 */
				bt.setVisibility(View.VISIBLE);
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
								/**
								 * 进入返厂重新页面
								 */
								Intent intent = new Intent(basecontext,
										UniversalOrderDetail.class);
								intent.putExtra(MsConfiguration.ORDER_TYPE,
										MsConfiguration.WASH_AGAIN);
								intent.putExtra(MsConfiguration.ORDER_ID_KEY,
										baseListData.get(position).getOrderId());
								intent.putExtra(MsConfiguration.PAGE_TYPE_KEY,
										MsConfiguration.PAGE_TYPE_WASH_AGAIN);
								intent.putExtra("washStatus",
										baseListData.get(position)
												.getWashStatus());
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
			}

			if (isShowAfresh == 0) {
				/**
				 * 不可以返厂重洗
				 */
				bt.setVisibility(View.GONE);
			}

			break;
		}
		return convertView;
	}

	public String trimInnerSpaceStr(String str) {
		str = str.trim();
		while (str.startsWith(" ")) {
			str = str.substring(1, str.length()).trim();
		}
		while (str.endsWith(" ")) {
			str = str.substring(0, str.length() - 1).trim();
		}

		return str;
	}

}
