package cn.bluerhino.driver.model.datasource.orderflow;

import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.utils.LogUtil;

/**
 * 
 * Describe:当前已经完成状态：从收货地出发 4800 多个收货地
 * 
 * Date:2015-7-17
 * 
 * Author:liuzhouliang
 */
public class DepartureReceipt4800 extends OrderState {
	private static final String MTAG = DepartureReceipt4800.class.getName();
	// 总共的收货地数目
	private int totalDestination;
	// 当前到达第几个发货地
	private int arriveDestination;

	public DepartureReceipt4800(OrderStateMachineContext machineContext) {
		super(machineContext);
		totalDestination = getOrderInfo().getDeliver().size();
		arriveDestination = getOrderInfo().getArriveCount();
		LogUtil.d(MTAG, "一共收货地：" + totalDestination + "已到达收货地=== "
				+ (arriveDestination));
	}

	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar
				.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);
		leftTabBar.setText("到达收货地" + (arriveDestination+1));

		Button rightTabBar = getRightTab();
		LayoutParams rightTabParams = (LayoutParams) rightTabBar
				.getLayoutParams();
		rightTabParams.weight = 0;
		rightTabBar.setLayoutParams(rightTabParams);

	}

	@Override
	public int getValue() {
		/**
		 * 当前已经完成状态：从收货地出发
		 */
		return OrderInfo.OrderState.DEPARTURERECEIPT;
	}

	@Override
	public int getNextValue() {
		/**
		 * 下一个完成状态：到达收货地
		 */
		return OrderInfo.OrderState.REACHPLACEOFRECEIPT;
	}

	@Override
	public String getExecuteDialogMessage() {
		return String
				.format(mNormalNoticeFormat, "到达收货地" + (arriveDestination+1));
	}

	@Override
	protected String getUrl() {
		return super.getUrl();

	}

}
