package cn.bluerhino.driver.model.datasource.orderflow;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bluerhino.driver.model.OrderInfo;
import com.android.volley.RequestQueue;
import com.bluerhino.library.network.framework.BRJsonRequest;

public class OrderStateMachineContext implements IOrderFlowComponent, IExecute {

	private static final String TAG = OrderStateMachineContext.class
			.getSimpleName();
	private IOrderFlowComponent mComponent;
	private OrderState mOrderState;

	public OrderStateMachineContext(IOrderFlowComponent component) {
		super();
		mComponent = component;
	}

	public void create() {
		buildView();
	}

	/**
	 * 
	 * Describe:终止任务
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	public void destory() {
//		mOrderState.stopAllTimerTask();
	}

	public void buildView() {
		if (mOrderState == null) {
			mOrderState = getOrderState(getOrderInfo().getFlag());
		}
		/**
		 * 初始化各个订单状态下的view状态
		 */
		mOrderState.buildView();
	}

	public OrderState getOrderState() {
		return mOrderState;
	}

	/**
	 * 
	 * Describe:获取各个状态下的订单
	 * 
	 * Date:2015-6-26
	 * 
	 * Author:liuzhouliang
	 */
	public OrderState getOrderState(int value) {
		OrderState orderState = null;
		switch (value) {
		case OrderInfo.OrderState.NEEDDRIVER2:// 2100
			/**
			 * 等待司机抢单
			 */
			orderState = new WaitSnatchOrder2100(this);
			break;
		case OrderInfo.OrderState.NEEDDRIVER3:// 2200
			/**
			 * 抢单提交成功
			 */
			orderState = new WaitSnatchOrder2200(this);
			break;
		case OrderInfo.OrderState.WAITRESPONSE:// 2600
			/**
			 * 等待响应
			 */
			orderState = new WaitResponse2600(this);
			break;
		case OrderInfo.OrderState.WAITSERVICE:// 3000
			/**
			 * 等待服务
			 */
			orderState = new WaitService3000(this);

			break;
		case OrderInfo.OrderState.DELIVERDEPARTURE:// 3300
			/**
			 * 司机出发状态===============根据定位自动处理，判断司机是否已经出发
			 */
			orderState = new DeliverDeparture3300(this);

			break;
		case OrderInfo.OrderState.REACHSHIPADDRESS:// 4000
			/**
			 * 到达发货地
			 */
			orderState = new ReachshipAddress4000(this);
			break;
		case OrderInfo.OrderState.DEPARTURESHIPADDRESS:// 4150
			/**
			 * 从发货地出发=================根据定位自动处理，判断司机是否已经从发货地出发
			 */
			orderState = new DepartureShipAddress4150(this);
			break;
		case OrderInfo.OrderState.REACHPLACEOFRECEIPT:// 4600
			/**
			 * 到达收货地=================根据定位自动处理，判断司机是否已经到达收货地
			 */
			orderState = new ReachPlaceOfReceipt4600(this);
			break;
		case OrderInfo.OrderState.DEPARTURERECEIPT:// 4800
			/**
			 * 从收货地出发
			 */
			orderState = new DepartureReceipt4800(this);
			break;
		case OrderInfo.OrderState.SERVICEFINISH:// 5000
			/**
			 * 服务结束
			 */
			orderState = new ServiceFinish5000(this);
			break;
		case OrderInfo.OrderState.ORDERCANCEL:// 10000
			/**
			 * 订单取消
			 */
			orderState = new CancelOrder10000(this);
			break;
		case OrderInfo.OrderState.ORDERFINISH:// 20000
			/**
			 * 订单结束
			 */
			orderState = new FinishOrder20000(this);
			break;
		default:
			// TODO 返回订单异常状态
			orderState = new ExceptionOrder(this);
			break;
		}
		return orderState;
	}

	public void setOrderState(OrderState orderState) {
		mOrderState = orderState;
	}

	@Override
	public OrderInfo getOrderInfo() {
		return mComponent.getOrderInfo();
	}

	@Override
	public Context getContext() {
		return mComponent.getContext();
	}

	@Override
	public Button getLeftTab() {
		return mComponent.getLeftTab();
	}

	@Override
	public Button getRightTab() {
		return mComponent.getRightTab();
	}

	@Override
	public RequestQueue getRequestQueue() {
		return mComponent.getRequestQueue();
	}

	@Override
	public void execute(BRJsonRequest request) {
		request.setTag(TAG);
		request.setShouldCache(false);
		getRequestQueue().add(request);
	}

	@Override
	public View getWaitTimeView() {
		return mComponent.getWaitTimeView();
	}

	@Override
	public TextView getOrderStateTv() {
		return mComponent.getOrderStateTv();
	}

	@Override
	public LinearLayout getOrderStateParentView() {
		return mComponent.getOrderStateParentView();
	}
}
