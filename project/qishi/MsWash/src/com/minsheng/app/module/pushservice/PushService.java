package com.minsheng.app.module.pushservice;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.Homepage;
import com.minsheng.app.entity.UpdateLocation;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.main.HomeActivity;
import com.minsheng.app.util.LogUtil;
import com.minsheng.wash.R;
import com.minsheng.wash.Test;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

/**
 * 
 * 
 * @describe:推送服务
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-3下午5:46:49
 * 
 */
public class PushService extends Service {
	private static final String TAG = "PushService";
	private AlarmManager mAlarmManager = null;
	private PendingIntent mPendingIntent = null;
	private Homepage homePageData;
	private Context context;
	public static final String NUM_COUNT_RECEIVER = "com.min.musicdemo.action.NUM_COUNT";
	private UpdateLocation updateLocationBean;

	@Override
	public void onCreate() {
		Intent intent = new Intent(getApplicationContext(), PushService.class);
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		mPendingIntent = PendingIntent.getService(this, 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 1000*60,
				mPendingIntent);
		/**
		 * 设置提示
		 */
		context = getApplicationContext();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/**
		 * 更新预约订单消息
		 */
		getOrderInfor();
		/**
		 * 更新地址坐标
		 */
		// updateLocation();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	/**
	 * 
	 * 
	 * @describe:获取首页订单信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-28下午1:23:45
	 * 
	 */
	private void getOrderInfor() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		getHomepageData(map, MsRequestConfiguration.HOME_PAGE,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
	}

	/**
	 * 
	 * 
	 * @describe:发起预约订单请求
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-28下午1:24:21
	 * 
	 */
	public void getHomepageData(Map<Object, Object> map, String url,
			String className) {
		RequestParams paramsIn = new RequestParams();
		paramsIn = MsApplication.getRequestParams(map, paramsIn, className);
		BasicHttpClient.getInstance().post(this.getApplicationContext(),
				paramsIn, url, new BaseJsonHttpResponseHandler<Homepage>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, Homepage arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerHomepage.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							Homepage arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==");
					}

					@Override
					protected Homepage parseResponse(String arg0, boolean arg1)
							throws Throwable {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "parseResponse==");
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							homePageData = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									Homepage.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerHomepage.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerHomepage.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return homePageData;
					}
				});
	}

	/**
	 * 处理首页数据返回
	 */
	Handler handlerHomepage = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (homePageData != null && homePageData.getCode() == 0) {
					if (homePageData.getInfo().getNoCheckMakeNum() == MsApplication.appointNotRead) {
						LogUtil.d(TAG, "没有新订单");
					}
					LogUtil.d(TAG, "old order" + MsApplication.appointNotRead);
					if (homePageData.getInfo().getNoCheckMakeNum() > MsApplication.appointNotRead) {
						/**
						 * 有新订单
						 */
						LogUtil.d(TAG, "有新订单"
								+ homePageData.getInfo().getNoCheckMakeNum());
						MsApplication.appointNotRead = homePageData.getInfo()
								.getNoCheckMakeNum();
						LogUtil.d(TAG, "new order="
								+ MsApplication.appointNotRead);
						/**
						 * 发送提示
						 */
						Intent intent1 = new Intent(NUM_COUNT_RECEIVER);
						sendBroadcast(intent1);
						// if (!MsApplication.isFirstData) {
						// playVoice();
						// }
						playVoice();
					} else {
						LogUtil.d(TAG, "没有新订单");
					}

				} else {
					if (homePageData != null) {
						LogUtil.d(TAG, homePageData.getMsg());
					} else {
						LogUtil.d(TAG, "获取新订单失败");
					}

				}
				break;
			case MsConfiguration.FAIL:
				LogUtil.d(TAG, "获取新订单失败");
				break;
			}
		}

	};

	public void show() {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);

		builder.setContentTitle("tt");

		builder.setContentText("ss");

		builder.setSmallIcon(R.drawable.msxq_icon)
				.setWhen(System.currentTimeMillis()).setAutoCancel(true);
		Intent intent = new Intent(this, HomeActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context,
				R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);

		Notification n = builder.build();
		nm.notify(n.hashCode(), n);

	}

	private void showNotification(boolean finish) {
		Notification notification;
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent intent = new Intent(this, Test.class);// 下拉点击执行的activity
		PendingIntent contentIntent = PendingIntent.getActivity(this,
				R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		if (!finish) {
			// 图标 下载提示 时间
			notification = new Notification(R.drawable.msxq_icon, "开始下载",
					System.currentTimeMillis());
			// 显示在拉伸状态栏中的Notification属性，点击后将发送PendingIntent对象。
			notification.setLatestEventInfo(this, "下载", "正在下载中", contentIntent);
			notification.flags = notification.FLAG_AUTO_CANCEL;
		} else {
			notification = new Notification(R.drawable.msxq_icon, "下载完毕",
					System.currentTimeMillis());
			notification
					.setLatestEventInfo(this, "下载", "程序下载完毕", contentIntent);
		}
		// 下载的默认声音
		notification.defaults = Notification.DEFAULT_SOUND;
		// r.layout.main是为manager指定一个唯一的id
		manager.notify(R.layout.homepage, notification);// 将自定义的notification放入NotificationManager
		playVoice();
	}

	/**
	 * 
	 * 
	 * @describe:语音提示订单消息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-28下午1:27:49
	 * 
	 */
	private void playVoice() {

		// // 指定声音池的最大音频流数目为10，声音品质为5
		// pool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		// // 载入音频流，返回在池中的id
		// final int sourceid = pool.load(this, R.raw.pj, 0);
		// //
		// 播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环;第六个参数为速率，速率
		// // 最低0.5最高为2，1代表正常速度
		// pool.play(sourceid, 1, 1, 0, -1, 1);
		/**
		 * 如果SoundPool刚调完加载load函数之后，直接调用SoundPool的play函数可能出现 error
		 * "sample 1 not READY"
		 * 所以建议，调用加载资源函数load之后，实现资源加载结束的监听函数，在这个监听到资源加载结束之后，播放音频文件。 如：
		 */
		SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
		// 载入音频流，返回在池中的id
		final int sourceid = soundPool.load(this, R.raw.new_order, 0);
		// 播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环;第六个参数为速率，速率最低0.5最高为2，1代表正常速度
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				// TODO Auto-generated method stub
				soundPool.play(sourceid, 2, 2, 0, 0, 1);
			}
		});
	}

	/**
	 * 
	 * 
	 * @describe:更新骑士地址坐标
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-28下午1:16:08
	 * 
	 */
	private void updateLocation() {
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("wdLongitude", MsApplication.longitude);
		map.put("wdLatitude", MsApplication.latitude);
		LogUtil.d(TAG, "latitudeD=" + "--" + MsApplication.latitude
				+ "longitudeD=" + "---" + MsApplication.longitude);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.LOGIN_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(context, paramsIn,
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
}
