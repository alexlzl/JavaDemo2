package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @describe:订单评价实体数据类
 * 
 * @author:LiuZhouLiang
 * 
 * @2015-3-22下午10:35:42
 * 
 */
public class CourierEvaluate implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "CourierEvaluate [code=" + code + ", msg=" + msg + ", info="
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
		private List<EvaluateDetail> evaluateList;
		private int endCount;
		private String pageToken;

		@Override
		public String toString() {
			return "Infor [evaluateList=" + evaluateList + ", endCount="
					+ endCount + ", pageToken=" + pageToken + "]";
		}

		public List<EvaluateDetail> getEvaluateList() {
			return evaluateList;
		}

		public void setEvaluateList(List<EvaluateDetail> evaluateList) {
			this.evaluateList = evaluateList;
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

		public static class EvaluateDetail implements Serializable {
			private static final long serialVersionUID = 1L;
			private int addTime;
			private String dateString;
			private int evaluateId;
			private int orderId;
			private int washQuality;
			private int serviceQuality;
			private int timeQuality;
			private String evaluateContent;
			private String orderSn;

			public int getAddTime() {
				return addTime;
			}

			public void setAddTime(int addTime) {
				this.addTime = addTime;
			}

			public String getDateString() {
				return dateString;
			}

			public void setDateString(String dateString) {
				this.dateString = dateString;
			}

			public int getEvaluateId() {
				return evaluateId;
			}

			public void setEvaluateId(int evaluateId) {
				this.evaluateId = evaluateId;
			}

			public int getOrderId() {
				return orderId;
			}

			public void setOrderId(int orderId) {
				this.orderId = orderId;
			}

			public int getWashQuality() {
				return washQuality;
			}

			public void setWashQuality(int washQuality) {
				this.washQuality = washQuality;
			}

			public int getServiceQuality() {
				return serviceQuality;
			}

			public void setServiceQuality(int serviceQuality) {
				this.serviceQuality = serviceQuality;
			}

			public int getTimeQuality() {
				return timeQuality;
			}

			public void setTimeQuality(int timeQuality) {
				this.timeQuality = timeQuality;
			}

			public String getEvaluateContent() {
				return evaluateContent;
			}

			public void setEvaluateContent(String evaluateContent) {
				this.evaluateContent = evaluateContent;
			}

			public String getOrderSn() {
				return orderSn;
			}

			public void setOrderSn(String orderSn) {
				this.orderSn = orderSn;
			}

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

			@Override
			public String toString() {
				return "EevaluateDetail [addTime=" + addTime + ", dateString="
						+ dateString + ", evaluateId=" + evaluateId
						+ ", orderId=" + orderId + ", washQuality="
						+ washQuality + ", serviceQuality=" + serviceQuality
						+ ", timeQuality=" + timeQuality + ", evaluateContent="
						+ evaluateContent + ", orderSn=" + orderSn + "]";
			}

		}

	}
}
