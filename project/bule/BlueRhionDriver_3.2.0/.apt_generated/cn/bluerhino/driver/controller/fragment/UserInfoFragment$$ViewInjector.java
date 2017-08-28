// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class UserInfoFragment$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.fragment.UserInfoFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427505, "field 'mAnnounceLayout'");
    target.mAnnounceLayout = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131427502, "field 'mTodayRevenue'");
    target.mTodayRevenue = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427355, "field 'mCurtain'");
    target.mCurtain = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131427499, "field 'mTodayOrders'");
    target.mTodayOrders = (android.widget.TextView) view;
  }

  public static void reset(cn.bluerhino.driver.controller.fragment.UserInfoFragment target) {
    target.mAnnounceLayout = null;
    target.mTodayRevenue = null;
    target.mCurtain = null;
    target.mTodayOrders = null;
  }
}
