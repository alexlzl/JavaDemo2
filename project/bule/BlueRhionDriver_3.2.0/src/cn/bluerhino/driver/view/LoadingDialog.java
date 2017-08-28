package cn.bluerhino.driver.view;

import cn.bluerhino.driver.R;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadingDialog {

	private Context mContext;

	private LayoutInflater mInflater;// 填充对话框布局

	private View mView;// 将xml转成view

	private LinearLayout mRollLayout;

	private ImageView mRollImageView;

	private Animation mRollAnimation;// 加载的动画

	private Dialog mLoadingDialog;

	public LoadingDialog(Context mContext) {
		this(mContext, null);
	}

	public LoadingDialog(Context mContext, CharSequence loadingText) {
		super();
		this.mContext = mContext;
		initLoadingDialog(loadingText);
	}
	
	public LoadingDialog(Context mContext, int resId) {
		super();
		this.mContext = mContext;
		initLoadingDialog(mContext.getString(resId));
	}
	
	private void initLoadingDialog(CharSequence loaddingText) {
		mInflater = LayoutInflater.from(mContext);
		mView = mInflater.inflate(R.layout.loading_dialog, null);
		mRollLayout = (LinearLayout) mView
				.findViewById(R.id.loading_dialog_root);// 加载布局
		mRollImageView = (ImageView) mView
				.findViewById(R.id.loading_dialog_img);
		if(TextUtils.isEmpty(loaddingText)) loaddingText = "努力加载中...";
		((TextView)mView.findViewById(R.id.loading_dialog_text)).setText(loaddingText);
		mRollAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.loading_dialog_anim);
		mLoadingDialog = new Dialog(mContext, R.style.loading_dialog);// 创建自定义样式dialog
		mLoadingDialog.setCancelable(false);// 不可点击返回键取消
		mLoadingDialog.setContentView(mRollLayout,
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
	};

	public void show() {
		// 使用ImageView显示动画
		mRollImageView.startAnimation(mRollAnimation);
		mLoadingDialog.show();
	}

	public void dismiss() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}
	public boolean isShowing(){
		return mLoadingDialog != null?mLoadingDialog.isShowing():false;
	}
}
