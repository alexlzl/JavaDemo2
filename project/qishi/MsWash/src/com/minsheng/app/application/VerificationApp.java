package com.minsheng.app.application;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.AppInfor;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.util.LogUtil;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * @describe:获取APP验证信息
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-1-4下午2:39:03
 * 
 */
public class VerificationApp {
	private static String TAG = "APK信息";
	private static Context mcontext;
	private static String packageName, md5Code;
	private static AppInfor resultBean;

	public static String getPackageName() {
		PackageInfo info;
		String packageNames = null;
		try {
			info = mcontext.getPackageManager().getPackageInfo(
					mcontext.getPackageName(), 0);
			// 当前应用的版本名称
			// String versionName = info.versionName;
			// 当前版本的版本号
			// int versionCode = info.versionCode;
			// 当前版本的包名
			packageNames = info.packageName;
			packageName = packageNames;
			LogUtil.d(TAG, "包名==" + packageNames);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageNames;
	}

	public static void getSign(Context context) {
		mcontext = context;
		Signature[] arrayOfSignature = getRawSignature(context,
				getPackageName());
		if ((arrayOfSignature == null) || (arrayOfSignature.length == 0)) {
			errout("signs is null");
			return;
		}
		int i = arrayOfSignature.length;
		for (int j = 0; j < i; j++)
			stdout(VerificationMD5.getMessageDigest(arrayOfSignature[j]
					.toByteArray()));
	}

	private static Signature[] getRawSignature(Context paramContext,
			String paramString) {
		if ((paramString == null) || (paramString.length() == 0)) {
			errout("getSignature, packageName is null");
			return null;
		}
		PackageManager localPackageManager = paramContext.getPackageManager();
		PackageInfo localPackageInfo;
		try {
			localPackageInfo = localPackageManager.getPackageInfo(paramString,
					64);
			if (localPackageInfo == null) {
				errout("info is null, packageName = " + paramString);
				return null;
			}
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			errout("NameNotFoundException");
			return null;
		}
		return localPackageInfo.signatures;
	}

	private static void errout(String paramString) {
		// tv_error.append(paramString + "\n");
	}

	private static void stdout(String paramString) {
		// tv_result.append(paramString + "\n");
		LogUtil.d(TAG, "签名信息" + paramString);
		md5Code = paramString;
		getAppInfor();
	}

	/**
	 * 
	 * 
	 * @describe:获取APP验证信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-1-5下午2:04:14
	 * 
	 */
	private static void getAppInfor() {
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("authToken", md5Code);
		params.put("authPack", packageName);
		paramsIn = MsApplication.getRequestParams(params, paramsIn,
				MsConfiguration.CustomerUserParam);
		BasicHttpClient.getInstance().post(mcontext, paramsIn,
				MsRequestConfiguration.VERIFICATION_APP,
				new BaseJsonHttpResponseHandler<AppInfor>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, AppInfor arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handleMessage.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							AppInfor arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
					}

					@Override
					protected AppInfor parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							resultBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									AppInfor.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handleMessage.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handleMessage.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						// MsApplication.getDataBean(loginBean, handlerLogin,
						// arg0, LoginBean.class);
						return resultBean;
					}
				});
	}

	static Handler handleMessage = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (resultBean != null && resultBean.getCode() == 0) {
					/**
					 * 通过验证
					 */
				} else {
					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(1);
				}
				break;
			case MsConfiguration.FAIL:
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
				break;
			}
		}

	};

}
