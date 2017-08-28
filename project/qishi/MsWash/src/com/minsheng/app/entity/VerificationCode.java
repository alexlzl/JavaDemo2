package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * 
 * @describe:发送手机验证码对应实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-16下午6:21:33
 * 
 */
public class VerificationCode implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "VerificationCode [code=" + code + ", msg=" + msg + ", info="
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

	public static class Infor implements Serializable {

		private static final long serialVersionUID = 1L;
		private String authCode;

		public String getAuthCode() {
			return authCode;
		}

		public void setAuthCode(String authCode) {
			this.authCode = authCode;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		@Override
		public String toString() {
			return "Infor [authCode=" + authCode + "]";
		}

	}
}
