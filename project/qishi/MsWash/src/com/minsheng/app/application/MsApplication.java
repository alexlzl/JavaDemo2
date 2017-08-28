package com.minsheng.app.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.json.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.JsonBean;
import com.minsheng.app.entity.UpdateLocation;
import com.minsheng.app.entity.UpdateShopcarParam;
import com.minsheng.app.exception.CrashHandler;
import com.minsheng.app.exception.MsAppExceptionHandler;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.http.DES;
import com.minsheng.app.http.JsonUtil;
import com.minsheng.app.module.appointmentorder.ConfirmOrderCheckObject;
import com.minsheng.app.module.appointmentorder.SelectObject;
import com.minsheng.app.util.FileManager;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.Md5Util;
import com.minsheng.app.util.SharedPreferencesUtil;
import com.minsheng.app.util.StringUtil;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @describe:初始化应用资源
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-10-27下午2:22:16
 * 
 */
public class MsApplication extends Application {
	public static boolean isCheckedVersion = false;
	private static final String TAG = "应用初始化";
	public static Context applicationContext = null;
	public static ImageLoader imageLoader;
	public static DisplayImageOptions options;
	public static DisplayImageOptions optionsGoodsDetail;
	public static DisplayImageOptions options_gray;
	public static DisplayImageOptions options_white;
	public static DisplayImageOptions options_null;
	public static DisplayImageOptions options_headcircle;
	public static DisplayImageOptions options_banner;
	public static ImageLoadingListener animateFirstListener;
	// 全局Handler
	private static Handler globalHandler = null;
	public static String token = "";
	public static String source = "";
	public static String deviceId = "";
	public static String clientVersion = "";
	public static Context mContext;
	public static LoginCallBack loginCallBack;
	/**
	 * 定位
	 */
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	public TextView mLocationResult, logMsg;
	public TextView trigger, exit;
	public Vibrator mVibrator;
	public static double longitude;
	public static double latitude;
	public static String currentLocation;
	// 存储完善订单中，商品选择信息
	public static List<SelectObject> selectList;
	public static List<UpdateShopcarParam> updateShopcarParam;
	public static ArrayList<ConfirmOrderCheckObject> checkContent;
	public static int appointNotRead;
	// 记录消息已读状态值
	public static List<Integer> messageList = new ArrayList<Integer>();
	public static OrderListStateChange orderListChangeListener;
	public static BackHomepageCallback backHomeListener;
	private UpdateLocation updateLocationBean;
	public static  boolean isFirstData=true;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	public interface OrderListStateChange {
		public void orderListChange();
	}

	public static void setOrderListChangeListener(OrderListStateChange listener) {
		orderListChangeListener = listener;
	}

	public interface BackHomepageCallback {
		public void backHomepage();
	}

	public static void setBackHomepageListener(BackHomepageCallback listener) {
		backHomeListener = listener;

	}

	public static void setHandler(Handler handler) {
		globalHandler = handler;
	}

	public static Handler getHandler() {
		return globalHandler;

	}

	/**
	 * 
	 * @describe:初始化
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-10-27下午2:26:22
	 * 
	 */
	private void init() {
		/**
		 * 地图初始化
		 */
		SDKInitializer.initialize(getApplicationContext());
		/**
		 * 定位初始化
		 */
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);
		String token = SharedPreferencesUtil.getSharedPreferencesString(
				getApplicationContext(), SharedPreferencesUtil.LOGIN_TOKEN_SP,
				SharedPreferencesUtil.LOGIN_TOKEN_KEY, "");
		LogUtil.d(TAG, "用户token" + token);
		if (!StringUtil.isEmpty(token)) {
			/**
			 * 存在token
			 */
			LogUtil.d(TAG, "存在用户信息");
		} else {
			LogUtil.d(TAG, "不存在用户信息");
		}

		mContext = this.getApplicationContext();

		applicationContext = getApplicationContext();
		imageLoader = ImageLoader.getInstance();
		/**
		 * 初始化异常处理
		 */
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		if (!MsConfiguration.DEBUG) {
			MsAppExceptionHandler appExceptionHandler = MsAppExceptionHandler
					.getInstance(applicationContext);
			Thread.setDefaultUncaughtExceptionHandler(appExceptionHandler);
		}
		MsAppExceptionHandler.uploadLogInfo(applicationContext);

