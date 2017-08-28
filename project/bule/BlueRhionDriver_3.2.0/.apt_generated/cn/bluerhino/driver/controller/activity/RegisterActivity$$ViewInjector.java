// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.RegisterActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427395, "field 'mDeletePasswordView' and method 'clearPassword'");
    target.mDeletePasswordView = (android.widget.ImageView) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clearPassword();
        }
      });
    view = finder.findRequiredView(source, 2131427392, "field 'mSecurityCode'");
    target.mSecurityCode = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427430, "field 'mbackbtn'");
    target.mbackbtn = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131427393, "field 'mSecurityCodeBtn'");
    target.mSecurityCodeBtn = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427349, "field 'mDeletePhoneView' and method 'clearPhone'");
    target.mDeletePhoneView = (android.widget.ImageView) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clearPhone();
        }
      });
    view = finder.findRequiredView(source, 2131427394, "field 'mUserPassword'");
    target.mUserPassword = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427391, "field 'mUserPhone'");
    target.mUserPhone = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427432, "field 'mTitle'");
    target.mTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427399, "method 'register'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
  }

  public static void reset(cn.bluerhino.driver.controller.activity.RegisterActivity target) {
    target.mDeletePasswordView = null;
    target.mSecurityCode = null;
    target.mbackbtn = null;
    target.mSecurityCodeBtn = null;
    target.mDeletePhoneView = null;
    target.mUserPassword = null;
    target.mUserPhone = null;
    target.mTitle = null;
  }
}
