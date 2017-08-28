package com.minsheng.app.scanning.activity;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.minsheng.app.scanning.camera.CameraManager;
import com.minsheng.app.scanning.decoding.CaptureActivityHandler;
import com.minsheng.app.scanning.decoding.InactivityTimer;
import com.minsheng.app.scanning.decoding.RGBLuminanceSource;
import com.minsheng.app.scanning.view.ViewfinderView;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.StringUtil;
import com.minsheng.wash.R;

public class CaptureActivity extends Activity implements Callback,
		View.OnClickListener {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private static final int REQUEST_CODE = 100;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;
	private ProgressDialog mProgress;
	private String photo_path;
	private Bitmap scanBitmap;
	private static ScanCallback scanCallback;
	private String TAG = "扫描页";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanning);
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		init();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			mProgress.dismiss();
			switch (msg.what) {
			case PARSE_BARCODE_SUC:
				onResultHandler((String) msg.obj, scanBitmap);
				break;
			case PARSE_BARCODE_FAIL:
				Toast.makeText(CaptureActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;

			}
		}

	};

	/**
	 * 回调处理相册中取图片，暂时不用
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE:
				Cursor cursor = getContentResolver().query(data.getData(),
						null, null, null, null);
				if (cursor.moveToFirst()) {
					photo_path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
				}
				cursor.close();

				mProgress = new ProgressDialog(CaptureActivity.this);
				mProgress.setMessage("正在扫描");
				mProgress.setCancelable(false);
				mProgress.show();

				new Thread(new Runnable() {
					@Override
					public void run() {
						Result result = scanningImage(photo_path);
						if (result != null) {
							Message m = mHandler.obtainMessage();
							m.what = PARSE_BARCODE_SUC;
							m.obj = result.getText();
							mHandler.sendMessage(m);
						} else {
							Message m = mHandler.obtainMessage();
							m.what = PARSE_BARCODE_FAIL;
							m.obj = "Scan failed!";
							mHandler.sendMessage(m);
						}
					}
				}).start();

				break;

			}
		}
	}

	/**
	 * 
	 * 
	 * @describe:扫描二维码图片
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-5下午3:49:48
	 * 
	 */
	public Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanning_sv);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 
	 * 
	 * @describe:处理扫描结果
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-5下午3:47:25
	 * 
	 */
	public void handleDecode(Result obj, Bitmap barcode) {

		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		LogUtil.d(TAG, "扫描结果" + obj.toString());
		if (!StringUtil.isEmpty(obj.toString())) {
			if (scanCallback != null) {
				scanCallback.setScanResult(obj.toString());
			}
		}
		this.finish();
	}

	public boolean isUrl(String urlString) {
		String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

		Pattern patt = Pattern.compile(regex);

		Matcher matcher = patt.matcher(urlString);

		boolean isMatch = matcher.matches();

		if (!isMatch) {

			return false;

		} else {
			return true;

		}
	}

	@SuppressWarnings("unused")
	private boolean isProductDetail(String url) {
		String regex = "(http://www.[d|t|qa|sfbest]+.com/html/products/)([0-9]+)(/)([0-9]+)(.html)";
		Pattern patt = Pattern.compile(regex);
		Matcher matcher = patt.matcher(url);
		boolean isMatch = matcher.find();
		if (!isMatch) {
			return false;
		} else {
			return true;
		}
	}

	private void onResultHandler(String resultString, Bitmap bitmap) {
		if (TextUtils.isEmpty(resultString)) {
			Toast.makeText(CaptureActivity.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
			return;
		}

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

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

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	private void init() {
		viewfinderView = (ViewfinderView) findViewById(R.id.scanning_show);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

	}

	public interface ScanCallback {
		public void setScanResult(String result);
	}

	public static void setScanListener(ScanCallback callback) {
		scanCallback = callback;
	}
}