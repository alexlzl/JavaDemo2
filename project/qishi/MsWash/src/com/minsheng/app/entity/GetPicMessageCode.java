package com.minsheng.app.entity;

import java.io.Serializable;

/**
 * 
 * 
 * @describe:获取验证码图片
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-16下午7:07:21
 * 
 */
public class GetPicMessageCode implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		return "GetVerificationCodeUrl [code=" + code + ", msg=" + msg
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
		private String imgByte;
		private String authCodeKey;

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public String getImgByte() {
			return imgByte;
		}

		public void setImgByte(String imgByte) {
			this.imgByte = imgByte;
		}

		public String getAuthCodeKey() {
			return authCodeKey;
		}

		public void setAuthCodeKey(String authCodeKey) {
			this.authCodeKey = authCodeKey;
		}

		@Override
		public String toString() {
			return "Infor [imgByte=" + imgByte + ", authCodeKey=" + authCodeKey
					+ "]";
		}

	}
}
