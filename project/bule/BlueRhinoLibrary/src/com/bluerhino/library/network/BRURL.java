package com.bluerhino.library.network;

/***
 * BlueRhion URL ...
 * 
 * @author chowjee
 * 
 */
public interface BRURL {

	// public static String HOST_IP = "http://www.lanxiniu.cn";

	// public static final String HOST_IP = "http://192.168.1.120:20000";

	public static final String HOST_IP = "http://119.253.57.204:10000";

	// public static final String HOST_IP = "http://192.168.1.118:8081";

	public static final String HOST_SPEEKING = HOST_IP + "/Api1/";

	public static final String ACTION_GET_ORDERINFO_NEW = HOST_SPEEKING + "getOrderInfo_new?";

	public static final String ACTION_EXECUTE_ORDER_NEW = HOST_SPEEKING + "executeOrderNew";

	public static final String ACTION_CHECKIN = HOST_SPEEKING + "driverRegister?";

	public static final String ACTION_GETPAY = HOST_SPEEKING + "getOrderPay?";

	public static final String ACTION_DRIVER_LOGIN = HOST_SPEEKING + "driverLogin?";

	public static final String ACTION_UPLOAD_LOCATION = HOST_SPEEKING + "getIsOrderTest?";

	public static final String ACTION_GET_DRIVER_ORDERINFO = HOST_SPEEKING + "GetDriverOrder?";

	public static final String ACTION_GET_ORDER_REMARK = HOST_SPEEKING + "GetOrderRemark?";


	// ----------------------------------------------------------------------
	
	
	public static final String UPGRADE_HD_URL = HOST_IP + "/UpdateApk/lanxiniu_driver_update.xml";

	// ---- new address ----

	public static final String HOST = HOST_IP+"/Api/";
	
	// http://119.253.57.204:10000/Api/getCurrentTime
	public static final String ACTION_GET_CURRTEN_TIME = HOST + "getCurrentTime";

	// http://192.168.1.120:10000/Api/getCurrentOrderInfo?did=13&page=1
	//public static final String CURRENT_ORDERLIST_GET = HOST + "getCurrentOrderInfo?did=%s&page=%d";
	
	/** 获取当前所有订单信息 */
	public static final String CURRENT_ORDERLIST_POST = HOST + "getCurrentOrderInfo";
	//public static final String CURRENT_ORDERINFO_GET = HOST + "getCurrentOrderInfo?did=%s&orderId=%s";
	
	/**获取当前单个订单信息*/
	public static final String CURRENT_ORDERINFO_POST = HOST + "getOrderInfo";
	
	// http://119.253.57.204:10000/Api/GetHistoryOrder?did=8&page=1
	public static final String HISTORY_ORDERINFO_POST = HOST + "GetHistoryOrder";

	public static final String WAITLIST_ORDERINFO_GET = HOST + "GetWaitListOrder";

	// http://119.253.57.204:10000/Api/driverLogin?name=18610968829&password=123456
	public static final String LOGOIN_GET = HOST + "driverLogin?name=%s&password=%s";
	
	public static final String LOGOIN_POST = HOST + "driverLogin";

	// http://119.253.57.204:10000/Api/driverCheckIn?MobileAllNet=%7CHSDPA%7CWIFI%7C&MoblieIp=-1744131904
	// &MoblieGpsStatus=3&did=8&MoblieModel=H30-T10&MoblieActiveNet=WIFI&type=1&MoblieMac=88%3Ae3%3Aab%3Acb%3A46%3A8c&version=1.2.3
	public static final String DRIVERCHECKIN = HOST
	        + "driverCheckIn?MobileAllNet=%s&MoblieIp=%s&MoblieGpsStatus=%s&did=%s&MoblieModel=%s&MoblieActiveNet=%s&type=%s&MoblieMac=%s&version=%s";
	
	// http://119.253.57.204:10000/Api/getCheckInData?did=8
	public static final String DRIVERCHECKIN_STATUS = HOST+"getCheckInData?did=%s";
	
	// http://119.253.57.204:10000/Api/getOrderPay?did=8&oId=403611&ic=0&km=10&autokm=170
	public static final String PAYORDER_GET = HOST + "getOrderPay?did=%s&oId=%d";
	public static final String PAYORDER_POST = HOST + "getOrderPay";
	
	// http://192.168.1.120:10000/Api/executeOrder?did=8&oId=402919&oType=4600&deliverid=2756&delivertype=1&y=116.485024&x=39.908703&ip=0&pay=10&r=客户不支持
	public static final String EXECUTEORDER_GET_BASE = HOST+"executeOrder?did=%s&oId=%d&oType=%d&delivertype=%d&y=%s&x=%s";
	public static final String EXECUTEORDER_GET = HOST+"executeOrder?did=%s&oId=%d&oType=%d&deliverid=%d&delivertype=%d&y=%s&x=%s&ip=%d&pay=%d&r=%s";
	
	public static final String EXCUTEORDER_GET_NEW = HOST+"executeOrder?did=%s&oId=%d&oType=%d&lng=%s&lat=%s&pay=%d";
	
	public static final String EXECUTEORDER_POST = HOST+"executeOrder";
	
	public static final String UPLOADPOI = HOST+"uploadPoi";
	
	public static final String DISTANCE_GET = HOST +"getTravelDistance?did=%s&oId=%d";
	
	public static final String DRIVER_INVITE_URL = HOST_IP+"/Driver/index?did=%s";
	public static final String DRIVER_FAQ_URL = HOST_IP+"/Driver/faq?did=%s";
	public static final String UPLOAD_DEVICE_URL = HOST_IP+"/Driver/upload";

}
