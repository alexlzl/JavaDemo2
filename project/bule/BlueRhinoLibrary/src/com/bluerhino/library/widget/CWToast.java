package com.bluerhino.library.widget;

/*
 * File Name：TYToast.java
 * Copyright： jejun All Rights Reserved. 
 * Description： TYToast.java
 * Modify By：jejun
 * Modify Date：2013-8-11
 * Modify Type：Add
 */

import com.bluerhino.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * 吐司提示类
 * </p>
 * <p>
 * 继承{@link android.widget.Toast}
 * </p>
 * 
 * @author jejun
 * @version v.1.0 2013-8-11
 * @since v.1.0
 */
public class CWToast extends Toast
{
    /**
     * 提示错误信息图标
     */
    public static final int TOAST_TYPE_ERROR = 0;

    /**
     * 提示一般信息图标
     */
    public static final int TOAST_TYPE_ALERT = 1;

    /**
     * 正确执行某一项操作后提示的图标
     */
    public static final int TOAST_TYPE_SUC = 2;

    private static final int TOAST_REPEAT_DURATION = 3000;

    private static long startTime;

    private static boolean isShow;

    private static CharSequence lastString;

    private boolean hasSquare;

    /**
     * 构造函数
     * 
     * @param context
     *            上下文
     */
    public CWToast(Context context)
    {
        super(context);
    }

    /**
     * 设置提示信息类型
     * 
     * @param type
     *            信息类型
     * @return this
     */
    public CWToast setToastType(int type)
    {
        int resId;
        switch (type)
        {
            case TOAST_TYPE_ALERT:
                if (hasSquare)
                {
                    resId = R.drawable.weixiao;
                }
                else
                {
                    resId = R.drawable.libs_dialog_alert_icon;
                }
                break;
            case TOAST_TYPE_ERROR:
                if (hasSquare)
                {
                    resId = R.drawable.be_grieved;
                }
                else
                {
                    resId = R.drawable.libs_dialog_err_icon;
                }
                break;
            case TOAST_TYPE_SUC:
                if (hasSquare)
                {
                    resId = R.drawable.dagou;
                }
                else
                {
                    resId = R.drawable.libs_dialog_suc_icon;
                }
                break;
            default:
                if (hasSquare)
                {
                    resId = R.drawable.weixiao;
                }
                else
                {
                    resId = R.drawable.libs_dialog_default_icon;
                }
                break;
        }
        ImageView mImageView = (ImageView) getView().findViewById(
                R.id.toastIcon);
        mImageView.setImageResource(resId);
        return this;
    }

    /**
     * 
     * Make a standard toast that just contains a text view.
     * 
     * @param context
     *            The context to use. Usually your
     *            {@link android.app.Application} or
     *            {@link android.app.Activity} object.
     * @param text
     *            The text to show. Can be formatted text.
     * @param duration
     *            How long to display the message. Either {@link #LENGTH_SHORT}
     *            or {@link #LENGTH_LONG}
     * @return {@link CWToast}
     */
    public static CWToast makeText(Context context, CharSequence text,
            int duration)
    {
        return makeText(context, text, duration, false);
    }

    /**
     * 
     * Make a standard toast that just contains a text view.
     * 
     * @param context
     *            The context to use. Usually your
     *            {@link android.app.Application} or
     *            {@link android.app.Activity} object.
     * @param text
     *            The text to show. Can be formatted text.
     * @param duration
     *            How long to display the message. Either {@link #LENGTH_SHORT}
     *            or {@link #LENGTH_LONG}
     * @param isSquare
     *            是否是圆表情
     * @return CWToast this
     */
    public static CWToast makeText(Context context, CharSequence text,
            int duration, boolean isSquare)
    {
        CWToast result = new CWToast(context);
        result.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout;
        
        if (isSquare)
        {
            layout = R.layout.libs_toast_square;
        }
        else
        {
            layout = R.layout.libs_toast;
        }
        View v = inflate.inflate(layout, null);
        TextView tv = (TextView) v.findViewById(R.id.toastMessage);
        tv.setText(text);
        result.setView(v);
        result.setDuration(duration);
        if (lastString == null || !lastString.equals(text))
        {
            startTime = System.currentTimeMillis();
            isShow = true;
        }
        else
        {
            if (System.currentTimeMillis() - startTime > TOAST_REPEAT_DURATION)
            {
                startTime = System.currentTimeMillis();
                isShow = true;
            }
            else
            {
                isShow = false;
            }
        }
        lastString = text;
        result.hasSquare = isSquare;
        return result;
    }

