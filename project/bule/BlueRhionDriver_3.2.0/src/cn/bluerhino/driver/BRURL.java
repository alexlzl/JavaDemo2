package cn.bluerhino.driver;

public interface BRURL {

	// 测试环境
	// public static final String HOST_IP_TEST = "http://115.29.34.206:7078";
	public static final String HOST_IP_TEST = "http://115.29.34.206:7086";
	// 线上环境
	public static final String HOST_IP_ONLINE = "http://www.lanxiniu.com";
	public static final String HOST_IP = ApplicationController.testSwitch ? HOST_IP_TEST
			: HOST_IP_ONLINE;
	public static final String WEB_HOST_IP = HOST_IP;
	public static final String WAP_HOST_IP = HOST_IP + "/wap";
	
	//----------------wap页----------------

	public static final String WAP_COMMON_SUFFIX = "?did=%1$s&session_id=%2$s&deviceid=%3$s&fr=%4$s";

	/** 帐户信息展示 */
	public static final String ACCOUNT_URL = WAP_HOST_IP + "/account"
			+ WAP_COMMON_SUFFIX;
	/** 内部访问,暂不需要, 账户信息子页面:提现 */
	// public static final String ACCOUNT_WITHDRAW_URL = WAP_HOST_IP+
	// "/account_withdraw";
	/** 账户信息子页面: 包含订单服务费的收支记录的列表 */
	public static final String ACCOUNT_DETAIL_URL = WAP_HOST_IP
			+ "/account_detail" + WAP_COMMON_SUFFIX;

	/** 顾客评分 */
	public static final String CUSTOMER_GRADE_URL = WAP_HOST_IP
			+ "/customer_grade" + WAP_COMMON_SUFFIX;

	/** 邀请好友 */
	public static final String DRIVER_INVITE_URL = WEB_HOST_IP
			+ "/Driver/index" + WAP_COMMON_SUFFIX;

	/** 月度周度司机排名 */
	public static final String ORDER_RANK_URL = WAP_HOST_IP + "/order_rank"
			+ WAP_COMMON_SUFFIX;
	
	//----------------错误载入图片url----------------
	String ERROR_IMAGE_URL = "http://a.b/error.png";
	
	//----------------司机端api----------------
	/**
	 * 检测用户是否存在
	 * 0代表已经存在该用户
	 * 1代表不存在该用户
	 */
	public static final String CHECKUSER_IS_NOT_EXIST_POST_URL = WEB_HOST_IP
			+ "/DriverApi/checkUserIsNOTExist";
	/**
	 * 城市和子车型对应关系
	 */
	public static final String CITYCAR_DETAIL_POST_URL = WEB_HOST_IP
			+ "/BaseApi/getCityCarDetail";
	/**
	 * 获得当前订单: ‘orderId’: 0-所有 >0特定订单
	 */
	public static final String CURRENT_ORDER_LIST_POST_URL = WEB_HOST_IP
			+ "/DriverApi/getCurrentOrderInfo";
	/**
	 * 司机签到/签退
	 */
	public static final String DRIVER_CHECKIN_POST_URL = WEB_HOST_IP
			+ "/DriverApi/driverCheckIn";
	/**
	 * 获取司机信息
	 */
	public static final String DRIVER_DETAIL_POST_URL = WEB_HOST_IP
			+ "/DriverApi/getDriverInfo";
	/**
	 * 司机登陆
	 */
	public static final String DRIVER_LOGIN_REQUEST_POST_URL = WEB_HOST_IP
			+ "/DriverApi/driverLogin";
	/**
	 * 司机登出: 需要取消Jpush推送
	 */
	public static final String DRIVER_LOGOUT_REQUEST_POST_URL = WEB_HOST_IP
			+ "/DriverApi/driverLogout";
	/**
	 * 司机注册
	 */
	public static final String DRIVER_REGISTER_POST_URL = WEB_HOST_IP
			+ "/DriverApi/driverRegister";
	/**
	 * 获取验证码
	 */
	public static final String DYNAMIC_CODE_POST_URL = WEB_HOST_IP
			+ "/Phone/getDynamicCode";
	/**
	 * 执行订单接口
	 */
	public static final String EXECUTE_ORDER_POST_URL = WEB_HOST_IP
			+ "/DriverApi/executeOrder";
	/**
	 * 获得历史订单
	 */
	public static final String HISTORY_ORDERLIST_POST_URL = WEB_HOST_IP
			+ "/DriverApi/getHistoryOrder";
	/**
	 * 获得错过订单
	 */
	public static final String MISS_ORDERLIST_POST_URL = WEB_HOST_IP
			+ "/DriverApi/getMissedOrder";
	/**
	 * 获取支付金额
	 */
	public static final String ORDERPAY_INFO_POST_URL = WEB_HOST_IP
			+ "/DriverApi/getOrderPay";
	/**
	 * 重置密码
	 */
	public static final String RESET_PASSWORD_POST_URL = WEB_HOST_IP
			+ "/DriverApi/ResetPassword";
	/**
	 * 更新司机信息
	 */
	public static final String UPDATE_DRIVERINFO_POST_URL = WEB_HOST_IP
			+ "/DriverApi/updateDriverInfo";
	/**
	 * 上传图片
	 */
	public static final String UPLOAD_IMAGE_POST_URL = WEB_HOST_IP
			+ "/DriverApi/uploadImg";
	/**
	 * 上传坐标
	 */
	public static final String UPLOAD_POI_POST_URL = WEB_HOST_IP
			+ "/DriverApi/uploadPoi";
	/**
	 * 获得多个抢单列表
	 */
	public static final String WAIT_ORDER_LIST_POST_URL = WEB_HOST_IP
			+ "/DriverApi/GetWaitListOrder";
	
	/**
	 * 已过时, 获得单个抢单订单信息(被WAIT_ORDER_LIST_POST_URL代替了)
	 */
	String WAIT_ORDER_POST_URL = WEB_HOST_IP
			+ "/DriverApi/GetWaitOrderByorderId";
	
	/**
	 * 已过时, 被WAIT_ORDER_LIST_POST_URL代替了
	 */
	String WAITINFO_BY_ORDER_POST_URL = WEB_HOST_IP
			+ "/DriverApi/getWaitInfoByoId";
	/**
	 * 获取loading页面（广告页面）的url
	 */
	public static final String GET_LOADING_URL = WEB_HOST_IP
			+ "/BaseApi/getLoadingPic";
	/**
	 * 获取住tab页面服务页面的详细信息
	 */
	public static final String GET_TABINFOR = WEB_HOST_IP
			+ "/BaseApi/getTabPageInfo";
	/**
	 * 记录司机严重错误 
	 * success 1操作成功 2失败
	 */
	public static final String SAVEDRIVERERROR = WEB_HOST_IP
			+ "/DriverApi/saveDriverError";
	/**
	 * 判断司机抢单情况 1抢单成功 2抢单失败 3未抢单
	 */
	String CHECK_DRIVER_CFMORDER_URL = WEB_HOST_IP
			+ "/DriverApi/checkDriverCfmOrder";

}
