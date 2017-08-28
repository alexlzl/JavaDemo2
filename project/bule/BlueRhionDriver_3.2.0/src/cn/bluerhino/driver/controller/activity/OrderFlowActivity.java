package cn.bluerhino.driver.controller.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRAction;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.receiver.JPushReceiver;
import cn.bluerhino.driver.model.DeliverInfo;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.datasource.orderflow.IOrderFlowComponent;
import cn.bluerhino.driver.model.datasource.orderflow.OrderStateMachineContext;
import cn.bluerhino.driver.utils.BaiduNaviStatus;
import cn.bluerhino.driver.utils.ContactDriverManager;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.TimeUtil;
import cn.bluerhino.driver.view.CurOrderRemarkLinearLayout;
import cn.bluerhino.driver.view.NavigatorController;
import cn.bluerhino.driver.view.PlaceofReceiptLinearLayout;
import com.android.volley.RequestQueue;
import com.baidu.navisdk.BaiduNaviManager;
import com.bluerhino.library.widget.CWToast;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * Describe:订单状态跟踪页面
 * 
 * Date:2015-6-25
 * 
 * Author:liuzhouliang
 */
public class OrderFlowActivity extends BaseParentActivity implements
		IOrderFlowComponent {
	private static final String TAG = OrderFlowActivity.class.getSimpleName();
	public static final String ACTION_ORDERDEAL_RESULT = "cn.bluerhno_RESULT";
	private LayoutInflater mLayoutInflater;

	@InjectView(R.id.order_flow_lefttab_item)
	Button mLeftTab;
	@InjectView(R.id.order_flow_righttab_item)
	Button mRightTab;
	@InjectView(R.id.center_title)
	TextView mTitle;
	@InjectView(R.id.orderinfo_item_placeofdispatch)
	TextView place_of_dispatch;
	@InjectView(R.id.orderinfo_item_placeofShipping)
	LinearLayout place_of_Shipping;
	@InjectView(R.id.orderinfo_remark_info_detail)
	LinearLayout orderinfo_remark_info_detail;
	@InjectView(R.id.orderinfo_item_user)
	TextView orderinfo_item_user;
	@InjectView(R.id.orderinfo_item_userphone)
	TextView orderinfo_item_userphone;
	@InjectView(R.id.orderinfo_item_kilometer)
	TextView orderinfo_item_kilometer;
	@InjectView(R.id.orderinfo_orderid)
	TextView mOrderinfo_orderid;
	@InjectView(R.id.orderinfo_remark_info)
	TextView orderinfo_remark_info;
	@InjectView(R.id.orderbill_cost)
	TextView orderbill_cost;
	@InjectView(R.id.waiting_for_the_cost)
	TextView waiting_for_the_cost;
	@InjectView(R.id.money_need_cost)
	TextView money_need_cost;
	@InjectView(R.id.left_img)
	ImageView leftBar_img;
	@InjectView(R.id.right_img)
	ImageView rightBar_img;
	@InjectView(R.id.wait_time)
	// 倒计时视图
	LinearLayout mWaitTimeView;
	private String mChargeconSignor;
	private String mChargeconSignee;
	private String mNoPay;
	private OrderStateMachineContext mMachineContext;
	private RequestQueue mQueue;
	private OrderInfo mOrderInfo;
	private NavigatorController mNavigatorController;
	// 当前订单状态显示视图
	private TextView tvOrderState;
	private LinearLayout llOrderStateParent;
	private String fromType;
	/**
	 * 接受推送到的广播，如果接受到相同订单广播，结束当前页面
	 */
	private final BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (mOrderInfo != null) {
				int ordernum = intent.getIntExtra(JPushReceiver.EXTRA_ORDERNUM,
						0);
				if (ordernum != 0 && mOrderInfo.getOrderNum() == ordernum) {
					//判断订单是否被改派或者是否正在被服务，然后关闭当前页面
					if (JPushReceiver.ACTION_NOTIFICATION_PULLREFRESH
							.equals(action)) {
						OrderFlowActivity.this.finish();
					} else if (JPushReceiver.ACTION_REARRANGED_ORDER
							.equals(action)) {
						OrderFlowActivity.this.finish();
					}
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.mLayoutInflater = LayoutInflater.from(this);
		initStringFormat();
		initView();
		parserOrderInfo();
		buildOrderInfo();
		mQueue = ApplicationController.getInstance().getRequestQueue();
		mMachineContext = new OrderStateMachineContext(this);
		mMachineContext.create();
		registerBroadCast();
		loadView();
		LogUtil.d(TAG, "onCreate" + "taskid==" + getTaskId());
		MobclickAgent.onEvent(this, "pageCurrList_page_detail");
	}

	private void initView() {
		setContentView(R.layout.activity_orderflow);
		butterknife.ButterKnife.inject(this);
	}

	/**
	 * 判断司机状态，显示在界面上
	 */
	private void loadView() {
		llOrderStateParent = (LinearLayout) findViewById(R.id.activity_orderflow_orderstate_parent);
		tvOrderState = (TextView) findViewById(R.id.activity_orderflow_orderstate);
		initOrderState();
	}

	private void initOrderState() {
		switch (getOrderInfo().getOrderFlag()) {
		case OrderInfo.OrderState.DELIVERDEPARTURE:
			llOrderStateParent.setVisibility(View.VISIBLE);
			tvOrderState.setText("当前司机出发");
			break;
		case OrderInfo.OrderState.REACHSHIPADDRESS:
			llOrderStateParent.setVisibility(View.VISIBLE);
			tvOrderState.setText("当前抵达发货地，正在计算等待时间");
			break;
		case OrderInfo.OrderState.DEPARTURESHIPADDRESS:
			llOrderStateParent.setVisibility(View.VISIBLE);
			tvOrderState.setText("已从发货地出发");
			break;
		case OrderInfo.OrderState.REACHPLACEOFRECEIPT:
			llOrderStateParent.setVisibility(View.VISIBLE);
			if (isSingleOrMultipleDestination()) {
				tvOrderState.setText("已到达收货地" + getOrderInfo().getArriveCount()
						+ "正在计算等待时间");
			} else {
				tvOrderState.setText("已到达收货地" + "正在计算等待时间");
			}

			break;
		case OrderInfo.OrderState.DEPARTURERECEIPT:
			llOrderStateParent.setVisibility(View.VISIBLE);
			tvOrderState.setText("已到达收货地" + getOrderInfo().getArriveCount()
					+ "正在计算等待时间");
			break;
		default:
			break;
		}
	}

	protected boolean isSingleOrMultipleDestination() {
		boolean isSingleOrMultiple = false;
		OrderInfo orderInfo = getOrderInfo();
		List<DeliverInfo> takeDeliverList = orderInfo.getDeliver();
		int size = 0;
		if (takeDeliverList != null && takeDeliverList.size() > 0) {
			size = takeDeliverList.size();
		}
		if (size == 1) {
			/**
			 * 一个收货地
			 */
			isSingleOrMultiple = false;
			return isSingleOrMultiple;
		}
		if (size > 1) {
			/**
			 * 多个收货地
			 */
			isSingleOrMultiple = true;
			return isSingleOrMultiple;
		}
		return isSingleOrMultiple;
	}

	/**
	 * 
	 * Describe:初始化文本资源
	 * 
	 * Date:2015-6-25
	 * 
	 * Author:liuzhouliang
	 */
	private void initStringFormat() {
		Resources resources = getResources();
		mChargeconSignor = resources
				.getString(R.string.paystate_chargeconsignor);
		mChargeconSignee = resources
				.getString(R.string.paystate_chargeconsignee);
		mNoPay = resources.getString(R.string.paystate_chargeconother);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			setIntent(intent);
			parserOrderInfo();
		}
	}

	/**
	 * 
	 * Describe:获取传入的订单对象数据
	 * 
	 * Date:2015-6-25
	 * 
	 * Author:liuzhouliang
	 */
	private void parserOrderInfo() {
		Intent intent = getIntent();
		if (intent != null) {
			mOrderInfo = intent.getParcelableExtra(BRAction.EXTRA_ORDER_INFO);
			fromType = intent.getStringExtra("fromType");
		}
	}

	/**
	 * 
	 * Describe:初始化控件状态
	 * 
	 * Date:2015-6-26
	 * 
	 * Author:liuzhouliang
	 */
	private void buildOrderInfo() {
		leftBar_img.setVisibility(View.VISIBLE);
		rightBar_img.setVisibility(View.VISIBLE);
		rightBar_img.setImageResource(R.drawable.contact_while_icon);

		//设置头部标题，判断服务时间，有的是预约的，有的是即时的
		String transTimeLabel = "";
		if (mOrderInfo.getServeType() == 200) {
			transTimeLabel = "即时服务";
		} else {
			transTimeLabel = TimeUtil.format(new Date(
					mOrderInfo.getTransTime() * 1000));
		}
		mTitle.setText(transTimeLabel);
		//设置发货地信息
		place_of_dispatch.setText(mOrderInfo.getPickupAddress());
		orderinfo_item_user.setText(mOrderInfo.getPickupMan());
		orderinfo_item_userphone.setText(mOrderInfo.getPickupPhone());
		place_of_Shipping.removeAllViews();
		
		//判断有几个目的地
		if (1 == mOrderInfo.getDeliver().size()) {
			/**
			 * 一个目的地
			 */
			place_of_Shipping.setVisibility(View.VISIBLE);
			PlaceofReceiptLinearLayout placeofreceipt = (PlaceofReceiptLinearLayout) mLayoutInflater
					.inflate(R.layout.fragment_currentorder_placeofreceipt,
							null);
			DeliverInfo deliverEx = mOrderInfo.getDeliver().get(0);
			placeofreceipt.updateAddressTitle();
			placeofreceipt.updateAddress(deliverEx.getDeliverAddress());
			placeofreceipt.updateConsigneeName(deliverEx.getTakeMan());
			placeofreceipt.updateConsigneePhone(deliverEx.getTakePhone());
			place_of_Shipping.addView(placeofreceipt);
		} else if (1 < mOrderInfo.getDeliver().size()) {
			/**
			 * 多个目的地
			 */
			place_of_Shipping.setVisibility(View.VISIBLE);
			int index = 0;
			for (DeliverInfo deliverEx : mOrderInfo.getDeliver()) {
				PlaceofReceiptLinearLayout placeofreceipt = (PlaceofReceiptLinearLayout) mLayoutInflater
						.inflate(R.layout.fragment_currentorder_placeofreceipt,
								null);
				placeofreceipt.updateAddressTitleIndex(++index);
				placeofreceipt.updateAddress(deliverEx.getDeliverAddress());
				placeofreceipt.updateConsigneeName(deliverEx.getTakeMan());
				placeofreceipt.updateConsigneePhone(deliverEx.getTakePhone());
				place_of_Shipping.addView(placeofreceipt);
			}
		} else {
			/**
			 * 没有目的地
			 */
			place_of_Shipping.setVisibility(View.GONE);
		}
		//订单号和公里数
		orderinfo_item_kilometer.setText(mOrderInfo.getKilometer());
		mOrderinfo_orderid.setText(String.valueOf(mOrderInfo.getOrderId()));

		//备注信息
		if (TextUtils.isEmpty(mOrderInfo.getRemark())) {
			orderinfo_remark_info.setText("无");
		} else {
			orderinfo_remark_info.setText(mOrderInfo.getRemark());
		}

		// 设置底部的额外标签
		List<String> mTagList = new ArrayList<String>();
		if (mOrderInfo.getTrolleyNum() > 0) {
			mTagList.add("需要小推车");
		}
		if (mOrderInfo.getIsFollowCar() > 0) {
			mTagList.add("需要跟车");
		}
		if (mOrderInfo.getCarringType() > 0) {
			mTagList.add("需要搬运");
		}
		if (mOrderInfo.getReceiptType() > 0) {
			mTagList.add("需要回单");
		}
		if (mOrderInfo.getCollectCharges() != 0.0) {
			// mTagList.add("需要代收款" + mOrderInfo.getCollectCharges());
			mTagList.add("需要代收款");
		}
		orderinfo_remark_info_detail.removeAllViews();
		if (mTagList.size() == 0) {
			orderinfo_remark_info_detail.setVisibility(View.GONE);
		} else if (mTagList.size() == 1) {
			orderinfo_remark_info_detail.setVisibility(View.VISIBLE);
			CurOrderRemarkLinearLayout curOrderRemarkLinearLayout = (CurOrderRemarkLinearLayout) mLayoutInflater
					.inflate(R.layout.fragment_currentorder_remarkinfo, null);
			curOrderRemarkLinearLayout.setTitleShow();
			curOrderRemarkLinearLayout.setRemarkInfo(mTagList.get(0));
			orderinfo_remark_info_detail.addView(curOrderRemarkLinearLayout);
		} else {
			orderinfo_remark_info_detail.setVisibility(View.VISIBLE);
			for (int i = 0; i < mTagList.size(); i++) {
				CurOrderRemarkLinearLayout curOrderRemarkLinearLayout = (CurOrderRemarkLinearLayout) mLayoutInflater
						.inflate(R.layout.fragment_currentorder_remarkinfo,
								null);
				if (i == 0) {
					curOrderRemarkLinearLayout.setTitleShow();
				} else {
					curOrderRemarkLinearLayout.setTitleDismiss();
				}
				curOrderRemarkLinearLayout.setRemarkInfo(mTagList.get(i));
				orderinfo_remark_info_detail
						.addView(curOrderRemarkLinearLayout);
			}
		}

		// 订单金额
		SpannableStringBuilder chargeconsignor_string = new SpannableStringBuilder(
				mOrderInfo.getOrderBill());
		chargeconsignor_string.setSpan(new RelativeSizeSpan(1.5f), 0,
				mOrderInfo.getOrderBill().length() - 1,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		orderbill_cost.setText(chargeconsignor_string);

		// 需要收取的金额
		updateOrderPayInfoView(money_need_cost, mOrderInfo.getPayFlag(),
				mOrderInfo.getNoPay());

	}

	/**
	 * 
	 * Describe:判断需要收取的金额
	 * 
	 * Date:2015-6-25
	 * 
	 * Author:liuzhouliang
	 */
	private void updateOrderPayInfoView(TextView view, int payInfo, String money) {
		SpannableStringBuilder chargeconsignor_string;
		switch (payInfo) {
		case OrderInfo.CashPayType.CHARGECONSIGNOR:// 向发货人收款
			chargeconsignor_string = new SpannableStringBuilder(String.format(
					mChargeconSignor, money));
			chargeconsignor_string.setSpan(new RelativeSizeSpan(1.5f), 0,
					money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			chargeconsignor_string.setSpan(
					new ForegroundColorSpan(Color.parseColor("#FD6138")), 0,
					money.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			view.setText(chargeconsignor_string);
			break;
		case OrderInfo.CashPayType.CHARGECONSIGNEE:// 向收货人收款
			chargeconsignor_string = new SpannableStringBuilder(String.format(
					mChargeconSignee, money));
			chargeconsignor_string.setSpan(new RelativeSizeSpan(1.5f), 0,
					money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			chargeconsignor_string.setSpan(
					new ForegroundColorSpan(Color.parseColor("#FD6138")), 0,
					money.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			view.setText(chargeconsignor_string);
			break;
		default:
			// 不收现金
			view.setText(mNoPay);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMachineContext.destory();
		mQueue.cancelAll(TAG);
		unRegisterBroadCast();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (fromType != null
					&& "AppointOrderRemindDialogActivity".equals(fromType)) {
				startActivity(new Intent(this, MainActivity.class));
			} else {
				if (ApplicationController.getInstance()
						.getOrderListBackHandler() != null) {
					ApplicationController.getInstance()
							.getOrderListBackHandler()
							.sendEmptyMessage(MainActivity.BACK_CURRERTLIST);
				}

			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@OnClick({ R.id.left_img, R.id.right_img, R.id.nav_button })
	public void handleBarEvent(View v) {
		switch (v.getId()) {
		case R.id.left_img:
			/**
			 * 返回
			 */
			if (ApplicationController.getInstance().getOrderListBackHandler() != null) {
				ApplicationController.getInstance().getOrderListBackHandler()
						.sendEmptyMessage(MainActivity.BACK_CURRERTLIST);
			}

			finish();
			break;
		case R.id.right_img:
			//联系运控
			ContactDriverManager.getIntance().contactDriverManager(this);
			MobclickAgent.onEvent(this, "pageCurrList_btn_contact");
			break;
		case R.id.nav_button:
			startNavigator();
			MobclickAgent.onEvent(this, "pageCurrList_btn_navi");
			break;
		default:
			break;
		}
	}

	@OnClick(R.id.orderinfo_item_userphone)
	public void callPhone() {
		startActivity(initCallIntent(mOrderInfo.getPickupPhone()));
	}

	/**
	 * 
	 * Describe:开始导航
	 * 
	 * Date:2015-7-1
	 * 
	 * Author:liuzhouliang
	 */
	private void startNavigator() {
		if(!BaiduNaviStatus.getInstance().hasInit()){
			BaiduNaviStatus.getInstance().initEngine(this);
			CWToast.alert(
					getApplicationContext(),
					R.string.orderstate_try_restart_app).show();
			return;
		}
		boolean isInitSuccess = BaiduNaviManager.getInstance().checkEngineStatus(getApplicationContext());
		if(!isInitSuccess){
			BaiduNaviStatus.getInstance().initEngine(this);
			CWToast.alert(
					getApplicationContext(),
					R.string.orderstate_try_restart_app).show();
			return ;
		}
		
		if (mNavigatorController == null) {
			mNavigatorController = new NavigatorController(this);
		}
		mNavigatorController.planRoute(mOrderInfo);
	}

	/**
	 * Describe:注册广播
	 * 
	 * Date:2015-7-1
	 * 
	 * Author:liuzhouliang
	 */
	private void registerBroadCast() {
		IntentFilter intentFilter = new IntentFilter(ACTION_ORDERDEAL_RESULT);
		intentFilter.addAction(JPushReceiver.ACTION_NOTIFICATION_PULLREFRESH);
		ApplicationController.getInstance().getLocalBroadcastManager()
				.registerReceiver(mBroadCastReceiver, intentFilter);
	}

	/**
	 * 
	 * Describe:解除注册广播
	 * 
	 * Date:2015-7-1
	 * 
	 * Author:liuzhouliang
	 */
	private void unRegisterBroadCast() {
		ApplicationController.getInstance().getLocalBroadcastManager()
				.unregisterReceiver(mBroadCastReceiver);
	}

	@Override
	public OrderInfo getOrderInfo() {
		return mOrderInfo;
	}

	@Override
	public Context getContext() {
		return this;
	}

	@Override
	public Button getLeftTab() {
		return mLeftTab;
	}

	@Override
	public Button getRightTab() {
		return mRightTab;
	}

	@Override
	public RequestQueue getRequestQueue() {
		return mQueue;
	}

	/**
	 * 
	 * Describe:拨打电话
	 * 
	 * Date:2015-7-1
	 * 
	 * Author:liuzhouliang
	 */
	private Intent initCallIntent(CharSequence phoneNum) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNum));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	@Override
	public View getWaitTimeView() {
		return mWaitTimeView;
	}

	@Override
	public TextView getOrderStateTv() {
		return tvOrderState;
	}

	@Override
	public LinearLayout getOrderStateParentView() {
		return llOrderStateParent;
	}

}
