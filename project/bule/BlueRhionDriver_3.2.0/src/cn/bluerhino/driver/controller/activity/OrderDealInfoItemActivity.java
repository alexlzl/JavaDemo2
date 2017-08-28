package cn.bluerhino.driver.controller.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRFLAG;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.ordermanager.CountdownOrderInfo;
import cn.bluerhino.driver.controller.ordermanager.CountdownOrderInfoUtil;
import cn.bluerhino.driver.controller.receiver.PushState;
import cn.bluerhino.driver.model.DeliverInfo;
import cn.bluerhino.driver.model.LocationInfo;
import cn.bluerhino.driver.model.OrderExecuteResponse;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.OrderInfo.OrderState;
import cn.bluerhino.driver.network.ExecuteOrderRequest;
import cn.bluerhino.driver.network.WaitOrderListRequest;
import cn.bluerhino.driver.network.listener.BrErrorListener;
import cn.bluerhino.driver.utils.TimeUtil;
import cn.bluerhino.driver.view.LoadingDialog;
import cn.bluerhino.driver.view.PlaceofReceiptLinearLayout;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRResponseError;
import com.bluerhino.library.network.framework.BRModelListRequest.BRModelListResponse;
import com.bluerhino.library.network.framework.BRModelRequest.BRModelResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.bluerhino.library.utils.WeakHandler;
import com.umeng.analytics.MobclickAgent;

/**
 * 抢单详情页
 */
