package cn.bluerhino.driver.model.datasource;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.OrderInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.VolleySucceedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RequestOrderTask implements Callback{
	
	public static final String REQUEST_TAG = RequestOrderTask.class.getSimpleName();
	
	public static final String PARAMS_DID = "did";
	
	public static final String PARAMS_NOWPAGE = "nowPage";
	
	public static final String PARAMS_TOTALPAGE = "totalPage";

	public static final int MSG_COMPLETE = 1;
	
	public static final int MSG_ERROR = 2;
	
	private static final int ORDER_RQUEST_TIMEOUT = 10000;
	
	private onResponseListener mListener;
	
	public interface onResponseListener {
		void onRequestComplete(Message msg);
		void onRequestTimeOut();
		void onRequestError();
	}
	
	private String mUrl;
	private HashMap<String,String> mRequestParams;
	private StringRequest mOrderRequest;
	private Listener<String> mSuccessListener;
	private ErrorListener mErrorListener;
	
	private Context mContext;
	private Resources mRes;
	private Handler mHandler;
	
	private int mNowPage;
	private int mTotalPage;

	public RequestOrderTask(String url, HashMap<String,String> requestParams) {
		mUrl = url;
		mRequestParams = requestParams;
		mNowPage = Integer.parseInt(mRequestParams.get(PARAMS_NOWPAGE));
		mTotalPage = Integer.parseInt(mRequestParams.get(PARAMS_TOTALPAGE));
		mContext = ApplicationController.getInstance().getBaseContext();
		mRes = mContext.getResources();
		mHandler = new Handler(this);
		initVollry();
	}
	
	private void initVollry(){
		mSuccessListener = new VolleySucceedListener() {
			@Override
            public void onResponse(String response) {
	        	Message msg = Message.obtain();
	        	String errorMsg = mRes.getString(R.string.update_order_exception);
	        	try {
	        		JSONObject rootJson = new JSONObject(response);
	        		System.err.println("rootjsoon" + rootJson.toString());
	        		if(rootJson.has("ret")){
	        			if(rootJson.getInt("ret") == 0){
	        				errorMsg = rootJson.getString("msg");
	        				msg.what = MSG_ERROR;
	        				msg.obj = errorMsg;
	        			}else{
	        				if(rootJson.has("data")){
	    	        			JSONObject dataObject = rootJson.getJSONObject("data");
	    	        			
	    	        			if(dataObject.has("orderInfo")){
	    	        				Gson gson = new Gson();
	    	        				Type t = new TypeToken<List<OrderInfo>>(){}.getType();
	    	        				List<OrderInfo> orderInfoExs = gson.fromJson(
	    	        						dataObject.getString("orderInfo"), t);
	    	        				msg.what = MSG_COMPLETE;
	    	        				msg.obj = orderInfoExs;
	    	        			}else{
	    	        				msg.what = MSG_ERROR;
	    	        				msg.obj = errorMsg;
	    	        			}
	    	        			
	    	        			if(dataObject.has("nowPage")){
	    	        				mNowPage = dataObject.getInt("nowPage");
	    	        				msg.arg1 = mNowPage;
	    	        			}else{
	    	        				msg.what = MSG_ERROR;
	    	        				msg.obj = errorMsg;
	    	        			}
	    	        			
	    	        			if(dataObject.has("totalPage")){
	    	        				mTotalPage = dataObject.getInt("totalPage");
	    	        				msg.arg2 = mTotalPage;
	    	        			}else{
	    	        				msg.what = MSG_ERROR;
	    	        				msg.obj = errorMsg;
	    	        			}
	    	        		}
	        			}
	        		}
	        		else{
	        			//解析data失败
	        			msg.what = MSG_ERROR;
	        			msg.obj = errorMsg;
	        		}
	        	} catch (JSONException e) {
	        		e.printStackTrace();
	        		msg.what = MSG_ERROR;
	        		String serverErrorMsg = mRes.getString(R.string.server_unknow_error);
	        		msg.obj = serverErrorMsg;
	        	}finally{
	        		notifyChanged(msg);
	        	}
            }
		};
		
		mErrorListener = new VolleyErrorListener(ApplicationController.getInstance()
		        .getBaseContext()) {
			@Override
            public void onErrorResponse(VolleyError error) {
				super.onErrorResponse(error);
				Message msg = Message.obtain();
		        msg.what = MSG_ERROR;
		        msg.obj = error.getMessage();
		        notifyChanged(msg);
            }
		};

		mOrderRequest = new StringRequest(Method.POST, mUrl, mSuccessListener, mErrorListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return mRequestParams;
			}
		};

		mOrderRequest.setRetryPolicy(new DefaultRetryPolicy(ORDER_RQUEST_TIMEOUT, 1,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	public void setRequestParams(HashMap<String,String> requestParams){
		mRequestParams = requestParams;
		mNowPage = Integer.parseInt(mRequestParams.get(PARAMS_NOWPAGE));
		mTotalPage = Integer.parseInt(mRequestParams.get(PARAMS_TOTALPAGE));
	}

	public void execute(){
		if (mNowPage <= mTotalPage) {
			Log.i(REQUEST_TAG, "now:" + mRequestParams.get(PARAMS_NOWPAGE) + "  total:"
			        + mRequestParams.get(PARAMS_TOTALPAGE));
			ApplicationController.getInstance().addToRequestQueue(mOrderRequest, REQUEST_TAG);
		}else{
			Message msg = Message.obtain();
			msg.what = MSG_COMPLETE;
			msg.obj = null;
			msg.arg1 = --mNowPage;
			msg.arg2 = mTotalPage;
			notifyChanged(msg);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case MSG_COMPLETE:
				if (null != mListener) {
					mListener.onRequestComplete(msg);
				}
				break;
			case MSG_ERROR:
				if (null != mListener) {
					String errorMsg = (String) msg.obj;
					if(null != errorMsg){
						Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
					}
					mListener.onRequestError();
				}
				break;
			default:
				break;
		}
		return true;
	}

	private void notifyChanged(Message msg){
		mHandler.sendMessage(msg);
	}
	
	public void setOnResponseListener(onResponseListener responseListener){
		mListener = responseListener;
	}
}
