package cn.bluerhino.driver.utils;

/**
 * Describe:常量管理
 * 
 * Date:2015-7-13
 * 
 * Author:liuzhouliang
 */
public class ConstantsManager {
	public static final String PUSH_MESSAGE_DIALOG_CONTENT = "push_message_dialog_content";
	public static final String PUSH_MESSAGE_DIALOG_ACTION_TYPE = "push_message_dialog_action_type";
	public static final String PUSH_MESSAGE_DIALOG_ACTION_URL = "push_message_dialog_action_url";
	public static final String FROM_WHERE = "from_where";
	public static final String ISNEEDPLAYVOIVE = "isNeedPlayVoice";
	// 广告出现间隔时间
	public static final int ADVERTISEMENT_SHOW_INTERVAL = 1000 * 60 * 60;

	public static final String IS_FORCE_UPDATE_KEY = "is_force_update";
	/**
	 * 处理推送跳转页面
	 */
	// 抢单页面
	public static final String COMPETITIONLIST = "page=competitionList";
	// 当前页面
	public static final String CURRENTLIST = "page=currentList";
	// 个人信息页面
	public static final String PERSONFINFO = "page=personInfo";
	// 历史订单
	public static final String HISTORYLIST = "page=historyList";
	// 账户信息页面
	public static final String MYACCOUNT = "page=myAccount";
	// 账号详情页面
	public static final String ACCOUNTDETAIL = "page=accountDetail";
	// 客户评分
	public static final String COMMENTINFO = "page=commentInfo";
	// 推荐客户页面
	public static final String INVITE = "page=invite";
	// 抢单页面详情
	public static final String COMPETITIONDETAIL = "page=competitionDetail";
	// 服务订单详情
	public static final String SERVICEORDERDETAIL = "page=serviceOrderDetail";
	// 升级页面
	public static final String UPDATE_ACTION = "actionNum=1";
	/**
	 * 定义上传错误类型
	 */
	// 推送异常信息
	public static final int PUSH_ERROR = 1000;
	// 语音异常信息
	public static final int TTS_ERROR = 1001;
	// 导航异常信息
	public static final int NAVIGATION_ERROR = 1002;
	// 定位异常信息
	public static final int LOCATION_ERROR = 1003;
}
