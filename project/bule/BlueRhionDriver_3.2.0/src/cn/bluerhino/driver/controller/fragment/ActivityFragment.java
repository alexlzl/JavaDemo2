package cn.bluerhino.driver.controller.fragment;

import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.MainActivity;
import cn.bluerhino.driver.network.TabInforRequest;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.bluerhino.driver.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.baidu.navisdk.util.common.StringUtils;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;

/**
 * Describe:
 * 
 * Date:2015-8-24
 * 
 * Author:liuzhouliang
 */
public class ActivityFragment extends BaseParentFragment {
	private Context mContext;
	private WebSettings mWebSettings = null;
	private LoadingDialog mDialog;
	private static final String TAG = ActivityFragment.class.getSimpleName();

	ApplicationController controller;
	private View view;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
		mDialog = new LoadingDialog(mContext);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		view = layoutInflater.inflate(R.layout.activity_fragment_layout, container, false);
		getUrlToLoadWeb();
		return view;
	}

	/**
	 * 先从本地内存获取url 如果获取不到，再去加载网络
	 */
	private void getUrlToLoadWeb() {
		controller = controller == null ? ApplicationController.getInstance() : controller;
		if (StringUtils.isEmpty(controller.getTabTitle()) || !StringUtil.isUrl(controller.getTabUrl())) {
			inviteTabNet();
		} else {
			loadView(view);
		}
	}

	private void loadView(View parentView) {
		WebView webView = (WebView) parentView.findViewById(R.id.activity_fragment_layout_wv);
		// setWebViewLoadListener(webView);
		if (null != webView) {
			mWebSettings = webView.getSettings();
		}
		mWebSettings.setUseWideViewPort(true);
		mWebSettings.setLoadWithOverviewMode(true);
		mWebSettings.setLoadsImagesAutomatically(true);
		mWebSettings.setPluginState(WebSettings.PluginState.ON);
		mWebSettings.setLightTouchEnabled(true);
		mWebSettings.setSaveFormData(false);
		mWebSettings.setSavePassword(false);
		mWebSettings.setNeedInitialFocus(false);
		mWebSettings.setSupportMultipleWindows(false);
		mWebSettings.setAppCacheEnabled(false);
		mWebSettings.setDatabaseEnabled(false);
		mWebSettings.setDomStorageEnabled(false);
		mWebSettings.setGeolocationEnabled(false);
		mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebSettings.setRenderPriority(RenderPriority.HIGH);
		mWebSettings.setBlockNetworkImage(false);
		mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl(controller.getTabUrl());

	}

	private void setWebViewLoadListener(WebView webView) {
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mDialog.dismiss();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (!mDialog.isShowing()) {
					mDialog.show();
				}
			}

		});

	}

	/**
	 * 请求网络访问tab页面的url
	 */
	public void inviteTabNet() {
		if (mDialog != null) {
			mDialog.show();
		}
		BRRequestParams mParams = new BRRequestParams(ApplicationController.getInstance().getSessionID());
		mParams.setDeviceId(ApplicationController.getInstance().getDeviceId());
		mParams.setVerCode(ApplicationController.getInstance().getVerCode());
		mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		VolleyErrorListener mErrorListener = new VolleyErrorListener(getActivity()) {
			@Override
			public void onErrorResponse(VolleyError error) {
				super.onErrorResponse(error);
				LogUtil.d(TAG, "加载tab页面url失败");
				if (mDialog != null) {
					mDialog.dismiss();
				}
				ToastUtil.showToast(mContext, R.string.load_web_error);
			}
		};

		final BRJsonResponse updateInfoSucceedListener = new BRJsonResponse() {
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.d("url", response.toString());
				if (mDialog != null) {
					mDialog.dismiss();
				}
				try {

					if (!StringUtils.isEmpty(response.getString("title"))) {
						controller.setTabTitle(response.getString("title"));
						((MainActivity) getActivity()).setTitle();
					}
					if (StringUtil.isUrl(response.getString("url"))) {
						controller.setTabUrl(response.getString("url"));
						loadView(view);
					} else {
						ToastUtil.showToast(mContext, R.string.load_web_error);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtil.showToast(mContext, R.string.load_web_error);
				}
			}
		};
		mRequestQueue.add(new TabInforRequest.Builder().setSucceedListener(updateInfoSucceedListener)
				.setErrorListener(mErrorListener).setParams(mParams).build());
	}

}
