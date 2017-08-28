package cn.bluerhino.driver.controller.receiver;

import cn.bluerhino.driver.BRFLAG;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.MainActivity;
import cn.bluerhino.driver.model.PushInfo;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 订单改派: 您有一个订单已被改派，请注意查看 
 * 发生原因: 车现在用不了, 用户另有选择
 * 语音: 您有一个订单已被改派，请注意查看
 * 行为: 会在当前订单停留5分钟，之后被回收到历史订单中
 */
public class RearrangedOrder extends PushState {
	
	public static final String TAG = RearrangedOrder.class.getSimpleName();
	
	public RearrangedOrder(Context context, PushInfo pushInfo, int orderNum) {
        super(context, pushInfo, orderNum);
	}
	
	@Override
	protected boolean isNeedRingtone() {
		return true;
	}
	
	/**
	 * 从字面上将'改派'改为'取消'
	 */
	@Override
	protected String getVoiceText() {
		return "您有一个订单被取消，请注意查看";
	}
	
	/**
	 * '改派' 改为 '取消样式'
	 */
	@Override
	protected String getMessage() {
		return String.format(mRes
				.getString(R.string.jpush_notification_msg_hascancledorder),
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
		return new Intent(JPushReceiver.ACTION_REARRANGED_ORDER);
	}

}
