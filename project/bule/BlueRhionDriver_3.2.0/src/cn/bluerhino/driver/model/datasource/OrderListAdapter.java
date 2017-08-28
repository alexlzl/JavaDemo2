package cn.bluerhino.driver.model.datasource;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.helper.OrderInfoHelper;
import cn.bluerhino.driver.model.DeliverInfo;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.utils.ReDrawGridView;
import cn.bluerhino.driver.utils.TimeUtil;
import cn.bluerhino.driver.utils.ViewHolder;
import cn.bluerhino.driver.view.PlaceofReceiptLinearLayout;

public abstract class OrderListAdapter extends BaseAdapter {

	protected Context mContext;
	protected Resources mRes;
	protected LayoutInflater mLayoutInflater;
	public List<OrderInfo> mOrderInfoList;
	/**
	 * 底部标签list
	 */
	protected List<String> mTagList = new ArrayList<String>();

	/**
	 * 标签array
	 */
	protected String[] mTagStrArr;
	/**
	 * 预计公里数
	 */
	protected String mExpectMileage;

	public OrderListAdapter(Context context, List<OrderInfo> list) {
		mContext = context;
		mRes = context.getResources();
		mLayoutInflater = LayoutInflater.from(context);
		mOrderInfoList = list;
		initResString();
	}

	private void initResString() {
		mTagStrArr = mRes.getStringArray(R.array.tag_str_array);
		mExpectMileage = mRes.getString(R.string.wait_order_expect_mileage);
	}

