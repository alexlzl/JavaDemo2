package com.minsheng.app.configuration;

public class MsRequestConfiguration {

	public static final String CONTENT_TYPE = "application/json; encoding=utf-8";
	// public static final String BASE_URL =
	// "http://10.7.8.55:8080/mscappport/";
	// public static final String BASE_URL =
	// "http://10.7.15.46:8089/mscappport/";
	// public static final String BASE_URL = "http://xqmobile.minshengec.com/";
	// 加密服务器
	public static final String BASE_URL = "http://172.30.10.235:8088/mscappportsecurity/";
	//
	// public static final String
	// BASE_URL="http://10.7.18.59:8088/mscappportsecurity/";
	// 线上
//	public static final String BASE_URL = "http://xqmobile.minshengec.com/";

	// public static final String BASE_URL = "http://172.16.18.11:8080/";

	// public static final String BASE_URL =
	// "http://10.7.8.55:8080/mscappport/";

	// public static final String BASE_URL =
	// "http://10.7.15.46:8080/mscappport/";
	// public static final String BASE_URL =
	// "http://10.7.8.55:8080/mscappport/";
	// public static final String BASE_URL = "http://10.7.6.6:8080/mscappport/";
	// public static final String BASE_URL =
	// "http://10.7.6.24:8080/mscappport/";
	// public static final String BASE_URL =
	// "http://10.7.17.125:8080/mscappport/";

	// 验证APP
	public static final String VERIFICATION_APP = "communityport/validateauth.jhtml";
	// 检测APP升级
	public static final String CHECK_APP_UPDATE = "versionportcontroller/getnewestversion.jhtml?";
	/**
	 * 骑士客户端==================================================
	 */
	// 登录
	public static final String LOGIN = "deliveryport/deliveryLogin.jhtml?";
	// 发送手机验证码
	public static final String SEND_VERIFICATION_CODE = "deliveryport/sendMobileCode.jhtml?";
	// 获取图片验证码
	public static final String GET_PIC_MESSAGE_CODE = "deliveryport/getValidateImg.jhtml?";
	// 找回密码，核实验证码信息
	public static final String VERIFY_MESSAGE_CODE = "deliveryport/findPassword.jhtml?";
	// 密码重置
	public static final String RESET_PASSWORD = "deliveryport/resetDeliveryPassword.jhtml?";
	// 首页
	public static final String HOME_PAGE = "deliveryorder/deliveryIndex.jhtml?";
	// 个人消息
	public static final String MY_MESSAGE = "deliveryport/deliveryMsgList.jhtml?";
	// 订单列表
	public static final String APPOINT_ORDER = "deliveryorder/deliveryOrderList.jhtml?";
	// 订单详情
	public static final String ORDER_DETAIL = "deliveryorder/deliveryOrderDetail.jhtml?";
	// 修改订单状态
	public static final String MODIFY_ORDER_STATE = "deliveryorder/changeOrderStatus.jhtml?";
	// 修改车载量，快递员状态
	public static final String MODIFY_COURIER_STATE = "deliveryorder/changeDeliveryStatus.jhtml?";
	// 完善订单列表
	public static final String COMPLETE_ORDER_LIST = "deliveryorder/categoryProductList.jhtml?";
	// 评价列表
	public static final String COURIER_EVALUATE = "deliveryport/deliveryOrderEvaluate.jhtml?";
	// 加入购物车
	public static final String ADD_SHOP_CAR = "deliveryorder/addOrderCart.jhtml?";
	// 删除购物车商品
	public static final String DELETE_SHOPCAR_PRODUCT = "deliveryorder/delOrderCartProduct.jhtml?";
	// 更新购物车商品
	public static final String UPDATE_SHOPCAR_PRODUCT = "deliveryorder/upOrderCartProduct.jhtml?";
	// 送店确认衣服信息
	public static final String SENDSHOP_CONFIRM_CLOTHES_INFOR = "deliveryorder/updateWashOrder.jhtml?";
	// 收取现金
	public static final String GET_MONEY = "deliveryorder/updateOrderPayStatus.jhtml?";
	// 返厂重洗
	public static final String WASH_AGAIN = "deliveryorder/afreshWashOrder.jhtml?";
	// 搜索订单
	public static final String SEARCH_ORDER = "deliveryorder/queryWashOrder.jhtml?";
	// 更新配送员坐标
	public static final String UPDATE_LOCATION = "deliveryport/setDeliveryLocation.jhtml?";
	// 更新配送员消息状态
	public static final String UPDATE_MESSAGE_STATE = "deliveryport/upDeliveryMsgRead.jhtml?";
	// 修改时间
	public static final String MODIFY_ORDER_TIME = "deliveryorder/changeOrderTime.jhtml?";
	// 取消修改时间
	public static final String CANCEL_MODIFY_ORDER_TIME = "deliveryorder/cancelChangeOrderTime.jhtml?";
	// 扫描查询订单
	public static final String SCAN_ORDER = "deliveryorder/searchOrderByOneCode.jhtml?";
	//过滤订单时间
	public static final String ORDER_FILTER_TIME="deliveryorder/searchTimeList.jhtml?";
	//查询过滤时间订单列表
	public static final String ORDER_FILTER_BY_DATE="deliveryorder/searchOrderByDate.jhtml?";
}
