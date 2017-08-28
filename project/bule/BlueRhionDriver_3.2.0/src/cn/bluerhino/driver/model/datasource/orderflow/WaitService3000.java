package cn.bluerhino.driver.model.datasource.orderflow;

import com.umeng.analytics.MobclickAgent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.utils.LogUtil;

/**
 * 
 * Describe:当前已经完成状态：等待服务 3000
 * 
 * Date:2015-7-17
 * 
 * Author:liuzhouliang
 */
public class WaitService3000 extends OrderState {

	public WaitService3000(OrderStateMachineContext machineContext) {
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
		getWaitTime().setVisibility(View.GONE);
	}

	@Override
	public int getValue() {
		/**
		 * 当前状态等待服务：3000
		 */
		return OrderInfo.OrderState.WAITSERVICE;
	}

	@Override
	public int getNextValue() {
		/**
		 * 下一个状态司机出发:3300
		 */
		return OrderInfo.OrderState.DELIVERDEPARTURE;
	}

	@Override
	public String getExecuteDialogMessage() {
		/**
		 * 弹窗框，提示信息
		 */
		return String.format(mNormalNoticeFormat, "到达发货地");
	}

	/**
	 * 若用户在等待服务状态下，点击到达发货地
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == getLeftTab().getId()) {
			/**
			 * 等待服务状态下，用户点击到达发货地，不进行条件判断，直接执行：司机出发，到达发货地
			 */
			ApplicationController.getInstance().deliverGoodsLocationInfo = ApplicationController
					.getInstance().getLastLocationInfo();
			LogUtil.d(
					TAG,
					"点击到达发货地的坐标信息"
							+ ApplicationController.getInstance().deliverGoodsLocationInfo
									.toString());
			ApplicationController.getInstance().isAutomaticTo4000 = true;
			super.onClick(v);
			MobclickAgent.onEvent(ApplicationController.getInstance(),
					"pageCurrList_btn_come");
		}

	}

}
