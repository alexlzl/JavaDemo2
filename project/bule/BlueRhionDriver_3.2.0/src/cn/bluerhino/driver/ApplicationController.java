package cn.bluerhino.driver;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.Volley;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.util.common.StringUtils;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import com.bluerhino.library.network.framework.BRModelListRequest.BRModelListResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import cn.bluerhino.driver.controller.ordermanager.CountdownOrderInfo;
import cn.bluerhino.driver.controller.ordermanager.CountdownOrderInfoUtil;
import cn.bluerhino.driver.controller.service.GetOrderStateService;
import cn.bluerhino.driver.exception.CrashHandler;
import cn.bluerhino.driver.helper.ServiceHelper;
import cn.bluerhino.driver.helper.url.URIFormater;
import cn.bluerhino.driver.model.ChangeOrderInfor;
import cn.bluerhino.driver.model.CheckInStat;
import cn.bluerhino.driver.model.CityCarDetail;
import cn.bluerhino.driver.model.LocationInfo;
import cn.bluerhino.driver.model.Loginfo;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.network.CityCarDetailRequest;
import cn.bluerhino.driver.network.TabInforRequest;
import cn.bluerhino.driver.utils.AppRunningInfor;
import cn.bluerhino.driver.utils.BaiduTtsUtil;
import cn.bluerhino.driver.utils.DeviceInfo;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;

public class ApplicationController extends Application {
	/**
	 * 启动调试开关
	 */
	public static final boolean testSwitch = true;
	private static final String TAG = ApplicationController.class.getSimpleName();
	// 测试用定位周期
	private static final int MIN_SCAN_SPAN_NETWORK_TEST = 1000 * 5;
	// 定时时间间隔
	private static final int MIN_SCAN_SPAN_NETWORK = testSwitch ? MIN_SCAN_SPAN_NETWORK_TEST : 1000 * 10;
	private static ApplicationController INSTANCE;
	private String mDeviceId;
	private String mVerCode;
	private SharedPreferences mLoginfoSharedPreferences;
	private LocationClient mLocationClient;
	private LocationInfo mLastLocatioInfo;
	private LocalBroadcastManager mLocalBroadcastManager;
	private RequestQueue mRequestQueue;
	private WakeLock mWakeLock;
	private long mLastRealTime, mServerTime;
	private List<CityCarDetail> mCityCarDetailList;
	private BaiduTtsUtil baiduTts;
	private List<LatLng> locationList;
	// 是否启动定时任务，默认启动，但在手动触发的时候，不进行启动
	public boolean isStartTimer = true;
	// 4600下点击服务完成
	public boolean isAutomaticToNextState4600;
	// 4150状态下，点击服务完成
	public boolean isAutomaticTo4800;
	// 多个收货地:已经完成状态：4000，点击到达收货地--从发货地出发--到达收货地
	public boolean isMultiAutomaticTo4600;
	public boolean isAutomaticTo5000;
	public boolean isTimerStart;
	public boolean isStartAgainApp;
	public Handler gloablHandler;
	/**
	 * 订单列表监听到activity返回
	 */
	public Handler mOrderListBackHandler;
	// 标记订单是否可以被清除
	public boolean isOrderDelete = true;
	// 已经取消或者改派的订单ID
	public int rearrangedOrderId;
	// 标记订单更改状态,1:取消；2:改派
	public int orderChangeState;
	public boolean isNeedShowDialog;
	public boolean isAutomaticTo4000;
	// 多个收货地，还有未走完的收货地，在已经完成4600状态点击到达下一个收货地，需要手动执行4800,4600，到下一个收货地
	public boolean isAutomaticToNextDestination;
	public boolean isShowLoadingView;
	public boolean isAPPRunningForeground = true;
	// 单个收货地，在已经完成状态：到达发货地 4000，点击再次出发，自动执行状态：4150,4600
	public boolean isStartAgainAutomaticTo4600;
	// 判断单个收货地，是否再次出发
	public boolean isSingleStartAgain;
	// 判断多个收货地，是否再次出发
	public boolean isMultipleStartAgain;
	// 点击到达发货地，获取当前地理位置信息
	public LocationInfo deliverGoodsLocationInfo;
	public List<LocationInfo> locationInfos;
	/**
	 * 拥有倒计时秒数的抢单列表list
	 */
	public List<CountdownOrderInfo> mCountDownList;
	private CheckInStat mCheckInstat;
	private String tabTitle;
	private String tabUrl;
	// 缓存更改状态的订单信息
	private List<ChangeOrderInfor> changeStateOrderList;
	// 缓存当前订单中状态改变的订单
	private List<OrderInfo> mCacheCurrentOrderList;

