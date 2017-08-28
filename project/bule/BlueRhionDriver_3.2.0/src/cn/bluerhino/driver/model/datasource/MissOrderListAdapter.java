package cn.bluerhino.driver.model.datasource;

import java.util.List;

import android.content.Context;
import android.widget.Button;
import cn.bluerhino.driver.model.OrderInfo;

public class MissOrderListAdapter extends OutofTimeOrderInfoListAdapter {

	public MissOrderListAdapter(Context context, List<OrderInfo> list) {
		super(context, list);
	}

	@Override
	protected void setOrderReason(Button orderStateView,
			OrderInfo orderInfo) {
		orderStateView.setText(orderInfo.getReason());
	}

}
