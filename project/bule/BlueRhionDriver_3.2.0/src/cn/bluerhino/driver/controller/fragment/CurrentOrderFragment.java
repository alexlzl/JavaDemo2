package cn.bluerhino.driver.controller.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRAction;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.AppointOrderRemindDialogActivity;
import cn.bluerhino.driver.controller.activity.OrderFlowActivity;
import cn.bluerhino.driver.controller.receiver.JPushReceiver;
import cn.bluerhino.driver.helper.OrderInfoHelper;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.datasource.CurrentOrderListAdapter;
import cn.bluerhino.driver.network.CurrentOrderListRequest;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.bluerhino.driver.view.SingleLayoutListView;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRModelListRequest.BRModelListResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.bluerhino.library.utils.WeakHandler;
import com.umeng.analytics.MobclickAgent;

/**
 * 当前订单
 * 
 * @author Administrator
 * 
 */
public class CurrentOrderFragment extends BaseParentFragment implements OnItemClickListener {
	private static final String TAG = CurrentOrderFragment.class.getSimpleName();
	private List<OrderInfo> mOrderInfoList = new ArrayList<OrderInfo>();;
	private H mHandler;
	private SingleLayoutListView mPullListView;
	private static CurrentOrderListAdapter mAdapter;
	private BroadcastReceiver mPushReceiver;
	private boolean mIsRefreshingList = false;
	private BRFastRequest mRequest;
	private Timer appointOrderRemindTimer;
	private TimerTask appointOrderRemindTimerTask;
	private SparseBooleanArray stateArray;
	private static Timer changeOrderTimer;
	private static TimerTask changeOrderTimerTask;

	private static class H extends WeakHandler<CurrentOrderFragment> {

		// private static final int MSG_NEED_COUNT = 1;

		public H(CurrentOrderFragment fragment) {
			super(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case MSG_NEED_COUNT:
			// if (getReference() != null) {
			// getReference().refreshOrders();
			// sendEmptyMessageDelayed(MSG_NEED_COUNT, 1000 * 60);
			// }
			// break;

			case 200001:
				/**
				 * 处理缓存订单过期
				 */
				LogUtil.d(TAG, "处理移除过期缓存订单消息");
				int orderId = msg.getData().getInt("orderId");
				getReference().removeOutTimeOrder(orderId);
				break;
			case 200002:
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
				break;
			case 200003:
				/**
				 * 处理订单状态更改消息
				 */
				LogUtil.d(TAG, "处理订单状态更改消息");
				if (!StringUtil.isEmpty(msg.getData().getString("orderId"))) {
					int orderNum = Integer.parseInt(msg.getData().getString("orderId"));
					getReference().addChangeOrder(orderNum);

				}
				break;
			}
		}
	}

	/**
	 * 
	 * Describe:刷新订单状态改变的订单
	 * 
	 * Date:2015-9-8
	 * 
	 * Author:liuzhouliang
	 */
	private void addChangeOrder(int orderNum) {
		int size = mOrderInfoList.size();

		for (int i = 0; i < size; i++) {
			OrderInfo order = mOrderInfoList.get(i);
			if (order.getOrderNum() == orderNum) {
				ApplicationController.getInstance().getmCacheOrderList().add(order);
				if (ApplicationController.getInstance().getmCacheOrderList() != null) {
					LogUtil.d(TAG, "缓存后的订单" + ApplicationController.getInstance().getmCacheOrderList().toString());
				} else {
					LogUtil.d(TAG, "缓存当前订单为空");
				}
				List<OrderInfo> cacheOrderList = ApplicationController.getInstance().getmCacheOrderList();
				ApplicationController.getInstance().setmCacheOrderList(cacheOrderList);
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
				return;
			}
		}

	}

