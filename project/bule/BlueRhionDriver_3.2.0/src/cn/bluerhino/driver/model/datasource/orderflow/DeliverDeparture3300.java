package cn.bluerhino.driver.model.datasource.orderflow;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.utils.LogUtil;

/**
 * 
 * Describe:当前已经完成订单状态：司机出发
 * 
 * Date:2015-7-17
 * 
 * Author:liuzhouliang
 */
public class DeliverDeparture3300 extends OrderState {

	public DeliverDeparture3300(OrderStateMachineContext machineContext) {
		super(machineContext);
	}

	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar
				.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);
		leftTabBar.setText("到达发货地");

		Button rightTabBar = getRightTab();
		LayoutParams rightTabParams = (LayoutParams) rightTabBar
				.getLayoutParams();
		rightTabParams.weight = 0;
		rightTabBar.setLayoutParams(rightTabParams);

	}

	@Override
	public int getValue() {
		/**
		 * 当前状态为司机出发状态：3300
		 */
		return OrderInfo.OrderState.DELIVERDEPARTURE;
	}

	@Override
	public int getNextValue() {
		/**
		 * 下一个状态为到达发货地状态：4000
		 */
		return OrderInfo.OrderState.REACHSHIPADDRESS;
	}

	@Override
	public String getExecuteDialogMessage() {
		/**
		 * 司机出发状态下，提示窗口信息
		 */
		return String.format(mNormalNoticeFormat, "到达发货地");
	}

	/**
	 * 执行到达发货地
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == getLeftTab().getId()) {
			ApplicationController.getInstance().deliverGoodsLocationInfo = ApplicationController
					.getInstance().getLastLocationInfo();
			LogUtil.d(
					TAG,
					"点击到达发货地的坐标信息"
							+ ApplicationController.getInstance().deliverGoodsLocationInfo
									.toString());
			super.onClick(v);
		}

	}
}
