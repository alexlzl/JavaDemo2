package cn.bluerhino.driver.controller.receiver;

import android.content.Context;
import android.text.TextUtils;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.PushInfo;
import cn.bluerhino.driver.utils.LogUtil;

/**
 * 
 * Describe:处理推送过来 的消息类型
 * 
 * Date:2015-7-8
 * 
 * Author:liuzhouliang
 */
public class PushStateContext {

	/** 指派订单,需要响应 */
	public static final String HASNEWORDER = "100001";

	/** 订单发生变化 */
	public static final String HASMODIFYEDORDER = "100002";

	/** 抢单成功 */
	public static final String SNATCHORDERSUCCEED = "100003";

	/** 抢单失败 */
	public static final String SNATCHORDERFAILURE = "100004";

	/** 订单被改派 */
	public static final String HASREARRANGEDORDER = "200001";

	/** 订单被取消 */
	public static final String HASCANCLEDORDER = "200002";

	/** 需要抢的新订单 */
	public static final String HASAVAILABLEORDER = "300000";
	/** 系统推送提示信息 */
	public static final String SYSTEM_ALERT = "400000";
	// 二代协议，页面跳转消息
	public static final String PUSHINFOR_PAGE_MESSAGE = "500000";

	public static final PushState getPushState(Context context, PushInfo pushInfo,
			OrderInfo orderInfo) {
		//过滤掉无效情况: 只有在是订单类型的推送, 获得推送信息的订单号
		int orderNum = 0;
		try {
			orderNum = Integer.parseInt(pushInfo.getOrderNum());
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		PushState pushState = null;
		String stateAction = pushInfo.getAction();
		if (TextUtils.equals(stateAction, HASNEWORDER)) {
			// 指派到当前订单
			pushState = new NewOrder(context, pushInfo, orderNum);

		} else if (TextUtils.equals(stateAction, HASMODIFYEDORDER)) {
			LogUtil.d("------", "订单发生变化--------");
			pushState = new ModifyedOrder(context, pushInfo, orderNum);

		} else if (TextUtils.equals(stateAction, SNATCHORDERSUCCEED)) {

			pushState = new SnatchorderSucceed(context, pushInfo, orderNum, orderInfo);

		} else if (TextUtils.equals(stateAction, SNATCHORDERFAILURE)) {

			pushState = new SnatchorderFailure(context, pushInfo, orderNum);
			LogUtil.d("-----","抢单失败");

		} else if (TextUtils.equals(stateAction, HASREARRANGEDORDER)) {

			pushState = new RearrangedOrder(context, pushInfo, orderNum);
			LogUtil.d("------", "改派订单--------");

		} else if (TextUtils.equals(stateAction, HASCANCLEDORDER)) {

			pushState = new CancledOrder(context, pushInfo, orderNum);
			LogUtil.d("------", "取消订单--------");

		} else if (TextUtils.equals(stateAction, HASAVAILABLEORDER)) {
			// 需要抢的单
			pushState = new AvailableOrder(context, pushInfo, orderNum, orderInfo);
		}

		return pushState;
	}

}
