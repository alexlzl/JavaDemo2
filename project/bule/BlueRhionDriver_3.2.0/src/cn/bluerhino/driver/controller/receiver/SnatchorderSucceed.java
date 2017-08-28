package cn.bluerhino.driver.controller.receiver;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.WindowManager;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRFLAG;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.MainActivity;
import cn.bluerhino.driver.controller.service.ProcessPushService;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.PushInfo;
import cn.bluerhino.driver.utils.AppRunningInfor;

/**
 * 抢单成功
 * 前提 能正确请求到orderinfo信息
 * 1.语音 :抢单成功，请前往当前订单查看(抢单成功（实时／预约，预约包括半日租和日租）+据您X公里+预计XX元)
 * 2.通知: 需要 , 文本"抢单成功,订单号"
 * 3.行为:  在前台显示时广播通知跳到当前订单页
 * 4.自定义: 抢单成功后，系统会出现置顶弹窗，提醒司机联系用户
 */
public class SnatchorderSucceed extends PushState {
	
	public static final String TAG = SnatchorderSucceed.class.getSimpleName();
	
	public SnatchorderSucceed(Context context, PushInfo pushInfo, int orderNum,  OrderInfo orderInfo) {
	    super(context, pushInfo, orderNum);
    }
	
	@Override
	protected boolean isNeedBlock() {
		//订单号中是否成功被响应(抢单成功 或 失败)表示该订单已经播报,则不用再次播报
		return ApplicationController.getInstance().isOrderNumResponse(super.mOrderNum);
	}
	
	@Override
	protected boolean isNeedRingtone() {
		return true;
	}
	
	@Override
	protected String getVoiceText() {
		return super.mRes.getString(R.string.jpush_notification_voice_hassnatchordersucceed);
	}

	@Override
    protected String getMessage() {
	    return String.format(mRes.getString(R.string.jpush_notification_msg_hassnatchordersucceed),
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
		if (AppRunningInfor.isActivityRunningForeground(mContext,
				"cn.bluerhino.driver.controller.activity.MainActivity")) {
			return new Intent(JPushReceiver.ACTION_CURLIST_UPDATE);
		}
		return null;
    }
	
	@Override
	protected void customBehavior() {
		this.updateOrderInfo(); 
		this.showContactDialog(); 
	}
	
	/**
	 * 抢单成功后，系统会出现置顶弹窗(适用全局)。，提醒司机联系用户
	 */
	private void showContactDialog(){
		if(mContext == null){
			return;
		}
		//如果运行在后台,先跳到Mainactivity,再等待开启弹窗
		if(!AppRunningInfor.isRunningForeground(mContext)){
			this.JumpToPage();
		}
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		alertDialog.setTitle(R.string.jpush_dialog_hassnatchordersucceed_title).setMessage(R.string.jpush_dialog_hassnatchordersucceed_msg);
		//当手机号没填写时,不显示'联系用户'按钮, 但仍弹窗
		final String contactPhone = mPushInfo.getPhone();
		if(!contactPhone.equals("")){
			alertDialog.setPositiveButton(R.string.jpush_dialog_hassnatchordersucceed_posi, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactPhone));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
					mContext.startActivity(intent);
				}
			});
			alertDialog.setNegativeButton(R.string.jpush_dialog_hassnatchordersucceed_contact_later, null);
		}else{
			alertDialog.setNegativeButton(R.string.jpush_dialog_hassnatchordersucceed_i_know, null);
		}
		AlertDialog alert = alertDialog.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.show();
	}
	
	/**
	 * 将订单状态改为'抢单成功'
	 */
	private void updateOrderInfo(){
		Intent intent = new Intent(mContext, ProcessPushService.class);
		intent.putExtra(ProcessPushService.ACTION, ProcessPushService.ACTION_UPDATE);
		intent.putExtra(ProcessPushService.INT_ORDER_NUM, super.mOrderNum);
		intent.putExtra(ProcessPushService.STR_ORDER_REASON, mRes.getString(R.string.wait_order_success));
		mContext.startService(intent);
	}
	
	/**
	 * 跳转到对应页面
	 */
	private void JumpToPage(){
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(TAG, "");
		mContext.startActivity(intent);
	}
}
