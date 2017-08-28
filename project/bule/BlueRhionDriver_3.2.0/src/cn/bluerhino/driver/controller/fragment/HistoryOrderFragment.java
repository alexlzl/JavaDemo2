package cn.bluerhino.driver.controller.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.datasource.HistoryOrderListAdapter;
import cn.bluerhino.driver.model.datasource.OrderListAdapter;
import cn.bluerhino.driver.network.HistoryOrderListRequest;
import cn.bluerhino.driver.view.SingleLayoutListView;
import cn.bluerhino.driver.view.SingleLayoutListView.OnLoadMoreListener;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRModelListRequest.BRModelListResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 历史订单页面
 * 
 * @author Administrator
 * 
 */
public class HistoryOrderFragment extends BaseParentFragment {

    private static final String TAG = HistoryOrderFragment.class
            .getSimpleName();

    private Activity mActivity;

    private SingleLayoutListView mPullListView;

    private OrderListAdapter mAdapter;

    private List<OrderInfo> mOrderInfoList;

    private boolean mIsRefreshList = false;
    private boolean mIsLoadingList = false;

    /**
     * 当前页
     */
    private int mNowPage = 1;

    private ErrorListener mRefreshErrorListener;

    private ErrorListener mLoadedErrorListener;

    private BRModelListResponse<List<OrderInfo>> mRefreshSuccessListener;
    private BRModelListResponse<List<OrderInfo>> mLoadedSuccessListener;

    private BRRequestParams mParams;

    private BRFastRequest mHistoryOrderListrequest;

    private RequestQueue mRequestQueue;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initBaseModule();
        initVolley();
    }

    private void initBaseModule() {
        mOrderInfoList = new ArrayList<OrderInfo>();
        mAdapter = new HistoryOrderListAdapter(mActivity, mOrderInfoList);
    }

    private void initVolley() {
        mRequestQueue = ApplicationController.getInstance().getRequestQueue();
        mParams = new BRRequestParams(ApplicationController.getInstance().getLoginfo().getSessionID());
        mParams.setDeviceId(ApplicationController.getInstance().getDeviceId());
        mParams.setVerCode(ApplicationController.getInstance().getVerCode());
        mRefreshSuccessListener = new BRModelListResponse<List<OrderInfo>>() {
            @Override
            public void onResponse(List<OrderInfo> modelList) {
            	//重置为刷新第一页数据
            	mNowPage = 1;
                mIsRefreshList = false;
                if (mPullListView != null && modelList != null
                        && mOrderInfoList != null) {
                    mOrderInfoList.clear();
                    if (modelList.size() > 0) {
                        enableLoadMore();
                        mOrderInfoList.addAll(modelList);
                        refreshComplete();
                    } else {
                        mPullListView.onRefreshComplete();
                        ToastUtil.showToast(mActivity,
                                R.string.miss_order_nodata);
                    }
                }
            }
        };
        mLoadedSuccessListener = new BRModelListResponse<List<OrderInfo>>() {
            @Override
            public void onResponse(List<OrderInfo> modelList) {
            	mNowPage++;
                mIsLoadingList = false;
                if (mPullListView != null && modelList != null
                        && mOrderInfoList != null) {
                    if (modelList.size() > 0) {
                        mOrderInfoList.addAll(modelList);
                        loadMoreComplete();
                    } else {
                        mPullListView.onLoadMoreComplete();
                        disableLoadMore();
                        ToastUtil.showToast(mActivity,
                                R.string.miss_order_nomoredata);
                    }
                }
            }
        };

        mRefreshErrorListener = new VolleyErrorListener(mActivity) {
            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                if (mPullListView != null) {
                    mPullListView.onRefreshComplete();
                    mIsRefreshList = false;
                }
            }
        };

        mLoadedErrorListener = new VolleyErrorListener(mActivity) {
            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                if (mPullListView != null) {
                    mNowPage--;
                    mPullListView.onLoadMoreComplete();
                    mIsLoadingList = false;
                }
            }
        };

    }

    @Override
    public void onDestroyView() {
        if (mPullListView != null) {
            mPullListView.setAdapter(null);
            mPullListView = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null) {
            mAdapter = null;
        }
        if (mOrderInfoList != null) {
            mOrderInfoList.clear();
            mOrderInfoList = null;
        }
        mHistoryOrderListrequest.cancel();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common_orderlistview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPullListView = (SingleLayoutListView) view
                .findViewById(R.id.fragment_history_refresh_list);
        mPullListView.setAdapter(mAdapter);
        mPullListView.setMoveToFirstItemAfterRefresh(true);
        enableRefreshdMore();
        disableLoadMore();
        if (mIsLoadingList == false) {
            mPullListView.pull2RefreshManually();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }
    
    @Override
    public void onStop() {
        if (mIsRefreshList) {
            mPullListView.onRefreshComplete();
            mIsRefreshList = false;
        }
        if (mIsLoadingList) {
        	mPullListView.onLoadMoreComplete();
        	mIsLoadingList = false;
        }
    	super.onStop();
    }

    public void refresh() {
        if (mIsRefreshList) {
            return;
        }
        mIsRefreshList = true;
        mParams.put("page", 1);
        mHistoryOrderListrequest = new HistoryOrderListRequest.Builder()
                .setSucceedListener(mRefreshSuccessListener)
                .setErrorListener(mRefreshErrorListener).setParams(mParams)
                .build();
        mRequestQueue.add(mHistoryOrderListrequest);
    }

    public void loadMore() {
        if (mIsLoadingList) {
            return;
        }
        mIsLoadingList = true;
        mParams.put("page", mNowPage+1);
        mHistoryOrderListrequest = new HistoryOrderListRequest.Builder()
                .setSucceedListener(mLoadedSuccessListener)
                .setErrorListener(mLoadedErrorListener).setParams(mParams)
                .build();
        mRequestQueue.add(mHistoryOrderListrequest);
    }

    private void refreshComplete() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        if (mPullListView != null) {
            mPullListView.onRefreshComplete();
        }
    }

    private void loadMoreComplete() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        if (mPullListView != null) {
            mPullListView.onLoadMoreComplete();
        }
    }

    private void enableLoadMore() {
        mPullListView.setCanLoadMore(true);
        mPullListView.setAutoLoadMore(true);
        mPullListView.setOnLoadListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
    }

    private void disableLoadMore() {
        mPullListView.setCanLoadMore(false);
        mPullListView.setAutoLoadMore(false);
        mPullListView.setOnLoadListener(null);
    }

    private void enableRefreshdMore() {
        mPullListView.setCanRefresh(true);
        mPullListView
                .setOnRefreshListener(new SingleLayoutListView.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                });
    }
}