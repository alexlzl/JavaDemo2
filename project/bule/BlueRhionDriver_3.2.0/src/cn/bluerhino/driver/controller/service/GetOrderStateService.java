package cn.bluerhino.driver.controller.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRAction;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.network.CheckDriverCfmOrderRequest;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import com.bluerhino.library.network.framework.BRRequestParams;

/**
 * 单线程处理获得订单的最新状态
 */
public class GetOrderStateService extends IntentService {

	public static final String INT_ORDER_NUM = "orderNum";

	public GetOrderStateService() {
		super("GetOrderStateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent == null)
			return;
		if (!intent.hasExtra(INT_ORDER_NUM))
			return;
		final String orderNum = String.valueOf(intent.getExtras().getInt(
				INT_ORDER_NUM));

		BRRequestParams params = new BRRequestParams();
		ApplicationController appInstance = ApplicationController.getInstance();
		params = new BRRequestParams(appInstance.getLoginfo().getSessionID());
		params.setDeviceId(appInstance.getDeviceId());
		params.setVerCode(appInstance.getVerCode());
		params.put(BRAction.EXTRA_ORDER_ID, orderNum);

		
		BRJsonResponse succeedListener = new BRJsonResponse() {
			@Override
			public void onResponse(JSONObject response) {
				//{"status":1}
				int flag = -1;
				try {
					flag = response.getInt("status");
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
				//只有抢单成功只播语音
				if(flag == 1){
					ApplicationController.getInstance().getBaiduTts().speak(getString(R.string.jpush_notification_voice_hassnatchordersucceed));
				}
			}
		};
		BRFastRequest request = new CheckDriverCfmOrderRequest.Builder()
		.setSucceedListener(succeedListener)
		.setErrorListener(new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//none
			}
		})
		.setParams(params)
		.build();
		
		appInstance.addToRequestQueue(request);
	}
}
