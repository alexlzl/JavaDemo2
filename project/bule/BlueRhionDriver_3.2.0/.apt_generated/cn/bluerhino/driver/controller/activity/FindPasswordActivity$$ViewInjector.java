// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FindPasswordActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.FindPasswordActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427392, "field 'mSecurityCode'");
    target.mSecurityCode = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427430, "field 'mbackbtn'");
    target.mbackbtn = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131427394, "field 'mUserPassword'");
    target.mUserPassword = (android.widget.EditText) view;
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
    view = finder.findRequiredView(source, 2131427399, "field 'mResetBtn' and method 'findPassword'");
    target.mResetBtn = (android.widget.Button) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.findPassword();
        }
      });
    view = finder.findRequiredView(source, 2131427393, "field 'mSecurityCodeBtn'");
    target.mSecurityCodeBtn = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427432, "field 'mTitle'");
    target.mTitle = (android.widget.TextView) view;
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
    view = finder.findRequiredView(source, 2131427391, "field 'mUserPhone'");
    target.mUserPhone = (android.widget.EditText) view;
  }

  public static void reset(cn.bluerhino.driver.controller.activity.FindPasswordActivity target) {
    target.mSecurityCode = null;
    target.mbackbtn = null;
    target.mUserPassword = null;
    target.mDeletePhoneView = null;
    target.mResetBtn = null;
    target.mSecurityCodeBtn = null;
    target.mTitle = null;
    target.mDeletePasswordView = null;
    target.mUserPhone = null;
  }
}
