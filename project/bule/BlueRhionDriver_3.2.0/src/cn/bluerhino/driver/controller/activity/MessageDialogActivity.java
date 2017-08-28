package cn.bluerhino.driver.controller.activity;

import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.utils.CheckUpdateServer;
import cn.bluerhino.driver.utils.ConstantsManager;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Describe:系统消息通知对话框
 * 
 * Date:2015-7-8
 * 
 * Author:liuzhouliang
 */
public class MessageDialogActivity extends Activity implements OnClickListener {
	private static final String TAG = MessageDialogActivity.class.getName();
	/**
	 * 底部按钮布局
	 */
	private LinearLayout llRightLayout, llLeftLayout;
	private TextView tvContent, tvLeftTextView, tvRightTextView;;
	private String messageContentString, messageTypeString, messageTitleString;
	private boolean isNeedPlayVoive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "onCreate");
		init();
		bindViewData();
		setViewListener();
	}

	/**
	 * 
	 * Describe:初始化
	 * 
	 * Date:2015-8-28
	 * 
	 * Author:liuzhouliang
	 */
	private void init() {
		getIntentData();
		if (!StringUtil.isEmpty(messageContentString)) {
			LogUtil.d(TAG, "content==" + messageContentString);
		}
		if (!StringUtil.isEmpty(messageTypeString)) {
			LogUtil.d(TAG, "type==" + messageTypeString);
		}

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		setContentView(R.layout.confirm_dialog);
		llRightLayout = (LinearLayout) findViewById(R.id.confirm_dialog_right_ll);
		llLeftLayout = (LinearLayout) findViewById(R.id.confirm_dialog_left_ll);
		tvContent = (TextView) findViewById(R.id.confirm_dialog_content_tv);
		tvLeftTextView = (TextView) findViewById(R.id.confirm_dialog_left_tv);
		tvRightTextView = (TextView) findViewById(R.id.confirm_dialog_right_tv);
		setFinishOnTouchOutside(false);

	}

	/**
	 * 
	 * Describe:获取传入的参数
	 * 
	 * Date:2015-7-13
	 * 
	 * Author:liuzhouliang
	 */
	private void getIntentData() {
		messageContentString = getIntent().getStringExtra(
				ConstantsManager.PUSH_MESSAGE_DIALOG_CONTENT);
		messageTypeString = getIntent().getStringExtra(
				ConstantsManager.PUSH_MESSAGE_DIALOG_ACTION_TYPE);
		messageTitleString = getIntent().getStringExtra(
				ConstantsManager.PUSH_MESSAGE_DIALOG_ACTION_URL);
		isNeedPlayVoive = getIntent().getBooleanExtra(ConstantsManager.ISNEEDPLAYVOIVE, true);
	}

	/**
	 * 
	 * Describe:设置监听
	 * 
	 * Date:2015-8-28
	 * 
	 * Author:liuzhouliang
	 */
	private void setViewListener() {
		llLeftLayout.setOnClickListener(this);
		llRightLayout.setOnClickListener(this);
	}

	/**
	 * 
	 * Describe:绑定数据
	 * 
	 * Date:2015-8-28
	 * 
	 * Author:liuzhouliang
	 */
	private void bindViewData() {
		tvContent.setText(messageContentString);
		String leftTitle = messageTypeString;
		if(TextUtils.isEmpty(leftTitle)){
			leftTitle = "我知道了";
		}
		tvLeftTextView.setText(leftTitle);
		tvRightTextView.setText("忽略");
		//500000 二代协议
		if (isPageIntentType()) {
			/**
			 * 二代协议页面
			 */
		} 
		//自定义升级
		else if (!StringUtil.isEmpty(messageTitleString)
				&& messageTitleString.contains(ConstantsManager.UPDATE_ACTION)) {
			/**
			 * 升级页面
			 */
		} else //包括400000  系统推送提示信息
		{
			/**
			 * 系统消息
			 */
			llRightLayout.setVisibility(View.GONE);
			if(isNeedPlayVoive){
				speakVoice();
			}
		}

	}

	/**
	 * 
	 * Describe:判断是否是二代协议跳转页面
	 * 
	 * Date:2015-8-28
	 * 
	 * Author:liuzhouliang
	 */
	private boolean isPageIntentType() {
		if (!StringUtil.isEmpty(messageTitleString)) {
			if (messageTitleString.contains(ConstantsManager.COMPETITIONLIST)) {
				/**
				 * 跳转到抢单页面
				 */
				return true;
			} else if (messageTitleString
					.contains(ConstantsManager.CURRENTLIST)) {
				/**
				 * 当前页面
				 */
				return true;
			} else if (messageTitleString
					.contains(ConstantsManager.PERSONFINFO)) {
				/**
				 * 个人信息页面
				 */
				return true;
			} else if (messageTitleString
					.contains(ConstantsManager.HISTORYLIST)) {
				/**
				 * 历史订单
				 */
				return true;
			} else if (messageTitleString.contains(ConstantsManager.MYACCOUNT)) {
				/**
				 * 账号信息页面
				 */
				return true;
			} else if (messageTitleString
					.contains(ConstantsManager.ACCOUNTDETAIL)) {
				/**
				 * 账号详情页面
				 */
				return true;
			} else if (messageTitleString
					.contains(ConstantsManager.COMMENTINFO)) {
				/**
				 * 客户评分
				 */
				return true;
			} else if (messageTitleString.contains(ConstantsManager.INVITE)) {
				/**
				 * 推荐客户页面
				 */
				return true;
			} else if (messageTitleString
					.contains(ConstantsManager.COMPETITIONDETAIL)) {
				/**
				 * 抢单页面详情
				 */
				return true;
			} else if (messageTitleString
					.contains(ConstantsManager.SERVICEORDERDETAIL)) {
				/**
				 * 服务订单详情
				 */
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * 
	 * Describe:播放语音
	 * 
	 * Date:2015-8-28
	 * 
	 * Author:liuzhouliang
	 */
	private void speakVoice() {
		ApplicationController.getInstance().getBaiduTts()
				.speak(messageContentString);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			setIntent(intent);
		}
		getIntentData();
		bindViewData();
		LogUtil.d(TAG, "onNewIntent====" + "content==" + messageContentString
				+ "type==" + messageTypeString);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_dialog_left_ll:
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			finish();
			if (!StringUtil.isEmpty(messageTitleString)) {
				if (messageTitleString
						.contains(ConstantsManager.COMPETITIONLIST)) {
					/**
					 * 跳转到抢单页面
					 */
					intoDestinationActivity();
				} else if (messageTitleString
						.contains(ConstantsManager.CURRENTLIST)) {
					/**
					 * 当前页面
					 */
					intoDestinationActivity();
				} else if (messageTitleString
						.contains(ConstantsManager.PERSONFINFO)) {
					/**
					 * 个人信息页面
					 */
					intoDestinationActivity();
				} else if (messageTitleString
						.contains(ConstantsManager.HISTORYLIST)) {
					/**
					 * 历史订单
					 */
					intoDestinationActivity();
				} else if (messageTitleString
						.contains(ConstantsManager.MYACCOUNT)) {
					/**
					 * 账号信息页面
					 */
					intoDestinationActivity();
				} else if (messageTitleString
						.contains(ConstantsManager.ACCOUNTDETAIL)) {
					/**
					 * 账号详情页面
					 */
					intoDestinationActivity();
				} else if (messageTitleString
						.contains(ConstantsManager.COMMENTINFO)) {
					/**
					 * 客户评分
					 */
					intoDestinationActivity();
				} else if (messageTitleString.contains(ConstantsManager.INVITE)) {
					/**
					 * 推荐客户页面
					 */
					intoDestinationActivity();
				} else if (messageTitleString
						.contains(ConstantsManager.COMPETITIONDETAIL)) {
					/**
					 * 抢单页面详情
					 */
					intoDestinationActivity();
				} else if (messageTitleString
						.contains(ConstantsManager.SERVICEORDERDETAIL)) {
					/**
					 * 服务订单详情
					 */
					intoDestinationActivity();
				} else if (messageTitleString
						.contains(ConstantsManager.UPDATE_ACTION)) {
					/**
					 * 升级页面
					 */
					new CheckUpdateServer(this).checkUpdate();
				}
			}
			break;
		case R.id.confirm_dialog_right_ll:
			/**
			 * 取消
			 */
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * Describe:跳转到目的页面
	 * 
	 * Date:2015-8-28
	 * 
	 * Author:liuzhouliang
	 */
	private void intoDestinationActivity() {
		Intent intent = new Intent(this, PageRedirectActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		intent.setData(Uri.parse(messageTitleString));
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == event.getAction()) {
			LogUtil.d("TestActivity", "KEYCODE_BACK");
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		return;
	}
}
