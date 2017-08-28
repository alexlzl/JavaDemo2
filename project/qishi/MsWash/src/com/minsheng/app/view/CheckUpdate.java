package com.minsheng.app.view;

import java.io.Serializable;

/**
 * 
 * @describe:
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-1-8下午5:01:50
 * 
 */
public class CheckUpdate implements Serializable {

	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
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
		private APPVersion newversion;

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return super.toString();
		}

		public APPVersion getNewversion() {
			return newversion;
		}

		public void setNewversion(APPVersion newversion) {
			this.newversion = newversion;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public static class APPVersion implements Serializable {

			private static final long serialVersionUID = 1L;

			@Override
			public String toString() {
				return "APPVersion [id=" + id + ", deviceFlag=" + deviceFlag
						+ ", addTime=" + addTime + ", downUrl=" + downUrl
						+ ", version=" + version + ", lowVersion=" + lowVersion
						+ ", forceCueWords=" + forceCueWords + ", cueWords="
						+ cueWords + ", versionName=" + versionName + "]";
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getDeviceFlag() {
				return deviceFlag;
			}

			public void setDeviceFlag(String deviceFlag) {
				this.deviceFlag = deviceFlag;
			}

			public int getAddTime() {
				return addTime;
			}

			public void setAddTime(int addTime) {
				this.addTime = addTime;
			}

			public String getDownUrl() {
				return downUrl;
			}

			public void setDownUrl(String downUrl) {
				this.downUrl = downUrl;
			}

			public int getVersion() {
				return version;
			}

			public void setVersion(int version) {
				this.version = version;
			}

			public int getLowVersion() {
				return lowVersion;
			}

			public void setLowVersion(int lowVersion) {
				this.lowVersion = lowVersion;
			}

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

			public String getForceCueWords() {
				return forceCueWords;
			}

			public void setForceCueWords(String forceCueWords) {
				this.forceCueWords = forceCueWords;
			}

			public String getCueWords() {
				return cueWords;
			}

			public void setCueWords(String cueWords) {
				this.cueWords = cueWords;
			}

			private int id;
			private String deviceFlag;
			private int addTime;
			private String downUrl;
			private int version;
			private int lowVersion;
			private String forceCueWords;
			private String cueWords;
			private String versionName;

			public String getVersionName() {
				return versionName;
			}

			public void setVersionName(String versionName) {
				this.versionName = versionName;
			}

		}

	}
}
