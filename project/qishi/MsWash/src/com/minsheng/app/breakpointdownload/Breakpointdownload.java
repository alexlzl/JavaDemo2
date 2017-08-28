package com.minsheng.app.breakpointdownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.minsheng.wash.R;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

/**
 * 
 * 
 * @describe:断点下载实现
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-3下午3:11:21
 * 
 */
public class Breakpointdownload extends AsyncTask<Void, Integer, String> {

	private String mUrl;// 下载地址
	private Context mContext;
	private String mDownloadFileDir;
	private boolean mSupportBreakPoint = true;// 是否支持断点下载
	private NotificationManager mNotiManager;
	private Notification mNoti;
	private NotificationCompat.Builder mBuilder;
	private static final int notificationId = (int) System.currentTimeMillis();
	private static final String CACHE_FILE = "/SFBest/APK";

	/**
	 * 构造方法，传入下载地址参数
	 * 
	 * @param context
	 * @param url
	 */
	public Breakpointdownload(Context context, String url) {
		mUrl = url;
		mContext = context.getApplicationContext();
		mDownloadFileDir = getCacheFilePath();
		setNotification(context);

	}

	/**
	 * 
	 * 
	 * @describe:设置通知栏提示
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午3:12:05
	 * 
	 */
	private void setNotification(Context context) {
		// TODO Auto-generated method stub
		mNotiManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(mContext);
		mBuilder.setAutoCancel(false);
		mBuilder.setContentTitle(mContext
				.getString(R.string.upgrade_is_loading));
		mBuilder.setSmallIcon(R.drawable.msxq_icon);
		mBuilder.setProgress(100, 0, false);
		mBuilder.setWhen(System.currentTimeMillis());
		mBuilder.setTicker(mContext.getString(R.string.upgrade_is_loading));
		mBuilder.setContentIntent(PendingIntent.getBroadcast(mContext, 1,
				new Intent(), 0));
		mNoti = mBuilder.build();
		mNoti.flags = Notification.FLAG_SHOW_LIGHTS;

	}

