package com.minsheng.app.http;

import com.minsheng.app.util.Md5Util;
import com.minsheng.app.util.TimeUtil;

/**
 * 
 * @describe:
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-13下午4:38:11
 * 
 */
public class RequestUtil {
	private static final String TOKEN_PARAM = "1c169e98d753988347394b80f95e860c";
	private static RequestUtil requestObj;

	private RequestUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public synchronized static RequestUtil getInstance() {
		if (requestObj == null) {
			requestObj = new RequestUtil();
		}
		return requestObj;
	}

	/**
	 * 
	 * 
	 * @describe:获取TOKEN信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-13下午4:56:40
	 * 
	 */
	public String getToken() {

		String token = null;
		token = Md5Util.getMd5(TOKEN_PARAM + TimeUtil.getCurrentSecond());
		return token;

	}

	/**
	 * 
	 * 
	 * @describe:获取当前时间戳
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-13下午4:56:19
	 * 
	 */

	public String getCurrentTime() {
		String time = null;
		time = TimeUtil.getCurrentSecond();
		return time;
	}

}
