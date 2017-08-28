package com.minsheng.app.module.main;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.animation.Rotate3dAnimation;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.application.MsApplication.BackHomepageCallback;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.Homepage;
import com.minsheng.app.entity.ModifyState;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.module.againorder.WashAgain;
import com.minsheng.app.module.appointmentorder.AppointmentOrder;
import com.minsheng.app.module.cancelorder.CancelOrder;
import com.minsheng.app.module.finishorder.FinishOrder;
import com.minsheng.app.module.getclothesorder.GetClothesOrder;
import com.minsheng.app.module.pushservice.PushService;
import com.minsheng.app.module.search.SearchOrderList;
import com.minsheng.app.module.sendclothesbackorder.SendClothesBackOrder;
import com.minsheng.app.module.sendshoporder.SendShopOrder;
import com.minsheng.app.module.usercenter.MyMessage;
import com.minsheng.app.module.usercenter.MyMessage.MessageStateChange;
import com.minsheng.app.module.usercenter.MyOrderList;
import com.minsheng.app.module.usercenter.MyUserEvaluationNew;
import com.minsheng.app.module.usercenter.UserInfor;
import com.minsheng.app.scan_one.CameraActivity;
import com.minsheng.app.util.AppManager;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.app.view.CommonDialog;
import com.minsheng.app.view.MsToast;
import com.minsheng.app.view.CommonDialog.OnDialogListener;
import com.minsheng.app.view.ConfirmDialog.OnConfirmListener;
import com.minsheng.wash.R;

/**
 * 
 * @describe:主页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-2-27下午3:37:06
 * 
 */
