package com.minsheng.app.module.usercenter;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.ModifyState;
import com.minsheng.app.entity.MyMessageEntity.Infor.MessageObj;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.TimeUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:消息中心详情页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-2下午5:09:44
 * 
 */
public class MyMessageDetail extends BaseActivity {
	private static String TAG = "MyMessageDetail";
	private MessageObj obj;
	private TextView tvTitle, tvContent, tvTime;
	private String title, content, addTime;
	private int messageId;
	private ModifyState modifyStateBean;
	private boolean isRead;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.usercenter_mymessage_detail);
		messageId = getIntent().getIntExtra("messageid", 0);
		isRead = getIntent().getBooleanExtra("isRead", false);
		obj = (MessageObj) getIntent().getSerializableExtra("messagedetail");
		LogUtil.d(TAG, "参数onCreate" + obj.toString() + obj.getMsgContent()
				+ obj.getAddTime() + obj.getMsgTitle() + "messageId=="
				+ messageId + "是否已读" + isRead);
		title = obj.getMsgTitle();
		content = obj.getMsgContent();
		addTime = TimeUtil.changeTimeStempToString(obj.getAddTime());
		LogUtil.d(TAG, "title==" + title + "content=" + content + "addTime="
				+ addTime);
		tvTitle = (TextView) findViewById(R.id.usercenter_mymessage_detail_title);
		tvContent = (TextView) findViewById(R.id.usercenter_mymessage_detail_content);
		tvTime = (TextView) findViewById(R.id.usercenter_mymessage_detail_time);
		tvTitle.setText(title);
		tvContent.setText(content);
		tvTime.setText(addTime);
		changeMessageState();
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		// changeMessageState();
	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "消息中心";
	}

	@Override
	protected String setRightText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int setLeftImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setMiddleImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setRightImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 
	 * 
	 * @describe:更改消息状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-28下午8:25:01
	 * 
	 */
	public void changeMessageState() {
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("msgId", messageId);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.LOGIN_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.UPDATE_MESSAGE_STATE,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						ViewUtil.dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerUpdateMesageState.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ModifyState arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						ViewUtil.dismissRoundProcessDialog();
					}

					@Override
					protected ModifyState parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						ViewUtil.dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							modifyStateBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerUpdateMesageState.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerUpdateMesageState.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return modifyStateBean;
					}
				});
	}

	/**
	 * 处理修改消息返回
	 */
	Handler handlerUpdateMesageState = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (modifyStateBean != null && modifyStateBean.getCode() == 0) {
					LogUtil.d(TAG, "修改状态成功");
					/**
					 * 进入详情页成功，标记未读消息状态
					 */
					if (MsApplication.messageList != null) {
						if (!MsApplication.messageList.contains(messageId)) {
							// if (!isRead) {
							// MsApplication.messageList.add(messageId);
							// }
							switch (obj.getIsRead()) {
							case 0:
								LogUtil.d(TAG, "未读");
								MsApplication.messageList.add(messageId);
								break;
							case 1:
								LogUtil.d(TAG, "已读");
								break;
							}
						}
					}

				} else {
					if (modifyStateBean != null) {
						LogUtil.d(TAG, modifyStateBean.getMsg());

					} else {
						LogUtil.d(TAG, "修改状态失败");
					}
				}
				break;
			case MsConfiguration.FAIL:
				LogUtil.d(TAG, "修改状态失败");
				break;
			}
		}

	};
}
