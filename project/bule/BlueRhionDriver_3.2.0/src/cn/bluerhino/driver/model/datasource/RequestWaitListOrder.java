package cn.bluerhino.driver.model.datasource;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.Toast;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.model.OrderInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.VolleySucceedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RequestWaitListOrder{

	private String TAG = RequestWaitListOrder.class.getSimpleName();

	public static final String RPC_CODE = "ret";
	public static final String RPC_DATA = "data";
	public static final String RPC_MSG 	= "msg";
	public static final int PRC_RQUEST_TIMEOUT = 10000;
	public static final int RPC_SUCCESS = 1;
	public static final int RPC_ERROR 	= 0;

	public static final int DATA_SUCCESS = 0;
	public static final int DATA_ERROR = 1;

	public static final String PARAMS_DID = "did";

	private HashMap<String, String> _reqParams;
	private Handler _handler;
	private Context _context;
	private StringRequest strRequest;

	public RequestWaitListOrder(String url, Callback callback) {
		_handler = new Handler(callback);

		_context = ApplicationController.getInstance().getBaseContext();

		VolleySucceedListener listener = new VolleySucceedListener() {
			@Override
			public void onResponse(String response) {
				Message msg = Message.obtain();
				msg.what = DATA_ERROR;
				try {
					JSONObject rootJson = new JSONObject(response);

					int code = (int) rootJson.getInt(RPC_CODE);
					//响应成功,但是ret!=1表示服务器端返回数据不符,直接弹出服务端返回的msg字段返回信息
					if (code != RPC_SUCCESS) {
						String text = (String) rootJson.getString(RPC_MSG);
						Toast.makeText(_context, text, Toast.LENGTH_LONG)
								.show();
						return;
					}

					Gson gson = new Gson();
					Type _type = new TypeToken<List<OrderInfo>>() {
					}.getType();
					List<OrderInfo> _orderInfo = gson.fromJson(rootJson
							.getJSONObject(RPC_DATA).getString("orderInfo"),
							_type);

					msg.what = DATA_SUCCESS;
					msg.obj = _orderInfo;
					Toast.makeText(_context, "数据加载成功", Toast.LENGTH_SHORT)
							.show();
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(_context, "数据异常", Toast.LENGTH_LONG).show();
				} finally {
					_handler.dispatchMessage(msg);
				}
			}
		};

		VolleyErrorListener errorListener = new VolleyErrorListener(_context) {
			@Override
			public void onErrorResponse(VolleyError error) {
				super.onErrorResponse(error);
				Message msg = Message.obtain();
				msg.what = DATA_ERROR;
				_handler.dispatchMessage(msg);
			}
		};

		_reqParams = new HashMap<String, String>();
		//存入司机的Id号
		_reqParams
				.put(PARAMS_DID, ApplicationController.getInstance().getDriverId());
		strRequest = new StringRequest(Method.POST, url,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return _reqParams;
			}
		};
		strRequest.setRetryPolicy(new DefaultRetryPolicy(PRC_RQUEST_TIMEOUT, 1,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}
	
	public void excute() {
		ApplicationController.getInstance().addToRequestQueue(strRequest, TAG);
	}

}
