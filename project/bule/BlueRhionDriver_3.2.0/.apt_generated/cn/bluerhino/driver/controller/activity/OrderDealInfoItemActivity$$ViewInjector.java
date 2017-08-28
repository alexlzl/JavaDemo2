// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class OrderDealInfoItemActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.OrderDealInfoItemActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427410, "field 'order_type'");
    target.order_type = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427409, "field 'mBackBtn'");
    target.mBackBtn = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131427380, "field 'mKilometer'");
    target.mKilometer = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427414, "field 'mRemark'");
    target.mRemark = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427376, "field 'mPlaceofdispatch'");
    target.mPlaceofdispatch = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427415, "field 'mGetOrderButton'");
    target.mGetOrderButton = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131427412, "field 'mOrderID'");
    target.mOrderID = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427411, "field 'mOrderBill'");
    target.mOrderBill = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427413, "field 'orderinfo_item_3_grounp'");
    target.orderinfo_item_3_grounp = (android.widget.LinearLayout) view;
  }

  public static void reset(cn.bluerhino.driver.controller.activity.OrderDealInfoItemActivity target) {
    target.order_type = null;
    target.mBackBtn = null;
    target.mKilometer = null;
    target.mRemark = null;
    target.mPlaceofdispatch = null;
    target.mGetOrderButton = null;
    target.mOrderID = null;
    target.mOrderBill = null;
    target.orderinfo_item_3_grounp = null;
  }
}
