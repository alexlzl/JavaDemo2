package cn.bluerhino.driver.utils;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.network.SaveDriverErrorRequest;

public class UploadErrorMessage {
	Context mContext;
	RequestQueue mRequestQueue;
	BRRequestParams params;
	int errorCode;
	String errorMessage;
	int uploadCount;

	private final String TAG = UploadErrorMessage.class.getSimpleName();

	public UploadErrorMessage() {
		this.mContext = ApplicationController.getInstance().getApplicationContext();
	}

	/**
	 * 上传错误信息
	 * 
	 * @param errorCode
	 *            所要上传错误的code
	 * @param errorMessage
	 *            所要上传错误的信息
	 */
	public void uploadErrorMessage(int errorCode, String errorMessage) {
		if (errorCode < 0 || StringUtil.isEmpty(errorMessage)) {
			return;
		}
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		if (params == null) {
			params = new BRRequestParams();
			params.setToken(ApplicationController.getInstance().getLoginfo().getSessionID());
			params.setDeviceId(ApplicationController.getInstance().getDeviceId());
			params.setVerCode(ApplicationController.getInstance().getVerCode());
		}
		if (mRequestQueue == null) {
			mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		}
		params.put("code", errorCode);
		params.put("desc", errorMessage);
		BRFastRequest request = new SaveDriverErrorRequest.Builder().setSucceedListener(succeedResponse)
				.setErrorListener(mErrorListener).setParams(params).build();
		mRequestQueue.add(request);
	}

	ErrorListener mErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			// ToastUtil.showToast(mContext, "上传错误失败");
			LogUtil.d(TAG, error.toString());
			LogUtil.d(TAG, "上传错误失败");
			// 重新上传
			if (uploadCount < 2) {
				handler.sendEmptyMessageDelayed(10088, 1000 * 3);
			} else {
				uploadCount = 0;
			}
		}
	};
	BRJsonRequest.BRJsonResponse succeedResponse = new BRJsonRequest.BRJsonResponse() {
		@Override
		public void onResponse(JSONObject response) {
			if (response != null) {
				Log.d(TAG, response.toString());
				String successMessage = response.optString("success");
				if (!StringUtil.isEmpty(successMessage)) {
					// ToastUtil.showToast(mContext, "上传错误成功");
					LogUtil.d(TAG, "上传错误成功");
				}
				uploadCount = 0;
			}

		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 10088) {
				uploadCount++;
				uploadErrorMessage(errorCode, errorMessage);
			}
		}
	};

}
