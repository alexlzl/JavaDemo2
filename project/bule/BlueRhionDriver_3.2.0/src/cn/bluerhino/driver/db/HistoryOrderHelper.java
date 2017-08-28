package cn.bluerhino.driver.db;

import java.util.List;

import android.content.Context;
import cn.bluerhino.driver.model.DeliverInfo;
import cn.bluerhino.driver.model.OrderInfo;

public class HistoryOrderHelper extends OrderInfoSQLiteHelper {

	private static HistoryOrderHelper INSTANCE;
	
	private Context mContext;
	
	public synchronized static HistoryOrderHelper getInstance(Context context){
		if(null == INSTANCE){
			INSTANCE = new HistoryOrderHelper(context);
		}
		return INSTANCE;
	}
	
	private HistoryOrderHelper(Context context) {
		super(context);
		mContext = context;
	}
	
	public List<OrderInfo> relevanceQuery(String where) {
		List<OrderInfo> orderList = null;
		orderList = query(where);
		HistoryDeliverHelper historyDeliverHelper = HistoryDeliverHelper.getInstance(mContext);
		for (OrderInfo orderInfo : orderList) {
			List<DeliverInfo> deliverInfos = historyDeliverHelper.query(String.format(WHERE,
					orderInfo.getOrderNum()));
			orderInfo.setDeliver(deliverInfos);
		}
		return orderList;
	}
	
	public List<OrderInfo> relevanceQuery(int orderNum) {
		return relevanceQuery(String.format(WHERE, orderNum));
	}
	
	public List<OrderInfo> relevanceQuery() {
		return relevanceQuery("");
	}

	@Override
	protected String getPath() {
		return SQLiteHelperConstant.HISTORY_ORDERINFO_PATH;
	}
	
}
