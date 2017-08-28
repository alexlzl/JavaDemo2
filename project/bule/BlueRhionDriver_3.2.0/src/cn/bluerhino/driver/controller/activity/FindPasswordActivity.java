package cn.bluerhino.driver.controller.activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.network.CheckUserIsNotExistRequest;
import cn.bluerhino.driver.network.DynamicCodeRequest;
import cn.bluerhino.driver.network.ResetPasswordRequest;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 忘记密码
 * 
 * @author Administrator
 * 
 */
public class FindPasswordActivity extends BaseParentActivity implements
		OnClickListener {

	protected static final String TAG = FindPasswordActivity.class
			.getSimpleName();
	private static final String TOKEN = "token";
	private ErrorListener mErrorListener;
	private TextView tvRecomond;

	@InjectView(R.id.left_img)
	ImageView mbackbtn;
	@InjectView(R.id.center_title)
	TextView mTitle;
	@InjectView(R.id.user_phone)
	EditText mUserPhone;
	@InjectView(R.id.security_code)
	EditText mSecurityCode;
	@InjectView(R.id.get_security_code_button)
	TextView mSecurityCodeBtn;
	@InjectView(R.id.password)
	EditText mUserPassword;
	@InjectView(R.id.register)
	Button mResetBtn;
	@InjectView(R.id.delete_phone)
	ImageView mDeletePhoneView;
	@InjectView(R.id.delete_password)
	ImageView mDeletePasswordView;

	private int mCount = 60;
	private Handler mHandler;
	private Runnable mCountDownRunnable;
	private String mToken = "";

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		init();
		loadView();
		setViewListener();
		initAsyncCodeTask();
	}

	/**
	 * 
	 * Describe:初始化
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 */
	private void init() {
		setContentView(R.layout.activity_register_or_reset_both);
		ButterKnife.inject(this);
		mErrorListener = new VolleyErrorListener(mContext) {
			@Override
			public void onErrorResponse(VolleyError error) {
				dismissLoadingDialog();
				super.onErrorResponse(error);
			}
		};

	}

	/**
	 * 
	 * Describe:获取控件
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 */
	private void loadView() {
		tvRecomond = (TextView) findViewById(R.id.register_recommend_tv);
		tvRecomond.setVisibility(View.GONE);
		mbackbtn.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.register_forget_pwd);
		mResetBtn.setText(R.string.reset_and_login);
		if (TextUtils.isEmpty(mUserPhone.getText().toString())) {
			mDeletePhoneView.setVisibility(View.GONE);
		} else {
			mDeletePhoneView.setVisibility(View.VISIBLE);
		}

		if (TextUtils.isEmpty(mUserPassword.getText().toString())) {
			mDeletePasswordView.setVisibility(View.GONE);
		} else {
			mDeletePasswordView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 
	 * Describe:绑定控件监听事件
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 */
	private void setViewListener() {
		mbackbtn.setOnClickListener(this);
		mSecurityCodeBtn.setOnClickListener(this);
		mUserPhone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(mUserPhone.getText().toString())) {
					mDeletePhoneView.setVisibility(View.GONE);
				} else {
					mDeletePhoneView.setVisibility(View.VISIBLE);
				}
			}
		});

		mUserPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(mUserPassword.getText().toString())) {
					mDeletePasswordView.setVisibility(View.GONE);
				} else {
					mDeletePasswordView.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	/**
	 * 
	 * Describe:找回密码请求
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 */
	@OnClick(R.id.register)
	void findPassword() {
		if (TextUtils.isEmpty(mUserPhone.getText())) {
			ToastUtil.showToast(mContext, R.string.register_enter_phone);
			return;
		}
		if (mUserPhone.getText().length() < 11) {
			ToastUtil.showToast(mContext, R.string.login_short_number);
			return;
		}
		if (TextUtils.isEmpty(mSecurityCode.getText())) {
			ToastUtil.showToast(mContext, R.string.register_enter_code);
			return;
		}
		if (mSecurityCode.getText().length() < 4) {
			ToastUtil.showToast(mContext, R.string.register_code_less);
			return;
		}
		// 测试账号不用输密码
		if (TextUtils.isEmpty(mUserPassword.getText())) {
			ToastUtil.showToast(mContext, R.string.register_enter_pwd);
			return;
		}

		showLoadingDialog(R.string.findpwd_loading);

		BRRequestParams mParams = new BRRequestParams(mToken);
		mParams.setDeviceId(ApplicationController.getInstance().getDeviceId());
		mParams.setVerCode(ApplicationController.getInstance().getVerCode());
		String phone = mUserPhone.getText().toString().trim();
		String msgCode = mSecurityCode.getText().toString().trim();
		String password = mUserPassword.getText().toString().trim();
		mParams.put("phone", phone);
		mParams.put("password", password);
		mParams.put("DynamicCode", msgCode);
		BRJsonRequest.BRJsonResponse succeedResponse = new BRJsonRequest.BRJsonResponse() {
			@Override
			public void onResponse(JSONObject response) {
				dismissLoadingDialog();
				try {
					mToken = response.getString(TOKEN);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (!TextUtils.isEmpty(mToken)) {
					ToastUtil.showToast(mContext, R.string.findpwd_get_pwd);
					FindPasswordActivity.this.finish();
				}
			}
		};
		BRFastRequest request = new ResetPasswordRequest.Builder()
				.setSucceedListener(succeedResponse)
				.setErrorListener(mErrorListener).setParams(mParams).build();

		ApplicationController.getInstance().getRequestQueue().add(request);

	}

	@OnClick(R.id.delete_phone)
	void clearPhone() {
		mUserPhone.setText("");
	}

	@OnClick(R.id.delete_password)
	void clearPassword() {
		mUserPassword.setText("");
	}

	@Override
	protected void onStart() {
		mHandler = new Handler();
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_img:
			onBackPressed();
			break;
		case R.id.get_security_code_button:
			checkUserIsNotExist();
			break;
		}
	}

	/**
	 * 
	 * Describe:检测用户是否存在
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 */
	private void checkUserIsNotExist() {
		if (TextUtils.isEmpty(mUserPhone.getText().toString())) {
			ToastUtil.showToast(mContext, R.string.register_enter_phone);
			return;
		}
		if (mUserPhone.getText().length() < 11) {
			ToastUtil.showToast(mContext, R.string.login_short_number);
			return;
		}

		mSecurityCodeBtn.setEnabled(false);
		mSecurityCodeBtn.setTextColor(Color.parseColor("#ccc5c5"));
		mSecurityCodeBtn.setText(R.string.register_process);
		String phone = mUserPhone.getText().toString().trim();

		BRJsonRequest.BRJsonResponse succeedResponse = new BRJsonRequest.BRJsonResponse() {
			@Override
			public void onResponse(JSONObject response) {
				if (response.has("ret")) {
					try {
						int ret = response.getInt("ret");
						if (ret == 1) {
							ToastUtil.showToast(mContext,
									R.string.findpwd_nonexistent_uesr);
							mSecurityCodeBtn.setEnabled(true);
							mSecurityCodeBtn
									.setText(R.string.register_get_smdcode);
							mSecurityCodeBtn.setTextColor(Color
									.parseColor("#39A1E8"));
						} else {
							mCount = 60;
							mHandler.post(mCountDownRunnable);
							getMessageSecurityCode();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		};

		ErrorListener errorListener = new VolleyErrorListener(mContext) {
			public void onErrorResponse(com.android.volley.VolleyError error) {
				super.onErrorResponse(error);
				mSecurityCodeBtn.setEnabled(true);
				mSecurityCodeBtn.setText(R.string.register_get_smdcode);
				mSecurityCodeBtn.setTextColor(Color.parseColor("#39A1E8"));
			};
		};

		BRRequestParams params = new BRRequestParams();
		params.put("Telephone", phone);
		BRFastRequest request = new CheckUserIsNotExistRequest.Builder()
				.setSucceedListener(succeedResponse)
				.setErrorListener(errorListener).setParams(params)
				.setRequestTag(TAG).build();

		ApplicationController.getInstance().getRequestQueue().add(request);

	}

	/**
	 * 
	 * Describe:倒计时任务
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 */
	private void initAsyncCodeTask() {
		mCountDownRunnable = new Runnable() {
			@Override
			public void run() {
				mCount--;
				if (mCount > 0) {
					mSecurityCodeBtn.setEnabled(false);
					mSecurityCodeBtn.setTextColor(Color.parseColor("#ccc5c5"));
					mSecurityCodeBtn.setText(mCount + "s");
					mHandler.postDelayed(this, 1000);
				} else {
					mSecurityCodeBtn.setEnabled(true);
					mSecurityCodeBtn.setTextColor(Color.parseColor("#39A1E8"));
					mSecurityCodeBtn.setText(R.string.register_get_smdcode);
					mHandler.removeCallbacks(this);
				}
			}
		};
	}

	@Override
	protected void onDestroy() {
		mHandler.removeCallbacks(mCountDownRunnable);
		super.onDestroy();
	}

	/**
	 * 请求验证码
	 */
	private void getMessageSecurityCode() {
		if (TextUtils.isEmpty(mUserPhone.getText().toString())) {
			ToastUtil.showToast(mContext, R.string.register_enter_phone);
			return;
		}

		BRJsonRequest.BRJsonResponse succeedResponse = new BRJsonRequest.BRJsonResponse() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					mToken = response.getString(TOKEN);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ToastUtil.showToast(mContext, R.string.register_code_sent);
			}
		};
		String telephone = mUserPhone.getText().toString();
		String compressedStr = DynamicCodeRequest.verifyEncode(telephone + "_"
				+ System.currentTimeMillis() / 1000);
		BRRequestParams params = new BRRequestParams();
		params.setDeviceId(ApplicationController.getInstance().getDeviceId());
		params.setVerCode(ApplicationController.getInstance().getVerCode());
		params.put("Telephone", telephone);
		params.put("verify", compressedStr);
		params.put("type", 1);

		BRFastRequest request = new DynamicCodeRequest.Builder()
				.setSucceedListener(succeedResponse)
				.setErrorListener(mErrorListener).setParams(params).build();

		ApplicationController.getInstance().getRequestQueue().add(request);

	}

}
