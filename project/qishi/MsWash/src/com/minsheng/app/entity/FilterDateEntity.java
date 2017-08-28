
package com.minsheng.app.entity;

import java.io.Serializable;
import java.util.List;


/**
 *
 * @describe:
 *
 * @author:LiuZhouLiang
 *
 * @time:2015-5-19下午6:21:49
 * 
 */
public class FilterDateEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private Infor info;
	
	@Override
	public String toString() {
		return "FilterDateEntity [code=" + code + ", msg=" + msg + ", info="
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

	public static class Infor implements Serializable{
		private static final long serialVersionUID = 1L;
		private List<Integer> timeDayList;

		public List<Integer> getTimeDayList() {
			return timeDayList;
		}

		public void setTimeDayList(List<Integer> timeDayList) {
			this.timeDayList = timeDayList;
		}

		@Override
		public String toString() {
			return "Infor [timeDayList=" + timeDayList + "]";
		}
		
	}
}

