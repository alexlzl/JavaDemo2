package com.minsheng.app.view;


import com.minsheng.app.util.LogUtil;
import com.minsheng.wash.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class MsToast extends PopupWindow {
    private static MsToast toast = null;
    private static View contentView = null;
    private static TextView tvMsg = null;
    // 延时时间
    private long time = 1000;
    private Activity mActivity = null;
    private DismissAsyncTask dismissAsyncTask = null;
    // 关闭县城是否正在执行中
    private boolean isStart = false;

    private MsToast(View contentView, int with, int height, boolean focusable) {
        super(contentView, with, height, focusable);
    }

    /**
     * 退出界面之前需要操作关闭
     */
    public static void close() {

        if (toast != null) {
            synchronized (toast) {
                try {
                    if (toast.isStart && toast.dismissAsyncTask != null) {
                        // System.out.println("取消执行");
                        toast.dismissAsyncTask.cancel(true);
                    }
//                    System.out.println("toast.isShowing() =" + toast.isShowing() + "toast.isStart ="  +toast.isStart);
                    if (toast.isShowing()) {
                        toast.dismiss();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * 
     * @param context
     *            必须是Activity
     * @param stringId
     * @return
     */
    public static MsToast makeText(Context context, int stringId) {
        String msg = context.getResources().getString(stringId);
        return makeText(context, msg);
    }

    /**
     * 
     * @param context
     *            必须是Activity
     * @param msg
     * @return
     */
    public static MsToast makeText(Context context, String msg) {
        close();
        if (contentView == null) {
            contentView = LayoutInflater.from(context).inflate(
                    R.layout.dialog_toast, null);
            tvMsg = (TextView) contentView.findViewById(R.id.msg_tv);
        }
        toast = new MsToast(contentView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, false);
        toast.mActivity = (Activity) context;
        toast.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toast.setOutsideTouchable(false);
        toast.setAnimationStyle(android.R.style.Animation_Toast);
        toast.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                toast = null;
                // System.out.println("释放资源");
            }
        });
        tvMsg.setText(msg);
        return toast;
    }

    public void show() {
        try {
            View d_v = mActivity.getWindow().getDecorView();
            DisplayMetrics dm = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenHeight = dm.heightPixels;
            showAtLocation(d_v, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0,
                    screenHeight / 2);
            startDismiss();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("SfToast show e = " + e);
        }
    }

    /**
     * 启动延时关闭
     */
    private void startDismiss() {
        if (dismissAsyncTask == null) {
            dismissAsyncTask = new DismissAsyncTask();
        }
        dismissAsyncTask.execute(true);
    }

    public class DismissAsyncTask extends AsyncTask<Boolean, Boolean, Boolean> {
        @Override
        protected Boolean doInBackground(Boolean... params) {
            try {
                // System.out.println("等待关闭");
                isStart = true;
                Thread.sleep(time);
                // System.out.println("开始执行关闭");
            } catch (InterruptedException e) {

            }
            return true;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            isStart = false;
            // System.out.println("取消执行完成");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            isStart = false;
            close();
            // System.out.println("执行完成");
        }
    }
}
