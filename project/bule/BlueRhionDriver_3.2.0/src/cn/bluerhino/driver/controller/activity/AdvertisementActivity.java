package cn.bluerhino.driver.controller.activity;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import com.bluerhino.library.utils.WeakHandler;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.network.LoadingURLRequest;
import cn.bluerhino.driver.utils.BrStartActivity;
import cn.bluerhino.driver.utils.ConstantsManager;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.bluerhino.driver.view.ViewUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

/**
 * Describe:广告页面
 * 
 * Date:2015-7-20
 * 
 * Author:liuzhouliang
 */
public class AdvertisementActivity extends BaseParentActivity {
	private static final String TAG = AdvertisementActivity.class.getName();
	private static ImageView ivImageView;
	private final int SPLASH_DISPLAY_LENGH = 2000;
	@SuppressWarnings("unused")
	private MessageHandler messageHandler;
	private String fromString;
	private String imageURL = "http://b.hiphotos.baidu.com/image/pic/item/4a36acaf2edda3ccbd71393602e93901213f920b.jpg";

	static class MessageHandler extends WeakHandler<AdvertisementActivity> {

		public MessageHandler(AdvertisementActivity reference) {
			super(reference);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				Bundle dataBundle = msg.getData();
				Bitmap bitmap = ViewUtil.getBitmap(dataBundle
						.getByteArray("1001"));
				ivImageView.setImageBitmap(bitmap);
				break;

			default:
				break;
			}
		}

	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		init();
		getChildView();
		getImageURL();
		// getBitmap();
	}

	/**
	 * 
	 * Describe:初始化
	 * 
	 * Date:2015-7-22
	 * 
	 * Author:liuzhouliang
	 */
	private void init() {
		setContentView(R.layout.activity_advertisement);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		messageHandler = new MessageHandler(this);
		getIntentData();
		// if (Build.VERSION.SDK_INT > 11 || Build.VERSION.SDK_INT == 11) {
		// setFinishOnTouchOutside(false);
		// }

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& isOutOfBounds(this, event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	private boolean isOutOfBounds(Activity context, MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context)
				.getScaledWindowTouchSlop();
		final View decorView = context.getWindow().getDecorView();
		return (x < -slop) || (y < -slop)
				|| (x > (decorView.getWidth() + slop))
				|| (y > (decorView.getHeight() + slop));
	}

	private void getChildView() {
		// ivImageView = (NetworkImageView)
		// findViewById(R.id.activity_advertisement_iv);
		ivImageView = (ImageView) findViewById(R.id.activity_advertisement_iv);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			setIntent(intent);
		}
		getIntentData();
	}

	private void getIntentData() {
		fromString = getIntent().getStringExtra(ConstantsManager.FROM_WHERE);

	}

	/**
	 * 
	 * Describe:获取广告图片
	 * 
	 * Date:2015-7-22
	 * 
	 * Author:liuzhouliang
	 */
	private void getBitmap() {

		ImageRequest imageRequest = new ImageRequest(imageURL,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						LogUtil.d(TAG, "onResponse==" + "fromString=="
								+ fromString);
						// Message message = Message.obtain();
						// message.what = 1001;
						// Bundle bundle = new Bundle();
						// bundle.putByteArray("1001",
						// ViewUtil.getBytes(response));
						// message.setData(bundle);
						// messageHandler.sendMessage(message);
						ivImageView.setImageBitmap(response);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								handleCallback();
							}
						}, SPLASH_DISPLAY_LENGH);
					}
				}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// ivImageView.setImageResource(R.drawable.ic_launcher);
						LogUtil.d(TAG, "onErrorResponse" + "fromString=="
								+ fromString);
						handleCallback();
					}
				});
		ApplicationController.getInstance().getRequestQueue().add(imageRequest);

	}

	/**
	 * 
	 * Describe:获取广告图片链接
	 * 
	 * Date:2015-7-22
	 * 
	 * Author:liuzhouliang
	 */
	private void getImageURL() {
		BRRequestParams params = new BRRequestParams();
		params.setDeviceId(ApplicationController.getInstance().getDeviceId());
		params.setVerCode(ApplicationController.getInstance().getVerCode());
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int density = displayMetrics.densityDpi;
		LogUtil.d(TAG, "屏幕密度==" + density);
		int type = 4;
		switch (density) {
		case DisplayMetrics.DENSITY_XXHIGH:// 480
			type = 4;
			break;
		case DisplayMetrics.DENSITY_XHIGH:// 320
			type = 3;
			break;
		case DisplayMetrics.DENSITY_HIGH:// 240
			type = 2;
			break;
		case DisplayMetrics.DENSITY_DEFAULT:// 160
			type = 1;
			break;
		default:
			type = 4;
			break;
		}
		params.put("sr", "android_" + type);
		BRJsonResponse succeedListener = new BRJsonResponse() {

			@SuppressWarnings("unused")
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.d(TAG, "返回数据" + response.toString());

				if (response != null) {
					try {
						if (response.has("ret")) {
							int type = response.getInt("ret");
							LogUtil.d(TAG, "type==" + type);
							switch (type) {
							case 0:
								/**
								 * 没有启动图片
								 */
								handleCallback();
								break;
							case 1:
								/**
								 * 有启动图片
								 */
								if (response.has("picture")
										|| !StringUtil.isEmpty(response
												.getString("picture"))) {
									imageURL = response.getString("picture");
									LogUtil.d(TAG, "图片url==" + imageURL);
									/**
									 * 请求图片=================
									 */
									getBitmap();
								} else {
									handleCallback();
								}
								break;

							default:
								handleCallback();
								break;
							}
						} else {
							handleCallback();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					handleCallback();
				}
			}
		};

		BRJsonRequest loginRequest = new LoadingURLRequest.Builder()
				.setSucceedListener(succeedListener)
				.setErrorListener(new VolleyErrorListener(mContext) {
					@Override
					public void onErrorResponse(VolleyError error) {
						super.onErrorResponse(error);
						LogUtil.d(TAG, "异常返回数据" + error.getMessage());
						handleCallback();
					}
				}).setParams(params).build();
		LogUtil.d(TAG, "请求参数===" + params.toString());
		ApplicationController.getInstance().getRequestQueue().add(loginRequest);
	}

	/**
	 * 
	 * Describe:处理页面跳转
	 * 
	 * Date:2015-7-24
	 * 
	 * Author:liuzhouliang
	 */
	private void handleCallback() {
		if ("MainActivity".equals(fromString)) {
			finish();
		} else {
			Intent intent = new Intent(mContext, LoginActivity.class);
			BrStartActivity.startActivity(AdvertisementActivity.this, intent);
			finish();
		}

	}

	@SuppressWarnings("unused")
	private void imageRequest2() {
		/**
		 * 方式2
		 */
		// LruImageCache lruImageCache = LruImageCache.instance();
		//
		// ImageLoader imageLoader = new ImageLoader(ApplicationController
		// .getInstance().getRequestQueue(), lruImageCache);
		// ivImageView.setDefaultImageResId(R.drawable.ic_launcher);
		// ivImageView.setErrorImageResId(R.drawable.ic_launcher);
		// ivImageView.setImageUrl(urlString, imageLoader);
	}

	@Override
	public void onBackPressed() {
		return;
	}
}
