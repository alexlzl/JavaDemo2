package cn.bluerhino.driver.utils;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.helper.ToastHelper;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;

public class BaiduNaviStatus {
	
	private static final BaiduNaviStatus sInstance = new BaiduNaviStatus();
	
	private Activity mActivity;
	
	private boolean mIsEngineInitSuccess = false;
	private boolean mKeyAuthResult = false;
	
	public static BaiduNaviStatus getInstance(){
		return sInstance;
	}
	
	public boolean hasInit(){
		return mIsEngineInitSuccess && mKeyAuthResult;
	}
	
	public void initEngine(Activity activity){
		mActivity = activity;
		this.initEngineSelf();
	}
	
	private void initEngineSelf(){
		if(!this.isConnected(mActivity)){
			if(ApplicationController.testSwitch){
				ToastHelper.showToast("初始化网路不好", mActivity);
			}
			mHandler.sendEmptyMessageDelayed(InnerHandler.MSG_NETWORK_ERR, 8000);
			return;			
		}else{
			if(ApplicationController.testSwitch){
				ToastHelper.showToast("初始化网路好", mActivity);
			}
			//初始化导航引擎
	        BaiduNaviManager.getInstance().initEngine(mActivity,this.getSdcardDir(),
	                mNaviEngineInitListener, 
	                //key验证结果
	                new LBSAuthManagerListener() {
	                    @Override
	                    public void onAuthResult(int status, String msg) {
	                        //key校验成功
	                    	if (0 == status) {
	                        	mKeyAuthResult = true;
	                        } 
	                    }
	        });
		}
	}
	
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}
	
	/**
	 * 引擎初始化(异步)
	 */
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		/**
		 * 引擎初始化成功
		 */
		public void engineInitSuccess() {
			mIsEngineInitSuccess = true;
		}
		/**
		 * 引擎初始化开始
		 */
		public void engineInitStart() {
		}
		/**
		 * 引擎初始化失败
		 */
		public void engineInitFail() {
			new UploadErrorMessage().uploadErrorMessage(ConstantsManager.NAVIGATION_ERROR, "初始化导航引擎失败");
		}
	};
	
    private boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    
    private InnerHandler mHandler = new InnerHandler(this);
    
    private static class InnerHandler extends Handler {
    	
    	 private static final int MSG_NETWORK_ERR = 1;
    	 
         private WeakReference<BaiduNaviStatus> mWeakOwner;

         private InnerHandler(BaiduNaviStatus reference) {
             mWeakOwner = new WeakReference<BaiduNaviStatus>(reference);
         }
         
         @Override
         public void handleMessage(Message msg) {
        	 switch (msg.what) {
        	 case InnerHandler.MSG_NETWORK_ERR:
				mWeakOwner.get().initEngineSelf();
				break;
        	 default:
				break;
        	 }
         }
    }
}
