package cn.bluerhino.driver.controller.activity;

import java.util.Timer;
import java.util.TimerTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRFLAG;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.fragment.CurrentOrderFragment;
import cn.bluerhino.driver.controller.fragment.WaitListFragment;
import cn.bluerhino.driver.controller.receiver.CancledOrder;
import cn.bluerhino.driver.controller.receiver.JPushReceiver;
import cn.bluerhino.driver.controller.receiver.ModifyedOrder;
import cn.bluerhino.driver.controller.receiver.RearrangedOrder;
import cn.bluerhino.driver.controller.receiver.SnatchorderSucceed;
import cn.bluerhino.driver.helper.ServiceHelper;
import cn.bluerhino.driver.helper.url.URLProcessor;
import cn.bluerhino.driver.utils.AppRunningInfor;
import cn.bluerhino.driver.utils.BaiduNaviStatus;
import cn.bluerhino.driver.utils.ConstantsManager;
import cn.bluerhino.driver.utils.ContactDriverManager;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.view.framework.FragmentTabAdapter;
import cn.bluerhino.driver.view.framework.FragmentTabComponent;
import cn.bluerhino.driver.view.framework.FragmentTabContext;
import cn.bluerhino.driver.view.framework.FragmentTabHost;
import cn.bluerhino.driver.view.framework.FragmentTabInfo;
import cn.bluerhino.driver.view.tab.MainTabAdapter;
import cn.jpush.android.api.JPushInterface;
import com.baidu.navisdk.util.common.StringUtils;
import com.bluerhino.library.utils.ToastUtil;
import com.bluerhino.library.utils.WeakHandler;
import com.umeng.analytics.MobclickAgent;

/**
 * Describe:主页面
 * 
 * Date:2015-6-26
 * 
 * Author:liuzhouliang
 */
