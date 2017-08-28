package cn.bluerhino.driver.utils;

import java.util.List;

import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.OrderInfo.OrderState;

public class OrderInfoUtil {
	
	/**
	 * Describe:判断订单是2200状态 且 判断 是否处于可以抢单的状态
	 * 
	 * Date:2015-7-10
	 * 
	 * Author:liuzhouliang
	 */
	public static boolean isOrderNotAvail(OrderInfo info) {
		if (info.getOrderFlag() == OrderState.NEEDDRIVER2){
			if(info.getReason().equals("")){
				return true;
			}else{
				
			}
		}
		if (info.getOrderFlag() == OrderState.NEEDDRIVER3) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 过滤掉不可抢订单
	 * @param orderInfor 
	 */
	public static void filteringUnWaitOrderList(List<OrderInfo> orderInfor){
		//TODO
	}

}
