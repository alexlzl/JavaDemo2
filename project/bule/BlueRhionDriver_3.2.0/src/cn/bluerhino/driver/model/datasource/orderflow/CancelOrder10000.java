package cn.bluerhino.driver.model.datasource.orderflow;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.model.OrderInfo;

/**
 * 订单取消
 * 
 * @author chowjee
 * @date 2015-3-16
 */
public class CancelOrder10000 extends OrderState {

	public CancelOrder10000(OrderStateMachineContext machineContext) {
		super(machineContext);
	}

	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);

		leftTabBar.setText("订单取消");

		Button rightTabBar = getRightTab();
		LayoutParams rightTabParams = (LayoutParams) rightTabBar.getLayoutParams();
		rightTabParams.weight = 0;
		rightTabBar.setLayoutParams(rightTabParams);

		getWaitTime().setVisibility(View.GONE);
		
		setOnClickListener(this);
	}

	@Override
	public int getValue() {
		return OrderInfo.OrderState.ORDERCANCEL;// 10000
	}

	@Override
	public int getNextValue() {
		return OrderInfo.OrderState.ORDERCANCEL;// 10000;
	}

	@Override
	public String getExecuteDialogMessage() {
		return String.format(mNormalNoticeFormat, "[订单取消]");
	}
}
