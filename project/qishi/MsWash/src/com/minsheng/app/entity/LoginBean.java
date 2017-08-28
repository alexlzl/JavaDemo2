package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @describe:登陆信息实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-15下午2:57:55
 * 
 */
public class LoginBean implements Serializable {
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

	@Override
	public String toString() {
		return "LoginBean [code=" + code + ", msg=" + msg + ", info=" + info
				+ "]";
	}

	public static class Infor implements Serializable {
		private static final long serialVersionUID = 1L;
		private String loginToken;
		private UserInfor deliveryUser;

		public UserInfor getDeliveryUser() {
			return deliveryUser;
		}

		public void setDeliveryUser(UserInfor deliveryUser) {
			this.deliveryUser = deliveryUser;
		}

		public String getLoginToken() {
			return loginToken;
		}

		public void setLoginToken(String loginToken) {
			this.loginToken = loginToken;
		}

		@Override
		public String toString() {
			return "Infor [loginToken=" + loginToken + ", deliveryUser="
					+ deliveryUser + "]";
		}

		public static class UserInfor implements Serializable {

			private static final long serialVersionUID = 1L;
			private int wdId;
			private String wdName;
			private String wdNumber;
			private String wdMobile;
			private String wdPassword;
			private String wdPicUrl;
			private List<String> serviceCyList;

			public int getWdId() {
				return wdId;
			}

			public void setWdId(int wdId) {
				this.wdId = wdId;
			}

			public String getWdName() {
				return wdName;
			}

			public void setWdName(String wdName) {
				this.wdName = wdName;
			}

			public String getWdNumber() {
				return wdNumber;
			}

			public void setWdNumber(String wdNumber) {
				this.wdNumber = wdNumber;
			}

			public String getWdMobile() {
				return wdMobile;
			}

			public void setWdMobile(String wdMobile) {
				this.wdMobile = wdMobile;
			}

			public String getWdPassword() {
				return wdPassword;
			}

			public void setWdPassword(String wdPassword) {
				this.wdPassword = wdPassword;
			}

			public String getWdPicUrl() {
				return wdPicUrl;
			}

			public void setWdPicUrl(String wdPicUrl) {
				this.wdPicUrl = wdPicUrl;
			}

			public List<String> getServiceCyList() {
				return serviceCyList;
			}

			public void setServiceCyList(List<String> serviceCyList) {
				this.serviceCyList = serviceCyList;
			}

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

			@Override
			public String toString() {
				return "UserInfor [wdId=" + wdId + ", wdName=" + wdName
						+ ", wdNumber=" + wdNumber + ", wdMobile=" + wdMobile
						+ ", wdPassword=" + wdPassword + ", wdPicUrl="
						+ wdPicUrl + ", serviceCyList=" + serviceCyList + "]";
			}

		}
	}

}