	@Override
	public int getCount() {
		return mOrderInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(mOrderInfoList!=null&&mOrderInfoList.size()>0){
			OrderInfo orderInfo = mOrderInfoList.get(position);
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(
						R.layout.adapter_orderlist_item, parent, false);
			}

			// 订单状态的图标
			ImageView orderinfo_item_charStatus = ViewHolder.get(convertView,
					R.id.orderinfo_item_charStatus);
			// 设置图标
			setIcon(orderInfo, orderinfo_item_charStatus);

			// 订单时间(判断到时是即时 还是预定)
			TextView curlist_item_sendtime = ViewHolder.get(convertView,
					R.id.orderinfo_item_ordertime);
			setOrderTimedesc(orderInfo, curlist_item_sendtime);

			// 订单号
			TextView mOrderNumber = ViewHolder.get(convertView,
					R.id.orderinfo_item_ordernumber);
			setOrderNum(mOrderNumber, String.valueOf(orderInfo.getOrderId()));

			// 订单金额 可能会有后缀(补贴)
			TextView curlist_item_orderbill = ViewHolder.get(convertView,
					R.id.orderinfo_item_orderbill);
			setOrderBill(curlist_item_orderbill, orderInfo.getOrderBill());
			
			if(this.isOrderItemSpecShow()){
				TextView infoSpec = ViewHolder.get(convertView,
						R.id.orderinfo_spec);
				infoSpec.setText(R.string.adapter_list_item_specifics);
			}

			// 发货地址
			TextView place_of_dispatch = ViewHolder.get(convertView,
					R.id.orderinfo_item_placeofdispatch);
			setOrderdisPatch(place_of_dispatch, orderInfo.getPickupAddress());

			// 收货地址
			LinearLayout place_of_receipt_group = ViewHolder.get(convertView,
					R.id.orderinfo_item_3_grounp);
			setOrderReceipt(place_of_receipt_group, orderInfo);

			// 公里数
			TextView orderinfo_item_kilometer = ViewHolder.get(convertView,
					R.id.orderinfo_item_kilometer);
			setKilometer(orderinfo_item_kilometer,
					String.format(mExpectMileage, orderInfo.getKilometer()));

			// 订单状态,用来显示右边的内容判断按钮能否可点, 默认为橙色, 按下为灰色, 不可用也为灰色 判断焦点
			Button orderState_button = ViewHolder.get(convertView,
					R.id.orderinfo_item_state_button);
			// 距离多少公里 或 显示被xx抢走了
			TextView orderinfo_item_killmeters_change = ViewHolder.get(convertView,
					R.id.orderinfo_item_killmeters_change);
			setOrderStatusAndReason(orderState_button,
					orderinfo_item_killmeters_change, orderInfo,position);

			// 设置底部的额外标签
			GridView remark_gridview = ViewHolder.get(convertView,
					R.id.remark_gridview);
			// 10.设置底部标签
			setRemarkTag(orderInfo, remark_gridview, convertView);

		}
		

		
		return convertView;
	}

	/**
	 * 设置图标描述
	 */
	protected void setIcon(OrderInfo orderInfo,
			ImageView orderinfo_item_charStatus) {
		// 设置是(如果servertype==200)马上服务 还是 预约的订单
		if (OrderInfoHelper.getOrderType(orderInfo)) {
			orderinfo_item_charStatus.setImageResource(R.drawable.char_yue);
		} else {
			orderinfo_item_charStatus.setImageResource(R.drawable.char_ma);
		}
	}

	// 马上服务 还是 预约的订单
	protected void setOrderTimedesc(OrderInfo orderInfo,
			TextView curlist_item_sendtime) {
		// 设置是(如果servertype==200)马上服务 还是 预约的订单
		if (orderInfo.getServeType() == 200) {
			curlist_item_sendtime.setText("即时服务");
		} else {
			curlist_item_sendtime.setText(getServerTime(orderInfo
					.getTransTime() * 1000));
		}
	}

	/**
	 * 时间的展示样式
	 * 
	 * @param mills
	 * @return
	 */
	protected CharSequence getServerTime(long mills) {
		return TimeUtil.format(mills);
	}

	/**
	 * 设置订单号
	 * 
	 * @param mOrderNumber
	 */
	private void setOrderNum(TextView mOrderNumber, String num) {
		mOrderNumber.setText(num);
	}

	private void setOrderBill(TextView curlist_item_orderbill, String orderBill) {
		curlist_item_orderbill.setText(orderBill);
	}
	
	/**
	 * 是否显示'详情'二字
	 */	
	protected boolean isOrderItemSpecShow(){
		return false;
	}

	private void setOrderdisPatch(TextView place_of_dispatch,
			String pickupAddress) {
		place_of_dispatch.setText(pickupAddress);
	}

	/**
	 * 1.根据订单info的deliver属性获取一共有几个要显示的地址 2.如果是一条就get(0)元素,
	 * 3.否则获取多条地址,每条填充到LinearLayout中,格式为:收货地址:xxx.
	 */
	private void setOrderReceipt(LinearLayout place_of_receipt_group,
			OrderInfo orderInfo) {
		place_of_receipt_group.removeAllViews();
		// 单地址
		if (orderInfo.getDeliver().size() == 1) {
			// 显示收货地信息
			place_of_receipt_group.setVisibility(View.VISIBLE);

			// 引入额外的PlaceofReceiptLinearLayout,每一条代表一条收货信息
			PlaceofReceiptLinearLayout placeofreceipt = (PlaceofReceiptLinearLayout) mLayoutInflater
					.inflate(R.layout.fragment_currentorder_placeofreceipt,
							null);
			// 获得唯一一条收获地址信息
			DeliverInfo deliverEx = orderInfo.getDeliver().get(0);

			placeofreceipt.updateAddressTitle();
			placeofreceipt.updateAddress(deliverEx.getDeliverAddress());

			place_of_receipt_group.addView(placeofreceipt);
		}
		// 多地址
		else if (orderInfo.getDeliver().size() > 1) {
			place_of_receipt_group.setVisibility(View.VISIBLE);

			// 从一条订单中分拆出多条收货信息
			int index = 0;
			for (DeliverInfo deliverEx : orderInfo.getDeliver()) {
				PlaceofReceiptLinearLayout placeofreceipt = (PlaceofReceiptLinearLayout) mLayoutInflater
						.inflate(R.layout.fragment_currentorder_placeofreceipt,
								null);

				placeofreceipt.updateAddressTitleIndex(++index);
				placeofreceipt.updateAddress(deliverEx.getDeliverAddress());
				place_of_receipt_group.addView(placeofreceipt);
			}
		} else {
			place_of_receipt_group.setVisibility(View.GONE);
		}

	}

	private void setKilometer(TextView orderinfo_item_kilometer, String format) {
		orderinfo_item_kilometer.setText(format);
	}

	/**
	 * 设置按钮样式 和 底部的描述文字
	 * 
	 * @param orderState_button
	 */
	protected abstract void setOrderStatusAndReason(Button orderState_button,
			TextView orderinfo_item_killmeters_change, OrderInfo orderInfo,int position);

	/**
	 * 设置底部的tag标签, 底部标签默认不变红
	 * 
	 * @param orderInfo
	 * @param remark_gridview
	 * @param convertView
	 */
	protected void setRemarkTag(OrderInfo orderInfo, GridView remark_gridview,
			View convertView) {
		if (mTagList != null) {
			mTagList.clear();
		}

		if (orderInfo.getCarringType() > 0) {
			mTagList.add(mTagStrArr[0]);
		}
		if (orderInfo.getCollectCharges() > 0) {
			mTagList.add(mTagStrArr[1]);
		}
		if (orderInfo.getReceiptType() > 0) {
			mTagList.add(mTagStrArr[2]);
		}
		if (orderInfo.getIsFollowCar() > 0) {
			mTagList.add(mTagStrArr[3]);
		}
		if (orderInfo.getTrolleyNum() > 0) {
			mTagList.add(mTagStrArr[4]);
		}
		if (mTagList.size() == 0) {
			ViewHolder.get(convertView, R.id.seperate_line).setVisibility(
					View.GONE);
			remark_gridview.setVisibility(View.GONE);
		} else {
			ViewHolder.get(convertView, R.id.seperate_line).setVisibility(
					View.VISIBLE);
			remark_gridview.setVisibility(View.VISIBLE);
			remark_gridview.setAdapter(new OrderTagAdapter(mContext, mTagList));
			ReDrawGridView.setGridViewHeightBasedOnChildren(remark_gridview);
		}

	}

}
