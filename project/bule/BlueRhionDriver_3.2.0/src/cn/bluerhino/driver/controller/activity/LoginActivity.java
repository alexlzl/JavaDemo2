package cn.bluerhino.driver.controller.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.bluerhino.library.utils.WeakHandler;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.CheckInStat;
import cn.bluerhino.driver.model.Loginfo;
import cn.bluerhino.driver.network.DriverLoginRequest;
import cn.bluerhino.driver.network.listener.BrErrorListener;
import cn.bluerhino.driver.utils.AppManager;
import cn.bluerhino.driver.utils.BrStartActivity;
import cn.bluerhino.driver.utils.CheckUpdateServer;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.MatchUtil;
import cn.bluerhino.driver.utils.NetWorkTools;
import cn.bluerhino.driver.utils.SharedPreferencesUtil;
import cn.bluerhino.driver.view.LoadingAnimator;

/**
 * Describe:登录页面
 * 
 * Date:2015-7-28
 * 
 * Author:liuzhouliang
 */
public class LoginActivity extends BaseParentActivity {

	private static final String TAG = LoginActivity.class.getSimpleName();
	/**
	 * 此值判断登陆按钮是否可用
	 */
	// private boolean mNetworkStatus = false;

	private MyHandler mHandler = new MyHandler(this);

	/**
	 * 获取车型列表
	 */
	private RequestQueue mRequestQueue = ApplicationController.getInstance().getRequestQueue();
	@InjectView(R.id.login_username)
	EditText mInputUserName;
	@InjectView(R.id.login_password)
	EditText mInputPassword;
	@InjectView(R.id.login_memorize_password)
	ImageView mMemorizePassword;
	@InjectView(R.id.login_remenber_password)
	LinearLayout mRemenberPassword;
	@InjectView(R.id.login_login)
	Button mLogin;
	@InjectView(R.id.register_newuser)
	TextView mRegisterNewUserBtn;
	@InjectView(R.id.forgot_pwd)
	TextView forgot_pwd;
	@InjectView(R.id.delete_name)
	ImageView mDeleteNameView;
	@InjectView(R.id.delete_phone)
	ImageView mDeletePhoneView;
	@InjectView(R.id.curtain)
	RelativeLayout mCurtain;
	@InjectView(R.id.loading_animator)
	LoadingAnimator mAnimator;
	private OnClickListener mNetworkOKOnClickListener;

