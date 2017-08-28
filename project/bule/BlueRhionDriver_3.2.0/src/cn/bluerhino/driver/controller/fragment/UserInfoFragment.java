package cn.bluerhino.driver.controller.fragment;

import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRAction;
import cn.bluerhino.driver.BRURL;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.activity.HistoryOrderActivity;
import cn.bluerhino.driver.controller.activity.MyInfoActivity;
import cn.bluerhino.driver.controller.activity.WebViewActivity;
import cn.bluerhino.driver.model.DriverIDetail;
import cn.bluerhino.driver.network.DriverDetailRequest;
import cn.bluerhino.driver.network.DriverLogoutRequest;
import cn.bluerhino.driver.network.listener.BrErrorListener;
import cn.bluerhino.driver.utils.AppManager;
import cn.bluerhino.driver.utils.JPushSettings;
import cn.bluerhino.driver.utils.NumberUtils;
import cn.bluerhino.driver.utils.DeviceInfo;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.view.CircleNetImage;
import cn.bluerhino.driver.view.LoadingDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import com.bluerhino.library.network.framework.BRModelRequest.BRModelResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.network.image.ImageCacheManager;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;

/***
 * @author chowjee
 * @date 2014-8-2 个人信息页
 */
public class UserInfoFragment extends BaseParentFragment implements
		View.OnClickListener {

	private static final String TAG = UserInfoFragment.class.getSimpleName();
	private CircleNetImage mUserIcon;
	private TextView mUserName;
	private TextView mCarName;
	private TextView mCarNum;
	private TextView mTotalRevenue;
	/** 总单数 */
	private TextView mTotalOrders;
	/** 每日订单数 */
	@InjectView(R.id.info_today_orders)
	TextView mTodayOrders;
	/** 本日收入 */
	@InjectView(R.id.info_today_revenue)
	TextView mTodayRevenue;
	/** 公告通知栏 */
	@InjectView(R.id.dri_announcement_layout)
	LinearLayout mAnnounceLayout;
	private TextView mCommentLevel;
	private ImageButton mExit;
	private LinearLayout mInviteUser;
	/** 司机排名 */
	private LinearLayout mRankingInfo;
	private LinearLayout mRatingPage;
	private LinearLayout mHisorderlist;
	private LinearLayout mTotalRevenueBar;
	private LinearLayout mInfoBar;
	private TextView mVersion;
	private LinearLayout mUpdate;
	private LinearLayout mURLContainer;
	private DriverIDetail mDriverIDetail;
	private Dialog mExitDialog;
	private TextView personinfo_status;
	private TextView driver_ranking;
	@InjectView(R.id.curtain)
	RelativeLayout mCurtain;
	private DriverDetailRequest mDriverDetailRequest;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private String mDid;
	private boolean mIsFirshLoadView = true;
	private LoadingDialog mLoadingDialog;
	private String mSessionId;
	private String mDeviceId = ApplicationController.getInstance()
			.getDeviceId();
	private String mVerCode = ApplicationController.getInstance().getVerCode();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

	}

	private void init() {
		initDataRequest();
		createLoadinDialog();
		createExitDialog();
		MobclickAgent.onEvent(mActivity, "pageUserInfo");
	}

	/**
	 * 
	 * Describe:初始化网络请求
	 * 
	 * Date:2015-8-18
	 * 
	 * Author:liuzhouliang
	 */
	private void initDataRequest() {
		mSessionId = ApplicationController.getInstance().getLoginfo()
				.getSessionID();
		mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		ImageCacheManager imageCacheManager = ImageCacheManager.getInstance();
		imageCacheManager.init(ApplicationController.getInstance()
				.getApplicationContext(), "uniqueName", 1024 * 1024 * 10,
				CompressFormat.PNG, 100, mRequestQueue);
		mImageLoader = imageCacheManager.getImageLoader();

		BRRequestParams params = new BRRequestParams(mSessionId);
		params.setDeviceId(mDeviceId);
		params.setVerCode(mVerCode);
		BRModelResponse<DriverIDetail> succeedListener = new BRModelResponse<DriverIDetail>() {
			@Override
			public void onResponse(DriverIDetail driverIDetail) {
				if (driverIDetail != null) {
					mDriverIDetail = driverIDetail;
					parseDriverInfo();
					if (mIsFirshLoadView) {
						mLoadingDialog.dismiss();
						mIsFirshLoadView = false;
					}
				}
			}
		};

		mDriverDetailRequest = new DriverDetailRequest.Builder()
				.setSucceedListener(succeedListener)
				.setErrorListener(
						new BrErrorListener(mActivity) {
							@Override
							public void onErrorResponse(VolleyError error) {
								super.onErrorResponse(error);
								mCurtain.setVisibility(View.GONE);
								if (mIsFirshLoadView) {
									mLoadingDialog.dismiss();
									mIsFirshLoadView = false;
								}
							}
						}
				).setParams(params).build();

	}

	private void createLoadinDialog() {
		mLoadingDialog = new LoadingDialog(mActivity);
	}

	private void createExitDialog() {
		if (mExitDialog == null) {
			mExitDialog = new AlertDialog.Builder(mActivity)
					.setTitle(R.string.myinfo_exit_app)
					.setPositiveButton(R.string.myinfo_ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									BRRequestParams params = new BRRequestParams(
											mSessionId);
									params.setDeviceId(mDeviceId);
									params.setVerCode(mVerCode);
									BRJsonResponse succeedListener = new BRJsonResponse() {
										@Override
										public void onResponse(
												JSONObject response) {
											//"" （空字符串）表示取消之前的设置
											JPushSettings.getInstance().setAlias("");
											AppManager.getAppManager().exitApp(
													mActivity);
										}
									};

									ErrorListener errorListener = new BrErrorListener(
											mActivity);

									BRJsonRequest logoutRequest = new DriverLogoutRequest.Builder()
											.setSucceedListener(succeedListener)
											.setErrorListener(errorListener)
											.setParams(params).build();

									mRequestQueue.add(logoutRequest);
								}
							}).setNegativeButton(R.string.myinfo_cancel, null)
					.create();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mDriverIDetail = null;
		mDriverDetailRequest.cancel();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_userinfo, container,
				false);
		ButterKnife.inject(this, view);
		mUserIcon = (CircleNetImage) view.findViewById(R.id.driver_img);
		mUserName = (TextView) view.findViewById(R.id.myinfo_username);
		mCarName = (TextView) view.findViewById(R.id.myinfo_carname);
		mCarNum = (TextView) view.findViewById(R.id.licence_plate_number);
		mTotalRevenue = (TextView) view.findViewById(R.id.info_total_revenue);
		mTotalOrders = (TextView) view.findViewById(R.id.info_total_orders);
		mCommentLevel = (TextView) view.findViewById(R.id.info_coment_level);
		mExit = (ImageButton) view.findViewById(R.id.myinfo_exit);
		mExit.setOnClickListener(this);
		mVersion = (TextView) view.findViewById(R.id.myinfo_version);
		mInviteUser = (LinearLayout) view
				.findViewById(R.id.myinfo_invite_users);
		mInviteUser.setOnClickListener(this);
		mUpdate = (LinearLayout) view.findViewById(R.id.myinfo_update);
		mUpdate.setOnClickListener(this);
		mURLContainer = (LinearLayout) view.findViewById(R.id.url_container);
		mRankingInfo = (LinearLayout) view.findViewById(R.id.myinfo_ranking);
		mRankingInfo.setOnClickListener(this);
		mRatingPage = (LinearLayout) view
				.findViewById(R.id.myinfo_cus_ratings_page);
		mRatingPage.setOnClickListener(this);
		mHisorderlist = (LinearLayout) view
				.findViewById(R.id.info_his_orderlist);
		mHisorderlist.setOnClickListener(this);
		mTotalRevenueBar = (LinearLayout) view
				.findViewById(R.id.info_total_revenue_bar);
		mTotalRevenueBar.setOnClickListener(this);
		mInfoBar = (LinearLayout) view.findViewById(R.id.myinfo_viewmyinfo);
		mInfoBar.setOnClickListener(this);
		personinfo_status = (TextView) view
				.findViewById(R.id.personinfo_status);
		driver_ranking = (TextView) view.findViewById(R.id.driver_ranking);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mVersion.setText(mRes.getString(R.string.myinfo_curr_version)
				+ DeviceInfo.getInstance().getAppVersionName(mActivity,
						mActivity.getPackageName()));
		if (!TextUtils.isEmpty(ApplicationController.getInstance()
				.getLoginfo().getSessionID())) {
			if (mIsFirshLoadView) {
				mLoadingDialog.show();
			} else {
				parseDriverInfo();
			}
		}
	}

	/**
	 * 在view构建后加载mDriverIDetail的数据
	 */
	private void parseDriverInfo() {
		if (mDriverIDetail != null) {
			LogUtil.d(TAG, mDriverIDetail.toString());
			mDid = mDriverIDetail.Id;
			String portraitPath = mDriverIDetail.Image;
			if (portraitPath.equals("")) {
				portraitPath = BRURL.ERROR_IMAGE_URL;
			}
			mUserIcon.setImageUrl(portraitPath, mImageLoader);
			mUserIcon.setDefaultImageResId(R.drawable.myinfo_headicon_bg);
			mUserIcon.setErrorImageResId(R.drawable.myinfo_headicon_bg);
			// 车牌号
			mCarNum.setText(mDriverIDetail.CarNum);
			// 司机姓名
			mUserName.setText(mDriverIDetail.Name);

			// 驾驶车型:数字转车型, 要区分城市
			String carType = mDriverIDetail.driverInfo.detailName;
			if (TextUtils.isEmpty(carType)) {
				carType = mRes.getString(R.string.myinfo_unselected_cartype);
			}
			mCarName.setText(carType);
			// 累计单数
			int orders = mDriverIDetail.totalOrders;
			mTotalOrders.setText(mRes.getString(R.string.myinfo_total_orders,
					orders));
			// 每日单数
			orders = mDriverIDetail.todayOrders;
			mTodayOrders.setText(mRes.getString(R.string.myinfo_today_orders,
					orders));
			// 累计收入
			mTotalRevenue.setText(mRes.getString(R.string.myinfo_total_revenue,
					mDriverIDetail.totalEarns));
			// 本日收入
			mTodayRevenue.setText(mRes.getString(R.string.myinfo_today_revenue,
					mDriverIDetail.todayEarns));
			// 顾客评分
			String commentLv = mDriverIDetail.commentLevel;
			if (TextUtils.isEmpty(commentLv)) {
				commentLv = mRes.getString(R.string.myinfo_no_data);
			} else {
				commentLv = mRes.getString(R.string.myinfo_comment_lv,
						NumberUtils.formatFloatFen(Float.valueOf(commentLv)));
			}
			mCommentLevel.setText(commentLv);
			if (mRes.getString(R.string.myinfo_approved).equals(
					mDriverIDetail.accountStat)) {
				personinfo_status.setTextColor(Color.parseColor("#00a4e3"));
			} else {
				personinfo_status.setTextColor(Color.parseColor("#fd6138"));
			}
			personinfo_status.setText(mDriverIDetail.accountStat);

			// 本周排名
			driver_ranking.setText(mRes.getString(
					R.string.myinfo_driver_ranking, mDriverIDetail.weekSort));

			final String policyStr = mRes
					.getString(R.string.myinfo_announcement);
			// 获取其他网页连接的个数
			List<Map<String, String>> wapList = mDriverIDetail.WapList;
			String announcementURL = "";
			for (Map<String, String> map : wapList) {
				if (map.containsKey("title")
						&& policyStr.equals(map.get("title"))) {
					if (map.containsKey("url")) {
						announcementURL = map.get("url");
						break;
					}
				}
			}
			if (!announcementURL.equals("")) {
				final String url_key = announcementURL;
				final String url_key_params = String.format(url_key.trim()
						+ BRURL.WAP_COMMON_SUFFIX, mDid, mSessionId, mDeviceId,
						mVerCode);
				mAnnounceLayout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent it = new Intent(mActivity, WebViewActivity.class);
						it.putExtra(WebViewActivity.URL_KEY, url_key_params);
						it.putExtra(WebViewActivity.PAGE_TITLE, policyStr);
						startActivity(it);
						MobclickAgent.onEvent(mActivity,
								"pageUserInfo_btn_anno");
					}
				});
			} else {
				mAnnounceLayout.setVisibility(View.GONE);
			}

			if (mURLContainer.getChildCount() == 1) {
				int index = 0;
				String content = "";
				String name = "";

				for (Map<String, String> map : mDriverIDetail.WapList) {
					if (map.containsKey("title")) {
						name = map.get("title");
						if (policyStr.equals(name)) {
							name = "";
							continue;
						}
					}
					if (map.containsKey("url")) {
						content = map.get("url");
					}
					if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(name)) {
						final String url_content = String.format(content.trim()
								+ BRURL.WAP_COMMON_SUFFIX, mDid, mSessionId,
								mDeviceId, mVerCode);
						final String url_name = name;
						View itemURLView = View.inflate(mActivity,
								R.layout.info_url_item, null);
						((TextView) itemURLView.findViewById(R.id.item1_head))
								.setText(url_name);
						itemURLView
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(mActivity,
												WebViewActivity.class);
										intent.putExtra(
												WebViewActivity.URL_KEY,
												url_content);
										intent.putExtra(
												WebViewActivity.PAGE_TITLE,
												url_name);
										startActivity(intent);
									}
								});
						mURLContainer.addView(itemURLView, index++);
					}
				}
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		initDataRequest();
		mRequestQueue.add(mDriverDetailRequest);
	}

	@Override
	public void onResume() {
		mSessionId = ApplicationController.getInstance().getLoginfo()
				.getSessionID();
		MobclickAgent.onPageStart(TAG);
		super.onResume();
	}

	@Override
	public void onPause() {
		MobclickAgent.onPageEnd(TAG);

		super.onPause();
	}

	@Override
	public void onClick(View v) {
		String mSessionID = ApplicationController.getInstance().getLoginfo().getSessionID();
		if (mSessionID != null && !mSessionID.equals("")) {
			int id = v.getId();
			if (R.id.myinfo_exit == id) {
				mExitDialog.show();
			}

			// 推荐客户
			else if (R.id.myinfo_invite_users == id) {
				Intent intent = new Intent(mActivity, WebViewActivity.class);
				String url = String.format(BRURL.DRIVER_INVITE_URL, mDid,
						mSessionID, mDeviceId, mVerCode);
				intent.putExtra(WebViewActivity.URL_KEY, url);
				intent.putExtra(WebViewActivity.PAGE_TITLE,
						mRes.getString(R.string.myinfo_driver_invite));
				startActivity(intent);
				MobclickAgent.onEvent(mActivity, "pageUserInfo_btn_invite");
			} else if (R.id.myinfo_update == id) {
				checkUpdate();
				MobclickAgent.onEvent(mActivity, "pageUserInfo_btn_update");
			} else if (R.id.myinfo_ranking == id) {
				// 进入司机排名页
				Intent intent = new Intent(mActivity, WebViewActivity.class);
				String url = String.format(BRURL.ORDER_RANK_URL, mDid,
						mSessionID, mDeviceId, mVerCode);
				intent.putExtra(WebViewActivity.URL_KEY, url);
				intent.putExtra(WebViewActivity.PAGE_TITLE,
						mRes.getString(R.string.myinfo_order_rank));
				startActivity(intent);
				MobclickAgent.onEvent(mActivity, "pageUserInfo_btn_ranking");
			} else if (R.id.myinfo_cus_ratings_page == id) {
				// 顾客评分页
				Intent intent = new Intent(mActivity, WebViewActivity.class);
				String url = String.format(BRURL.CUSTOMER_GRADE_URL, mDid,
						mSessionID, mDeviceId, mVerCode);
				intent.putExtra(WebViewActivity.URL_KEY, url);
				intent.putExtra(WebViewActivity.PAGE_TITLE,
						mRes.getString(R.string.myinfo_customer_grade));
				startActivity(intent);
				MobclickAgent.onEvent(mActivity, "pageUserInfo_btn_ratings");
			} else if (R.id.info_total_revenue_bar == id) {
				// 进入累计收入页(账户信息页)
				Intent intent = new Intent(mActivity, WebViewActivity.class);
				String url = String.format(BRURL.ACCOUNT_URL, mDid, mSessionID,
						mDeviceId, mVerCode);
				intent.putExtra(WebViewActivity.URL_KEY, url);
				intent.putExtra(WebViewActivity.PAGE_TITLE,
						mActivity.getString(R.string.account_info));
				startActivity(intent);
				MobclickAgent.onEvent(mActivity, "pageUserInfo_page_drive");
			} else if (R.id.myinfo_viewmyinfo == id && mDriverIDetail != null) {
				// 进入个人信息页
				Intent intent = new Intent(mActivity, MyInfoActivity.class);
				intent.putExtra(BRAction.EXTRA_DRIVERINFO, mDriverIDetail.driverInfo);
				intent.putExtra(BRAction.EXTRA_ACCOUNTSTAT, mDriverIDetail.accountStat);
				startActivity(intent);
				MobclickAgent
						.onEvent(mActivity, "pageUserInfo_btn_myinfo_view");
			} else if (R.id.info_his_orderlist == id) {
				// 进入历史订单
				Intent intent = new Intent(mActivity,
						HistoryOrderActivity.class);
				startActivity(intent);
				MobclickAgent.onEvent(mActivity,
						"pageUserInfo_btn_hisorderlist");
			}
		}
	}

	private void checkUpdate() {
		if (null == mActivity)
			return;
		UpdateConfig.setDebug(true);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateOnlyWifi(false); // 是否在只在wifi下提示更新，默认为 true
		UmengUpdateAgent.setUpdateAutoPopup(true); // 是否自动弹出更新对话框
		UmengUpdateAgent.setDownloadListener(null); // 下载新版本过程事件监听，可设为 null
		UmengUpdateAgent.setDialogListener(null); // 用户点击更新对话框按钮的回调事件，直接 null
		// 从服务器获取更新信息的回调函数
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case 0: // 有更新
					UmengUpdateAgent.showUpdateDialog(mActivity, updateInfo);
					break;
				case 1: // 无更新
					ToastUtil.showToast(mActivity,
							R.string.myinfo_umeng_no_update);
					break;
				case 2: // 如果设置为wifi下更新且wifi无法打开时调用
					ToastUtil.showToast(mActivity,
							R.string.myinfo_umeng_need_net_update);
					break;
				case 3: // 连接超时
					ToastUtil.showToast(mActivity,
							R.string.myinfo_umeng_timeout_update);
					break;
				}
			}

		});
		UmengUpdateAgent.update(mActivity);
	}
}
