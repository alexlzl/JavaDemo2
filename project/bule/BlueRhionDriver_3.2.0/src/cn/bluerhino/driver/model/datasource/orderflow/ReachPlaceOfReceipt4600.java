package cn.bluerhino.driver.model.datasource.orderflow;

import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRAction;
import cn.bluerhino.driver.controller.activity.ConfirmOrderInfoActivity;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.model.OrderInfo.CashPayType;
import cn.bluerhino.driver.model.OrderInfo.PayMode;
import cn.bluerhino.driver.model.PayInfo;
import cn.bluerhino.driver.utils.LogUtil;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * Describe:当前已经完成状态：到达收货地 4600
 * 
 * Date:2015-7-17
 * 
 * Author:liuzhouliang
 */
public class ReachPlaceOfReceipt4600 extends OrderState {
	private static final String MTAG = ReachPlaceOfReceipt4600.class.getName();

	private DialogMessageStrategy mDialogMessageStrategy;
	private PayInfo mPayInfo;
	// 标记是否需要再次出发
	private boolean isStartAgain;
	// 标记是否服务完成
	private boolean isOrderFinish;
	// 总共的收货地数目
	private int totalDestination;
	// 当前到达第几个收货地
	private int arriveDestination;
	LayoutParams leftTabParams;
	LayoutParams rightTabParams;
	Button leftTabBar;
	Button rightTabBar;

	public ReachPlaceOfReceipt4600(OrderStateMachineContext machineContext) {
		super(machineContext);

		totalDestination = getOrderInfo().getDeliver().size();
		arriveDestination = getOrderInfo().getArriveCount();
		LogUtil.d(MTAG, "一共收货地：" + totalDestination + "已经到达收货地=== "
				+ arriveDestination);
	}

	@Override
	public void buildView() {
		leftTabBar = getLeftTab();
		leftTabParams = (LayoutParams) leftTabBar.getLayoutParams();
		leftTabBar.setLayoutParams(leftTabParams);

		rightTabBar = getRightTab();
		rightTabParams = (LayoutParams) rightTabBar.getLayoutParams();
		rightTabBar.setLayoutParams(rightTabParams);
		if (isSingleOrMultipleDestination()) {
			/**
			 * 多个收货地===========================1
			 */
			if ((totalDestination - arriveDestination) > 0) {
				/**
				 * 还有未到达的收货地=============
				 */

				LogUtil.d(MTAG, "多个收货地==还有未到达的收货地"
						+ (totalDestination - arriveDestination));
				leftTabParams.weight = 1;
				rightTabParams.weight = 0;
				leftTabBar.setText("到达收货地" + (arriveDestination + 1));

			} else {
				/**
				 * 所有收货地已经完成==============
				 */
				LogUtil.d(MTAG, "多个收货地===所有收货地已经完成");
				leftTabParams.weight = 1;
				leftTabBar.setText("服务完成");
				rightTabParams.weight = 1;
				rightTabBar.setText("再次出发");
			}

		} else {
			/**
			 * 单个收货地=================================2
			 */
			LogUtil.d(MTAG, "单个收货地");
			leftTabParams.weight = 1;
			leftTabBar.setText("服务完成");
			rightTabParams.weight = 1;
			rightTabBar.setText("再次出发");

		}

	}

