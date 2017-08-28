package cn.bluerhino.driver.view.framework;

import android.widget.TabHost.OnTabChangeListener;

public interface FragmentTabComponent extends OnTabChangeListener{
	public void setup();

	public FragmentTabAdapter getTabDataSource();
}
