// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.controller.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.controller.activity.LoginActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427353, "field 'mMemorizePassword'");
    target.mMemorizePassword = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131427526, "field 'mAnimator'");
    target.mAnimator = (cn.bluerhino.driver.view.LoadingAnimator) view;
    view = finder.findRequiredView(source, 2131427351, "field 'mRegisterNewUserBtn'");
    target.mRegisterNewUserBtn = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427355, "field 'mCurtain'");
    target.mCurtain = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131427349, "field 'mDeletePhoneView' and method 'deletePhone'");
    target.mDeletePhoneView = (android.widget.ImageView) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deletePhone();
        }
      });
    view = finder.findRequiredView(source, 2131427352, "field 'mRemenberPassword'");
    target.mRemenberPassword = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131427350, "field 'mLogin'");
    target.mLogin = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131427348, "field 'mInputPassword'");
    target.mInputPassword = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427346, "field 'mInputUserName'");
    target.mInputUserName = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427354, "field 'forgot_pwd'");
    target.forgot_pwd = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427347, "field 'mDeleteNameView' and method 'deleteName'");
    target.mDeleteNameView = (android.widget.ImageView) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deleteName();
        }
      });
  }

  public static void reset(cn.bluerhino.driver.controller.activity.LoginActivity target) {
    target.mMemorizePassword = null;
    target.mAnimator = null;
    target.mRegisterNewUserBtn = null;
    target.mCurtain = null;
    target.mDeletePhoneView = null;
    target.mRemenberPassword = null;
    target.mLogin = null;
    target.mInputPassword = null;
    target.mInputUserName = null;
    target.forgot_pwd = null;
    target.mDeleteNameView = null;
  }
}
