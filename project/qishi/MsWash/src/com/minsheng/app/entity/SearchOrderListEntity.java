package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @describe:搜索订单实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-27上午11:37:06
 * 
 */
public class SearchOrderListEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "SearchOrderList [code=" + code + ", msg=" + msg + ", info="
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
		private List<SearchOrderDetail> deliveryOrderList;
		private int endCount;
		private String pageToken;

		@Override
		public String toString() {
			return "Infor [deliveryOrderList=" + deliveryOrderList
					+ ", endCount=" + endCount + ", pageToken=" + pageToken
					+ "]";
		}

		public List<SearchOrderDetail> getDeliveryOrderList() {
			return deliveryOrderList;
		}

		public void setDeliveryOrderList(
				List<SearchOrderDetail> deliveryOrderList) {
			this.deliveryOrderList = deliveryOrderList;
		}

		public int getEndCount() {
			return endCount;
		}

		public void setEndCount(int endCount) {
			this.endCount = endCount;
		}

		public String getPageToken() {
			return pageToken;
		}

		public void setPageToken(String pageToken) {
			this.pageToken = pageToken;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public static class SearchOrderDetail implements Serializable {
			private static final long serialVersionUID = 1L;
			private int orderId;
			private String consignee;
			private String orderMobile;
			private String orderSn;
			private String headPic;
			private String headPicUrl;
			private int washStatus;
			private int payStatus;
			private int isShowAfresh;
			private int businessId;

			public int getBusinessId() {
				return businessId;
			}

			public void setBusinessId(int businessId) {
				this.businessId = businessId;
			}

			public int getIsShowAfresh() {
				return isShowAfresh;
			}

			public void setIsShowAfresh(int isShowAfresh) {
				this.isShowAfresh = isShowAfresh;
			}

			public int getPayStatus() {
				return payStatus;
			}

			public void setPayStatus(int payStatus) {
				this.payStatus = payStatus;
			}

			public int getWashStatus() {
				return washStatus;
			}

			public void setWashStatus(int washStatus) {
				this.washStatus = washStatus;
			}

			public int getOrderId() {
				return orderId;
			}

			public void setOrderId(int orderId) {
				this.orderId = orderId;
			}

			public String getConsignee() {
				return consignee;
			}

			public void setConsignee(String consignee) {
				this.consignee = consignee;
			}

			public String getOrderMobile() {
				return orderMobile;
			}

			public void setOrderMobile(String orderMobile) {
				this.orderMobile = orderMobile;
			}

			public String getOrderSn() {
				return orderSn;
			}

			public void setOrderSn(String orderSn) {
				this.orderSn = orderSn;
			}

			public String getHeadPic() {
				return headPic;
			}

			public void setHeadPic(String headPic) {
				this.headPic = headPic;
			}

			public String getHeadPicUrl() {
				return headPicUrl;
			}

			public void setHeadPicUrl(String headPicUrl) {
				this.headPicUrl = headPicUrl;
			}

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

			@Override
			public String toString() {
				return "SearchOrderDetail [orderId=" + orderId + ", consignee="
						+ consignee + ", orderMobile=" + orderMobile
						+ ", orderSn=" + orderSn + ", headPic=" + headPic
						+ ", headPicUrl=" + headPicUrl + ", washStatus="
						+ washStatus + ", payStatus=" + payStatus
						+ ", isShowAfresh=" + isShowAfresh + ", businessId="
						+ businessId + "]";
			}

		}
	}
}
