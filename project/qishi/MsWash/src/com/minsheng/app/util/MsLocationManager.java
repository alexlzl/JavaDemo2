package com.minsheng.app.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 
 * 
 * @describe:获取经度纬度
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:29:11
 * 
 */
public class MsLocationManager {
	private static Context mContext;
	private LocationManager gpsLocationManager;
	private LocationManager networkLocationManager;
	// 更新数据的最短时间
	private static final int MINTIME = 2000;
	// 更新数据的最小位移变化
	@SuppressWarnings("unused")
	private static final int MININSTANCE = 1000;
	private static MsLocationManager instance;
	private Location lastLocation = null;
	private static LocationCallBack mCallback;

	public static void init(Context c, LocationCallBack callback) {
		mContext = c;
		mCallback = callback;
	}

	/**
	 * 通过GPS和基站获取经纬度
	 */
	private MsLocationManager() {
		LogUtil.e("定位初始化");
		// Gps 定位
		gpsLocationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		gpsLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		// gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// MINTIME, MININSTANCE, locationListener);
		// 基站定位
		networkLocationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		networkLocationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (networkLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			networkLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, MINTIME, MINTIME,
					locationListener);
		}

		if (gpsLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			gpsLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, MINTIME, MINTIME,
					locationListener);
		}
	}

	/**
	 * 
	 * 
	 * @describe:获取单例
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:29:39
	 * 
	 */
	public static MsLocationManager getInstance() {
		if (null == instance) {
			instance = new MsLocationManager();
		}
		return instance;
	}

	/**
	 * 
	 * @describe:更新经纬度方法
	 * 
	 * @author:liuzhouliang
	 * 
	 * @time:2014-2-10上午10:26:09
	 */
	private void updateLocation(Location location) {
		lastLocation = location;
		mCallback.onCurrentLocation(location);
	}

	/**
	 * 定位监听器
	 */
	private final LocationListener locationListener = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

		public void onLocationChanged(Location location) {
			/**
			 * 获取Location
			 */
			updateLocation(location);
			LogUtil.e("定位监听器");
		}
	};

	public Location getMyLocation() {
		return lastLocation;
	}

	public interface LocationCallBack {

		void onCurrentLocation(Location location);
	}

	public void destoryLocationManager() {
		gpsLocationManager.removeUpdates(locationListener);
		networkLocationManager.removeUpdates(locationListener);
	}
}
