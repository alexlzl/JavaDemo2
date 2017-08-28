package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * @describe:优惠券激活
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-26下午5:23:31
 * 
 */
public class AppInfor implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

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

	}
}
