package cn.bluerhino.driver.controller.activity;

import java.util.List;

import com.bluerhino.library.utils.ToastUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.helper.url.URIFormater;

/**
 * Describe:页面跳转处理页
 * 
 * Date:2015-8-26
 * 
 * Author:likai
 */
public class PageRedirectActivity extends BaseParentActivity {
	
	private final int SPLASH_DISPLAY_LENGTH = 0;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				parsePath(getIntent().getData());
			}
		}, SPLASH_DISPLAY_LENGTH);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		return;
	}
	
	private void parsePath(Uri uri){
		boolean isParse = false;
		if(uri != null){
			//将字符串重新转发给对应Activity
			uri = URIFormater.uriFormatter(uri);
			if(uri != null){
				isParse = true;
			}
		}
		//解析成功
		if(isParse){
			jumpToActivity(uri);
		}
		finish();
	}
	
	private void jumpToActivity(Uri uri){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		intent.setData(uri);
		if(isIntentCanResponse(intent)){
			startActivity(intent);
		}else{
			ToastUtil.showToast(getApplicationContext(), R.string.pageredirect_load_err);
		}
	}
	
	/**
	 * 检测intent能否被处理
	 */
	private boolean isIntentCanResponse(Intent intent){
       	PackageManager packageManager = getPackageManager();
       	List<ResolveInfo> query = packageManager.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
       	return query.size() > 0;
	}
}
