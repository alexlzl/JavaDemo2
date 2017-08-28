package cn.bluerhino.driver.helper.url;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;

/**
 * 处理各自页面跳转类
 * @author likai
 *
 */
public abstract class URLProcessor {
	
	private boolean mIsInit = false;
	private String[] mArrURL = null;
	
	public URLProcessor(Context context, int resId){
		init(context, resId);
	}
	
	private boolean init(Context context, int resId){
		if(mIsInit)
			return true;
		if(context == null){
			return false;
		}
		Resources res = context.getApplicationContext().getResources();
		try{
			mArrURL = res.getStringArray(resId);
			mIsInit = true;
		}catch(NotFoundException e){
			e.printStackTrace();
			mIsInit = false;
		}
		return mIsInit;
	}
	
	/**
	 * 从data中获得内部url, 得到列表中对应的index
	 */
	public int getJumpUrlIndex(Uri uri){
		if(!mIsInit)
			throw new RuntimeException("使用前需要先进行初始化");
		String path = uri.getPath();
		int index;
		int size = mArrURL.length;
		for(index =0; index < size; index++){
			if(mArrURL[index].equals(path)){
				break;
			}
		}
		if(index >= size){
			throw new NullPointerException("进入找不到对应url");
		}
		return index;
	}

}
