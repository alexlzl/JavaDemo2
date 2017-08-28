package cn.bluerhino.driver.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.media.ThumbnailUtils;

public class ImageZoomUtil {
	
	public static Bitmap ZoomImageByPath(String pathName, int outputWidth, int outputHeight ){
		Bitmap bitmap = null;
		Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opts);

		opts.inJustDecodeBounds = false;
		float scale = getOptimalScale(opts.outWidth, opts.outHeight, outputWidth, outputHeight);
		
		if(scale <= 0){
			throw new NullPointerException("ImageZoomUtil: scale = " + scale + "<=0");
		}else if(scale > 1){
			opts.inSampleSize = (int)scale;
			bitmap = BitmapFactory.decodeFile(pathName, opts);
			return EnhancedExtractThumbnail(bitmap, outputWidth, outputHeight);
		}else{
			return BitmapFactory.decodeFile(pathName, opts);
		}
	}
	
	public static Bitmap ZoomImageByStream(InputStream is, int outputWidth, int outputHeight ) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024*2];
		int len = -1;
		while((len = is.read(buf))!=-1){
			baos.write(buf, 0, buf.length);
		}
		baos.flush();
		is.close();

		Bitmap bitmap = null;
		Rect outPaddingRect = new Rect(-1, -1, -1, -1);
		Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;      
        BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), outPaddingRect, opts);
		
		opts.inJustDecodeBounds = false;
		float scale = getOptimalScale(opts.outWidth, opts.outHeight, outputWidth, outputHeight);
		
		if(scale <= 0){
			throw new NullPointerException("ImageZoomUtil: scale = " + scale + "<=0");
		}else if(scale > 1){
			opts.inSampleSize = (int)scale;
			bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), outPaddingRect, opts);
			return EnhancedExtractThumbnail(bitmap, outputWidth, outputHeight);
		}else{
			return BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), outPaddingRect, opts);
		}

	}
	

	/**
	 * 获得缩放比例的浮点值
	 */
	private static float getOptimalScale(int sourceWidth, int sourceHeight, int templateWidth, int templateHeight) {
        float scale = 1;
		float scaleX = sourceWidth/ (float)templateWidth;
		float scaleY = sourceHeight / (float)templateHeight;
		if (scaleX > scaleY && scaleY > 0) {
			scale = scaleX;
		} 
		else if (scaleX < scaleY && scaleX > 0) {
			scale = scaleY;
		}
        return scale;
	}
	
	/**
	 * ThumbnailUtils工具类进行精确缩放
	 */
	private static Bitmap EnhancedExtractThumbnail(Bitmap bitmap, int outputWidth, int outputHeight){
		int curWidth = bitmap.getWidth();
		int curHeight = bitmap.getHeight();
		float scale = getOptimalScale(curWidth, curHeight, outputWidth, outputHeight);
		outputWidth = (int) (curWidth/scale);
		outputHeight = (int) (curHeight/scale);
		return ThumbnailUtils.extractThumbnail(bitmap, outputWidth, outputHeight);
		
	}
	
}
