package com.minsheng.app.configuration;

/**
 * 
 * 
 * @describe:配置程序公用常量值
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-17上午10:12:34
 * 
 */
public class MsConfiguration {
	// 设备类型标示
	public static final int DEVICE_TYPE = 0;
	// 退出toast持续时间
	public static int EXIT_TOAST_TIME = 3000;
	// 手机找回密码获取验证码倒计时间180秒
	public static final int FIND_PWD_CHECK_CODE_TIME = 60;
	// 定位时间间隔
	public static final int LOCATION_INTERVAL = 1000 * 60;
	// public static final int LOCATION_INTERVAL = 1000;
	// 商家ID
	public static final String BUSINESS_ID_KEY = "businessId";
	public static final String ORDER_STATE_KEY = "order_state_key";
	// 控制是否处于调试模式
	public static boolean DEBUG = false;
	public static final int SUCCESS = 1000;
	public static final int FAIL = 1001;
	public static final int OVER = 1010;
	public static final int COUNT_DOWNING = 4;
	public static final int COUNT_DOWN_END = 5;
	public static final int PAGE_SIZE = 10;
	public static final String MOBILE_PHONE_KEY = "mobile_phone_key";
	// 订单ID KEY
	public static final String ORDER_ID_KEY = "order_id_key";
	// 订单列表类型
	public static final String ORDER_TYPE = "order_type";
	public static final String COMPLETE_ORDER = "complete_order";
	public static final String ON_THE_WAY = "on_the_way";
	public static final String SEND_SHOP_ORDER = "send_shop_order";
	public static final String GET_CLOTHES_BACK = "get_clothes_back";
	public static final String IS_SENDDING = "is_send";
	public static final String NOT_PAY = "not_pay";
	public static final String WASH_AGAIN = "wash_again";
	public static final String WASH_AGAGIN_OVER = "wash_again_over";
	public static final String CONFIRM_GET_CASH = "get_cash";
	public static final String ORDER_OVER_CAN_WASH_AGAIN = "order_over_can_wash_again";
	public static final String ORDER_OVER_CANNOT_WASH_AGAIN = "order_over_cannot_wash_again";
	public static final String ORDER_CANCEL = "order_cancel";
	public static final String FINISH_ORDER_WAIT_CONFIRM = "finish_order_wait_confirm";
	public static final String WASH_AGAGIN_NOT_PAY = "wash_again_not_pay";
	public static final String WASH_AGAGIN_PAY_OVER = "wash_again_pay_over";
	public static final String PAGE_TYPE_KEY = "page_type_key";
	public static final String PAGE_TYPE_DETAIL = "page_type_detail";
	public static final String PAGE_TYPE_WASH_AGAIN = "page_type_wash_again";
	public static final String NORMAL_ORDER_LIST_TYPE = "order_list_type";
	public static final String CANCEL_ORDER="cancel_order";
	// 请求订单列表参数类型
	public static final int APPOINT_LIST = 0;
	public static final int SEND_SHOP_LIST = 1;
	public static final int GET_CLOTHES_LIST = 2;
	public static final int SEND_CLOTHES_LIST = 3;
	public static final int FINISH_ORDER_LIST = 4;
	public static final int WASH_AGAIN_LIST = 5;
	public static final int MY_ORDER_LIST = 6;
	public static final int CANCEL_ORDER_LIST=7;
	// 更改订单状态参数值
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	// 标记页面请求类型
	public static final int APPOINT_ORDER_TO_DETAIL = 1001;
	// 标记从完善订单到完善订单确认页面
	public static final int REQUEST_FROM_CONFIRM_ORDER = 1002;
	// 标记从送店页面到订单修改页面
	public static final int REQUEST_FROM_SEND_SHOP = 1005;
	// 标记从首页到消息页面
	public static final int HOME_PAGE_TO_MESSAGE = 1003;
	// 标记从首页到预约订单页面
	public static final int HOME_PAGE_TO_APPOINT = 1004;
	// 标记从哪进入扫描页面
	public static final String FROM_WHERE_TO_SCAN = "from_where_to_scan";
	public static final String FROM_CONFIRM_ORDER = "from_confirm_order";
	public static final String FROM_QUERY_ORDER = "from_query_order";
	public static final String FROM_MODIFY_ORDER_INFOR = "from_modify_order_infor";
	//标记从扫描页面进入详情页面
	public static final String FROM_SCAN_TO_DETAIL="from_scan_to_detail";
	// 保存用户信息对象文件名
	public static final String LOGIN_INFOR_FILE = "user_infor_file";
	/**
	 * 加密接口参数值
	 */
	// 实体名称对应的KEY
	public static final String BEAN_NAME_KEY = "e1f6d28a";
	// 接口认证对应的KEY
	public static final String AUTHENTICATION_KEY = "f65b56db";
	// 传入加密参数对应的KEY
	public static final String PARAMS_KEY = "ae0ac5bac";
	// 认证秘钥
	public static final String AUTHENTICATION_SECRET = "1c169e98d753988347394b80f95e860c";
	// old===============
	public static final String CustomerUserParam = "CustomerUserParam";
	public static final String VailidateCodeParam = "VailidateCodeParam";
	public static final String SysVersionParam = "SysVersionParam";
	/**
	 * 各个接口对应的实体名称====================================
	 */
	// 登录
	public static final String LOGIN_INTERFACE_NAME = "DeliveryManParam";
	// 首页，订单
	public static final String HOME_PAGE_INTERFACE_NAME = "DeliverOrderParam";

}
