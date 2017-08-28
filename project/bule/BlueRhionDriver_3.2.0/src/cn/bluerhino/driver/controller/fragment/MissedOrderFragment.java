package cn.bluerhino.driver.controller.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.LoginActivity;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.datasource.MissOrderListAdapter;
import cn.bluerhino.driver.model.datasource.OrderListAdapter;
import cn.bluerhino.driver.network.MisssOrderListRequest;
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
 * 错过订单页面
 * 
 * @author Administrator
 * 
 */
public class MissedOrderFragment extends Fragment {

    private static final String TAG = MissedOrderFragment.class.getSimpleName();

    private List<OrderInfo> mOrderInfoList;

    private OrderListAdapter mAdapter;

    private SingleLayoutListView mPullListView;

    private boolean mIsRefreshList = false;
    private boolean mIsLoadingList = false;

    private Activity mActivity;

    private RequestQueue mRequestQueue;

    private BRRequestParams mParams;

    private BRFastRequest mMissOrderListrequest;

    private BRModelListResponse<List<OrderInfo>> mRefreshSuccessListener;

    private BRModelListResponse<List<OrderInfo>> mLoadedSuccessListener;

    private ErrorListener mRefreshErrorListener;

    private ErrorListener mLoadedErrorListener;

    private int mNowPage = 1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderInfoList = new ArrayList<OrderInfo>();
        mAdapter = new MissOrderListAdapter(mActivity, mOrderInfoList);
        initVolley();
    }

    /**
     * 
     * Describe:初始化网络
     * 
     * Date:2015-6-24
     * 
     * Author:liuzhouliang
     */
    private void initVolley() {
        if (checkDriverSession()) {
            ToastUtil.showToast(mActivity, R.string.wait_order_login_timeout);
            startActivity(new Intent(mActivity, LoginActivity.class));
            mActivity.finish();
        }
        mRequestQueue = ApplicationController.getInstance().getRequestQueue();
        mParams = new BRRequestParams(ApplicationController.getInstance().getSessionID());
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
            	MobclickAgent.onEvent(mActivity, "pagemissList_action_loadmore");
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
                    mPullListView.onLoadMoreComplete();
                    mIsLoadingList = false;
                }
            }
        };

    }

    protected void refreshComplete() {
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
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (mIsRefreshList == false) {
            mPullListView.pull2RefreshManually();
        }
    	super.onActivityCreated(savedInstanceState);
    }
    
    protected void refresh() {
        if (mIsRefreshList) {
            return;
        }
        mIsRefreshList = true;
        mParams.put("page", 1);
        mMissOrderListrequest = new MisssOrderListRequest.Builder()
                .setSucceedListener(mRefreshSuccessListener)
                .setErrorListener(mRefreshErrorListener).setParams(mParams)
                .build();
        mRequestQueue.add(mMissOrderListrequest);

    }

    protected void loadMore() {
        if (mIsLoadingList) {
            return;
        }
        mIsLoadingList = true;
        mParams.put("page", mNowPage+1);
        mMissOrderListrequest = new MisssOrderListRequest.Builder()
                .setSucceedListener(mLoadedSuccessListener)
                .setErrorListener(mLoadedErrorListener).setParams(mParams)
                .build();
        mRequestQueue.add(mMissOrderListrequest);

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
        mMissOrderListrequest.cancel();
        mMissOrderListrequest = null;
        super.onDestroy();
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
    
    private boolean checkDriverSession() {
        return TextUtils.isEmpty(ApplicationController.getInstance()
                .getSessionID());
    }

}
