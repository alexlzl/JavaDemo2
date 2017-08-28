package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * @describe:验证码提交返回数据实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-12-3上午9:55:08
 * 
 */
public class VerificationCodeSubmit implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "VerificationCodeSubmit [code=" + code + ", msg=" + msg
				+ ", info=" + info + "]";
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

	}
}
