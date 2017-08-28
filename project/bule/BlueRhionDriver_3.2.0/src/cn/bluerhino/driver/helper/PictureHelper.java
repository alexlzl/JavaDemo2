package cn.bluerhino.driver.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.utils.BitmapUtil;
import cn.bluerhino.driver.utils.ImageZoomUtil;
import cn.bluerhino.driver.utils.PictureUtil;

public class PictureHelper {
	/**
	 * 从文件路径中获得处理好的图片
	 */
	public static Bitmap getBitmap(String fileSrc, int outputWidth, int outputHeight) {
		Bitmap mImage = ImageZoomUtil.ZoomImageByPath(fileSrc, outputWidth, outputHeight);
		// 部分手机会对图片做旋转，这里检测旋转角度
		int degree = PictureUtil.readPictureDegree(fileSrc);
		if (degree != 0) {
			// 把图片旋转为正的方向
			mImage = BitmapUtil.rotateImage(degree, mImage);
		}
		return mImage;
	}

	/**
	 * 将bitmap转换成文件存储在手机cache文件夹下
	 * 
	 * @param bitmap
	 * @param fileName
	 * @return
	 */
	public static File convertBitMapToFile(Bitmap bitmap, String fileName) {
		boolean b = false;
		try {
			File myFile = new File(ApplicationController.getInstance().getApplicationContext().getExternalCacheDir(),
					fileName);
			if (!myFile.exists()) {
				myFile.createNewFile();
			}
			FileOutputStream optput = new FileOutputStream(myFile);
			BufferedOutputStream stream = new BufferedOutputStream(optput);
			b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, optput);
			stream.flush();
			stream.close();
			if (b) {
				return myFile;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据uri获取文件路径
	 * 
	 * @param mContext
	 * @param uri
	 * @return
	 */
	public static String getAbsloutePath(Context mContext, Uri uri) {
		final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		if (mIsKitKat) {
			return getPath(mContext, uri);
		} else {
			return getAbsPath_BelowKAK(mContext, uri);
		}
	}

	/**
	 * android系统4.4版本以下获取文件路径(根据uri)
	 * 
	 * @param mContext
	 * @param uri
	 * @return
	 */
	private static String getAbsPath_BelowKAK(Context mContext, Uri uri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * 4.4及以上获取图片的方法(根据uri)
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	private static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

}
