package cn.bluerhino.driver.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
	
	/**
	 * 旋转图片
	 * 
	 * @param angle
	 *            旋转角度
	 * @param bitmap
	 *            原图
	 * @return bitmap 旋转后的图片
	 */
	public static Bitmap rotateImage(int angle, Bitmap bitmap) {
		// 图片旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 得到旋转后的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

}