	@Override
	public void onCreate() {
		super.onCreate();
		String pName = AppRunningInfor.getProcessName(this, android.os.Process.myPid());
		if (pName != null && super.getPackageName().equals(pName)) {
			init();
			MobclickAgent.onEvent(this, "test_event_start_times");
		}
	}

	public Handler getGloablHandler() {
		return gloablHandler;
	}

	public Handler getOrderListBackHandler() {
		return mOrderListBackHandler;
	}

	public void setGloablHandler(Handler handler) {
		if (gloablHandler == null) {
			gloablHandler = handler;
		}
	}

	public void setOrderListBackHandler(Handler handler) {
		if (mOrderListBackHandler == null) {
			mOrderListBackHandler = handler;
		}
	}

	public static final synchronized ApplicationController getInstance() {
		return INSTANCE;
	}

	private void init() {
		/**
		 * 初始化异常处理
		 */
		CrashHandler.getInstance().init(this);
		INSTANCE = this;
		SDKInitializer.initialize(this);
		initApplication();
		initLoginfoSharedPreferences();
		initLocationClient();
		initSynthesizer();
		initUmengAnalytics();
		URIFormater.init(this);
		initAppData();
	}

	private void initAppData() {
		// 初始化服务管理数据
		if (StringUtils.isEmpty(tabTitle) || !StringUtil.isUrl(tabUrl)) {
			inviteTabNet();
		}
		//TODO 初始化车辆列表数据
		/*if (getCityCarDetailList() == null || getCityCarDetailList().size() == 0) {
			getCityCarData();
		}*/
	}

	public synchronized List<LatLng> getLocationList() {
		return locationList;
	}

	public synchronized void setLocationList(List<LatLng> locationList) {
		this.locationList = locationList;
	}

	private void initApplication() {
		mCacheCurrentOrderList = new ArrayList<OrderInfo>();
		changeStateOrderList = new ArrayList<ChangeOrderInfor>();
		locationInfos = new ArrayList<LocationInfo>();
		initCountDownList();
		// 本地广播，更安全
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		// 默认是为0(未知状态)
		mCheckInstat = CheckInStat.UnknownStat;
	}

	public synchronized List<OrderInfo> getmCacheOrderList() {
		return mCacheCurrentOrderList;
	}

	public synchronized void setmCacheOrderList(List<OrderInfo> mCacheOrderList) {
		this.mCacheCurrentOrderList = mCacheOrderList;
	}

	public synchronized List<ChangeOrderInfor> getChangeStateOrderList() {
		return changeStateOrderList;
	}

	public synchronized void setChangeStateOrderList(List<ChangeOrderInfor> changeStateOrderList) {
		this.changeStateOrderList = changeStateOrderList;
	}

