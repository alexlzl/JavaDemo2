package cn.bluerhino.driver.controller.receiver;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import cn.bluerhino.driver.BRFLAG;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.MainActivity;
import cn.bluerhino.driver.model.PushInfo;

/**
 * 订单变更 测试: (必定属于自己已抢单)修改订单信息 语音: 您的订单行程已变更，请注意查看 行为: 广播通知跳到当前订单页-->抢单列表页 通知:
 * 您有新的订单,订单号xxx 自定义: 无
 */
public class ModifyedOrder extends PushState {

	public static final String TAG = ModifyedOrder.class.getSimpleName();

	public ModifyedOrder(Context context, PushInfo pushInfo, int orderNum) {
        super(context, pushInfo, orderNum);
    }

	@Override
	protected boolean isNeedBlock() {
		//如果当前订单中不存在该订单则不提示, 存在则提示
		return super.isNeedBlock();
	}

	@Override
	protected String getVoiceText() {
		return "您的订单行程已变更，请注意查看";
	}

	@Override
	protected String getMessage() {
		return String.format(mRes
				.getString(R.string.jpush_notification_msg_hasmodifyedorder),
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
