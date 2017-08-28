package com.minsheng.app.base;

import java.util.ArrayList;
import java.util.List;
import com.minsheng.app.listener.OnBackListener;
import com.minsheng.app.util.AppManager;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.wash.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * 
 * @describe:Activity基础类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-3上午11:19:39
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener {
	private String TAG = "BaseActivity";
	public List<OnBackListener> onBackListenerList = new ArrayList<OnBackListener>();
	private static Dialog mDialog = null;
	public static Context baseContext;
	public Activity baseActivity;
	protected LayoutInflater baseLayoutInflater;
	// 返回控件
	public ImageView ivBack;
	// 导航条父容器
	public RelativeLayout rlTitlePrent;
	// 标题名称
	public TextView tvTitleName;
	// 标题栏上右侧三个区域的图标控件ivTitleMiddle
	public ImageView ivTitleLeft, ivTitleMiddle, ivTitleRight;
	// 右侧文本
	public TextView tvRight;
	// 服务ID
	protected int serviceId;
	private RelativeLayout rlReload;
	// 异常页面
	protected View loadFialView, noDataView, noNetWorkView, noOrderDataView,
			noCouponDataView;
	public LinearLayout llParent;
	private TextView tvNoDataContent;

	protected abstract void onCreate();

	/**
	 * 
	 * 
	 * @describe:是否显示标题栏
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:37:13
	 * 
	 */
	protected abstract boolean hasTitle();

	/**
	 * 
	 * @describe:加载子控件
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:37:42
	 * 
	 */

	protected abstract void loadChildView();

	/**
	 * 
	 * 
	 * @describe:获取数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:37:57
	 * 
	 */
	protected abstract void getNetData();

	/**
	 * 
	 * 
	 * @describe:重新加载控件
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:38:13
	 * 
	 */

	protected abstract void reloadCallback();

	/**
	 * 
	 * 
	 * @describe:设置子控件监听事件
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:38:32
	 * 
	 */

	protected abstract void setChildViewListener();

	/**
	 * 
	 * 
	 * @describe:设置标题栏名称
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:39:00
	 * 
	 */
	protected abstract String setTitleName();

	/**
	 * 
	 * 
	 * @describe:设置标题栏右侧文本
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:39:16
	 * 
	 */

	protected abstract String setRightText();

	/**
	 * 
	 * 
	 * @describe:设置标题栏右边左侧图片
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:39:27
	 * 
	 */
	protected abstract int setLeftImageResource();

	/**
	 * 
	 * 
	 * @describe:设置标题栏右边中间图片
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:39:59
	 * 
	 */

	protected abstract int setMiddleImageResource();

	/**
	 * 
	 * 
	 * @describe:设置标题栏右边右侧图片
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午9:40:05
	 * 
	 */

	protected abstract int setRightImageResource();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		baseLayoutInflater = getLayoutInflater();
		baseContext = this;
		baseActivity = this;
		serviceId = getIntent().getIntExtra("serviceId", 0);
		onCreate();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 结束Activity从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	/**
	 * 设置当前界面内容视图
	 */
	@SuppressLint("InflateParams")
	public void setBaseContentView(int layoutResID) {
		super.setContentView(R.layout.base_activity);
		// 获取父类外层容器
		llParent = (LinearLayout) findViewById(R.id.base_activity_rootview);
		// 加载子类控件资源
		View childMainView = baseLayoutInflater.inflate(layoutResID, null);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llParent.addView(childMainView, 1, ll);
		initParentView();
		setParentViewListener();
		setParentViewData();
		/**
		 * 子视图控制
		 */
		loadChildView();
		getNetData();
		setChildViewListener();
	}

	/**
	 * 
	 * 
	 * @describe:重新加载页面，设置内容视图
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-8下午4:04:58
	 * 
	 */
	public void setReloadContent(int layoutResID) {
		super.setContentView(R.layout.base_activity);
		// 获取父类外层容器
		llParent = (LinearLayout) findViewById(R.id.base_activity_rootview);
		// 加载子类控件资源
		View childMainView = baseLayoutInflater.inflate(layoutResID, null);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llParent.addView(childMainView, 1, ll);
		initParentView();
		setParentViewListener();
		setParentViewData();
		/**
		 * 子视图控制
		 */
		loadChildView();
		setChildViewListener();
	}

	/**
	 * 
	 * 
	 * @describe:初始化父类控件资源
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午1:59:02
	 * 
	 */
	private void initParentView() {
		// TODO Auto-generated method stub
		loadFialView = findViewById(R.id.base_activity_loadfail);
		noDataView = findViewById(R.id.base_activity_no_data);
		noNetWorkView = findViewById(R.id.base_activity_no_network);
		ivBack = (ImageView) findViewById(R.id.base_activity_title_backicon);
		rlTitlePrent = (RelativeLayout) findViewById(R.id.base_activity_title_parent);
		tvTitleName = (TextView) findViewById(R.id.base_activity_title_titlename);
		ivTitleLeft = (ImageView) findViewById(R.id.base_activity_title_right_lefticon);
		ivTitleMiddle = (ImageView) findViewById(R.id.base_activity_title_right_middleicon);
		ivTitleRight = (ImageView) findViewById(R.id.base_activity_title_right_righticon);
		tvRight = (TextView) findViewById(R.id.base_activity_title_right_righttv);
		rlReload = (RelativeLayout) findViewById(R.id.base_activity_load_fail_reload);
		tvNoDataContent = (TextView) findViewById(R.id.base_activity_no_data_content);

	}

	/**
	 * 
	 * 
	 * @describe:设置父控件监听
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午1:56:54
	 * 
	 */
	private void setParentViewListener() {
		// TODO Auto-generated method stub
		ivBack.setOnClickListener(this);
		rlReload.setOnClickListener(this);
		noNetWorkView.setOnClickListener(this);
	}

	/**
	 * 
	 * 
	 * @describe:绑定父控件数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:00:43
	 * 
	 */
	private void setParentViewData() {
		// TODO Auto-generated method stub
		if (setLeftImageResource() != 0) {
			ivTitleLeft.setVisibility(View.VISIBLE);
			ivTitleLeft.setImageResource(setLeftImageResource());
		}
		if (setMiddleImageResource() != 0) {
			ivTitleMiddle.setVisibility(View.VISIBLE);
			ivTitleMiddle.setImageResource(setMiddleImageResource());
		}
		if (setRightImageResource() != 0) {
			ivTitleRight.setVisibility(View.VISIBLE);
			ivTitleRight.setImageResource(setRightImageResource());
		}
		if (hasTitle()) {
			rlTitlePrent.setVisibility(View.VISIBLE);
		} else {
			rlTitlePrent.setVisibility(View.GONE);
		}
		tvTitleName.setText(setTitleName());
		if (rlTitlePrent.getBackground() != null) {
			rlTitlePrent.getBackground().setAlpha(230);
		}
		if (setRightText() != null) {
			tvRight.setVisibility(View.VISIBLE);
			tvRight.setText(setRightText());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.base_activity_title_backicon:
			// 返回键处理
			onBackPressed();
			break;

		case R.id.base_activity_load_fail_reload:
			/**
			 * 数据加载失败重新加载数据
			 */
			reloadCallback();
			break;

		case R.id.base_activity_no_data:
			/**
			 * 网络异常页面事件
			 */
			reloadCallback();
			break;

		case R.id.base_activity_no_network:
			/**
			 * 网络不给力，点击屏幕刷新
			 */

			reloadCallback();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		MsStartActivity.finishActivity(this);
	}

	/**
	 * 
	 * 
	 * @describe:加载对话框
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午2:08:43
	 * 
	 */
	@SuppressLint("NewApi")
	public static void showRoundProcessDialog() {

		OnKeyListener keyListener = new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_HOME
						|| keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				}
				return false;
			}
		};
		try {
			if (null != mDialog && mDialog.isShowing()) {
				return;
			}
			if (VERSION.SDK_INT < 11) {
				mDialog = new AlertDialog.Builder(baseContext).create();
			} else {
				mDialog = new AlertDialog.Builder(baseContext, R.style.WindowAnimation)
						.create();
			}
			WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
			lp.width = ViewUtil.getScreenWith(baseContext);
			lp.height = ViewUtil.getScreenHeight(baseContext);
			lp.alpha = 0.9f;
			mDialog.getWindow().setAttributes(lp);
			mDialog.setOnKeyListener(keyListener);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.show();
			// 注意此处要放在show之后 否则会报异常
			mDialog.setContentView(R.layout.loading_process_dialog_color);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 
	 * @describe:取消加载进度框
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午2:18:14
	 * 
	 */
	public static void dismissRoundProcessDialog() {
		try {
			if (null != mDialog) {
				if (mDialog.isShowing()) {
					mDialog.dismiss();
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @describe:设置加载失败页面
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @2014-11-2下午11:10:56
	 */
	protected void showLoadFailView() {
		if (llParent != null) {
			llParent.removeViewAt(1);
		}
		LogUtil.d(TAG, "showLoadFailView");
		loadFialView.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * @describe:设置数据为空页面
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @2014-11-2下午11:11:07
	 */
	protected void showNoDataView(String content) {
		if (llParent != null) {
			llParent.removeViewAt(1);
		}
		LogUtil.d(TAG, "showNoDataView");
		tvNoDataContent.setText(content);
		noDataView.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * 
	 * @describe:设置没有网络页面
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-8下午1:21:54
	 * 
	 */
	protected void showNoNetWork() {

		if (llParent != null) {
			llParent.removeViewAt(1);
		}
		LogUtil.d(TAG, "showNoNetWork");
		noNetWorkView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// JPushInterface.onPause(getApplicationContext());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// JPushInterface.onResume(getApplicationContext());
	}
}
