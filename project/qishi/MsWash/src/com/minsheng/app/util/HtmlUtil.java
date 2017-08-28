package com.minsheng.app.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * 
 * @describe:TextView 超链接处理
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:23:33
 * 
 */
public class HtmlUtil {
	public static final String SYMBOL_PRICE = "&#165;"; // 价格双杠符号
	public static final String TEXT_RED_HTML_FONT_LEFT = "<font color=\"#ff0000\">"; // 字体红色左标记
	public static final String TEXT_ORANER_HTML_FONT_LEFT = "<font color=\"#fa6400\">"; // 字体橙色左标记
	public static final String TEXT_HTML_FONT_RIGHT = "</font>"; // 字体右标记

	/**
	 * 超连接点击点击打开URL
	 * 
	 * @param v
	 */
	public static void htmlDoLink(Context context, TextView v) {
		v.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence text = v.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) v.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();// should clear old spans
			for (URLSpan url : urls) {
				SfURLSpan sfUrl = new SfURLSpan(context, url.getURL());
				style.setSpan(sfUrl, sp.getSpanStart(url), sp.getSpanEnd(url),
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			v.setText(style);
		}
	}

	private static class SfURLSpan extends ClickableSpan {

		private Context mContext;
		private String mUrl;

		public SfURLSpan(Context context, String url) {
			this.mContext = context;
			this.mUrl = url;
		}

		@Override
		public void onClick(View v) {
			LogUtil.d(mUrl);
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(mUrl);
			intent.setData(content_url);
			mContext.startActivity(intent);
		}
	}

}
