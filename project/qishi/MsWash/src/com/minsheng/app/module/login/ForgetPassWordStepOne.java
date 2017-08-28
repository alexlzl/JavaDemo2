package com.minsheng.app.module.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.Header;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.GetPicMessageCode;
import com.minsheng.app.entity.VerificationCode;
import com.minsheng.app.entity.VerificationCodeSubmit;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.util.BitmapUtil;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;

/**
 * 
 * 
 * @describe:忘记密码第一步
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-16下午6:15:41
 * 
 */
public class ForgetPassWordStepOne extends BaseActivity {
	private static final String TAG = "忘记密码第一步";
	private static final int COUNT_DOWN_TIME = 1000;
	private int mCountDown = MsConfiguration.FIND_PWD_CHECK_CODE_TIME;
	private Timer countDownTimer = null;
	private EditText etPhoneNum, etMessageCode, etVerificationCode;
	private ImageView ivVerificationIv;
	private TextView tvNext;
	private Button btSendCode;
	private VerificationCode verificationCodeBean;
	private GetPicMessageCode picMessageCodeObj;
	private VerificationCodeSubmit codeSubmitBean;
	private String verificationCode;
	private ImageView ivCheck;
	private String imageCodeKey;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.forget_password_step_one);

	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		etPhoneNum = (EditText) findViewById(R.id.forget_password_step_one_phonenum);
		etMessageCode = (EditText) findViewById(R.id.forget_password_step_one_message_code);
		etVerificationCode = (EditText) findViewById(R.id.forget_password_step_one_verification_code_et);
		ivVerificationIv = (ImageView) findViewById(R.id.forget_password_step_one_verification_code_iv);
		tvNext = (TextView) findViewById(R.id.forget_password_step_one_verification_code_next);
		btSendCode = (Button) findViewById(R.id.forget_password_step_one_sendcode);
		ivCheck = (ImageView) findViewById(R.id.usercenter_register_verification_code_iv);
	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		/**
		 * 获取图片验证码
		 */
		getPicMessageCode();
	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		tvRight.setOnClickListener(this);
		tvNext.setOnClickListener(this);
		btSendCode.setOnClickListener(this);
		etMessageCode
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							// 此处为得到焦点时的处理内容
							if (etMessageCode.getText().toString() != null
									&& !"".equals(etMessageCode.getText()
											.toString())) {
								/**
								 * 有内容
								 */
								ivCheck.setImageResource(R.drawable.usercenter_write_yes);
							} else {
								/**
								 * 没有内容
								 */
							}
						} else {
							// 此处为失去焦点时的处理内容
							if (etMessageCode.getText().toString() != null
									&& !"".equals(etMessageCode.getText()
											.toString())) {
								/**
								 * 有内容
								 */
								ivCheck.setImageResource(R.drawable.usercenter_write_yes);
							} else {

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
		return "下一步";
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
		case R.id.base_activity_title_right_righttv:
			/**
			 * 下一步
			 */
			if (StringUtil.isEmpty(etPhoneNum.getText().toString())) {
				MsToast.makeText(baseContext, "请先输入手机号码").show();
				return;
			}
			if (!StringUtil.isNumber(etPhoneNum.getText().toString())) {
				MsToast.makeText(baseContext, "请先输入正确的手机号码").show();
				return;
			}
			if (StringUtil.isEmpty(etMessageCode.getText().toString())) {
				MsToast.makeText(baseContext, "请先输入短信验证码").show();
				return;
			}
			if (StringUtil.isEmpty(etVerificationCode.getText().toString())) {
				MsToast.makeText(baseContext, "请先输入右侧验证码").show();
				return;
			}
			/**
			 * 提交验证码
			 */

			submitVerificationCode();

			break;
		case R.id.forget_password_step_one_verification_code_next:
			/**
			 * 换一张
			 */
			getPicMessageCode();
			break;
		case R.id.forget_password_step_one_sendcode:
			/**
			 * 发送验证码
			 */
			if (StringUtil.isEmpty(etPhoneNum.getText().toString())) {
				MsToast.makeText(baseContext, "请先输入手机号码").show();
				return;
			}
			if (!StringUtil.isMobileNo(etPhoneNum.getText().toString())) {
				MsToast.makeText(baseContext, "请先输入正确的手机号码").show();
				return;
			}
			sendVerificationCode();

			break;
		}
	}

	/**
	 * 
	 * 
	 * @describe:提交验证码
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-16下午7:23:26
	 * 
	 */
	public void submitVerificationCode() {
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("imgCode", etVerificationCode.getText().toString());
		params.put("wdMobile", etPhoneNum.getText().toString());
		params.put("authCode", etMessageCode.getText().toString());
		params.put("imgCodeKey", imageCodeKey);
		paramsIn = MsApplication.getRequestParams(params, paramsIn,
				MsConfiguration.LOGIN_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.VERIFY_MESSAGE_CODE,
				new BaseJsonHttpResponseHandler<VerificationCodeSubmit>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3,
							VerificationCodeSubmit arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure===" + arg2.toString());
						Message message = new Message();
						message.what = MsConfiguration.FAIL;
						submitVerificationCode.sendMessage(message);
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
							submitVerificationCode.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							submitVerificationCode.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return codeSubmitBean;
					}
				});
	}

	/**
	 * 提交验证码消息返回处理
	 */
	Handler submitVerificationCode = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case MsConfiguration.SUCCESS:
				if (codeSubmitBean != null && codeSubmitBean.getCode() == 0) {
					Intent intentStepTwo = new Intent(baseContext,
							ForgetPassWordStepTwo.class);
					intentStepTwo.putExtra("phoneNum", etPhoneNum.getText()
							.toString());
					intentStepTwo.putExtra("messageCode", etMessageCode
							.getText().toString());
					MsStartActivity.startActivity(baseActivity, intentStepTwo);
				} else {
					if (codeSubmitBean != null) {
						MsToast.makeText(baseContext, codeSubmitBean.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "提交验证码失败").show();
					}

				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "提交验证码失败").show();
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:开始倒计时
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-2下午7:53:13
	 * 
	 */

	private void startCountDown() {
		mCountDown = MsConfiguration.FIND_PWD_CHECK_CODE_TIME;
		countDownTimer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				mCountDown--;
				if (mCountDown < 0) {
					countDownHandler
							.sendEmptyMessage(MsConfiguration.COUNT_DOWN_END);
					countDownTimer.cancel();
					countDownTimer = null;
				} else {
					countDownHandler
							.sendEmptyMessage(MsConfiguration.COUNT_DOWNING);
				}
			}
		};
		countDownTimer.schedule(timerTask, 0, COUNT_DOWN_TIME);
	}

	/**
	 * 倒计时消息处理
	 */
	private Handler countDownHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsConfiguration.COUNT_DOWNING:
				btSendCode.setText("重新发送" + mCountDown + "秒");
				btSendCode.setClickable(false);
				btSendCode.setBackground(baseContext.getResources()
						.getDrawable(R.drawable.sendmessage_bg));
				break;
			case MsConfiguration.COUNT_DOWN_END:
				btSendCode.setText("发送验证码");
				btSendCode.setClickable(true);
				btSendCode.setBackground(baseContext.getResources()
						.getDrawable(R.drawable.bg_blue_corner));
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 
	 * 
	 * @describe:发送手机验证码
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-16下午6:17:15
	 * 
	 */
	private void sendVerificationCode() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("wdMobile", etPhoneNum.getText().toString());
		paramsIn = MsApplication.getRequestParams(params, paramsIn,
				MsConfiguration.LOGIN_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.SEND_VERIFICATION_CODE,
				new BaseJsonHttpResponseHandler<VerificationCode>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, VerificationCode arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure===" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = new Message();
						message.what = MsConfiguration.FAIL;
						handlerSendVerificationCode.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							VerificationCode arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess===" + arg2.toString());
						dismissRoundProcessDialog();
					}

					@Override
					protected VerificationCode parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "onSuccess===" + arg0.toString());
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							verificationCodeBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									VerificationCode.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerSendVerificationCode.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerSendVerificationCode.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return verificationCodeBean;
					}
				});
	}

	/**
	 * 获取手机验证码消息处理
	 */
	Handler handlerSendVerificationCode = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (verificationCodeBean != null
						&& verificationCodeBean.getCode() == 0) {
//					etMessageCode.setText(verificationCodeBean.getInfo()
//							.getAuthCode());
					startCountDown();
				} else {
					if (verificationCodeBean != null) {
						MsToast.makeText(baseContext,
								verificationCodeBean.getMsg()).show();
					} else {
						MsToast.makeText(baseContext, "发送验证码失败").show();
						LogUtil.d(TAG, "验证码返回空");
					}

				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "发送验证码失败").show();
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:获取验证码图片
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-2下午8:57:21
	 * 
	 */
	private void getPicMessageCode() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.LOGIN_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.GET_PIC_MESSAGE_CODE,
				new BaseJsonHttpResponseHandler<GetPicMessageCode>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, GetPicMessageCode arg4) {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "onFailure===" + arg2.toString());
						Message message = new Message();
						message.what = MsConfiguration.FAIL;
						handlerGetPicMessageCode.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							GetPicMessageCode arg3) {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
					}

					@Override
					protected GetPicMessageCode parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "onSuccess===" + arg0.toString());
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							picMessageCodeObj = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									GetPicMessageCode.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerGetPicMessageCode.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerGetPicMessageCode.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return picMessageCodeObj;
					}
				});
	}

	/**
	 * 处理获取验证码图片消息返回
	 */
	Handler handlerGetPicMessageCode = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (picMessageCodeObj != null
						&& picMessageCodeObj.getCode() == 0) {
					String picData = picMessageCodeObj.getInfo().getImgByte();
					imageCodeKey = picMessageCodeObj.getInfo().getAuthCodeKey();
					LogUtil.d(TAG, "验证码图片==" + picData);
					Bitmap bitmap = BitmapUtil.convertStringToBitmap(picData);
					ivVerificationIv.setBackground(BitmapUtil
							.bitmapToDrawable(bitmap));
				} else {
					LogUtil.d(TAG, "验证码图片返回空");
					if (picMessageCodeObj != null) {

						MsToast.makeText(baseContext, "获取验证码失败").show();
					} else {
						MsToast.makeText(baseContext,
								picMessageCodeObj.getMsg()).show();
					}

				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "获取验证码失败").show();
				break;
			}
		}

	};
}
