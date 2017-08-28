// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AbsOrderListActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.AbsOrderListActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427430, "field 'mBackImageView'");
    target.mBackImageView = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131427432, "field 'mTitleTextView'");
    target.mTitleTextView = (android.widget.TextView) view;
  }

  public static void reset(cn.bluerhino.driver.controller.activity.AbsOrderListActivity target) {
    target.mBackImageView = null;
    target.mTitleTextView = null;
  }
}