	/**
	 * 
	 * 
	 * @describe:获取本地文件存储路径
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午3:12:17
	 * 
	 */
	private String getCacheFilePath() {
		String filePath = "";
		String rootpath = "";

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			rootpath = Environment.getExternalStorageDirectory().toString();
		}
		filePath = rootpath + CACHE_FILE;
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return filePath;
	}

	@Override
	protected void onPreExecute() {
		mNoti = mBuilder.build();
		mNoti.flags = Notification.FLAG_SHOW_LIGHTS;
		mNoti.defaults = Notification.DEFAULT_SOUND;
		mNoti.ledARGB = 0xff00ffff;
		mNoti.ledOffMS = 2000;
		mNoti.ledOnMS = 2000;
		mNotiManager.notify(notificationId, mNoti);
		super.onPreExecute();
	}

	/**
	 * 下载数据
	 */
	@SuppressWarnings("resource")
	@Override
	protected String doInBackground(Void... params) {
		String filePath = null;
		InputStream is = null;
		FileOutputStream fos = null;
		HttpURLConnection conn = null;
		RandomAccessFile raf = null;
		try {
			long total = DownloadUtil.getDownloadFileSize(mUrl);
			URL url = new URL(mUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(1000 * 10);
			conn.setRequestMethod("GET");
			String fileName = MD5.md5s(mUrl
					+ DownloadUtil.getPackageVersionName(mContext)
					+ DownloadUtil.getPackageVersionCode(mContext))
					+ ".apk";
			File upgradeDir = new File(mDownloadFileDir);
			File upgrade = new File(upgradeDir, fileName);
			/**
			 * 如果未下载完，继续之前的下载
			 */
			if (mSupportBreakPoint && upgrade.exists()
					&& upgrade.length() <= total) {
				/**
				 * 获取文件大小
				 */
				long fileLength = upgrade.length();
				/**
				 * 如果已经下载完成，返回路径
				 */
				if (fileLength == total) {
					filePath = upgrade.getAbsolutePath();
					return filePath;
				}
				conn.setRequestProperty("RANGE", "bytes=" + fileLength + "-");
				conn.connect();
				is = conn.getInputStream();
				int preciousProgress = 0;
				long size = fileLength;
				int count = 0;
				raf = new RandomAccessFile(upgrade, "rws");
				byte[] temp = new byte[1024 * 60];
				raf.seek(fileLength);
				count = is.read(temp, 0, temp.length);
				while (count > 0) {
					if (isCancelled()) {
						return null;
					}
					raf.write(temp, 0, count);
					size += count;
					int progress = (int) (size * 100 / total);
					if (progress - preciousProgress > 0) {
						publishProgress(progress);
						preciousProgress = progress;
					}
					count = is.read(temp, 0, temp.length);
				}
				raf.close();
				filePath = upgrade.getAbsolutePath();
			} else {
				conn.connect();
				is = conn.getInputStream();
				DownloadUtil.deleteContentsOfDir(upgradeDir);
				fos = new FileOutputStream(upgrade);
				long size = 0;
				int count = 0;
				byte[] temp = new byte[1024 * 60];
				count = is.read(temp, 0, temp.length);
				int preciousProgress = 0;
				while (count > 0) {
					if (isCancelled()) {
						return null;
					}
					fos.write(temp, 0, count);
					size += count;
					int progress = (int) (size * 100 / total);
					if (progress - preciousProgress > 0) {
						publishProgress(progress);
						preciousProgress = progress;
					}
					count = is.read(temp, 0, temp.length);
				}
				fos.flush();
				filePath = upgrade.getAbsolutePath();
			}
		} catch (MalformedURLException e) {
			cancel(true);
			return null;
		} catch (IOException e) {
			cancel(true);
			return null;
		} finally {
			DownloadUtil.closeInputStream(is);
			DownloadUtil.closeOutputStream(fos);
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
				}
			}
		}

		return filePath;
	}

	/**
	 * 下载中更新进度
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (values[0] < 100) {
			mBuilder.setContentText(mContext.getString(R.string.has_downed)
					+ " " + values[0] + "%");
			mNoti = mBuilder.setProgress(100, values[0], false).build();
			mNotiManager.notify(notificationId, mNoti);
		}
	}

	/**
	 * 下载完成安装程序
	 */
	@Override
	protected void onPostExecute(String result) {
		if (TextUtils.isEmpty(result))
			return;
		super.onPostExecute(result);

		Intent promptInstall = new Intent(Intent.ACTION_VIEW).setDataAndType(
				Uri.fromFile(new File(result)),
				"application/vnd.android.package-archive");
		promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		mBuilder.setTicker(mContext.getString(R.string.downed_complete));
		mBuilder.setContentTitle(mContext.getString(R.string.downed_complete));
		mBuilder.setContentIntent(PendingIntent.getActivity(mContext, 0,
				promptInstall, PendingIntent.FLAG_UPDATE_CURRENT));
		mBuilder.setContentText(mContext.getString(R.string.has_downed) + " "
				+ 100 + "%");
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mNoti = mBuilder.setProgress(100, 100, false).build();
		mNoti.flags = Notification.FLAG_SHOW_LIGHTS;
		mNoti.defaults = Notification.DEFAULT_SOUND;
		mNoti.ledARGB = 0xff00ffff;
		mNoti.ledOffMS = 2000;
		mNoti.ledOnMS = 2000;
		mNotiManager.notify(notificationId, mNoti);
		/**
		 * 执行下载成功后的安装
		 */
		mContext.startActivity(promptInstall);
		// HomePageFragmentNew.isForceUpdate = false;
	}

	/**
	 * 取消下载的操作
	 */
	@Override
	protected void onCancelled(String result) {
		mBuilder.setTicker(mContext.getString(R.string.download_fail));
		mBuilder.setContentTitle(mContext.getString(R.string.download_fail));
		mBuilder.setContentText(mContext.getString(R.string.please_try_later));
		mBuilder.setContentIntent(PendingIntent.getBroadcast(mContext, 0,
				new Intent(), PendingIntent.FLAG_NO_CREATE));
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		mNoti = mBuilder.build();
		mNoti.flags = Notification.FLAG_SHOW_LIGHTS
				| Notification.FLAG_AUTO_CANCEL;
		mNoti.defaults = Notification.DEFAULT_SOUND;
		mNoti.ledARGB = 0xff00ffff;
		mNoti.ledOffMS = 2000;
		mNoti.ledOnMS = 2000;
		mNotiManager.notify(notificationId, mNoti);
	}

}