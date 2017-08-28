package cn.bluerhino.driver.controller.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bluerhino.library.network.framework.BRErrorListener;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.service.ForegroundService;

import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.controller.activity.LoginActivity;
import cn.bluerhino.driver.model.LocationInfo;
import cn.bluerhino.driver.network.UploadPoiRequest;
import cn.bluerhino.driver.utils.Info2FileUtil;

public class LocationService extends ForegroundService {

	private static final String TAG = LocationService.class.getSimpleName();
	private static final String REQUEST_TAG = TAG;
	public static final String ACTION_START_LOCATION = "cn.bluerhno.location_START";
	public static final String ACTION_STOP_LOCATION = "cn.bluerhno.location_STOP";
	// for test===============
	private final int MAX_SKIP_COUNT_TEST = 3;
	// 6次数 * 定位间隔10s -> 1min
	private final int MAX_SKIP_COUNT = ApplicationController.testSwitch ? MAX_SKIP_COUNT_TEST
			: 6;
	// for test=========
	private final double MIN_DISTANCE_TEST = 2;
	// 两点之间最短距离需保证最少20m
	private final double MIN_DISTANCE = ApplicationController.testSwitch ? MIN_DISTANCE_TEST
			: 20;
	private RequestQueue mRequestQueue;
	private final List<LocationInfo> mUploadLocationList = new ArrayList<LocationInfo>();
	private LocationClient mLocationClient;
	private BDLocationListener mLocationListener;
	private int mSkipCount = 0;

	private BRRequestParams mRequestParams;
	private final int MAX_ERR_LOC_COUNT_TEST = 6;
	/*
	 * 能容错的定位次数为6次 (* 10s = 60s)
	 */
	private final int MAX_ERR_LOC_COUNT = ApplicationController.testSwitch ? MAX_ERR_LOC_COUNT_TEST
			: 6;

	/**
	 * 当前的已容错的错误次数
	 */
	private int curr_err_count = 0;