    /**
     * 生成土司
     * 
     * @param context
     *            上下文
     * @param resId
     *            资源ID
     * @param duration
     *            显示时间
     * @param isSquare
     *            是否是圆表情
     * @return this
     */
    public static CWToast makeText(Context context, int resId, int duration,
            boolean isSquare)
    {
        return makeText(context, context.getString(resId), duration, isSquare);
    }

    @Override
    public void show()
    {
        // CWLog.d("debug", "isShow:" + isShow);
        if (isShow)
        {
            super.show();
        }
    }

    /**
     * 
     * Make a standard toast that just contains a text view with the text from a
     * resource.
     * 
     * @param context
     *            The context to use. Usually your
     *            {@link android.app.Application} or
     *            {@link android.app.Activity} object.
     * @param resId
     *            The resource id of the string resource to use. Can be
     *            formatted text.
     * @param duration
     *            How long to display the message. Either {@link #LENGTH_SHORT}
     *            or {@link #LENGTH_LONG}
     * 
     * @return CWToast this
     */
    public static CWToast makeText(Context context, int resId, int duration)
    {
        return makeText(context, context.getResources().getText(resId),
                duration);
    }

    /**
     * 提示错误信息(默认显示在底部)
     * 
     * @param mContext
     *            上下文
     * @param resId
     *            字符资源id
     * @return Toast
     */
    public static Toast error(Context mContext, int resId)
    {
        CWToast toast = makeText(mContext, resId, Toast.LENGTH_LONG);
        toast.setToastType(CWToast.TOAST_TYPE_ERROR);
        return toast;
    }

