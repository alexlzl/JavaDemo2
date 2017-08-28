package cn.bluerhino.driver.model.datasource.orderflow;

import com.bluerhino.library.utils.ToastUtil;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.utils.LogUtil;

/**
 * 
 * Describe:当前已经完成状态：到达发货地，4000，下一个状态：4150
 * 
 * Date:2015-7-17
 * 
 * Author:liuzhouliang
 */
public class ReachshipAddress4000 extends OrderState {
	private static final String MTAG = ReachshipAddress4000.class.getName();
	// 总共的收货地数目
	private int totalDestination;
	// 当前到达第几个发货地
	private int arriveDestination;

	// 标记是否再次出发
	private boolean isStartAgain;

	public ReachshipAddress4000(OrderStateMachineContext machineContext) {
		super(machineContext);
		totalDestination = getOrderInfo().getDeliver().size();
		arriveDestination = getOrderInfo().getArriveCount();
		LogUtil.d(MTAG, "一共收货地：" + totalDestination + "要到达的收货地=== "
				+ arriveDestination);
	}

	@Override
	public void buildView() {
		Button leftTabBar = getLeftTab();
		LayoutParams leftTabParams = (LayoutParams) leftTabBar
				.getLayoutParams();
		leftTabBar.setLayoutParams(leftTabParams);

		Button rightTabBar = getRightTab();
		LinearLayout.LayoutParams rightTabParams = (LinearLayout.LayoutParams) rightTabBar
				.getLayoutParams();
		rightTabBar.setLayoutParams(rightTabParams);
		if (isSingleOrMultipleDestination()) {
			/**
			 * 多个收货地=============================1
			 */
			leftTabParams.weight = 1;
			rightTabParams.weight = 0;
			leftTabBar.setText("到达收货地" + (arriveDestination + 1));

		} else {
			/**
			 * 单个收货地==============================2
			 */
			LogUtil.d(MTAG, "单个收货地");
			leftTabParams.weight = 1;
			leftTabBar.setText("服务完成");
			rightTabParams.weight = 1;
			rightTabBar.setText("再次出发");
		}

	}

	@Override
	public void onClick(View v) {
		/**
		 * 获取当前收货地信息
		 */
		int id = v.getId();
		if (isSingleOrMultipleDestination()) {
			/**
			 * 多个收货地========================================1
			 */
			if (id == getLeftTab().getId()) {
				/**
				 * 已经完成到达发货地状态下：点击到达收货地，执行从发货地出发--到达收货地--从收货地出发
				 */
				ApplicationController.getInstance().isMultiAutomaticTo4600 = true;
				super.onClick(v);
			}
		} else {
			/**
			 * 只有一个收货地，显示服务完成和再次出发===============================2
			 */

			if (id == getLeftTab().getId()) {
				/**
				 * 已经完成到达发货地的状态下：点击服务完成，执行从发货地出发--到达发货地--完成
				 */
				ApplicationController.getInstance().isAutomaticTo5000 = true;
				// 控制回调显示支付对话框
				ApplicationController.getInstance().isNeedShowDialog = true;
				LogUtil.d(MTAG,
						"ApplicationController.getInstance().isAutomaticTo5000 = true;");
				setCancleListener(new CancleClickListener() {

					@Override
					public void onCancleClick() {
						if (ApplicationController.testSwitch) {
							ToastUtil.showToast(mContext, "test");
						}

						ApplicationController.getInstance().isAutomaticTo5000 = false;
					}
				});
				super.onClick(v);

			} else if (id == getRightTab().getId()) {
				/**
				 * 已经完成到达发货地的状态下：点击再次出发，执行从收货地出发4800
				 */
				isStartAgain = true;
				ApplicationController.getInstance().isStartAgainAutomaticTo4600 = true;
				ApplicationController.getInstance().isSingleStartAgain = true;
				setCancleListener(new CancleClickListener() {

					@Override
					public void onCancleClick() {
						if (ApplicationController.testSwitch) {
							ToastUtil.showToast(mContext, "test");
							LogUtil.d(TAG, "----取消4000-----");
						}

						isStartAgain = false;
						ApplicationController.getInstance().isStartAgainAutomaticTo4600 = false;
						ApplicationController.getInstance().isSingleStartAgain = false;
					}
				});
				super.onClick(v);

			}
		}

	}

	@Override
	public int getValue() {
		/**
		 * 当前状态：到达发货地 4000
		 */
		return OrderInfo.OrderState.REACHSHIPADDRESS;
	}

	@Override
	public int getNextValue() {

		return OrderInfo.OrderState.DEPARTURESHIPADDRESS;

	}

	@Override
	public String getExecuteDialogMessage() {
		if (isSingleOrMultipleDestination()) {
			/**
			 * 多个收货地
			 */
			return "你确定到达收货地" + (arriveDestination + 1) + "吗？";
		} else {
			/**
			 * 一个收货地
			 */
			if (isStartAgain) {
				return "你确定再次出发吗？";
			} else {
				return "你确定服务完成吗？";
			}
		}
	}

}
