package cn.bluerhino.driver.controller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.BRFLAG;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.utils.StringUtil;

import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * desc:通过链接进入网页的Activity
 * condition: 默认至少包含title和url形式的链接地址
 */
public class JPushWebViewActivity extends BaseParentActivity {

	private static final String TAG = JPushWebViewActivity.class
			.getSimpleName();

	@InjectView(R.id.left_img)
	ImageView mBackBtn;
	@InjectView(R.id.center_title)
	TextView mPageTitleTextView;
	@InjectView(R.id.right_title)
	TextView mRightTitleTextView;
	@InjectView(R.id.webview)
	WebView mWebView;
	@InjectView(R.id.progressBar)
	ProgressBar mProgressBar;

	private String mUrl = "http://www.lanxiniu.com/";
	private String mPageTitle = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (super.checkUserIsLogin()) {
			setContentView(R.layout.activity_web_view);
			ButterKnife.inject(this);
			this.initTitle();
			this.parserIntent();
			this.initWebSetting();
			this.loadPage();
		}
	}

	private void initTitle() {
		mBackBtn.setVisibility(View.VISIBLE);
		mBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JPushWebViewActivity.this.responseBackEvent();
			}
		});
		mRightTitleTextView.setVisibility(View.INVISIBLE);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebSetting() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});

		WebChromeClient webChromeClient = new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					mProgressBar.setVisibility(View.GONE);
				} else {
					mProgressBar.setVisibility(View.VISIBLE);
					mProgressBar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

		};
		mWebView.setWebChromeClient(webChromeClient);
	}

	/**
	 * 解析是直接进入 还是 从链接进来的数据
	 */
	private void parserIntent() {
		Intent intent = getIntent();
		if (intent == null) {
			this.enterNotAdmit();
			return;
		}
		// 1.目前只能通过链接进入
		Uri data = intent.getData();
		if (intent.getData() != null) {
			String[] info = URLProcessor.getInfo(data);
			if (info == null) {
				this.enterNotAdmit();
				return;
			} else {
				mUrl = info[0];
				mPageTitle = info[1];
				// 从链接点击进入的页面将清空原来的全部网页内容
				mWebView.clearHistory();
			}
		}
		// 非链接方式进入(直接进入 )
		else {
			// 2. 通过通知进入
			if (intent.getFlags() == BRFLAG.PUSH_ACTIVITY_FLAG) {
				return;
			}
			// 3. 直接处理
			else {
				return;
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			setIntent(intent);
			parserIntent();
		}
	}

	@Override
	protected void onRestart() {
		this.loadPage();
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MobclickAgent.onPageStart(TAG);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPageEnd(TAG);
		super.onPause();
	}

	/**
	 * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			responseBackEvent();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 响应后退和back按下的事件
	 */
	private void responseBackEvent() {
		if (mWebView != null && mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		mPageTitle = null;
		mUrl = null;
		if (mWebView != null) {
			((FrameLayout) mWebView.getParent()).removeView(mWebView);
			mWebView.removeAllViews();
			mWebView.destroy();
			mWebView = null;
		}
		super.onDestroy();
	}

	/**
	 * 处理url载入错误
	 */
	private void enterNotAdmit() {
		startActivity(new Intent(this, LoginActivity.class));
		finish();
		ToastUtil.showToast(getApplicationContext(),
				R.string.toast_piggy_back_entry);
	}

	private void loadPage() {
		mWebView.loadUrl(mUrl);
		mPageTitleTextView.setText(mPageTitle);
	}

	private static class URLProcessor {
		static final String SCHEMA_HTTP = "http";

		/**
		 * 将对应url解析得到标题和url 例如br://native/wap?url=xxxx&title=xxx 如果不包含url 和
		 * title 返回 null
		 */
		static String[] getInfo(Uri uri) {
			String url = uri.getQueryParameter("url");
			String title = uri.getQueryParameter("title");
			if (StringUtil.isEmpty(url))
				return null;
			if (title == null)
				return null;
			url = SCHEMA_HTTP + "://" +  url;
			return new String[] { url, title };
		}
	}
}
