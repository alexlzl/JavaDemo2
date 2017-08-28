package cn.bluerhino.driver;

/**
 * Action集合
 * 
 * @author chowjee
 * @date 2014-9-23
 */
public interface BRAction {
	
	/**
	 * 订单列表发生变化
	 */
	String ACTION_ORDER_LIST_CHANGED = "cn.bluerhno.orderlist_CHANGED";

	String EXTRA_ORDER_INFO = "OrderInfo"; 
	
	String EXTRA_ORDER_ID = "order_id";	
	
	String EXTRA_ORDER_NUM = "OrderNum";
	
	String EXTRA_ORDER_PAYINFO = "PayInfo"; 
	
	String EXTRA_DRIVERINFO = "driverinfo"; 
	
	/**
	 * 司机个人信息状态
	 */
	String EXTRA_ACCOUNTSTAT = "accountStat"; 

}
