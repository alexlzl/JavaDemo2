package com.minsheng.app.module.sendshoporder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.ConfirmClothesInforParam;
import com.minsheng.app.entity.ModifyState;
import com.minsheng.app.entity.OrderDetail;
import com.minsheng.app.entity.OrderDetail.Infor.Detail.OrderList;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.main.HomeActivity;
import com.minsheng.app.scan_one.CameraActivity;
import com.minsheng.app.scan_one.CameraActivity.ScanCallbackOne;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.TimeUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:订单信息匹配页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-14下午2:26:49
 * 
 */
public class OrderInforMatch extends BaseActivity implements ScanCallbackOne {
	private static final String TAG = "OrderInforMatch";
	private ListView lv;
	private OrderInforMatchAdapter adapter;
	private View headView;
	private ImageView ivCheckTime;
	private LinearLayout llChckeTimeParent;
	private TextView tvCheckDate;
	private int orderId;
	private Button btConfirm;
	private ModifyState confirmInforBean;
	private ImageView ivHead;
	private TextView tvName;
	private TextView tvOrderSn;
	private EditText etInputOrderSn;
	private OrderDetail orderDetailInfor;
	private List<OrderList> orderList;
	public static ConfirmInforListener mlistener;
	public ArrayList<ConfirmClothesInforParam> clothesInforList = new ArrayList<ConfirmClothesInforParam>();
	private Calendar c = null;
	private Dialog dialog = null;
	private int checkTime;
	private int washStatus;
	private ImageView ivScanOrderSn;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.orderinfor_match);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		lv = (ListView) findViewById(R.id.orderinfor_match_lv);
		headView = baseLayoutInflater.inflate(R.layout.orderinfor_match_head,
				null);
		ivScanOrderSn = (ImageView) headView
				.findViewById(R.id.orderinfor_match_head_scan);
		ivCheckTime = (ImageView) findViewById(R.id.orderinfor_match_checktime_iv);
		llChckeTimeParent = (LinearLayout) findViewById(R.id.orderinfor_match_checktime_parent);
		tvCheckDate = (TextView) findViewById(R.id.orderinfor_match_checkdate);
		orderId = getIntent().getIntExtra(MsConfiguration.ORDER_ID_KEY, 0);
		washStatus = getIntent().getIntExtra("washStatus", 0);
		btConfirm = (Button) findViewById(R.id.orderinfor_match_confirm);
		ivHead = (ImageView) headView
				.findViewById(R.id.orderinfor_match_head_usericon);
		tvName = (TextView) headView
				.findViewById(R.id.orderinfor_match_head_name);
		tvOrderSn = (TextView) headView
				.findViewById(R.id.orderinfor_match_head_ordersn);
		etInputOrderSn = (EditText) headView
				.findViewById(R.id.orderinfor_match_head_ordersn_shop);
	}

	/**
	 * 获取订单详情
	 */
	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.ORDER_DETAIL,
				new BaseJsonHttpResponseHandler<OrderDetail>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, OrderDetail arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerDetailInfor.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							OrderDetail arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						dismissRoundProcessDialog();
					}

					@Override
					protected OrderDetail parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							orderDetailInfor = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									OrderDetail.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerDetailInfor.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerDetailInfor.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return orderDetailInfor;
					}
				});
	}

	/**
	 * 处理订单详情消息返回
	 */
	Handler handlerDetailInfor = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (orderDetailInfor != null && orderDetailInfor.getCode() == 0) {
					setViewData();
				} else {
					if (orderDetailInfor != null) {
						MsToast.makeText(baseContext, orderDetailInfor.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "获取数据失败").show();
					}
				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "获取数据失败").show();
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
	 * @time:2015-3-26下午3:18:54
	 * 
	 */
	private void setViewData() {
		orderList = orderDetailInfor.getInfo().getOrderDetail()
				.getDeliveryProductDetails();
		adapter = new OrderInforMatchAdapter(orderList, lv, baseContext);
		lv.addHeaderView(headView);
		lv.setAdapter(adapter);
		tvName.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getConsignee());
		tvOrderSn.setText(orderDetailInfor.getInfo().getOrderDetail()
				.getOrderSn());

		MsApplication.imageLoader.displayImage(orderDetailInfor.getInfo()
				.getOrderDetail().getHeadPic(), ivHead, MsApplication.options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

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
		ivCheckTime.setOnClickListener(this);
		llChckeTimeParent.setOnClickListener(this);
		btConfirm.setOnClickListener(this);
		tvRight.setOnClickListener(this);
		ivScanOrderSn.setOnClickListener(this);
	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "订单信息匹配";
	}

	@Override
	protected String setRightText() {
		// TODO Auto-generated method stub
		return "编辑";
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
		case R.id.orderinfor_match_checktime_iv:
			showTimeCheckWindow();
			break;

		case R.id.orderinfor_match_checktime_parent:
			showTimeCheckWindow();
			break;

		case R.id.orderinfor_match_confirm:

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
					/**
					 * 提交送店衣物信息
					 */
					if (StringUtil.isEmpty(etInputOrderSn.getText().toString())) {
						MsToast.makeText(baseContext, "请输入订单标识码").show();
						return;
					} else {
						updateClothesInfor();
					}
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
		case R.id.base_activity_title_right_righttv:
			/**
			 * 进入编辑页面
			 */
			Intent intent = new Intent(baseActivity, OrderModified.class);
			intent.putExtra("shopcarlist", orderDetailInfor);
			intent.putExtra("orderId", orderId);
			intent.putExtra("washState", washStatus);
			MsStartActivity.startActivityForResult(baseActivity, intent,
					MsConfiguration.REQUEST_FROM_SEND_SHOP);

			break;
		case R.id.orderinfor_match_head_scan:
			/**
			 * 扫描订单号
			 */
			CameraActivity.setScanOneListener(this);
			Intent intentScan = new Intent(baseActivity, CameraActivity.class);
			intentScan.putExtra(MsConfiguration.FROM_WHERE_TO_SCAN,
					MsConfiguration.FROM_MODIFY_ORDER_INFOR);
			MsStartActivity.startActivity(baseActivity, intentScan);
			break;
		}
	}

	/**
	 * 
	 * 
	 * @describe:检测衣服是否绑定标示码
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-26下午3:20:21
	 * 
	 */
	private void updateClothesInfor() {
		ArrayList<String> inputContent = adapter.inputContent;
		int size = inputContent.size();
		boolean isEmpty = false;
		for (int i = 0; i < size; i++) {
			String content = inputContent.get(i);
			if (StringUtil.isEmpty(content)) {
				MsToast.makeText(this, "请输入完整衣服标识码").show();
				isEmpty = true;
				return;
			}
		}
		if (checkTime == 0) {
			MsToast.makeText(this, "请选择取衣时间").show();
			return;
		}
		if (checkTime * 1000L < System.currentTimeMillis()) {
			MsToast.makeText(this, "请选择正确的取衣时间").show();
			return;
		}
		if (!isEmpty) {
			for (int i = 0; i < size; i++) {
				ConfirmClothesInforParam param = new ConfirmClothesInforParam();
				param.setMerchantOneCode(inputContent.get(i));
				param.setOrderProductId(orderList.get(i).getOrderProductId());
				clothesInforList.add(param);
			}
			confirmClothesInfor();
		}
	}

	/**
	 * 
	 * 
	 * @describe:提交衣服确认信息
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-26下午3:19:42
	 * 
	 */
	private void confirmClothesInfor() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		map.put("deliveryProductDetails", clothesInforList);
		map.put("finishTime", checkTime);
		map.put("orderNumber", etInputOrderSn.getText().toString());
		map.put("washStatus", washStatus);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.SENDSHOP_CONFIRM_CLOTHES_INFOR,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerConfirmClothesInfor.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ModifyState arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						dismissRoundProcessDialog();
					}

					@Override
					protected ModifyState parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							confirmInforBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerConfirmClothesInfor.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerConfirmClothesInfor.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return confirmInforBean;
					}
				});
	}

	/**
	 * 
	 * 
	 * @describe:显示洗衣完成时间
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-20上午10:45:26
	 * 
	 */
	private void showTimeCheckWindow() {
		c = Calendar.getInstance();
		dialog = new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				// 获取时间戳
				checkTime = TimeUtil.getTimeStemp(arg1 + "-" + (arg2 + 1) + "-"
						+ arg3);

				LogUtil.d(TAG, TimeUtil.changeTimeStempToDate(checkTime));
				tvCheckDate.setText(arg1 + "年" + (arg2 + 1) + "月" + arg3 + "日");
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
				.get(Calendar.DAY_OF_MONTH));
		dialog.show();

	}

	/**
	 * 确认洗衣信息消息返回
	 */

	Handler handlerConfirmClothesInfor = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:

				if (confirmInforBean != null && confirmInforBean.getCode() == 0) {
					MsStartActivity.startActivity(baseActivity,
							HomeActivity.class);
				} else {
					if (confirmInforBean != null) {
						MsToast.makeText(baseContext, confirmInforBean.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "提交失败").show();
					}

				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "提交失败").show();
				break;
			}
		}

	};

	public interface ConfirmInforListener {
		public void confirmListener();
	}

	public static void setConfirmListener(ConfirmInforListener listener) {
		mlistener = listener;
	}

	@Override
	public void setScanResult(String result) {
		// TODO Auto-generated method stub
		etInputOrderSn.setText(result);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, mIntent);
		if (MsConfiguration.REQUEST_FROM_SEND_SHOP == requestCode) {
			// getNetData();
		}
	}
}
