// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ConfirmOrderInfoActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.ConfirmOrderInfoActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427432, "field 'center_title'");
    target.center_title = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427389, "field 'mWaitTime'");
    target.mWaitTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427456, "field 'mNightPeriord'");
    target.mNightPeriord = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427457, "field 'mTotalCharge'");
    target.mTotalCharge = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427459, "field 'mNotPayed'");
    target.mNotPayed = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427430, "field 'mLeftBarButton'");
    target.mLeftBarButton = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131427458, "field 'mPayed'");
    target.mPayed = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427455, "field 'mServiceMiles'");
    target.mServiceMiles = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427460, "field 'mAacceptMoneyButton'");
    target.mAacceptMoneyButton = (android.widget.Button) view;
  }

  public static void reset(cn.bluerhino.driver.controller.activity.ConfirmOrderInfoActivity target) {
    target.center_title = null;
    target.mWaitTime = null;
    target.mNightPeriord = null;
    target.mTotalCharge = null;
    target.mNotPayed = null;
    target.mLeftBarButton = null;
    target.mPayed = null;
    target.mServiceMiles = null;
    target.mAacceptMoneyButton = null;
  }
}
