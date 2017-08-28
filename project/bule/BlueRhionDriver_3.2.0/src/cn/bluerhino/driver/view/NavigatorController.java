package cn.bluerhino.driver.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.BNavigatorActivity;
import cn.bluerhino.driver.model.DeliverInfo;
import cn.bluerhino.driver.model.LocationInfo;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.OrderInfo.OrderState;
import cn.bluerhino.driver.utils.AppRunningInfor;
import cn.bluerhino.driver.utils.LogUtil;

import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.CommonParams.Const.ModelName;
import com.baidu.navisdk.CommonParams.NL_Net_Mode;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.routeplan.IRouteResultObserver;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.baidu.navisdk.model.NaviDataEngine;
import com.baidu.navisdk.model.RoutePlanModel;
import com.baidu.navisdk.model.datastruct.RoutePlanNode;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.baidu.nplatform.comapi.basestruct.GeoPoint;
import com.bluerhino.library.utils.ToastUtil;
import com.bluerhino.library.utils.WeakHandler;

public class NavigatorController implements OnClickListener {

	private static final String TAG = NavigatorController.class.getSimpleName();
	private Activity mActivity;
	private AlertDialog mRoutePlanDialog;
	private TextView mDicFirstView;
	private TextView mRecommendView;
	private String mDicFirstFormat;
	private String mRecommendFormat;
	private String mHourMinFormat;
	private String mMidFormat;

	/**
	 * 规划好的集合
	 */
	private ArrayList<RoutePlanNode> mPlanNodeList = new ArrayList<RoutePlanNode>(2);
	private OrderInfo mOrderInfo;
	/**
	 * 当前状态 3 ， 4 ，5
	 */
	private int mPlanState;
	private H mHandler = new H(this);

	private static class H extends WeakHandler<NavigatorController> {
		
		/**
		 * 最短距离
		 */
		private static final int MSG_ROUTE_PLAN_MOD_MIN_DIST = 3;

		/**
		 * 最优距离
		 */
		private static final int MSG_ROUTE_PLAN_MOD_RECOMMEND = 4;

