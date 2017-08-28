package cn.bluerhino.driver.controller.activity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.tts.BNTTSPlayer;
import com.baidu.navisdk.comapi.tts.BNavigatorTTSPlayer;
import com.baidu.navisdk.comapi.tts.IBNTTSPlayerListener;
import com.baidu.navisdk.model.datastruct.LocData;
import com.baidu.navisdk.model.datastruct.SensorData;
import com.baidu.navisdk.ui.routeguide.BNavigator;
import com.baidu.navisdk.ui.routeguide.IBNavigatorListener;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.baidu.navisdk.ui.widget.RoutePlanObserver.IJumpToDownloadListener;
import com.baidu.nplatform.comapi.map.MapGLSurfaceView;
import com.umeng.analytics.MobclickAgent;

public class BNavigatorActivity extends BaseParentActivity{

	private static final String TAG = BNavigatorActivity.class.getSimpleName(); 
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		createAndLoadView();
	    initBavigator();

		BNRoutePlaner.getInstance().setObserver(
		        new RoutePlanObserver(this, new IJumpToDownloadListener() {
			        @Override
			        public void onJumpToDownloadOfflineData() {
			        }
		        }));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	        BNavigator.getInstance().onBackPressed();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	private void createAndLoadView(){
		if (Build.VERSION.SDK_INT < 14) {
            BaiduNaviManager.getInstance().destroyNMapView();
        }
		MapGLSurfaceView nMapView = null;
		
		try{
			nMapView = BaiduNaviManager.getInstance().createNMapView(this);
		}catch(UnsatisfiedLinkError e){
			e.printStackTrace();
			BNavigatorActivity.this.finish();
			return;
		}
		
		if(nMapView != null){
			//创建导航视图
			View navigatorView = BNavigator.getInstance().init(BNavigatorActivity.this, getIntent().getExtras(), nMapView);
			
			//填充视图
			setContentView(navigatorView);
		}
	}
	
    private void initBavigator(){
    	//设置导航监听器
		BNavigator.getInstance().setListener(mBNavigatorListener);
		BNavigator.getInstance().startNav();
		// 初始化TTS. 开发者也可以使用独立TTS模块，不用使用导航SDK提供的TTS
		startTTS();
    }
	
	@Override
    public void onResume() {
		MobclickAgent.onPageStart(TAG);
        BNavigator.getInstance().resume();
        BNMapController.getInstance().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
    	MobclickAgent.onPageEnd(TAG);
        BNavigator.getInstance().pause();
        BNMapController.getInstance().onPause();
        super.onPause();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	BNavigator.getInstance().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onDestroy(){
    	stopTTS();
    	BNavigator.getInstance().quitNav();
    	BNavigator.destory();
    	BNRoutePlaner.getInstance().setObserver(null);
    	super.onDestroy();
    }
    
    private void startTTS() {
	    BNTTSPlayer.initPlayer();
		
		//设置TTS播放回调
		BNavigatorTTSPlayer.setTTSPlayerListener(new IBNTTSPlayerListener() {
            
            @Override
            public int playTTSText(String arg0, int arg1) {
            	//开发者可以使用其他TTS的API
				return BNTTSPlayer.playTTSText(arg0, arg1);
            }
            
            @Override
            public void phoneHangUp() {
                //手机挂断
            }
            @Override
            public void phoneCalling() {
                //通话中
            }
            
            @Override
            public int getTTSState() {
            	//开发者可以使用其他TTS的API,
                return BNTTSPlayer.getTTSState();
            }
        });
		BNavigatorTTSPlayer.setPhoneIn(true);
    }
    
    private void stopTTS(){
    	BNavigatorTTSPlayer.setTTSPlayerListener(new IBNTTSPlayerListener() {
			
			@Override
			public int playTTSText(String arg0, int arg1) {
				return 0;
			}
			
			@Override
			public void phoneHangUp() {
				
			}
			
			@Override
			public void phoneCalling() {
				
			}
			
			@Override
			public int getTTSState() {
				return 0;
			}
		});
    }
    
    /**
     * 导航监听器
     */
    private IBNavigatorListener mBNavigatorListener = new IBNavigatorListener() {
        
        @Override
        public void onYawingRequestSuccess() {
            // TODO 偏航请求成功
        }
        @Override
        public void onYawingRequestStart() {
            // TODO 开始偏航请求
        }
        
        @Override
        public void onPageJump(int jumpTiming, Object arg) {
        	if(IBNavigatorListener.PAGE_JUMP_WHEN_GUIDE_END == jumpTiming){
        	    finish();
        	}else if(IBNavigatorListener.PAGE_JUMP_WHEN_ROUTE_PLAN_FAIL == jumpTiming){
        		finish();
        	}
        }

		@Override
		public void notifyGPSStatusData(int arg0) {
		}
		@Override
		public void notifyLoacteData(LocData arg0) {
		}
		@Override
		public void notifyNmeaData(String arg0) {
		}
		@Override
		public void notifySensorData(SensorData arg0) {
		}
		@Override
		public void notifyStartNav() {
			BaiduNaviManager.getInstance().dismissWaitProgressDialog();
		}
		@Override
		public void notifyViewModeChanged(int arg0) {
		}
    };
}
