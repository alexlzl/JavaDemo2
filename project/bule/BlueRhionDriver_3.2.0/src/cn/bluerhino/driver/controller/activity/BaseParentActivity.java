package cn.bluerhino.driver.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.utils.AppManager;
import cn.bluerhino.driver.view.LoadingDialog;

import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseParentActivity extends FragmentActivity {

	protected static Context mContext;
	protected Resources mRes;
	private LoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		mContext = getBaseContext();
		mRes = getResources();
		AppManager.getAppManager().addActivity(this);

	}

	protected void showLoadingDialog(int resId) {
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog(this, resId);

		}
		mLoadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialog != null) {
			mLoadingDialog.dismiss();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 保证友盟统计中onPageEnd在onPause之前调用,因为onPause中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// AppManager.getAppManager().finishActivity(this);
	}

	@Override
	public void onBackPressed() {
		// BrStartActivity.finishActivity(this);
		super.onBackPressed();
	}

	/**
	 * 检测用户是否登陆, 如果没有登陆则关闭并跳转到登陆页
	 * @return
	 */
	protected final boolean checkUserIsLogin() {
		boolean isLogin = ApplicationController.getInstance().getLoginStatus();
		if (!isLogin) {
			ToastUtil.showToast(this, R.string.toast_already_exit);
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
		return isLogin;

	}
}