	@Override
	public void onClick(View v) {
		if (isSingleOrMultipleDestination()) {
			/**
			 * 多个收货地==========================1
			 */
			int id = v.getId();
			if (arriveDestination > totalDestination
					|| arriveDestination == totalDestination) {
				/**
				 * 要到达的收货地数目大于总共的收货地数目 ===== 服务完成和再次出发
				 */

				if (id == getLeftTab().getId()) {
					/*
					 * 服务完成===请求支付信息
					 */
					LogUtil.d(MTAG, "onClick" + "收货地走完===服务完成===请求支付信息");
					MobclickAgent.onEvent(ApplicationController.getInstance(),
							"pageCurrList_btn_finish");
					if (mPayInfo == null) {
						/**
						 * 点击服务完成，先请求支付信息，然后在成功回调后，显示支付信息对话框
						 */
						LogUtil.d(MTAG, "onClick" + "收货地走完===服务完成===请求支付信息mPayInfo == null");
						mDialogMessageStrategy=null;
						ApplicationController.getInstance().isNeedShowDialog = true;
						isOrderFinish = true;
					} else {
						showOrderFinishDialogMessage();
						setFinishOrderDialogListener();
					}

				}

				if (id == getRightTab().getId()) {
					/**
					 * 再次出发====更新状态
					 */
					LogUtil.d(MTAG, "onClick" + "收货地走完===再次出发===更新状态");
					MobclickAgent.onEvent(ApplicationController.getInstance(),
							"pageCurrList_btn_again");
					isStartAgain = true;
					ApplicationController.getInstance().isStartAgainApp = true;
					ApplicationController.getInstance().isMultipleStartAgain=true;
					showStartAgainDialogMessage();
					setDialogPositiveDefalutListener();
					setCancleListener(new CancleClickListener() {

						@Override
						public void onCancleClick() {
							if (ApplicationController.testSwitch) {
								ToastUtil.showToast(mContext, "取消");
							}

							isStartAgain = false;
							ApplicationController.getInstance().isStartAgainApp = false;
							ApplicationController.getInstance().isMultipleStartAgain=false;
						}
					});
				}
				super.onClick(v);
			} else {
				/**
				 * 收货地还未走完
				 */
				if (id == getLeftTab().getId()) {
					/*
					 * 到达下一个收货地===更新订单状态
					 */
					LogUtil.d(MTAG, "onClick" + "收货地未走完===到达下个收货地===更新订单状态"
							+ arriveDestination);
					ApplicationController.getInstance().isAutomaticToNextDestination = true;
					MobclickAgent.onEvent(ApplicationController.getInstance(),
							"pageCurrList_btn_finish");
					setDialogPositiveDefalutListener();
					super.onClick(v);

				}
			}

		} else {
			/**
			 * 单个收货地===============================2
			 */

			int id = v.getId();
			LogUtil.d(TAG, "----d==getLeftTab().getId()-----"+(id==getLeftTab().getId())+"---------");
			if (id == getLeftTab().getId()) {

				LogUtil.d(TAG, (mPayInfo == null)+"-------");
				/**
				 * 点击服务完成:请求支付信息
				 */
				if (mPayInfo == null) {
					/**
					 * 点击服务完成，先请求支付信息，然后在成功回调后，显示支付信息对话框
					 */
					ApplicationController.getInstance().isNeedShowDialog = true;
					mDialogMessageStrategy=null;
					isOrderFinish = true;
				} else {
					showOrderFinishDialogMessage();
					setFinishOrderDialogListener();
				}

			} else if (id == getRightTab().getId()) {
				/**
				 * 点击再次出发:从收货地出发
				 */
				isStartAgain = true;
				ApplicationController.getInstance().isSingleStartAgain=true;
				showStartAgainDialogMessage();
				setDialogPositiveDefalutListener();
				LogUtil.d(TAG, "----arriveDestination------"+arriveDestination+"-----totalDestination----"+totalDestination);
				setCancleListener(new CancleClickListener() {

					@Override
					public void onCancleClick() {
						if (ApplicationController.testSwitch) {
							ToastUtil.showToast(mContext, "取消");
							LogUtil.d(TAG, "----取消4600-----");
						}

						isStartAgain = false;
						ApplicationController.getInstance().isSingleStartAgain=false;
					}
				});
			}
			super.onClick(v);
		}

	}

	/**
	 * 
	 * Describe:根据是否完成了所有的收货地，设置对话框的消息内容
	 * 
	 * Date:2015-6-30
	 * 
	 * Author:liuzhouliang
	 */
	protected void showStartAgainDialogMessage() {
		setDialogMessageStrategy(new DialogMessageStrategy() {
			@Override
			public String getMessage() {
				
				if (arriveDestination < totalDestination) {
					/**
					 * 完成的收货地小于总的收货地数目
					 */
					return String.format(mNormalNoticeFormat, "再次出发");
				} else if (arriveDestination >= totalDestination) {
					/**
					 * 已经完成了所有的收货地
					 */
					return String.format(mAgaindepartureFormat, getOrderInfo()
							.getDeliver().size());
				} else {

					return String.format(mNormalNoticeFormat, "再次出发");
				}
			}
		});
	}

