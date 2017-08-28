package cn.bluerhino.driver.view.framework;

import android.view.View;
import android.widget.TabHost.OnTabChangeListener;

public class FragmentTabContext extends FragmentTabDecorate {

	private FragmentTabHost mFragmentTabHost;

	public FragmentTabContext(FragmentTabComponent component,
			FragmentTabHost fragmentTabHost) {
		super(component);
		mFragmentTabHost = fragmentTabHost;
	}

	@Override
	public void setup() {
		super.setup();
		FragmentTabAdapter tabItemTarget = getTabDataSource();
		int size = tabItemTarget.getCount();
		/**
		 * 遍历添加TAB标签
		 */
		for (int i = 0; i < size; i++) {
			FragmentTabInfo info = tabItemTarget.getItem(i);
			View view = tabItemTarget.getView(i);
			if (view != null) {
				mFragmentTabHost.addTab(
						mFragmentTabHost.newTabSpec(info.getTabId())
								.setIndicator(view), info.getFragmentClass(),
						info.getBundle());
			} else {
				mFragmentTabHost.addTab(
						mFragmentTabHost.newTabSpec(info.getTabId())
								.setIndicator(""), info.getFragmentClass(),
						info.getBundle());
			}
		}
	}

	public void setOnTabChangedListener(OnTabChangeListener l) {
		mFragmentTabHost.setOnTabChangedListener(l);
	}

	public void setCurrentTabByTag(String tag) {
		mFragmentTabHost.setCurrentTabByTag(tag);
	}

	public void setCurrentTab(int index) {
		mFragmentTabHost.setCurrentTab(index);
	}

	public int getCurrentTab() {
		return mFragmentTabHost.getCurrentTab();
	}
}
