package cn.bluerhino.driver.controller.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.OrderDealInfoItemActivity;
import cn.bluerhino.driver.controller.ordermanager.CountdownOrderInfo;
import cn.bluerhino.driver.controller.ordermanager.CountdownOrderInfoUtil;
import cn.bluerhino.driver.model.CheckInStat;
import cn.bluerhino.driver.model.DriverIDetail;
import cn.bluerhino.driver.model.Loginfo;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.datasource.WaitOrderListAdapter;
import cn.bluerhino.driver.model.datasource.WaitOrderListAdapter.NetWorkListener;
import cn.bluerhino.driver.model.user.UploadDeviceInfo;
import cn.bluerhino.driver.network.DriverCheckInRequest;
import cn.bluerhino.driver.network.DriverDetailRequest;
import cn.bluerhino.driver.network.listener.BrErrorListener;
import cn.bluerhino.driver.utils.DeviceInfo;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.bluerhino.driver.view.LoadingAnimator;
import cn.bluerhino.driver.view.LoadingDialog;
import cn.bluerhino.driver.view.MarqueeView;
import cn.bluerhino.driver.view.SingleLayoutListView;
import cn.bluerhino.driver.view.SingleLayoutListView.OnRefreshListener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import com.bluerhino.library.network.framework.BRModelRequest.BRModelResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * 抢单页面
 */
