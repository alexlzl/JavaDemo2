package com.minsheng.wash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.navigation.BNavigatorActivity;
import com.minsheng.app.scanning.activity.CaptureActivity;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.view.WheelView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @describe:
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-2-3下午4:54:56
 * 
 */
public class Test extends Activity implements OnClickListener {
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	Marker markera, markerb;
	public LocationClient mLocationClient = null;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private TextView LocationResult;
	private static final String[] TIME_QUANTUM_INIT = { "9:00-11:00",
			"10:00-12:00", "11:00-13:00", "12:00-14:00", "13:00-15:00",
			"14:00-16:00", "15:00-17:00", "16:00-18:00" };
	private String serviceTime;
	private String serviceTimeDisplay;
	/**
	 * 导航
	 */
	private boolean mIsEngineInitSuccess = false;
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		public void engineInitSuccess() {
			// 导航初始化是异步的，需要一小段时间，以这个标志来识别引擎是否初始化成功，为true时候才能发起导航
			mIsEngineInitSuccess = true;
		}

		public void engineInitStart() {
		}

		public void engineInitFail() {
		}
	};

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/**
		 * 导航部分-=========================
		 */
		// 初始化导航引擎
		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
				mNaviEngineInitListener, "YsoYgHFXWrVTPtr2NU6DCCOq", null);
		/**
		 * 地图部分========================
		 */
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.test);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 卫星地图
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		// mBaiduMap = mMapView.getMap();
		// 开启交通图
		mBaiduMap.setTrafficEnabled(true);
		// 定义Maker坐标点
		LatLng pointa = new LatLng(39.963175, 116.400244);
		LatLng pointb = new LatLng(39.86923, 116.397428);
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.map_location_user, null);
		ImageView iv = (ImageView) view.findViewById(R.id.iv);
		Bitmap bitmapO = getBitmapFromView(view);
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(bitmapO);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(pointa)
				.icon(bitmap).zIndex(9).draggable(true);
		OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00)
				.fontSize(24).fontColor(0xFFFF00FF).text("百度地图SDK").rotate(-30)
				.position(pointb);
		// 在地图上添加Marker，并显示
		// markera = (Marker) mBaiduMap.addOverlay(textOption);
		markerb = (Marker) mBaiduMap.addOverlay(option);

		// 将marker添加到地图上
		// marker = (Marker) (mBaiduMap.addOverlay(option));
		// 第二步，设置监听方法：
		// 调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
		// mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
		// public void onMarkerDrag(Marker marker) {
		// //拖拽中
		// }
		//
		// public void onMarkerDragEnd(Marker marker) {
		// // 拖拽结束
		// }
		// public void onMarkerDragStart(Marker marker) {
		// //开始拖拽
		// }
		// });
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				if (arg0 == markera) {
					Toast.makeText(Test.this, "makera", 1000).show();
				}
				if (arg0 == markerb) {
					Toast.makeText(Test.this, "makerb", 1000).show();
				}
				return true;
				// 获得marker中的数据
				// Info info = (Info) marker.getExtraInfo().get("info");
				//
				// InfoWindow mInfoWindow = null;
				// // 生成一个TextView用户在地图中显示InfoWindow
				// TextView location = new TextView(getApplicationContext());
				// location.setBackgroundResource(R.drawable.ic_launcher);
				// location.setPadding(30, 20, 30, 50);
				// location.setText(info.getName());
				// // 将marker所在的经纬度的信息转化成屏幕上的坐标
				// final LatLng ll = marker.getPosition();
				// Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				// p.y -= 47;
				// LatLng llInfo =
				// mBaiduMap.getProjection().fromScreenLocation(p);
				// 为弹出的InfoWindow添加点击事件
				// mInfoWindow = new InfoWindow(location, llInfo,
				// new OnInfoWindowClickListener()
				// {
				//
				// @Override
				// public void onInfoWindowClick()
				// {
				// // 隐藏InfoWindow
				// mBaiduMap.hideInfoWindow();
				// }
				// });
				// 显示InfoWindow
				// mBaiduMap.showInfoWindow(mInfoWindow);
				// 设置详细信息布局为可见
				// mMarkerInfoLy.setVisibility(View.VISIBLE);
				// 根据商家信息为详细信息布局设置信息
				// popupInfo(mMarkerInfoLy, info);
				// return true;
			}
		});
		/**
		 * ===============================================================定位部分
		 */
		mLocationClient = ((MsApplication) getApplication()).mLocationClient;

		LocationResult = (TextView) findViewById(R.id.textView1);
		((MsApplication) getApplication()).mLocationResult = LocationResult;

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

	/**
	 * 将View转换为Bitmap
	 * 
	 * @param view
	 * @return
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

	/**
	 * 根据base64字符串获取Bitmap位图 getBitmap
	 * 
	 * @throws
	 */
	public static Bitmap getBitmap(String imgBase64Str) {
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(imgBase64Str, Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void getLocation(View view) {
		Toast.makeText(Test.this, "location", 1000).show();
		InitLocation();
		mLocationClient.start();
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 3000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	public void nav(View view) {
		// 这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标
		BaiduNaviManager.getInstance().launchNavigator(this, 40.05087,
				116.30142, "百度大厦", 39.90882, 116.39750, "北京天安门",
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
				true, // 真实导航
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
				new OnStartNavigationListener() { // 跳转监听

					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(Test.this,
								BNavigatorActivity.class);
						intent.putExtras(configParams);
						startActivity(intent);
					}

					@Override
					public void onJumpToDownloader() {
					}
				});
		// BaiduNaviManager.getInstance().launchNavigator(this, 40.05087,
		// 116.30142, "当前位置", 39.90882, 116.39750, "知春路",
		// NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
		// true, // 真实导航
		// BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
		// new OnStartNavigationListener() { // 跳转监听
		//
		// @Override
		// public void onJumpToNavigator(Bundle configParams) {
		// Intent intent = new Intent(Test.this,
		// BNavigatorActivity.class);
		// intent.putExtras(configParams);
		// startActivity(intent);
		// }
		//
		// @Override
		// public void onJumpToDownloader() {
		// }
		// });
		// launchNavigator2();
	}

	private void launchNavigator2() {
		BNaviPoint startPoint = new BNaviPoint(116.307854, 40.055878, "百度大厦",
				BNaviPoint.CoordinateType.BD09_MC);
		BNaviPoint endPoint = new BNaviPoint(116.403875, 39.915168, "北京天安门",
				BNaviPoint.CoordinateType.BD09_MC);
		BaiduNaviManager.getInstance().launchNavigator(this,
				startPoint, // 起点（可指定坐标系）
				endPoint, // 终点（可指定坐标系）
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME,// 算路方式
				true, // 真实导航
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY,
				new OnStartNavigationListener() {

					@Override
					public void onJumpToDownloader() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onJumpToNavigator(Bundle arg0) {
						// TODO Auto-generated method stub

					}
				} // 跳转监听
				);
	}

	public void intoScan(View view) {
		Intent intent = new Intent(Test.this, CaptureActivity.class);
		MsStartActivity.startActivity(this, intent);

	}

	public void intoList(View view) {
//		Intent intent = new Intent(Test.this, Tlist.class);
//		MsStartActivity.startActivity(this, intent);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.custom_date_picker, null);
		final WheelView date_wv = (WheelView) view.findViewById(R.id.date_wv);
		final WheelView time_wv = (WheelView) view.findViewById(R.id.time_wv);
		date_wv.setItems(getDateList());
		time_wv.setItems(getTimeList(false));
		date_wv.setSeletion(0);
		time_wv.setSeletion(0);

		date_wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				if (selectedIndex > 1) {
					time_wv.setItems(getTimeList(true));
					time_wv.setSeletion(0);
				} else {
					time_wv.setItems(getTimeList(false));
					time_wv.setSeletion(0);
				}
			}
		});

		time_wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
			}
		});

		builder.setView(view);
		builder.setTitle("选择洗车时间");
		builder.setPositiveButton("确  定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						serviceTime = dates.get(date_wv.getSeletedIndex())
								+ " " + time_wv.getSeletedItem();
						System.out.println("serviceTime = " + serviceTime);
						serviceTimeDisplay = date_wv.getSeletedItem() + "  "
								+ time_wv.getSeletedItem();
						// serviceTimeEt.setText(serviceTimeDisplay);
						dialog.cancel();
					}
				});

		Dialog dialog = builder.create();
		dialog.show();
	}

	List<String> dates = new ArrayList<String>();

	private List<String> getDateList() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour > 16) {
			String[] dateNames = new String[2];
			for (int i = 0; i < dateNames.length; i++) {
				cal.add(Calendar.DATE, 1);
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				int date = cal.get(Calendar.DATE);
				dateNames[i] = month + "月" + date + "日" + getWeekOfDate(cal);
				dates.add(year + "-" + (month < 10 ? "0" + month : month) + "-"
						+ (date < 10 ? "0" + date : date) + "");
			}
			return Arrays.asList(dateNames);
		} else {
			String[] dateNames = new String[3];
			for (int i = 0; i < dateNames.length; i++) {
				if (i == 0) {
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH) + 1;
					int date = cal.get(Calendar.DATE);
					dateNames[i] = "今天 " + getWeekOfDate(cal);
					dates.add(year + "-" + (month < 10 ? "0" + month : month)
							+ "-" + (date < 10 ? "0" + date : date) + "");
				} else {
					cal.add(Calendar.DATE, 1);
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH) + 1;
					int date = cal.get(Calendar.DATE);
					dateNames[i] = month + "月" + date + "日"
							+ getWeekOfDate(cal);
					dates.add(year + "-" + (month < 10 ? "0" + month : month)
							+ "-" + (date < 10 ? "0" + date : date) + "");
				}

			}
			return Arrays.asList(dateNames);
		}
	}

	private List<String> getTimeList(boolean showAll) {
		List<String> initList = new ArrayList<String>();
		if (showAll) {
			initList.add(0, "全天");
			initList.addAll((List<String>) Arrays.asList(TIME_QUANTUM_INIT));
			return initList;
		}

		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);

		if (hour < 9 || hour > 16) {
			initList.add(0, "全天");
			initList.addAll((List<String>) Arrays.asList(TIME_QUANTUM_INIT));
			return initList;
		}

		initList.addAll((List<String>) Arrays.asList(TIME_QUANTUM_INIT));
		String startTimeQuantum = hour + ":00-" + (hour + 2) + ":00";
		int startPoint = initList.indexOf(startTimeQuantum);
		List<String> subList = initList.subList(startPoint, initList.size());

		return subList;
	}

	public String getWeekOfDate(Calendar cal) {
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
}
