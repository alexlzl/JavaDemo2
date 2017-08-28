// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427408, "field 'myActionBar'");
    target.myActionBar = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131427432, "field 'mTitle'");
    target.mTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427434, "field 'mRightText'");
    target.mRightText = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427431, "field 'mLeftTitle'");
    target.mLeftTitle = (android.widget.TextView) view;
  }

  public static void reset(cn.bluerhino.driver.controller.activity.MainActivity target) {
    target.myActionBar = null;
    target.mTitle = null;
    target.mRightText = null;
    target.mLeftTitle = null;
  }
}
