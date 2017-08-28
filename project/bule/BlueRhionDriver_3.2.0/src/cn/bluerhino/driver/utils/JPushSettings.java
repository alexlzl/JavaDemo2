package cn.bluerhino.driver.utils;

import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import cn.bluerhino.driver.ApplicationController;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JPushSettings {
    private static final String TAG = JPushSettings.class.getName();

    private static JPushSettings INSTANCE = new JPushSettings();

    private H mHandler;

    public static final synchronized JPushSettings getInstance() {
        return INSTANCE;
    }

    private JPushSettings() {
        mHandler = new H(this);
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public void setAlias(String alias) {
    	if(alias == null){
    		return;
    	}else if("".equals(alias)){
    		mHandler.sendMessage(mHandler.obtainMessage(H.MSG_SET_ALIAS, alias));    		
    	}else{
    		alias = alias.trim();
    		
    		Log.i(TAG, "alias = " + alias);
    		
    		if (!isValidTagAndAlias(alias)) {
    			return;
    		}
    		// 调用JPush API设置Tag
    		mHandler.sendMessage(mHandler.obtainMessage(H.MSG_SET_ALIAS, alias));
    	}
    }

    public void setTag(String tag) {
        tag = tag.trim();

        Log.i(TAG, "tag = " + tag);

        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            return;
        }

        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!isValidTagAndAlias(sTagItme)) {
                return;
            }
            tagSet.add(sTagItme);
        }
        // 调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(H.MSG_SET_TAGS, tagSet));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
            case 0:
                logs = "Set alias success";
                LogUtil.d(TAG, "Set alias success");
                break;
            case 6002:
                logs = "Failed to set alias due to timeout. Try again after 3s.";
                if (isConnected(ApplicationController.getInstance())) {
                    mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(H.MSG_SET_ALIAS, alias),
                            1000 * 3);
                } else {
                    Log.i(TAG, "No network");
                }
                LogUtil.d(TAG, logs);
                break;
            default:
                logs = "Failed with errorCode = " + code;
                LogUtil.d(TAG, logs);
            }
        };
    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
            case 0:
                logs = "Set tag success";
                Log.i(TAG, logs);
                break;
            case 6002:
                logs = "Failed to set tags due to timeout. Try again after 3s.";
                if (isConnected(ApplicationController.getInstance())) {
                    mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(H.MSG_SET_TAGS, tags),
                            1000 * 3);
                } else {
                    Log.i(TAG, "No network");
                }
                Log.i(TAG, logs);
                break;
            default:
                logs = "Failed with errorCode = " + code;
                Log.e(TAG, logs);
            }
        }
    };

    private static class H extends Handler {
        private static final int MSG_SET_ALIAS = 1001;
        private static final int MSG_SET_TAGS = 1002;
        private WeakReference<JPushSettings> mWeakOwner;

        public H(JPushSettings jPushSettings) {
            mWeakOwner = new WeakReference<JPushSettings>(jPushSettings);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            JPushSettings settings = mWeakOwner.get();
            if (null == settings) {
                return;
            }
            switch (msg.what) {
            case MSG_SET_TAGS:
                // 调用JPush API设置Tag
                JPushInterface.setAliasAndTags(
                        ApplicationController.getInstance(), null,
                        (Set<String>) msg.obj, settings.mTagsCallback);
                break;
            case MSG_SET_ALIAS:
                JPushInterface.setAliasAndTags(
                        ApplicationController.getInstance(), (String) msg.obj,
                        null, settings.mAliasCallback);
                break;
            default:
                super.handleMessage(msg);
                break;
            }
        }
    }

}