	/**
	 * 记录一分钟内经纬度点的列表
	 */
	private List<LatLng> mLocationList;
	/**
	 * 当前是记录第几个点的index
	 */
	private int mIndex = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		initLocationClient();
		initDataRequest();
	}

	/**
	 * 
	 * Describe:初始化定位
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */
	private void initLocationClient() {
		mLocationList = new ArrayList<LatLng>();
		mLocationClient = ApplicationController.getInstance()
				.getLocationClient();
		mLocationListener = new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				parserBDLocation(location);
			}
		};
		mLocationClient.registerLocationListener(mLocationListener);
	}

	/**
	 * 
	 * Describe:初始化上传坐标请求
	 * 
	 * Date:2015-8-10
	 * 
	 * Author:liuzhouliang
	 */
	private void initDataRequest() {
		mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		mRequestParams = new BRRequestParams();
		mRequestParams.setDeviceId(ApplicationController.getInstance()
				.getDeviceId());
		mRequestParams.setVerCode(ApplicationController.getInstance()
				.getVerCode());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String session = ApplicationController.getInstance().getLoginfo()
				.getSessionID();
		if (TextUtils.isEmpty(session)) {
			// session失效,需重新登陆
			/*
			 * ToastUtil.showToast(getApplicationContext(),
			 * R.string.loc_serv_net_error);
			 */
			stopSelf();
			Intent it = new Intent(this, LoginActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(it);
		} else {
			mRequestParams.setToken(session);
			handleStartCommand(intent);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void handleStartCommand(Intent intent) {

		String action = ACTION_START_LOCATION;
		if (intent != null) {
			action = intent.getAction();
			if (TextUtils.equals(ACTION_START_LOCATION, action)) {
				startLocation();
			} else if (TextUtils.equals(ACTION_STOP_LOCATION, action)) {
				stopLocation();
			}
		}
	}

	/**
	 * 执行定位
	 * 
	 * @author chowjee
	 * @date 2014-8-20
	 */
	private void startLocation() {
		mLocationClient.start();
		mLocationClient.requestLocation();
		ApplicationController.getInstance().acquireWakeLock();
	}

	/**
	 * 解析BDLocation
	 * 
	 * @param location
	 * @author chowjee
	 * @date 2014-8-20
	 */
	private void parserBDLocation(BDLocation location) {
		if (ApplicationController.testSwitch) {
			// Info2FileUtil.saveInfo2File(location);
		}

		if (null == location) {
			return;
		}

		LocationInfo locationInfo = new LocationInfo(location);
		/*
		 * LogUtil.d(TAG, "1.百度定位解析到的信息" + locationInfo.toString() +
		 * "curr_err_count==" + curr_err_count + "====mSkipCount==" +
		 * mSkipCount);
		 */

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		/**
		 * 间隔5s存储当前地理位置信息，存储一分钟内的地理位置信息
		 */
		LatLng locationInfor = new LatLng(latitude, longitude);
		if (mIndex < 12) {
			if (mLocationList != null) {
				mLocationList.add(mIndex, locationInfor);
			}
			mIndex = mIndex + 1;
		} else {
			/**
			 * 存储一分钟内的坐标信息
			 */
			if (mLocationList != null) {
				// LogUtil.d(TAG, "存储坐标" + mLocationList.toString());
				ApplicationController.getInstance().setLocationList(
						mLocationList);
				mLocationList.clear();
				mIndex = 0;
			}

		}

		boolean isAvailDot = false;
		// 获得符合要求的点的List(1.满足经纬度的正常范围 2.距离>20)
		if (latitude > 1.0 && longitude > 1.0) {
			double dis = Double.MIN_VALUE;
			LocationInfo lastLocationInfo = ApplicationController.getInstance()
					.getLastLocationInfo();
			LatLng sp = new LatLng(lastLocationInfo.getLatitude(),
					lastLocationInfo.getLongitude());
			LatLng ep = new LatLng(latitude, longitude);
			dis = DistanceUtil.getDistance(sp, ep);
			// LogUtil.d(TAG, "坐标距离----" + (float) dis +
			// "----lastLocationInfo----"
			// + lastLocationInfo.toString());
			/**
			 * test=============
			 */
//			if (ApplicationController.testSwitch) {
//				ToastUtil.showToast(mContext, "实时定位两个坐标距离" + dis);
//			}

			/*LogUtil.d(TAG, "最新坐标"
					+ locationInfo.getLatitude()
					+ "--距离---"
					+ dis
					+ "--"
					+ "时间："
					+ System.currentTimeMillis()
					/ 1000
					+ "-----"
					+ "老坐标"
					+ ApplicationController.getInstance().getLastLocationInfo()
							.getLatitude());*/
			// 距离符合要求
			if (dis > MIN_DISTANCE) {
				isAvailDot = true;
			}
		}
		/**
		 * 满足存储条件：1,坐标到达变化条件：每次都存储;2,坐标未变化，一分钟后存储
		 */
		if (isAvailDot || curr_err_count == MAX_ERR_LOC_COUNT) {
			// mLocationSQLiteHelper.insert(locationInfo);
			saveLocationInfor(locationInfo);
			/*LogUtil.d(TAG, "老坐标"
					+ ApplicationController.getInstance().getLastLocationInfo()
							.getLatitude()
					+ "----"
					+ ApplicationController.getInstance().getLastLocationInfo()
							.getLongitude());
			LogUtil.d(
					TAG,
					"新坐标" + locationInfo.getLatitude() + "----"
							+ System.currentTimeMillis() / 1000 + "-----"
							+ locationInfo.getLongitude());*/
			curr_err_count = 0;
		} else {
			curr_err_count++;
		}

		// 超过6次定位 * 每次定位秒间隔 (6*10) = 1分钟则打包一分钟上传一次
		if (mSkipCount == MAX_SKIP_COUNT) {
			if (prepareUpload()) {
				executeUpload();
			}
			// 清楚坐标点
			clearLocationInfor();
			mSkipCount = 0;
		} else {
			mSkipCount++;
		}
		ApplicationController.getInstance().setLastLocationInfo(locationInfo);
	}

	/**
	 * 
	 * Describe:存储一分钟内的坐标点
	 * 
	 * Date:2015-8-11
	 * 
	 * Author:liuzhouliang
	 */
	private void saveLocationInfor(LocationInfo locationInfo) {
		synchronized (mUploadLocationList) {
			if (ApplicationController.getInstance().locationInfos != null) {
				ApplicationController.getInstance().locationInfos
						.add(locationInfo);
				/*
				 * LogUtil.d( TAG, "存储满足条件坐标点===" +
				 * ApplicationController.getInstance().locationInfos
				 * .toString());
				 */
				/**
				 * test=============
				 */
				if (ApplicationController.testSwitch) {

					/*ToastUtil
							.showToast(
									mContext,
									"实时定位存储满足条件坐标点===数量=="
											+ ApplicationController
													.getInstance().locationInfos
													.size());*/

				}
			}
		}
	}

	/**
	 * 
	 * Describe:在上传坐标成功后，清除之前的坐标数据
	 * 
	 * Date:2015-8-11
	 * 
	 * Author:liuzhouliang
	 */
	private void clearLocationInfor() {

		if (ApplicationController.getInstance().locationInfos != null) {
			ApplicationController.getInstance().locationInfos.clear();
			/*
			 * LogUtil.d( TAG, "清除旧的坐标完成====" +
			 * ApplicationController.getInstance().locationInfos .toString());
			 */
		}
	}

	/**
	 * Describe:停止定位
	 * 
	 * Date:2015-7-29
	 * 
	 * Author:liuzhouliang
	 */
	private void stopLocation() {
		ApplicationController.getInstance().releaseWakeLock();
		mLocationClient.stop();
	}

	/**
	 * 
	 * Describe:准备上传参数
	 * 
	 * Date:2015-8-10
	 * 
	 * Author:liuzhouliang
	 */
	private boolean prepareUpload() {
		// List<LocationInfo> locationInfoList = mLocationSQLiteHelper
		// .quereyUnUpLoad(UPLOAD_LIMIT);

		List<LocationInfo> locationInfoList = ApplicationController
				.getInstance().locationInfos;
		/**
		 * test=============
		 */
		if (ApplicationController.testSwitch) {

			/*ToastUtil.showToast(mContext,
					"实时定位上传坐标长度=====" + locationInfoList.size());*/

		}
		if (null == locationInfoList || locationInfoList.isEmpty()) {
			return false;
		}
		synchronized (mUploadLocationList) {
			mUploadLocationList.clear();
			mUploadLocationList.addAll(locationInfoList);
		}
		JSONArray jsonArray = new JSONArray();
		for (LocationInfo locationInfo : mUploadLocationList) {
			if (null != locationInfo) {
				// LogUtil.d(TAG, locationInfo.toString());
				JSONObject jsonLocation = new JSONObject();
				try {
					jsonLocation.put("lng", "" + locationInfo.getLongitude());
					jsonLocation.put("lat", "" + locationInfo.getLatitude());
					jsonLocation.put("t", locationInfo.getTime());
					jsonLocation.put("isGps",
							"" + locationInfo.getLocationStatus());
					jsonLocation.put("verticalAccuracy",
							locationInfo.getVerticalAccuracy());
					jsonLocation.put("speed", locationInfo.getSpeed());
					jsonLocation.put("course", locationInfo.getCourse());
					jsonLocation.put("altitude", locationInfo.getAltitude());
					jsonArray.put(jsonLocation);
				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		mRequestParams.setToken(ApplicationController.getInstance()
				.getLoginfo().getSessionID());
		mRequestParams.put("json", jsonArray.toString());
		// LogUtil.d(TAG, "3.截取到的上传json" + jsonArray.toString());
		if (ApplicationController.testSwitch) {
			Info2FileUtil.saveInfo2File("上传给后台坐标==数量===" + jsonArray.length()
					+ "====" + jsonArray.toString());
		}
		return true;
	}

	/**
	 * 执行上传
	 * 
	 * @author chowjee
	 * @date 2014-8-20
	 */
	private void executeUpload() {
		BRFastRequest request = new UploadPoiRequest.Builder()
				.setSucceedListener(SucceedResponse)
				.setErrorListener(ErrorResponse).setParams(mRequestParams)
				.setRequestTag(REQUEST_TAG).build();
		mRequestQueue.add(request);
	}

	@Override
	public void onDestroy() {
		stopLocation();
		mRequestQueue.cancelAll(REQUEST_TAG);
		mLocationList.clear();
		mLocationList = null;
		super.onDestroy();
	}

	private final BRJsonResponse SucceedResponse = new BRJsonResponse() {

		@Override
		public void onResponse(JSONObject response) {
			if (!mUploadLocationList.isEmpty()) {
				mUploadLocationList.clear();
			}
		}
	};

	private final BRErrorListener ErrorResponse = new BRErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			/**
			 * do nothing
			 */
		}		
	};
}
