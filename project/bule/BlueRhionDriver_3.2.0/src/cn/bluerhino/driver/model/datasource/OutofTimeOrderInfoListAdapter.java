package cn.bluerhino.driver.model.datasource;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bluerhino.driver.model.OrderInfo;

public abstract class OutofTimeOrderInfoListAdapter extends OrderListAdapter {
	protected String mStrOutofDay;
	protected String mStrToday;
	protected String mStrTomorrow;
	protected String mStrAfterTomorrow;
	protected String mStrNextyear;

	public OutofTimeOrderInfoListAdapter(Context context, List<OrderInfo> list) {
		super(context, list);
	}

	// 错过页面，马上服务的的订单不展示“即时服务”四个字，改为展示订单发生的时间，即成功抢单的事件
	@Override
	protected void setOrderTimedesc(OrderInfo orderInfo,
			TextView curlist_item_sendtime) {
		curlist_item_sendtime.setText(super.getServerTime(orderInfo
				.getTransTime() * 1000));
	}

	@Override
	protected void setOrderStatusAndReason(Button orderStateView,
			TextView orderinfo_item_killmeters_change, OrderInfo orderInfo,int position) {
		// 根据条件进行按钮的显示以及隐藏 和 文字公里数的显示
		orderStateView.setOnClickListener(null);
		orderStateView.setTextColor(Color.parseColor("#00A7E3"));
		orderStateView.setBackgroundColor(Color.parseColor("#ffffff"));
		LayoutParams params = orderStateView.getLayoutParams();
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		orderStateView.setLayoutParams(params);
		LinearLayout orderStateParentView = (LinearLayout) orderStateView
				.getParent();
		LinearLayout.LayoutParams parentParams = (LinearLayout.LayoutParams) orderStateParentView
				.getLayoutParams();
		parentParams.gravity = Gravity.CENTER_VERTICAL;
		orderStateParentView.setLayoutParams(parentParams);
		// 显示错过订单的原因;
		setOrderReason(orderStateView, orderInfo);
		//按钮底部的文字不显示
		orderinfo_item_killmeters_change.setVisibility(View.GONE);
	}
	
	protected abstract void setOrderReason(Button orderStateView, OrderInfo orderInfo);

}
