// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class WebViewActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.WebViewActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427430, "field 'mBackBtn'");
    target.mBackBtn = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131427416, "field 'mWebView'");
    target.mWebView = (android.webkit.WebView) view;
    view = finder.findRequiredView(source, 2131427417, "field 'mProgressBar'");
    target.mProgressBar = (android.widget.ProgressBar) view;
    view = finder.findRequiredView(source, 2131427433, "field 'mRightTitleTextView'");
    target.mRightTitleTextView = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427432, "field 'mPageTitleTextView'");
    target.mPageTitleTextView = (android.widget.TextView) view;
  }

  public static void reset(cn.bluerhino.driver.controller.activity.WebViewActivity target) {
    target.mBackBtn = null;
    target.mWebView = null;
    target.mProgressBar = null;
    target.mRightTitleTextView = null;
    target.mPageTitleTextView = null;
  }
}
