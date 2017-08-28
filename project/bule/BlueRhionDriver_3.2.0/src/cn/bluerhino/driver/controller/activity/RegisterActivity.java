package cn.bluerhino.driver.controller.activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.network.CheckUserIsNotExistRequest;
import cn.bluerhino.driver.network.DriverRegisterRequest;
import cn.bluerhino.driver.network.DynamicCodeRequest;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.bluerhino.driver.view.LoadingDialog;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 注册新司机
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends BaseParentActivity implements
		OnClickListener {

	private static final String TAG = RegisterActivity.class.getSimpleName();
	protected static final String REQUEST_TAG = TAG;
	private static final String TOKEN = "token";
	private Context mContext;
	private VolleyErrorListener mErrorListener;
	private LoadingDialog mLoadingDialog;

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
	@InjectView(R.id.delete_phone)
	ImageView mDeletePhoneView;
	@InjectView(R.id.delete_password)
	ImageView mDeletePasswordView;
	private TextView tvRegisterRecommend;
	private EditText etRegisterRecommend;
	private RelativeLayout rlRegisterRecommend;
	private String registerRecommendNum;

	/**
	 * 
	 * Describe:注册事件
	 * 
	 * Date:2015-6-23
	 * 
	 * Author:liuzhouliang
	 */
	@OnClick(R.id.register)
	void register() {
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
		if (TextUtils.isEmpty(mUserPassword.getText())) {
			ToastUtil.showToast(mContext, R.string.register_enter_pwd);
			return;
		}

		mLoadingDialog.show();
		BRJsonRequest.BRJsonResponse succeedResponse = new BRJsonRequest.BRJsonResponse() {
			@Override
			public void onResponse(JSONObject response) {
				mLoadingDialog.dismiss();
				try {
					mToken = response.getString(TOKEN);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (!TextUtils.isEmpty(mToken)) {
					ToastUtil.showToast(mContext, R.string.register_success);
					RegisterActivity.this.finish();
				}
			}
		};

		BRRequestParams mParams = new BRRequestParams(mToken);
		mParams.setDeviceId(ApplicationController.getInstance().getDeviceId());
		mParams.setVerCode(ApplicationController.getInstance().getVerCode());
		String phone = mUserPhone.getText().toString().trim();
		String msgCode = mSecurityCode.getText().toString().trim();
		String password = mUserPassword.getText().toString().trim();
		mParams.put("phone", phone);
		mParams.put("password", password);
		mParams.put("DynamicCode", msgCode);
		if ("true".equals(rlRegisterRecommend.getTag())
				&& !StringUtil
						.isEmpty(etRegisterRecommend.getText().toString())) {
			registerRecommendNum = etRegisterRecommend.getText().toString();
			mParams.put("inviteCode", registerRecommendNum);
		}
		LogUtil.d(TAG, "注册参数" + mParams.toString());
		BRFastRequest request = new DriverRegisterRequest.Builder()
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

	private int mCount = 60;
	private Handler mHandler = new Handler();
	private Runnable mCountDownRunnable;

	private String mToken = "";

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_register_or_reset_both);
		ButterKnife.inject(this);
		mContext = getBaseContext();
		rlRegisterRecommend = (RelativeLayout) findViewById(R.id.register_recommend_parent);
		rlRegisterRecommend.setTag("false");
		etRegisterRecommend = (EditText) findViewById(R.id.register_recommend_et);
		tvRegisterRecommend = (TextView) findViewById(R.id.register_recommend_tv);
		tvRegisterRecommend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvRegisterRecommend.setOnClickListener(this);
		etRegisterRecommend = (EditText) findViewById(R.id.register_recommend_et);
		mLoadingDialog = new LoadingDialog(this, R.string.register_loading);

		mErrorListener = new VolleyErrorListener(mContext) {
			@Override
			public void onErrorResponse(VolleyError error) {
				super.onErrorResponse(error);
				mLoadingDialog.dismiss();
			}
		};

		mTitle.setText(R.string.register_newdriver_quest);
		mbackbtn.setVisibility(View.VISIBLE);
		mbackbtn.setOnClickListener(this);
		mSecurityCodeBtn.setOnClickListener(this);

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

		initAsyncCodeTask();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_img:
			finish();
			break;
		case R.id.get_security_code_button:
			checkUserIsNotExist();
			break;
		case R.id.register_recommend_tv:
			/**
			 * 市场人员推荐
			 */
			if ("false".equals(rlRegisterRecommend.getTag())) {
				/**
				 * 当前隐藏
				 */
				rlRegisterRecommend.setVisibility(View.VISIBLE);
				rlRegisterRecommend.setTag("true");
				return;
			}
			if ("true".equals(rlRegisterRecommend.getTag())) {
				/**
				 * 当前显示
				 */
				rlRegisterRecommend.setVisibility(View.GONE);
				rlRegisterRecommend.setTag("false");
				return;
			}
			break;
		}
	}

	/**
	 * 
	 * Describe:获取验证码
	 * 
	 * Date:2015-6-23
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
							mCount = 60;
							mHandler.post(mCountDownRunnable);
							getMessageSecurityCode();
						} else {
							ToastUtil.showToast(mContext,
									R.string.register_repeat);
							mSecurityCodeBtn.setEnabled(true);
							mSecurityCodeBtn.setTextColor(Color
									.parseColor("#39A1E8"));
							mSecurityCodeBtn
									.setText(R.string.register_get_smdcode);
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
				.setRequestTag(REQUEST_TAG).build();

		ApplicationController.getInstance().getRequestQueue().add(request);

	}

	/**
	 * 
	 * Describe:倒计时
	 * 
	 * Date:2015-6-23
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
	protected void onResume() {
		MobclickAgent.onPageStart(TAG);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPageEnd(TAG);
		super.onPause();
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
