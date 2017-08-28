package cn.bluerhino.driver.db.observer;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRAction;
import cn.bluerhino.driver.db.CurrentOrderHelper;
import cn.bluerhino.driver.model.OrderInfo;

public class CurrentOrderObserver extends SQLiteObserver<OrderInfo> {

	private final CurrentOrderHelper mCurrentOrderHelper;

	public CurrentOrderObserver(Context context, List<OrderInfo> orderList) {
		super(context, orderList);
		mCurrentOrderHelper = CurrentOrderHelper.getInstance(context);
		// mCurrentDeliverHelper = CurrentDeliverHelper.getInstance(context);
	}

	@Override
	public List<OrderInfo> onChange() {
		List<OrderInfo> orderList = mCurrentOrderHelper.relevanceQuery();
		
		if (null != tList && null != orderList) {
			tList.clear();
			tList.addAll(orderList);
		}
		
		notifyOrderStateChanged();
		return orderList;
	}
	
	
	private void notifyOrderStateChanged() {
		ApplicationController.getInstance().getLocalBroadcastManager()
		        .sendBroadcast(new Intent(BRAction.ACTION_ORDER_LIST_CHANGED));
	}
}
