package com.minsheng.app.module.usercenter;

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
import com.minsheng.app.entity.OrderListEntity.Infor.OrderObject;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:我的订单数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-13下午5:22:37
 * 
 */
public class MyOrderListAdapter extends BaseListAdapter<OrderObject> {

	public MyOrderListAdapter(List<OrderObject> data, Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.usercenter_myorderlist_item, parent, false);
		}
		ImageView ivHead = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.usercenter_myorderlist_item_head);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.usercenter_myorderlist_item_name);
		TextView tvOrderSn = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.usercenter_myorderlist_item_ordersn);
		TextView tvPhone = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.usercenter_myorderlist_item_phone);
		TextView tvPrice = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.usercenter_myorderlist_item_price);
		/**
		 * 绑定视图数据
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
		tvName.setText(baseListData.get(position).getConsignee());
		tvOrderSn.setText(baseListData.get(position).getOrderSn());
		tvPhone.setText(baseListData.get(position).getOrderMobile());
		tvPrice.setText(baseListData.get(position).getOrderAmountD() + "元");

		return convertView;
	}

}
