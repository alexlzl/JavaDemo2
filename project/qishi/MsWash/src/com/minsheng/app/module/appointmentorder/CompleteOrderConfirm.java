package com.minsheng.app.module.appointmentorder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.Header;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.AddShopCarEntity;
import com.minsheng.app.entity.AddShopCarEntity.Infor.ShopCarList;
import com.minsheng.app.entity.ModifyState;
import com.minsheng.app.entity.UpdateShopcarParam;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.appointmentorder.CompleteOrderConfirmAdapter.RemoveCallback;
import com.minsheng.app.module.main.HomeActivity;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;

/**
 * 
 * @describe:完善订单，确认页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-4下午4:56:54
 * 
 */
public class CompleteOrderConfirm extends BaseActivity implements
		RemoveCallback {
	private static final String TAG = "CompleteOrderConfirm";
	private SwipeListView mSwipeListView;
	private CompleteOrderConfirmAdapter adapter;
	private Button btSure;
	private static final int COUNT_DOWN_TIME = 1000;
	private int mCountDown = MsConfiguration.FIND_PWD_CHECK_CODE_TIME;
	private Timer countDownTimer = null;
	private TextView tvTotalPrice;
	private AddShopCarEntity shopcarData;
	private List<ShopCarList> shopcarList;
	private TextView tvSn;
	private TextView tvName;
	private int orderId;
	private ModifyState updateShopcarBean;
	private int washState;
	// private boolean isFirstRequest = true;
	private boolean isBack;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.complete_order_confirm);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		mSwipeListView = (SwipeListView) findViewById(R.id.complete_order_confirm_lv);
		btSure = (Button) findViewById(R.id.complete_order_confirm_sure);
		tvTotalPrice = (TextView) findViewById(R.id.complete_order_confirm_totalprice_num);
		shopcarData = (AddShopCarEntity) getIntent().getSerializableExtra(
				"shopcarlist");
		orderId = getIntent().getIntExtra("orderId", 0);
		washState = getIntent().getIntExtra("washState", 0);
		shopcarList = shopcarData.getInfo().getOrderCartList();
		tvSn = (TextView) findViewById(R.id.complete_order_confirm_num_content);
		tvName = (TextView) findViewById(R.id.complete_order_confirm_username);
		LogUtil.d(TAG, "购物车数据列表" + shopcarList.toString());
	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub

		adapter = new CompleteOrderConfirmAdapter(shopcarList, mSwipeListView,
				baseContext);
		mSwipeListView.setAdapter(adapter);
		adapter.setListener(this);
		ViewUtil.setActivityPrice(tvTotalPrice, shopcarData.getInfo()
				.getCartPrice() + "");
		tvSn.setText(shopcarData.getInfo().getOrderSn());
		tvName.setText(shopcarData.getInfo().getConsignee());
		setSwipeConfiguration();
	}

	private void setSwipeConfiguration() {
		mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		mSwipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
//		mSwipeListView
//				.setOffsetLeft(getResources().getDisplayMetrics().widthPixels * 1 / 3);
		mSwipeListView.setAnimationTime(0);
		mSwipeListView.setSwipeOpenOnLongPress(false);
	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		btSure.setOnClickListener(this);

	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "完善订单";
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
	 * 回调删除购物车数据
	 */
	@Override
	public void remove(int position) {
		// TODO Auto-generated method stub
		shopcarList.remove(position);
		adapter.notifyDataSetChanged();
		// 关闭SwipeListView
		mSwipeListView.closeOpenedItems();
		if (shopcarList != null && shopcarList.size() == 0) {
			tvTotalPrice.setVisibility(View.GONE);
		}
		MsToast.makeText(this, "删除成功").show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.complete_order_confirm_sure:
			/**
			 * 用户确认=======================
			 */
			final Dialog dialog = new AlertDialog.Builder(this).create();
			dialog.show();
			dialog.setContentView(R.layout.change_time_dialog);
			Window window = dialog.getWindow();
			window.setWindowAnimations(R.style.WindowAnimation);
			WindowManager.LayoutParams wm = window.getAttributes();
			wm.gravity = Gravity.CENTER;
			Button btConfirm = (Button) dialog
					.findViewById(R.id.change_time_dialog_confirm);
			Button btCancle = (Button) dialog
					.findViewById(R.id.change_time_dialog_cancle);
			TextView tvTitle = (TextView) dialog
					.findViewById(R.id.change_time_dialog_title);
			tvTitle.setText("你确定要更改状态吗?");
			btConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.cancel();
					userConfirm();
				}
			});
			btCancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.cancel();
					/**
					 * 取消变更
					 */
				}
			});

			break;
		}
	}

	private void userConfirm() {
		/**
		 * 
		 */
		if (shopcarList != null && shopcarList.size() > 0) {
			if (adapter.oneCodeList != null && adapter.oneCodeList.size() > 0) {
				boolean isEmpty = false;
				for (int i = 0; i < adapter.oneCodeList.size(); i++) {
					if (StringUtil.isEmpty(adapter.oneCodeList.get(i))) {
						isEmpty = true;
						MsToast.makeText(baseContext, "请扫描条形码").show();
						return;
					}
				}
				if (!isEmpty) {
					startCountDown();
					getUpdateList();
					updateShopcar(true);
				}

			} else {
				MsToast.makeText(baseContext, "请扫描条形码").show();
			}
		} else {
			MsToast.makeText(baseContext, "请先选择衣物").show();
		}
	}

	/**
	 * 
	 * 
	 * @describe:开始倒计时
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-31下午4:21:25
	 * 
	 */
	private void startCountDown() {
		mCountDown = 10;
		countDownTimer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				mCountDown--;
				if (mCountDown < 0) {
					countDownHandler
							.sendEmptyMessage(MsConfiguration.COUNT_DOWN_END);
					countDownTimer.cancel();
					countDownTimer = null;
				} else {
					countDownHandler
							.sendEmptyMessage(MsConfiguration.COUNT_DOWNING);
				}
			}
		};
		countDownTimer.schedule(timerTask, 0, COUNT_DOWN_TIME);
	}

	/**
	 * 
	 * 
	 * @describe:获取更新购物车的数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-31下午4:21:45
	 * 
	 */

	private void getUpdateList() {
		if (MsApplication.updateShopcarParam != null) {
			MsApplication.updateShopcarParam.clear();
		}
		int size = shopcarList.size();
		for (int i = 0; i < size; i++) {
			String oneCode = adapter.oneCodeList.get(i);
			boolean isFirshOrder = adapter.isFirstOrderList.get(i);
			int isFirst = 0;
			if (isFirshOrder) {
				isFirst = 1;
			} else {
				isFirst = 0;
			}
			// 选择备注按钮内容
			String remarkTag;
			StringBuilder sb = new StringBuilder();
			if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
					.getCheckOne())) {
				if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
						.getCheckTwo())
						|| !StringUtil.isEmpty(MsApplication.checkContent
								.get(i).getCheckThree())
						|| !StringUtil.isEmpty(MsApplication.checkContent
								.get(i).getCheckFour())
						|| !StringUtil.isEmpty(MsApplication.checkContent
								.get(i).getCheckFive())) {
					sb.append(MsApplication.checkContent.get(i).getCheckOne()
							+ ", ");
				} else {
					sb.append(MsApplication.checkContent.get(i).getCheckOne()
							+ "");
				}

			}
			if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
					.getCheckTwo())) {
				if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
						.getCheckThree())
						|| !StringUtil.isEmpty(MsApplication.checkContent
								.get(i).getCheckFour())
						|| !StringUtil.isEmpty(MsApplication.checkContent
								.get(i).getCheckFive())) {
					sb.append(MsApplication.checkContent.get(i).getCheckTwo()
							+ ", ");
				} else {
					sb.append(MsApplication.checkContent.get(i).getCheckTwo()
							+ "");
				}

			}
			if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
					.getCheckThree())) {
				if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
						.getCheckFour())
						|| !StringUtil.isEmpty(MsApplication.checkContent
								.get(i).getCheckFive())) {
					sb.append(MsApplication.checkContent.get(i).getCheckThree()
							+ ", ");
				} else {
					sb.append(MsApplication.checkContent.get(i).getCheckThree()
							+ "");
				}

			}
			if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
					.getCheckFour())) {
				if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
						.getCheckFive())) {
					sb.append(MsApplication.checkContent.get(i).getCheckFour()
							+ ", ");
				} else {
					sb.append(MsApplication.checkContent.get(i).getCheckFour()
							+ "");
				}

			}
			if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
					.getCheckFive())) {
				sb.append(MsApplication.checkContent.get(i).getCheckFive() + "");
			}

			remarkTag = sb.toString();
			// 输入备注内容
			String remark = null;
			if (!StringUtil.isEmpty(MsApplication.checkContent.get(i)
					.getCheckSix())) {
				remark = MsApplication.checkContent.get(i).getCheckSix();
			}
			int ordeCartId = shopcarList.get(i).getOrdeCartId();
			UpdateShopcarParam param = new UpdateShopcarParam();
			param.setOrdeCartId(ordeCartId);
			param.setOneCode(oneCode);
			param.setRemark(remark);
			param.setRemarkTag(remarkTag);
			param.setIsFirst(isFirst);
			MsApplication.updateShopcarParam.add(param);
		}
		LogUtil.d(TAG, "更新购物车数据" + MsApplication.updateShopcarParam.toString());
	}

	/**
	 * 倒计时消息处理
	 */
	private Handler countDownHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsConfiguration.COUNT_DOWNING:
				btSure.setText("再次发送请求" + mCountDown + "秒");
				btSure.setTextSize(15);
				btSure.setClickable(false);
				// btSure.setBackground(baseContext.getResources().getDrawable(
				// R.drawable.sendmessage_bg));
				btSure.setBackgroundDrawable(baseContext.getResources()
						.getDrawable(R.drawable.sendmessage_bg));
				break;
			case MsConfiguration.COUNT_DOWN_END:
				btSure.setText("请用户确认");
				btSure.setClickable(true);
				btSure.setTextSize(18);
				// btSure.setBackground(baseContext.getResources().getDrawable(
				// R.drawable.order_list_button_bg_yellow));
				btSure.setBackgroundDrawable(baseContext.getResources()
						.getDrawable(R.drawable.order_list_button_bg_yellow));
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 
	 * 
	 * @describe:更新购物车信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-24下午1:52:27
	 * 
	 */
	private void updateShopcar(final boolean isShow) {
		if (isShow) {
			showRoundProcessDialog();
		}

		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		map.put("orderCarts", MsApplication.updateShopcarParam);
		map.put("washStatus", washState);
		map.put("isReturn", isShow == true ? 0 : 1);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.UPDATE_SHOPCAR_PRODUCT,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						if (isShow) {
							dismissRoundProcessDialog();
						}

						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerUpdateShopcar.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ModifyState arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						if (isShow) {
							dismissRoundProcessDialog();
						}
					}

					@Override
					protected ModifyState parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						if (isShow) {
							dismissRoundProcessDialog();
						}
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							updateShopcarBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerUpdateShopcar.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerUpdateShopcar.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return updateShopcarBean;
					}
				});
	}

	/**
	 * 更新购物车信息
	 */
	Handler handlerUpdateShopcar = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:

				if (updateShopcarBean != null
						&& updateShopcarBean.getCode() == 0) {
					if (!isBack) {
						MsToast.makeText(baseContext, "请求成功").show();
						Intent intent = new Intent(baseActivity,
								HomeActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						MsStartActivity.startActivity(baseActivity, intent);
					}

				} else {
					if (updateShopcarBean != null) {
						if (!isBack) {
							MsToast.makeText(baseContext,
									updateShopcarBean.getMsg()).show();
						}

					} else {
						if (!isBack) {
							MsToast.makeText(baseContext, "提交失败").show();
						}

					}

				}

				break;
			case MsConfiguration.FAIL:
				if (!isBack) {
					MsToast.makeText(baseContext, "提交失败").show();
				}

				break;
			}
		}

	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (shopcarList != null && shopcarList.size() > 0) {
			getUpdateList();
			updateShopcar(false);
		} else {
		}
		isBack = true;
		setResult(MsConfiguration.REQUEST_FROM_CONFIRM_ORDER);
		super.onBackPressed();
	}

}
