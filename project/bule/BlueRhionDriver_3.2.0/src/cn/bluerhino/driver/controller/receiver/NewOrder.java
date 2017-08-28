package cn.bluerhino.driver.controller.receiver;

import android.content.Context;
import android.content.Intent;
import cn.bluerhino.driver.model.PushInfo;
import cn.bluerhino.driver.utils.AppRunningInfor;

/**
 * 订单指派：新订单时无推送通知
 * 1.语音 系统为您分配了一个新订单，请注意响应(原语音:  来订单啦,赶紧相应啊, 再不响应就要被扣钱了    )
 * 2.通知 不发
 * 3.跳转到当前订单页
 */
public class NewOrder extends PushState {
	public static final String TAG = NewOrder.class.getSimpleName();
	
    public NewOrder(Context context, PushInfo pushInfo, int orderNum) {
        super(context, pushInfo, orderNum);
    }
    
    @Override
    protected boolean isNeedRingtone() {
    	return true;
    }

	@Override
	protected String getVoiceText() {
		return "系统为您分配了一个新订单，请注意响应";
	}
	
	@Override
	protected boolean isNeedNotification() {
		return false;
	}
    
	@Override
    protected Intent getNotifyAction() {
		if (AppRunningInfor.isActivityRunningForeground(mContext,
				"cn.bluerhino.driver.controller.activity.MainActivity")) {
			return new Intent(JPushReceiver.ACTION_CURLIST_UPDATE);
		}
		return null;
    }

}
