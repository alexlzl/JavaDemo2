package cn.bluerhino.driver.controller.activity;

import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;

/**
 * 历史订单
 */
public class HistoryOrderActivity extends AbsOrderListActivity {

	@Override
	protected int setContentViewId() {
		return R.layout.activity_history_order;
	}

	@Override
	protected int getCenterTitle() {
		return R.string.his_act_order;
	}

	@Override
	protected int getStatisticParam() {
		return R.string.event_page_hisList;
	}

	@Override
	public void onBackPressed() {
		if (ApplicationController.getInstance().getOrderListBackHandler() != null) {
			ApplicationController.getInstance().getOrderListBackHandler()
					.sendEmptyMessage(MainActivity.BACK_CURRERTLIST);
		}
		super.onBackPressed();
	}
}
