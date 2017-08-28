package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @describe:订单列表数据实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-18下午1:32:09
 * 
 */
public class OrderListEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "AppointOrderEnty [code=" + code + ", msg=" + msg + ", info="
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
		private List<OrderObject> deliveryOrderList;
		private int endCount;
		private String pageToken;

		@Override
		public String toString() {
			return "Infor [deliveryOrderList=" + deliveryOrderList
					+ ", endCount=" + endCount + ", pageToken=" + pageToken
					+ "]";
		}

		public List<OrderObject> getDeliveryOrderList() {
			return deliveryOrderList;
		}

		public void setDeliveryOrderList(List<OrderObject> deliveryOrderList) {
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

		public static class OrderObject implements Serializable {
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
			private int businessId;
			private int payStatus;
			private int addTime;
			private int orderAmount;
			private float orderAmountD;
			private String orderSn;
			private String productNum;
			private int isShowAfresh;
			private String headPicUrl;
			private int intervalTime;
			private int deliveryCheckStatus;
			private String businessName;
			private int payType;
			private int todayTakeFlag;
			private int todayGiveFlag;
			

			public int getPayType() {
				return payType;
			}

			public void setPayType(int payType) {
				this.payType = payType;
			}

			public int getTodayTakeFlag() {
				return todayTakeFlag;
			}

			public void setTodayTakeFlag(int todayTakeFlag) {
				this.todayTakeFlag = todayTakeFlag;
			}

			public int getTodayGiveFlag() {
				return todayGiveFlag;
			}

			public void setTodayGiveFlag(int todayGiveFlag) {
				this.todayGiveFlag = todayGiveFlag;
			}

			public int getDeliveryCheckStatus() {
				return deliveryCheckStatus;
			}

			public void setDeliveryCheckStatus(int deliveryCheckStatus) {
				this.deliveryCheckStatus = deliveryCheckStatus;
			}

			public String getBusinessName() {
				return businessName;
			}

			public void setBusinessName(String businessName) {
				this.businessName = businessName;
			}

			public void setOrderAmountD(float orderAmountD) {
				this.orderAmountD = orderAmountD;
			}

			public int getDelivery_check_status() {
				return deliveryCheckStatus;
			}

			public void setDelivery_check_status(int delivery_check_status) {
				this.deliveryCheckStatus = delivery_check_status;
			}

			public int getIntervalTime() {
				return intervalTime;
			}

			public void setIntervalTime(int intervalTime) {
				this.intervalTime = intervalTime;
			}

			public String getHeadPicUrl() {
				return headPicUrl;
			}

			public void setHeadPicUrl(String headPicUrl) {
				this.headPicUrl = headPicUrl;
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

			public int getAddTime() {
				return addTime;
			}

			public void setAddTime(int addTime) {
				this.addTime = addTime;
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

			public String getOrderSn() {
				return orderSn;
			}

			public void setOrderSn(String orderSn) {
				this.orderSn = orderSn;
			}

			public String getProductNum() {
				return productNum;
			}

			public void setProductNum(String productNum) {
				this.productNum = productNum;
			}

			public int getBusinessId() {
				return businessId;
			}

			public void setBusinessId(int businessId) {
				this.businessId = businessId;
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

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

			@Override
			public String toString() {
				return "OrderObject [orderId=" + orderId + ", consignee="
						+ consignee + ", orderMobile=" + orderMobile
						+ ", address=" + address + ", wdLongitude="
						+ wdLongitude + ", wdLatitude=" + wdLatitude
						+ ", takeDate=" + takeDate + ", takeTime=" + takeTime
						+ ", giveDate=" + giveDate + ", giveTime=" + giveTime
						+ ", washStatus=" + washStatus + ", headPic=" + headPic
						+ ", businessId=" + businessId + ", payStatus="
						+ payStatus + ", addTime=" + addTime + ", orderAmount="
						+ orderAmount + ", orderAmountD=" + orderAmountD
						+ ", orderSn=" + orderSn + ", productNum=" + productNum
						+ ", isShowAfresh=" + isShowAfresh + ", headPicUrl="
						+ headPicUrl + ", intervalTime=" + intervalTime
						+ ", deliveryCheckStatus=" + deliveryCheckStatus
						+ ", businessName=" + businessName + ", payType="
						+ payType + ", todayTakeFlag=" + todayTakeFlag
						+ ", todayGiveFlag=" + todayGiveFlag + "]";
			}

		}

	}
}
