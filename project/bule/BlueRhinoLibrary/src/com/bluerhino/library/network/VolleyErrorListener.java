package com.bluerhino.library.network;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.ParseError;
import com.bluerhino.library.network.framework.BRResponseError;
import com.bluerhino.library.utils.ToastUtil;
import com.bluerhino.R;

public class VolleyErrorListener implements ErrorListener {

	private Context mContext;
	private Resources mRes;
	private String mCustmErrorMsg;

	public VolleyErrorListener(){
	}
	
	public VolleyErrorListener(Context context) {
		this.mContext = context;
		mRes = mContext.getResources();
	}
	
	public VolleyErrorListener(Context context, String customErrorMsg) {
		this(context);
		mCustmErrorMsg = customErrorMsg;
	}
	
	public void setContext(Context context){
		mContext = context;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		if (null != error) {
			int msgID = 0;
			String msg = "";
			if (error instanceof ServerError) {
				// 服务器错误
				msgID = getServerErrorMessageID();
				msg = getServerErrorMessage();
			} else if (error instanceof TimeoutError) {
				// 超时错误
				msgID = getTimeOutMessageID();
				msg = getTimeOutMessage();
			} else if (error instanceof NetworkError) {
				// 网络连接错误
				msgID = getNetWorkErrorMessageID();
				msg = getNetWorkErrorMessage();
			} else if(error instanceof ParseError){
				//解析错误
				msgID = getParseErrorMessageID();
				msg = getParseErrorMessage();
			}  else if(error instanceof BRResponseError){
				//自定义错误
				msgID = 0;
				msg = error.getMessage();
			} else{
				msgID = getOtherErrorMessageID();
				if(TextUtils.isEmpty(mCustmErrorMsg)){
					msg = getOtherErrorMessage();
				}else {
					msg = mCustmErrorMsg;
				}
			}
			
			if(null == mContext){
				throw new IllegalArgumentException("you must override this method or set Context.");
			}
			
			if (msgID != 0) {
//				Toast.makeText(mContext, msgID, Toast.LENGTH_SHORT).show();
				ToastUtil.showToast(mContext, msgID);
			} else {
//				Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
				ToastUtil.showToast(mContext, msg);
			}
		}
	}

	public String getServerErrorMessage() {
		if(null != mRes){
			return mRes.getString(R.string.servererror); 
		}else{
			//throw new IllegalArgumentException("you must override this method.");
			return null;
		}
	}

	public String getTimeOutMessage() {
		if(null != mRes){
			return mRes.getString(R.string.timeouterror);
		}else{
			// throw new IllegalArgumentException("you must override this method.");
			return null;
		}
	}

	public String getNetWorkErrorMessage() {
		if(null != mRes){
			return mRes.getString(R.string.networkerror);
		}else{
			// throw new IllegalArgumentException("you must override this method.");
			return null;
		}
	}
	
	private String getParseErrorMessage() {
		if(null != mRes){
			return mRes.getString(R.string.parseerror);
		}else{
			// throw new IllegalArgumentException("you must override this method.");
			return null;
		}
	}

	public String getOtherErrorMessage(){
		if(null != mRes){
			return mRes.getString(R.string.othererror);
		}else{
			// throw new IllegalArgumentException("you must override this method.");
			return "出错啦";
		}
	}
	
	public int getServerErrorMessageID() {
		if(null == mRes){
			return 0;
		}else{
			return R.string.servererror;
		}
	}

	public int getTimeOutMessageID() {
		if(null == mRes){
			return 0;
		}else{
			return R.string.timeouterror;
		}
	}

	public int getNetWorkErrorMessageID() {
		if(null == mRes){
			return 0;
		}else{
			return R.string.networkerror;
		}
	}
	
	private int getParseErrorMessageID() {
		if(null == mRes){
			return 0;
		}else{
			return R.string.networkerror;
		}
	}

	public int getOtherErrorMessageID() {
		return 0;
	}
}
