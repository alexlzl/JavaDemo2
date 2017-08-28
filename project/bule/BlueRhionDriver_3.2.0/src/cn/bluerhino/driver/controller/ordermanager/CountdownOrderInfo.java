package cn.bluerhino.driver.controller.ordermanager;

import cn.bluerhino.driver.model.OrderInfo;

public class CountdownOrderInfo extends OrderInfo {
	/**
	 * 当前倒计时剩余剩余秒数
	 */
	private int mSec = 60;
	
	/**
	 * 是否成功抢过该单
	 */
	private boolean mIsClicked = false;
	
	/**
	 * 该订单抢单后是否有回应(抢单成功 or 抢单失败)
	 */
	private boolean mIsResponse = false;
	
	public CountdownOrderInfo(OrderInfo orderInfo) {
		super(orderInfo);
	}

	public int getSecond() {
		return mSec;
	}

	public void setSecond(int second) {
		this.mSec = second;
	}

	public boolean isClicked() {
		return mIsClicked;
	}

	public void setClicked(boolean isClicked) {
		this.mIsClicked = isClicked;
	}

	public boolean isResponse() {
		return this.mIsResponse;
	}

	public void setResponse(boolean isResponse) {
		this.mIsResponse = isResponse;
	}
	
	/**
	 * 判断是否是抢过且未响应的订单
	 */
	public boolean isNeedNotify() {
		if(this.isClicked()){
			return ! this.mIsResponse;
		}
		return false;
	}

	@Override
	public String toString() {
		return super.toString() + "CountdownOrderInfo [mSec=" + mSec + ", mIsClicked="
				+ mIsClicked + ", mIsResponse=" + mIsResponse + "]";
	}
}
