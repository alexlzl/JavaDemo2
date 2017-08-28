package com.minsheng.app.module.usercenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.CourierEvaluate;
import com.minsheng.app.entity.CourierEvaluate.Infor.EvaluateDetail;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.NetWorkState;
import com.minsheng.app.view.MsRefreshListView;
import com.minsheng.app.view.MsToast;
import com.minsheng.app.view.MsRefreshListView.OnMoreListener;
import com.minsheng.app.view.MsRefreshListView.OnRefreshListener;
import com.minsheng.wash.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;

/**
 * 
 * 
 * @describe:我的评价页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-13下午3:51:34
 * 
 */
public class MyUserEvaluation extends BaseActivity implements OnMoreListener,
		OnRefreshListener {
	private static final String TAG = "AppointmentOrder";
	private StandardArrayAdapter arrayAdapter;

	private MyUserEvaluationAdapter evaluationAdapter;
	private int pageNum = 0;
	private boolean isRefresh;
	private boolean isLoadMore;
	// 标记是否分页数据加载完成
	private boolean isLoadOver;
	private int startCount;
	private String pageToken;
	private CourierEvaluate evaluateEntity;
	private MsRefreshListView listview;
	private List<EvaluateDetail> evaluateDetailList;
	MyUserEvaluationBean[] exampleArray = {
			new MyUserEvaluationBean("123456", "一月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "一月", "alex", "ͦ不错"),
			new MyUserEvaluationBean("123456", "一月", "alex", "ͦ不错"),
			new MyUserEvaluationBean("123456", "二月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "二月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "二月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "二月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "三月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "三月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "三月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "三月", "alex", "不错"),
			new MyUserEvaluationBean("123456123456", "三月", "alex", "不错"),
			new MyUserEvaluationBean("123456123456", "三月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "四月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "四月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "四月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "四月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "四月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "五月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "五月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "五月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "六月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "七月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "八月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "九月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "十月", "alex", "不错"),
			new MyUserEvaluationBean("123456", "十一月", "alex", "不错"), };

	public class StandardArrayAdapter extends
			ArrayAdapter<MyUserEvaluationBean> {

		final MyUserEvaluationBean[] items;

		public StandardArrayAdapter(final Context context,
				final int textViewResourceId, final MyUserEvaluationBean[] items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

	}

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.my_userevaluation);
		// arrayAdapter = new StandardArrayAdapter(this,
		// R.id.my_userevaluation_listitem_ordercontent, exampleArray);
		evaluationAdapter = new MyUserEvaluationAdapter(getLayoutInflater(),
				arrayAdapter);
		listview = (MsRefreshListView) findViewById(R.id.my_userevaluation_lv);
		listview.setAdapter(evaluationAdapter);
		listview.setOnScrollListener(evaluationAdapter);

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
		showRoundProcessDialog();
		RequestParams params = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		if (MsApplication.isLogin()) {
			map.put("loginToken", MsApplication.getLoginToken());
		}
		map.put("pageSize", MsConfiguration.PAGE_SIZE);
		map.put("queryType", MsConfiguration.APPOINT_LIST);
		map.put("outerType", 2);
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
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, params,
				MsRequestConfiguration.COURIER_EVALUATE,
				new BaseJsonHttpResponseHandler<CourierEvaluate>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, CourierEvaluate arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();

						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerOrderList.sendMessage(message);

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							CourierEvaluate arg3) {

					}

					@Override
					protected CourierEvaluate parseResponse(String arg0,
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
							evaluateEntity = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									CourierEvaluate.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerOrderList.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerOrderList.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return evaluateEntity;
					}

				});
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
		return "我的评价";
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

	Handler handlerOrderList = new Handler() {

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
				// init();
				break;
			case MsConfiguration.FAIL:
				/**
				 * 数据加载失败
				 */
				setFailView();
				break;
			case MsConfiguration.OVER:

				listview.onLoadComplete();
				listview.onRefreshComplete();
				break;

			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:绑定数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-18下午4:40:15
	 * 
	 */
	private void setViewData() {
		// TODO Auto-generated method stub

		if (pageNum == 0) {
			/**
			 * 绑定第一页数据：1,初始化加载，需要处理空页面;2,下拉刷新，不显示空页面
			 */
			if (evaluateEntity != null && evaluateEntity.getCode() == 0) {
				/**
				 * 返回成功===================
				 */
				if (evaluateEntity.getInfo() != null
						&& evaluateEntity.getInfo().getEvaluateList() != null
						&& evaluateEntity.getInfo().getEvaluateList().size() > 0) {

					/**
					 * 列表数据不为空=======================
					 */
					pageToken = evaluateEntity.getInfo().getPageToken();
					startCount = evaluateEntity.getInfo().getEndCount();
					evaluateDetailList = evaluateEntity.getInfo()
							.getEvaluateList();
					evaluationAdapter = new MyUserEvaluationAdapter(
							getLayoutInflater(), arrayAdapter);
					listview.setAdapter(evaluationAdapter);

					LogUtil.d(TAG, "页数" + pageNum + "startCount==" + startCount
							+ "数据长度" + evaluateDetailList.size());
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

				if (evaluateEntity != null) {
					MsToast.makeText(baseContext, evaluateEntity.getMsg())
							.show();
				} else {
					MsToast.makeText(baseContext, "加载数据失败").show();
				}

			}
			if (isRefresh) {
				/**
				 * 如果是刷新数据，重置标记
				 */
				listview.onRefreshComplete();
				isRefresh = false;
			}
		} else {
			/**
			 * 绑定分页数据========================
			 */
			if (evaluateEntity != null && evaluateEntity.getCode() == 0) {
				/**
				 * 处理分页数据返回成功====================
				 */

				if (evaluateEntity.getInfo() != null
						&& evaluateEntity.getInfo().getEvaluateList() != null
						&& evaluateEntity.getInfo().getEvaluateList().size() > 0) {
					/**
					 * 分页数据不为空
					 */
					isLoadOver = false;
					startCount = evaluateEntity.getInfo().getEndCount();
					List<EvaluateDetail> pageList = evaluateEntity.getInfo()
							.getEvaluateList();
					if (pageList != null) {
						evaluateDetailList.addAll(pageList);
					}
					// if (adapter != null) {
					// adapter.setNewData(orderList);
					// }

					LogUtil.d(TAG, "页数" + pageNum + "startCount==" + startCount
							+ "总数据长度" + evaluateDetailList.size());
				} else {

					/**
					 * 没有分页数据
					 */
					// 标记为分页加载完成
					isLoadOver = true;
					listview.onLoadComplete();
					LogUtil.d(TAG, "分页数据加载完成");
				}
			} else {

				/**
				 * 返回失败========================
				 */
				if (evaluateEntity != null) {
					MsToast.makeText(baseContext, evaluateEntity.getMsg())
							.show();
				} else {
					MsToast.makeText(baseContext, "返回失败").show();
				}

			}
			/**
			 * 标记如果是分页加载，重置标记
			 */
			if (isLoadMore) {
				listview.onLoadComplete();
				isLoadMore = false;
			}

		}

	}

	/**
	 * 
	 * 
	 * @describe:设置空页面
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-18下午4:55:17
	 * 
	 */
	public void setNullView() {
		/**
		 * 正常请求
		 */
		if (isRefresh) {
			/**
			 * 刷新状态，不显示空页面
			 */
			isRefresh = false;
			listview.onRefreshComplete();
			MsToast.makeText(baseContext, "暂无更新").show();
			return;
		}
		if (isLoadMore) {
			/**
			 * 分页加载状态，不显示空页面
			 */
			isLoadMore = false;
			listview.onLoadComplete();
			return;
		}

		else {
			/**
			 * 初始化为空
			 */
			showNoDataView("目前还没有内容信息");
			return;
		}
	}

	/**
	 * 
	 * 
	 * @describe:设置加载失败页面
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-18下午4:58:56
	 * 
	 */
	public void setFailView() {
		if (isRefresh) {
			/*
			 * 下拉刷新失败，不显示失败页面
			 */
			MsToast.makeText(baseContext, "刷新失败").show();
			isRefresh = false;
			listview.onRefreshComplete();
			return;
		}
		if (isLoadMore) {
			/**
			 * 分页加载失败
			 */
			MsToast.makeText(baseContext, "分页加载失败").show();
			isLoadMore = false;
			listview.onLoadComplete();
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
			handlerOrderList.sendMessage(message);
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
			handlerOrderList.sendMessage(message);
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
				handlerOrderList.sendMessage(message);
			} else {
				pageNum++;
				isLoadMore = true;
				getNetData();
			}

		}
	}
}