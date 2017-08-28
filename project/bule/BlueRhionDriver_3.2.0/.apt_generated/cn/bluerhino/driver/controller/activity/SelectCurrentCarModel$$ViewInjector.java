// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SelectCurrentCarModel$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.SelectCurrentCarModel target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427430, "field 'left_img'");
    target.left_img = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131427408, "field 'myactionbar'");
    target.myactionbar = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131427432, "field 'center_title'");
    target.center_title = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427445, "field 'listview_carmode'");
    target.listview_carmode = (android.widget.ListView) view;
  }

  public static void reset(cn.bluerhino.driver.controller.activity.SelectCurrentCarModel target) {
    target.left_img = null;
    target.myactionbar = null;
    target.center_title = null;
    target.listview_carmode = null;
  }
}
