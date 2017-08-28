package cn.bluerhino.driver.controller.ordermanager;

public class CountdownOrderInfoUtil {
	private static final int DELAY_TIME_LENGTH = 120;
	
	/*
	 * 条件:在按钮可用的时点击的结果告知已抢单
	 * 描述:该订单对象设置成被抢状态,设置该订单被响应了
	 */
	public static void setGrabOrderInGrab(CountdownOrderInfo orderInfo){
		if(!orderInfo.isClicked()){
			int second = orderInfo.getSecond();
			// 延长120s显示
			orderInfo.setSecond(second + DELAY_TIME_LENGTH);
			orderInfo.setReason("已抢单");//表示抢单成功
			//底部文字为带用户选择不要了
			orderInfo.setClicked(true);
			orderInfo.setResponse(false);
		}
	}
	
	/**
	 * 条件:在被抢单状态向下已延长120s, 已是被抢单状态, 设置抢单成功和失败的回馈信息
	 * 描述:更新reason
	 */
	public static void setReason(CountdownOrderInfo orderInfo, String reason) {
		if(orderInfo.isClicked() && !orderInfo.isResponse()){
			orderInfo.setReason(reason);
			orderInfo.setResponse(true);
		}
	}
	
	/**
	 * 条件:在抢单列表页下按钮可用的时点击的结果直接告知抢单失败
	 * 描述:告知抢单失败
	 */
	public static void setReasonInNotGrab(CountdownOrderInfo orderInfo) {
		setReasonInNot(orderInfo, "抢单失败");
	}
	
	/**
	 * 条件:详情页下在按钮可用的时点击的结果直接告知抢单失败
	 * 描述:告知抢单失败
	 */
	public static void setReasonInNotGrabDetail(CountdownOrderInfo orderInfo) {
		setReasonInNot(orderInfo, "订单已被抢走");
	}
	
	public static void setReasonInNot(CountdownOrderInfo orderInfo, String reason) {
		if(!orderInfo.isClicked()){
			orderInfo.setReason(reason);
			orderInfo.setClicked(true);
			orderInfo.setResponse(true);
		}
	}
	
}
