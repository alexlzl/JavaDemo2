package com.minsheng.app.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.util.LogUtil;

/**
 * 
 * 
 * @describe:网络请求工具类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-3下午3:58:02
 * 
 */
public class BasicHttpClient {
	private final static String TAG = "BasicHttpClient";

	private static BasicHttpClient basicHttpClient = null;
	private static Header[] defaultHeaders;
	private AsyncHttpClient asyncHttpClient = null;

	private BasicHttpClient() {

		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setTimeout(60 * 1000);
	}

	public static BasicHttpClient getInstance() {
		if (basicHttpClient == null) {
			basicHttpClient = new BasicHttpClient();
		}
		return basicHttpClient;
	}

	public AsyncHttpClient getAsyncHttpClient() {
		return asyncHttpClient;
	}

	public void setAsyncHttpClient(AsyncHttpClient asyncHttpClient) {
		this.asyncHttpClient = asyncHttpClient;
	}

	/**
	 * 
	 * 
	 * @describe:Get请求，无参数
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午4:21:34
	 * 
	 */
	public void get(Context context, String method,
			ResponseHandlerInterface responseHandler) {
		asyncHttpClient.get(context, MsRequestConfiguration.BASE_URL + method,
				responseHandler);
		LogUtil.d("json", MsRequestConfiguration.BASE_URL + method);
	}

	/**
	 * 
	 * 
	 * @describe:Get请求，有参数
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午4:25:24
	 * 
	 */
	public void get(Context context, RequestParams params, String method,
			ResponseHandlerInterface responseHandler) {
		asyncHttpClient.get(context, MsRequestConfiguration.BASE_URL + method,
				params, responseHandler);
	}

	/**
	 * 
	 * 
	 * @describe:网络get请求(有参),设置header
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午4:25:51
	 * 
	 */
	public void get(Context context, RequestParams params, String method,
			Header[] headers, ResponseHandlerInterface responseHandler) {
		asyncHttpClient.get(context, MsRequestConfiguration.BASE_URL + method,
				headers, params, responseHandler);
	}

	/**
	 * 
	 * 
	 * @describe:网络post请求(HttpEntity参数)
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午4:26:43
	 * 
	 */
	public void post(Context context, HttpEntity entity, String method,
			ResponseHandlerInterface responseHandler) {
		asyncHttpClient.post(context, MsRequestConfiguration.BASE_URL + method,
				entity, MsRequestConfiguration.CONTENT_TYPE, responseHandler);
	}

	/**
	 * 
	 * 
	 * @describe:网络post请求(HttpEntity参数),设置header
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午4:27:12
	 * 
	 */
	public void post(Context context, Header[] headers, HttpEntity entity,
			String method, ResponseHandlerInterface responseHandler) {
		asyncHttpClient.post(context, MsRequestConfiguration.BASE_URL + method,
				headers, entity, MsRequestConfiguration.CONTENT_TYPE,
				responseHandler);
	}

	/**
	 * 
	 * 
	 * @describe:网络post请求(HttpEntity参数),设置header
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午4:27:28
	 * 
	 */
	public void post(Context context, String method, Header[] headers,
			RequestParams params, ResponseHandlerInterface responseHandler) {
		asyncHttpClient.post(context, MsRequestConfiguration.BASE_URL + method,
				headers, params, MsRequestConfiguration.CONTENT_TYPE,
				responseHandler);
	}

	public void post(Context context, RequestParams params, String method,
			ResponseHandlerInterface responseHandler) {
		// String url = MsRequestConfiguration.BASE_URL + method + "token="
		// + RequestUtil.getInstance().getToken() + "&timestamp="
		// + RequestUtil.getInstance().getCurrentTime();
		String url = MsRequestConfiguration.BASE_URL + method;
		asyncHttpClient.post(context, url, params, responseHandler);
		LogUtil.d(TAG, "请求地址" + url+params.toString());
	}
	
	public void postWeather(String url,
			AsyncHttpResponseHandler responseHandler){
		asyncHttpClient.get(url, responseHandler);
		
	}

	/**
	 * 
	 * 
	 * @describe:取消当前页面所有请求
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午4:27:41
	 * 
	 */
	public void cancleRequest(Context context) {
		asyncHttpClient.cancelRequests(context, true);
	}

	/**
	 * 
	 * 
	 * @describe:获取默认header
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午4:27:59
	 * 
	 */
	public Header[] getDefaultHeaders() {
		return defaultHeaders;
	}

}
