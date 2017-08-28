package com.minsheng.app.module.ordermap;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.util.LogUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:地图页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-4下午7:56:43
 * 
 */
public class MsMapView extends BaseActivity {
	private static final String TAG = "MsMapView";
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private Marker markerUser, markerDriver;
	private LatLng pointUser;
	private LatLng pointDriver;
	private Double latitudeD, longitudeD;
	private String userAddress;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.mapview);
		latitudeD = getIntent().getDoubleExtra("latitudeD", 0.0);
		longitudeD = getIntent().getDoubleExtra("longitudeD", 0.0);
		userAddress = getIntent().getStringExtra("address");
		LogUtil.d(TAG, "latitudeD=" + latitudeD + "longitudeD==" + longitudeD);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.mapview_map);
		mBaiduMap = mMapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 开启交通图
		mBaiduMap.setTrafficEnabled(true);

		// 定义Maker坐标点
		pointUser = new LatLng(latitudeD, longitudeD);
		pointDriver = new LatLng(MsApplication.latitude,
				MsApplication.longitude);
		/**
		 * 设置比例尺
		 */
		MapStatusUpdate user = MapStatusUpdateFactory.newLatLngZoom(pointUser,
				19);
		mBaiduMap.setMapStatus(user);
		// MapStatusUpdate driver = MapStatusUpdateFactory.newLatLngZoom(
		// pointDriver, 19);
		// mBaiduMap.setMapStatus(driver);
		View viewUser = baseLayoutInflater.inflate(R.layout.map_location_user,
				null);
		TextView tvUserLocation = (TextView) viewUser
				.findViewById(R.id.map_location_user_icon);
		tvUserLocation.setText(userAddress);
		View viewDriver = baseLayoutInflater.inflate(
				R.layout.map_location_driver, null);
		TextView tvDriverLocation = (TextView) viewDriver
				.findViewById(R.id.map_location_driver_icon);
		tvDriverLocation.setText(MsApplication.currentLocation);
		// 将VIEW转换为Bitmap
		Bitmap bitmapViewUser = getBitmapFromView(viewUser);
		Bitmap bitmapViewDriver = getBitmapFromView(viewDriver);
		// 构建Marker图标
		BitmapDescriptor bitmapDescriptorUser = BitmapDescriptorFactory
				.fromBitmap(bitmapViewUser);
		BitmapDescriptor bitmapDescriptorDriver = BitmapDescriptorFactory
				.fromBitmap(bitmapViewDriver);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions optionUser = new MarkerOptions().position(pointUser)
				.icon(bitmapDescriptorUser).zIndex(9).draggable(true);
		OverlayOptions optionDriver = new MarkerOptions().position(pointDriver)
				.icon(bitmapDescriptorDriver).zIndex(9).draggable(true);
		// // 添加两个覆盖物
		markerUser = (Marker) mBaiduMap.addOverlay(optionUser);
		markerDriver = (Marker) mBaiduMap.addOverlay(optionDriver);
		/**
		 * 覆盖物添加监听事件
		 */
		// mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
		//
		// @Override
		// public boolean onMarkerClick(Marker arg0) {
		// // TODO Auto-generated method stub
		// if (arg0 == markera) {
		// Toast.makeText(baseContext, "makera", 1000).show();
		// /**
		// * 弹出文本====================
		// */
		// // 构建文字Option对象，用于在地图上添加文字
		// OverlayOptions textOption = new TextOptions()
		// .bgColor(0xAAFFFF00).fontSize(24)
		// .fontColor(0xFFFF00FF).text("百度地图SDK").rotate(-30)
		// .position(pointa);
		// // 在地图上添加该文字对象并显示
		// mBaiduMap.addOverlay(textOption);
		//
		// /**
		// * ===========================弹出窗口
		// */
		// // 创建InfoWindow展示的view
		// Button button = new Button(getApplicationContext());
		// button.setBackgroundResource(android.R.drawable.dialog_frame);
		// // 定义用于显示该InfoWindow的坐标点
		// // 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
		// InfoWindow mInfoWindow = new InfoWindow(button, pointb, -47);
		// // 显示InfoWindow
		// mBaiduMap.showInfoWindow(mInfoWindow);
		// }
		// return true;
		//
		// }
		// });
	}

	/**
	 * 
	 * 
	 * @describe:将View转换为Bitmap
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-4下午8:16:48
	 * 
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "地图";
	}

	@Override
	protected String setRightText() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}
}
