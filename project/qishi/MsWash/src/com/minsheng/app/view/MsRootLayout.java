package com.minsheng.app.view;

import com.minsheng.app.util.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * DESCRIPTION:自定义LinearLayout 实现监听软键盘弹出事件
 * 
 * 用于处理搜索界面软件盘弹出问题
 * 
 */
public class MsRootLayout extends LinearLayout {
	private OnResizeListener mListener;

	public interface OnResizeListener {
		void OnResize(int w, int h, int oldw, int oldh);
	}

	public void setOnResizeListener(OnResizeListener l) {
		mListener = l;
	}

	public MsRootLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		LogUtil.d("SfRootLayout onSizeChanged");
		if (mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
	}
}
