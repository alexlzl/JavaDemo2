package cn.bluerhino.driver.controller.server;

import android.content.Context;
import android.content.Intent;
import cn.bluerhino.driver.model.BRLocation;
import cn.bluerhino.driver.model.Key;
import cn.bluerhino.driver.utils.LocalBroadCastUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationServer {

	public static final String ACTION_LOCATION_CHANGED = "cn.bluerhino.location_CHANGED";

	private static final int MIN_SCAN_SPAN_NETWORK = 10000;// 10s

	private Context mContext;

	private LocationClient mLocationClient = null;

	/** Baidu location listener callback */
	private BDLocationListener mBDdLocationListener;

	/** custom location listener callback */
	private BRLocationListener mBRLocationListener;

	private Intent mLocationChangedIntent;

	private boolean isRequestLocation;

	public LocationServer(Context context) {
		super();
		this.mContext = context;
		init();
	}

	private void init() {
		initLocationClient();
	}

	private void initLocationClient() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setScanSpan(MIN_SCAN_SPAN_NETWORK);
		option.setCoorType("bd09ll");
		option.setOpenGps(true);
		option.setIsNeedAddress(true);

		mLocationClient = new LocationClient(mContext, option);
		mBDdLocationListener = new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				parserBDLocation(location);
				stopLocation();
			}
		};

		mLocationClient.registerLocationListener(mBDdLocationListener);
		mLocationChangedIntent = new Intent(ACTION_LOCATION_CHANGED);
	}

	public void deallocLocationClient() {
		mLocationClient.stop();
		mLocationClient.unRegisterLocationListener(mBDdLocationListener);
		mLocationClient = null;
	}

	public void startLocation() {
		if (isRequestLocation()) {
			return;
		}

		mLocationClient.start();
		isRequestLocation = true;
	}

	public void stopLocation() {
		if (!isRequestLocation()) {
			return;
		}
		mLocationClient.stop();
		isRequestLocation = false;
	}

	private boolean isRequestLocation() {
		return isRequestLocation;
	}

	public void registerLocationListener(BRLocationListener listener) {
		mBRLocationListener = listener;
	}

	public void unRegisterLocationListener() {
		mBRLocationListener = null;
	}

	private void parserBDLocation(BDLocation location) {
		if (null == location) {
			return;
		}

		BRLocation brLotion = new BRLocation(location);
		mLocationChangedIntent.putExtra(Key.EXTRA_BRLOCATION, brLotion);
		sendBroadCast(mLocationChangedIntent);

		if (mBRLocationListener != null) {
			mBRLocationListener.onReceiveLocationComplete(brLotion);
		}
	}

	private void sendBroadCast(Intent intent) {
		LocalBroadCastUtils.getInstance().sendBroadcast(mContext, intent);
	}

	public interface BRLocationListener {
		public void onReceiveLocationComplete(BRLocation location);
	}

}
