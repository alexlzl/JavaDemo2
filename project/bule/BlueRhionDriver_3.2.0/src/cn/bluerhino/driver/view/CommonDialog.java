package cn.bluerhino.driver.view;

import cn.bluerhino.driver.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * Describe:自定义通用提示框
 * 
 * Date:2015-7-8
 * 
 * Author:liuzhouliang
 */
public class CommonDialog extends Dialog implements
        android.view.View.OnClickListener {

    private static CommonDialog dialog = null;
    private Context mContext = null;
    private String title = null;
    private String content = null;
    private OnDialogListener mOnDialogListener = null;
    /**
     * 控件部分
     */
    private LinearLayout llRightText = null;
    private TextView tvTitle = null;
    private TextView tvContent = null;
    private TextView tvRight = null;
    private TextView tvleft = null;
    private String left = null;
    private String right = null;
    private boolean isHideRightLayout;

    private CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 
     * Describe:获取窗口实例
     * 
     * Date:2015-7-8
     * 
     * Author:liuzhouliang
     */
    public static CommonDialog makeText(Context context, String title,
            String content, OnDialogListener onDialogListener) {
        dialog = new CommonDialog(context, R.style.dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.mContext = context;
        dialog.title = title;
        dialog.content = content;
        dialog.setContentView(R.layout.confirm_dialog);
        dialog.mOnDialogListener = onDialogListener;
        return dialog;
    }

    public static CommonDialog makeText(Context context, String content,
            OnDialogListener onDialogListener) {
        dialog = new CommonDialog(context, R.style.dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.mContext = context;
        dialog.content = content;
        dialog.setContentView(R.layout.confirm_dialog);
        dialog.mOnDialogListener = onDialogListener;
        return dialog;
    }

    /**
     * 
     * Describe:设置左侧按钮文本
     * 
     * Date:2015-7-8
     * 
     * Author:liuzhouliang
     */
    public void setLeftText(String text) {
        this.left = text;
    }

    /**
     * 
     * Describe:设置右侧按钮文本
     * 
     * Date:2015-7-8
     * 
     * Author:liuzhouliang
     */
    public void setRightText(String text) {
        this.right = text;
    }

    /**
     * 
     * Describe:隐藏右侧按钮
     * 
     * Date:2015-7-8
     * 
     * Author:liuzhouliang
     */
    public void setRightViewHide(boolean hideLayout) {

        isHideRightLayout = hideLayout;
    }

    /**
     * 
     * Describe:显示对话框
     * 
     * Date:2015-7-8
     * 
     * Author:liuzhouliang
     */
    public void showDialog() {
        loadViews();
        if (isHideRightLayout) {
            if (null != llRightText) {
                llRightText.setVisibility(View.GONE);
            }
        }
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.WindowAnimation);
        WindowManager.LayoutParams wm = window.getAttributes();
        wm.gravity = Gravity.CENTER;
        wm.width = ViewUtil.getScreenWith(mContext) * 11 / 12;
        dialog.show();
    }

    private void loadViews() {
        llRightText = (LinearLayout) findViewById(R.id.confirm_dialog_right_ll);
        tvTitle = (TextView) findViewById(R.id.confirm_dialog_title_tv);
        tvContent = (TextView) findViewById(R.id.confirm_dialog_content_tv);
        tvleft = (TextView) findViewById(R.id.confirm_dialog_left_tv);
        tvRight = (TextView) findViewById(R.id.confirm_dialog_right_tv);
        tvleft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvContent.setText(this.content);
        if (this.right != null) {
            tvRight.setText(this.right);
        }
        if (this.left != null) {
            tvleft.setText(this.left);
        }
        if (this.title != null) {
            tvTitle.setText(this.title);
        }
    }

    /**
     * 结果监听器
     */
    public interface OnDialogListener {
        public static final int LEFT = 1;
        public static final int RIGHT = 0;

        public void onResult(int result, CommonDialog commonDialog);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.confirm_dialog_right_tv:
            if (CommonDialog.this.mOnDialogListener != null)
                CommonDialog.this.mOnDialogListener.onResult(
                        OnDialogListener.RIGHT, CommonDialog.this);
            break;
        case R.id.confirm_dialog_left_tv:
            if (CommonDialog.this.mOnDialogListener != null)
                CommonDialog.this.mOnDialogListener.onResult(
                        OnDialogListener.LEFT, CommonDialog.this);
            break;
        default:
            break;
        }
    }

}
