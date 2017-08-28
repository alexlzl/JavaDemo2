package com.minsheng.app.module.appointmentorder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.AddShopCarEntity;
import com.minsheng.app.entity.CompleteOrderListEntity;
import com.minsheng.app.entity.CompleteOrderListEntity.Infor.CategoryListInfor;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.appointmentorder.CompleteOrderChildAdapter.ChildCheckChange;
import com.minsheng.app.module.appointmentorder.CompleteOrderParentAdapter.CategoryChange;
import com.minsheng.app.module.appointmentorder.ShopCarAdapter.SelectChangeListener;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;

/**
 * 
 * @describe:完善订单页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-3下午7:37:53
 * 
 */
public class CompleteOrder extends BaseActivity implements CategoryChange,
		ChildCheckChange, SelectChangeListener {
	private static final String TAG = "CompleteOrder";
	private ListView lvParent, lvChild;
	private CompleteOrderParentAdapter parentAdapter;
	private CompleteOrderChildAdapter childAdapter;
	private ShopCarAdapter shopCarAdapter;
	private ImageView ivShopcar;
	private PopupWindow popupWindow = null;
	public static RelativeLayout rlShopcar;
	private ListView lvShopcar;
	private Animation animationIn, animationOUt;
	private Button confirmAdd;
	private CompleteOrderListEntity completeOrderObj;
	private int businessId;
	private List<CategoryListInfor> categoryList;
	private int currentPosition;
	private TextView tvTotalNum;
	private int orderId;
	private AddShopCarEntity shopCarObj;
	private int washState;
	private boolean isBack;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.complete_order);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		washState = getIntent().getIntExtra(MsConfiguration.ORDER_STATE_KEY, 0);
		businessId = getIntent()
				.getIntExtra(MsConfiguration.BUSINESS_ID_KEY, 0);
		orderId = getIntent().getIntExtra(MsConfiguration.ORDER_ID_KEY, 0);
		LogUtil.d(TAG, "washState==" + washState + "businessId==" + businessId
				+ "orderId==" + orderId);
		lvParent = (ListView) findViewById(R.id.complete_order_parent_lv);
		lvChild = (ListView) findViewById(R.id.complete_order_child_lv);
		ivShopcar = (ImageView) findViewById(R.id.complete_order_shopcar_icon);
		rlShopcar = (RelativeLayout) findViewById(R.id.complete_order_shopcar);
		confirmAdd = (Button) findViewById(R.id.complete_order_confirm_add);
		tvTotalNum = (TextView) findViewById(R.id.complete_order_total_num);
		rlShopcar.getBackground().setAlpha(125);
		rlShopcar.setTag(false);
		lvShopcar = (ListView) findViewById(R.id.shopcar_lv);
		animationIn = AnimationUtils.loadAnimation(baseContext,
				R.anim.scroll_up);
		animationOUt = AnimationUtils.loadAnimation(baseContext,
				R.anim.scroll_out);

	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		if (!isBack) {
			showRoundProcessDialog();
		}

		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("businessId", businessId);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.COMPLETE_ORDER_LIST,
				new BaseJsonHttpResponseHandler<CompleteOrderListEntity>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3,
							CompleteOrderListEntity arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						if (!isBack) {
							dismissRoundProcessDialog();
						}

						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerCompleteOrder.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							CompleteOrderListEntity arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						if (!isBack) {
							dismissRoundProcessDialog();
						}

					}

					@Override
					protected CompleteOrderListEntity parseResponse(
							String arg0, boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						if (!isBack) {
							dismissRoundProcessDialog();
						}

						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							completeOrderObj = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									CompleteOrderListEntity.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerCompleteOrder.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerCompleteOrder.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return completeOrderObj;
					}
				});

	}

	Handler handlerCompleteOrder = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (completeOrderObj != null && completeOrderObj.getCode() == 0) {
					categoryList = completeOrderObj.getInfo()
							.getCategoryProducts();
					parentAdapter = new CompleteOrderParentAdapter(
							categoryList, lvParent, baseContext);
					lvParent.setAdapter(parentAdapter);
					childAdapter = new CompleteOrderChildAdapter(categoryList
							.get(0).getWashProductLists(), lvChild,
							baseContext, clothesHandler);
					lvChild.setAdapter(childAdapter);
					parentAdapter.setCheckListener(CompleteOrder.this);
					childAdapter.setCheckListener(CompleteOrder.this);
					updateClothesNum();
				} else {
					if (completeOrderObj != null) {
						MsToast.makeText(baseContext, "获取数据失败").show();
					} else {
						MsToast.makeText(baseContext, completeOrderObj.getMsg())
								.show();
					}
				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "获取数据失败").show();
				break;
			}
		}

	};

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		ivShopcar.setOnClickListener(this);
		rlShopcar.setOnClickListener(this);
		confirmAdd.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		case R.id.complete_order_shopcar_icon:
			/**
			 * 购物车显示隐藏控件,点击事件
			 */

			if (MsApplication.selectList != null
					&& MsApplication.selectList.size() > 0) {
				/**
				 * 存在历史选择数据
				 */

				shopCarAdapter = new ShopCarAdapter(MsApplication.selectList,
						baseContext, clothesHandler);
				lvShopcar.setAdapter(shopCarAdapter);
				shopCarAdapter.setSelectListerner(this);
				if (!(Boolean) rlShopcar.getTag()) {
					rlShopcar.setVisibility(View.VISIBLE);
					rlShopcar.setTag(true);
					rlShopcar.startAnimation(animationIn);
				} else {
					rlShopcar.setVisibility(View.GONE);
					rlShopcar.setTag(false);
					rlShopcar.startAnimation(animationOUt);
				}

			} else {
				// MsToast.makeText(baseContext, "请先向购物车添加商品").show();
				Toast.makeText(baseContext, "请先向购物车添加商品", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.complete_order_shopcar:
			/**
			 * 购物车背景点击事件，隐藏购物车
			 */
			rlShopcar.setVisibility(View.GONE);
			rlShopcar.setTag(false);
			rlShopcar.startAnimation(animationOUt);
			break;
		case R.id.complete_order_confirm_add:
			/**
			 * 确认添加
			 */
			if (isBack) {
				/**
				 * 如果是从购物车返回，可以直接进入购物车
				 */
				addShopCar();
			} else {
				/**
				 * 如果不是从购物车返回需要先选中衣服
				 */
				if (MsApplication.selectList != null
						&& MsApplication.selectList.size() > 0) {
					addShopCar();
				} else {
					// MsToast.makeText(baseContext, "请先选择衣服").show();
					Toast.makeText(baseContext, "请先选择衣服", Toast.LENGTH_SHORT)
							.show();
				}
			}

			break;
		}
	}

	@SuppressWarnings("unused")
	private void showPopUp(View content, View position) {

		popupWindow = new PopupWindow(content, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		popupWindow.getBackground().setAlpha(127);
		int[] location = new int[2];
		position.getLocationOnScreen(location);
		popupWindow.showAtLocation(position, Gravity.NO_GRAVITY, location[0],
				location[1] + popupWindow.getHeight());
		RelativeLayout layout = (RelativeLayout) content
				.findViewById(R.id.shopcar_parent);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
	}

	/**
	 * 切换父类位置回调
	 */
	@Override
	public void checkChange(int position) {
		// TODO Auto-generated method stub
		currentPosition = position;
		childAdapter = new CompleteOrderChildAdapter(categoryList.get(position)
				.getWashProductLists(), lvChild, baseContext, clothesHandler);
		lvChild.setAdapter(childAdapter);
	}

	@Override
	public void childCheckChange(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shopcarAmountChage() {
		// TODO Auto-generated method stub

		LogUtil.d(TAG, "刷新child数据");
		childAdapter = new CompleteOrderChildAdapter(categoryList.get(
				currentPosition).getWashProductLists(), lvChild, baseContext,
				clothesHandler);
		lvChild.setAdapter(childAdapter);
		/**
		 * 刷新购物车
		 */
		shopCarAdapter = new ShopCarAdapter(MsApplication.selectList,
				baseContext, clothesHandler);
		lvShopcar.setAdapter(shopCarAdapter);
		shopCarAdapter.setSelectListerner(this);
	}

	/**
	 * 处理更新购物车数量消息
	 */
	Handler clothesHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			updateClothesNum();

		}

	};

	/**
	 * 
	 * 
	 * @describe:更新购物车衣服数量
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-23下午3:26:45
	 * 
	 */
	private void updateClothesNum() {
		int num = 0;
		if (MsApplication.selectList != null
				&& MsApplication.selectList.size() > 0) {

			for (int i = 0; i < MsApplication.selectList.size(); i++) {
				num = num + MsApplication.selectList.get(i).getProductAmount();
			}
			tvTotalNum.setText(num + "");
		} else {
			tvTotalNum.setText(0 + "");
		}
	}

	/**
	 * 
	 * 
	 * @describe:添加购物车
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-28下午5:41:08
	 * 
	 */
	private void addShopCar() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderId", orderId);
		map.put("orderCarts", MsApplication.selectList);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.ADD_SHOP_CAR,
				new BaseJsonHttpResponseHandler<AddShopCarEntity>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, AddShopCarEntity arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerAddShopcar.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							AddShopCarEntity arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						dismissRoundProcessDialog();
					}

					@Override
					protected AddShopCarEntity parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							shopCarObj = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									AddShopCarEntity.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerAddShopcar.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerAddShopcar.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return shopCarObj;
					}
				});
	}

	/**
	 * 处理加入购物车消息返回
	 */
	Handler handlerAddShopcar = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:

				if (shopCarObj != null && shopCarObj.getCode() == 0) {
					MsApplication.selectList.clear();
					Intent intent = new Intent(baseActivity,
							CompleteOrderConfirm.class);
					intent.putExtra("shopcarlist", shopCarObj);
					intent.putExtra("orderId", orderId);
					intent.putExtra("washState", washState);
					MsStartActivity.startActivityForResult(baseActivity,
							intent, MsConfiguration.REQUEST_FROM_CONFIRM_ORDER);

				} else {
					if (shopCarObj != null) {
						MsToast.makeText(baseContext, shopCarObj.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "加入购物车失败").show();
					}

				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "加入购物车失败").show();
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, mIntent);
		if (MsConfiguration.REQUEST_FROM_CONFIRM_ORDER == requestCode) {
			currentPosition = 0;
			// ivShopcar.setOnClickListener(this);
			// rlShopcar.setOnClickListener(this);
			// confirmAdd.setOnClickListener(this);
			isBack = true;
			getNetData();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		/**
		 * 返回后清除当前选择记录
		 */
		if (MsApplication.selectList != null
				&& MsApplication.selectList.size() > 0) {
			MsApplication.selectList.clear();
		}

	}

}