	private void initCountDownList() {
		mCountDownList = new ArrayList<CountdownOrderInfo>();
		Runnable cycleRunnable = new Runnable() {
			long start;
			long end;
			long interval;
			int size;

			@Override
			public void run() {
				while (true) {
					start = SystemClock.elapsedRealtime();
					size = mCountDownList.size();
					for (int i = 0; i < size; i++) {
						CountdownOrderInfo orderInfo = mCountDownList.get(i);
						int sec = orderInfo.getSecond();
						if (sec != 0) {
							orderInfo.setSecond(--sec);
						} else {
							CountdownOrderInfo removeInfo = mCountDownList.remove(i);
							// 判断是否是抢单成功且未响应的订单, 是则进行网络请求获取最新状态
							if (removeInfo.isNeedNotify()) {
								Intent intent = new Intent(getApplicationContext(), GetOrderStateService.class);
								intent.putExtra(GetOrderStateService.INT_ORDER_NUM, removeInfo.getOrderNum());
								startService(intent);
							}
							i--;
							size--;
						}
					}
					end = SystemClock.elapsedRealtime();
					interval = end - start;
					if (interval < 1000) {
						try {
							Thread.sleep(1000 - interval);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}

		};
		new Thread(cycleRunnable).start();
	}

	private void initLoginfoSharedPreferences() {
		mLoginfoSharedPreferences = getLoginSharedPreferences();
	}

	/**
	 * 
	 * Describe:初始化定位信息
	 * 
	 * Date:2015-6-24
	 * 
	 * Author:liuzhouliang
	 */
	private void initLocationClient() {
		LocationClientOption option = new LocationClientOption();
		option.setProdName("BRDriver");// 渠道号
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setScanSpan(MIN_SCAN_SPAN_NETWORK);
		option.setCoorType("bd09ll");
		option.setOpenGps(true);
		option.setIsNeedAddress(true);
		mLastLocatioInfo = new LocationInfo();
		mLocationClient = new LocationClient(getApplicationContext(), option);
	}

	/**
	 * 初始化讯飞语音合成器
	 */
	private void initSynthesizer() {
		baiduTts = new BaiduTtsUtil(this);
	}

	public BaiduTtsUtil getBaiduTts() {
		return baiduTts;
	}

	private void initUmengAnalytics() {
		AnalyticsConfig.enableEncrypt(true);
		MobclickAgent.openActivityDurationTrack(false);
	}

	public final LocationClient getLocationClient() {
		return mLocationClient;
	}

	public final void setLastLocationInfo(LocationInfo lastLocationInfo) {
		synchronized (mLastLocatioInfo) {
			this.mLastLocatioInfo = lastLocationInfo;
			if (testSwitch) {
//				ToastUtil.showToast(getApplicationContext(), "最新坐标Longitude===" + lastLocationInfo.getLongitude()
//						+ "Latitude===" + lastLocationInfo.getLatitude(), 100);
			}

		}
	}

	public final LocationInfo getLastLocationInfo() {
		synchronized (this.mLastLocatioInfo) {
			return this.mLastLocatioInfo;
		}
	}

	/**
	 * 
	 * Describe:获取网络请求队列
	 * 
	 * Date:2015-7-27
	 * 
	 * Author:liuzhouliang
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public final LocalBroadcastManager getLocalBroadcastManager() {
		return mLocalBroadcastManager;
	}

	public final SharedPreferences getSharedPreferences(String perferencesName) {
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(perferencesName,
				Context.MODE_PRIVATE);
		return sharedPreferences;
	}

	private final SharedPreferences getLoginSharedPreferences() {
		if (null == mLoginfoSharedPreferences) {
			mLoginfoSharedPreferences = getSharedPreferences("loginfo");
		}
		return mLoginfoSharedPreferences;
	}

	/**
	 * 同步服务器时间
	 */
	public final void setServerTime(long serverTime) {
		mLastRealTime = SystemClock.elapsedRealtime();
		mServerTime = serverTime;
	}

	// public final void setServerTime(long currentRealTime, long serverTime) {
	// mLastRealTime = currentRealTime;
	// mServerTime = serverTime;
	// }

	/**
	 * 获得服务器的当前时间
	 * 
	 * @return
	 */
	public final long generateServerTime() {
		return mServerTime * 1000 + (SystemClock.elapsedRealtime() - mLastRealTime);
	}

	public final String generateServerTimeValue() {
		return DateUtils.formatDateTime(getApplicationContext(), generateServerTime(), DateUtils.FORMAT_SHOW_YEAR
				| DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
	}

	private Loginfo getLoginfoFromSharedPre() {
		SharedPreferences sharedPreferences = getSharedPreferences("loginfo");
		String id = sharedPreferences.getString("id", "");
		String userName = sharedPreferences.getString("userName", "");
		String passWord = sharedPreferences.getString("passWord", "");
		boolean isMemorize = sharedPreferences.getBoolean("isMemorize", false);
		String sessionID = sharedPreferences.getString("sessionID", "");
		boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
		Loginfo loginfo = new Loginfo();
		loginfo.setId(id);
		loginfo.setUserName(userName);
		loginfo.setPassWord(passWord);
		loginfo.setMemorize(isMemorize);
		loginfo.setSessionID(sessionID);
		loginfo.setIsLogin(isLogin);
		return loginfo;

	}

	private void setLoginfoToSharedPre(Loginfo loginfo) {
		if (loginfo != null) {
			Editor editor = mLoginfoSharedPreferences.edit();
			editor.clear();
			editor.putString("id", loginfo.getId());
			editor.putString("userName", loginfo.getUserName());
			editor.putString("passWord", loginfo.getPassWord());
			editor.putBoolean("isMemorize", loginfo.getMemorize());
			editor.putString("sessionID", loginfo.getSessionID());
			editor.putBoolean("isLogin", loginfo.getIsLogin());
			editor.commit();
		}

	}

	/**
	 * Describe:获取登陆信息
	 * 
	 * Date:2015-6-24
	 * 
	 * Author:liuzhouliang
	 */
	public Loginfo getLoginfo() {
		Loginfo loginfo = this.getLoginfoFromSharedPre();
		return loginfo;

	}

	public void setLoginfo(Loginfo loginfo) {
		this.setLoginfoToSharedPre(loginfo);
	}

	/**
	 * 
	 * Describe:保证在锁屏情况下后台应用唤醒
	 * 
	 * Date:2015-6-24
	 * 
	 * Author:liuzhouliang
	 */
	public final void acquireWakeLock() {
		if (null == mWakeLock) {
			PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, TAG);
			if (null != mWakeLock) {
				mWakeLock.acquire();
			}
		}
	}

	public final void releaseWakeLock() {
		if (null != mWakeLock) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	public final String getDeviceId() {
		if (mDeviceId == null) {
			DeviceInfo di = DeviceInfo.getInstance();
			mDeviceId = di.getDeviceId(this);
		}
		return mDeviceId;
	}

	public final String getVerCode() {
		if (mVerCode == null) {
			mVerCode = "andriod_d_" + DeviceInfo.getInstance().getAppVersionName(this, getPackageName());
		}
		return mVerCode;
	}

	/**
	 * 司机ID非手机号, 唯一标识 ,使用getLoginfo方法代替
	 */
	@Deprecated
	public final String getDriverId() {
		return this.getLoginfo().getId();
	}

	/**
	 * session字段依附于Loginfo对象 ,使用getLoginfo方法代替
	 */
	@Deprecated
	public final String getSessionID() {
		return this.getLoginfo().getSessionID();
	}

	public final List<CityCarDetail> getCityCarDetailList() {
		return mCityCarDetailList;
	}

	public final void setCityCarDetailList(List<CityCarDetail> cityCarDetailList) {
		mCityCarDetailList = cityCarDetailList;
	}

	public final void setLoginStatus(boolean status) {
		Editor editor = mLoginfoSharedPreferences.edit();
		editor.putBoolean("isLogin", status);
		editor.commit();
	}

	/**
	 * private boolean mLoginStatus; 功能:判断进入该页面(非正常跳转:
	 * 1.从Jpush跳转,2.从外部链接跳转)能否需要重新登录: 满足条件:点击登录并正确响应 不满足条件 1.已退出应用回退栈为空
	 * 2.session为""或null 则根据该字段判断是否需要重新登录
	 */
	public final boolean getLoginStatus() {
		return mLoginfoSharedPreferences.getBoolean("isLogin", false);
	}

	public void setCheckInstat(CheckInStat stat) {
		mCheckInstat = stat;
	}

	public CheckInStat getCheckInstat() {
		return mCheckInstat;
	}

	public String getTabUrl() {
		return tabUrl;
	}

	public String getTabTitle() {
		return tabTitle;
	}

	public void setTabUrl(String tabUrl) {
		this.tabUrl = tabUrl;
		LogUtil.d(TAG, tabUrl);
	}

	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
		LogUtil.d(TAG, tabTitle);
	}

	/**
	 * 请求访问tab页面的url
	 */
	public void inviteTabNet() {
		BRRequestParams mParams = new BRRequestParams(ApplicationController.getInstance().getSessionID());
		mParams.setDeviceId(ApplicationController.getInstance().getDeviceId());
		mParams.setVerCode(ApplicationController.getInstance().getVerCode());
		mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		VolleyErrorListener mErrorListener = new VolleyErrorListener(this) {
			@Override
			public void onErrorResponse(VolleyError error) {
				super.onErrorResponse(error);
				LogUtil.d(TAG, getResources().getString(R.string.load_web_error));
			}
		};

		final BRJsonResponse updateInfoSucceedListener = new BRJsonResponse() {
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.d(TAG, response.toString());
				try {
					if (StringUtil.isUrl(response.getString("url"))) {
						setTabUrl(response.getString("url"));
					}
					if (!StringUtils.isEmpty("title"))
						setTabTitle(response.getString("title"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		mRequestQueue.add(new TabInforRequest.Builder().setSucceedListener(updateInfoSucceedListener)
				.setErrorListener(mErrorListener).setParams(mParams).build());
	}

	/**
	 * @return 获得能处理倒计时订单的增强抢单列表
	 */
	public List<CountdownOrderInfo> getCountDownOrderList() {
		return mCountDownList;
	}

	/**
	 * 添加一条新消息, 该方法只用于极光推送
	 * 
	 * @param orderInfo
	 */
	public void addCountdownOrderInfo(OrderInfo orderInfo) {
		mCountDownList.add(new CountdownOrderInfo(orderInfo));
	}

	/**
	 * 通知抢单列表页中如若存在则删除该订单 该方法只用于极光推送
	 * 
	 * @param orderNum
	 */
	public void removeCountdownOrderInfo(int orderNum) {
		CountdownOrderInfo orderInfo = this.getMatchOrder(orderNum);
		if (orderInfo != null) {
			orderInfo.setSecond(0);
		}
	}

	/**
	 * 情况一:在为抢单情况下 情况二:在已抢单,等待抢单结果的情况下(在被抢单状态向下已延长120s, 已是被抢单状态, 设置抢单成功和失败的回馈信息)
	 * 修改对应订单号中从极光推送中的reason,该方法只用于极光推送
	 * 
	 * @param orderNum
	 *            该条订单的订单号
	 * @param reason
	 *            对应的变动理由
	 */
	public void updateReason(int orderNum, String reason) {
		CountdownOrderInfo orderInfo = this.getMatchOrder(orderNum);
		if (orderInfo != null) {
			CountdownOrderInfoUtil.setReason(orderInfo, reason);
			orderInfo.setReason(reason);
		}
	}

	/**
	 * 对应订单号中是否成功被响应(抢单成功, 不包含抢单失败) 该方法只用于极光推送
	 * 
	 * @param orderNum
	 *            该条订单的订单号
	 * @return 是否需要阻止消息传递
	 */
	public boolean isOrderNumResponse(int orderNum) {
		CountdownOrderInfo orderInfo = this.getMatchOrder(orderNum);
		if (orderInfo != null) {
			return orderInfo.isResponse();
		}
		return true;
	}

	/**
	 * 正对订单推送失败的情况, 判断是否需要改变状态 该方法只用于极光推送
	 * 
	 * @param orderNum
	 *            该条订单的订单号
	 * @return 是否需要阻止消息传递
	 */
	public boolean isFailureOrderNeedResopnse(int orderNum) {
		CountdownOrderInfo orderInfo = this.getMatchOrder(orderNum);
		if (orderInfo != null) {
			if (orderInfo.isClicked()) {
				return orderInfo.isResponse();
			}
			// 未点击, 但已接收到抢单失败消息,response状态置为true且不处理消息
			else {
				orderInfo.setResponse(true);
				return true;
			}
		}
		return true;
	}

	/**
	 * 从列表中匹配对应订单号的订单
	 * 
	 * @param orderNum
	 *            该条订单的订单号
	 */
	public CountdownOrderInfo getMatchOrder(int orderNum) {
		CountdownOrderInfo orderInfo = null;
		int size = mCountDownList.size();
		int countdownOrderId = 0;
		int index;
		for (index = 0; index < size; index++) {
			orderInfo = mCountDownList.get(index);
			countdownOrderId = orderInfo.getOrderId();
			if (countdownOrderId == orderNum) {
				break;
			}
		}
		if (index == size)
			return null;
		else {
			return orderInfo;
		}
	}

	private void getCityCarData() {
		BRRequestParams params = new BRRequestParams();
		RequestQueue mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		params.setDeviceId(ApplicationController.getInstance().getDeviceId());
		params.setVerCode(ApplicationController.getInstance().getVerCode());
		BRModelListResponse<List<CityCarDetail>> succeedListener = new BRModelListResponse<List<CityCarDetail>>() {
			@Override
			public void onResponse(List<CityCarDetail> model) {
				if (model != null) {
					LogUtil.d(TAG, "车型列表==" + model.toString());
					ApplicationController.getInstance().setCityCarDetailList(model);
				}
			}
		};
		ErrorListener errorListener = new VolleyErrorListener(this) {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.d(TAG, "---车型列表获取失败---");
			}
		};
		CityCarDetailRequest mDetailRequest = new CityCarDetailRequest.Builder().setSucceedListener(succeedListener)
				.setErrorListener(errorListener).setParams(params).build();
		if (mRequestQueue != null) {
			mRequestQueue.add(mDetailRequest);
		}
	}

}
