package com.bluerhino.library.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.SystemClock;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bluerhino.R;
import com.bluerhino.library.network.BRURL;
import com.bluerhino.library.network.VolleyErrorListener;

public class ServerTimeSynchronization {
	
	private static final String TAG = ServerTimeSynchronization.class.getSimpleName();
		
	public interface SyncListener{
		public void sync(String serverTimeValue,long serverTime, long currentRealTime);		
	}
	
	private Context mContext;
	private RequestQueue mRequestQueue;
	
	public ServerTimeSynchronization(Context context, RequestQueue requestQueue){
		mContext = context;
		mRequestQueue = requestQueue;
	}

	public void rquestSyncServerTime(final SyncListener listener){
		if(null == listener){			
			return;
		}			
		
    	Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
    		
			@Override
			public void onResponse(JSONObject json) {
				if (json.has("data")){
			    	JSONObject jsonData;
					try {
						jsonData = json.getJSONObject("data");
						String serverTimeValue = "";
						long serverTime = 0, realTime = 0;
						
						if(jsonData.has("timestamp")){				    	
							serverTime = jsonData.getLong("timestamp");
							realTime = SystemClock.elapsedRealtime();
				    	}
						
						if(jsonData.has("time")){
							serverTimeValue = jsonData.getString("time");
						}
						listener.sync(serverTimeValue, serverTime, realTime);			    						    		
					} catch (JSONException e) {
						e.printStackTrace();
					}				    	
			    }
			}
		}; 		
		
		VolleyErrorListener errorListener = new VolleyErrorListener(mContext){

			@Override
            public int getTimeOutMessageID() {
	            return R.string.timesynchronization_timeout;
            }
		};
		
		JsonObjectRequest jsonRequest = new JsonObjectRequest(
				BRURL.ACTION_GET_CURRTEN_TIME, 
				null, 
				successListener,
				errorListener);
		//超时20s,重试连接1次
		jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
		jsonRequest.setTag(TAG);
		mRequestQueue.add(jsonRequest);	
    }
}
