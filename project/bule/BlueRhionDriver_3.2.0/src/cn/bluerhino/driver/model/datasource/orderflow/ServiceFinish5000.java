package cn.bluerhino.driver.model.datasource.orderflow;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.model.OrderInfo;

/**
 * 服务完成
 * statecode 5000
 * 
 * @author chowjee
 * @date 2015-3-16
 */
public class ServiceFinish5000 extends OrderState {

	public ServiceFinish5000(OrderStateMachineContext machineContext) {
		super(machineContext);
	}

	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);

		leftTabBar.setText("订单结束--重新演示");

		Button rightTabBar = getRightTab();
		LayoutParams rightTabParams = (LayoutParams) rightTabBar.getLayoutParams();
		rightTabParams.weight = 0;
		rightTabBar.setLayoutParams(rightTabParams);
		
		getWaitTime().setVisibility(View.GONE);
	}

	@Override
	public int getValue() {
		return OrderInfo.OrderState.SERVICEFINISH;// 5000
	}

	@Override
	public int getNextValue() {
		return OrderInfo.OrderState.NEEDDRIVER2;// 5000;
	}

	@Override
	public String getExecuteDialogMessage() {
		return String.format(mNormalNoticeFormat, "[订单结束--重新演示]");
	}
}
