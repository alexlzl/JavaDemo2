package cn.bluerhino.driver.view;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.network.LoadingURLRequest;
import cn.bluerhino.driver.utils.LogUtil;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

/**
 * Describe:
 * 
 * Date:2015-7-21
 * 
 * Author:liuzhouliang
 */
public class ShowAdvertisementDialog extends Dialog {
	private static final String TAG = ShowAdvertisementDialog.class.getName();
	private static ShowAdvertisementDialog mDialog;
	private static ImageView ivImageView;
	private static Context mContext;
	private final int SPLASH_DISPLAY_LENGH = 3000;

	private ShowAdvertisementDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public static ShowAdvertisementDialog makeDialog(Context context, int theme) {
		mContext = context;
		mDialog = new ShowAdvertisementDialog(context, theme);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.setContentView(R.layout.activity_advertisement);
		ivImageView = (ImageView) mDialog
				.findViewById(R.id.activity_advertisement_iv);
		return mDialog;

	}

	public void showDialog() {
		Window window = mDialog.getWindow();
		window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		window.setWindowAnimations(R.style.WindowAnimation);
		WindowManager.LayoutParams wm = window.getAttributes();
		wm.gravity = Gravity.CENTER;
		wm.width = LayoutParams.MATCH_PARENT;
		wm.height = LayoutParams.MATCH_PARENT;
		mDialog.show();
		getBitmapRequest();
	}

	private void getURLRequest() {
		BRRequestParams params = new BRRequestParams();
		params.setDeviceId(ApplicationController.getInstance().getDeviceId());
		params.setVerCode(ApplicationController.getInstance().getVerCode());

		BRJsonResponse succeedListener = new BRJsonResponse() {

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
								break;
							case 1:
								/**
								 * 有启动图片
								 */

								break;

							default:
								break;
							}
						}
						if (response.has("picture")) {
							String url = response.getString("picture");
							LogUtil.d(TAG, "url==" + url);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
					}
				}).setParams(params).build();
		ApplicationController.getInstance().getRequestQueue().add(loginRequest);
	}

	private void getBitmapRequest() {

		ImageRequest imageRequest = new ImageRequest(
				"http://pic1.nipic.com/2009-02-20/2009220135032130_2.jpg",
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						LogUtil.d(TAG, "onResponse");
						ivImageView.setImageBitmap(response);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								mDialog.dismiss();
							}
						}, SPLASH_DISPLAY_LENGH);
					}
				}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						LogUtil.d(TAG, "onErrorResponse");
						mDialog.dismiss();
					}
				});
		ApplicationController.getInstance().getRequestQueue().add(imageRequest);

	}

}
