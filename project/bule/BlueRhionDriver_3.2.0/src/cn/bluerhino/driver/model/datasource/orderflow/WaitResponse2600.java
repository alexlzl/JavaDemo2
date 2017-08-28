package cn.bluerhino.driver.model.datasource.orderflow;

import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.model.OrderInfo;

/**
 * 
 * Describe:当前已经完成订单状态：等待响应
 * 
 * Date:2015-7-17
 * 
 * Author:liuzhouliang
 */
public class WaitResponse2600 extends OrderState {

	public WaitResponse2600(OrderStateMachineContext orderStateMachineContext) {
		super(orderStateMachineContext);
	}

	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar
				.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);
		leftTabBar.setText("响应订单");

		Button rightTabBar = getRightTab();
		LayoutParams rightTabParams = (LayoutParams) rightTabBar
				.getLayoutParams();
		rightTabParams.weight = 0;
		rightTabBar.setLayoutParams(rightTabParams);

	}

	@Override
	public int getValue() {
		/**
		 * 当前订单状态：等待响应 2600
		 */
		return OrderInfo.OrderState.WAITRESPONSE;
	}

	@Override
	public int getNextValue() {
		/**
		 * 下一个订单状态：等待服务 3000
		 */
		return OrderInfo.OrderState.WAITSERVICE;
	}

	@Override
	public String getExecuteDialogMessage() {
		return String.format(mNormalNoticeFormat, "响应订单");
	}

}