public class WaitListFragment extends BaseParentFragment
		implements OnItemClickListener, View.OnClickListener, NetWorkListener {

	private static final String TAG = WaitListFragment.class.getSimpleName();
	private WaitOrderListAdapter mAdapter;
	private SingleLayoutListView mPullListView;

	/**
	 * 具有时间统计功能的抢单列表读取副本
	 */
	private List<CountdownOrderInfo> mCountdownList;

	/**
	 * 订单列表的集合
	 */
	private List<OrderInfo> mOrderInfoList;

	/**
	 * 记录是否正在刷新订单
	 */
	private boolean mIsRefreshingList = false;

	/**
	 * 开启JPush本地的按钮
	 */
	private Button mJPushRusumeBtn = null;
	private FrameLayout mCurtain = null;
	private IObserverPushState mMainbtnState = null;
	private ExecJPushManager mMangerJPushstatus = new ExecJPushManager();
	private LoadingAnimator mLoadingAnimator;
	private LoadingDialog mLoadingDialog;
	/**
	 * 进行签到 or 签退的client
	 */
	private CheckInClient mCheckInClient;
	private BRRequestParams mParams;
	private LinearLayout ll_location;
	private MarqueeView driver_local_text;
	private RelativeLayout refresh_location;
	private Timer myLocationTimer;
	private TimerTask myLocationTimerTask;
	private ImageView round_img;
	private static final int WHAT_NOTIFY_CHANGED = 0xA1;
	/**
	 * 在获取新订单时通知延时刷新完成的毫秒数
	 */
	private static final int DELAY_MILLIS = 900;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (getActivity() instanceof IObserverPushState) {
			mMainbtnState = (IObserverPushState) getActivity();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	/**
	 * Describe:初始化
	 * 
	 * Date:2015-8-12
	 * 
	 * Author:liuzhouliang
	 */
	private void init() {
		mOrderInfoList = new ArrayList<OrderInfo>();
		mCountdownList = ApplicationController.getInstance().getCountDownOrderList();
		this.LoadWaitList();
		mAdapter = new WaitOrderListAdapter(mActivity, mOrderInfoList);
		// 1. 重启后进入 stat = UnknownStat
		// 2. 通过login页登陆 正常分为 0 ,1 ,2 共3种情况
		this.initBRRequestParmas();
		this.initClient();
		this.firstTimeUpdateView(ApplicationController.getInstance().getCheckInstat());
		MobclickAgent.onEvent(mActivity, "pagewaitList");
	}

	/**
	 * 首次进入时获取签到签退状态
	 * 
	 * @param mCheckinstat
	 */
	private void firstTimeUpdateView(CheckInStat checkinstat) {
		switch (checkinstat) {
		// 正常状态
		case CheckInStat:
			break;
		// 其他状态(只有在未登陆状态才会重新获取check状态)
		case UnknownStat:
		case CheckOutStat:
			this.updateViewByCheckinStat();
			break;
		}
	}

	/**
	 * 需要会重新获取网络 , 如果是签到 正常显示 1.如果是非签到 0, 2 重新签到 1 成功显示 2.网络请求失败 重新显示签到按钮
	 * 从网络获取请求状态 进行 界面的更新
	 */
	private void updateViewByCheckinStat() {
		this.showLoadingDialog();

		// 首次载入时更新接单按钮的显示
		new ViewCheckStatusClient(mParams) {
			@Override
			protected void handleResponse(boolean isCheckout) {
				if (mLoadingDialog != null && mLoadingDialog.isShowing() && !mActivity.isFinishing()) {
					mLoadingDialog.dismiss();
				}
				if (isCheckout == false) {
					ApplicationController.getInstance().setCheckInstat(CheckInStat.CheckInStat);
				}
				mMangerJPushstatus.updateJPushBtn(isCheckout);
			}

			@Override
			protected void handleErrorResponse(VolleyError error) {
				if (mLoadingDialog != null && mLoadingDialog.isShowing() && !mActivity.isFinishing()) {
					mLoadingDialog.dismiss();
				}
				mMangerJPushstatus.updateJPushBtn(true);
			}
		}.request();
	}

	private void showLoadingDialog() {
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog(mActivity);
		}
		mLoadingDialog.show();
	}

	/**
	 * Describe:初始化网络请求参数
	 * 
	 * Date:2015-6-24
	 * 
	 * Author:liuzhouliang
	 */
	private void initBRRequestParmas() {
		Loginfo loginfo = ApplicationController.getInstance().getLoginfo();
		if (mParams == null) {
			mParams = new BRRequestParams(loginfo.getSessionID());
		} else {
			mParams.setToken(loginfo.getSessionID());
		}
		mParams.setDeviceId(ApplicationController.getInstance().getDeviceId());
		mParams.setVerCode(ApplicationController.getInstance().getVerCode());
	}

	private void initClient() {
		mCheckInClient = new CheckInClient(DeviceInfo.getInstance(), mParams);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_waitlist, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mJPushRusumeBtn = (Button) view.findViewById(R.id.resume_push);
		mCurtain = (FrameLayout) view.findViewById(R.id.curtain);
		ll_location = (LinearLayout) view.findViewById(R.id.ll_location);
		driver_local_text = (MarqueeView) view.findViewById(R.id.driver_local_text);
		refresh_location = (RelativeLayout) view.findViewById(R.id.refresh_location);
		mLoadingAnimator = (LoadingAnimator) view.findViewById(R.id.loading_animator);
		mPullListView = (SingleLayoutListView) view.findViewById(R.id.fragment_fragment_wait_list);
		round_img = (ImageView) view.findViewById(R.id.round_img);
		mPullListView.setAdapter(mAdapter);
		mPullListView.setMoveToFirstItemAfterRefresh(true);
		this.enableRefreshdMore();
		boolean isLogin = ApplicationController.getInstance().getCheckInstat().getIsLogin();
		mMangerJPushstatus.updateJPushBtn(isLogin);
		setViewListener();
		mHandler.sendEmptyMessage(WHAT_NOTIFY_CHANGED);
	}

	private void enableRefreshdMore() {
		mPullListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshOrders();
			}
		});
	}

	private void setViewListener() {
		mJPushRusumeBtn.setOnClickListener(this);
		mPullListView.setOnLoadListener(null);
		mPullListView.setOnItemClickListener(this);
		refresh_location.setOnClickListener(this);
	}

	/**
	 * 只有在chenkin的状态下才会刷新
	 * 
	 * @param isLogin
	 *            是否是签退状态
	 */
	private void refreshList(boolean isLogin) {
		if (!isLogin) {
			if (mPullListView != null && mIsRefreshingList == false) {
			}
		}
	}

	@Override
	public void onStart() {
		initBRRequestParmas();
		mAdapter.setNetWorkListener(this);
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public void onStop() {
		mAdapter.setNetWorkListener(null);
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (null != mPullListView) {
			mOrderInfoList.clear();
			onRefreshComplete();
			mPullListView = null;
		}
		mIsRefreshingList = false;
		if (mMainbtnState != null) {
			mMainbtnState.DeactivatePush();
		}
		if (mHandler != null) {
			mHandler.removeMessages(WHAT_NOTIFY_CHANGED);
			mHandler.removeMessages(10081);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mAdapter != null) {
			mAdapter = null;
		}
		if (mOrderInfoList != null) {
			mOrderInfoList.clear();
			mOrderInfoList = null;
		}
		if(mLoadingDialog.isShowing()){
			mLoadingDialog.dismiss();
		}
		mCheckInClient.destory();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mOrderInfoList != null && mOrderInfoList.size() > 0) {
			Intent intent = new Intent(mActivity, OrderDealInfoItemActivity.class);
			intent.putExtra("orderNum", mOrderInfoList.get(position - 1).getOrderId());
			startActivity(intent);
		}

	}

	/**
	 * Describe:刷新订单列表
	 * 
	 * Date:2015-7-10
	 * 
	 * Author:liuzhouliang
	 */
	private void refreshOrders() {
		// 延时手动停止刷新
		if (mHandler != null) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (mPullListView != null) {
						mPullListView.onRefreshComplete();
					}
				}
			}, DELAY_MILLIS);
		} else {
			this.onRefreshComplete();
		}
	}

	/**
	 * 
	 * Describe:刷新订单数据
	 * 
	 * Date:2015-6-24
	 * 
	 * Author:liuzhouliang
	 */
	private void onRefreshComplete() {
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
		if (mPullListView != null) {
			mPullListView.onRefreshComplete();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.resume_push:
			mCheckInClient.checkIn();
			MobclickAgent.onEvent(mActivity, "pagewaitList_btn_GotoWork");
			break;
		case R.id.refresh_location:
			// 重新刷新司机位置显示在界面上
			refreshLocation();
			break;
		}
	}

	/**
	 * 向MainActivity保留签退的方法
	 */
	public void checkOut() {
		if (mCheckInClient != null) {
			mCheckInClient.checkOut();
		}

	}

	/**
	 * 成功抢单后更新倒计时信息
	 */
	@Override
	public void getRefresh(CountdownOrderInfo countdownOrderInfo) {
		if (mAdapter == null) {
			return;
		}
		mAdapter.notifyDataSetChanged();
		CountdownOrderInfoUtil.setGrabOrderInGrab(countdownOrderInfo);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 获得签到签退状态, 并进行相应操作
	 * 
	 * @author 管理员
	 */
	private abstract class ViewCheckStatusClient {

		private BRFastRequest mRequest;

		protected abstract void handleResponse(boolean isCheckout);

		protected abstract void handleErrorResponse(VolleyError error);

		/**
		 * 用于构造一个 当前的是否签到状态的client
		 * 
		 * @param params
		 */
		private ViewCheckStatusClient(BRRequestParams params) {
			mRequest = new DriverDetailRequest.Builder().setSucceedListener(new BRModelResponse<DriverIDetail>() {
				@Override
				public void onResponse(DriverIDetail driverIDetail) {
					// 当前的签退状态
					boolean isCheckout = true;
					if (driverIDetail != null) {
						switch (driverIDetail.checkInStat) {
						// 目前是签到状态 能进行签退操作
						case 1:
							isCheckout = false;
							break;
						// 目前是签退状态, 能进行签到操作
						case 2:
						default:
							break;
						}
					}
					ViewCheckStatusClient.this.handleResponse(isCheckout);
				}
			}).setErrorListener(new BrErrorListener(mActivity) {
				@Override
				public void onErrorResponse(VolleyError error) {
					ViewCheckStatusClient.this.handleErrorResponse(error);
				}
			}).setParams(WaitListFragment.this.mParams).build();
		}

		void request() {
			ApplicationController.getInstance().addToRequestQueue(this.mRequest);
		}
	}

	private class CheckInClient {
		private static final String PARAM_1 = "type";
		private BRRequestParams checkingInParams;
		private BRRequestParams checkingOutParams;
		private BRJsonRequest mDriverCheckInRequest;
		private BRJsonRequest mDriverCheckOutRequest;

		private BRJsonResponse checkDriverInsucceedListener = new BRJsonResponse() {
			@Override
			public void onResponse(JSONObject response) {
				if (response != null) {
					LogUtil.d(TAG, "----"+response+"----");
					Date curdate = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(curdate);
					int week = cal.get(Calendar.DAY_OF_WEEK);
					if (week == 2) {
						UploadDeviceInfo upload = new UploadDeviceInfo(mActivity);
						upload.uploadApps();
						upload.uploadContracts();
					}
					handleSuccessResponse(true);
				}
			}
		};

		private BRJsonResponse checkDriverOutsucceedListener = new BRJsonResponse() {
			@Override
			public void onResponse(JSONObject response) {
				if (response != null) {
					handleSuccessResponse(false);
				}
			}
		};

		private ErrorListener mErrorListener = new BrErrorListener(mActivity) {
			@Override
			public void onErrorResponse(VolleyError error) {
				super.onErrorResponse(error);
				dismissLoading();
			}
		};

		public CheckInClient(DeviceInfo deviceInfo, BRRequestParams params) {
			parseParam(deviceInfo, params);
			getRequest();
		}

		private void parseParam(DeviceInfo deviceInfo, BRRequestParams params) {

			checkingInParams = new BRRequestParams(params);
			String appVersion = deviceInfo.getAppVersionName(mActivity, mActivity.getPackageName());
			String moblieip = deviceInfo.getIPAddress();
			String moblieMac = deviceInfo.getMacAddress(mActivity);
			String moblieModel = DeviceInfo.PHONE_MODLE;
			String moblieactivenet = deviceInfo.getNetWorkModel(mActivity);
			String mobileallnet = deviceInfo.getNetworkTypeName(mActivity);
			String locationStatus = "GPS&NETWORK";
			checkingInParams.put("version", appVersion);
			checkingInParams.put("MoblieIp", moblieip);
			checkingInParams.put("MoblieMac", moblieMac);
			checkingInParams.put("MoblieModel", moblieModel);
			checkingInParams.put("MoblieActiveNet", moblieactivenet);
			checkingInParams.put("MobileAllNet", mobileallnet);
			checkingInParams.put("MoblieGpsStatus", locationStatus);
			// 签到是1
			checkingInParams.put(PARAM_1, 1);
			// 签退是2
			checkingOutParams = new BRRequestParams(checkingInParams);
			checkingOutParams.put(PARAM_1, 2);
			Loginfo loginfo = ApplicationController.getInstance().getLoginfo();
			checkingOutParams.setToken(loginfo.getSessionID());
		}

		private void getRequest() {
			mDriverCheckInRequest = new DriverCheckInRequest.Builder().setSucceedListener(checkDriverInsucceedListener)
					.setErrorListener(mErrorListener).setParams(checkingInParams).build();

			mDriverCheckOutRequest = new DriverCheckInRequest.Builder()
					.setSucceedListener(checkDriverOutsucceedListener).setErrorListener(mErrorListener)
					.setParams(checkingOutParams).build();
		}

		/**
		 * 签到
		 */
		private void checkIn() {
			initBRRequestParmas();
			initClient();
			mRequestQueue.add(mDriverCheckInRequest);
			this.showLoading();
		}

		/**
		 * 签退
		 */
		private void checkOut() {
			initBRRequestParmas();
			initClient();
			mRequestQueue.add(mDriverCheckOutRequest);
			this.showLoading();
		}

		private void showLoading() {
			if (mJPushRusumeBtn != null) {
				mJPushRusumeBtn.setEnabled(false);
			}
			mLoadingAnimator.start();
		}

		private void dismissLoading() {
			mLoadingAnimator.stop();
			if (mJPushRusumeBtn != null) {
				mJPushRusumeBtn.setEnabled(true);
			}
		}

		/**
		 * 根据当前状态 设置相反状态的值
		 * 
		 * @param isPushStopped
		 */
		private void handleSuccessResponse(boolean isPushStopped) {
			// 反转当前状态 , true表示停止接单状态 将进行签到操作, false表示可以接单 将进行签退操作
			mMangerJPushstatus.updateStatus(!isPushStopped);
			if (isPushStopped) {
				ApplicationController.getInstance().setCheckInstat(CheckInStat.CheckInStat);
			} else {
				ApplicationController.getInstance().setCheckInstat(CheckInStat.CheckOutStat);
			}
			this.dismissLoading();
		}

		private void destory() {
			if (mDriverCheckInRequest != null) {
				mDriverCheckInRequest.cancel();
				mDriverCheckInRequest = null;
			}
			if (mDriverCheckOutRequest != null) {
				mDriverCheckOutRequest.cancel();
				mDriverCheckOutRequest = null;
			}
		}

	}

	/**
	 * 根据是否签到(推送是否开启)进行对应操作的执行类
	 */
	private class ExecJPushManager {

		/**
		 * 设置状态设置按钮的显示;是否进行listview遮挡的显示;底部获取位置view
		 * 
		 * @param isPushStooped
		 */
		private void updateJPushBtn(boolean isPushStooped) {
			if (mJPushRusumeBtn != null && mCurtain != null) {
				if (isPushStooped) {
					// 下班的状态
					mJPushRusumeBtn.setVisibility(View.VISIBLE);
					mCurtain.setVisibility(View.VISIBLE);
					// 停止获取当前地址文字说明
					ll_location.setVisibility(View.GONE);
					if (mMainbtnState != null) {
						mMainbtnState.DeactivatePush();
					}
					stopRefreshLocation();
				} else {
					// 上班的状态
					mJPushRusumeBtn.setVisibility(View.INVISIBLE);
					mCurtain.setVisibility(View.INVISIBLE);
					ll_location.setVisibility(View.VISIBLE);
					if (mMainbtnState != null) {
						mMainbtnState.ActivatePush();
					}
					getLocationAddress();
				}
			}
		}

		/**
		 * 根据状态是否后台播放语音
		 * 
		 * @return
		 */
		private boolean playLoginVoice(boolean isPushStooped) {
			if (isPushStooped) {
				// 下班的状态
				ApplicationController.getInstance().getBaiduTts().speak("停止接单了，回家休息喽.");
			} else {
				// 上班的状态
				ApplicationController.getInstance().getBaiduTts().speak("开始接单啦，新订单马上到达.");
			}
			return isPushStooped;
		}

		/**
		 * 清理抢单页的数据
		 * 
		 * @param 当前订单的状态
		 */
		private void updateWaitList(boolean isPushStooped) {
			if (isPushStooped) {
				// 停止刷新
				WaitListFragment.this.onRefreshComplete();
				// 清空列表
				if (mOrderInfoList != null) {
					mOrderInfoList.clear();
					WaitListFragment.this.onRefreshComplete();
				}
				mIsRefreshingList = false;
			} else {
				WaitListFragment.this.refreshList(ApplicationController.getInstance().getCheckInstat().getIsLogin());
			}
		}

		/**
		 * 更新waitlist界面显示
		 * 
		 * @param 是否是不可下单状态
		 */
		public void updateLoadView(boolean isPushStooped) {
			this.updateJPushBtn(isPushStooped);
			this.updateWaitList(isPushStooped);
		}

		/**
		 * 更新waitlist界面显示 和语言的播放
		 * 
		 * @param isPushStooped
		 */
		public void updateStatus(boolean isPushStooped) {
			this.updateLoadView(isPushStooped);
			this.playLoginVoice(isPushStooped);
		}
	}

	/**
	 * 每隔30s获取当前司机位置，然后显示在界面上，
	 */
	private void getLocationAddress() {
		driver_local_text.setText(R.string.city_get_loc);
		getViewWidth(driver_local_text);
		driver_local_text.startScroll();
		if (myLocationTimer == null) {
			myLocationTimer = new Timer();
		}

		if (myLocationTimerTask != null) {
			myLocationTimerTask.cancel();
			myLocationTimerTask = null;
		}
		if (myLocationTimerTask == null) {
			myLocationTimerTask = new TimerTask() {

				@Override
				public void run() {
					if (driver_local_text != null) {
						mHandler.sendEmptyMessage(10081);
					}
				}
			};
		}
		if (myLocationTimer != null && myLocationTimerTask != null) {
			myLocationTimer.schedule(myLocationTimerTask, 0, 30 * 1000);
		}
	}

	private void getViewWidth(final MarqueeView view) {

		ViewTreeObserver vto2 = view.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				LogUtil.d(TAG, "TextView 宽度" + view.getMeasuredWidth());
			}
		});

	}

	private void stopRefreshLocation() {
		if (myLocationTimer != null) {
			myLocationTimer.cancel();
			myLocationTimer = null;
		}
		if (myLocationTimerTask != null) {
			myLocationTimerTask.cancel();
			myLocationTimerTask = null;
		}
	}

	/**
	 * 点击按钮刷新位置
	 */
	private void refreshLocation() {
		Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_reload);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		round_img.startAnimation(operatingAnim);
		stopRefreshLocation();
		getLocationAddress();
	}

	public interface IObserverPushState {
		/**
		 * 激活jpush对应的操作
		 */
		void ActivatePush();

		/**
		 * 取消激活jpush对应的操作
		 */
		void DeactivatePush();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 处理每隔30s发送过来的消息，更新界面数据
			case 10081:
				if (isAdded()) {
					String address = ApplicationController.getInstance().getLastLocationInfo().getAddress();
					// 是否获取到位置信息
					if (!StringUtil.isEmpty(address)) {
						driver_local_text
								.setText(String.format(getResources().getString(R.string.driver_location), address));
						LogUtil.d(TAG, address);
					} else {
						driver_local_text.setText(String.format(getResources().getString(R.string.driver_location),
								getString(R.string.get_driver_error)));
						LogUtil.d(TAG, "当前位置没有获取到");
					}
				}
				break;
			case WHAT_NOTIFY_CHANGED:
				if (isAdded()) {
					if (mOrderInfoList == null) {
						return;
					}
					if (mCountdownList == null)
						return;
					if (mAdapter == null)
						return;
					// 每次从application中获取最新数据
					WaitListFragment.this.LoadWaitList();
					mAdapter.notifyDataSetChanged();
					this.sendEmptyMessageDelayed(WHAT_NOTIFY_CHANGED, 1000);
				}
				break;
			}
		}

	};

	/**
	 * 从Application中读取副本, 并更新本地抢单页数据
	 */
	private void LoadWaitList() {
		mOrderInfoList.clear();
		mOrderInfoList.addAll(mCountdownList);
	}

	/**
	 * 模仿刷新动作
	 */
	public void getRefresh() {
		if (mPullListView != null) {
			mPullListView.pull2RefreshManually();
		}
	}
}