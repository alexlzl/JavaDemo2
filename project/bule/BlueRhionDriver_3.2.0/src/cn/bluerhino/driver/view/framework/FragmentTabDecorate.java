package cn.bluerhino.driver.view.framework;

public abstract class FragmentTabDecorate implements FragmentTabComponent {

	private FragmentTabComponent component;

	public FragmentTabDecorate(FragmentTabComponent component) {
		super();
		this.component = component;
	}

	@Override
	public void setup() {
		component.setup();
	}

	@Override
	public FragmentTabAdapter getTabDataSource() {
		return component.getTabDataSource();
	}

	@Override
    public void onTabChanged(String tabId) {
	    component.onTabChanged(tabId);
    }
}