package cn.bluerhino.driver.controller.receiver;

import android.content.Context;
import android.content.Intent;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.service.ProcessPushService;
import cn.bluerhino.driver.model.PushInfo;

/**
 * 抢单失败
 * 1.通知 不发
 * 2.语音 不发
 * 3.如果存在更改抢单页状态信息
 * 4.该条订单如果存在的话仍然停留
 * 发生情况一: 推送的AvailableOrder推送后未抢导致的超时情况
 */
public class SnatchorderFailure extends PushState {
	
	public static final String TAG = SnatchorderFailure.class.getSimpleName();
	

	public SnatchorderFailure(Context context, PushInfo pushInfo, int orderNum) {
        super(context, pushInfo, orderNum);
    }
	
	/**
	 * 是否需要阻止处理消息:如果该订单号对应订单已经处理过则不用播报
	 */
	@Override
	protected boolean isNeedBlock() {
		return ApplicationController.getInstance().isFailureOrderNeedResopnse(super.mOrderNum);
	}
	
	@Override
	protected boolean isNeedVoice() {
		return false;
	}

	@Override
	protected boolean isNeedNotification() {
		return false;
	}
	
	@Override
	protected Intent getNotifyAction() {
		return null;
	}
	
	@Override
	protected void customBehavior() {
		this.updateOrderInfo();
	}
	
	/**
	 * 抢单失败获取pushInfo的msg信息(包含订单已取消, 订单已被抢),现一律改为抢单失败
	 */
	private void updateOrderInfo(){
		Intent intent = new Intent(mContext, ProcessPushService.class);
		intent.putExtra(ProcessPushService.ACTION, ProcessPushService.ACTION_UPDATE);
		intent.putExtra(ProcessPushService.INT_ORDER_NUM, super.mOrderNum);
		intent.putExtra(ProcessPushService.STR_ORDER_REASON, mRes.getString(R.string.wait_order_failure));
		mContext.startService(intent);
	}
}
