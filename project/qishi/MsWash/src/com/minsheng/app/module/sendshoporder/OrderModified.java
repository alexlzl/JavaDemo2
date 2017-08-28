package com.minsheng.app.module.sendshoporder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.ModifyState;
import com.minsheng.app.entity.OrderDetail;
import com.minsheng.app.entity.OrderDetail.Infor.Detail.OrderList;
import com.minsheng.app.entity.UpdateShopcarParam;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;

/**
 * 
 * @describe:修改订单信息页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-4下午4:56:54
 * 
 */
public class OrderModified extends BaseActivity {
	private static final String TAG = "OrderModified";
	private ListView lv;
	private OrderModifiedAdapter adapter;
	private Button btSure;
	private TextView tvTotalPrice;
	private OrderDetail shopcarData;
	private List<OrderList> shopcarList;
	private TextView tvSn;
	private TextView tvName;
	private int orderId;
	private ModifyState updateShopcarBean;
	private int washState;
	private boolean isBack;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.order_modified_list);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		lv = (ListView) findViewById(R.id.complete_order_confirm_lv);
		btSure = (Button) findViewById(R.id.complete_order_confirm_sure);
		tvTotalPrice = (TextView) findViewById(R.id.complete_order_confirm_totalprice_num);
		shopcarData = (OrderDetail) getIntent().getSerializableExtra(
				"shopcarlist");
		orderId = getIntent().getIntExtra("orderId", 0);
		washState = getIntent().getIntExtra("washState", 0);
		shopcarList = shopcarData.getInfo().getOrderDetail()
				.getDeliveryProductDetails();
		tvSn = (TextView) findViewById(R.id.complete_order_confirm_num_content);
		tvName = (TextView) findViewById(R.id.complete_order_confirm_username);
		LogUtil.d(TAG, "购物车数据列表" + shopcarList.toString());
	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub

		adapter = new OrderModifiedAdapter(shopcarList, lv, this);
		lv.setAdapter(adapter);
		ViewUtil.setActivityPrice(tvTotalPrice, shopcarData.getInfo()
				.getOrderDetail().getOrderAmountD()
				+ "");
		tvSn.setText(shopcarData.getInfo().getOrderDetail().getOrderSn());
		tvName.setText(shopcarData.getInfo().getOrderDetail().getConsignee());
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
		return "订单信息编辑";
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.complete_order_confirm_sure:
			/**
			 * 修改订单信息
			 */

			getUpdateList();
			updateShopcar(true);

			break;
		}
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
			// boolean isFirshOrder = adapter.isFirstOrderList.get(i);
			// int isFirst = 0;
			// if (isFirshOrder) {
			// isFirst = 1;
			// } else {
			// isFirst = 0;
			// }
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
			int ordeCartId = shopcarList.get(i).getOrderProductId();
			UpdateShopcarParam param = new UpdateShopcarParam();
			param.setOrdeCartId(ordeCartId);
			param.setOneCode(oneCode);
			param.setRemark(remark);
			param.setRemarkTag(remarkTag);
			// param.setIsFirst(isFirst);
			MsApplication.updateShopcarParam.add(param);
		}
		LogUtil.d(TAG, "更新购物车数据" + MsApplication.updateShopcarParam.toString());
	}

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
						// MsToast.makeText(baseContext, "请求成功").show();
						// Intent intent = new Intent(baseActivity,
						// HomeActivity.class);
						// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						// MsStartActivity.startActivity(baseActivity, intent);
						onBackPressed();
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
		setResult(MsConfiguration.REQUEST_FROM_SEND_SHOP);
		super.onBackPressed();
	}

}
