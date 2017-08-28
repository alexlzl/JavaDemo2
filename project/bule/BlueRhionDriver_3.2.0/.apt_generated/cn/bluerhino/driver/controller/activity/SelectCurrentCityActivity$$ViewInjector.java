// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SelectCurrentCityActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.SelectCurrentCityActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427408, "field 'myactionbar'");
    target.myactionbar = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131427432, "field 'mCommonTitle'");
    target.mCommonTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427446, "field 'current_city_textview'");
    target.current_city_textview = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427430, "field 'left_img'");
    target.left_img = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131427447, "field 'mCityGridView'");
    target.mCityGridView = (android.widget.GridView) view;
  }

  public static void reset(cn.bluerhino.driver.controller.activity.SelectCurrentCityActivity target) {
    target.myactionbar = null;
    target.mCommonTitle = null;
    target.current_city_textview = null;
    target.left_img = null;
    target.mCityGridView = null;
  }
}
