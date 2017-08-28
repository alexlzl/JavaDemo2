package com.minsheng.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

/**
 * 
 * 
 * @describe:加载网络图片
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:27:49
 * 
 */
public class NetworkUtil {

	/**
	 * 加载网络图片url
	 * 
	 * @param imageUrl
	 * @return drawable
	 */
	public static Drawable loadImageFromNetwork(String imageUrl) {
		Drawable drawable = null;
		try {
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(
					new URL(imageUrl).openStream(), "image.jpg");
		} catch (IOException e) {
			LogUtil.d("NetwordUtil e = " + e.getMessage());
		}
		return drawable;
	}

	/**
	 * 加载网络图片url
	 * 
	 * @param imageUrl
	 * @return bitmap
	 */
	public static Bitmap loadImageUrlFromNetwork(String imageUrl) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(imageUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 加载网络图片url
	 * 
	 * @param url
	 * @return bitmap
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			conn.setConnectTimeout(2000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			if (conn.getResponseCode() != 200) {
				/* Can't get data. Then use default img. */
				return null;
			}

			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 加载网络图片url
	 * 
	 * @param url
	 * @return bitmap
	 */
	public static Bitmap downloadBitmap(String url) {

		final HttpClient httpClient = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse httpResponse = httpClient.execute(getRequest);
			final int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				LogUtil.d("NetworkUtil downloadBitmap Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = null;
				try {
					inputStream = httpEntity.getContent();
					return BitmapFactory.decodeStream(inputStream);
				} catch (Exception e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					System.gc();
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					httpEntity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			LogUtil.d("NetworkUtil downloadBitmap I/O error while retrieving bitmap from "
					+ url);
		} catch (IllegalStateException e) {
			getRequest.abort();
			LogUtil.d("NetworkUtil downloadBitmap Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			LogUtil.d("NetworkUtil downloadBitmap Error while retrieving bitmap from "
					+ url);
		}
		return null;
	}

	/**
	 * 跳转到系统设置界面
	 */
	public static void startToSettings(Context context) {
		if (null == context) {
			return;
		}

		try {
			if (android.os.Build.VERSION.SDK_INT > 10) {
				// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到WiFi界面
				context.startActivity(new Intent(
						android.provider.Settings.ACTION_SETTINGS));
			} else {
				context.startActivity(new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
