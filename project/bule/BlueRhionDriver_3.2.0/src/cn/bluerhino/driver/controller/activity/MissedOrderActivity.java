package cn.bluerhino.driver.controller.activity;

import cn.bluerhino.driver.R;

/**
 * 错过的订单
 */
public class MissedOrderActivity extends AbsOrderListActivity {

	@Override
	protected int setContentViewId() {
		return R.layout.activity_missed_order;
	}

	@Override
	protected int getCenterTitle() {
		return R.string.miss_act_order;
	}

	@Override
	protected int getStatisticParam() {
		return R.string.event_page_missList;
	}
}
