package cn.bluerhino.driver.controller.receiver;


import android.content.Context;
import android.content.Intent;
import cn.bluerhino.driver.controller.activity.MainActivity;
import cn.bluerhino.driver.controller.service.ProcessPushService;
import cn.bluerhino.driver.helper.OrderInfoHelper;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.PushInfo;
import cn.bluerhino.driver.utils.AppRunningInfor;

/**
 * 新订单(需要抢单)
 * 1.语音:实时／预约，预约包括半日租和日租）+据您X公里+预计XX元
 * 2.通知:不发
 * 3.跳转到抢单页
 * 4.add该条订单信息
 */
public class AvailableOrder extends PushState {
	
	private OrderInfo mOrderInfo;
	
	public AvailableOrder(Context context, PushInfo pushInfo, int orderNum,  OrderInfo orderInfo) {
	    super(context, pushInfo, orderNum);
		this.mOrderInfo = orderInfo;
	}
	
	@Override
	protected boolean isNeedRingtone() {
		return true;
	}
	
	@Override
	protected String getVoiceText() {
		return OrderInfoHelper.getOrderDesc(mOrderInfo);
	}
	
	@Override
	protected boolean isNeedNotification() {
		return false;
	}
	
	/**
	 * 1.当用户处于“历史订单”、“个人信息页面”、“我的订单”页面时，来新订单时，会自动跳转到“抢单列表”页面
	 * 2.其他页面: 包括当用户处于wap页面浏览时，不自动跳转到抢单列表页面
	 */
	@Override
    protected Intent getNotifyAction() {
		//弹窗并切换到对应页面
		if(this.isNeedPopup()){
			this.JumpToPage();
			return new Intent(JPushReceiver.ACTION_WAITLIST_UPDATE);
		}
		return null;
    }
	
	/**
	 * 插入一条订单数据
	 */
	@Override
	protected void customBehavior() {
		Intent intent = new Intent(mContext, ProcessPushService.class);
		intent.putExtra(ProcessPushService.ACTION, ProcessPushService.ACTION_ADD);
		intent.putExtra(ProcessPushService.OBJ_ORDERINFO, mOrderInfo);
		mContext.startService(intent);
	}
	
	/**
	 * 是否需要弹窗
	 * @return
	 */
	private boolean isNeedPopup(){
		//应用运行在前台
		if(AppRunningInfor.isRunningForeground(mContext)){
			//主页
			if (AppRunningInfor.isActivityRunningForeground(mContext,
					"cn.bluerhino.driver.controller.activity.MainActivity") ){
				return true;
			}
			//历史订单页面
			else if (AppRunningInfor.isActivityRunningForeground(mContext,
					"cn.bluerhino.driver.controller.activity.HistoryOrderActivity") ){
				return true;
			}
			//个人信息页面
			else if (AppRunningInfor.isActivityRunningForeground(mContext,
					"cn.bluerhino.driver.controller.activity.MyInfoActivity") ){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	/**
	 * 跳转到对应页面
	 */
	private void JumpToPage(){
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}
