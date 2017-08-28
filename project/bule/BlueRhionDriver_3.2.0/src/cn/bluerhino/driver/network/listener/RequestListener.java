package cn.bluerhino.driver.network.listener;

import org.json.JSONObject;

import com.google.gson.JsonObject;

/**
 * 
 * @author sunliang
 *
 */
public interface RequestListener<T> {
	public static final String ERROR_WITHOUT_NETWORK = "无网络";
	public static final String ERROR_JSON_PARSE = "返回错误（不是json）";
	public static final String ERROR_TIMEOUT = "连接网络超时";
	public static final String ERROR_IO = "上传失败，请检查网络";
	public static final String ERROR_OTHER = "其他错误";
	public static final int OTHER_ERROR = 0;
	public static final int IO_ERROR = 1;
	public static final int TIMEOUT_ERROR = 2;
	public static final int WITHOUT_NETWORK_ERROR = 3;
	public static final int JSON_ERROR = 4;

	public void onResponse(String result);
}
