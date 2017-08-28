package cn.bluerhino.driver.model.datasource.orderflow;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.model.OrderInfo;

/**
 * 待用户确认2200 statecode 2200
 * 
 * @author chowjee
 * @date 2015-3-16
 */
public class WaitSnatchOrder2200 extends OrderState {

	public WaitSnatchOrder2200(OrderStateMachineContext context) {
		super(context);
	}

	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar
				.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);
		leftTabBar.setText("等待用户确认");

		Button rightTabBar = getRightTab();
		LayoutParams rightTabParams = (LayoutParams) rightTabBar
				.getLayoutParams();
		rightTabParams.weight = 0;
		rightTabBar.setLayoutParams(rightTabParams);

		getWaitTime().setVisibility(View.GONE);
	}

	@Override
	public int getValue() {
		/**
		 * 当前状态值，等待司机响应 2600
		 */
		return OrderInfo.OrderState.WAITRESPONSE;
	}

	@Override
	public int getNextValue() {
		/**
		 * 下一个订单状态，等待服务 3000
		 */
		return OrderInfo.OrderState.WAITSERVICE;
	}

	@Override
	public String getExecuteDialogMessage() {
		return String.format(mNormalNoticeFormat, "[等待用户确认]");
	}
}
