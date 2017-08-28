package cn.bluerhino.driver.model;

import java.io.Serializable;

/**
 * Describe:订单状态更改对应实体
 * 
 * Date:2015-9-8
 * 
 * Author:liuzhouliang
 */
public class ChangeOrderInfor implements Serializable {
	private static final long serialVersionUID = 1L;
	private int orderId;
	private long time;
	private int orderChangeType;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getOrderChangeType() {
		return orderChangeType;
	}

	public void setOrderChangeType(int orderChangeType) {
		this.orderChangeType = orderChangeType;
	}

	@Override
	public String toString() {
		return "ChangeOrderInfor [orderId=" + orderId + ", time=" + time
				+ ", orderChangeType=" + orderChangeType + "]";
	}

}
