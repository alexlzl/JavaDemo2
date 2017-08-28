package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @describe:订单详情实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-19上午10:26:36
 * 
 */
public class OrderDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "OrderDetail [code=" + code + ", msg=" + msg + ", info=" + info
				+ "]";
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

	public static class Infor implements Serializable{
		private static final long serialVersionUID = 1L;
		private Detail orderDetail;

		public Detail getOrderDetail() {
			return orderDetail;
		}

		public void setOrderDetail(Detail orderDetail) {
			this.orderDetail = orderDetail;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public static class Detail implements Serializable {

			private static final long serialVersionUID = 1L;
			private int orderId;
			private String consignee;
			private String orderMobile;
			private String address;
			private String wdLongitude;
			private String wdLatitude;
			private int takeDate;
			private String takeTime;
			private int giveDate;
			private String giveTime;
			private int washStatus;
			private String headPic;
			private int orderAmount;
			private float orderAmountD;
			private String washRemark;
			private String orderSn;
			private int businessId;
			private String headPicUrl;
			private List<OrderList> deliveryProductDetails;
			private int applyStatus;
			private String businessName;
			private String washOrderNumber;
			
			public String getBusinessName() {
				return businessName;
			}

			public void setBusinessName(String businessName) {
				this.businessName = businessName;
			}

			public String getWashOrderNumber() {
				return washOrderNumber;
			}

			public void setWashOrderNumber(String washOrderNumber) {
				this.washOrderNumber = washOrderNumber;
			}

			public int getApplyStatus() {
				return applyStatus;
			}

			public void setApplyStatus(int applyStatus) {
				this.applyStatus = applyStatus;
			}

			public String getHeadPicUrl() {
				return headPicUrl;
			}

			public void setHeadPicUrl(String headPicUrl) {
				this.headPicUrl = headPicUrl;
			}

		

			public int getBusinessId() {
				return businessId;
			}

			public void setBusinessId(int businessId) {
				this.businessId = businessId;
			}

			public void setOrderAmountD(float orderAmountD) {
				this.orderAmountD = orderAmountD;
			}

			@Override
			public String toString() {
				return "Detail [orderId=" + orderId + ", consignee="
						+ consignee + ", orderMobile=" + orderMobile
						+ ", address=" + address + ", wdLongitude="
						+ wdLongitude + ", wdLatitude=" + wdLatitude
						+ ", takeDate=" + takeDate + ", takeTime=" + takeTime
						+ ", giveDate=" + giveDate + ", giveTime=" + giveTime
						+ ", washStatus=" + washStatus + ", headPic=" + headPic
						+ ", orderAmount=" + orderAmount + ", orderAmountD="
						+ orderAmountD + ", washRemark=" + washRemark
						+ ", orderSn=" + orderSn + ", businessId=" + businessId
						+ ", headPicUrl=" + headPicUrl
						+ ", deliveryProductDetails=" + deliveryProductDetails
						+ ", applyStatus=" + applyStatus + ", businessName="
						+ businessName + ", washOrderNumber=" + washOrderNumber
						+ "]";
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

			public String getAddress() {
				return address;
			}

			public void setAddress(String address) {
				this.address = address;
			}

			public String getWdLongitude() {
				return wdLongitude;
			}

			public void setWdLongitude(String wdLongitude) {
				this.wdLongitude = wdLongitude;
			}

			public String getWdLatitude() {
				return wdLatitude;
			}

			public void setWdLatitude(String wdLatitude) {
				this.wdLatitude = wdLatitude;
			}

			public int getTakeDate() {
				return takeDate;
			}

			public void setTakeDate(int takeDate) {
				this.takeDate = takeDate;
			}

			public String getTakeTime() {
				return takeTime;
			}

			public void setTakeTime(String takeTime) {
				this.takeTime = takeTime;
			}

			public int getGiveDate() {
				return giveDate;
			}

			public void setGiveDate(int giveDate) {
				this.giveDate = giveDate;
			}

			public String getGiveTime() {
				return giveTime;
			}

			public void setGiveTime(String giveTime) {
				this.giveTime = giveTime;
			}

			public int getWashStatus() {
				return washStatus;
			}

			public void setWashStatus(int washStatus) {
				this.washStatus = washStatus;
			}

			public String getHeadPic() {
				return headPic;
			}

			public void setHeadPic(String headPic) {
				this.headPic = headPic;
			}

			public int getOrderAmount() {
				return orderAmount;
			}

			public void setOrderAmount(int orderAmount) {
				this.orderAmount = orderAmount;
			}

			public float getOrderAmountD() {
				return orderAmountD;
			}

			public void setOrderAmountD(int orderAmountD) {
				this.orderAmountD = orderAmountD;
			}

			public String getWashRemark() {
				return washRemark;
			}

			public void setWashRemark(String washRemark) {
				this.washRemark = washRemark;
			}

			public String getOrderSn() {
				return orderSn;
			}

			public void setOrderSn(String orderSn) {
				this.orderSn = orderSn;
			}

			public List<OrderList> getDeliveryProductDetails() {
				return deliveryProductDetails;
			}

			public void setDeliveryProductDetails(
					List<OrderList> deliveryProductDetails) {
				this.deliveryProductDetails = deliveryProductDetails;
			}

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

			public static class OrderList implements Serializable {

				private static final long serialVersionUID = 1L;
				private int orderProductId;
				private String productName;
				private int sellPrice;
				private float sellPriceD;
				private String oneCode;
				private String remark;
				private String productTag;
				private int isToWash;

				public int getOrderProductId() {
					return orderProductId;
				}

				public void setOrderProductId(int orderProductId) {
					this.orderProductId = orderProductId;
				}

				public String getProductName() {
					return productName;
				}

				public void setProductName(String productName) {
					this.productName = productName;
				}

				public int getSellPrice() {
					return sellPrice;
				}

				public void setSellPrice(int sellPrice) {
					this.sellPrice = sellPrice;
				}

				public float getSellPriceD() {
					return sellPriceD;
				}

				public void setSellPriceD(float sellPriceD) {
					this.sellPriceD = sellPriceD;
				}

				public String getOneCode() {
					return oneCode;
				}

				public void setOneCode(String oneCode) {
					this.oneCode = oneCode;
				}

				public String getRemark() {
					return remark;
				}

				public void setRemark(String remark) {
					this.remark = remark;
				}

				public String getProductTag() {
					return productTag;
				}

				public void setProductTag(String productTag) {
					this.productTag = productTag;
				}

				public int getIsToWash() {
					return isToWash;
				}

				public void setIsToWash(int isToWash) {
					this.isToWash = isToWash;
				}

				public static long getSerialversionuid() {
					return serialVersionUID;
				}

				@Override
				public String toString() {
					return "OrderList [orderProductId=" + orderProductId
							+ ", productName=" + productName + ", sellPrice="
							+ sellPrice + ", sellPriceD=" + sellPriceD
							+ ", oneCode=" + oneCode + ", remark=" + remark
							+ ", productTag=" + productTag + ", isToWash="
							+ isToWash + "]";
				}

			}

		}
	}

}
