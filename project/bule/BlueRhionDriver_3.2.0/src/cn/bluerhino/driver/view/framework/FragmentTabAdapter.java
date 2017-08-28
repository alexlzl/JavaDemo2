package cn.bluerhino.driver.view.framework;

import android.view.View;

public interface FragmentTabAdapter {

	public int getCount();

	public FragmentTabInfo getItem(int position);

	public View getView(int position);
}