public class MainActivity extends BaseParentActivity implements
		FragmentTabComponent, WaitListFragment.IObserverPushState,
		View.OnClickListener {
	private static String TAG = MainActivity.class.getSimpleName();

	@InjectView(R.id.myactionbar)
	RelativeLayout myActionBar;
	@InjectView(R.id.left_text)
	TextView mLeftTitle;
	@InjectView(R.id.center_title)
	TextView mTitle;
	@InjectView(R.id.right_text)
	TextView mRightText;

	private FragmentTabHost mFragmentTabHost;
	private MainTabAdapter mFragmentTabAdapter;
	/**
	 * 顶部右侧标题说明
	 */
	private String[] mBarTextArr;
	private FragmentTabContext mManagerContext;
	private Timer isAPPRunningForegroundTimer;
	private TimerTask isAPPRunningForegroundTimerTask;
	private H mHandler = new H(this);
	private ProcessPageJump mPageJump = new ProcessPageJump();
	private BroadcastReceiver mReceiver;
	private ShowLoadingThread showThread;
	private MainURLProcessor mURLProcessor;
	public static final int BACK_CURRERTLIST = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (super.checkUserIsLogin()) {
			init();
			loadView();
			processJmupTabPage();
		}
	}

	/**
	 * Describe:初始化
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */
	private void init() {
		this.initView();
		this.initMemeber();
		this.setHandler();
		this.startCustomService();
		this.initNaviEngine();
		startAdvertisementTimer();
		registerLocalReceiver();
	}

	private void initView() {
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
	}

	private void initMemeber() {
		mFragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mFragmentTabAdapter = new MainTabAdapter(mContext);
		mBarTextArr = mRes.getStringArray(R.array.bar_text_array);
		mManagerContext = new FragmentTabContext(this, mFragmentTabHost);
		mManagerContext.setup();
		mManagerContext.setOnTabChangedListener(this);
		mURLProcessor = MainURLProcessor.getInstance(this);
	}

	private void setHandler() {
		if (ApplicationController.getInstance().getOrderListBackHandler() == null) {
			ApplicationController.getInstance().setOrderListBackHandler(
					mHandler);
		}
	}

	private void startCustomService() {
		ServiceHelper.startLoc(this);
		String alias = ApplicationController.getInstance().getLoginfo()
				.getUserName();
		ServiceHelper.initJPushService(getApplicationContext(), alias,
				ApplicationController.testSwitch);
		JPushInterface.resumePush(getApplicationContext());
	}

	/**
	 * Describe:注册广播，接受广播更新当前TAB位置
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */
	private void registerLocalReceiver() {
		if (mReceiver == null) {
			/**
			 * 接受到推送后，更新当前TAB位置
			 */
			class RefreshBroadcastReceiver extends BroadcastReceiver {
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (!TextUtils.isEmpty(action)) {
						if (JPushReceiver.ACTION_WAITLIST_UPDATE.equals(action)) {
							mHandler.sendEmptyMessage(H.JUMPTO_WAITLIST);
						} else if (JPushReceiver.ACTION_CURLIST_UPDATE
								.equals(action)) {
							mHandler.sendEmptyMessage(H.JUMPTO_CURRENTLIST);
						}
					}
				}
			}
			mReceiver = new RefreshBroadcastReceiver();
		}
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(JPushReceiver.ACTION_WAITLIST_UPDATE);
		intentFilter.addAction(JPushReceiver.ACTION_CURLIST_UPDATE);
		ApplicationController.getInstance().getLocalBroadcastManager()
				.registerReceiver(mReceiver, intentFilter);

	}

	private void initNaviEngine() {
		BaiduNaviStatus.getInstance().initEngine(this);
	}

	/**
	 * Describe:初始化视图
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */
	private void loadView() {
		mTitle.setText(R.string.main_tab_item_wait);
		mLeftTitle.setText(R.string.main_act_stoporders);
		mLeftTitle.setOnClickListener(this);
	}

	/**
	 * 
	 * Describe:启动定时任务，当APP后台运行超过一段时间后，进入APP会显示广告页面
	 * 
	 * Date:2015-7-21
	 * 
	 * Author:liuzhouliang
	 */
	private void startAdvertisementTimer() {
		/**
		 * 定时任务检测APP是否在后台运行
		 */
		if (isAPPRunningForegroundTimer == null) {
			isAPPRunningForegroundTimer = new Timer();

		}
		if (isAPPRunningForegroundTimerTask == null) {
			isAPPRunningForegroundTimerTask = new TimerTask() {

				@Override
				public void run() {

					if (AppRunningInfor.isRunningForeground(mContext)) {
						/**
						 * 运行在前台，停止是否显示广告页面的定时任务
						 */
						// LogUtil.d(TAG, "前台运行==============");
						ApplicationController.getInstance().isAPPRunningForeground = true;

						if (ApplicationController.getInstance().isShowLoadingView) {
							/**
							 * 展示广告页面
							 */
							// LogUtil.d(TAG, "前台运行====显示广告页面");
							Message message = Message.obtain();
							message.what = 2001;
							mHandler.sendMessage(message);
							ApplicationController.getInstance().isShowLoadingView = false;
							if (showThread != null) {
								showThread.interrupt();
								LogUtil.d(
										TAG,
										"前台运行====中断线程"
												+ showThread.isInterrupted());
								showThread = null;

							}
						}
					} else {
						/**
						 * 运行在后台，启动定时任务判断是否显示广告页面===========
						 */
						// LogUtil.d(TAG, "后台运行===================");
						ApplicationController.getInstance().isAPPRunningForeground = false;
						if (showThread == null) {
							LogUtil.d(TAG, "后台运行===================启动线程");
							showThread = new ShowLoadingThread();
							showThread.start();

						}

					}

				}
			};
		}
		if (isAPPRunningForegroundTimer != null
				&& isAPPRunningForegroundTimerTask != null) {
			isAPPRunningForegroundTimer.schedule(
					isAPPRunningForegroundTimerTask, 0, 1000);
		}

	}

	/**
	 * 
	 * Describe:线程处理延迟显示启动广告页面
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */
	private class ShowLoadingThread extends Thread {

		@Override
		public void run() {
			try {
				sleep(ConstantsManager.ADVERTISEMENT_SHOW_INTERVAL);
				LogUtil.d(TAG, "睡眠后可以显示广告");
				if (!AppRunningInfor.isRunningForeground(mContext)) {
					/**
					 * 运行在后台
					 */
					ApplicationController.getInstance().isShowLoadingView = true;
				} else {
					/**
					 * 运行前台
					 */
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			super.run();
		}
	}

	/**
	 * Describe:展示广告页面
	 * 
	 * Date:2015-7-22
	 * 
	 * Author:liuzhouliang
	 */
	private void showLoadingView() {
		Intent intent = new Intent(mContext, AdvertisementActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		intent.putExtra(ConstantsManager.FROM_WHERE, "MainActivity");
		mContext.startActivity(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			setIntent(intent);
			processJmupTabPage();
		}
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		this.unRegisterLocalReceiver();
		super.onDestroy();
	}

	/**
	 * 处理跳转页面
	 */
	private void processJmupTabPage() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		Uri data = intent.getData();
		// 1.通过链接进入
		if (data != null) {
			int index = mURLProcessor.getJumpUrlIndex(intent.getData());
			int size = mFragmentTabAdapter.getCount();
			if (index >= size || index < 0) {
				ToastUtil.showToast(getApplicationContext(),
						R.string.main_tab_jump_failure);
				return;
			}
			mPageJump.JumpToIndexList(index);
		} else {
			// 2. 极光推送进入
			if (intent.getFlags() == BRFLAG.PUSH_ACTIVITY_FLAG) {
				// 订单变更: 当前订单页
				if (intent.hasExtra(ModifyedOrder.TAG)) {
					mPageJump.JumpToCurrentList();
				}
				// 订单改派RearrangedOrder : 会在当前订单停留5分钟，之后被回收到历史订单中
				else if (intent.hasExtra(RearrangedOrder.TAG)) {
					mPageJump.JumpToCurrentList();
				}
				// 订单取消: 跳转到当前订单
				else if (intent.hasExtra(CancledOrder.TAG)) {
					mPageJump.JumpToCurrentList();
				}
				// 抢单成功: 跳转到当前订单
				else if (intent.hasExtra(SnatchorderSucceed.TAG)) {
					mPageJump.JumpToCurrentList();
				}
			}
			// 3. 直接进入
			else {
				// 从完成订单跳转到当前页
				if (intent.hasExtra(ConfirmOrderInfoActivity.TAG)) {
					mPageJump.JumpToCurrentList();
				}
				// 主动进入抢单成功: 跳转到当前订单
				else if (intent.hasExtra(SnatchorderSucceed.TAG)) {
					mPageJump.JumpToCurrentList();
				}
			}
		}
	}

	@Override
	public void setup() {
		mFragmentTabHost.setup(getBaseContext(), getSupportFragmentManager(),
				R.id.realtabcontent);
	}

	@Override
	public FragmentTabAdapter getTabDataSource() {
		return mFragmentTabAdapter;
	}

	/**
	 * 标签切换回调
	 */
	@Override
	public void onTabChanged(String tabId) {
		int size = mFragmentTabAdapter.getCount();
		FragmentTabInfo info = null;
		int index = -1;
		for (int i = 0; i < size; i++) {
			info = mFragmentTabAdapter.getItem(i);
			if (TextUtils.equals(info.getTabId(), tabId)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return;
		}
		if (index < size - 1) {
			/**
			 * 非个人信息页面标题栏设置
			 */
			myActionBar.setBackgroundColor(Color.parseColor("#00A4E3"));
			mTitle.setTextColor(Color.WHITE);
			mRightText.setTextColor(Color.WHITE);
		} else {
			/**
			 * 个人信息页面标题栏设置
			 */
			myActionBar.setBackgroundColor(Color.WHITE);
			mTitle.setTextColor(Color.parseColor("#4F5F6F"));
			mRightText.setTextColor(Color.parseColor("#4F5F6F"));
		}
		mTitle.setText(info.getTitleResID());
		mRightText.setText(mBarTextArr[index]);
		View.OnClickListener listener = null;
		switch (index) {
		case 0:
			/**
			 * 抢单页面
			 */
			mRightText.setVisibility(View.GONE);
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 错过订单
					startActivity(new Intent(mContext,
							MissedOrderActivity.class));
				}
			};
			break;
		case 1:
			/**
			 * 当前订单
			 */
			mLeftTitle.setVisibility(View.GONE);
			mRightText.setVisibility(View.VISIBLE);
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 历史订单
					startActivity(new Intent(mContext,
							HistoryOrderActivity.class));
					MobclickAgent.onEvent(mContext,
							"pageCurrList_btn_hisorderlist");
				}
			};
			break;
		case 2:
			/**
			 * 活动页面
			 */
			mLeftTitle.setVisibility(View.GONE);
			mRightText.setVisibility(View.GONE);
			if (!StringUtils.isEmpty(ApplicationController.getInstance()
					.getTabTitle())) {
				mTitle.setText(ApplicationController.getInstance()
						.getTabTitle());
			} else {
				mTitle.setText("");
			}
			break;
		case 3:
			/**
			 * 个人信息
			 */
			mLeftTitle.setVisibility(View.GONE);
			mRightText.setVisibility(View.VISIBLE);
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 联系运控
					ContactDriverManager.getIntance().contactDriverManager(
							mContext);
					// 统计: 个人信息页 - 客服电话
					MobclickAgent.onEvent(mContext,
							"pageUserInfo_btn_Customer_service");
				}
			};
			break;
		}
		mRightText.setOnClickListener(listener);
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	/**
	 * 显示停止接单按钮
	 */
	@Override
	public void ActivatePush() {
		if (mLeftTitle != null) {
			mLeftTitle.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏停止接单按钮
	 */
	@Override
	public void DeactivatePush() {
		if (mLeftTitle != null) {
			mLeftTitle.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_text:
			WaitListFragment waitFragment = (WaitListFragment) mPageJump
					.getWaitFragment();
			if (waitFragment != null) {
				waitFragment.checkOut();
				MobclickAgent.onEvent(this, "pagewaitList_btn_ComeoffWork");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Describe:解除广播
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */
	private void unRegisterLocalReceiver() {
		if (mReceiver != null) {
			ApplicationController.getInstance().getLocalBroadcastManager()
					.unregisterReceiver(mReceiver);
		}
	}

	private class ProcessPageJump {

		Fragment getFragmentByOId(int oId) {
			return getSupportFragmentManager().findFragmentByTag(
					mFragmentTabAdapter.getItem(oId).getTabId());
		}

		Fragment getWaitFragment() {
			return this.getFragmentByOId(0);
		}

		Fragment getCurrentFragment() {
			return this.getFragmentByOId(1);
		}

		/*
		 * 模拟刷新抢单页(手动模仿刷新动作)
		 */
		void refreshWaitList() {
			Fragment fg = this.getWaitFragment();
			if (fg != null) {
				((WaitListFragment) fg).getRefresh();
			}
		}
		
		/*
		 * 刷新当前页
		 */
		void refreshCurrentList() {
			Fragment fg = this.getCurrentFragment();
			if (fg != null) {
				((CurrentOrderFragment) fg).getRefresh();
			}
		}

		/**
		 * 跳转到对应index页面
		 * 
		 * @param index
		 *            索引
		 */
		void JumpToIndexList(int index) {
			if (mManagerContext != null) {
				if (mManagerContext.getCurrentTab() != index) {
					mManagerContext.setCurrentTab(index);
				}
			}
		}

		// 跳到抢单页
		void jumpToWaitList() {
			if (mManagerContext != null) {
				mManagerContext.setCurrentTab(0);
			}
		}

		// 跳到并刷新当前页
		private void JumpToCurrentList() {
			if (mManagerContext != null) {
				if (mManagerContext.getCurrentTab() == 1) {
					this.refreshCurrentList();
				} else {
					mManagerContext.setCurrentTab(1);
				}
			}
		}
	}

	private static class MainURLProcessor extends URLProcessor {

		private static MainURLProcessor sInstance = null;

		private MainURLProcessor(Context context) {
			super(context, R.array.array_main_url);
		}

		static MainURLProcessor getInstance(Context context) {
			if (sInstance == null) {
				synchronized (MainURLProcessor.class) {
					if (sInstance == null) {
						sInstance = new MainURLProcessor(context);
					}
				}
			}
			return sInstance;
		}
	}

	private static class H extends WeakHandler<MainActivity> {
		/**
		 * 跳到抢单页并刷新抢单页
		 */
		static final int JUMPTO_WAITLIST = 11;
		/**
		 * 跳到当前页并刷新当前页
		 */
		static final int JUMPTO_CURRENTLIST = 12;

		static final int SHOW_LOADING_DIALOG = 2001;

		H(MainActivity reference) {
			super(reference);
		}

		@Override
		public void handleMessage(Message msg) {
			if (getReference() != null) {
				switch (msg.what) {
				case BACK_CURRERTLIST:
					getReference().mPageJump.refreshCurrentList();
					break;
				case H.JUMPTO_WAITLIST:
					getReference().mPageJump.jumpToWaitList();
					getReference().mPageJump.refreshWaitList();
					break;
				case H.JUMPTO_CURRENTLIST:
					getReference().mPageJump.JumpToCurrentList();
					break;
				case SHOW_LOADING_DIALOG:
					/**
					 * 显示启动页面
					 */
					getReference().showLoadingView();
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * 设置页面的头和底部标签
	 * 
	 * @param title
	 */
	public void setTitle() {
		if (!StringUtils.isEmpty(ApplicationController.getInstance()
				.getTabTitle())) {
			mTitle.setText(ApplicationController.getInstance().getTabTitle());
			// 获取当前界面底部标签
			TextView view = (TextView) mFragmentTabHost.getCurrentTabView()
					.findViewById(R.id.main_tab_item_text);
			view.setText(ApplicationController.getInstance().getTabTitle());
		}
	}

}