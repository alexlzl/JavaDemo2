package cn.bluerhino.driver.model.datasource.orderflow;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

public class ExceptionOrder extends OrderState {

	public ExceptionOrder(OrderStateMachineContext machineContext) {
		super(machineContext);
	}

	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);

		Button rightTabBar = getRightTab();
		LayoutParams rightTabParams = (LayoutParams) rightTabBar.getLayoutParams();
		rightTabParams.weight = 0;
		rightTabBar.setLayoutParams(rightTabParams);

		leftTabBar.setText("订单异常");
		
		getWaitTime().setVisibility(View.GONE);
	}

	@Override
	public int getValue() {
		return 0;
	}

	@Override
	public int getNextValue() {
		return 0;
	}

	@Override
	public String getExecuteDialogMessage() {
		return String.format(mNormalNoticeFormat, "[订单异常]");
	}

}
