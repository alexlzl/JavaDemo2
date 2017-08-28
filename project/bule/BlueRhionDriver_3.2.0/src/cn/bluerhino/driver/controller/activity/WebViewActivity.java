package cn.bluerhino.driver.controller.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRFLAG;
import cn.bluerhino.driver.BRURL;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.helper.url.URLProcessor;
import cn.bluerhino.driver.model.Loginfo;
import cn.bluerhino.driver.utils.LogUtil;

import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgentJSInterface;

/**
 * 个人中心，嵌套网页
 */
public class WebViewActivity extends BaseParentActivity {

	private static final String TAG = WebViewActivity.class.getSimpleName();
	public static final String URL_KEY = "Url";
	public static final String PAGE_TITLE = "PAGE_TITLE";

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

	/**
	 * 判断是否从本地推送进入账户详情页
	 */
	private boolean mAccoutDetailFromLoc;

	/**
	 * 序号与对应标题的关系
	 */
	private String[] mCenterTitleArray;

	private int mIndex = -1;
	
	private WapURLProcessor mURLProcessor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initURLProcessor();
		if (super.checkUserIsLogin()) {
			setContentView(R.layout.activity_web_view);
			ButterKnife.inject(this);
			initRes();
			initTitle();
			initWebSetting();
			parserIntent();
			mWebView.loadUrl(mUrl);
		}
	}
	
	private void initURLProcessor() {
		mURLProcessor = WapURLProcessor.getInstance(this);
	}

	private void initRes() {
		if(mCenterTitleArray == null){
			mCenterTitleArray = mRes.getStringArray(R.array.jump_url_path_name);
		}
	}

	private void initTitle() {
		mBackBtn.setVisibility(View.VISIBLE);
		mBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				responseBackEvent();
			}
		});
		mRightTitleTextView.setVisibility(View.INVISIBLE);
		mRightTitleTextView.setOnClickListener(new View.OnClickListener() {
			/**
			 * 通过wap页进入账户详情页, 非Jpush, 短信链接方式
			 */
			@Override
			public void onClick(View v) {
				mAccoutDetailFromLoc = true;
				mIndex = 1;
				String url = WapURLProcessor.getUrl(mIndex);
				mWebView.loadUrl(url);
				MobclickAgent.onEvent(mContext, "pageUserInfo_page_driveinfo");
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebSetting() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				/**
				 * 根据url得到对应的标题文字
				 */
				// 1.从固定条目中的跳转
				if (mIndex == -2) {
					mPageTitleTextView.setText(mPageTitle);
					if (mRes.getString(R.string.account_info).equals(mPageTitle)) {
						mRightTitleTextView.setText(mRes.getString(R.string.account_detail));
						mRightTitleTextView.setVisibility(View.VISIBLE);
					}else{
						mRightTitleTextView.setVisibility(View.INVISIBLE);
					}
				}
				// 2.默认加载出错,不匹配的url
				else if (mIndex == -1) {
					mPageTitle = mRes.getString(R.string.webview_load_error);
					mPageTitleTextView.setText(mPageTitle);
					mRightTitleTextView.setVisibility(View.INVISIBLE);
				}
				// 3.从短信链接进入 
				else if (mIndex >= 0 && mIndex < mCenterTitleArray.length) {
					//固定url得到的对应数组的标题
					mPageTitle = mCenterTitleArray[mIndex];
					mPageTitleTextView.setText(mPageTitle);
					//设置能否进行后退操作
					//从短信进入, 但不属于订单详情页
					if (mIndex != 1) {
						// 从短信进入的后退设置为无效: 顺便clear, 这样按返回键,判断后退无页面, 就能直接退出
						if (mWebView != null) {
							mWebView.clearHistory();
						}
					} 
					//从短信进入, 属于订单详情页
					else {
						// usually 如果是从本地,需要后退
						if (mAccoutDetailFromLoc) {
							// none
						}
						// 默认false, 表示推送进入的, 设置不能后退
						else {
							if (mWebView != null) {
								mWebView.clearHistory();
							}
						}
					}
					// 根据得到的标题文字,设置右侧标题的显示, 如果本页是账户信息, 则右边字段变为账户详情
					if (mIndex == 0) {
						mRightTitleTextView.setText(mRes.getString(R.string.account_detail));
						mRightTitleTextView.setVisibility(View.VISIBLE);
					} else {
						mRightTitleTextView.setVisibility(View.INVISIBLE);
					}
				}
				// 其他情况, 默认无处理
				else {
					mRightTitleTextView.setVisibility(View.INVISIBLE);
				}
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

		// webView 统计
		new MobclickAgentJSInterface(this, mWebView, webChromeClient);
	}

	/**
	 * 解析是直接进入 还是 从链接进来的数据
	 */
	private void parserIntent() {
		Intent intent = getIntent();
		if(intent == null){
			this.enterNotAdmit();
			return;
		}
		// 能否合法进入(能获得url 和 title)
		if(mUrl == null || mPageTitle == null){
			this.enterNotAdmit();
			return;
		}
		//1.通过链接进入
		if (intent.getData() != null) {
			// 得到index 和 url
			mIndex = mURLProcessor.getJumpUrlIndex(intent.getData());
			mUrl = WapURLProcessor.getUrl(mIndex);
			// 从短信点击进入的页面将清空原来的全部网页内容
			mWebView.clearHistory();
			LogUtil.d(TAG, "请求URL=="+mUrl);
		}
		// 非链接方式进入(直接进入 , 不包含Jpush方式进入)
		else {
			//2. 极光推送进入(无需处理)
			if (intent.getFlags() == BRFLAG.PUSH_ACTIVITY_FLAG) {
				//none
				this.enterNotAdmit();
				return;
			}
			//3. 直接处理
			else{
				mUrl = intent.getStringExtra(URL_KEY);
				if(mUrl == null){
					this.enterNotAdmit();
					return;
				}
				mPageTitle = intent.getStringExtra(PAGE_TITLE);
				if(mPageTitle == null){
					this.enterNotAdmit();
					return;
				}
				mIndex = -2;
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
		mWebView.loadUrl(mUrl);
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
			// 可以后退的情况: 只有从本地进入
			if (mIndex == 1) {
				mRightTitleTextView.setVisibility(View.VISIBLE);
				mIndex = 0;
			}
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
	 * 错误进入该页
	 */
	private void enterNotAdmit(){
		startActivity(new Intent(this, LoginActivity.class));
		finish();
		ToastUtil.showToast(getApplicationContext(), R.string.toast_piggy_back_entry);
	}
	
	private static class WapURLProcessor extends URLProcessor{
		
		private static WapURLProcessor sInstance = null;
		
		private WapURLProcessor(Context context){
			super(context, R.array.array_wap_url);	
		}
		
		static WapURLProcessor getInstance(Context context){
			if(sInstance == null){
				synchronized(WapURLProcessor.class){
					if(sInstance == null){
						sInstance = new WapURLProcessor(context);
					}
				}
			}
			return sInstance;
		}
		
		/**
		 * 序号与对应真实url的关系
		 */
		private final static String[] urlAliasArray = new String[] { 
			BRURL.ACCOUNT_URL, 
			BRURL.ACCOUNT_DETAIL_URL,
			BRURL.CUSTOMER_GRADE_URL, 
			BRURL.DRIVER_INVITE_URL};
		
		//0~array.length 根据index得到对应的url
		/**
		 * 通过序号得到对应的url
		 * @param index
		 * @return 加入session新的url
		 */
		static String getUrl(int index){
			Loginfo loginfo = ApplicationController.getInstance().getLoginfo();
			String url = String.format(urlAliasArray[index], loginfo.getId(),
					loginfo.getSessionID(),
					ApplicationController.getInstance().getDeviceId(),
					ApplicationController.getInstance().getVerCode());
			if(url == null){
				throw new NullPointerException("得到url为空, 通过序号获取url失败");
			}
			return url;
		}
	}
}
