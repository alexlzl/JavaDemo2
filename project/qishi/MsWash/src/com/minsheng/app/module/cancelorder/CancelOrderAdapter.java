package com.minsheng.app.module.cancelorder;

import java.text.DecimalFormat;
import java.util.List;
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
 * @describe:取消订单列表数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-16下午8:57:08
 * 
 */
public class CancelOrderAdapter extends BaseListAdapter<OrderObject> {
	private static final String TAG = "CancelOrderAdapter";

	public CancelOrderAdapter(Context context, List<OrderObject> list) {
		super(list, context);

	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(R.layout.cancel_order_list_item,
					parent, false);
		}
		TextView tvLocation = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.cancel_order_list_item_location_tv);
		final LinearLayout llPhoneParent = (LinearLayout) ViewHolderUtil
				.getItemView(convertView,
						R.id.cancel_order_list_item_phone_parent);
		final TextView tvPhone = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.cancel_order_list_item_phone_tv);
		TextView tvDate = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.cancel_order_list_item_date);
		TextView tvDistance = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.cancel_order_list_item_distance);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.cancel_order_list_item_name);
		ImageView ivHead = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.cancel_order_list_item_head);

		tvLocation.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvLocation.getPaint().setAntiAlias(true);
		tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvPhone.getPaint().setAntiAlias(true);

		final LinearLayout tvMap = (LinearLayout) ViewHolderUtil.getItemView(
				convertView, R.id.cancel_order_list_item_location_parent);
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
				tvTitle.setText("我要给"
						+ baseListData.get(position).getConsignee() + "("
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

		convertView.setEnabled(true);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/**
				 * 进入详情页面==============================
				 */

				Intent intent = new Intent((Activity) basecontext,
						UniversalOrderDetail.class);
				intent.putExtra(MsConfiguration.ORDER_TYPE,
						MsConfiguration.CANCEL_ORDER);
				intent.putExtra(MsConfiguration.ORDER_ID_KEY,
						baseListData.get(position).getOrderId());
				intent.putExtra("washStatus", baseListData.get(position)
						.getWashStatus());
				intent.putExtra(MsConfiguration.MOBILE_PHONE_KEY, baseListData
						.get(position).getOrderMobile());
				MsStartActivity.startActivity((Activity) basecontext, intent);

			}
		});

		return convertView;
	}

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

}
