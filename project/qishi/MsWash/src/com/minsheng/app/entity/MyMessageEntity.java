package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @describe:个人消息实体类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-17下午8:24:12
 * 
 */
public class MyMessageEntity implements Serializable {
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

	@Override
	public String toString() {
		return "MyMessage [code=" + code + ", msg=" + msg + ", info=" + info
				+ "]";
	}

	public static class Infor implements Serializable {
		private static final long serialVersionUID = 1L;
		private List<MessageObj> deliverMsgList;
		private String pageToken;
		private int endCount;

		public int getEndCount() {
			return endCount;
		}

		public void setEndCount(int endCount) {
			this.endCount = endCount;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		@Override
		public String toString() {
			return "Infor [deliverMsgList=" + deliverMsgList + ", pageToken="
					+ pageToken + ", endCount=" + endCount + "]";
		}

		public List<MessageObj> getDeliverMsgList() {
			return deliverMsgList;
		}

		public void setDeliverMsgList(List<MessageObj> deliverMsgList) {
			this.deliverMsgList = deliverMsgList;
		}

		public String getPageToken() {
			return pageToken;
		}

		public void setPageToken(String pageToken) {
			this.pageToken = pageToken;
		}

		public static class MessageObj implements Serializable {
			private static final long serialVersionUID = 1L;
			private int msgId;
			private String msgTitle;
			private String msgContent;
			private int addTime;
			private int isRead;

			public int getMsgId() {
				return msgId;
			}

			public void setMsgId(int msgId) {
				this.msgId = msgId;
			}

			public String getMsgTitle() {
				return msgTitle;
			}

			public void setMsgTitle(String msgTitle) {
				this.msgTitle = msgTitle;
			}

			public String getMsgContent() {
				return msgContent;
			}

			public void setMsgContent(String msgContent) {
				this.msgContent = msgContent;
			}

			public int getAddTime() {
				return addTime;
			}

			public void setAddTime(int addTime) {
				this.addTime = addTime;
			}

			public int getIsRead() {
				return isRead;
			}

			public void setIsRead(int isRead) {
				this.isRead = isRead;
			}

			@Override
			public String toString() {
				return "MessageObj [msgId=" + msgId + ", msgTitle=" + msgTitle
						+ ", msgContent=" + msgContent + ", addTime=" + addTime
						+ ", isRead=" + isRead + "]";
			}

		}
	}
}
