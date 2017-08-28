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
import android.widget.ImageView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.VerificationCodeSubmit;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.main.HomeActivity;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.Md5Util;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;

/**
 * 
 * @describe:忘记密码第二步
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-12-1下午5:37:05
 * 
 */
public class ForgetPassWordStepTwo extends BaseActivity {
	private static final String TAG = "忘记密码第二步";
	private EditText etNewPassWord, etConfirmNewPassWord;
	private Button btAdd;
	private String phoneNum;
	private VerificationCodeSubmit codeSubmitBean;
	private ImageView ivCheck, ivConfirmChcek;
	private String messageCode;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.forget_password_step_two);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		etNewPassWord = (EditText) findViewById(R.id.forget_password_step_two_new_password);
		etConfirmNewPassWord = (EditText) findViewById(R.id.forget_password_step_two_confirm_password);
		btAdd = (Button) findViewById(R.id.forget_password_step_two_add);
		ivCheck = (ImageView) findViewById(R.id.forget_password_step_two_new_password_check);
		ivConfirmChcek = (ImageView) findViewById(R.id.forget_password_step_two_new_password_confirm_check);
		phoneNum = getIntent().getStringExtra("phoneNum");
		messageCode = getIntent().getStringExtra("messageCode");
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
		btAdd.setOnClickListener(this);
		etNewPassWord
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							// 此处为得到焦点时的处理内容
							if (etNewPassWord.getText().toString() != null
									&& !"".equals(etNewPassWord.getText()
											.toString())) {
								/**
								 * 有内容
								 */
								ivCheck.setImageResource(R.drawable.usercenter_write_yes);
							} else {
								/**
								 * 没有内容
								 */
								// ivCheck
								// .setImageResource(R.drawable.usercenter_write_no);
							}
						} else {
							// 此处为失去焦点时的处理内容
							if (etNewPassWord.getText().toString() != null
									&& !"".equals(etNewPassWord.getText()
											.toString())) {
								/**
								 * 有内容
								 */
								ivCheck.setImageResource(R.drawable.usercenter_write_yes);
							} else {
								/**
								 * 没有内容
								 */
								// ivCheck
								// .setImageResource(R.drawable.usercenter_write_no);
							}
						}
					}
				});
		etConfirmNewPassWord
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							// 此处为得到焦点时的处理内容
							if (etConfirmNewPassWord.getText().toString() != null
									&& !"".equals(etConfirmNewPassWord
											.getText().toString())) {
								/**
								 * 有内容
								 */
								ivConfirmChcek
										.setImageResource(R.drawable.usercenter_write_yes);
							} else {
								/**
								 * 没有内容
								 */
								// ivConfirmChcek
								// .setImageResource(R.drawable.usercenter_write_no);
							}
						} else {
							// 此处为失去焦点时的处理内容
							if (etConfirmNewPassWord.getText().toString() != null
									&& !"".equals(etConfirmNewPassWord
											.getText().toString())) {
								/**
								 * 有内容
								 */
								ivConfirmChcek
										.setImageResource(R.drawable.usercenter_write_yes);
							} else {
								/**
								 * 没有内容
								 */
								// ivConfirmChcek
								// .setImageResource(R.drawable.usercenter_write_no);
							}
						}
					}
				});
	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "找回密码";
	}

	@Override
	protected String setRightText() {
		// TODO Auto-generated method stub
		return null;
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
		case R.id.forget_password_step_two_add:
			/**
			 * 确认添加
			 */
			if (StringUtil.isEmpty(etNewPassWord.getText().toString())) {
				MsToast.makeText(baseContext, "请先输入新密码").show();
				return;
			}
			if (StringUtil.isEmpty(etConfirmNewPassWord.getText().toString())) {
				MsToast.makeText(baseContext, "请输入确认新密码").show();
				return;
			}
			if (!StringUtil.judgeStringEquals(etNewPassWord.getText()
					.toString(), etConfirmNewPassWord.getText().toString())) {
				MsToast.makeText(baseContext, "两次密码输入不一致").show();
				return;
			}
			if (etNewPassWord.getText().toString().length() < 6) {
				MsToast.makeText(baseContext, "密码长度不能少于6位").show();
				return;
			}
			if (etNewPassWord.getText().toString().length() > 16) {
				MsToast.makeText(baseContext, "密码长度不能多余16位").show();
				return;
			}
			submitNewPassWord();
			break;
		}
	}

	/**
	 * 
	 * 
	 * @describe:提交新密码
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-3上午10:53:50
	 * 
	 */
	public void submitNewPassWord() {
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("wdPassword",
				Md5Util.getMd5(etNewPassWord.getText().toString()));
		params.put("resetPassword",
				Md5Util.getMd5(etNewPassWord.getText().toString()));
		params.put("wdMobile", phoneNum);
		params.put("authCode", messageCode);
		paramsIn = MsApplication.getRequestParams(params, paramsIn,
				MsConfiguration.LOGIN_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.RESET_PASSWORD,
				new BaseJsonHttpResponseHandler<VerificationCodeSubmit>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3,
							VerificationCodeSubmit arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure===" + arg2.toString());
						Message message = new Message();
						message.what = MsConfiguration.FAIL;
						submitNewPassCode.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							VerificationCodeSubmit arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess===" + arg2.toString());
						dismissRoundProcessDialog();
					}

					@Override
					protected VerificationCodeSubmit parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "onSuccess===" + arg0.toString());
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							codeSubmitBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									VerificationCodeSubmit.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							submitNewPassCode.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							submitNewPassCode.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						// MsApplication.getDataBean(codeSubmitBean,
						// submitNewPassCode, arg0,
						// VerificationCodeSubmit.class);
						return codeSubmitBean;
					}
				});
	}

	/**
	 * 提交新密码消息返回
	 */
	Handler submitNewPassCode = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case MsConfiguration.SUCCESS:
				if (codeSubmitBean != null && codeSubmitBean.getCode() == 0) {
					Intent intent = new Intent(baseContext, Login.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					MsStartActivity.startActivity(baseActivity, intent);
				} else {
					if (codeSubmitBean != null) {
						MsToast.makeText(baseContext, codeSubmitBean.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "更新密码失败").show();
					}

				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "更新密码失败").show();
				break;
			}
		}

	};

}