		private H(NavigatorController reference) {
			super(reference);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ROUTE_PLAN_MOD_MIN_DIST:
			case MSG_ROUTE_PLAN_MOD_RECOMMEND:
				getReference().checkResult(msg);
				break;
			default:
				return;
			}
		}
	};

	public NavigatorController(Activity activity) {
		mActivity = activity;
		View view = View.inflate(mActivity,
				R.layout.dialog_navigator_select_route, null);
		mDicFirstView = ButterKnife.findById(view,
				R.id.navigator_dis_first_text);
		mRecommendView = ButterKnife.findById(view,
				R.id.navigator_recommend_text);
		view.findViewById(R.id.navigator_dis_first).setOnClickListener(this);
		view.findViewById(R.id.navigator_avoid_jam).setOnClickListener(this);
		view.findViewById(R.id.navigator_cancel).setOnClickListener(this);
		Resources res = mActivity.getResources();
		mDicFirstFormat = res.getString(R.string.navigator_dis_first);
		mRecommendFormat = res.getString(R.string.navigator_recommend);
		mHourMinFormat = res.getString(R.string.navigator_hour_min);
		mMidFormat = res.getString(R.string.navigator_min);
		/**
		 * 判断当前ACTIVITY是否处于栈顶
		 */
		if (AppRunningInfor.isActivityRunningForeground(mActivity,
				"cn.bluerhino.driver.controller.activity.OrderFlowActivity")) {
			/**
			 * 栈顶
			 */
			LogUtil.d(TAG, "OrderFlowActivity==栈顶");
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity,
					R.style.navigator_dialog_theme);
			builder.setCancelable(false);
			mRoutePlanDialog = builder.create();
			mRoutePlanDialog.setView(view, 0, 0, 0, 0);
		} else {
			LogUtil.d(TAG, "OrderFlowActivity==非栈顶");
		}
	}

	/**
	 * 
	 * Describe:更新算路页面信息
	 * 
	 * Date:2015-8-20
	 * 
	 * Author:liuzhouliang
	 */
	private void checkResult(Message msg) {
		int distance = msg.arg1;
		int duration = msg.arg2;

		switch (mPlanState) {
		case H.MSG_ROUTE_PLAN_MOD_MIN_DIST:
			updateMinDictInfo(distance, duration);
			startCalcRoute(NE_RoutePlan_Mode.ROUTE_PLAN_MOD_RECOMMEND,
					H.MSG_ROUTE_PLAN_MOD_RECOMMEND);
			break;
		case H.MSG_ROUTE_PLAN_MOD_RECOMMEND:
			if (!mActivity.isFinishing()) {
				updateRecommendInfo(distance, duration);
				selectedRoutePlan();
			}
			break;
		default:
			break;
		}
	}

	private void updateMinDictInfo(int distance, int duration) {
		mDicFirstView.setText(String.format(mDicFirstFormat, distance,
				geturationString(duration)));
	}

	private void updateRecommendInfo(int distance, int duration) {
		mRecommendView.setText(String.format(mRecommendFormat, distance,
				geturationString(duration)));
	}

	/**
	 * Describe:
	 * 
	 * Date:2015-6-18
	 * 
	 * Author:likai
	 */
	public void planRoute(OrderInfo orderInfo) {
		LogUtil.d(TAG, "导航订单详情===" + orderInfo.toString());
		mOrderInfo = orderInfo;
		mPlanState = H.MSG_ROUTE_PLAN_MOD_MIN_DIST;
		planRoute(mOrderInfo, NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_DIST,
				mPlanState);
	}

	/**
	 * Describe:算路开始
	 * 
	 * Date:2015-6-18
	 * 
	 * Author:liuzhouliang
	 */
	private void planRoute(OrderInfo orderInfo, int calculateMode, int planState) {
		LogUtil.d(TAG, "planRoute==订单信息" + orderInfo.toString());
		if (!checkOrderInfoIsAvailable(orderInfo)) {
			return;
		}
		initPlanerNode(orderInfo, planState);
		startCalcRoute(calculateMode, planState);
	}

	/**
	 * Describe:检查司机是否已完全服务了所有的收货地
	 * 
	 * Date:2015-6-18
	 * 
	 * Author:likai
	 * 
	 * @return 以判断是否可以开启导航
	 */
	private boolean checkOrderInfoIsAvailable(OrderInfo orderInfo) {
		// 固定收货次数
		int fixedCount = orderInfo.getDeliver().size();
		if (orderInfo.getArriveCount() >= fixedCount) {
			String toastMsg = mActivity.getString(
					R.string.navigator_schedule_changes, fixedCount);
			ToastUtil.showToast(mActivity, toastMsg, Toast.LENGTH_LONG);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * Describe:初始化算路
	 * 
	 * Date:2015-6-18
	 * 
	 * Author:liuzhouliang
	 */
	private void initPlanerNode(OrderInfo orderInfo, int planState) {
		ArrayList<RoutePlanNode> temPlanNodeList = new ArrayList<RoutePlanNode>();

		// 添加司机当前位置信息
		LocationInfo locationInfo = ApplicationController.getInstance()
				.getLastLocationInfo();
		temPlanNodeList.add(
				new RoutePlanNode(
						BaiduCoordinateTrans.createGeoPoint(locationInfo.getLongitude(), locationInfo.getLatitude()),
						RoutePlanNode.FROM_MY_POSITION, locationInfo.getAddress(),
						null));

		if (mOrderInfo.getOrderFlag() < OrderState.REACHSHIPADDRESS  ) {
			temPlanNodeList.add(
					new RoutePlanNode(
							BaiduCoordinateTrans.createGeoPoint(orderInfo.getPickupAddressLng(), orderInfo.getPickupAddressLat()),
							RoutePlanNode.FROM_MAP_POINT, orderInfo.getPickupAddress(),
							null));
		}

		int arrivecount = 0;
		if (mOrderInfo.getOrderFlag() > OrderState.REACHSHIPADDRESS  ) {
			arrivecount = mOrderInfo.getArriveCount();
		}
		for (DeliverInfo deliverInfo : mOrderInfo.getDeliver()) {
			if (arrivecount > 0) {
				arrivecount--;
				continue;
			}
			temPlanNodeList.add(
					new RoutePlanNode(
							BaiduCoordinateTrans.createGeoPoint(deliverInfo.getDeliverAddressLng(), deliverInfo.getDeliverAddressLat()),
							RoutePlanNode.FROM_MAP_POINT, 
							deliverInfo.getDeliverAddress(), 
							deliverInfo.getDeliverAddress()));
		}
		
		if(temPlanNodeList.size() >= 2){
			mPlanNodeList.clear();
			mPlanNodeList.add(temPlanNodeList.get(0));
			mPlanNodeList.add(temPlanNodeList.get(1));
		}
		
		// 初始化路线规划
		BNRoutePlaner.getInstance().init(mActivity);
	}

	private void startCalcRoute(int mode, int planState) {
	    BNRoutePlaner.getInstance().setObserver(new RoutePlanObserver(mActivity, null));
		// 设置路线规划结果监听
		BNRoutePlaner.getInstance()
				.setRouteResultObserver(mRouteResultObserver);
		// 设置路线规划模式
		BNRoutePlaner.getInstance().setCalcMode(mode);
		Log.i(TAG, mPlanNodeList.toString());
		// 设置路线规划点算路
		boolean ret = BNRoutePlaner.getInstance().setPointsToCalcRoute(
				mPlanNodeList, NL_Net_Mode.NL_Net_Mode_OnLine);
		if (ret) {
			mPlanState = planState;
		}
	}

	/**
	 * 路线规划监听观察者
	 */
	private final IRouteResultObserver mRouteResultObserver = new IRouteResultObserver() {

		@Override
		public void onRoutePlanYawingSuccess() {
		}

		@Override
		public void onRoutePlanYawingFail() {
		}

		/**
		 * 正常规划成功
		 */
		@Override
		public void onRoutePlanSuccess() {
			RoutePlanModel routePlanModel = (RoutePlanModel) NaviDataEngine
					.getInstance().getModel(ModelName.ROUTE_PLAN);
			Message msg = Message.obtain(mHandler, mPlanState);
			switch (mPlanState) {
			case H.MSG_ROUTE_PLAN_MOD_MIN_DIST:
			case H.MSG_ROUTE_PLAN_MOD_RECOMMEND:
				int distance = routePlanModel.getTotalDistanceInt() / 1000;
				int duration = routePlanModel.getTotalTimeInt();
				msg.arg1 = distance;
				msg.arg2 = duration;
				Log.i(TAG, String.format("distance=%dkm time=%dmin", distance,
						duration / 60));
				break;
			default:
				break;
			}
			msg.sendToTarget();
		}

		/**
		 * 正常规划失败
		 */
		@Override
		public void onRoutePlanFail() {
		}

		/**
		 * 取消路线规划
		 */
		@Override
		public void onRoutePlanCanceled() {
		}

		@Override
		public void onRoutePlanStart() {

		}
	};

	private String geturationString(int duration) {
		int[] times = getDurationTime(duration);
		if (times[0] > 0) {
			return String.format(mHourMinFormat, times[0], times[1]);
		} else {
			return String.format(mMidFormat, times[1]);
		}
	}

	private int[] getDurationTime(int duration) {
		int min;
		int hour;
		if(duration <= 60){
			min = 1;
			hour = 0;
		}else{
			min = duration / 60;
			hour = min / 60;
			if (hour > 0) {
				min = min % 60;
			}
		}
		int[] times = new int[2];
		times[0] = hour;
		times[1] = min;
		return times;
	}

	private void selectedRoutePlan() {
		if (AppRunningInfor.isActivityRunningForeground(mActivity,
				"cn.bluerhino.driver.controller.activity.OrderFlowActivity")) {
			mRoutePlanDialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.navigator_dis_first:
			this.launchNavigatorViaPoints(NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_DIST);
			break;
		case R.id.navigator_avoid_jam:
			this.launchNavigatorViaPoints(NE_RoutePlan_Mode.ROUTE_PLAN_MOD_RECOMMEND);
			break;
		case R.id.navigator_cancel:
			handleCancelEvent();
			break;
		default:
			break;
		}
	}

	private void handleCancelEvent() {
		if (!mActivity.isFinishing()) {
			mRoutePlanDialog.dismiss();
		}
	}

	// 开启导航
	private void launchNavigatorViaPoints(int strategy) {
		if (!mActivity.isFinishing()) {
			mRoutePlanDialog.dismiss();
		}
		if (!this.checkOrderInfoIsAvailable(mOrderInfo)) {
			return;
		}
		// 获得点
		List<BNaviPoint> points = this.getGBJ02Point();
		// 进行导航
		if (!mActivity.isFinishing()) {
			this.launchNavigator(points, strategy);
		}
	}

	private void launchNavigator(List<BNaviPoint> points, int strategy) {
		BaiduNaviManager.getInstance().launchNavigator(mActivity, points, // 路线点列表
				strategy, // 算路方式
				true, // 真实导航
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
				new OnStartNavigationListener() { // 跳转监听
					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(mActivity,
								BNavigatorActivity.class);
						intent.putExtras(configParams);
						mActivity.startActivity(intent);

					}

					@Override
					public void onJumpToDownloader() {
					}
				});
	}

	private List<BNaviPoint> getGBJ02Point() {
		List<BNaviPoint> points = new ArrayList<BNaviPoint>();
		/**
		 * 开始导航初始化
		 */
		LocationInfo locationInfo = ApplicationController.getInstance()
				.getLastLocationInfo();
		LogUtil.d(TAG, "司机导航当前坐标==" + locationInfo.toString());
		/**
		 * 添加司机当前位置信息
		 */
		points.add(BaiduCoordinateTrans.createBNaviPoint(
				String.valueOf(locationInfo.getLongitude()),
				String.valueOf(locationInfo.getLatitude()),
				locationInfo.getAddress()));

		/**
		 * 如果是在处于到达发货地之前，加入发货地位置信息
		 */
		if (mOrderInfo.getOrderFlag() < OrderState.REACHSHIPADDRESS  ) {
			points.add(BaiduCoordinateTrans.createBNaviPoint(
					mOrderInfo.getPickupAddressLng(),
					mOrderInfo.getPickupAddressLat(),
					mOrderInfo.getPickupAddress()));
		}

		int arrivecount = 0;
		if (mOrderInfo.getOrderFlag() > OrderState.REACHSHIPADDRESS  ) {
			arrivecount = mOrderInfo.getArriveCount();
		}
		for (DeliverInfo deliverInfo : mOrderInfo.getDeliver()) {
			if (arrivecount > 0) {
				arrivecount--;
				continue;
			}
			points.add(BaiduCoordinateTrans.createBNaviPoint(
					deliverInfo.getDeliverAddressLng(),
					deliverInfo.getDeliverAddressLat(),
					deliverInfo.getDeliverAddress()));
		}
		if(points.size() >= 2){
			List<BNaviPoint> twoPoints = new ArrayList<BNaviPoint>();
			twoPoints.add(points.get(0));
			twoPoints.add(points.get(1));
			return twoPoints;
		}
		return points;
	}

	private static class BaiduCoordinateTrans {

		private static double x_pi = Math.PI * 3000.0 / 180.0;

		/**
		 * BD-09 坐标转换成GCJ-02 坐标
		 * 
		 * @param bd_lon
		 *            经度
		 * @param bd_lat
		 *            纬度
		 */

		private static double[] bd_decrypt(double bd_lon, double bd_lat) {
			double[] d = new double[2];
			double x = bd_lon - 0.0065, y = bd_lat - 0.006;
			double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
			double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
			double gg_lon = z * Math.cos(theta);
			double gg_lat = z * Math.sin(theta);
			d[0] = gg_lon;
			d[1] = gg_lat;
			return d;
		}

		private static BNaviPoint createBNaviPoint(String longitude,
				String latitude, String name) {
			double[] d = BaiduCoordinateTrans
					.bd_decrypt(Double.parseDouble(longitude),
							Double.parseDouble(latitude));
			return new BNaviPoint(d[0], d[1], name);
		}
		
		static GeoPoint createGeoPoint(String lng, String lat) {
			return createGeoPoint(Double.parseDouble(lng), Double.parseDouble(lat));
		}
		
		static GeoPoint createGeoPoint(double lng,
				double lat) {
			//使用正确的gcj坐标算路
			double[] d = BaiduCoordinateTrans
					.bd_decrypt(lng,lat);
			Double lngE6 = d[0] * 1E5;
			Double latE6 = d[1] * 1E5;
			return new GeoPoint(lngE6.intValue(), latE6.intValue());
		}
	}
}
