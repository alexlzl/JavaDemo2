package cn.bluerhino.driver.model.datasource.orderflow;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.model.OrderInfo;

public class FinishOrder20000 extends OrderState {

	public FinishOrder20000(OrderStateMachineContext machineContext) {
		super(machineContext);
	}

	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);

		leftTabBar.setText("订单结束");

		Button rightTabBar = getRightTab();
		LayoutParams rightTabParams = (LayoutParams) rightTabBar.getLayoutParams();
		rightTabParams.weight = 0;
		rightTabBar.setLayoutParams(rightTabParams);

		getWaitTime().setVisibility(View.GONE);
		
		setOnClickListener(this);
	}

	@Override
	public int getValue() {
		return OrderInfo.OrderState.ORDERFINISH;// 20000
	}

	@Override
	public int getNextValue() {
		return OrderInfo.OrderState.ORDERFINISH;// 20000
	}

	@Override
	public String getExecuteDialogMessage() {
		return String.format(mNormalNoticeFormat, "[订单完成]");
	}
}
