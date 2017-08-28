package cn.bluerhino.driver.db;

import java.util.List;

import android.content.Context;
import cn.bluerhino.driver.model.DeliverInfo;
import cn.bluerhino.driver.model.OrderInfo;

public class CurrentOrderHelper extends OrderInfoSQLiteHelper {

	private static final String CURRENT_WHERE = "OrderFlag<>5000 AND OrderFlag<20000";
	
	private static CurrentOrderHelper INSTANCE;

	private Context mContext;

	public synchronized static CurrentOrderHelper getInstance(Context context) {
		if (null == INSTANCE) {
			INSTANCE = new CurrentOrderHelper(context);
		}
		return INSTANCE;
	}

	private CurrentOrderHelper(Context context) {
		super(context);
		mContext = context;
	}

	@Override
    public List<OrderInfo> query() {
	    return super.query(CURRENT_WHERE);
    }
	
	public List<OrderInfo> relevanceQuery() {
		return relevanceQuery(CURRENT_WHERE);
	}
	
	public List<OrderInfo> relevanceQuery(int orderNum) {
		return relevanceQuery(String.format(WHERE, orderNum));
	}

	public List<OrderInfo> relevanceQuery(String where) {
		List<OrderInfo> orderList = null;
		orderList = query(where);
		CurrentDeliverHelper currentDeliverHelper = CurrentDeliverHelper.getInstance(mContext);
		for (OrderInfo orderInfo : orderList) {
			List<DeliverInfo> deliverInfos = currentDeliverHelper.query(String.format(WHERE,
			        orderInfo.getOrderNum()));
			orderInfo.setDeliver(deliverInfos);
		}
		return orderList;
	}

	@Override
	protected String getPath() {
		return SQLiteHelperConstant.CURRENT_ORDERINFO_PATH;
	}
	
	/**
	 * 订单状态机
	 * 
	 * @param flag
	 * @return
	 * @author chowjee
	 * @date 2014-10-24
	 */
	public int orderFlagMachineByPay(int flag) {		
		
		if(flag == OrderInfo.OrderState.DEPARTURESHIPADDRESS){
			return OrderInfo.OrderState.REACHPLACEOFRECEIPT;
		}
		if(flag ==  OrderInfo.OrderState.REACHPLACEOFRECEIPT) {// 到达收货地
			return OrderInfo.OrderState.SERVICEFINISH;
		}
		else if(flag >= OrderInfo.OrderState.ORDERCANCEL && flag < OrderInfo.OrderState.ORDERFINISH){// 从收货地出发
				
			return flag + 10000;
		}
		return flag;
				
	}
	
	
}