	/** 记录每一次按下的时间(当间隔大于2S退出该应用) */
	private long mExitTime = -2000;


	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_login);
		ButterKnife.inject(this);
		loadView();
		checkUpdate();
		// 检测网络是否可以登录
		checkNet();
	}

	/**
	 * 
	 * Describe:初始化登陆请求
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 */
	private void initLoginButton() {
		mNetworkOKOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneNum = mInputUserName.getText().toString();
				if (!MatchUtil.isMobileNum(phoneNum)) {
					String reason = MatchUtil.StatusManager.getReason();
					ToastUtil.showToast(mContext, reason);
					return;
				}

				if (TextUtils.isEmpty(mInputPassword.getText())) {
					Toast.makeText(mContext, R.string.login_pwd_hint, Toast.LENGTH_SHORT).show();
					return;
				}

				BRRequestParams params = new BRRequestParams();
				params.put("name", mInputUserName.getText().toString());
				params.put("password", mInputPassword.getText().toString());
				params.setDeviceId(ApplicationController.getInstance().getDeviceId());
				params.setVerCode(ApplicationController.getInstance().getVerCode());
				/**
				 * 登陆请求返回数据成功回调
				 */
				BRJsonResponse succeedListener = new BRJsonResponse() {

					@Override
					public void onResponse(JSONObject response) {
						LogUtil.d(TAG, "登录成功后返回" + response.toString());
						mAnimator.stop();
						mCurtain.setVisibility(View.GONE);
						/**
						 * 判断司机能否登陆
						 */
						boolean isLogin = false;
						String sessionId = "";
						long serverTime = 0L;
						String id = "";
						int checkInstat = 0;

						try {
							if (response.has(BRRequestParams.Constant.TOKEN)) {
								sessionId = response.getString(BRRequestParams.Constant.TOKEN);
								if (sessionId != null && sessionId.trim().length() > 1) {
									isLogin = true;
								}
							}

							if (isLogin && response.has("time")) {
								serverTime = response.getLong("time");
								if (serverTime == 0) {
									isLogin = false;
								}
							}

							if (isLogin && response.has("id")) {
								id = response.getString("id");
								if (id == null || id.length() == 0) {
									isLogin = false;
								}
							}

							checkInstat = response.getInt("checkInStat");

						} catch (JSONException e) {
							isLogin = false;
							e.printStackTrace();
						}

						if (isLogin) {
							onLoginSuccess(sessionId, serverTime, id, checkInstat);
						} else {
							ToastUtil.showToast(mContext, R.string.login_error);
						}

					}
				};

				BRJsonRequest loginRequest = new DriverLoginRequest.Builder().setSucceedListener(succeedListener)
						.setErrorListener(new BrErrorListener(mContext) {
					@Override
					public void onErrorResponse(VolleyError error) {
						super.onErrorResponse(error);
						LogUtil.d(TAG, "异常返回数据" + error.getMessage());
						mAnimator.stop();
						mCurtain.setVisibility(View.GONE);
					}
				}).setParams(params).build();

				mRequestQueue.add(loginRequest);
				mCurtain.setOnClickListener(null);
				mCurtain.setVisibility(View.VISIBLE);
				mAnimator.start();
			}
		};
		mLogin.setEnabled(false);
	}

	@OnClick(R.id.delete_name)
	void deleteName() {
		mInputUserName.setText("");
	}

	@OnClick(R.id.delete_phone)
	void deletePhone() {
		mInputPassword.setText("");
	}

	/**
	 * 
	 * Describe:检测版本升级
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 */
	private void checkUpdate() {
		new CheckUpdateServer(this).checkUpdate();
	}

	private void loadView() {
		Loginfo loginfo = ApplicationController.getInstance().getLoginfo();
		mInputUserName.setText(loginfo.getUserName());
		mInputUserName.setSelection(loginfo.getUserName().length());
		mInputPassword.setText(loginfo.getPassWord());
		mInputPassword.setSelection(loginfo.getPassWord().length());
		mLogin.setText(R.string.login_check_network);

		if (TextUtils.isEmpty(loginfo.getUserName())) {
			mDeleteNameView.setVisibility(View.INVISIBLE);
		} else {
			mDeleteNameView.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(loginfo.getPassWord())) {
			mDeletePhoneView.setVisibility(View.INVISIBLE);
		} else {
			mDeletePhoneView.setVisibility(View.VISIBLE);
		}

		mInputUserName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s.toString().trim())) {
					mDeleteNameView.setVisibility(View.INVISIBLE);
				} else {
					mDeleteNameView.setVisibility(View.VISIBLE);
				}
			}
		});

		mInputPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s.toString().trim())) {
					mDeletePhoneView.setVisibility(View.INVISIBLE);
				} else {
					mDeletePhoneView.setVisibility(View.VISIBLE);
				}
			}
		});

		mMemorizePassword.setSelected(loginfo.getMemorize());
		/**
		 * 记住密码
		 */
		mRemenberPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean currtStatus = mMemorizePassword.isSelected();
				mMemorizePassword.setSelected(!currtStatus);
				ApplicationController.getInstance().getLoginfo().setMemorize(!currtStatus);
			}
		});
		/**
		 * 注册新用户
		 */
		mRegisterNewUserBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 忘记密码
		 */
		forgot_pwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
				startActivity(intent);
			}
		});

		initLoginButton();

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
		super.onDestroy();
		if(mHandler!=null){
			mHandler.removeMessages(10081);
		}
	}

	/**
	 * Describe:登录成功回调
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 * 
	 * @param checkInstat
	 * @param id
	 * @param serverTime
	 * @param sessionId
	 */
	private void onLoginSuccess(String sessionId, long serverTime, String id, int checkInstat) {
		SharedPreferencesUtil.writeSharedPreferencesString(mContext, "loginfo", "sessionID", sessionId);
		ApplicationController.getInstance().setServerTime(serverTime);
		saveAllUserInfo(id);
		ApplicationController.getInstance().setCheckInstat(CheckInStat.valueOfInt(checkInstat));
		Intent intent = new Intent(this, MainActivity.class);
		BrStartActivity.startActivity(this, intent);
	}

	/**
	 * Describe:登录成功后保存新的用户信息
	 * 
	 * Date:2015-7-28
	 * 
	 * Author:liuzhouliang
	 */
	private void saveAllUserInfo(String id) {
		Loginfo loginfo = ApplicationController.getInstance().getLoginfo();
		if (loginfo != null) {
			loginfo.setId(id);
			loginfo.setUserName(mInputUserName.getText().toString());
			boolean isSelected = mMemorizePassword.isSelected();
			loginfo.setPassWord(isSelected ? mInputPassword.getText().toString().trim() : "");
			loginfo.setMemorize(isSelected);
			loginfo.setIsLogin(true);
		}
		ApplicationController.getInstance().setLoginfo(loginfo);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			long currentTime = SystemClock.uptimeMillis();
			if (currentTime - mExitTime > 2000) {
				mExitTime = currentTime;
				ToastUtil.showToast(this, R.string.login_double_exit);
			} else {
				AppManager.getAppManager().exitApp(mContext);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * @Description: 检测网络是否可用
	 * @author: sunliang
	 * @date 2015年9月15日 上午11:24:41
	 *
	 * @return
	 */
	public void checkNet() {
		if (new NetWorkTools(this).isNetworkConnected()) {
			mLogin.setText(R.string.login_button_login);
			mLogin.setOnClickListener(mNetworkOKOnClickListener);
			mLogin.setEnabled(true);
		} else {
			mLogin.setText(R.string.login_cannot_login);
			mLogin.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtil.showToast(mContext, R.string.toast_no_net);
				}
			});
			mLogin.setEnabled(true);
			mHandler.sendEmptyMessageDelayed(10081, 2 * 1000);
		}
	}

	private class MyHandler extends WeakHandler<LoginActivity> {

		public MyHandler(LoginActivity reference) {
			super(reference);
		}

		@Override
		public void handleMessage(Message msg) {
			checkNet();
		}
	};
}
