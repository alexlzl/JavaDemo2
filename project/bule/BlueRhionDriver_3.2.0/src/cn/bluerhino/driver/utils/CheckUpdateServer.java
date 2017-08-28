package cn.bluerhino.driver.utils;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import cn.bluerhino.driver.controller.activity.UpdateActivity;
import cn.bluerhino.driver.model.Key;
import cn.bluerhino.driver.view.UmengUpdateDialog;
import cn.bluerhino.driver.view.UmengUpdateDialog.DataSource;
import cn.bluerhino.driver.view.UmengUpdateDialog.Delegate;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;

public class CheckUpdateServer {

	private static final String TAG = CheckUpdateServer.class.getSimpleName();
	public static final String ACTION_VERSION_CHECK = "cn.bluerhino.version_CHECK";
	private static final String KEY_FORCE_UPDATE_CODE = "force_update_code";
	private static final String KEY_MAX_VERSIONCODE = "max_version_code";
	private UpdateResponse mUpdateResponse;
	private boolean isIgonreCheckUpdate;
	private boolean isForceCheckUpdate;
	private UmengUpdateDialog mUpdateDialog = null;
	private Context mContext;

	public CheckUpdateServer(Context context) {
		super();
		this.mContext = context;
	}

	public void checkUpdate() {
		UmengUpdateAgent.setDefault();
		UpdateConfig.setDebug(false);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateCheckConfig(false);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.forceUpdate(mContext);

		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				if (updateStatus == 0 && updateInfo != null) {

					mUpdateResponse = updateInfo;
					LogUtil.d(TAG, "升级返回信息==" + mUpdateResponse.toString());
					if (updateInfo.hasUpdate) {
						forceUpdate();
					}
				}
			}
		});
	}

	private void forceUpdate() {
		/**
		 * 设置在线参数的监听器
		 */
		MobclickAgent
				.setOnlineConfigureListener(new UmengOnlineConfigureListener() {
					@Override
					public void onDataReceived(JSONObject data) {
						if (null != data) {
							try {
								Log.i(TAG, "force_update_code onDataReceived"
										+ data.toString());
								String intForceVersionCode = data
										.getString(KEY_FORCE_UPDATE_CODE);
								String maxVersionCode = data
										.getString(KEY_MAX_VERSIONCODE);
								forceUpdate(intForceVersionCode, maxVersionCode);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
		/**
		 * 请求在线参数
		 */
		MobclickAgent.updateOnlineConfig(mContext);
		/**
		 * 请求在线参数后，在线参数被缓存到本地，可以通过如下接口来获取在线参数值，key表示要获取的在线参数， value表示返回的在线参数值
		 */
		String minVersionCodeString = MobclickAgent.getConfigParams(mContext,
				"force_update_code");
		String maxVersionCodeString = MobclickAgent.getConfigParams(mContext,
				"max_version_code");
		if (minVersionCodeString != null && !minVersionCodeString.equals("")) {
			forceUpdate(minVersionCodeString, maxVersionCodeString);
		}
	}

	private void forceUpdate(String minVersionCode, String maxVersionCode) {
		Log.i(TAG, "forceUpdateCode" + minVersionCode);
		int intForceVersionCode = 0;
		int intMaxVersionCode = 0;
		try {
			intForceVersionCode = Integer.parseInt(minVersionCode);
			intMaxVersionCode = Integer.parseInt(maxVersionCode);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		forceUpdate(intForceVersionCode, intMaxVersionCode);
	}

	private void forceUpdate(int minVersionCode, int maxVersionCode) {
		int currentVersionCode = getCurrentAppVersionCode();
		if (currentVersionCode < minVersionCode) {
			isForceCheckUpdate = true;
		} else {
			isForceCheckUpdate = false;
		}
		Intent intent = new Intent(mContext, UpdateActivity.class);
		intent.putExtra(ConstantsManager.IS_FORCE_UPDATE_KEY,
				isForceCheckUpdate);
		intent.putExtra("response", mUpdateResponse);
		mContext.startActivity(intent);
		/**
		 * old=================
		 */
		// SharedPreferences sharedPreferences =
		// mContext.getApplicationContext()
		// .getSharedPreferences(Key.PREFERENCES_NAME,
		// Context.MODE_PRIVATE);
		//
		// isIgonreCheckUpdate = sharedPreferences.getBoolean(
		// Key.KEY_ISIGONRE_UPDATE, false);
		//
		// if (isForceCheckUpdate
		// || (!isIgonreCheckUpdate && !TextUtils
		// .isEmpty(mUpdateResponse.updateLog))) {
		// showUpdateDialog();
		// }
	}

	private int getCurrentAppVersionCode() {
		int intVersionCodeCur = 0;
		try {
			intVersionCodeCur = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return intVersionCodeCur;
	}

	private void showUpdateDialog() {
		ToastUtil.showToast(mContext, "test");
		if (mUpdateDialog == null) {
			mUpdateDialog = new UmengUpdateDialog(mContext, SELF, UPDATE_DATA);
		}

		if (mUpdateDialog.isShowing()) {
			mUpdateDialog.dismiss();
		}

		mUpdateDialog.show();
	}

	private final Delegate SELF = new Delegate() {

		@Override
		public void downloadApk() {
			UmengUpdateAgent.startDownload(mContext, mUpdateResponse);
		}

		@Override
		public void cancelUpdate() {
			MobclickAgent.onKillProcess(mContext);
			Process.killProcess(Process.myPid());
		}

		@Override
		public void ignoreCheckUpdate(boolean isIgonre) {
			SharedPreferences.Editor editor = mContext
					.getApplicationContext()
					.getSharedPreferences(Key.PREFERENCES_NAME,
							Context.MODE_PRIVATE).edit();
			editor.putBoolean(Key.KEY_ISIGONRE_UPDATE, isIgonre);
			editor.commit();
		}
	};

	private final DataSource UPDATE_DATA = new DataSource() {

		@Override
		public boolean isForceUpdate() {
			return isForceCheckUpdate;
		}

		@Override
		public boolean isIgonreUpdate() {
			return isIgonreCheckUpdate;
		}

		@Override
		public String getMessage() {
			return mUpdateResponse.updateLog;
		}
	};
}
