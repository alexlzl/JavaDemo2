
package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @describe:
 *
 * @author:LiuZhouLiang
 *
 * @time:2015-3-28下午1:04:33
 * 
 */
public class UpdateLocation implements Serializable{
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
            private String wdLongitude;
            private String wdLatitude;
            private String wdPic;
            
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

			public String getWdPic() {
				return wdPic;
			}

			public void setWdPic(String wdPic) {
				this.wdPic = wdPic;
			}

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
						+ wdPicUrl + ", serviceCyList=" + serviceCyList
						+ ", wdLongitude=" + wdLongitude + ", wdLatitude="
						+ wdLatitude + ", wdPic=" + wdPic + "]";
			}

		}
	}
}

