package cn.bluerhino.driver.model.datasource;

import java.util.List;

import android.content.Context;
import android.widget.Button;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.OrderInfo;

public class HistoryOrderListAdapter extends OutofTimeOrderInfoListAdapter {

	public HistoryOrderListAdapter(Context context, List<OrderInfo> list) {
		super(context, list);
	}

	@Override
	protected void setOrderReason(Button orderStateView, OrderInfo orderInfo) {
		orderStateView.setText(R.string.orderstate_servicefinish);
	}
	
}
