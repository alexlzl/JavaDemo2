package com.minsheng.app.module.getclothesorder;

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
 * @describe:取衣订单数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-14下午4:21:24
 * 
 */
public class GetClothesOrderAdaper extends BaseListAdapter<OrderObject> {
	private static final String TAG = "GetClothesOrderAdaper";
	private ModifyState modifyStateBean;
	private List<Boolean> isNewOrderList;

	public GetClothesOrderAdaper(Context context, List<OrderObject> list) {
		super(list, context);
		setData(list);
	}

	@Override
	public void setNewData(List<OrderObject> newData) {
		// TODO Auto-generated method stub
		super.setNewData(newData);
		setData(newData);
	}

	private void setData(List<OrderObject> list) {
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
		}

	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(R.layout.getclothes_order_item,
					parent, false);
		}

		TextView tvOrderSn = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.getclothes_order_item_sn);
		TextView tvDate = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.getclothes_order_item_date);
		TextView tvAmount = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.getclothes_order_item_amount);
		TextView tvPrice = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.getclothes_order_item_price);
		Button btCheck = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.getclothes_order_item_bt);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.getclothes_order_item_name);
		final TextView tvTime = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.appointment_order_list_item_time);
		ImageView ivHead = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.getclothes_order_item_head);
		tvTime.setText(TimeUtil.changeTimeStempToDate(baseListData
				.get(position).getTakeDate()));
		ImageView ivIsNewOrder = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.getclothes_order_item_isnew_order);
		ImageView ivGetToday = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.getclothes_order_item_gettoday);
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
		 * 我已经取走事件
		 */
		btCheck.setOnClickListener(new OnClickListener() {

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
						 * 更改状态
						 */
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
		/**
		 * 绑定数据
		 */
		tvOrderSn.setText(baseListData.get(position).getOrderSn());
		tvDate.setText(TimeUtil.changeTimeStempToDate(baseListData
				.get(position).getTakeDate())
				+ " "
				+ baseListData.get(position).getTakeTime());
		if (!StringUtil.isEmpty(baseListData.get(position).getProductNum())) {
			tvAmount.setText("衣服数量:"
					+ baseListData.get(position).getProductNum() + "件");
		} else {
			tvAmount.setText("衣服数量:" + 0 + "件");
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
		tvName.setText(baseListData.get(position).getConsignee());
		return convertView;
	}

	/**
	 * 
	 * 
	 * @describe:修改取衣订单状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-25下午5:33:41
	 * 
	 */
	public void changeOrderState(int position) {
		ViewUtil.showRoundProcessDialog(basecontext);
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", baseListData.get(position).getOrderId());
		map.put("changeStatusType", MsConfiguration.THREE);
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