    /**
     * 提示错误信息
     * 
     * @param mContext
     *            上下文
     * @param resId
     *            字符资源id
     * @return Toast {@link Toast}
     */
    public static Toast centerSquareError(Context mContext, int resId)
    {
        CWToast toast = makeText(mContext, resId, Toast.LENGTH_LONG, true);
        toast.setToastType(TOAST_TYPE_ERROR);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 提示错误信息
     * 
     * @param mContext
     *            上下文
     * @param msg
     *            显示信息
     * @return Toast {@link Toast}
     */
    public static Toast centerSquareError(Context mContext, String msg)
    {
        CWToast toast = makeText(mContext, msg, Toast.LENGTH_LONG, true);
        toast.setToastType(TOAST_TYPE_ERROR);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 提示错误信息
     * 
     * @param mContext
     *            上下文
     * @param resId
     *            字符资源id
     * @return Toast
     */
    public static Toast centerError(Context mContext, int resId)
    {
        Toast toast = error(mContext, resId);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 
     * 提示错误信息(默认显示在底部)
     * 
     * @param mContext
     *            上下文
     * @param msg
     *            提示信息
     * @return Toast
     */
    public static Toast error(Context mContext, CharSequence msg)
    {
        CWToast toast = makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.setToastType(CWToast.TOAST_TYPE_ERROR);
        return toast;
    }

    /**
     * 
     * 提示错误信息
     * 
     * @param mContext
     *            上下文
     * @param msg
     *            提示信息
     * @return Toast
     */
    public static Toast centerError(Context mContext, CharSequence msg)
    {
        Toast toast = error(mContext, msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 一般提示(默认显示在底部)
     * 
     * @param mContext
     *            上下文
     * @param resId
     *            字符资源id
     * @return Toast
     */
    public static Toast alert(Context mContext, int resId)
    {
        CWToast toast = makeText(mContext, resId, Toast.LENGTH_LONG);
        toast.setToastType(CWToast.TOAST_TYPE_ALERT);
        return toast;
    }

    /**
     * 一般提示(默认显示在底部)
     * 
     * @param mContext
     *            上下文
     * @param resId
     *            字符资源id
     * @return Toast
     */
    public static Toast centerSquareAlert(Context mContext, int resId)
    {
        CWToast toast = makeText(mContext, resId, Toast.LENGTH_LONG, true);
        toast.setToastType(CWToast.TOAST_TYPE_ALERT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 一般提示(默认显示在底部)
     * 
     * @param mContext
     *            上下文
     * @param msg
     *            显示信息
     * @return Toast {@link Toast}
     */
    public static Toast centerSquareAlert(Context mContext, String msg)
    {
        CWToast toast = makeText(mContext, msg, Toast.LENGTH_LONG, true);
        toast.setToastType(CWToast.TOAST_TYPE_ALERT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 一般提示
     * 
     * @param mContext
     *            上下文
     * @param resId
     *            字符资源id
     * @return Toast
     */
    public static Toast centerAlert(Context mContext, int resId)
    {
        Toast toast = alert(mContext, resId);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 一般提示(默认显示在底部)
     * 
     * @param mContext
     *            上下文
     * @param msg
     *            提示信息
     * @return Toast
     */
    public static Toast alert(Context mContext, CharSequence msg)
    {
        CWToast toast = makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.setToastType(CWToast.TOAST_TYPE_ALERT);
        return toast;
    }

    /**
     * 一般提示
     * 
     * @param mContext
     *            上下文
     * @param msg
     *            提示信息
     * @return Toast
     */
    public static Toast cetnerAlert(Context mContext, CharSequence msg)
    {
        Toast toast = alert(mContext, msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 一般提示
     * 
     * @param mContext
     *            上下文
     * @param msg
     *            提示信息
     * @return Toast
     */
    public static Toast centerSquareCorrect(Context mContext, CharSequence msg)
    {
        CWToast toast = makeText(mContext, msg, Toast.LENGTH_LONG, true);
        toast.setToastType(TOAST_TYPE_SUC);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 正确执行某项操作后的提示(默认显示在底部)
     * 
     * @param mContext
     *            上下文
     * @param resId
     *            字符资源id
     * @return Toast
     */
    public static Toast centerSquareCorrect(Context mContext, int resId)
    {
        CWToast toast = makeText(mContext, resId, Toast.LENGTH_LONG, true);
        toast.setToastType(TOAST_TYPE_SUC);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 正确执行某项操作后的提示(默认显示在底部)
     * 
     * @param mContext
     *            上下文
     * @param resId
     *            字符资源id
     * @return Toast
     */
    public static Toast correct(Context mContext, int resId)
    {
        CWToast toast = makeText(mContext, resId, Toast.LENGTH_LONG);
        toast.setToastType(CWToast.TOAST_TYPE_SUC);
        return toast;
    }

    /**
     * 
     * 正确执行某项操作后的提示
     * 
     * @param mContext
     *            上下文
     * @param resId
     *            字符资源id
     * @return Toast
     */
    public static Toast centerCorrect(Context mContext, int resId)
    {
        Toast toast = correct(mContext, resId);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 正确执行某项操作后的提示
     * 
     * @param mContext
     *            上下文
     * @param msg
     *            提示信息
     * @return Toast
     */
    public static Toast correct(Context mContext, CharSequence msg)
    {
        CWToast toast = makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.setToastType(CWToast.TOAST_TYPE_SUC);
        return toast;
    }

    /**
     * 正确执行某项操作后的提示
     * 
     * @param mContext
     *            上下文
     * @param msg
     *            提示信息
     * @return Toast
     */
    public static Toast centerCorrect(Context mContext, CharSequence msg)
    {
        Toast toast = correct(mContext, msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }
}