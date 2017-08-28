package cn.bluerhino.driver.helper;

import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.OrderInfo.OrderState;
import cn.bluerhino.driver.utils.NumberUtils;

public class OrderInfoHelper {

	/**
	 * Describe:获取订单类型
	 * 
	 * Date:2015-7-9
	 * 
	 * Author:liuzhouliang
	 * 
	 * @return false实时订单, true预约订单
	 */
	public static boolean getOrderType(OrderInfo infor) {
		int type = infor.getServeType();
		if (200 == type) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Describe:获取订单类型
	 */
	public static String getOrderTypeString(OrderInfo infor) {
		if (OrderInfoHelper.getOrderType(infor)) {
			return "预约订单";
		} else {
			return "实时订单";
		}
	}
	
	/**
	 * Describe:获取车辆租赁类型
	 * 
	 * Date:2015-7-9
	 * 
	 * Author:liuzhouliang
	 */
	public static String getLeaseType(int num) {
		String typeString = null;
		switch (num) {
		case 1:
			typeString = "跑一趟";
			break;
		case 2:
			typeString = "日租";
			break;
		case 3:
			typeString = "半日租";
			break;
		case 4:
			typeString = "买入卖出";
			break;
		default:
			typeString = "默认类型";
			break;
		}
		return typeString;
	}

	/**
	 * 如果新订单和抢单成功播报语音文字
	 * 
	 * @return 实时／预约，预约包括半日租和日租）+据您X公里+预计XX元
	 */
	public static String getOrderDesc(OrderInfo info) {
		StringBuilder orderDesc = new StringBuilder();
		String timeInterval = ",,,,,,,,,,,,,,,,,,,,,,,";
		/**
		 * 可以进行抢订单，提示进行抢单============
		 */
		if (getOrderType(info)) {
			orderDesc.append("预约");
			orderDesc.append(timeInterval);
			// 租赁类型
			orderDesc.append(OrderInfoHelper.getLeaseType(info.getOrderType()));
		} else {
			orderDesc.append("实时");
			orderDesc.append(timeInterval);
			// 距离公里数
			orderDesc.append("距离您");
			String robDistance = OrderInfoHelper.getRobDistance(info);
			try {
				orderDesc.append(NumberUtils.parse(robDistance));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			orderDesc.append("公里");
		}
		// 价格
		orderDesc.append(",预计" + info.getBillMoney() + "元");
		return orderDesc.toString();
	}
	
	public static boolean isOrderCanGet(OrderInfo orderInfo){
		if (orderInfo.getOrderFlag() == OrderState.NEEDDRIVER2) {
			if (orderInfo.getReason().equals("")){
				return true;
			}
		}
		return false;
	}
	

	/**
	 * 获得此时此刻距离实际公里数
	 */
	private static String getRobDistance(OrderInfo orderInfo) {
		String robDistance = orderInfo.getRobDistance();
		if (robDistance.contains("公里")) {
			robDistance = robDistance.split("公里")[0];
		}
		return robDistance;
	}
}
