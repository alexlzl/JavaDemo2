package cn.bluerhino.driver.db.observer;

import java.util.List;

import android.content.Context;
import cn.bluerhino.driver.db.OrderDealSQLiteHelper;
import cn.bluerhino.driver.model.OrderDeal;

public class OrderDealObserver extends SQLiteObserver<OrderDeal> {
	
	private final OrderDealSQLiteHelper mOrderDealSQLiteHelper;
	public OrderDealObserver(Context context, List<OrderDeal> orderDealList) {
		super(context, orderDealList);
		mOrderDealSQLiteHelper = OrderDealSQLiteHelper.getInstance(context);
	}

	@Override
	public List<OrderDeal> onChange() {
		List<OrderDeal> orderList = mOrderDealSQLiteHelper.query();
		
		if (null != tList && null != orderList) {
			tList.clear();
			tList.addAll(orderList);
		}
		
		return orderList;
	}

}
