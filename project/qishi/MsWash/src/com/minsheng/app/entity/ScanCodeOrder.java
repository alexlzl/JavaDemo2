package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * @describe:扫描订单数据返回实体对象
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-5-13下午4:55:46
 * 
 */
public class ScanCodeOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "ScanCodeOrder [code=" + code + ", msg=" + msg + ", info="
				+ info + "]";
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Infor getInfo() {
		return info;
	}

	public void setInfo(Infor info) {
		this.info = info;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static class Infor implements Serializable {
		private static final long serialVersionUID = 1L;
		private ScanOrderList orderList;

		@Override
		public String toString() {
			return "Infor [orderList=" + orderList + "]";
		}

		public ScanOrderList getOrderList() {
			return orderList;
		}

		public void setOrderList(ScanOrderList orderList) {
			this.orderList = orderList;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public static class ScanOrderList implements Serializable {

			private static final long serialVersionUID = 1L;
			private int orderId;
			private int washStatus;
			private int payStatus;
			private int isShowAfresh;
			private int businessId;

			public int getOrderId() {
				return orderId;
			}

			public void setOrderId(int orderId) {
				this.orderId = orderId;
			}

			public int getWashStatus() {
				return washStatus;
			}

			public void setWashStatus(int washStatus) {
				this.washStatus = washStatus;
			}

			public int getPayStatus() {
				return payStatus;
			}

			public void setPayStatus(int payStatus) {
				this.payStatus = payStatus;
			}

			public int getIsShowAfresh() {
				return isShowAfresh;
			}

			public void setIsShowAfresh(int isShowAfresh) {
				this.isShowAfresh = isShowAfresh;
			}

			public int getBusinessId() {
				return businessId;
			}

			public void setBusinessId(int businessId) {
				this.businessId = businessId;
			}

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

			@Override
			public String toString() {
				return "ScanOrderList [orderId=" + orderId + ", washStatus="
						+ washStatus + ", payStatus=" + payStatus
						+ ", isShowAfresh=" + isShowAfresh + ", businessId="
						+ businessId + "]";
			}

		}

	}
}
