package com.minsheng.app.module.login;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.LoginBean;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.main.HomeActivity;
import com.minsheng.app.util.FileManager;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.Md5Util;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;

/**
 * 
 * 
 * @describe:登录界面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-16下午4:57:58
 * 
 */
public class Login extends BaseActivity {
	private static final String TAG = "登录界面";
	private EditText etPhoneNum, etPassWord;
	private Button btLogin;
	private LoginBean loginBean;
	private TextView tvForget;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		if (MsApplication.isLogin()) {
			MsStartActivity.startActivity(baseActivity, HomeActivity.class);
			finish();
		} else {
			setBaseContentView(R.layout.login);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		etPhoneNum = (EditText) findViewById(R.id.login_phonenum_et);
		etPassWord = (EditText) findViewById(R.id.login_password_et);
		btLogin = (Button) findViewById(R.id.login_bt);
		tvForget = (TextView) findViewById(R.id.login_forget_passord);
	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		btLogin.setOnClickListener(this);
		tvForget.setOnClickListener(this);
	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected String setRightText() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected int setLeftImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setMiddleImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setRightImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.login_bt:
			/**
			 * 登录
			 */
			if (StringUtil.isEmpty(etPhoneNum.getText().toString())) {
				MsToast.makeText(baseContext, "请输入手机号！").show();
				return;
			}
			boolean isPhoneNum = StringUtil.isMobileNo(etPhoneNum.getText()
					.toString());
			if (!isPhoneNum) {
				MsToast.makeText(baseContext, "请输入正确的手机格式").show();
				return;
			}
			if (StringUtil.isEmpty(etPassWord.getText().toString())) {
				MsToast.makeText(baseContext, "请输入密码！").show();
				return;
			}
			login();
			break;
		case R.id.login_forget_passord:
			/**
			 * 忘记密码
			 */
			Intent intentPassword = new Intent(this,
					ForgetPassWordStepOne.class);
			MsStartActivity.startActivity(baseActivity, intentPassword);
			break;
		}
	}

	/**
	 * 
	 * 
	 * @describe:登录
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-16下午4:57:39
	 * 
	 */
	private void login() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("wdPassword", Md5Util.getMd5(etPassWord.getText().toString()));
		map.put("wdMobile", etPhoneNum.getText().toString());
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.LOGIN_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.LOGIN,
				new BaseJsonHttpResponseHandler<LoginBean>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, LoginBean arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerLogin.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							LoginBean arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						dismissRoundProcessDialog();
					}

					@Override
					protected LoginBean parseResponse(String arg0, boolean arg1)
							throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							loginBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									LoginBean.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerLogin.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerLogin.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return loginBean;
					}
				});

	}

	/**
	 * 处理登录消息返回
	 */
	Handler handlerLogin = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:

				if (loginBean != null && loginBean.getCode() == 0) {
					/**
					 * 登录成功
					 */
					String loginToken = loginBean.getInfo().getLoginToken();
					LogUtil.d(TAG, "登录成功loginToken=" + loginToken);
					/**
					 * 缓存logintoken
					 */
					MsApplication.saveLoginToken(loginToken);
					/**
					 * 缓存用户信息
					 */
					FileManager.saveObject(baseContext, loginBean,
							MsConfiguration.LOGIN_INFOR_FILE);
					MsToast.makeText(baseContext, "登录成功").show();

					MsStartActivity.startActivity(baseActivity,
							HomeActivity.class);
					finish();

				} else {
					if (loginBean != null) {
						MsToast.makeText(baseContext, loginBean.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "登录失败").show();
					}

				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "登录失败").show();
				break;
			}
		}

	};

}