	/**
	 * 
	 * Describe:移除超过缓存期限的订单
	 * 
	 * Date:2015-9-8
	 * 
	 * Author:liuzhouliang
	 */
	private void removeOutTimeOrder(int orderNum) {
		LogUtil.d(TAG, "移除缓存订单==orderNum==" + orderNum);
		/**
		 * 移除当前订单中的数据
		 */
		int currentOrderSize = mOrderInfoList.size();
		if (currentOrderSize > 0) {
			for (int i = 0; i < currentOrderSize; i++) {
				OrderInfo order = mOrderInfoList.get(i);
				if (order.getOrderNum() == orderNum) {
					mOrderInfoList.remove(i);
					removeCacheOrder(orderNum);
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}

					return;
				}
			}
		}

	}

	/**
	 * 
	 * Describe:移除缓存中的数据
	 * 
	 * Date:2015-9-8
	 * 
	 * Author:liuzhouliang
	 */
	private void removeCacheOrder(int orderNum) {
		int cacheOrderSize = ApplicationController.getInstance().getmCacheOrderList().size();
		if (cacheOrderSize > 0) {
			for (int i = 0; i < cacheOrderSize; i++) {
				OrderInfo order = ApplicationController.getInstance().getmCacheOrderList().get(i);
				if (order.getOrderNum() == orderNum) {

					List<OrderInfo> cacheOrderInfos = ApplicationController.getInstance().getmCacheOrderList();
					cacheOrderInfos.remove(i);
					ApplicationController.getInstance().setmCacheOrderList(cacheOrderInfos);
					LogUtil.d(TAG, "更新缓存中数据后的数据" + ApplicationController.getInstance().getmCacheOrderList().toString());
					return;
				}
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	/**
	 * 
	 * Describe:初始化
	 * 
	 * Date:2015-8-6
	 * 
	 * Author:liuzhouliang
	 */
	private void init() {
		mHandler = new H(this);
		// 注册全局消息处理
		ApplicationController.getInstance().setGloablHandler(mHandler);
		mAdapter = new CurrentOrderListAdapter(mActivity, mOrderInfoList);
		initDataRequest();
		startAppointOrderRemind();
		MobclickAgent.onEvent(mActivity, "pageCurrList");
	}

	/**
	 * 
	 * Describe:初始化数据请求
	 * 
	 * Date:2015-8-6
	 * 
	 * Author:liuzhouliang
	 */
	private void initDataRequest() {

		BRModelListResponse<List<OrderInfo>> succesListResponse = new BRModelListResponse<List<OrderInfo>>() {
			@Override
			public void onResponse(List<OrderInfo> modelList) {
				if (mPullListView != null && modelList != null && mOrderInfoList != null) {
					mOrderInfoList.clear();
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}
					if (modelList.size() == 0) {
						/**
						 * 获取数据长度为0状态下
						 */
						if (ApplicationController.getInstance().getmCacheOrderList() != null
								&& ApplicationController.getInstance().getmCacheOrderList().size() > 0) {
							List<OrderInfo> cacheCurrentOrderInfos = ApplicationController.getInstance()
									.getmCacheOrderList();

							LogUtil.d(TAG, "长度==0获取缓存的订单=====" + cacheCurrentOrderInfos.toString());
							mOrderInfoList.addAll(cacheCurrentOrderInfos);
						} else {
							ToastUtil.showToast(mActivity, R.string.cur_order_nodata);
						}

					} else {
						/**
						 * 获取数据长度非0的状态
						 */
						mOrderInfoList.addAll(modelList);
						if (ApplicationController.getInstance().getmCacheOrderList() != null
								&& ApplicationController.getInstance().getmCacheOrderList().size() > 0) {
							List<OrderInfo> cacheCurrentOrderInfos = ApplicationController.getInstance()
									.getmCacheOrderList();

							LogUtil.d(TAG, "长度===" + ApplicationController.getInstance().getmCacheOrderList().size()
									+ "获取缓存的订单=====" + cacheCurrentOrderInfos.toString());
							mOrderInfoList.addAll(cacheCurrentOrderInfos);
						}

						mAdapter.setNewData(mOrderInfoList);
					}
					onRefreshComplete();

				}
				mIsRefreshingList = false;
			}
		};

		ErrorListener errorListener = new VolleyErrorListener(mActivity) {
			public void onErrorResponse(VolleyError error) {
				CurrentOrderFragment.this.onRefreshComplete();
				mIsRefreshingList = false;
			};
		};

		BRRequestParams params = new BRRequestParams(ApplicationController.getInstance().getLoginfo().getSessionID());
		params.setDeviceId(ApplicationController.getInstance().getDeviceId());
		params.setVerCode(ApplicationController.getInstance().getVerCode());
		params.put("orderId", 0);// 获取所有当前订单
		mRequest = new CurrentOrderListRequest.Builder().setSucceedListener(succesListResponse)
				.setErrorListener(errorListener).setParams(params).build();
	}

	/**
	 * 
	 * Describe:定时服务，预约订单开始前1个小时进行提醒
	 * 
	 * Date:2015-8-6
	 * 
	 * Author:liuzhouliang
	 */
	private void startAppointOrderRemind() {
		if (stateArray == null) {
			stateArray = new SparseBooleanArray();
		}
		if (appointOrderRemindTimer == null) {
			appointOrderRemindTimer = new Timer();
		}
		if (appointOrderRemindTimerTask == null) {
			appointOrderRemindTimerTask = new TimerTask() {
				@Override
				public void run() {
					if (mOrderInfoList != null && mOrderInfoList.size() > 0) {
						int size = mOrderInfoList.size();
						for (int i = 0; i < size; i++) {
							if (OrderInfoHelper.getOrderType(mOrderInfoList.get(i))) {
								/**
								 * 预约订单
								 */
								// 预约时间
								long appointmentTime = mOrderInfoList.get(i).getTransTime();
								// 当前时间
								long currentTime = System.currentTimeMillis() / 1000;
								// 时间差:分
								long diffTime = (appointmentTime - currentTime) / 60;
								LogUtil.d(TAG, "预约时间" + appointmentTime + "当前时间" + currentTime + "时间差" + diffTime);
								// 且订单状态<3000
								if (diffTime < 60 && diffTime > 0 && mOrderInfoList.size() > 0
										&& mOrderInfoList.get(i).getOrderFlag() < OrderInfo.OrderState.WAITSERVICE) {
									/**
									 * 符合提醒条件
									 */
									if (isRemindOrder(mOrderInfoList.get(i).getOrderId())) {
										/**
										 * 被提醒过
										 */
										LogUtil.d(TAG,
												"有符合提醒订单====已经提醒过==orderid==" + mOrderInfoList.get(i).getOrderId());
										break;
									} else {
										LogUtil.d(TAG,
												"有符合提醒订单====未进行提醒==orderid==" + mOrderInfoList.get(i).getOrderId());
										stateArray.put(mOrderInfoList.get(i).getOrderId(), true);
										ApplicationController.getInstance().getBaiduTts()
												.speak("您有一个预约订单即将开始服务，请准时到达收货地 ");
										startMessageDialogtActivity(i);
									}
								}
							}
						}
					}
				}
			};
		}
	}

	/**
	 * Describe:判断订单是否被提醒过
	 * 
	 * Date:2015-7-9
	 * 
	 * Author:liuzhouliang
	 */
	private boolean isRemindOrder(int orderId) {
		boolean isRemind = false;
		if (stateArray != null && stateArray.size() > 0) {
			isRemind = stateArray.get(orderId);
		}
		return isRemind;

	}

	/**
	 * 
	 * Describe:启动提示页面
	 * 
	 * Date:2015-7-9
	 * 
	 * Author:liuzhouliang
	 */
	private void startMessageDialogtActivity(int position) {
		Intent intent = new Intent(mActivity, AppointOrderRemindDialogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		intent.putExtra(BRAction.EXTRA_ORDER_INFO, mOrderInfoList.get(position));
		mActivity.startActivity(intent);
	}

	private void registerLocalReceiver() {
		if (mPushReceiver == null) {
			mPushReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (mPullListView != null) {
						// 订单被改派, 会停留5分钟
						if (JPushReceiver.ACTION_REARRANGED_ORDER.equals(action)) {
							CurrentOrderFragment.this.refreshList();
						}
					}
				}
			};
		}
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(JPushReceiver.ACTION_REARRANGED_ORDER);

		ApplicationController.getInstance().getLocalBroadcastManager().registerReceiver(mPushReceiver, intentFilter);
	}

	private void unRegisterLocalReceiver() {
		ApplicationController.getInstance().getLocalBroadcastManager().unregisterReceiver(mPushReceiver);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (changeOrderTimer != null) {
			changeOrderTimer.cancel();
			changeOrderTimer = null;
		}
		if (changeOrderTimerTask != null) {
			changeOrderTimerTask.cancel();
			changeOrderTimerTask = null;
		}
		if (mAdapter != null) {
			mAdapter = null;
		}

		mHandler = null;
		mPushReceiver = null;
		mRequest.cancel();
		mRequest = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_currentorder, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPullListView = (SingleLayoutListView) view.findViewById(R.id.fragment_fragment_cur_list);
		mPullListView.setAdapter(mAdapter);
		mAdapter.startCountDownTime();
		mPullListView.setMoveToFirstItemAfterRefresh(false);
		enableRefreshdMore();
		disableLoadMore();
		mPullListView.setOnItemClickListener(this);
		// mHandler.sendEmptyMessageDelayed(H.MSG_NEED_COUNT, 1000 * 60);
		/**
		 * 每5分钟轮询一次是否有订单需要提醒
		 */
		if (!ApplicationController.getInstance().isTimerStart) {
			appointOrderRemindTimer.schedule(appointOrderRemindTimerTask, 0, 1000 * 5);
			ApplicationController.getInstance().isTimerStart = true;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		this.refreshList();
		this.registerLocalReceiver();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		initDataRequest();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public void onDestroyView() {
		mAdapter.stopCountDownTime();
		unRegisterLocalReceiver();
		// mHandler.removeMessages(H.MSG_NEED_COUNT);
		if (null != mPullListView) {
			mOrderInfoList.clear();
			onRefreshComplete();
			mPullListView = null;
		}
		mIsRefreshingList = false;
		super.onDestroyView();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mOrderInfoList != null && mOrderInfoList.size() > 0 && mOrderInfoList.get(position - 1) != null) {
			Intent intent = new Intent(mActivity, OrderFlowActivity.class);
			// 从列表中获取订单数据传入到OrderFlowActivity中
			intent.putExtra(BRAction.EXTRA_ORDER_INFO, mOrderInfoList.get(position - 1));
			startActivityForResult(intent, 0);
			mActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}

	}

	private void refreshOrders() {
		if (mIsRefreshingList) {
			return;
		}
		mIsRefreshingList = true;
		mRequestQueue.add(mRequest);
	}

	private void onRefreshComplete() {

		if (mAdapter != null) {
			mAdapter.initCountDownTime();
			mAdapter.notifyDataSetChanged();
		}

		if (mPullListView != null) {
			mPullListView.onRefreshComplete();
		}
	}

	private void enableRefreshdMore() {
		mPullListView.setCanRefresh(true);
		mPullListView.setOnRefreshListener(new SingleLayoutListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshOrders();
			}
		});
	}

	private void disableLoadMore() {
		mPullListView.setCanLoadMore(false);
		mPullListView.setAutoLoadMore(false);
		mPullListView.setOnLoadListener(null);
	}

	private void refreshList() {
		if (mPullListView != null && mIsRefreshingList == false) {
			mPullListView.pull2RefreshManually();
		}
	}

	public void getRefresh() {
		this.refreshList();
	}
}