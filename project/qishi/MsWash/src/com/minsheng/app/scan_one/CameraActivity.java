package com.minsheng.app.scan_one;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.ScanCodeOrder;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.orderdetail.UniversalOrderDetail;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 
 * 
 * @describe:扫描二维码界面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-4-28下午1:52:14
 * 
 */
public class CameraActivity extends Activity {
	private static final String TAG = "CameraActivity";
	private static final float BEEP_VOLUME = 0.10f;
	private static final long VIBRATE_DURATION = 200L;
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private MediaPlayer mediaPlayer;
	private boolean playBeep = true;
	ImageScanner scanner;
	private static ScanCallbackOne scanCallback;
	private boolean previewing = true;
	private String fromWhere;
	private ScanCodeOrder scanCodeBean;
	private String orderType;

	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.scan_capture);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
		preview.addView(mPreview);

		mCamera.setPreviewCallback(previewCb);
		mCamera.startPreview();
		previewing = true;
		mCamera.autoFocus(autoFocusCB);

		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE,
				0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
		fromWhere = getIntent().getStringExtra(
				MsConfiguration.FROM_WHERE_TO_SCAN);
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();

				playBeepSoundAndVibrate();

				for (Symbol sym : syms) {
					/**
					 * 扫描结果返回
					 */
					// Intent intent = new Intent();
					// intent.putExtra("Code", sym.getData());
					// setResult(RESULT_OK, intent);
					if (MsConfiguration.FROM_CONFIRM_ORDER.equals(fromWhere)) {
						/**
						 * 从完善订单页面进入
						 */
						String newString = null;
						if (sym.getData() != null
								&& sym.getData().length() == 11) {
							LogUtil.d(TAG, "长度11" + sym.getData());
							newString = getFilterString(sym.getData());
							scanCallback.setScanResult(newString);
						} else {
							scanCallback.setScanResult(sym.getData());
							LogUtil.d(TAG, "长度不为11" + sym.getData());
						}
						finish();
					}

					if (MsConfiguration.FROM_QUERY_ORDER.equals(fromWhere)) {
						/**
						 * 从扫描查询页面进入，根据结果进行查询订单
						 */
						// MsToast.makeText(CameraActivity.this, sym.getData())
						// .show();

						getOrderId(sym.getData());
						// scanCallback.setScanResult(sym.getData());

					}
					if (MsConfiguration.FROM_MODIFY_ORDER_INFOR
							.equals(fromWhere)) {
						/**
						 * 从修改订单页面进入
						 */
						MsToast.makeText(CameraActivity.this, sym.getData())
								.show();
						scanCallback.setScanResult(sym.getData());
						finish();
					}

				}
			}
		}
	};

	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	private String getFilterString(String str) {
		String newString = null;
		if (StringUtil.isEmpty(str)) {
			return "";
		} else {
			if (str.length() == 11) {

				// String str1=null;
				newString = str.substring(1, str.length() - 2);
				newString = newString.substring(0, 3)
						+ newString.substring(4, newString.length());
				return newString;
			}
		}

		return newString;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(VIBRATE_DURATION);
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	public interface ScanCallbackOne {
		public void setScanResult(String result);
	}

	public static void setScanOneListener(ScanCallbackOne callback) {
		scanCallback = callback;
	}

	private void getOrderId(String oneCode) {
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("oneCode", oneCode);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(this, paramsIn,
				MsRequestConfiguration.SCAN_ORDER,
				new BaseJsonHttpResponseHandler<ScanCodeOrder>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ScanCodeOrder arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerScanOrder.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ScanCodeOrder arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
					}

					@Override
					protected ScanCodeOrder parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							scanCodeBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ScanCodeOrder.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerScanOrder.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerScanOrder.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return scanCodeBean;
					}
				});
	}

	Handler handlerScanOrder = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (scanCodeBean != null && scanCodeBean.getCode() == 0) {
					Intent intent = new Intent(CameraActivity.this,
							UniversalOrderDetail.class);
					int orderState = scanCodeBean.getInfo().getOrderList()
							.getWashStatus();
					int payStatus = scanCodeBean.getInfo().getOrderList()
							.getPayStatus();
					int isShowAfresh = scanCodeBean.getInfo().getOrderList()
							.getIsShowAfresh();
					switch (orderState) {
					case 1:
						/**
						 * 我在路上
						 */
						orderType = MsConfiguration.ON_THE_WAY;
						break;
					case 2:
						/**
						 * 完善订单
						 */
						orderType = MsConfiguration.COMPLETE_ORDER;
						break;

					case 3:
						/**
						 * 完善订单待用户确认
						 */
						orderType = MsConfiguration.FINISH_ORDER_WAIT_CONFIRM;
						break;

					case 4:
						/**
						 * 送店状态=====我已送店
						 */
						orderType = MsConfiguration.SEND_SHOP_ORDER;
						break;

					case 5:
						/**
						 * 取衣订单====我已取走
						 */
						orderType = MsConfiguration.GET_CLOTHES_BACK;
						break;

					case 7:
						/**
						 * 我在派送
						 */
						orderType = MsConfiguration.IS_SENDDING;

						break;

					case 8:
						/**
						 * 送货中
						 */

						if (payStatus == 0) {
							/**
							 * 未支付状态，显示返厂重新，收取现金
							 */
							orderType = MsConfiguration.NOT_PAY;
						}
						if (payStatus == 1) {
							/*
							 * 支付完成
							 */

						}

						break;

					case 9:
						/**
						 * 重洗
						 */
						if (payStatus == 0) {
							/**
							 * 未支付，对应确认收款
							 */
							orderType = MsConfiguration.WASH_AGAGIN_NOT_PAY;
						}
						if (payStatus == 1) {
							/**
							 * 已经支付，对应重洗完成
							 */
							orderType = MsConfiguration.WASH_AGAGIN_PAY_OVER;
						}
						break;
					case 10:
						/**
						 * 已经取消
						 */
						orderType = MsConfiguration.CANCEL_ORDER;
						break;
					case 11:
						/**
						 * 已经完成
						 */
						if (isShowAfresh == 1) {
							/**
							 * 可以返厂重洗
							 */
							orderType = MsConfiguration.ORDER_OVER_CAN_WASH_AGAIN;
						}
						if (isShowAfresh == 0) {
							/**
							 * 不可以返厂重洗
							 */
							orderType = MsConfiguration.ORDER_OVER_CANNOT_WASH_AGAIN;
						}

						break;
					default:
						break;
					}
					intent.putExtra(MsConfiguration.ORDER_TYPE, orderType);
					intent.putExtra(MsConfiguration.ORDER_ID_KEY, scanCodeBean
							.getInfo().getOrderList().getOrderId());
					MsStartActivity.startActivity(CameraActivity.this, intent);
					finish();
				} else {
					if (scanCodeBean != null) {
						MsToast.makeText(CameraActivity.this,
								scanCodeBean.getMsg()).show();
					} else {
						MsToast.makeText(CameraActivity.this, "获取数据失败").show();
					}
					finish();
				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(CameraActivity.this, "获取数据失败").show();
				finish();
				break;
			}
		}

	};
}
