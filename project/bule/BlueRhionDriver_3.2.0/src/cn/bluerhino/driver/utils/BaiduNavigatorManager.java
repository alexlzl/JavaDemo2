//package cn.bluerhino.driver.utils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.os.Environment;
//import android.widget.Toast;
//import cn.bluerhino.driver.ApplicationController;
//import cn.bluerhino.driver.model.DeliverInfo;
//import cn.bluerhino.driver.model.LocationInfo;
//import cn.bluerhino.driver.model.OrderInfo;
//
//import com.baidu.lbsapi.auth.LBSAuthManagerListener;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.utils.DistanceUtil;
//import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
//import com.baidu.navisdk.BNaviPoint;
//import com.baidu.navisdk.BaiduNaviManager;
//import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
//import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
//
//public class BaiduNavigatorManager {
//
//	private boolean mIsEngineInitSuccess = false;
//
//	private static BaiduNavigatorManager INSTANCE;
//
//	private Activity mActivity;
//
//	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
//		public void engineInitSuccess() {
//			mIsEngineInitSuccess = true;
//		}
//
//		public void engineInitStart() {
//		}
//
//		public void engineInitFail() {
//		}
//	};
//
//	public synchronized static BaiduNavigatorManager getInstance() {
//		if (null == INSTANCE) {
//			synchronized (BaiduNavigatorManager.class) {
//				if (null == INSTANCE) {
//					INSTANCE = new BaiduNavigatorManager();
//				}
//			}
//		}
//		return INSTANCE;
//	}
//
//	public void initBaiduNaviEngine(Activity activityContext) {
//		if (isEngineInited(activityContext)) {
//			return;
//		}
//		mActivity = activityContext;
//		if (mActivity != null) {
//			BaiduNaviManager naviManager = BaiduNaviManager.getInstance();
//			if (naviManager != null && !StringUtil.isEmpty(getSdcardDir())) {
//				naviManager.initEngine(mActivity, getSdcardDir(), mNaviEngineInitListener, new LBSAuthManagerListener() {
//                    @Override
//                    public void onAuthResult(int status, String msg) {
//                        String str = null;
//                        if (0 == status) {
//                            str = "key校验成功!";
//                        } else {
//                            str = "key校验失败, " + msg;
//                        }
//                    }
//				});
//			}
//
//		}
//
//	}
//
//	private Boolean isEngineInited(Activity activityContext) {
//		return BaiduNaviManager.getInstance().checkEngineStatus(
//				activityContext.getApplicationContext());
//	}
//
//	private String getSdcardDir() {
//		if (Environment.getExternalStorageState().equalsIgnoreCase(
//				Environment.MEDIA_MOUNTED)) {
//			return Environment.getExternalStorageDirectory().toString();
//		}
//		return "";
//	}
//
//	/**
//	 * 
//	 * @param bNaviPoints
//	 *            路线点列表
//	 * @param routPlanMode
//	 *            算路方式
//	 * @param isGPSNav
//	 *            是否使用GPS,真实导航
//	 * @param strategy
//	 *            在离线策略
//	 * @param listener
//	 *            事件回调
//	 * @author chowjee
//	 * @date 2015-4-22
//	 */
//	private void launchNavigator(List<BNaviPoint> bNaviPoints,
//			int routPlanMode, boolean isGPSNav, int strategy,
//			BaiduNaviManager.OnStartNavigationListener listener) {
//
//		BaiduNaviManager.getInstance().launchNavigator(mActivity, bNaviPoints, // 路线点列表
//				routPlanMode, // 算路方式
//				isGPSNav, // 真实导航
//				strategy, // 在离线策略
//				listener);// 跳转监听
//	}
//
//	public void launchNavigator(Builder builder) {
//		launchNavigator(builder.mNaviPoints, builder.mRoutPlanMode,
//				builder.isGPSNav, builder.mStrategy,
//				builder.mNavigationListener);
//	}
//
//	public static class Builder {
//		private List<BNaviPoint> mNaviPoints;
//		// private OrderInfo mOrderInfo;
//		private int mRoutPlanMode;
//		private boolean isGPSNav;
//		private int mStrategy;
//		private BaiduNaviManager.OnStartNavigationListener mNavigationListener;
//
//		public Builder() {
//			super();
//			isGPSNav = true;
//			mRoutPlanMode = NE_RoutePlan_Mode.ROUTE_PLAN_MOD_RECOMMEND;
//			mStrategy = BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY;
//		}
//
//		public final Builder setOrderInfo(OrderInfo orderInfo) {
//			mNaviPoints = parseViaPoint(orderInfo);
//			return this;
//		}
//
//		public final Builder setRoutPlanMode(int routPlanMode) {
//			mRoutPlanMode = routPlanMode;
//			return this;
//		}
//
//		public final Builder setEnableGps(boolean enable) {
//			isGPSNav = enable;
//			return this;
//		}
//
//		public final Builder setStrategy(int strategy) {
//			mStrategy = strategy;
//			return this;
//		}
//
//		public final Builder setNavigationListener(
//				OnStartNavigationListener listener) {
//			mNavigationListener = listener;
//			return this;
//		}
//
//		private static List<BNaviPoint> parseViaPoint(OrderInfo orderInfo) {
//			List<BNaviPoint> viaPoints = new ArrayList<BNaviPoint>();
//			if (orderInfo == null
//					|| orderInfo.getFlag() >= OrderInfo.OrderState.SERVICEFINISH) {
//				return viaPoints;
//			}
//
//			if (orderInfo.getFlag() < OrderInfo.OrderState.DEPARTURESHIPADDRESS) {
//				viaPoints.add(new BNaviPoint(Double.parseDouble(orderInfo
//						.getPickupAddressLng()), Double.parseDouble(orderInfo
//						.getPickupAddressLat()), "发货地",
//						BNaviPoint.CoordinateType.BD09_MC));
//			}
//
//			List<DeliverInfo> deliverInfos = orderInfo.getDeliver();
//			String deliverAddressFormat = "收货地 %d";
//			if (orderInfo.getFlag() < OrderInfo.OrderState.SERVICEFINISH
//					&& deliverInfos != null) {
//				for (int i = 0; i < deliverInfos.size(); i++) {
//					double lng = Double.parseDouble(deliverInfos.get(i)
//							.getDeliverAddressLng());
//					double lat = Double.parseDouble(deliverInfos.get(i)
//							.getDeliverAddressLat());
//					viaPoints.add(new BNaviPoint(lng, lat, String.format(
//							deliverAddressFormat, (i + 1)),
//							BNaviPoint.CoordinateType.BD09_MC));
//				}
//			}
//
//			if (viaPoints.isEmpty()) {
//				return viaPoints;
//			}
//
//			LocationInfo locationInfo = ApplicationController.getInstance()
//					.getLastLocationInfo();
//			LatLng sp = new LatLng(locationInfo.getLatitude(),
//					locationInfo.getLongitude());
//			LatLng ep = new LatLng(viaPoints.get(0).getLatitude(), viaPoints
//					.get(0).getLongitude());
//
//			double dis = DistanceUtil.getDistance(sp, ep);
//			if (dis > 100) {
//				viaPoints.add(0, new BNaviPoint(locationInfo.getLongitude(),
//						locationInfo.getLatitude(), "起始点",
//						BNaviPoint.CoordinateType.BD09_MC));
//
//			}
//			return viaPoints;
//		}
//	}
//}
