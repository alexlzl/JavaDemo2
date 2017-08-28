// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class OrderFlowActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.OrderFlowActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427386, "field 'money_need_cost'");
    target.money_need_cost = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427537, "field 'mRightTab'");
    target.mRightTab = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131427377, "field 'orderinfo_item_user'");
    target.orderinfo_item_user = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427536, "field 'mLeftTab'");
    target.mLeftTab = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131427376, "field 'place_of_dispatch'");
    target.place_of_dispatch = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427383, "field 'orderinfo_remark_info_detail'");
    target.orderinfo_remark_info_detail = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131427378, "field 'orderinfo_item_userphone' and method 'callPhone'");
    target.orderinfo_item_userphone = (android.widget.TextView) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.callPhone();
        }
      });
    view = finder.findRequiredView(source, 2131427375, "field 'mOrderinfo_orderid'");
    target.mOrderinfo_orderid = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427384, "field 'orderbill_cost'");
    target.orderbill_cost = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427430, "field 'leftBar_img' and method 'handleBarEvent'");
    target.leftBar_img = (android.widget.ImageView) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.handleBarEvent(p0);
        }
      });
    view = finder.findRequiredView(source, 2131427385, "field 'waiting_for_the_cost'");
    target.waiting_for_the_cost = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427435, "field 'rightBar_img' and method 'handleBarEvent'");
    target.rightBar_img = (android.widget.ImageView) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.handleBarEvent(p0);
        }
      });
    view = finder.findRequiredView(source, 2131427389, "field 'mWaitTimeView'");
    target.mWaitTimeView = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131427380, "field 'orderinfo_item_kilometer'");
    target.orderinfo_item_kilometer = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427432, "field 'mTitle'");
    target.mTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427379, "field 'place_of_Shipping'");
    target.place_of_Shipping = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131427382, "field 'orderinfo_remark_info'");
    target.orderinfo_remark_info = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427381, "method 'handleBarEvent'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.handleBarEvent(p0);
        }
      });
  }

  public static void reset(cn.bluerhino.driver.controller.activity.OrderFlowActivity target) {
    target.money_need_cost = null;
    target.mRightTab = null;
    target.orderinfo_item_user = null;
    target.mLeftTab = null;
    target.place_of_dispatch = null;
    target.orderinfo_remark_info_detail = null;
    target.orderinfo_item_userphone = null;
    target.mOrderinfo_orderid = null;
    target.orderbill_cost = null;
    target.leftBar_img = null;
    target.waiting_for_the_cost = null;
    target.rightBar_img = null;
    target.mWaitTimeView = null;
    target.orderinfo_item_kilometer = null;
    target.mTitle = null;
    target.place_of_Shipping = null;
    target.orderinfo_remark_info = null;
  }
}
