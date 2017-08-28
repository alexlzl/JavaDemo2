package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * @describe:首页数据实体
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-17下午5:36:57
 * 
 */
public class Homepage implements Serializable {

	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "Homepage [code=" + code + ", msg=" + msg + ", info=" + info
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

	public static class Infor implements Serializable {
		private static final long serialVersionUID = 1L;
		private int carCapacity;
		private int noCheckGiveClothNum;
		private int giveOrderNum;
		private int getOrderNum;
		private int noCheckGiveNum;
		private int noCheckFinishNum;
		private int makeOrderNum;
		private int giveClothNum;
		private int noCheckMsgNum;
		private int wdStatus;
		private int noCheckRepeatNum;
		private int noCheckGetNum;
		private int repeatOrderNum;
		private int noCheckMakeNum;
		private int finishOrderNum;

		public int getCarCapacity() {
			return carCapacity;
		}

		public void setCarCapacity(int carCapacity) {
			this.carCapacity = carCapacity;
		}

		public int getNoCheckGiveClothNum() {
			return noCheckGiveClothNum;
		}

		public void setNoCheckGiveClothNum(int noCheckGiveClothNum) {
			this.noCheckGiveClothNum = noCheckGiveClothNum;
		}

		public int getGiveOrderNum() {
			return giveOrderNum;
		}

		public void setGiveOrderNum(int giveOrderNum) {
			this.giveOrderNum = giveOrderNum;
		}

		public int getGetOrderNum() {
			return getOrderNum;
		}

		public void setGetOrderNum(int getOrderNum) {
			this.getOrderNum = getOrderNum;
		}

		public int getNoCheckGiveNum() {
			return noCheckGiveNum;
		}

		public void setNoCheckGiveNum(int noCheckGiveNum) {
			this.noCheckGiveNum = noCheckGiveNum;
		}

		public int getNoCheckFinishNum() {
			return noCheckFinishNum;
		}

		public void setNoCheckFinishNum(int noCheckFinishNum) {
			this.noCheckFinishNum = noCheckFinishNum;
		}

		public int getMakeOrderNum() {
			return makeOrderNum;
		}

		public void setMakeOrderNum(int makeOrderNum) {
			this.makeOrderNum = makeOrderNum;
		}

		public int getGiveClothNum() {
			return giveClothNum;
		}

		public void setGiveClothNum(int giveClothNum) {
			this.giveClothNum = giveClothNum;
		}

		public int getNoCheckMsgNum() {
			return noCheckMsgNum;
		}

		public void setNoCheckMsgNum(int noCheckMsgNum) {
			this.noCheckMsgNum = noCheckMsgNum;
		}

		public int getWdStatus() {
			return wdStatus;
		}

		public void setWdStatus(int wdStatus) {
			this.wdStatus = wdStatus;
		}

		public int getNoCheckRepeatNum() {
			return noCheckRepeatNum;
		}

		public void setNoCheckRepeatNum(int noCheckRepeatNum) {
			this.noCheckRepeatNum = noCheckRepeatNum;
		}

		public int getNoCheckGetNum() {
			return noCheckGetNum;
		}

		public void setNoCheckGetNum(int noCheckGetNum) {
			this.noCheckGetNum = noCheckGetNum;
		}

		public int getRepeatOrderNum() {
			return repeatOrderNum;
		}

		public void setRepeatOrderNum(int repeatOrderNum) {
			this.repeatOrderNum = repeatOrderNum;
		}

		public int getNoCheckMakeNum() {
			return noCheckMakeNum;
		}

		public void setNoCheckMakeNum(int noCheckMakeNum) {
			this.noCheckMakeNum = noCheckMakeNum;
		}

		public int getFinishOrderNum() {
			return finishOrderNum;
		}

		public void setFinishOrderNum(int finishOrderNum) {
			this.finishOrderNum = finishOrderNum;
		}

		@Override
		public String toString() {
			return "Infor [carCapacity=" + carCapacity
					+ ", noCheckGiveClothNum=" + noCheckGiveClothNum
					+ ", giveOrderNum=" + giveOrderNum + ", getOrderNum="
					+ getOrderNum + ", noCheckGiveNum=" + noCheckGiveNum
					+ ", noCheckFinishNum=" + noCheckFinishNum
					+ ", makeOrderNum=" + makeOrderNum + ", giveClothNum="
					+ giveClothNum + ", noCheckMsgNum=" + noCheckMsgNum
					+ ", wdStatus=" + wdStatus + ", noCheckRepeatNum="
					+ noCheckRepeatNum + ", noCheckGetNum=" + noCheckGetNum
					+ ", repeatOrderNum=" + repeatOrderNum
					+ ", noCheckMakeNum=" + noCheckMakeNum
					+ ", finishOrderNum=" + finishOrderNum + "]";
		}

	}
}
