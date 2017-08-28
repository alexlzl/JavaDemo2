package cn.bluerhino.driver.controller.service;

import com.bluerhino.library.service.ForegroundService;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.model.OrderInfo;

/**
 * 遍历订单, 管理订单工作的变动
 */
public class ProcessPushService extends ForegroundService {
	
	public static String ACTION = "action";
	public static String INT_ORDER_NUM = "num";
	public static String STR_ORDER_REASON = "reason";
	public static String OBJ_ORDERINFO = "order";
	
	/**
	 * 增加命令
	 */
	public static final int ACTION_ADD = 0x01;
	
	/**
	 * 修改reason命令
	 */
	public static final int ACTION_UPDATE = 0x02;
	
	/**
	 * 删除订单命令
	 */
	public static final int ACTION_DEL = 0x03;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null)
			return super.onStartCommand(intent, flags, startId);
		Bundle extras = intent.getExtras();
		if(extras == null)
			return super.onStartCommand(intent, flags, startId);
		final int action = extras.getInt(ACTION);
		final int orderNum = extras.getInt(INT_ORDER_NUM);
		final String reason = extras.getString(STR_ORDER_REASON);
		Runnable runnable = null;
		switch (action) {
		case ACTION_ADD:
			final OrderInfo orderInfo = extras.getParcelable(OBJ_ORDERINFO);
			if(orderInfo == null)
				return super.onStartCommand(intent, flags, startId);
			runnable = new Runnable() {
				@Override
				public void run() {
					ApplicationController.getInstance().addCountdownOrderInfo(orderInfo);
				}
			};
			break;
		case ACTION_UPDATE:
			runnable = new Runnable() {
				@Override
				public void run() {
					ApplicationController.getInstance().updateReason(orderNum, reason);	
				}
			};
			
			break;
		case ACTION_DEL:
			runnable = new Runnable() {
				@Override
				public void run() {
					//暂未使用
					//ApplicationController.getInstance().updateReason(orderNum, reason);	
				}
			};
			break;
		default:
			break;
		}
		if(runnable != null){
			new Thread(runnable).start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	

}
