package com.minsheng.app.module.usercenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.MyMessageEntity;
import com.minsheng.app.entity.MyMessageEntity.Infor.MessageObj;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.NetWorkState;
import com.minsheng.app.view.MsRefreshListView;
import com.minsheng.app.view.MsToast;
import com.minsheng.app.view.MsRefreshListView.OnMoreListener;
import com.minsheng.app.view.MsRefreshListView.OnRefreshListener;
import com.minsheng.wash.R;

/**
 * 
 * @describe:消息中心
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-2下午4:03:27
 * 
 */
public class MyMessage extends BaseActivity implements OnMoreListener,
		OnRefreshListener, OnItemClickListener {
	private static final String TAG = "MyMessage";
	private MsRefreshListView lv;
	private MyMessageAdapter adapter;
	private int pageNum = 0;
	private boolean isRefresh;
	private boolean isLoadMore;
	// 标记是否分页数据加载完成
	private boolean isLoadOver;
	private int startCount;
	private String pageToken;
	private MyMessageEntity messageObj;
	private List<MessageObj> messageObjList;
	private static MessageStateChange mlistener;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.usercenter_mymessage);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		lv = (MsRefreshListView) findViewById(R.id.usercenter_mymessage_lv);

	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		showRoundProcessDialog();
		RequestParams params = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		if (MsApplication.isLogin()) {
			map.put("loginToken", MsApplication.getLoginToken());
		}
		map.put("pageSize", MsConfiguration.PAGE_SIZE);
		if (pageNum == 0) {
			/**
			 * 加载第一页
			 */
			map.put("pageType", 0);
			map.put("startCount", 0);
			map.put("pageToken", pageToken);
			LogUtil.d(TAG, "页数==" + pageNum);
		} else {
			/**
			 * 加载分页
			 */
			map.put("pageType", 1);
			map.put("startCount", startCount);
			map.put("pageToken", pageToken);
			LogUtil.d(TAG, "页数==" + pageNum + "startCount==" + startCount);
		}
		params = MsApplication.getRequestParams(map, params,
				MsConfiguration.LOGIN_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, params,
				MsRequestConfiguration.MY_MESSAGE,
				new BaseJsonHttpResponseHandler<MyMessageEntity>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, MyMessageEntity arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();

						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerMessage.sendMessage(message);

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							MyMessageEntity arg3) {

					}

					@Override
					protected MyMessageEntity parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "parseResponse==" + arg0);
						dismissRoundProcessDialog();
						/**
						 * 更新刷新状态或者分页加载状态
						 */
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							messageObj = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									MyMessageEntity.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerMessage.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerMessage.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return messageObj;
					}

				});
	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub
		setReloadContent(R.layout.usercenter_mymessage);
		getNetData();
	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		lv.setonLoadListener(this);
		lv.setonRefreshListener(this);
	
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
	 * 处理获取消息返回
	 */
	Handler handlerMessage = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case MsConfiguration.SUCCESS:
				/**
				 * 数据加载成功
				 */
				setViewData();
				break;
			case MsConfiguration.FAIL:
				/**
				 * 数据加载失败
				 */
				setFailView();
				break;
			case MsConfiguration.OVER:
				/**
				 * 没有网络
				 */
				lv.onLoadComplete();
				lv.onRefreshComplete();
				break;

			}
		}

	};

	private void setViewData() {
		// TODO Auto-generated method stub

		if (pageNum == 0) {
			/**
			 * 绑定第一页数据：1，初始化；2，下拉刷新，需要处理空页面
			 */
			if (messageObj != null && messageObj.getCode() == 0) {
				/**
				 * 返回成功===================
				 */
				if (messageObj.getInfo() != null
						&& messageObj.getInfo().getDeliverMsgList() != null
						&& messageObj.getInfo().getDeliverMsgList().size() > 0) {

					/**
					 * 列表数据不为空
					 */
					pageToken = messageObj.getInfo().getPageToken();
					startCount = messageObj.getInfo().getEndCount() + 1;
					messageObjList = messageObj.getInfo().getDeliverMsgList();
					adapter = new MyMessageAdapter(messageObjList, baseContext);
					lv.setAdapter(adapter);
					lv.setOnItemClickListener(this);

					LogUtil.d(TAG, "页数" + pageNum + "startCount==" + startCount
							+ "数据长度" + messageObjList.size());
				} else {

					/**
					 * 列表数据为空=====================
					 */
					if (!isRefresh) {
						setNullView();
					} else {
						MsToast.makeText(baseContext, "加载数据失败").show();
					}
				}
			} else {
				/**
				 * 返回失败========================
				 */
				if (!isRefresh) {
					/**
					 * 如果是在初始化显示加载失败页面
					 */
					showLoadFailView();
				}
				if (messageObjList != null) {
					MsToast.makeText(baseContext, messageObj.getMsg()).show();
				} else {
					MsToast.makeText(baseContext, "返回失败").show();
				}

			}
			if (isRefresh) {
				/**
				 * 如果是刷新数据，重置标记
				 */
				lv.onRefreshComplete();
				isRefresh = false;
			}
		} else {
			/**
			 * 绑定分页数据========================
			 */
			if (messageObj != null && messageObj.getCode() == 0) {
				/**
				 * 有分页数据
				 */
				if (messageObj.getInfo() != null
						&& messageObj.getInfo().getDeliverMsgList() != null
						&& messageObj.getInfo().getDeliverMsgList().size() > 0) {

					isLoadOver = false;
					startCount = messageObj.getInfo().getEndCount() + 1;
					List<MessageObj> pageList = messageObj.getInfo()
							.getDeliverMsgList();
					if (messageObjList != null) {
						messageObjList.addAll(pageList);
					}
					if (adapter != null) {
						adapter.setNewData(messageObjList);
					}

					LogUtil.d(TAG, "页数" + pageNum + "startCount==" + startCount
							+ "总数据长度" + messageObjList.size());
				} else {

					/**
					 * 没有分页数据
					 */
					// 标记为分页加载完成
					isLoadOver = true;
					lv.onLoadComplete();
					LogUtil.d(TAG, "分页数据加载完成");
				}
			} else {

				/**
				 * 返回失败========================
				 */
				if (messageObjList != null) {
					MsToast.makeText(baseContext, messageObj.getMsg()).show();
				} else {
					MsToast.makeText(baseContext, "返回失败").show();
				}

			}
			/**
			 * 标记如果是分页加载，重置标记
			 */
			if (isLoadMore) {
				lv.onLoadComplete();
				isLoadMore = false;
			}

		}

	}

	public void setNullView() {
		/**
		 * 正常请求
		 */
		if (isRefresh) {
			/**
			 * 刷新状态，不显示空页面
			 */
			isRefresh = false;
			lv.onRefreshComplete();
			MsToast.makeText(baseContext, "暂无更新").show();
			return;
		}
		if (isLoadMore) {
			/**
			 * 分页加载状态，不显示空页面
			 */
			isLoadMore = false;
			lv.onLoadComplete();
			return;
		}

		else {
			showNoDataView("目前还没有内容信息");
			return;
		}
	}

	public void setFailView() {
		if (isRefresh) {
			/*
			 * 下拉刷新失败，不显示失败页面
			 */
			MsToast.makeText(baseContext, "刷新失败").show();
			isRefresh = false;
			lv.onRefreshComplete();
			return;
		}
		if (isLoadMore) {
			/**
			 * 分页加载失败
			 */
			MsToast.makeText(baseContext, "分页加载失败").show();
			isLoadMore = false;
			lv.onLoadComplete();
			return;
		} else {
			/**
			 * 初始化失败
			 */
			showLoadFailView();
			return;
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!NetWorkState.isNetWorkConnection(baseContext)) {
			/**
			 * 无网络连接状态
			 */
			MsToast.makeText(baseContext, "请检查网络").show();
			Message message = Message.obtain();
			message.what = MsConfiguration.OVER;
			handlerMessage.sendMessage(message);
		} else {
			pageNum = 0;
			isLoadOver = false;
			isRefresh = true;
			getNetData();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (isLoadOver) {
			/**
			 * 分页加载完成
			 */
			Message message = Message.obtain();
			message.what = MsConfiguration.OVER;
			handlerMessage.sendMessage(message);
			if (!NetWorkState.isNetWorkConnection(baseContext)) {
				/**
				 * 无网络连接状态
				 */
				MsToast.makeText(baseContext, "请检查网络").show();

			} else {
				MsToast.makeText(baseContext, "没有更多数据了!").show();

			}
		} else {
			/**
			 * 分页未加载完成
			 */
			if (!NetWorkState.isNetWorkConnection(baseContext)) {
				/**
				 * 无网络连接状态
				 */
				MsToast.makeText(baseContext, "请检查网络").show();
				Message message = Message.obtain();
				message.what = MsConfiguration.OVER;
				handlerMessage.sendMessage(message);
			} else {
				pageNum++;
				isLoadMore = true;
				getNetData();
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
//		setResult(MsConfiguration.HOME_PAGE_TO_MESSAGE);
		mlistener.messageChange();

	}

	public interface MessageStateChange {
		public void messageChange();
	}

	public static  void setMessageChangeListener(MessageStateChange listener) {
		mlistener = listener;
	}
}
