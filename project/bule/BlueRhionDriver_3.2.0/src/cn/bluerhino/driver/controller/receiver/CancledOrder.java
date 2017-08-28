package cn.bluerhino.driver.controller.receiver;

import cn.bluerhino.driver.BRFLAG;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.MainActivity;
import cn.bluerhino.driver.model.PushInfo;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 *  您有一个订单被取消，请注意查看
 */
public class CancledOrder extends PushState {
	
	public static final String TAG = CancledOrder.class.getSimpleName();

	public CancledOrder(Context context, PushInfo pushInfo, int orderNum) {
        super(context, pushInfo, orderNum);
	}
	
	@Override
	protected boolean isNeedBlock() {
		//如果当前订单中不存在该订单则不提示, 存在则提示
		return super.isNeedBlock();
	}
	
	@Override
	protected boolean isNeedRingtone() {
		return true;
	}
	
	@Override
	protected String getVoiceText() {
		return "您有一个订单被取消，请注意查看";
	}

	@Override
    protected String getMessage() {
	    return String.format(mRes.getString(R.string.jpush_notification_msg_hascancledorder),
		        mPushInfo.getOrderNum());
    }
	
	@Override
	protected PendingIntent getPendingIntent() {
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.setFlags(BRFLAG.PUSH_ACTIVITY_FLAG);
		intent.putExtra(TAG, "");
		PendingIntent contentIndent = PendingIntent.getActivity(mContext, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return contentIndent;
	}

	@Override
	protected Intent getNotifyAction() {
		return null;
	}

}
