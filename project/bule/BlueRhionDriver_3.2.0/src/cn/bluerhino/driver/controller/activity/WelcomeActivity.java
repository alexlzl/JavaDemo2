package cn.bluerhino.driver.controller.activity;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.model.CityCarDetail;
import cn.bluerhino.driver.network.CityCarDetailRequest;
import cn.bluerhino.driver.utils.BrStartActivity;
import cn.bluerhino.driver.utils.ConstantsManager;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.jpush.android.api.JPushInterface;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.network.framework.BRModelListRequest.BRModelListResponse;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * Describe:启动页面
 * 
 * Date:2015-8-17
 * 
 * Author:liuzhouliang
 */
public class WelcomeActivity extends BaseParentActivity {

	private static final String TAG = WelcomeActivity.class.getSimpleName();
	private final int SPLASH_DISPLAY_LENGH = 3000;
	/**
	 * 获取车型列表
	 */
	private CityCarDetailRequest mDetailRequest;
	private RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if (!StringUtil.isEmpty(ApplicationController.getInstance()
				.getLoginfo().getSessionID())) {
			/**
			 * session不为空
			 */
			getCityCarData();
		} else {
			/**
			 * session为空
			 */
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(mContext,
							AdvertisementActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(ConstantsManager.FROM_WHERE,
							"WelcomeActivity");
					mContext.startActivity(intent);
					finish();
				}
			}, SPLASH_DISPLAY_LENGH);
		}

	}

	@Override
	protected void onResume() {
		MobclickAgent.onPageStart(TAG);
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPageEnd(TAG);
		JPushInterface.onPause(this);
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		return;
	}

	private void getCityCarData() {
		mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		BRRequestParams params = new BRRequestParams();
		params.setDeviceId(ApplicationController.getInstance().getDeviceId());
		params.setVerCode(ApplicationController.getInstance().getVerCode());
		BRModelListResponse<List<CityCarDetail>> succeedListener = new BRModelListResponse<List<CityCarDetail>>() {
			@Override
			public void onResponse(List<CityCarDetail> model) {
				if (model != null) {
					LogUtil.d(TAG, "车型列表==" + model.toString());
					// ApplicationController.getInstance().setCityCarDetailList(
					// model);
					// Intent intent = new Intent(mContext,
					// AdvertisementActivity.class);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// intent.putExtra(ConstantsManager.FROM_WHERE,
					// "WelcomeActivity");
					// mContext.startActivity(intent);
					Intent intent = new Intent(mContext, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
					finish();
				}
			}
		};

		ErrorListener errorListener = new VolleyErrorListener(mContext) {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.d(TAG, "车型列表==获取失败");
				Intent intent = new Intent(mContext, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				finish();
			}
		};

		mDetailRequest = new CityCarDetailRequest.Builder()
				.setSucceedListener(succeedListener)
				.setErrorListener(errorListener).setParams(params).build();
		mRequestQueue.add(mDetailRequest);
	}
}
