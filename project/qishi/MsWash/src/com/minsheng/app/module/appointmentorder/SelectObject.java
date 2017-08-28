package com.minsheng.app.module.appointmentorder;

import java.io.Serializable;

/**
 * 
 * @describe:购物车存储实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @2015-3-22下午11:13:11
 * 
 */
public class SelectObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private int productId;
	private String productName;
	private int productNum;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductAmount() {
		return productNum;
	}

	public void setProductAmount(int productAmount) {
		this.productNum = productAmount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SelectObject [productId=" + productId + ", productName="
				+ productName + ", productAmount=" + productNum + "]";
	}

}
