package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * @describe:返厂重洗，参数实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-25下午8:38:37
 * 
 */
public class WashAgainParam implements Serializable {
	private static final long serialVersionUID = 1L;
	private int orderProductId;
	private String productName;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getOrderProductId() {
		return orderProductId;
	}

	public void setOrderProductId(int orderProductId) {
		this.orderProductId = orderProductId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "WashAgainParam [orderProductId=" + orderProductId
				+ ", productName=" + productName + "]";
	}

}
