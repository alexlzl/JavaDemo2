package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * @describe:加入购物车，订单实体列表类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-23下午9:25:53
 * 
 */
public class AddShopCarEntityParam implements Serializable {

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

	public int getProductNum() {
		return productNum;
	}

	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
