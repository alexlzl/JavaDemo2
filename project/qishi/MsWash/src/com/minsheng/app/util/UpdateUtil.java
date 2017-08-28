package com.minsheng.app.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.breakpointdownload.Breakpointdownload;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.CheckUpdate;
import com.minsheng.app.entity.CheckUpdate.Infor.APPVersion;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.view.CommonDialog;
import com.minsheng.app.view.CommonDialog.OnDialogListener;
import com.minsheng.app.view.ConfirmDialog.OnConfirmListener;
import com.minsheng.wash.R;

/**
 * 
 * 
 * @describe:版本升级
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:39:07
 * 
 */
public class UpdateUtil {
	private static String TAG = "检测版本升级";
	private static Activity mActivity = null;
	// 是否显示过升级提示框(保证升级提示框在应用启动后只显示一次)
	public static boolean hasShowUpdateDialog = false;
	// 是否请求版本更新
	public static boolean isRequestUpdate = true;
	// 是否提示异常信息
	@SuppressWarnings("unused")
	private static boolean isToastError = false;
	private static CheckUpdate checkUpdateBean;

	public static void init(Activity activity, boolean toastError) {
		mActivity = activity;
		isToastError = toastError;
	}

	/**
	 * 清除更新状态
	 */
	public static void clearUpdateStatus() {
		hasShowUpdateDialog = false;
		isRequestUpdate = true;
		isToastError = false;
		versionHandler.removeCallbacksAndMessages(null);
	}

	/**
	 * 
	 * 
	 * @describe:检测版本升级
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-1-8下午4:51:45
	 * 
	 */
	public static void checkUpdate() {
		if (null == mActivity) {
			return;
		}

		if (NetWorkState.isNetWorkConnection(mActivity)) {
			if (!hasShowUpdateDialog && isRequestUpdate) {
				checkAppUpdate();
			}
		} else {
		}
	}

	/**
	 * 
	 * 
	 * @describe:获取升级信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-1-8下午5:00:44
	 * 
	 */
	private static void checkAppUpdate() {
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("deviceFlag", MsConfiguration.DEVICE_TYPE);
		paramsIn = MsApplication.getRequestParams(params, paramsIn,
				MsConfiguration.SysVersionParam);
		BasicHttpClient.getInstance().post(mActivity, paramsIn,
				MsRequestConfiguration.CHECK_APP_UPDATE,
				new BaseJsonHttpResponseHandler<CheckUpdate>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, CheckUpdate arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						versionHandler.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							CheckUpdate arg3) {
						// TODO Auto-generated method stub
					}

					@Override
					protected CheckUpdate parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							checkUpdateBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									CheckUpdate.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							versionHandler.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							versionHandler.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}

						return checkUpdateBean;
					}
				});

	}

	/**
	 * 处理版本升级消息返回
	 */
	public static Handler versionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (checkUpdateBean != null && checkUpdateBean.getCode() == 0) {
					/**
					 * 成功返回版本信息
					 */
					if (checkUpdateBean.getInfo() != null) {
						APPVersion appversion = checkUpdateBean.getInfo()
								.getNewversion();
						if (appversion != null) {
							// 最新版本号
							String versionName = appversion.getVersionName();
							String title = mActivity.getResources().getString(
									R.string.have_new_version)
									+ versionName + getChannel();
							// 下载地址
							String updateUrl = appversion.getDownUrl();
							// String updateUrl =
							// "http://down.mumayi.com/889543";

							// 最低版本号
							int minVersion = appversion.getLowVersion();
							// 当前版本号
							int currentVersion = getVersionCode();
							LogUtil.d(TAG, "升级地址==" + updateUrl);
							/**
							 * 判断是否是强制更新
							 */
							if (currentVersion < minVersion) {
								/**
								 * 需要强制升级
								 */
								// 不兼容现有版本,强制升级
								String content = appversion.getForceCueWords();
								showUpdateDialog(true, title, content,
										updateUrl);
								LogUtil.d(TAG, "强制升级" + "最小版本" + minVersion);
							} else {
								/**
								 * 非强制升级
								 */
								// 兼容现有版本,非强制升级
								String content = appversion.getCueWords();
								showUpdateDialog(false, title, content,
										updateUrl);
								LogUtil.d(TAG, "非强制升级" + "最小版本" + minVersion);
							}

						}

					}

				}

				break;
			case MsConfiguration.FAIL:
				break;
			}

		}
	};

	/**
	 * 
	 * 
	 * @describe: 显示版本升级对话框
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-1-9下午1:58:20
	 * 
	 */
	private static void showUpdateDialog(final boolean isForceUpdate,
			String title, String content, final String updateUrl) {
		if (null == mActivity) {
			return;
		}

		try {
			final CommonDialog commonDialog = CommonDialog.makeText(mActivity,
					title, content, new OnDialogListener() {

						@Override
						public void onResult(int result, CommonDialog v) {
							if (result == OnConfirmListener.LEFT) {
								// 升级
								if (isForceUpdate) {
									// 强制升级状态，窗口不消失
									startUpdate(updateUrl);
								} else {
									// 非强制升级，窗口消失
									startUpdate(updateUrl);
									v.cancel();
								}
							}

							if (result == OnConfirmListener.RIGHT) {
								if (isForceUpdate) {
									// 退出程序
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
									System.exit(1);
								} else {
									// 取消
									v.cancel();
								}
							}
						}
					});

			if (isForceUpdate) {
				commonDialog.setCancelable(false);
			} else {
				commonDialog.setCancelable(true);
			}

			// 左侧按钮
			commonDialog.setLeftText("升级");
			// 右侧按钮
			if (isForceUpdate) {
				commonDialog.setRightText("退出程序");
			} else {
				commonDialog.setRightText("取消");
			}
			commonDialog.showDialog();

			hasShowUpdateDialog = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @describe:开始升级
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-1-15上午11:55:35
	 * 
	 */
	private static void startUpdate(String url) {
		if (null == mActivity) {
			return;
		}
		new Breakpointdownload(mActivity, url).execute();
	}

	/**
	 * 
	 * 
	 * @describe:获取版本号
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-1-8下午5:31:42
	 * 
	 */
	public static int getVersionCode() {
		try {
			PackageInfo pi = mActivity.getPackageManager().getPackageInfo(
					mActivity.getPackageName(), 0);
			LogUtil.d(TAG, "获取APP版本号" + pi.versionName);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.d(TAG, "获取版本号异常");
			return 0;
		}

	}

	/**
	 * 
	 * 
	 * @describe:获取渠道名称
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-1-26下午6:00:48
	 * 
	 */
	public static String getChannel() {
		ApplicationInfo appInfo = null;
		try {
			appInfo = mActivity.getPackageManager().getApplicationInfo(
					mActivity.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String msg = appInfo.metaData.getString("UMENG_CHANNEL");
		return msg;
	}

}