	/**
	 * 
	 * Describe:显示服务完成状态下的显示消息
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	private void showOrderFinishDialogMessage() {
		setDialogMessageStrategy(new DialogMessageStrategy() {
			@Override
			public String getMessage() {
				if (mPayInfo == null) {
					if (arriveDestination < totalDestination) {
						
						String str = String.format(mUnfinishFormat, totalDestination,
								arriveDestination);
						LogUtil.d(TAG, str+"-------");
						return str;
					} else {
						return "";
					}
				} else {
					String msg = "无需收取现金,请确认！";
					OrderInfo orderInfo = getOrderInfo();
					if (orderInfo.getPaymentMode() == PayMode.CASH) {// 现金支付
						msg = String.format(mPlayInfoFormatForDeliver,
								mPayInfo.getNeedCashPay());
						if (orderInfo.getPayFlag() == CashPayType.CHARGECONSIGNOR) {
							msg = String.format(mPlayInfoFormatForPickup,
									mPayInfo.getNeedCashPay());
						} else if (orderInfo.getPayFlag() == CashPayType.CHARGECONSIGNEE) {
							msg = String.format(mPlayInfoFormatForDeliver,
									mPayInfo.getNeedCashPay());
						}
					}
					return msg;
				}
			}
		});
	}

	@Override
	public int getValue() {
		/**
		 * 当前已经完成订单状态：到达收货地 4600
		 */
		return OrderInfo.OrderState.REACHPLACEOFRECEIPT;
	}

	@Override
	public int getNextValue() {
		if (isSingleOrMultipleDestination()) {
			if (arriveDestination > totalDestination
					|| arriveDestination == totalDestination) {
				/**
				 * 到达收货地数目大于总共收货地数目====下个状态：5000
				 */
				if (isStartAgain) {
					return OrderInfo.OrderState.DEPARTURERECEIPT;// 4800
				} else {
					return OrderInfo.OrderState.SERVICEFINISH;
				}

			} else {
				/**
				 * 还有未完成的收货地=====下一个状态:4800
				 */
				return OrderInfo.OrderState.DEPARTURERECEIPT;
			}

		} else {

			if (isStartAgain
					|| ApplicationController.getInstance().isStartAgainAutomaticTo4600) {
				return OrderInfo.OrderState.DEPARTURERECEIPT;// 4800
			} else {
				return OrderInfo.OrderState.SERVICEFINISH;// 5000
			}
		}

	}

	@Override
	protected String getUrl() {
		if (isSingleOrMultipleDestination()) {
			/**
			 * 多个收货地====================
			 */
			if (arriveDestination > totalDestination
					|| arriveDestination == totalDestination) {
				if (isStartAgain) {
					return super.getUrl();
				} else {
					return PAYORDER;
				}
			} else {
				return super.getUrl();
			}

		} else {
			/**
			 * 单个收货地=====================
			 */
			if (isStartAgain||ApplicationController.getInstance().isStartAgainAutomaticTo4600) {
				/**
				 * 一个收货地，如果点击再次出发，请求URL为更新状态
				 */
				return super.getUrl();
			} else {
				/**
				 * 一个收货地，完成服务，请求支付信息
				 */
				return PAYORDER;

			}
		}

	}

	@Override
	protected BRRequestParams getParams() {
		if (isSingleOrMultipleDestination()) {
			/**
			 * 多个收货地==========================1
			 */
			if (arriveDestination > totalDestination
					|| arriveDestination == totalDestination) {
				if (isStartAgain) {
					return super.getParams();
				} else {
					BRRequestParams params = new BRRequestParams(
							ApplicationController.getInstance().getSessionID());
					params.setDeviceId(ApplicationController.getInstance()
							.getDeviceId());
					params.setVerCode(ApplicationController.getInstance()
							.getVerCode());
					params.put("orderId", getOrderInfo().getOrderId());
					return params;
				}
			} else {
				return super.getParams();
			}

		} else {
			/**
			 * 一个收货地================================2
			 */
			if (isStartAgain||ApplicationController.getInstance().isStartAgainAutomaticTo4600) {
				/**
				 * 一个收货地，再次出发，请求参数为更新状态的设置
				 */
				return super.getParams();
			} else {

				/**
				 * 一个收货地，完成服务，请求参数为获取支付信息
				 */
				BRRequestParams params = new BRRequestParams(
						ApplicationController.getInstance().getSessionID());
				params.setDeviceId(ApplicationController.getInstance()
						.getDeviceId());
				params.setVerCode(ApplicationController.getInstance()
						.getVerCode());
				params.put("orderId", getOrderInfo().getOrderId());
				return params;

			}
		}

	}

	@Override
	public void callOnResponse(JSONObject response) {
		setButtonClickAble();
		if (isSingleOrMultipleDestination()) {
			/**
			 * 多个收货地=======================1
			 */
			if (arriveDestination > totalDestination
					|| arriveDestination == totalDestination) {
				if (isStartAgain) {
					super.callOnResponse(response);
				} else {
					handlePay(response);
					if (ApplicationController.getInstance().isNeedShowDialog) {
						/**
						 * 点击完成服务，获取支付信息后，显示支付信息对话框
						 */
						ApplicationController.getInstance().isNeedShowDialog = false;
						showOrderFinishDialogMessage();
						setFinishOrderDialogListener();
						showExecuteDialog();
					}
				}
			} else {
				super.callOnResponse(response);
			}

		} else {
			/**
			 * 一个收货地============================2
			 */
			if (isStartAgain||ApplicationController.getInstance().isStartAgainAutomaticTo4600) {
				/**
				 * 一个收货地，再次出发回调，继续获取下个状态
				 */
				super.callOnResponse(response);
			} else {
				/**
				 * 一个收货地，服务完成，获取支付信息
				 */
				setButtonClickAble();
				handlePay(response);
				if (ApplicationController.getInstance().isNeedShowDialog) {
					/**
					 * 点击完成服务，获取支付信息后，显示支付信息对话框
					 */
					ApplicationController.getInstance().isNeedShowDialog = false;
					showOrderFinishDialogMessage();
					setFinishOrderDialogListener();
					showExecuteDialog();
				}
			}
		}

	}

	/**
	 * 
	 * Describe:设置服务完成的事件响应
	 * 
	 * Date:2015-7-7
	 * 
	 * Author:liuzhouliang
	 */

	protected void setFinishOrderDialogListener() {
		setDialogPositiveListener(new DialogPositiveClickListener() {
			@Override
			public void onClick() {
				Context context = mMachineContext.getContext();
				Intent intent = new Intent(context,
						ConfirmOrderInfoActivity.class);
				intent.putExtra(BRAction.EXTRA_ORDER_INFO, getOrderInfo());
				intent.putExtra(BRAction.EXTRA_ORDER_PAYINFO, mPayInfo);
				context.startActivity(intent);
			}
		});
	}

	/**
	 * 
	 * Describe:设置确定按钮事件进入支付页面
	 * 
	 * Date:2015-7-2
	 * 
	 * Author:liuzhouliang
	 */
	protected void handlePay(JSONObject response) {
		/**
		 * 获取支付信息
		 */
		Gson gson = new Gson();
		mPayInfo = gson.fromJson(response.toString(), PayInfo.class);
		LogUtil.d(MTAG, "返回支付信息" + mPayInfo.toString());
		setDialogPositiveListener(new DialogPositiveClickListener() {
			@Override
			public void onClick() {
				Context context = mMachineContext.getContext();
				Intent intent = new Intent(context,
						ConfirmOrderInfoActivity.class);
				intent.putExtra(BRAction.EXTRA_ORDER_INFO, getOrderInfo());
				intent.putExtra(BRAction.EXTRA_ORDER_PAYINFO, mPayInfo);
				context.startActivity(intent);
			}
		});

	}

	interface DialogMessageStrategy {
		public String getMessage();
	}

	private void setDialogMessageStrategy(DialogMessageStrategy strategy) {
		mDialogMessageStrategy = strategy;
	}

	@Override
	public String getExecuteDialogMessage() {

		if (isSingleOrMultipleDestination()) {
			/**
			 * 多个收货地===========
			 */
			if (arriveDestination > totalDestination
					|| arriveDestination == totalDestination) {
				if (mDialogMessageStrategy != null) {
					return mDialogMessageStrategy.getMessage();
				}
				if (isOrderFinish) {
					return "你确定服务完成吗？";
				}
			} else {
				return "你确定到达收货地" + (arriveDestination + 1) + "吗？";
			}

		} else {
			/**
			 * 单个收货地================
			 */
			if (mDialogMessageStrategy != null) {
				return mDialogMessageStrategy.getMessage();
			}
			if (isOrderFinish) {
				return "你确定服务完成吗？";
			}

		}
		return "";
	}
}
