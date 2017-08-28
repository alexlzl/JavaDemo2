package com.minsheng.app.module.appointmentorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.application.MsApplication.OrderListStateChange;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.OrderListEntity;
import com.minsheng.app.entity.OrderListEntity.Infor.OrderObject;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.appointmentorder.AppointmentOrderAdapter.CourierStateChangeListener;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.NetWorkState;
import com.minsheng.app.view.MsRefreshListView;
import com.minsheng.app.view.MsToast;
import com.minsheng.app.view.MsRefreshListView.OnMoreListener;
import com.minsheng.app.view.MsRefreshListView.OnRefreshListener;
import com.minsheng.wash.R;

/**
 * 
 * @describe:预约订单列表页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-4下午7:24:28
 * 
 */
public class AppointmentOrder extends BaseActivity implements OnMoreListener,
		OnRefreshListener, OrderListStateChange, CourierStateChangeListener {
	private static final String TAG = "AppointmentOrder";
	private MsRefreshListView listview;
	private AppointmentOrderAdapter adapter;
	private int pageNum = 0;
	private boolean isRefresh;
	private boolean isLoadMore;
	// 标记是否分页数据加载完成
	private boolean isLoadOver;
	private int startCount;
	private String pageToken;
	private OrderListEntity orderEntity;
	private List<OrderObject> orderList;
	private ImageView ivBackToTop;
	private boolean isFreshData;
	// 标记返回后是否刷新首页数据
	private boolean isBackRefresh;
	private boolean isRefreshState = false;
	private PopupWindow mPopupWindow = null;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.appointment_order_list);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		listview = (MsRefreshListView) findViewById(R.id.appointment_order_list_lv);
		ivBackToTop = (ImageView) findViewById(R.id.appointment_order_list_back);
	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		if (!isFreshData) {
			showRoundProcessDialog();
		}

		RequestParams params = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		if (MsApplication.isLogin()) {
			map.put("loginToken", MsApplication.getLoginToken());
		}
		map.put("pageSize", MsConfiguration.PAGE_SIZE);
		map.put("queryType", MsConfiguration.APPOINT_LIST);
		map.put("outerType", 2);
		if (pageNum == 0) {
			/**
			 * 加载第一页
			 */
			map.put("pageType", 0);
			map.put("startCount", 0);
			map.put("pageToken", pageToken);
			LogUtil.d(TAG, "页数==" + pageNum);
		} else {
			/**
			 * 加载分页
			 */
			map.put("pageType", 1);
			map.put("startCount", startCount);
			map.put("pageToken", pageToken);
			LogUtil.d(TAG, "页数==" + pageNum + "startCount==" + startCount);
		}
		params = MsApplication.getRequestParams(map, params,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, params,
				MsRequestConfiguration.APPOINT_ORDER,
				new BaseJsonHttpResponseHandler<OrderListEntity>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, OrderListEntity arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						if (!isFreshData) {
							dismissRoundProcessDialog();
						}

						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerOrderList.sendMessage(message);

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							OrderListEntity arg3) {

					}

					@Override
					protected OrderListEntity parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (!isFreshData) {
							dismissRoundProcessDialog();
						}

						/**
						 * 更新刷新状态或者分页加载状态
						 */
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							orderEntity = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									OrderListEntity.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerOrderList.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerOrderList.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return orderEntity;
					}

				});
	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub
		setReloadContent(R.layout.appointment_order_list);
		getNetData();
	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		listview.setonLoadListener(this);
		listview.setonRefreshListener(this);
		ivBackToTop.setOnClickListener(this);
		tvRight.setOnClickListener(this);
		MsApplication.setOrderListChangeListener(this);
	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "预约订单";
	}

	@Override
	protected String setRightText() {
		// TODO Auto-generated method stub
		return "筛选";
	}

	@Override
	protected int setLeftImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setMiddleImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setRightImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 处理消息返回
	 */
	Handler handlerOrderList = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case MsConfiguration.SUCCESS:
				/**
				 * 数据加载成功
				 */
				setViewData();
				// init();
				break;
			case MsConfiguration.FAIL:
				/**
				 * 数据加载失败
				 */
				setFailView();
				break;
			case MsConfiguration.OVER:

				listview.onLoadComplete();
				listview.onRefreshComplete();
				break;

			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:绑定数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-18下午4:40:15
	 * 
	 */
	private void setViewData() {
		// TODO Auto-generated method stub

		if (pageNum == 0) {
			/**
			 * 绑定第一页数据：1,初始化加载，需要处理空页面;2,下拉刷新，不显示空页面
			 */
			if (orderEntity != null && orderEntity.getCode() == 0) {
				/**
				 * 返回成功===================
				 */
				if (orderEntity.getInfo() != null
						&& orderEntity.getInfo().getDeliveryOrderList() != null
						&& orderEntity.getInfo().getDeliveryOrderList().size() > 0) {

					/**
					 * 列表数据不为空=======================
					 */
					pageToken = orderEntity.getInfo().getPageToken();
					startCount = orderEntity.getInfo().getEndCount() + 1;
					orderList = orderEntity.getInfo().getDeliveryOrderList();
					adapter = new AppointmentOrderAdapter(baseContext,
							orderList);
					listview.setAdapter(adapter);
					adapter.start();
					adapter.seChangeListener(this);

					LogUtil.d(TAG, "页数" + pageNum + "startCount==" + startCount
							+ "数据长度" + orderList.size());
				} else {
					/**
					 * 列表数据为空=====================
					 */
					if (!isRefresh) {
						setNullView();
					} else {
						MsToast.makeText(baseContext, "加载数据失败").show();
					}

				}
			} else {
				/**
				 * 返回失败========================
				 */
				if (!isRefresh) {
					/**
					 * 如果是在初始化显示加载失败页面
					 */
					showLoadFailView();
				}

				if (orderEntity != null) {
					MsToast.makeText(baseContext, orderEntity.getMsg()).show();
				} else {
					MsToast.makeText(baseContext, "加载数据失败").show();
				}

			}
			if (isRefresh) {
				/**
				 * 如果是刷新数据，重置标记
				 */
				listview.onRefreshComplete();
				isRefresh = false;
			}
		} else {
			/**
			 * 绑定分页数据========================
			 */
			if (orderEntity != null && orderEntity.getCode() == 0) {
				/**
				 * 处理分页数据返回成功====================
				 */

				if (orderEntity.getInfo() != null
						&& orderEntity.getInfo().getDeliveryOrderList() != null
						&& orderEntity.getInfo().getDeliveryOrderList().size() > 0) {
					/**
					 * 分页数据不为空
					 */
					isLoadOver = false;
					startCount = orderEntity.getInfo().getEndCount() + 1;
					List<OrderObject> pageList = orderEntity.getInfo()
							.getDeliveryOrderList();
					if (pageList != null) {
						orderList.addAll(pageList);
					}
					if (adapter != null) {
						adapter.setNewData(orderList);
					}

					LogUtil.d(TAG, "页数" + pageNum + "startCount==" + startCount
							+ "总数据长度" + orderList.size());
				} else {

					/**
					 * 没有分页数据
					 */
					// 标记为分页加载完成
					isLoadOver = true;
					listview.onLoadComplete();
					LogUtil.d(TAG, "分页数据加载完成");
				}
			} else {

				/**
				 * 返回失败========================
				 */
				if (orderEntity != null) {
					MsToast.makeText(baseContext, orderEntity.getMsg()).show();
				} else {
					MsToast.makeText(baseContext, "返回失败").show();
				}

			}
			/**
			 * 标记如果是分页加载，重置标记
			 */
			if (isLoadMore) {
				listview.onLoadComplete();
				isLoadMore = false;
			}

		}

	}

	/**
	 * 
	 * 
	 * @describe:设置空页面
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-18下午4:55:17
	 * 
	 */
	public void setNullView() {
		/**
		 * 正常请求
		 */
		if (isRefresh) {
			/**
			 * 刷新状态，不显示空页面
			 */
			isRefresh = false;
			listview.onRefreshComplete();
			MsToast.makeText(baseContext, "暂无更新").show();
			return;
		}
		if (isLoadMore) {
			/**
			 * 分页加载状态，不显示空页面
			 */
			isLoadMore = false;
			listview.onLoadComplete();
			return;
		}

		else {
			/**
			 * 初始化为空
			 */
			showNoDataView("目前还没有内容信息");
			return;
		}
	}

	/**
	 * 
	 * 
	 * @describe:设置加载失败页面
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-18下午4:58:56
	 * 
	 */
	public void setFailView() {
		if (isRefresh) {
			/*
			 * 下拉刷新失败，不显示失败页面
			 */
			MsToast.makeText(baseContext, "刷新失败").show();
			isRefresh = false;
			listview.onRefreshComplete();
			return;
		}
		if (isLoadMore) {
			/**
			 * 分页加载失败
			 */
			MsToast.makeText(baseContext, "分页加载失败").show();
			isLoadMore = false;
			listview.onLoadComplete();
			return;
		} else {
			/**
			 * 初始化失败
			 */
			showLoadFailView();
			return;
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!NetWorkState.isNetWorkConnection(baseContext)) {
			/**
			 * 无网络连接状态
			 */
			MsToast.makeText(baseContext, "请检查网络").show();
			Message message = Message.obtain();
			message.what = MsConfiguration.OVER;
			handlerOrderList.sendMessage(message);
		} else {
			pageNum = 0;
			isLoadOver = false;
			isRefresh = true;
			getNetData();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (isLoadOver) {
			/**
			 * 分页加载完成
			 */
			Message message = Message.obtain();
			message.what = MsConfiguration.OVER;
			handlerOrderList.sendMessage(message);
			if (!NetWorkState.isNetWorkConnection(baseContext)) {
				/**
				 * 无网络连接状态
				 */
				MsToast.makeText(baseContext, "请检查网络").show();

			} else {
				MsToast.makeText(baseContext, "没有更多数据了!").show();

			}
		} else {
			/**
			 * 分页未加载完成
			 */
			if (!NetWorkState.isNetWorkConnection(baseContext)) {
				/**
				 * 无网络连接状态
				 */
				MsToast.makeText(baseContext, "请检查网络").show();
				Message message = Message.obtain();
				message.what = MsConfiguration.OVER;
				handlerOrderList.sendMessage(message);
			} else {
				pageNum++;
				isLoadMore = true;
				getNetData();
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.appointment_order_list_back:
			listview.requestFocusFromTouch();
			listview.setSelection(0);
			break;
		case R.id.base_activity_title_right_righttv:
			final PopupWindow popupwindow = showFilterWindow(baseActivity,
					rlTitlePrent, R.layout.appointment_order_filter_window);
			View view = popupwindow.getContentView();
			GridView gv = (GridView) view
					.findViewById(R.id.appointment_order_filter_window_gv);
			view.findViewById(R.id.appointment_order_filter_window_bg)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							popupwindow.dismiss();
						}
					});
			List<String> data = new ArrayList<String>();
			for (int i = 0; i < 6; i++) {
				data.add(i + "");
			}
			FilterDataAdapter adapter = new FilterDataAdapter(data,popupwindow, this);
			gv.setAdapter(adapter);

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, mIntent);
		if (MsConfiguration.APPOINT_ORDER_TO_DETAIL == requestCode) {
			isFreshData = mIntent.getBooleanExtra("isFreshData", false);
			if (isFreshData) {
				getNetData();
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (isBackRefresh) {
			/**
			 * 刷新首页数据
			 */
			MsApplication.backHomeListener.backHomepage();
		}
		if (isRefreshState) {
			setResult(MsConfiguration.HOME_PAGE_TO_APPOINT);
			LogUtil.d(TAG, "返回刷新首页数据");
		}

	}

	@Override
	public void orderListChange() {
		// TODO Auto-generated method stub
		isBackRefresh = true;
	}

	@Override
	public void changeListener() {
		// TODO Auto-generated method stub
		isRefreshState = true;
	}

	private PopupWindow showFilterWindow(Activity activity, View position,
			int layoutId) {
		View mPopMenuView = null;
		mPopMenuView = LayoutInflater.from(this).inflate(layoutId, null);

		mPopupWindow = new PopupWindow(mPopMenuView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, false);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		mPopupWindow.getBackground().setAlpha(127);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setAnimationStyle(R.style.WindowAnimation);
		mPopupWindow.showAsDropDown(position, 0, 0);
		mPopupWindow.update();
		return mPopupWindow;
	}
}
