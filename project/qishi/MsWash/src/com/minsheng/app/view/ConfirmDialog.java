package com.minsheng.app.view;

import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.wash.R;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfirmDialog extends Dialog implements
		android.view.View.OnClickListener {

	private static ConfirmDialog dialog = null;
	private static final int TEXT_ONLY_CONTENT_SIZE = 16;
	// private static final int MARGIN_CONTENT_LEFT_RIGHT = 15;
	// private static final int MARGIN_CONTENT_TOP = 30;
	private Context mContext = null;
	private String title = null;
	private String content = null;
	private String hintText = null;
	private OnConfirmListener mOnConfirmListener = null;
	private OnInputConfirmListener mOnInputConfirmListener = null;
	/**
	 * 控件部分
	 */
	private LinearLayout llRightText = null;
	private TextView tvTitle = null;
	private TextView tvContent = null;
	private TextView tvRight = null;
	private TextView tvleft = null;
	private View vContentMargin = null;
	private EditText etInput = null;
	private String left = null;
	private String right = null;
	private boolean isRightTvHide = false;
	// 是否有输入框
	private boolean hasInput = false;

	private ConfirmDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @return
	 */
	public static ConfirmDialog makeText(Context context, String title,
			String content, OnConfirmListener onConfirmListener) {
		dialog = new ConfirmDialog(context, R.style.dialog);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);

		dialog.mContext = context;
		dialog.title = title;
		dialog.content = content;
		dialog.hasInput = false;
		dialog.setContentView(R.layout.confirm_dialog);
		dialog.mOnConfirmListener = onConfirmListener;
		return dialog;
	}

	public static ConfirmDialog makeText(Context context, String content,
			OnConfirmListener onConfirmListener) {
		dialog = new ConfirmDialog(context, R.style.dialog);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);

		dialog.mContext = context;
		dialog.content = content;
		dialog.hasInput = false;
		dialog.setContentView(R.layout.confirm_dialog);
		dialog.mOnConfirmListener = onConfirmListener;
		return dialog;
	}

	/**
	 * 带输入框的确认取消对话框
	 * 
	 * @param context
	 * @param onConfirmListener
	 * @return
	 */
	public static ConfirmDialog makeText(Context context, boolean hasInput,
			String hintText, OnInputConfirmListener onInputConfirmListener) {
		dialog = new ConfirmDialog(context, R.style.dialog);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.hasInput = hasInput;
		dialog.mContext = context;
		dialog.hintText = hintText;
		dialog.setContentView(R.layout.confirm_dialog);
		dialog.mOnInputConfirmListener = onInputConfirmListener;
		return dialog;
	}

	/**
	 * 设置左侧按钮文本
	 * 
	 * @param text
	 */
	public void setLeftText(String text) {
		this.left = text;
	}

	/**
	 * 设置右侧按钮文本
	 * 
	 * @param text
	 */
	public void setRightText(String text) {
		this.right = text;
	}

	/**
	 * 设置右侧按钮文本隐藏
	 * 
	 * @param text
	 */
	public void setRightTvHide(boolean isRightTvHide) {
		this.isRightTvHide = isRightTvHide;
	}

	/**
	 * 隐藏右侧按钮
	 */
	private void hideRightLayout() {
		LogUtil.d("hideRightLayout llRightText = " + llRightText);
		if (null != llRightText) {
			llRightText.setVisibility(View.GONE);
		}
	}

	private void loadViews() {
		llRightText = (LinearLayout) findViewById(R.id.confirm_dialog_right_ll);
		tvTitle = (TextView) findViewById(R.id.confirm_dialog_title_tv);
		tvContent = (TextView) findViewById(R.id.confirm_dialog_content_tv);
		vContentMargin = (View) findViewById(R.id.confirm_dialog_content_margin_v);
		tvleft = (TextView) findViewById(R.id.confirm_dialog_left_tv);
		tvRight = (TextView) findViewById(R.id.confirm_dialog_right_tv);
		etInput = (EditText) findViewById(R.id.confirm_dialog_input_et);
		tvleft.setOnClickListener(this);
		tvRight.setOnClickListener(this);
		tvContent.setText(this.content);
		if (this.right != null) {
			tvRight.setText(this.right);
		}
		if (this.left != null) {
			tvleft.setText(this.left);
		}

		if (hasInput) {
			// 有输入框无标题内容
			etInput.setVisibility(View.VISIBLE);
			tvTitle.setVisibility(View.GONE);
			tvContent.setVisibility(View.GONE);
			if (!StringUtil.isEmpty(hintText)) {
				etInput.setHint(hintText);
				ViewUtil.setEditTextToBefore(etInput);
			}
		} else {
			// 有标题内容无输入框
			etInput.setVisibility(View.GONE);
			if (this.title != null) {
				tvTitle.setText(this.title);
				vContentMargin.setVisibility(View.GONE);
			} else {
				tvTitle.setVisibility(View.GONE);
				vContentMargin.setVisibility(View.VISIBLE);
				tvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
						TEXT_ONLY_CONTENT_SIZE);

				/*
				 * try { LinearLayout.LayoutParams lp = new
				 * LinearLayout.LayoutParams
				 * (LinearLayout.LayoutParams.WRAP_CONTENT,
				 * LinearLayout.LayoutParams.WRAP_CONTENT);
				 * lp.setMargins(MARGIN_CONTENT_LEFT_RIGHT, MARGIN_CONTENT_TOP,
				 * MARGIN_CONTENT_LEFT_RIGHT, 0); tvContent.setLayoutParams(lp);
				 * } catch (Exception e) { e.printStackTrace(); }
				 */
			}
		}
	}

	public void showDialog() {
		loadViews();
		if (isRightTvHide) {
			hideRightLayout();
		}
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.WindowAnimation);
		WindowManager.LayoutParams wm = window.getAttributes();
		wm.gravity = Gravity.CENTER;
		wm.width = ViewUtil.getScreenWith(mContext) * 11 / 12;
		dialog.show();
	}

	/**
	 * 结果监听器
	 * 
	 * @author sfbest
	 * 
	 */
	public interface OnConfirmListener {
		public static final int LEFT = 1;
		public static final int RIGHT = 0;

		public void onResult(int result);
	}

	/**
	 * 带输入框的结果监听器
	 * 
	 * @author sfbest
	 * 
	 */
	public interface OnInputConfirmListener {
		public static final int LEFT = 1;
		public static final int RIGHT = 0;

		public void onResult(int result, String input);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.confirm_dialog_right_tv:
			if (ConfirmDialog.this.mOnConfirmListener != null) {
				ConfirmDialog.this.mOnConfirmListener
						.onResult(OnConfirmListener.RIGHT);
			}

			if (ConfirmDialog.this.mOnInputConfirmListener != null) {
				ConfirmDialog.this.mOnInputConfirmListener.onResult(
						OnConfirmListener.RIGHT, getInputText());
			}
			ConfirmDialog.this.dismiss();
			break;
		case R.id.confirm_dialog_left_tv:
			if (ConfirmDialog.this.mOnConfirmListener != null) {
				ConfirmDialog.this.mOnConfirmListener
						.onResult(OnConfirmListener.LEFT);
			}

			if (ConfirmDialog.this.mOnInputConfirmListener != null) {
				ConfirmDialog.this.mOnInputConfirmListener.onResult(
						OnConfirmListener.LEFT, getInputText());
			}
			ConfirmDialog.this.dismiss();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取输入框中内容
	 * 
	 * @return
	 */
	private String getInputText() {
		String input = "";
		if (null != etInput && null != etInput.getText()) {
			input = etInput.getText().toString();
		}
		return input;
	}

}
