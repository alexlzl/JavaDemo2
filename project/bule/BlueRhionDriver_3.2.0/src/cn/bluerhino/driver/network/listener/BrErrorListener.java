package cn.bluerhino.driver.network.listener;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.LoginActivity;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.framework.BRResponseError;
import com.bluerhino.library.utils.ToastUtil;

public class BrErrorListener implements ErrorListener {
	
	/**
	 * 当session改变,需要实现的接口
	 */
	public interface OnSessinChangedListener{
		/**
		 * 一般为finsh自身依附Activity,跳到登陆页
		 */
		void onSessinChange();
	}
	
	private OnSessinChangedListener mListener;
	
	private Context mCxt;

	public BrErrorListener(Context cxt) {
		super();
		this.mCxt = cxt;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		/**
		 * 是否是自定义错误
		 */
		boolean isBRResponseError = false;
		String errorDesc = "";
		if (null != error) {
			if (error instanceof BRResponseError) {
				errorDesc = error.getMessage();
				isBRResponseError = true;
			} else if (error instanceof ServerError) {
				// 服务器错误
				errorDesc = "服务器错误";
			} else if (error instanceof TimeoutError) {
				// 超时错误
				errorDesc = "您的网络连接不畅，请检查网络";
			} else if (error instanceof NetworkError) {
				// 网络连接错误
				errorDesc = "您的网络连接不畅，请检查网络";
			} else if (error instanceof ParseError) {
				// 解析错误
				errorDesc = "内容解析错误";
			}
			handleError(error, isBRResponseError, errorDesc);
		}
	}
	
	/**
	 * 根据错误类型进行不同的处理
	 * @param error 
	 * @param isBRResponseError 是否是自定义错误
	 * @param errorDesc 错误描述
	 */
	private void handleError(VolleyError error, boolean isBRResponseError, String errorDesc) {
		if (isBRResponseError) {
			BRResponseError responseError = (BRResponseError) error;
			//responseError其他错误代码号可以忽略,只有300代表需要重新登录
			if (responseError.getCode() == 300) {
				if(mListener != null){
					mListener.onSessinChange();
				}
				Intent intent = new Intent(mCxt, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mCxt.startActivity(intent);
				//更新300的错误描述
				errorDesc = mCxt.getString(R.string.wait_order_login_timeout);
			}
		}
		ToastUtil.showToast(mCxt, errorDesc, Toast.LENGTH_LONG);
	}
	
	public void setOnSessinChangedListener(OnSessinChangedListener listener){
		this.mListener = listener;		
	}
}
