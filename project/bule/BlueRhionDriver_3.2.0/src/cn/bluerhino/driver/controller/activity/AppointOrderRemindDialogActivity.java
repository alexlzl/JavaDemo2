package cn.bluerhino.driver.controller.activity;

import cn.bluerhino.driver.BRAction;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.utils.LogUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Describe:预约订单提醒对话框，距离预约时间一个小时前提醒
 * 
 * Date:2015-7-9
 * 
 * Author:liuzhouliang
 */
public class AppointOrderRemindDialogActivity extends Activity implements
		OnClickListener {
	private static final String TAG = AppointOrderRemindDialogActivity.class
			.getName();
	private LinearLayout llRightLayout, llLeftLayout;
	private TextView tvContent;
	private OrderInfo orderInfor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "onCreate");
		init();
		bindViewData();
		setViewListener();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		LogUtil.d(TAG, "onNewIntent");
		if (intent != null) {
			setIntent(intent);
		}
		super.onNewIntent(intent);
	}

	private void init() {
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		setContentView(R.layout.confirm_dialog);
		llRightLayout = (LinearLayout) findViewById(R.id.confirm_dialog_right_ll);
		llLeftLayout = (LinearLayout) findViewById(R.id.confirm_dialog_left_ll);
		tvContent = (TextView) findViewById(R.id.confirm_dialog_content_tv);
		llRightLayout.setVisibility(View.GONE);
		// setFinishOnTouchOutside(false);
		orderInfor = getIntent().getParcelableExtra(BRAction.EXTRA_ORDER_INFO);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& isOutOfBounds(this, event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

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

	private void setViewListener() {
		llLeftLayout.setOnClickListener(this);
	}

	private void bindViewData() {
		tvContent.setText("您有一个预约订单即将开始服务，请准时到达收货地，详细信息请查看订单详情 ");
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
			Intent intent = new Intent(this, OrderFlowActivity.class);
			intent.putExtra(BRAction.EXTRA_ORDER_INFO, orderInfor);
			intent.putExtra("fromType", "AppointOrderRemindDialogActivity");
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (KeyEvent.KEYCODE_BACK == event.getAction()) {
	// LogUtil.d("TestActivity", "KEYCODE_BACK");
	// return false;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	// @Override
	// public void onBackPressed() {
	// return;
	// }

}
