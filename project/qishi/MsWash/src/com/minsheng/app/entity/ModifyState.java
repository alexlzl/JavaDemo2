package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * @describe:修改状态实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-19上午10:45:54
 * 
 */
public class ModifyState implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "ModifyOrderState [code=" + code + ", msg=" + msg + ", info="
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
		private int washStatus;

		public int getWashStatus() {
			return washStatus;
		}

		public void setWashStatus(int washStatus) {
			this.washStatus = washStatus;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		@Override
		public String toString() {
			return "Infor [washStatus=" + washStatus + "]";
		}

	}
}
