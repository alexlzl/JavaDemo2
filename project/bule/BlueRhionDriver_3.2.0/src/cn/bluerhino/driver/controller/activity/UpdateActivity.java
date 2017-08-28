package cn.bluerhino.driver.controller.activity;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateResponse;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.utils.AppManager;
import cn.bluerhino.driver.utils.ConstantsManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Describe:升级窗口
 * 
 * Date:2015-8-19
 * 
 * Author:liuzhouliang
 */
public class UpdateActivity extends BaseParentActivity implements
		OnClickListener {
	private boolean isForceUpdate;
	private LinearLayout llLeft, llLRight;
	private TextView tvLeft, tvRight, tvContent;
	private UpdateResponse response;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		init();
	}

	private void init() {
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		setContentView(R.layout.confirm_dialog);
		getChildView();
		getIntentData();
		setViewListener();
	}

	private void getChildView() {
		tvRight = (TextView) findViewById(R.id.confirm_dialog_right_tv);
		tvLeft = (TextView) findViewById(R.id.confirm_dialog_left_tv);
		tvContent = (TextView) findViewById(R.id.confirm_dialog_content_tv);
		llLeft = (LinearLayout) findViewById(R.id.confirm_dialog_left_ll);
		llLRight = (LinearLayout) findViewById(R.id.confirm_dialog_right_ll);
	}

	/**
	 * 
	 * Describe:获取回传参数
	 * 
	 * Date:2015-8-20
	 * 
	 * Author:liuzhouliang
	 */
	private void getIntentData() {
		isForceUpdate = getIntent().getBooleanExtra(
				ConstantsManager.IS_FORCE_UPDATE_KEY, false);
		response = (UpdateResponse) getIntent()
				.getSerializableExtra("response");
		tvContent.setText(response.updateLog);
		tvLeft.setText("升级");
		if (isForceUpdate) {
			/**
			 * 强制升级
			 */
			tvRight.setText("退出");
		} else {
			/**
			 * 非强制升级
			 */
			tvRight.setText("取消");
		}
	}

	private void setViewListener() {
		llLeft.setOnClickListener(this);
		llLRight.setOnClickListener(this);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			setIntent(intent);
		}
		getIntentData();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& isOutOfBounds(this, event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 
	 * Describe:处理空白区域触摸事件
	 * 
	 * Date:2015-8-20
	 * 
	 * Author:liuzhouliang
	 */
	private boolean isOutOfBounds(Activity context, MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context)
				.getScaledWindowTouchSlop();
		final View decorView = context.getWindow().getDecorView();
		return (x < -slop) || (y < -slop)
				|| (x > (decorView.getWidth() + slop))
				|| (y > (decorView.getHeight() + slop));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.confirm_dialog_left_ll:
			/**
			 * 左侧控件事件:响应下载
			 */
			UmengUpdateAgent.startDownload(mContext, response);
			break;

		case R.id.confirm_dialog_right_ll:
			/**
			 * 右侧视图事件
			 */
			if (isForceUpdate) {
				/**
				 * 强制升级:退出程序
				 */
				AppManager.getAppManager().exitApp(mContext);
			} else {
				/**
				 * 非强制升级：隐藏窗口
				 */
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
				finish();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		return;
	}

}