public class OrderDealInfoItemActivity extends BaseParentActivity implements
		View.OnClickListener, OnGetRoutePlanResultListener {

	private static final String TAG = OrderDealInfoItemActivity.class
			.getSimpleName();

	MapView mMapView = null;
	BaiduMap mBaidumap = null;

	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	
	/**
	 * 订单时间(判断到时是即时 还是预定)
	 */
	@InjectView(R.id.order_type)
	TextView order_type;
	/**
	 * 订单金额
	 */
	@InjectView(R.id.order_bill)
	TextView mOrderBill;
	@InjectView(R.id.orderinfo_ordernum)
	TextView mOrderID;
	/**
	 * 发货地
	 */
	@InjectView(R.id.orderinfo_item_placeofdispatch)
	TextView mPlaceofdispatch;
	/**
	 * 收货地线性布局集合
	 */
	@InjectView(R.id.orderinfo_item_3_grounp)
	LinearLayout orderinfo_item_3_grounp;
	/**
	 * 公里数
	 */
	@InjectView(R.id.orderinfo_item_kilometer)
	TextView mKilometer;
	/**
	 * 备注信息
	 */
	@InjectView(R.id.orderinfo_remark)
	TextView mRemark;

	@InjectView(R.id.order_deal_leftbar_barbutton)
	ImageView mBackBtn;
	@InjectView(R.id.button_graborder)
	Button mGetOrderButton;

	/**
	 * 当前的OrderInfo对象.
	 */
	private OrderInfo mCurrentOrder;

	/**已抢单提示框*/
	private AlertDialog mAlertDialog;

	private Activity mActivity;

	private RequestQueue mRequestQueue;

	private LoadingDialog mLoadingDialog;

	private BRRequestParams mParams;
	
	
	/**
	 * 是否启用倒计时
	 */
	private boolean mIsNeedCountDown = false;
	
	/**
	 * 当前剩余的秒数
	 */
	private int coundDownNum = -1;
	
	private CountDownHandler mHandler = new CountDownHandler(this);
	
	private static class CountDownHandler extends WeakHandler<OrderDealInfoItemActivity> {
		
		static final int MSG_NEED_COUNT = 0x0;
		static final int MSG_NOT_NEED_COUNT = 0x1;

		CountDownHandler(OrderDealInfoItemActivity reference) {
			super(reference);
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NEED_COUNT:
				if(getReference() != null){
					if(getReference().mIsNeedCountDown){
						//再次判断条件,1s后继续更新
						if(getReference().coundDownNum >= 1){
							sendEmptyMessageDelayed(MSG_NEED_COUNT, 1000);
							
							String head = getReference().mRes.getString(R.string.orderstate_needdelivery);
							String tail = String.format("(%dS)", getReference().coundDownNum);
							getReference().mGetOrderButton.setText(head + tail);
							getReference().coundDownNum--;
						}else{
							this.sendEmptyMessageDelayed(MSG_NOT_NEED_COUNT, 1000);
							removeMessages(MSG_NEED_COUNT);
						}
					}
				}
				break;
			case MSG_NOT_NEED_COUNT:
				if(getReference() != null){
					Toast.makeText(getReference().getApplicationContext(), "该订单已被抢走啦，下次手一定要快！", Toast.LENGTH_SHORT).show();
					getReference().setButtonStatus("订单超时");
				}
				break;				
			default:
				break;
			}
		}
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (super.checkUserIsLogin()) {
			setContentView(R.layout.activity_waitlist_iteminfo);
			ButterKnife.inject(this);
			mActivity = this;
			initTitle();
			initBaiduMap();
			initVolley();
			createLoadingDialog();
			createGotOrderDialog();
			processJmupTabPage();
			MobclickAgent.onEvent(this, "pagewaitList_page_detail");
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			setIntent(intent);
			processJmupTabPage();
		}
	}

	private void processJmupTabPage() {
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		Uri data = intent.getData();
		// 1.通过链接进入(短信 or Wap页面  , flag is 0)
		if (data != null) {
			this.handleFromLink(data);
		}else{
			// 2.极光推送进入: 排除掉通过home键最小化再次进入屏幕的情况
			Bundle extras = intent.getExtras();
			if (intent.getFlags() == BRFLAG.PUSH_ACTIVITY_FLAG) {
				this.handleNotificationOrder(extras);
			}
			// 3.直接进入
			else{
				this.handleFromWaitList(extras);
			}
		}
	}

	private void initTitle() {
		mBackBtn.setVisibility(View.VISIBLE);
		mBackBtn.setOnClickListener(this);
	}

	private void initBaiduMap() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaidumap = mMapView.getMap();

		mMapView.showZoomControls(false);
		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				mBaidumap.hideInfoWindow();
			}

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}
		});
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
	}

	private void initView() {
		if (mCurrentOrder != null) {
			//2100等待司机抢单状态
			if (mCurrentOrder.getOrderFlag() == OrderState.NEEDDRIVER2) {
				//2100 且 reason没有 才表示可以抢单
				if (mCurrentOrder.getReason().equals("")) {
					final CountdownOrderInfo countdownOrderInfo = (CountdownOrderInfo)mCurrentOrder;
			        //可以抢单
					if(this.mIsNeedCountDown){
						this.coundDownNum = countdownOrderInfo.getSecond();
						if(this.coundDownNum > 0){
							mHandler.sendEmptyMessage(CountDownHandler.MSG_NEED_COUNT);
						}else{
							mGetOrderButton.setText(R.string.orderstate_needdelivery);
						}
					}else{
						mGetOrderButton.setText(R.string.orderstate_needdelivery);
					}
		        	mGetOrderButton
		        	.setOnClickListener(new View.OnClickListener() {
		        		@Override
		        		public void onClick(View v) {
		        			if (!isFinishing()) {
		        				mLoadingDialog.show();
		        			}
		        			BRFastRequest mRequest = new ExecuteOrderRequest.Builder()
		    				.setSucceedListener(
		    					new BRModelResponse<OrderExecuteResponse>() {
		    						@Override
		    						public void onResponse(OrderExecuteResponse model) {
		    							if (!isFinishing()) {
		    								mIsNeedCountDown = false;
		    								mLoadingDialog.dismiss();
		    								mAlertDialog.show();
		    								//更改状态为成功抢单
		    								CountdownOrderInfoUtil.setGrabOrderInGrab(countdownOrderInfo);
		    								//显示
		    								OrderDealInfoItemActivity.this.setButtonStatus(countdownOrderInfo.getReason());	
		    							}
		    						}
		    					})
		    				.setErrorListener(new BrErrorListener(mActivity) {
		    					@Override
		    					public void onErrorResponse(VolleyError error) {
		    						if (!isFinishing()) {
		    							mLoadingDialog.dismiss();
		    						}
	        						if (error instanceof BRResponseError) {
	    								mIsNeedCountDown = false;
	        							CountdownOrderInfoUtil.setReasonInNotGrabDetail(countdownOrderInfo);
	        							//显示
	        							OrderDealInfoItemActivity.this.setButtonStatus(countdownOrderInfo.getReason());	
	        							//抢单功能键变灰，变成“订单已被抢走”, 出现toast提示“”
	        							Toast.makeText(mContext.getApplicationContext(), "该订单已被抢走啦，下次手一定要快！",Toast.LENGTH_SHORT).show();
	        						}else{
	        							super.onErrorResponse(error);
	        						}
		    						
		    					}
		    				})
		    				.setParams(mParams)
		    				.setRequestTag(TAG).build();
		        			mRequestQueue.add(mRequest);
		        		}
		        	});
				} 
				//2100 且 reason有显示, 不可点, 并显示原因
				else {
					this.setButtonStatus(mCurrentOrder.getReason());			
				}
			}
			//非2100状态
			else {
				this.setButtonStatus(mCurrentOrder.getReason());			
			}
			String orderTypeValue;
			if (mCurrentOrder.getServeType() == 200)
				orderTypeValue = mRes
						.getString(R.string.order_deal_servetype_instant);
			else
				orderTypeValue = TimeUtil.format(new Date(mCurrentOrder
						.getTransTime() * 1000));
			order_type.setText(orderTypeValue);
			mOrderBill.setText(mCurrentOrder.getOrderBill());
			mOrderID.setText(String.valueOf(mCurrentOrder.getOrderId()));
			mPlaceofdispatch.setText(mCurrentOrder.getPickupAddress());
			orderinfo_item_3_grounp.removeAllViews();
			// 单地址
			if (mCurrentOrder.getDeliver().size() < 0) {
				orderinfo_item_3_grounp.setVisibility(View.GONE);
			} else if (mCurrentOrder.getDeliver().size() == 1) {
				PlaceofReceiptLinearLayout myLayout = (PlaceofReceiptLinearLayout) View
						.inflate(this,
								R.layout.fragment_currentorder_placeofreceipt,
								null);
				// 获得唯一一条收获地址信息
				myLayout.updateAddressTitle();
				myLayout.updateAddress(mCurrentOrder.getDeliver().get(0)
						.getDeliverAddress());

				orderinfo_item_3_grounp.addView(myLayout);
				orderinfo_item_3_grounp.setVisibility(View.VISIBLE);
			} else if (mCurrentOrder.getDeliver().size() > 1) {
				// 从一条订单中分拆出多条收货信息
				int index = 0;
				for (DeliverInfo deliverEx : mCurrentOrder.getDeliver()) {
					PlaceofReceiptLinearLayout myLayout = (PlaceofReceiptLinearLayout) View
							.inflate(
									this,
									R.layout.fragment_currentorder_placeofreceipt,
									null);
					myLayout.updateAddressTitleIndex(++index);
					myLayout.updateAddress(deliverEx.getDeliverAddress());
					orderinfo_item_3_grounp.addView(myLayout);
				}
				orderinfo_item_3_grounp.setVisibility(View.VISIBLE);
			}
			// 设置公里数
			mKilometer.setText(mCurrentOrder.getKilometer());
			// 显示备注信息
			String remark = mCurrentOrder.getRemark();
			if (TextUtils.isEmpty(remark)) {
				mRemark.setVisibility(View.GONE);
			} else {
				remark = remark.replace("\n", "");
				mRemark.setText(remark);
			}
		}
	}

	private void initVolley() {
		mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		ApplicationController instance = ApplicationController.getInstance();
		mParams = new BRRequestParams(instance.getLoginfo().getSessionID());
		mParams.setDeviceId(instance.getDeviceId());
		mParams.setVerCode(instance.getVerCode());
	}

	private void createLoadingDialog() {
		mLoadingDialog = new LoadingDialog(mActivity,
				R.string.order_deal_info_getorder);
	}

	/**
	 * create抢单提示dialog
	 */
	private void createGotOrderDialog() {
		OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isFinishing()) {
					mAlertDialog.dismiss();
				}
			}
		};

		mAlertDialog = new AlertDialog.Builder(this).setCancelable(false)
				.create();
		mAlertDialog.show();
		Window alertWindow = mAlertDialog.getWindow();
		alertWindow.setContentView(R.layout.alertdialog_bg);
		alertWindow.findViewById(R.id.btn).setOnClickListener(onClickListener);
		alertWindow.findViewById(R.id.text).setOnClickListener(onClickListener);
		mAlertDialog.dismiss();
	}

	@Override
	protected void onResume() {
		MobclickAgent.onPageStart(TAG);
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPageEnd(TAG);
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		mHandler.removeMessages(CountDownHandler.MSG_NEED_COUNT);
		mRequestQueue.cancelAll(TAG);
		mSearch.destroy();
		mMapView.onDestroy();
		super.onDestroy();
	}

	private void startDrivingSearch() {
		if (mBaidumap != null) {
			mBaidumap.clear();
		}
		if (mCurrentOrder != null) {
			PlanNode startNode = PlanNode.withLocation(new LatLng(Double
					.parseDouble(mCurrentOrder.getPickupAddressLat()), Double
					.parseDouble(mCurrentOrder.getPickupAddressLng())));
			List<PlanNode> planNodeList = new ArrayList<PlanNode>();
			for (DeliverInfo deliverInfo : mCurrentOrder.getDeliver()) {
				double lat = Double.parseDouble(deliverInfo
						.getDeliverAddressLat());
				double lng = Double.parseDouble(deliverInfo
						.getDeliverAddressLng());
				planNodeList.add(PlanNode.withLocation(new LatLng(lat, lng)));
			}
			if (planNodeList.isEmpty()) {
				ToastUtil.showToast(mActivity,
						R.string.order_deal_msg_route_observer_failure);
				return;
			} else if (planNodeList.size() == 1) {
				mSearch.drivingSearch(new DrivingRoutePlanOption()
						.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST)
						.from(startNode).to(planNodeList.get(0)));
			} else {
				PlanNode endNode = planNodeList.remove(planNodeList.size() - 1);
				mSearch.drivingSearch(new DrivingRoutePlanOption()
						.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST)
						.from(startNode).passBy(planNodeList).to(endNode));
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_deal_leftbar_barbutton:
			this.onBackPressed();
		}
	}
	
	/**
	 * 处理从短信进入的情况
	 * @param data
	 */
	private void handleFromLink(Uri data) {
		//获得订单号
		final String orderId = data.getQueryParameter("orderId");
		this.processOrderId(orderId);
	}
	
	/**
	 * 处理极光推送的intent
	 */
	private void handleNotificationOrder(Bundle extras) {
		// 使用通知传来的订单号
		final String orderId = extras.getString(PushState.KEY_ORDERID);
		this.processOrderId(orderId);
	}
	
	/**
	 * 处理从订单详情进入的情况
	 */
	private void handleFromWaitList(Bundle extras) {
		if(extras == null){
			JumpFailure();
			return;
		}
		//得到orderNUm
		int orderNum = extras.getInt("orderNum");
		if(orderNum == 0){
			JumpFailure(R.string.orderstate_ordernum_failure);
			return;
		}
		//根据orderNum查找是否存在,不存在直接退出
		CountdownOrderInfo countdownOrderInfo = ApplicationController.getInstance().getMatchOrder(orderNum);
		if(countdownOrderInfo == null){
			JumpFailure(R.string.orderstate_ordernum_failure);
			return;
		}
		this.mCurrentOrder = countdownOrderInfo;
		this.mIsNeedCountDown = true;//启用倒计时
		this.handleOrderInfo();
	}
	
	/**
	 * 根据mCurrentOrder信息, 构建界面并初始化请求
	 */
	private void handleOrderInfo(){
		if (mCurrentOrder != null) {
			initExecuteOrderRequest();
			initView();
			startDrivingSearch();
		}else{
			this.JumpFailure(R.string.order_deal_get_order_null);
		}
	}

	private void initExecuteOrderRequest() {
		if (mCurrentOrder != null) {
			LocationInfo locationinfo = ApplicationController
					.getInstance().getLastLocationInfo();
			mParams.put("lat", String.valueOf(locationinfo
					.getLatitude()));
			mParams.put("lng", String.valueOf(locationinfo
					.getLongitude()));
			mParams.put("OrderFlag", OrderState.NEEDDRIVER3);
			mParams.put("orderId", mCurrentOrder.getOrderId());
		}else{
			this.JumpFailure(R.string.order_deal_get_order_null);
		}
	}

	/**
	 * 驾车路线结果回调
	 */
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			ToastUtil.showToast(this,
					R.string.wait_order_get_driving_route_result);
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			ToastUtil.showToast(this,
					R.string.wait_order_get_driving_route_ambiguous_addr);
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaidumap);
			// 设置地图 Marker 覆盖物点击事件监听者
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
	}
	
	/**
	 * 通过跳转到失败的处理提示
	 */
	private void JumpFailure(){
		JumpFailure(R.string.orderstate_restart_failure);
	}
	
	private void JumpFailure(int resId){
		finish();
		ToastUtil.showToast(getApplicationContext(), resId);
	}
	
	/**
	 * 通过传来的订单号获得order信息, 并进行界面初始化 和 网络请求的逻辑
	 */
	private void processOrderId(final String orderId) {
		if(orderId == null){
			JumpFailure();
			return;
		}
		try{
			Integer.parseInt(orderId);
		}catch(NumberFormatException e){
			e.printStackTrace();
			JumpFailure();
			return;
		}
		// 获得订单号,并进入订单详情页
		BRRequestParams params = new BRRequestParams();
		params.setToken(ApplicationController.getInstance().getLoginfo().getSessionID());
		params.put(PushState.KEY_ORDERID, orderId);
		BRFastRequest request = new WaitOrderListRequest.Builder()
				.setSucceedListener(
						new BRModelListResponse<List<OrderInfo>>() {
							@Override
							public void onResponse(List<OrderInfo> modelList) {
								if (!isFinishing() && modelList != null) {
									mLoadingDialog.dismiss();
								}
								if(modelList.size() == 0){
									JumpFailure();
								}
								else if (modelList.size() == 1) {
									OrderInfo orderInfo = modelList
											.get(0);
									if (orderInfo != null
											&& orderInfo.getOrderId() != 0) {
										mCurrentOrder = orderInfo;
										OrderDealInfoItemActivity.this.handleOrderInfo();
									} else {
										JumpFailure(R.string.order_deal_parse_info_error);
									}
								} else {
									JumpFailure(R.string.order_deal_parse_info_error);
								}
							}
						})
				.setErrorListener(new BrErrorListener(mActivity) {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (!isFinishing()) {
							super.onErrorResponse(error);
							mLoadingDialog.dismiss();
							JumpFailure();
						}
					}
				}).setParams(params).setRequestTag(TAG).build();
		if (!isFinishing()) {
			mLoadingDialog.show();
			mRequestQueue.add(request);
		}
	}
	
	/**
	 * 设置按钮可用状态
	 * @param string 
	 */
	private void setButtonStatus(String reason){
    	mGetOrderButton.setEnabled(false);
    	mGetOrderButton.setText(reason);
		mGetOrderButton.setBackgroundResource(R.drawable.order_recbutton_bg_pressed);
	}
}
