package cn.bluerhino.driver.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRAction;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.LocationInfo;
import cn.bluerhino.driver.model.OrderExecuteResponse;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.PayInfo;
import cn.bluerhino.driver.network.ExecuteOrderRequest;
import cn.bluerhino.driver.utils.ContactDriverManager;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.bluerhino.driver.view.FastDialog;
import cn.bluerhino.driver.view.LoadingDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.framework.BRFastRequest;
import com.bluerhino.library.network.framework.BRModelRequest.BRModelResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * Describe:订单信息确认页面
 * 
 * Date:2015-6-30
 * 
 * Author:liuzhouliang
 */
public class ConfirmOrderInfoActivity extends BaseParentActivity implements
        OnClickListener {
    public static final String TAG = ConfirmOrderInfoActivity.class
            .getSimpleName();

    @InjectView(R.id.left_img)
    ImageView mLeftBarButton;
    @InjectView(R.id.center_title)
    TextView center_title;
    @InjectView(R.id.distance)
    TextView mServiceMiles;
    @InjectView(R.id.wait_time)
    TextView mWaitTime;
    @InjectView(R.id.night_period)
    TextView mNightPeriord;
    @InjectView(R.id.total_charge)
    TextView mTotalCharge;
    @InjectView(R.id.payed)
    TextView mPayed;
    @InjectView(R.id.not_pay)
    TextView mNotPayed;
    /**提交付款按钮*/
    @InjectView(R.id.paymode_btn)
    Button mAacceptMoneyButton;

    private Activity mActivity;
    private OrderInfo mCurrentOrder;
    private PayInfo mPayInfo;
    private FastDialog mCashPayDialog;
    private BRFastRequest executeOrderRequest;
    private RequestQueue mRequestQueue;
    private LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_confirm_order);
        ButterKnife.inject(this);
        parserIntent();
        init();
        createDialog();
        buildInfoView();
        MobclickAgent.onEvent(this, "cost_information_page");
        LogUtil.d(TAG, "onCreate"+"taskid=="+getTaskId());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parserIntent();
        init();
        createDialog();
        buildInfoView();
    }

    private void parserIntent() {
        mCurrentOrder = getIntent().getParcelableExtra(
                BRAction.EXTRA_ORDER_INFO);
        mPayInfo = getIntent().getParcelableExtra(BRAction.EXTRA_ORDER_PAYINFO);
    }

    private void init() {
        mActivity = this;
        mLoadingDialog = new LoadingDialog(mActivity, "正在确认支付...");
        initVolley();
    }

    private void initVolley() {
        mRequestQueue = ApplicationController.getInstance().getRequestQueue();
        BRModelResponse<OrderExecuteResponse> succeedListener = new BRModelResponse<OrderExecuteResponse>() {
            @Override
            public void onResponse(OrderExecuteResponse model) {
                mLoadingDialog.dismiss();
                ApplicationController.getInstance().getBaiduTts().speak(mRes.getString(R.string.confrim_have_worked_hard));
                Intent intent = new Intent(mActivity,
                        MainActivity.class);
                //跳到到Mainactivitybin刷新当前订单页
                intent.putExtra(TAG, "");
                startActivity(intent);
                finish(); 
            }
        };

        ErrorListener mErrorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadingDialog.dismiss();
                Toast.makeText(mContext, "订单异常, 请注意订单是否支付完成", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(mActivity, MainActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();
            }
        };

        LocationInfo locationinfo = ApplicationController.getInstance()
                .getLastLocationInfo();
        BRRequestParams params = new BRRequestParams(ApplicationController
                .getInstance().getLoginfo().getSessionID());
        params.setDeviceId(ApplicationController.getInstance().getDeviceId());
        params.setVerCode(ApplicationController.getInstance().getVerCode());
        params.put("orderId", mCurrentOrder.getOrderId());
        params.put("OrderFlag", OrderInfo.OrderState.SERVICEFINISH);
        params.put("lat", String.valueOf(locationinfo.getLatitude()));
        params.put("lng", String.valueOf(locationinfo.getLongitude()));
        params.put("pay", mPayInfo.getNoPay());
        executeOrderRequest = new ExecuteOrderRequest.Builder()
                .setSucceedListener(succeedListener)
                .setErrorListener(mErrorListener).setParams(params).build();
    }

    private void buildInfoView() {
        center_title.setText("费用信息");
        mLeftBarButton.setVisibility(View.VISIBLE);
        mLeftBarButton.setOnClickListener(this);
        mServiceMiles.setText(mPayInfo.getKilometer() + "公里");
        mWaitTime.setText(mPayInfo.getWaitTime());
        mNightPeriord.setText(mPayInfo.getIsNightOrder() == 0 ? "未涉及" : "￥"
                + mPayInfo.getBillOfNight() + "元");
        mTotalCharge.setText(mPayInfo.getNeedPay());
        mPayed.setText(mPayInfo.getPay());
        mNotPayed.setText(mPayInfo.getNoPay());
		float unfinishedPay = 0;
        if(!StringUtil.isEmpty(mPayInfo.getNeedCashPay())){
        	unfinishedPay = Float.parseFloat(mPayInfo.getNeedCashPay());
        }
       
        if (unfinishedPay > 0.0F) {
            // 显示现金收款, 会弹出现金收款框
            mAacceptMoneyButton.setText(R.string.cash_receipts);
        } else {
            // 直接提示完成即可
            mAacceptMoneyButton.setText(getString(R.string.notneed_money_receipts));
        }
        mAacceptMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCashPayDialog != null) {
                    mCashPayDialog.show();
                }
            }
        });

    }

    private void createDialog() {
        /**
         * getNeedCashPay显示需要现金支付的金额
         */
        if (mCashPayDialog == null) {
            mCashPayDialog = new FastDialog(mActivity,
                    getString(R.string.order_deal_needcash),
                    mPayInfo.getNeedCashPay() + "元",
                    getString(R.string.order_deal_payalert),
                    getString(R.string.order_deal_confrim),
                    getString(R.string.order_deal_contact));

            mCashPayDialog.setOnClickListener(new FastDialog.OnClick() {
                @Override
                public void onLeftClick() {
                    mLoadingDialog.show();
                    mRequestQueue.add(executeOrderRequest);
                }

                @Override
                public void onRightClick() {
                    ContactDriverManager.getIntance().contactDriverManager(
                            mContext);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        executeOrderRequest.cancel();
        executeOrderRequest = null;
        mLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.left_img:
            finish();
            break;
        default:
            break;
        }
    }

    @Override
    protected void onResume() {
    	MobclickAgent.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
    	MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }
}
