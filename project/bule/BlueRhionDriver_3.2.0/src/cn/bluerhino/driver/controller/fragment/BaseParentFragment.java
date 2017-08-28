package cn.bluerhino.driver.controller.fragment;

import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.LoginActivity;
import com.android.volley.RequestQueue;
import com.bluerhino.library.utils.ToastUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

public class BaseParentFragment extends Fragment {

	protected Activity mActivity;
	protected Resources mRes;
	protected RequestQueue mRequestQueue;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
		this.mRes = activity.getResources();
		mRequestQueue = ApplicationController.getInstance().getRequestQueue();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkDriverSession();
	}

	private void checkDriverSession() {
		if (this.isSessionAvailable()) {
			ToastUtil.showToast(mActivity, R.string.myinfo_login_again);
			mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			mActivity.finish();
		}
	}

	/**
	 * 从Application中取得的Session是否可用
	 */
	protected final boolean isSessionAvailable() {
		return TextUtils.isEmpty(ApplicationController.getInstance()
				.getLoginfo().getSessionID());
	}
}
