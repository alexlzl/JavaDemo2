package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * @describe:更新购物车参数实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-24上午9:59:34
 * 
 */
public class UpdateShopcarParam implements Serializable {
	private static final long serialVersionUID = 1L;
	private int ordeCartId;
	private String oneCode;
	private String remarkTag;
	private String remark;
	private int isFirst;

	public int getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(int isFirst) {
		this.isFirst = isFirst;
	}

	public int getOrdeCartId() {
		return ordeCartId;
	}

	public void setOrdeCartId(int ordeCartId) {
		this.ordeCartId = ordeCartId;
	}

	public String getOneCode() {
		return oneCode;
	}

	public void setOneCode(String oneCode) {
		this.oneCode = oneCode;
	}

	public String getRemarkTag() {
		return remarkTag;
	}

	public void setRemarkTag(String remarkTag) {
		this.remarkTag = remarkTag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UpdateShopcarParam [ordeCartId=" + ordeCartId + ", oneCode="
				+ oneCode + ", remarkTag=" + remarkTag + ", remark=" + remark
				+ ", isFirst=" + isFirst + "]";
	}

}