		initImageLoader();
		selectList = new ArrayList<SelectObject>();
		updateShopcarParam = new ArrayList<UpdateShopcarParam>();
		checkContent = new ArrayList<ConfirmOrderCheckObject>();

	}

	private void updateLocation() {
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("wdLongitude", longitude);
		map.put("wdLatitude", latitude);
		LogUtil.d(TAG, "latitudeD=" + "--" + MsApplication.latitude
				+ "longitudeD=" + "---" + MsApplication.longitude);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.LOGIN_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(applicationContext, paramsIn,
				MsRequestConfiguration.UPDATE_LOCATION,
				new BaseJsonHttpResponseHandler<UpdateLocation>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, UpdateLocation arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerUpdate.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							UpdateLocation arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==");
					}

					@Override
					protected UpdateLocation parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "parseResponse==");
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							updateLocationBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									UpdateLocation.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerUpdate.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerUpdate.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return updateLocationBean;
					}
				});
	}

	/**
	 * 处理更新坐标消息返回
	 */
	Handler handlerUpdate = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:

				if (updateLocationBean != null
						&& updateLocationBean.getCode() == 0) {
					LogUtil.d(TAG, "更新坐标成功");
				} else {
					if (updateLocationBean != null) {
						LogUtil.d(TAG, updateLocationBean.getMsg());
					} else {
						LogUtil.d(TAG, "更新坐标失败");
					}

				}

				break;
			case MsConfiguration.FAIL:
				LogUtil.d(TAG, "更新坐标失败");
				break;
			}
		}

	};

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			// logMsg(sb.toString());
			LogUtil.d(TAG, "定位回调" + sb.toString());
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			LogUtil.d(TAG, "经度，纬度" + longitude + "" + latitude);
			updateLocation();
			currentLocation = location.getAddrStr();
		}

	}

	public static void saveLongitude(Double longitude) {
		SharedPreferencesUtil.writeSharedPreferencesDouble(applicationContext,
				"Longitude", "LongitudeKey", longitude);
	}

	public static Double getlongitude() {
		Double longitude = SharedPreferencesUtil.getSharedPreferencesDouble(
				applicationContext, "Longitude", "LongitudeKey", 0.0);
		return longitude;
	}

	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @describe:设置图片加载参数设置
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3上午10:45:41
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void initImageLoader() {
		options_banner = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.banner_default)
				.showImageForEmptyUri(R.drawable.banner_default)
				.showImageOnFail(R.drawable.banner_default)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 防止内存溢出的
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
		options_headcircle = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.usericon)
				.showImageForEmptyUri(R.drawable.usericon)
				.showImageOnFail(R.drawable.usericon)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 防止内存溢出的
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
		optionsGoodsDetail = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.product_default)
				.showImageForEmptyUri(R.drawable.product_default)
				.showImageOnFail(R.drawable.product_default)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 防止内存溢出的
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.usericon)
				.showImageForEmptyUri(R.drawable.usericon)
				.showImageOnFail(R.drawable.usericon)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 防止内存溢出的
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
		options_gray = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.product_default)
				.showImageForEmptyUri(R.drawable.product_default)
				.showImageOnFail(R.drawable.product_default)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 防止内存溢出的
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
		options_white = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.product_default)
				.showImageForEmptyUri(R.drawable.product_default)
				.showImageOnFail(R.drawable.product_default)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 防止内存溢出的
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
		options_null = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 防止内存溢出的
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
		animateFirstListener = new AnimateFirstDisplayListener();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this.getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)// 加载图片的线程数
				.denyCacheImageMultipleSizesInMemory() // 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置磁盘缓存文件名称
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置加载显示图片队列进程
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 
	 * 
	 * @describe:图片加载状态监听器
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3上午10:46:53
	 * 
	 */
	public static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		public static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/**
	 * 
	 * 
	 * @describe:判断是否登录
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-24下午7:54:28
	 * 
	 */
	public static boolean isLogin() {
		String loginToken = SharedPreferencesUtil.getSharedPreferencesString(
				mContext, SharedPreferencesUtil.LOGIN_TOKEN_SP,
				SharedPreferencesUtil.LOGIN_TOKEN_KEY, "");
		if ("".equals(loginToken)) {
			return false;
		}
		if (loginToken == null) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 * @describe:返回Token值
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-24下午7:54:13
	 * 
	 */
	public static String getLoginToken() {
		String loginToken = SharedPreferencesUtil.getSharedPreferencesString(
				mContext, SharedPreferencesUtil.LOGIN_TOKEN_SP,
				SharedPreferencesUtil.LOGIN_TOKEN_KEY, "");
		return loginToken;
	}

	/**
	 * 
	 * 
	 * @describe:存储登录LOGINTOKEN
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-9下午9:14:10
	 * 
	 */
	public static void saveLoginToken(String token) {
		SharedPreferencesUtil.writeSharedPreferencesString(mContext,
				SharedPreferencesUtil.LOGIN_TOKEN_SP,
				SharedPreferencesUtil.LOGIN_TOKEN_KEY, token);

	}

	/**
	 * 
	 * 
	 * @describe:清除TOKEN
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-25上午10:20:24
	 * 
	 */
	public static void clearToken() {
		/**
		 * 清除TOKEN
		 */
		SharedPreferencesUtil.writeSharedPreferencesString(mContext,
				SharedPreferencesUtil.LOGIN_TOKEN_SP,
				SharedPreferencesUtil.LOGIN_TOKEN_KEY, "");
		/**
		 * 清除用户信息
		 */
		FileManager.deleteFile(MsConfiguration.LOGIN_INFOR_FILE);

	}

	/**
	 * 
	 * 
	 * @describe:获取版本号
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-25上午10:26:15
	 * 
	 */
	public static String getVersionName(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			LogUtil.d("获取版本号", pi.versionName);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.d("获取版本号异常", "");
			return "";
		}

	}

	/**
	 * 
	 * 
	 * @describe:调用登录页面
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-25下午1:00:09
	 * 
	 */
	public static void startLoginForResult(LoginCallBack callBack,
			Activity context) {
		loginCallBack = callBack;
		// Intent intent = new Intent(mContext, Login.class);
		// intent.putExtra(MsConfiguration.FROM_WHERE_KEY,
		// MsConfiguration.FROM_OTHER);
		// context.startActivityForResult(intent,
		// MsConfiguration.LOGIN_OTHER_CODE);
	}

	/**
	 * 
	 * 
	 * @describe:登录后的回调
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-25下午1:00:19
	 * 
	 */

	public interface LoginCallBack {
		public void loginSuccess();

		public void loginFail();
	}

	/**
	 * 
	 * 
	 * @describe:获取定位城市
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-2下午3:06:09
	 * 
	 */
	public String getLocationCity() {
		String city = null;
		city = SharedPreferencesUtil.getSharedPreferencesString(mContext,
				SharedPreferencesUtil.LOCATION_CITY_SP,
				SharedPreferencesUtil.LOCATION_CITY_KEY, "");
		return city;
	}

	/**
	 * 
	 * 
	 * @describe:实体转换为Json字符串
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-11上午11:48:59
	 * 
	 */
	public static String objToString(Object obj) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);
		return jsonString;
	}

	/**
	 * 
	 * 
	 * @describe:MAP集合转换成JSON字符串
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-11下午5:36:06
	 * 
	 */

	public static String getJsonString(Map<?, ?> map) {
		JSONObject obj = null;
		try {
			obj = JsonUtil.getJson(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jsonString = null;
		if (obj != null) {
			jsonString = obj.toString();
		}

		return jsonString;
	}

	/**
	 * 
	 * 
	 * @describe:该方式将MAP中键值取出组装JSON
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-13下午9:27:08
	 * 
	 */
	public static String getMapToJson(Map<?, ?> map) {
		String jsonString = null;
		Gson gson = new Gson();
		jsonString = gson.toJson(map);
		return jsonString;
	}

	/**
	 * 
	 * 
	 * @describe:进行接口认证
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-11下午8:17:06
	 * 
	 */
	public static boolean isEqualKey(String content) {
		Gson gson = new Gson();
		JsonBean bean = gson.fromJson(content, JsonBean.class);
		String auth = bean.getAuth();
		String result = DES.decrypt(bean.getJsonResult());
		String newCode = Md5Util.getMd5(result
				+ MsConfiguration.AUTHENTICATION_SECRET);
		LogUtil.d(TAG, "AUTH==" + auth + "newCode==" + newCode);
		if (auth != null && newCode != null && auth.equals(newCode)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 
	 * @describe:获取数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-11下午8:19:28
	 * 
	 */
	public static String getBeanResult(String content) {

		Gson gson = new Gson();
		JsonBean bean = gson.fromJson(content, JsonBean.class);
		String result = DES.decrypt(bean.getJsonResult());
		LogUtil.d(TAG, "返回视图数据" + result);
		return result;

	}

	/**
	 * 
	 * 
	 * @describe:获取请求参数
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-11下午9:34:24
	 * 
	 */
	public static RequestParams getRequestParams(Map<Object, Object> map,
			RequestParams params, String beanName) {
		String resultString = null;
		map.put("terminal", 0);
		if (map.values() != null && map.values().size() == 0 || map == null) {
			/**
			 * map值为空的情况
			 */
			resultString = "{}";
		} else {
			resultString = MsApplication.getMapToJson(map);
		}
		LogUtil.d(TAG, "参数JSON字符串==" + resultString);
		// 传入实体名称
		params.put(MsConfiguration.BEAN_NAME_KEY, DES.encrypt(beanName));
		// 传入接口认证码
		params.put(
				MsConfiguration.AUTHENTICATION_KEY,
				Md5Util.getMd5(resultString
						+ MsConfiguration.AUTHENTICATION_SECRET));
		// 传入加密后的参数
		params.put(MsConfiguration.PARAMS_KEY, DES.encrypt(resultString));

		return params;
	}

}
