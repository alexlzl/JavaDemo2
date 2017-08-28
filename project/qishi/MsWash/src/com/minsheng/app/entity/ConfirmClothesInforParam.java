package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * @describe:送店匹配信息参数实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-25下午1:38:44
 * 
 */
public class ConfirmClothesInforParam implements Serializable {
	private static final long serialVersionUID = 1L;
	private int orderProductId;
	private String merchantOneCode;

	public int getOrderProductId() {
		return orderProductId;
	}

	public void setOrderProductId(int orderProductId) {
		this.orderProductId = orderProductId;
	}

	public String getMerchantOneCode() {
		return merchantOneCode;
	}

	public void setMerchantOneCode(String merchantOneCode) {
		this.merchantOneCode = merchantOneCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ConfirmClothesInforParam [orderProductId=" + orderProductId
				+ ", merchantOneCode=" + merchantOneCode + "]";
	}

}
