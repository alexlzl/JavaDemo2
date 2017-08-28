// Generated code from Butter Knife. Do not modify!
package cn.bluerhino.driver.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class UmengUpdateDialog$$ViewInjector {
  public static void inject(Finder finder, final cn.bluerhino.driver.view.UmengUpdateDialog target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427588, "field 'mOkayButton' and method 'downLoadApk'");
    target.mOkayButton = (android.widget.Button) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.downLoadApk();
        }
      });
    view = finder.findRequiredView(source, 2131427589, "field 'mCancelButton' and method 'cancelUpdate'");
    target.mCancelButton = (android.widget.Button) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.cancelUpdate();
        }
      });
    view = finder.findRequiredView(source, 2131427587, "field 'mUpdateIgnoreCheckBox' and method 'onIgonreCheckUpdate'");
    target.mUpdateIgnoreCheckBox = (android.widget.CheckBox) view;
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.onIgonreCheckUpdate(p1);
        }
      });
    view = finder.findRequiredView(source, 2131427586, "field 'mMessageTextView'");
    target.mMessageTextView = (android.widget.TextView) view;
  }

  public static void reset(cn.bluerhino.driver.view.UmengUpdateDialog target) {
    target.mOkayButton = null;
    target.mCancelButton = null;
    target.mUpdateIgnoreCheckBox = null;
    target.mMessageTextView = null;
  }
}
