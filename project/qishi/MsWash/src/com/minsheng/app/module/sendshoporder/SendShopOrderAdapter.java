package com.minsheng.app.module.sendshoporder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.entity.OrderListEntity.Infor.OrderObject;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.TimeUtil;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:送店订单数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-14下午1:49:13
 * 
 */
public class SendShopOrderAdapter extends BaseListAdapter<OrderObject> {
	private static final String TAG = "SendShopOrderAdapter";
	private boolean isPlay;
	// 存储倒计时差，秒
	private List<Long> gapTimeList = new ArrayList<Long>();
	private List<Boolean> isNewOrder;
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

	public SendShopOrderAdapter(Context context, List<OrderObject> list) {
		super(list, context);
		setOrderItemType(list);
	}

	@Override
	public void setNewData(List<OrderObject> newData) {
		// TODO Auto-generated method stub
		super.setNewData(newData);
		setOrderItemType(newData);
	}

	private void setOrderItemType(List<OrderObject> list) {

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
				} else {
					subTime = "00:00";
				}

				String appointDate = TimeUtil.changeTimeStempToDate(list.get(i)
						.getGiveDate()) + " " + subTime;
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
		isNewOrder = new ArrayList<Boolean>();
		if (list != null && list.size() > 0) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				int isCheck = list.get(i).getDelivery_check_status();
				switch (isCheck) {
				case 0:
					/**
					 * 未查看
					 */
					isNewOrder.add(i, false);
					break;
				case 1:
					/**
					 * 查看
					 */
					isNewOrder.add(i, true);
					break;
				}
			}

		}
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
			convertView = baseInflater.inflate(R.layout.send_shop_order_item,
					parent, false);
		}
		TextView tvAddress = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.send_shop_order_item_order_address);
		Button bt = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.appointment_order_list_item_bt);
		final TextView tvTime = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_time);
		TextView tvOrderSn = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.send_shop_order_item_order_sn);
		TextView tvDate = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.send_shop_order_item_date);
		TextView tvClothesNum = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.send_shop_order_item_clothes_num);
		TextView tvPrice = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.send_shop_order_item_price);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.send_shop_order_item_name);
		ImageView ivHead = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.send_shop_order_item_head);
		ImageView ivIsNewOrder = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.send_shop_order_item_isnew_order);
		/**
		 * 绑定数据
		 */
		tvDate.setText(TimeUtil.changeTimeStempToDate(baseListData
				.get(position).getGiveDate())
				+ " "
				+ baseListData.get(position).getGiveTime());
		tvOrderSn.setText(baseListData.get(position).getOrderSn());
		tvAddress.setText(baseListData.get(position).getBusinessName());
		// tvClothesNum.setText("衣服数量："
		// + baseListData.get(position).getProductNum() + "件");
		if (!StringUtil.isEmpty(baseListData.get(position).getProductNum())) {
			tvClothesNum.setText("衣服数量:"
					+ baseListData.get(position).getProductNum() + "件");
		} else {
			tvClothesNum.setText("衣服数量:" + 0 + "件");
		}
		tvPrice.setText(baseListData.get(position).getOrderAmountD() + "");
		tvName.setText(baseListData.get(position).getConsignee());
		/**
		 * 是否是新订单的icon是否显示
		 */
		if (!isNewOrder.get(position)) {
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
		long appointTime = baseListData.get(position).getTakeDate();
		long v = appointTime * 1000L - System.currentTimeMillis();
		Date date = new Date(v);
		// tvTime.setText(simpleDateFormat.format(date));
		if (gapTimeList.get(position) > 0) {
			/**
			 * 未结束
			 */
			tvTime.setVisibility(View.VISIBLE);
			tvTime.setText(TimeUtil.getCountTime(gapTimeList.get(position) * 1000));
			LogUtil.d(TAG, "未结束时间差" + gapTimeList.get(position));
		} else {
			// tvTime.setVisibility(View.GONE);
			tvTime.setText("00:00:00");
			LogUtil.d(TAG, "已经结束");
		}
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
		/**
		 * 处理我已送店的事件
		 */
		bt.setOnClickListener(new OnClickListener() {

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
						/**
						 * 送店页面
						 */
						Intent intent = new Intent((Activity) basecontext,
								OrderInforMatch.class);
						intent.putExtra(MsConfiguration.ORDER_ID_KEY,
								baseListData.get(position).getOrderId());
						intent.putExtra("washStatus", baseListData
								.get(position).getWashStatus());
						MsStartActivity.startActivity((Activity) basecontext,
								intent);
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
		return convertView;
	}

}
