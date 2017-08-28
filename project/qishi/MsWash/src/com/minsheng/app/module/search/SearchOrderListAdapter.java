package com.minsheng.app.module.search;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.entity.SearchOrderListEntity.Infor.SearchOrderDetail;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:搜索订单数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-16上午11:33:19
 * 
 */
public class SearchOrderListAdapter extends BaseListAdapter<SearchOrderDetail> {
	protected List<String> orderType;

	public SearchOrderListAdapter(List<SearchOrderDetail> data, Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
		setDataType(data);
	}

	@Override
	public void setNewData(List<SearchOrderDetail> newData) {
		// TODO Auto-generated method stub
		super.setNewData(newData);
		setDataType(newData);
	}

	private void setDataType(List<SearchOrderDetail> list) {
		// TODO Auto-generated method stub
		if (list != null) {
			orderType = new ArrayList<String>();
			int lenght = list.size();
			for (int i = 0; i < lenght; i++) {
				int orderState = list.get(i).getWashStatus();
				int payStatus = list.get(i).getPayStatus();
				int isShowAfresh = list.get(i).getIsShowAfresh();
				switch (orderState) {
				case 1:
					/**
					 * 我在路上
					 */
					orderType.add(i, MsConfiguration.ON_THE_WAY);
					break;
				case 2:
					/**
					 * 完善订单
					 */
					orderType.add(i, MsConfiguration.COMPLETE_ORDER);
					break;

				case 3:
					/**
					 * 完善订单待用户确认
					 */
					orderType.add(i, MsConfiguration.FINISH_ORDER_WAIT_CONFIRM);
					break;

				case 4:
					/**
					 * 送店状态=====我已送店
					 */
					orderType.add(i, MsConfiguration.SEND_SHOP_ORDER);
					break;

				case 5:
					/**
					 * 取衣订单====我已取走
					 */
					orderType.add(i, MsConfiguration.GET_CLOTHES_BACK);
					break;

				case 7:
					/**
					 * 我在派送
					 */
					orderType.add(i,MsConfiguration.IS_SENDDING);
					break;

				case 8:
					/**
					 * 送货中
					 */

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
				case 10:
					/**
					 * 已经取消
					 */
					orderType.add(i, MsConfiguration.CANCEL_ORDER);
					break;
				case 11:
					/**
					 * 已经完成
					 */
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
				default:
					break;
				}
			}
		}
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(R.layout.search_order_list_item,
					parent, false);
		}
		TextView tvOrderSn = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.search_order_list_item_order);
		TextView tvPhone = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.search_order_list_item_phone);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.search_order_list_item_name);
		ImageView ivHead = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.search_order_list_item_head);
		tvOrderSn.setText(baseListData.get(position).getOrderSn());
		tvPhone.setText(baseListData.get(position).getOrderMobile());
		tvName.setText(baseListData.get(position).getConsignee());
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
		return convertView;
	}

}
