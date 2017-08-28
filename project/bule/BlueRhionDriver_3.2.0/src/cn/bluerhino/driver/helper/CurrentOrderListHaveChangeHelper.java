package cn.bluerhino.driver.helper;

import java.util.List;

import cn.bluerhino.driver.model.OrderInfo;

public class CurrentOrderListHaveChangeHelper {
	
	/**
	 * Describe:判断缓存数据列表中是否有已经状态改变的单个订单
	 * 
	 * Date:2015-8-6
	 * 
	 * Author:liuzhouliang
	 */
	public static boolean isHaveChangeOrder(List<OrderInfo> modelList, int orderNum) {
		for(OrderInfo orderInfo: modelList){
			if (orderInfo.getOrderNum() == orderNum) {
				return true;
			}
		}
		return false;
	}

}