public class HomeActivity extends BaseActivity implements
		SeekBar.OnSeekBarChangeListener, MessageStateChange,
		BackHomepageCallback {
	private static final String TAG = "HomeActivity";
	/**
	 * 定位
	 */
	public LocationClient mLocationClient = null;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "bd09ll";
	private static SeekBar seekBar;
	/**
	 * 滑动条宽高
	 */
	private int width = 115;
	private int height = 115;
	private int lastProgress = 0;
	private int newProgress = 0;
	private String functionType;
	private TextView tvCarCapacity;
	private RelativeLayout rlAppointorder, rlSendShop, rlGetClothes,
			rlSendBackClothes, rlFinishOrder, rlWashAgain, rlCancle;
	private Button btMessageIcon;
	private static Button btMessageNum;
	private Button btMenu;
	private RelativeLayout rlTitle;
	private ImageView ivSearchIcon;
	private TextView tvSearch;
	private PopupWindow mPopupWindow = null;
	private EditText etSearch;
	private static TextView tvState;
	private static Homepage homePageData;
	private static Button btAppointNotReadNum;
	private static Button btSendShopNotReadNum;
	private static Button btGetClothesNotReadNum;
	private static Button btSendClothesNotReadNum;
	private static Button btFinishNotReadNum;
	private static Button btWashAgainNotReadNum;
	private static Button btCancleNotReadNum;
	private static TextView tvApponintTotalNum;
	private static TextView tvSendShopTotalNum;
	private static TextView tvGetClothesTotalNum;
	private static TextView tvFinishTotalNum;
	private static TextView tvSendClothesTotalNum;
	private static TextView tvWashAgainTotalNum;
	private static TextView tvCancleTotalNum;
	private static int courierState;
	private static int carCapacity;
	private ModifyState modifyStateBean;
	private int seekBarProgressBefor;
	private boolean isChange;
	// 点击切换状态动画配置
	int mDuration = 1500;
	float mCenterX = 0.0f;
	float mCenterY = 0.0f;
	float mDepthZ = 0.0f;
	int mIndex = 0;
	private boolean isFirsh = true;
	private ImageView ivScan;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.homepage);
		mLocationClient = ((MsApplication) getApplication()).mLocationClient;

		/**
		 * 启动定位
		 */
		initLocation();
		mLocationClient.start();
		/**
		 * 启动服务
		 */
		Intent intent = new Intent(baseContext, PushService.class);
		startService(intent);
		InnerReceiver msgReceiver = new InnerReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.min.musicdemo.action.NUM_COUNT");
		registerReceiver(msgReceiver, intentFilter);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		seekBar = (SeekBar) findViewById(R.id.homepage_seekbar);
		Drawable thumbDrawable = getNewDrawable(this, R.drawable.homepage_car,
				width, height);
		seekBar.setThumb(thumbDrawable);
		Drawable progressbarDrawable = getNewDrawable(this,
				R.drawable.homepage_car_bg, ViewUtil.dip2px(baseContext, 320),
				ViewUtil.dip2px(baseContext, 20));
		seekBar.setProgressDrawable(progressbarDrawable);
		tvCarCapacity = (TextView) findViewById(R.id.homepage_carcapacity_num);
		rlAppointorder = (RelativeLayout) findViewById(R.id.homepage_appointorder_item);
		btMessageIcon = (Button) findViewById(R.id.homepage_message_icon);
		btMenu = (Button) findViewById(R.id.homepage_menu);
		rlTitle = (RelativeLayout) findViewById(R.id.homepage_title);
		rlSendShop = (RelativeLayout) findViewById(R.id.homepage_sendshop_parent);
		rlGetClothes = (RelativeLayout) findViewById(R.id.homepage_getclothes_order_parent);
		rlSendBackClothes = (RelativeLayout) findViewById(R.id.homepage_sendclothesback_order_parent);
		rlFinishOrder = (RelativeLayout) findViewById(R.id.homepage_finish_order);
		rlWashAgain = (RelativeLayout) findViewById(R.id.homepage_wash_again);
		rlCancle = (RelativeLayout) findViewById(R.id.homepage_wash_cancle);
		ivSearchIcon = (ImageView) findViewById(R.id.homepage_search_icon);
		tvSearch = (TextView) findViewById(R.id.homepage_search_tv);
		btMessageNum = (Button) findViewById(R.id.homepage_message_num);
		etSearch = (EditText) findViewById(R.id.homepage_search_et);
		tvState = (TextView) findViewById(R.id.homepage_state_tv);
		/**
		 * 数量显示视图
		 */
		btAppointNotReadNum = (Button) findViewById(R.id.homepage_appointorder_notread_num);
		tvApponintTotalNum = (TextView) findViewById(R.id.homepage_appointorder_totalnum);
		btSendShopNotReadNum = (Button) findViewById(R.id.homepage_sendshop_notread_num);
		tvSendShopTotalNum = (TextView) findViewById(R.id.homepage_sendshop_total_num);
		btGetClothesNotReadNum = (Button) findViewById(R.id.homepage_getback_notread_num);
		tvGetClothesTotalNum = (TextView) findViewById(R.id.homepage_getback_total_num);
		btSendClothesNotReadNum = (Button) findViewById(R.id.homepage_sendclothes_notread_num);
		tvSendClothesTotalNum = (TextView) findViewById(R.id.homepage_sendclothes_total_num);
		btFinishNotReadNum = (Button) findViewById(R.id.homepage_finishorder_notread_num);
		tvFinishTotalNum = (TextView) findViewById(R.id.homepage_finishorder_total_num);
		btWashAgainNotReadNum = (Button) findViewById(R.id.homepage_washagain_notread_num);
		tvWashAgainTotalNum = (TextView) findViewById(R.id.homepage_washagain_total_num);
		btCancleNotReadNum = (Button) findViewById(R.id.homepage_cancle_notread_num);
		tvCancleTotalNum = (TextView) findViewById(R.id.homepage_cancle_total_num);
		ivScan = (ImageView) findViewById(R.id.homepage_scan_search);
	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		getHomepageData(map, MsRequestConfiguration.HOME_PAGE,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME, true);
	}

	/**
	 * 
	 * 
	 * @describe:首页数据请求
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-17下午6:04:34
	 * 
	 */
	public static void getHomepageData(Map<Object, Object> map, String url,
			String className, final boolean isShow) {
		if (isShow) {
			showRoundProcessDialog();
		}

		RequestParams paramsIn = new RequestParams();
		paramsIn = MsApplication.getRequestParams(map, paramsIn, className);
		BasicHttpClient.getInstance().post(baseContext, paramsIn, url,
				new BaseJsonHttpResponseHandler<Homepage>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, Homepage arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						if (isShow) {
							dismissRoundProcessDialog();
						}

						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerHomepage.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							Homepage arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						if (isShow) {
							dismissRoundProcessDialog();
						}

					}

					@Override
					protected Homepage parseResponse(String arg0, boolean arg1)
							throws Throwable {
						// TODO Auto-generated method stub
						if (isShow) {
							dismissRoundProcessDialog();
						}

						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							homePageData = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									Homepage.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerHomepage.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerHomepage.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return homePageData;
					}
				});
	}

	/**
	 * 处理首页数据返回
	 */
	static Handler handlerHomepage = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (homePageData != null && homePageData.getCode() == 0) {
					/**
					 * 数据返回非空
					 */
					MsApplication.isFirstData = false;
					setViewData();
				} else {
					if (homePageData != null) {
						MsToast.makeText(baseContext, homePageData.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "获取数据失败").show();
					}
				}
				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "获取数据失败").show();
				MsApplication.isFirstData = false;
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:绑定视图数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-17下午6:20:48
	 * 
	 */
	public static void setViewData() {
		/**
		 * 数量视图显示控制
		 */
		LogUtil.d(TAG, "setViewData");
		if (homePageData.getInfo().getNoCheckMsgNum() == 0) {
			btMessageNum.setVisibility(View.GONE);
		} else {
			if (homePageData != null && homePageData.getInfo() != null) {
				btMessageNum.setText(homePageData.getInfo().getNoCheckMsgNum()
						+ "");
				btMessageNum.setVisibility(View.VISIBLE);
			}

		}
		if (homePageData.getInfo().getNoCheckMakeNum() == 0) {
			// MsApplication.appointNotRead = 0;
			btAppointNotReadNum.setVisibility(View.GONE);
		} else {
			btAppointNotReadNum.setText(homePageData.getInfo()
					.getNoCheckMakeNum() + "");
			btAppointNotReadNum.setVisibility(View.VISIBLE);
			MsApplication.appointNotRead = homePageData.getInfo()
					.getNoCheckMakeNum();
			LogUtil.d(TAG, "未读预约订单" + MsApplication.appointNotRead);
		}
		if (homePageData.getInfo().getMakeOrderNum() == 0) {
			tvApponintTotalNum.setVisibility(View.GONE);
		} else {
			tvApponintTotalNum.setText(homePageData.getInfo().getMakeOrderNum()
					+ "");
			tvApponintTotalNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getNoCheckGiveNum() == 0) {
			btSendShopNotReadNum.setVisibility(View.GONE);
		} else {
			btSendShopNotReadNum.setText(homePageData.getInfo()
					.getNoCheckGiveNum() + "");
			btSendShopNotReadNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getGiveOrderNum() == 0) {
			tvSendShopTotalNum.setVisibility(View.GONE);
		} else {
			tvSendShopTotalNum.setText(homePageData.getInfo().getGiveOrderNum()
					+ "");
			tvSendShopTotalNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getNoCheckGetNum() == 0) {
			btGetClothesNotReadNum.setVisibility(View.GONE);
		} else {
			btGetClothesNotReadNum.setText(homePageData.getInfo()
					.getNoCheckGetNum() + "");
			btGetClothesNotReadNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getGetOrderNum() == 0) {
			tvGetClothesTotalNum.setVisibility(View.GONE);
		} else {
			tvGetClothesTotalNum.setText(homePageData.getInfo()
					.getGetOrderNum() + "");
			tvGetClothesTotalNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getNoCheckGiveClothNum() == 0) {
			btSendClothesNotReadNum.setVisibility(View.GONE);
		} else {
			btSendClothesNotReadNum.setText(homePageData.getInfo()
					.getNoCheckGiveClothNum() + "");
			btSendClothesNotReadNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getGiveClothNum() == 0) {
			tvSendClothesTotalNum.setVisibility(View.GONE);
		} else {
			tvSendClothesTotalNum.setText(homePageData.getInfo()
					.getGiveClothNum() + "");
			tvSendClothesTotalNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getNoCheckFinishNum() == 0) {
			btFinishNotReadNum.setVisibility(View.GONE);

		} else {
			btFinishNotReadNum.setText(homePageData.getInfo()
					.getNoCheckFinishNum() + "");
			btFinishNotReadNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getFinishOrderNum() == 0) {
			tvFinishTotalNum.setVisibility(View.GONE);

		} else {
			tvFinishTotalNum.setText(homePageData.getInfo().getFinishOrderNum()
					+ "");
			tvFinishTotalNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getNoCheckRepeatNum() == 0) {
			btWashAgainNotReadNum.setVisibility(View.GONE);

		} else {
			btWashAgainNotReadNum.setText(homePageData.getInfo()
					.getNoCheckRepeatNum() + "");
			btWashAgainNotReadNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getRepeatOrderNum() == 0) {
			tvWashAgainTotalNum.setVisibility(View.GONE);

		} else {
			tvWashAgainTotalNum.setText(homePageData.getInfo()
					.getRepeatOrderNum() + "");
			tvWashAgainTotalNum.setVisibility(View.VISIBLE);
		}
		/**
		 * 取消订单数据========
		 */
		if (homePageData.getInfo().getNoCheckRepeatNum() == 0) {
			btCancleNotReadNum.setVisibility(View.GONE);

		} else {
			btCancleNotReadNum.setText(homePageData.getInfo()
					.getNoCheckRepeatNum() + "");
			btCancleNotReadNum.setVisibility(View.VISIBLE);
		}
		if (homePageData.getInfo().getRepeatOrderNum() == 0) {
			tvCancleTotalNum.setVisibility(View.GONE);

		} else {
			tvCancleTotalNum.setText(homePageData.getInfo()
					.getRepeatOrderNum() + "");
			tvCancleTotalNum.setVisibility(View.VISIBLE);
		}
		/**
		 * 快递员状态
		 */
		courierState = homePageData.getInfo().getWdStatus();
		LogUtil.d(TAG, "快递员状态" + courierState);
		switch (courierState) {
		case 0:
			/**
			 * 休息状态
			 */
			tvState.setBackgroundResource(R.drawable.rest_bg);
			tvState.setTextColor(baseContext.getResources().getColor(
					R.color.colorfbb400));
			tvState.setText("休");
			break;
		case 1:
			/**
			 * 空闲状态
			 */
			tvState.setBackgroundResource(R.drawable.free_bg);
			tvState.setTextColor(baseContext.getResources().getColor(
					R.color.color8cc63f));
			tvState.setText("闲");
			break;
		case 2:
			/**
			 * 忙的状态
			 */
			tvState.setBackgroundResource(R.drawable.busy_bg);
			tvState.setTextColor(baseContext.getResources().getColor(
					R.color.colored1c24));
			tvState.setText("忙");
			break;
		default:
			/**
			 * 默认处理
			 */
			tvState.setBackgroundResource(R.drawable.rest_bg);
			tvState.setTextColor(baseContext.getResources().getColor(
					R.color.colorfbb400));
			tvState.setText("休");
			break;

		}
		/**
		 * 车载容量
		 */
		carCapacity = homePageData.getInfo().getCarCapacity();
		switch (carCapacity) {
		case 0:
			/**
			 * 空
			 */
			seekBar.setProgress(0);

			break;
		case 1:
			/**
			 * 50%
			 */
			seekBar.setProgress(50);
			break;
		case 2:
			/**
			 * 满了
			 */
			seekBar.setProgress(100);
			break;
		}

	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		seekBar.setOnSeekBarChangeListener(this);
		rlAppointorder.setOnClickListener(this);
		btMessageIcon.setOnClickListener(this);
		btMenu.setOnClickListener(this);
		rlSendShop.setOnClickListener(this);
		rlGetClothes.setOnClickListener(this);
		rlSendBackClothes.setOnClickListener(this);
		rlFinishOrder.setOnClickListener(this);
		rlWashAgain.setOnClickListener(this);
		rlCancle.setOnClickListener(this);
		ivSearchIcon.setOnClickListener(this);
		tvSearch.setOnClickListener(this);
		btMessageNum.setOnClickListener(this);
		tvState.setOnClickListener(this);
		MyMessage.setMessageChangeListener(this);
		MsApplication.setBackHomepageListener(this);
		ivScan.setOnClickListener(this);
	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return null;
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
		case R.id.homepage_appointorder_item:
			/**
			 * 预约订单
			 */
			// MsStartActivity.startActivity(baseActivity,
			// AppointmentOrder.class);
			MsStartActivity.startActivityForResult(baseActivity,
					AppointmentOrder.class,
					MsConfiguration.HOME_PAGE_TO_APPOINT);
			break;

		case R.id.homepage_message_icon:
			/**
			 * 消息中心
			 */

			MsStartActivity.startActivity(baseActivity, MyMessage.class);
			break;
		case R.id.homepage_menu:
			/**
			 * 个人中心
			 */
			PopupWindow popupwindow = showPopupWindow(baseActivity, rlTitle,
					R.layout.homepage_popupwindow);
			View view = popupwindow.getContentView();

			TextView tvInfor = (TextView) view
					.findViewById(R.id.homepage_popupwindow_userinfor);
			TextView tvOrder = (TextView) view
					.findViewById(R.id.homepage_popupwindow_order);
			TextView tvEvluation = (TextView) view
					.findViewById(R.id.homepage_popupwindow_evluation);
			tvInfor.getBackground().setAlpha(180);
			tvOrder.getBackground().setAlpha(180);
			tvEvluation.getBackground().setAlpha(180);
			tvInfor.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (mPopupWindow != null) {
						if (mPopupWindow.isShowing()) {
							mPopupWindow.dismiss();
						}
					}
					MsStartActivity
							.startActivity(baseActivity, UserInfor.class);
				}
			});
			tvOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (mPopupWindow != null) {
						if (mPopupWindow.isShowing()) {
							mPopupWindow.dismiss();
						}
					}

					MsStartActivity.startActivity(baseActivity,
							MyOrderList.class);
				}
			});
			tvEvluation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (mPopupWindow != null) {
						if (mPopupWindow.isShowing()) {
							mPopupWindow.dismiss();
						}
					}
					MsStartActivity.startActivity(baseActivity,
							MyUserEvaluationNew.class);
				}
			});

			break;
		case R.id.homepage_sendshop_parent:
			/**
			 * 送店订单
			 */
			MsStartActivity.startActivity(baseActivity, SendShopOrder.class);
			break;

		case R.id.homepage_getclothes_order_parent:
			/**
			 * 取衣订单
			 */
			MsStartActivity.startActivity(baseActivity, GetClothesOrder.class);
			break;

		case R.id.homepage_sendclothesback_order_parent:
			/**
			 * 送衣订单
			 */
			MsStartActivity.startActivity(baseActivity,
					SendClothesBackOrder.class);
			break;

		case R.id.homepage_finish_order:
			/**
			 * 当月完成订单
			 */
			MsStartActivity.startActivity(baseActivity, FinishOrder.class);
			break;
		case R.id.homepage_wash_again:
			/**
			 * 重洗订单
			 */
			MsStartActivity.startActivity(baseActivity, WashAgain.class);
			break;
		case R.id.homepage_wash_cancle:
			/**
			 * 取消订单
			 */
			MsStartActivity.startActivity(baseActivity, CancelOrder.class);
			break;

		case R.id.homepage_scan_search:
			/**
			 * 二维码扫描搜索
			 */
			Intent intentScan = new Intent(baseActivity,
					CameraActivity.class);
			intentScan.putExtra(MsConfiguration.FROM_WHERE_TO_SCAN,
					MsConfiguration.FROM_QUERY_ORDER);
			MsStartActivity.startActivity(baseActivity,
					intentScan);
			break;

		case R.id.homepage_search_icon:
			/**
			 * 图标搜索页面事件
			 */
			String searchIcon = etSearch.getText().toString();
			if (!StringUtil.isEmpty(searchIcon)) {
				Intent intent = new Intent(baseActivity, SearchOrderList.class);
				intent.putExtra("searchContent", searchIcon);
				MsStartActivity.startActivity(baseActivity, intent);
			} else {
				MsToast.makeText(baseContext, "请输入搜索内容").show();
			}

			break;
		case R.id.homepage_search_tv:
			/**
			 * 文本控件搜索页面事件
			 */
			String searchContent = etSearch.getText().toString();
			if (!StringUtil.isEmpty(searchContent)) {
				Intent intent = new Intent(baseActivity, SearchOrderList.class);
				intent.putExtra("searchContent", searchContent);
				MsStartActivity.startActivity(baseActivity, intent);
			} else {
				MsToast.makeText(baseContext, "请输入搜索内容").show();
			}

			break;

		case R.id.homepage_message_num:
			/**
			 * 消息中心
			 */
			// MsStartActivity.startActivity(baseActivity, MyMessage.class);
			MsStartActivity.startActivityForResult(baseActivity,
					MyMessage.class, MsConfiguration.HOME_PAGE_TO_MESSAGE);
			break;
		case R.id.homepage_state_tv:
			/**
			 * 切换状态
			 */
			if (courierState == 0) {
				/**
				 * 休息状态下可以切换成闲
				 */
				View contentView = baseLayoutInflater.inflate(
						R.layout.dialog_view, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("确定要改变状态吗?");
				builder.setPositiveButton("确  定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								modifyCourierState();
							}
						});
				builder.setNeutralButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
							}
						});
				builder.setView(contentView);
				builder.show();

			}

			break;
		}

	}

	/**
	 * 
	 * 
	 * @describe:显示窗口
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-16下午2:11:34
	 * 
	 */
	public PopupWindow showPopupWindow(Activity activity, View position,
			int layoutId) {

		View mPopMenuView = null;
		mPopMenuView = LayoutInflater.from(activity).inflate(layoutId, null);

		mPopupWindow = new PopupWindow(mPopMenuView, ViewUtil.dip2px(
				baseContext, 135), LayoutParams.WRAP_CONTENT, false);
		mPopupWindow
				.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setAnimationStyle(R.style.WindowAnimation);
		mPopupWindow.showAsDropDown(position, 0, 0);
		mPopupWindow.update();
		return mPopupWindow;
	}

	/**
	 * 
	 * 
	 * @describe:初始化定位public void setScanSpan ( int ) //设置定时定位的时间间隔。单位ms说明：
	 * 
	 *                       当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。
	 *                       调用requestLocation(
	 *                       )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化
	 *                       ，就不会发起网络请求
	 *                       ，返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
	 *                       定时定位时，调用一次requestLocation，会定时监听到定位结果。
	 * 
	 *                       当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。
	 *                       每调用一次requestLocation( )，定位SDK会发起一次定位。请求定位与监听结果一一对应。
	 * 
	 *                       设定了定时定位后，可以热切换成一次定位，需要重新设置时间间隔小于1000（ms）即可。
	 *                       locationClient对象stop后
	 *                       ，将不再进行定位。如果设定了定时定位模式后，多次调用requestLocation
	 *                       （），则是每隔一段时间进行一次定位，同时额外的定位请求也会进行定位，但频率不会超过1秒一次。
	 * 
	 * 
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-4下午8:40:51
	 * 
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = MsConfiguration.LOCATION_INTERVAL;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		mLocationClient.setLocOption(option);
	}

	@Override
	public void onProgressChanged(SeekBar mseekBar, int progress, boolean arg2) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG, "onProgressChanged");
		if (isChange) {
			seekBar.setProgress(seekBarProgressBefor);
			isChange = false;
			return;
		}
		if (isFirsh) {
			seekBar.setProgress(progress);
			tvCarCapacity.setText(getResources().getString(
					R.string.homepage_carcapacity)
					+ " : " + String.valueOf(seekBar.getProgress()) + "%");
			return;
		}
		if (progress > newProgress + 20 || progress < newProgress - 20) {
			newProgress = lastProgress;
			seekBar.setProgress(lastProgress);
			return;
		}

		newProgress = progress;

		tvCarCapacity.setText(getResources().getString(
				R.string.homepage_carcapacity)
				+ " : " + String.valueOf(seekBar.getProgress()) + "%");
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG, "onStartTrackingTouch");
		seekBarProgressBefor = arg0.getProgress();
		isFirsh = false;
	}

	/**
	 * 控制滑动位置
	 */
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG, "onStopTrackingTouch" + "newProgress==" + newProgress
				+ "lastProgress==" + lastProgress);
		if (newProgress == lastProgress) {
			return;
		}
		if ("proximitytag".equals(functionType) && newProgress < 30) {
			lastProgress = 0;
			newProgress = 0;
			seekBar.setProgress(0);

			changeCarCapacityState();
			return;
		}
		if ("findme".equals(functionType) && newProgress > 70) {
			lastProgress = 100;
			newProgress = 100;
			seekBar.setProgress(100);
			// modifyCarCapacityState();
			changeCarCapacityState();
			return;
		}
		if ("stop".equals(functionType) && newProgress > 30 && newProgress < 70) {
			lastProgress = 50;
			newProgress = 50;
			seekBar.setProgress(50);
			// modifyCarCapacityState();
			changeCarCapacityState();
			return;
		}
		if (newProgress < 30) {
			lastProgress = 0;
			newProgress = 0;
			seekBar.setProgress(0);
			// modifyCarCapacityState();
			changeCarCapacityState();
			functionType = "proximitytag";
		} else if (newProgress > 70) {
			// 设置lastProgress 要放在setProgress之前，否则可能导致执行多次onProgressChanged 改变了原值
			lastProgress = 100;
			newProgress = 100;
			seekBar.setProgress(100);
			// modifyCarCapacityState();
			changeCarCapacityState();
			functionType = "findme";

		} else {
			lastProgress = 50;
			newProgress = 50;
			seekBar.setProgress(50);
			// modifyCarCapacityState();
			changeCarCapacityState();
			functionType = "stop";

		}

	}

	/**
	 * 
	 * 
	 * @describe:更新车容量状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-30下午9:16:05
	 * 
	 */
	private void changeCarCapacityState() {
		final Dialog dialog = new AlertDialog.Builder(this, R.style.dialog)
				.create();
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.WindowAnimation);
		WindowManager.LayoutParams wm = window.getAttributes();
		wm.gravity = Gravity.CENTER;
		wm.width = ViewUtil.getScreenWith(this) * 11 / 12;
		dialog.setCancelable(false);
		dialog.show();
		dialog.setContentView(R.layout.confirm_dialog);
		TextView tvleft = (TextView) dialog
				.findViewById(R.id.confirm_dialog_left_tv);
		TextView tvRight = (TextView) dialog
				.findViewById(R.id.confirm_dialog_right_tv);
		TextView tvContent = (TextView) dialog
				.findViewById(R.id.confirm_dialog_content_tv);
		TextView tvTitle = (TextView) dialog
				.findViewById(R.id.confirm_dialog_title_tv);
		tvTitle.setText("温馨提示");
		tvleft.setText("确定");
		tvRight.setText("取消");
		tvContent.setText("你确定要更改车载容量吗?");
		tvleft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				modifyCarCapacityState();
			}
		});
		tvRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				isChange = true;
				seekBar.setProgress(seekBarProgressBefor);
				tvCarCapacity.setText(getResources().getString(
						R.string.homepage_carcapacity)
						+ " : " + String.valueOf(seekBar.getProgress()) + "%");
			}
		});
		// builder.setTitle("确定要更改容量吗?");
		// builder.setPositiveButton("确  定",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// modifyCarCapacityState();
		// }
		// });
		// builder.setNeutralButton("取消", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface arg0, int arg1) {
		// // TODO Auto-generated method stub
		// isChange = true;
		// seekBar.setProgress(seekBarProgressBefor);
		// tvCarCapacity.setText(getResources().getString(
		// R.string.homepage_carcapacity)
		// + " : " + String.valueOf(seekBar.getProgress()) + "%");
		// }
		// });
		// builder.setCancelable(false);
		// builder.setView(contentView);
		// builder.show();

		// showUpdateDialog(true, "温馨提示", "确定要更改车容量吗?");

	}

	private void showUpdateDialog(final boolean isForceUpdate, String title,
			String content) {
		LogUtil.d(TAG, "显示对话框");
		try {
			final CommonDialog commonDialog = CommonDialog.makeText(
					baseContext, title, content, new OnDialogListener() {

						@Override
						public void onResult(int result, CommonDialog v) {
							if (result == OnConfirmListener.LEFT) {
								v.cancel();
								modifyCarCapacityState();

							}

							if (result == OnConfirmListener.RIGHT) {
								if (isForceUpdate) {
									// 取消
									isChange = true;
									seekBar.setProgress(seekBarProgressBefor);
									tvCarCapacity
											.setText(getResources()
													.getString(
															R.string.homepage_carcapacity)
													+ " : "
													+ String.valueOf(seekBar
															.getProgress())
													+ "%");
									v.cancel();
								} else {
									// 取消
									v.cancel();
								}
							}
						}
					});

			if (isForceUpdate) {
				commonDialog.setCancelable(false);
			} else {
				commonDialog.setCancelable(true);
			}

			// 左侧按钮
			commonDialog.setLeftText("确定");
			// 右侧按钮
			if (isForceUpdate) {
				commonDialog.setRightText("取消");
			} else {
				commonDialog.setRightText("取消");
			}
			commonDialog.showDialog();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @describe:获取新的drawable
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-16下午3:55:31
	 * 
	 */
	private Drawable getNewDrawable(Activity context, int restId, int dstWidth,
			int dstHeight) {
		Bitmap Bmp = BitmapFactory.decodeResource(context.getResources(),
				restId);
		Bitmap bmp = Bmp.createScaledBitmap(Bmp, dstWidth, dstHeight, true);
		BitmapDrawable d = new BitmapDrawable(bmp);
		d.setTargetDensity(getApplicationContext().getResources()
				.getDisplayMetrics());
		return d;
	}

	/**
	 * 
	 * 
	 * @describe:修改车容量状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-19上午10:51:29
	 * 
	 */
	private void modifyCarCapacityState() {
		LogUtil.d(TAG, "滑动前进度" + seekBarProgressBefor);
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		int carCapacity = 0;
		switch (seekBar.getProgress()) {
		case 0:
			carCapacity = 0;
			break;
		case 50:
			carCapacity = 1;
			break;
		case 100:
			carCapacity = 2;
			break;
		}
		map.put("carCapacity", carCapacity);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.MODIFY_COURIER_STATE,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerModifyCarState.sendMessage(message);
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
							modifyStateBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerModifyCarState.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerModifyCarState.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return modifyStateBean;
					}
				});
	}

	/**
	 * 处理修改车容量状态消息返回
	 */
	Handler handlerModifyCarState = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			LogUtil.d(TAG, "滑动后进度" + seekBarProgressBefor);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (modifyStateBean != null && modifyStateBean.getCode() == 0) {
					/**
					 * 修改成功
					 */
					MsToast.makeText(baseContext, "修改成功").show();
				} else {
					/**
					 * 修改失败，需要返回到之前的状态
					 */
					isChange = true;
					seekBar.setProgress(seekBarProgressBefor);
					if (modifyStateBean != null) {
						MsToast.makeText(baseContext, modifyStateBean.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "修改失败").show();
					}
				}

				break;
			case MsConfiguration.FAIL:
				/**
				 * 修改失败，需要返回到之前的状态
				 */
				isChange = true;
				seekBar.setProgress(seekBarProgressBefor);
				MsToast.makeText(baseContext, "修改失败").show();
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:修改快递员状态
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-19下午3:39:57
	 * 
	 */
	public void modifyCourierState() {
		showRoundProcessDialog();
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("wdStatus", 1);
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(baseContext, paramsIn,
				MsRequestConfiguration.MODIFY_COURIER_STATE,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerModifyCourierState.sendMessage(message);
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
							modifyStateBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerModifyCourierState.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerModifyCourierState.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return modifyStateBean;
					}
				});
	}

	/**
	 * 修改快递员状态消息返回
	 */
	Handler handlerModifyCourierState = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:
				if (modifyStateBean != null && modifyStateBean.getCode() == 0) {
					/**
					 * 修改成功
					 */
					MsToast.makeText(baseContext, "修改成功").show();
					tvState.setBackgroundResource(R.drawable.free_bg);
					tvState.setTextColor(baseContext.getResources().getColor(
							R.color.color8cc63f));
					tvState.setText("闲");
					mCenterX = tvState.getWidth() / 2;
					mCenterY = tvState.getHeight() / 2;
					Rotate3dAnimation rotation = new Rotate3dAnimation(180, 0,
							mCenterX, mCenterY, mDepthZ, false);
					rotation.setDuration(mDuration);
					rotation.setFillAfter(true);
					rotation.setInterpolator(new DecelerateInterpolator());
					tvState.startAnimation(rotation);
					courierState = 1;
				} else {
					if (modifyStateBean != null) {
						MsToast.makeText(baseContext, modifyStateBean.getMsg())
								.show();
					} else {
						MsToast.makeText(baseContext, "修改失败").show();
					}
				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(baseContext, "修改失败").show();
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:接受广播，处理有新预约订单
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-30下午8:35:26
	 * 
	 */
	public static class InnerReceiver extends BroadcastReceiver {

		public InnerReceiver() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.min.musicdemo.action.NUM_COUNT")) {
				// Log.d(TAG, "*********** NUM_COUNT_RECEIVER Start **********"
				// + Common.getDate());
				try {

					LogUtil.d(TAG, "收到广播");
					// setViewData();
					// getNetData();
					getNewData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void getNewData() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		getHomepageData(map, MsRequestConfiguration.HOME_PAGE,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME, false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// TODO Auto-generated method stub
		exitApplication(keyCode, event);
		return true;
	}

	private long exitTime;

	protected void exitApplication(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				MsToast.makeText(this, getString(R.string.exit_message)).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				AppManager.getAppManager().AppExit(this);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);

		// if (MsConfiguration.HOME_PAGE_TO_APPOINT == requestCode) {
		// /**
		// * 如果当前状态为闲，刷新为忙
		// */
		// LogUtil.d(TAG, "HOME_PAGE_TO_APPOINT");
		// if (courierState == 1) {
		// tvState.setBackgroundResource(R.drawable.busy_bg);
		// tvState.setTextColor(baseContext.getResources().getColor(
		// R.color.colored1c24));
		// tvState.setText("忙");
		// Rotate3dAnimation rotation = new Rotate3dAnimation(180, 0,
		// mCenterX, mCenterY, mDepthZ, false);
		// rotation.setDuration(mDuration);
		// rotation.setFillAfter(true);
		// rotation.setInterpolator(new DecelerateInterpolator());
		// tvState.startAnimation(rotation);
		// courierState = 2;
		// }
		// }
	}

	@Override
	public void messageChange() {
		// TODO Auto-generated method stub
		if (MsApplication.messageList != null
				&& MsApplication.messageList.size() > 0) {
			int newMessageNum = homePageData.getInfo().getNoCheckMsgNum()
					- MsApplication.messageList.size();
			if (newMessageNum > 0) {
				btMessageNum.setText(newMessageNum + "");
			} else {
				btMessageNum.setVisibility(View.GONE);
			}
			LogUtil.d(TAG, "刷新消息回调");
		} else {
			LogUtil.d(TAG, "集合为空 刷新消息回调");
		}

	}

	@Override
	public void backHomepage() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG, "backhome");
		getNewData();
	}

}
