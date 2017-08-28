package cn.bluerhino.driver.model.datasource.orderflow;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.model.OrderInfo;

/**
 * 等待抢单2100 statecode 2100
 * 
 * @author chowjee
 * @date 2015-3-16
 */
public class WaitSnatchOrder2100 extends OrderState {

	public WaitSnatchOrder2100(OrderStateMachineContext context) {
		super(context);
	}

	/**
	 * 初始化控件
	 */
	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar
				.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);
		leftTabBar.setText("等待司机抢单");

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
		 * 获取当前订单类型：等待司机抢单 2100
		 */
		return OrderInfo.OrderState.NEEDDRIVER2;
	}

	@Override
	public int getNextValue() {
		/**
		 * 下一个订单类型：用户确认 2200
		 */
		return OrderInfo.OrderState.NEEDDRIVER3;
	}

	/**
	 * 获取对话框提示信息
	 */
	@Override
	public String getExecuteDialogMessage() {
		return String.format(mNormalNoticeFormat, "[等待司机抢单]");
	}
}
