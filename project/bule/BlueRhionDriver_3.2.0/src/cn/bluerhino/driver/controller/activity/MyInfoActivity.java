package cn.bluerhino.driver.controller.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.navisdk.util.common.StringUtils;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRJsonRequest.BRJsonResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.network.image.ImageCacheManager;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.BRAction;
import cn.bluerhino.driver.BRURL;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.helper.DriverInfoHelper;
import cn.bluerhino.driver.helper.DriverInfoHelper.UpdateStatus;
import cn.bluerhino.driver.helper.PictureHelper;
import cn.bluerhino.driver.model.DriverInfo;
import cn.bluerhino.driver.network.UpdateDriverInfoRequest;
import cn.bluerhino.driver.network.UploadFilesPost;
import cn.bluerhino.driver.network.listener.RequestListener;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.view.CircleNetImage;
import cn.bluerhino.driver.view.LoadingDialog;

/**
 * 账户信息页面
 */
public class MyInfoActivity extends BaseParentActivity implements View.OnClickListener {

	private static final String TAG = MyInfoActivity.class.getSimpleName();
	private static final int TAKE_MY_PROTRAIT_PIC_OK = 0;
	private static final int SEL_MY_PROTRAIT_PIC_OK = 1;
	private static final int TAKE_MY_INFO_PIC_OK = 2;
	private static final int SEL_MY_INFO_PIC_OK = 3;
	private static final int TAKE_DRIVER_PIC_OK = 4;
	private static final int SEL_DRIVER_INFO_PIC_OK = 5;
	private static final int TAKE_VEHICLE_PIC_OK = 6;
	private static final int SEL_VEHICLE_PIC_OK = 7;
	private static final int RESCODE_SELCITY = 15;
	private static final int RESCODE_SELCARMODE = 16;
	private static String mErrorImageUrl = BRURL.ERROR_IMAGE_URL;
	public final String RESPONSE_FIELD_FILEPATH = "filepath";
	public final String DEF_UPLOAD_PICNAME = "def.png";
	public final static String PIC_STATUS_NONE = "";
	public final static String PIC_STATUS_ISLOADING = "isloading";
	private PopupWindow mPopupWindow;
	private AlertDialog.Builder mAlertBuilder;
	private DriverInfo mDriverInfo;
	Bitmap bitmap = null;

	private static final int DRIVER_PHOTO = 0;
	private static final int DRIVER_IDCARD = 1;
	private static final int DRIVER_DRIVING_LICENSE = 2;
	private static final int DRIVER_VEHICLE_LICENSE = 3;

	@InjectView(R.id.left_img)
	ImageView myinfo_leftbar_barbutton;
	@InjectView(R.id.my_portrait_layout)
	LinearLayout my_portrait_layout;
	@InjectView(R.id.center_title)
	TextView center_title;
	@InjectView(R.id.myactionbar)
	RelativeLayout myHeadBar;
	@InjectView(R.id.curtain)
	FrameLayout mCurtain;
	/** 头像照片 */
	@InjectView(R.id.driver_img)
	CircleNetImage my_protrait_pic;
	/** 身份证 */
	@InjectView(R.id.info_pic)
	CircleNetImage info_pic;
	/** 驾驶证 */
	@InjectView(R.id.driver_pic)
	CircleNetImage driver_pic;
	/** 行驶证 */
	@InjectView(R.id.vehicle_pic)
	CircleNetImage vehicle_pic;

	@InjectView(R.id.resident_area_layout)
	LinearLayout resident_area_layout;
	@InjectView(R.id.carmode_layout)
	LinearLayout carmode_layout;

	@InjectView(R.id.city_info)
	TextView city_info;
	@InjectView(R.id.carmode_info)
	TextView carmode_info;

	private ImageView mPopWindowImage;
	private TextView takePhoto;
	private TextView selPhoto;
	private TextView cancel;

	/** 保存按钮 */
	@InjectView(R.id.commit_driver_info)
	Button mCommitButton;

	private Intent CAPTURE_INTENT;
	private Intent SELPIC_INTENT;
	private View mPopWindowcontentView;
	private boolean mIsHaveChanged = false;

	// 身份证
	private String mImageURL = PIC_STATUS_NONE;
	private String mIdCardPic = PIC_STATUS_NONE;
	private String mDriverCardPic = PIC_STATUS_NONE;
	private String mDriLicPic = PIC_STATUS_NONE;
	// private String mDriverName; 姓名 与 车牌号 直接保存即可.不需判断

	private String mOrignCity;

	/** detailName字段 */
	private String mOrignCityDetailName;
	/** CarType字段 */
	private String mCarType;
	/** detailType(detailId)字段 */
	private String mDetailType;

	/**
	 * 判断能否进行修改资料页信息
	 */
	private boolean mIsCanModified;
	/**
	 * 修改资料页信息描述
	 */
	private String mModifyDesc;

	@InjectView(R.id.driver_name)
	EditText driver_name;
	@InjectView(R.id.driver_phone)
	TextView driver_phone;
	@InjectView(R.id.driver_carnum)
	EditText driver_carnum;
	private RequestQueue mRequestQueue;
	private Activity mActivity;
	private ImageLoader mImageLoader;
	private BRRequestParams mParams;
	private LoadingDialog mLoadingDialog;
	private int PICTURE_QUALIFIED_WIDTH = 280;
	private int PICTURE_QUALIFIED_HEIGHT = 280;

	/*
	 * 拍照得到的照片文件
	 */
	private File mPictureFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfo_main);
		ButterKnife.inject(this);
		mPictureFile = new File(super.getExternalCacheDir(), "pic1.jpg");
		mActivity = this;
		initHead();
		checkData();
		initVolley();
		initData();
		initView();
		loadData();
	}

	private void initHead() {
		myHeadBar.setBackgroundColor(mRes.getColor(R.color.commonly_actionbar_gray));
		myinfo_leftbar_barbutton.setImageResource(R.drawable.comment_left_back_button);
		myinfo_leftbar_barbutton.setVisibility(View.VISIBLE);
		myinfo_leftbar_barbutton.setOnClickListener(this);
		center_title.setTextColor(mRes.getColor(R.color.main_text_color));
		center_title.setText(R.string.myinfo_modify_person_info);
	}

	private void checkData() {
		Intent intent = getIntent();
		if (intent == null) {
			ToastUtil.showToast(mActivity, R.string.myinfo_modify_net_noconnect);
			mActivity.finish();
		}
		if (intent.getParcelableExtra(BRAction.EXTRA_DRIVERINFO) == null) {
			ToastUtil.showToast(mActivity, R.string.myinfo_modify_net_noconnect);
			mActivity.finish();
		}
	}

	private void initData() {
		mDriverInfo = getIntent().getParcelableExtra(BRAction.EXTRA_DRIVERINFO);
		String accountStat = getIntent().getStringExtra(BRAction.EXTRA_ACCOUNTSTAT);
		UpdateStatus canUpdate = DriverInfoHelper.isCanUpdate(accountStat);
		mIsCanModified = canUpdate.getIsCanModify();
		mModifyDesc = canUpdate.getBtnDesc();
		mOrignCity = mDriverInfo.city;
		mOrignCityDetailName = mDriverInfo.detailName;
		mCarType = String.valueOf(mDriverInfo.CarType);
		mDetailType = String.valueOf(mDriverInfo.detailType);

	}

	private void initVolley() {
		mParams = new BRRequestParams(ApplicationController.getInstance().getLoginfo().getSessionID());
		mParams.setDeviceId(ApplicationController.getInstance().getDeviceId());
		mParams.setVerCode(ApplicationController.getInstance().getVerCode());

		mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		ImageCacheManager imageCacheManager = ImageCacheManager.getInstance();
		imageCacheManager.init(mActivity, "uniqueName", 1024 * 1024 * 10, CompressFormat.PNG, 100, mRequestQueue);
		mImageLoader = imageCacheManager.getImageLoader();
	}

	private void initIntnet() {
		CAPTURE_INTENT = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		CAPTURE_INTENT.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		CAPTURE_INTENT.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
		CAPTURE_INTENT.putExtra(MediaStore.Images.Media.ORIENTATION, 0);

		SELPIC_INTENT = new Intent(Intent.ACTION_GET_CONTENT);
		SELPIC_INTENT.setType("image/*");
		SELPIC_INTENT.putExtra("scale", true);
		SELPIC_INTENT.putExtra("outputX", PICTURE_QUALIFIED_WIDTH); // 返回数据的时候的
																	// X 像素大小。
		SELPIC_INTENT.putExtra("outputY", PICTURE_QUALIFIED_HEIGHT); // 返回的时候 Y
																		// 的像素大小。
	}

	private void initView() {
		mCommitButton.setText(mModifyDesc);
		if (!mIsCanModified) {
			mCurtain.setVisibility(View.VISIBLE);
			mCurtain.setOnClickListener(null);
			driver_carnum.setFocusable(false);
			driver_carnum.setEnabled(false);
			driver_name.setFocusable(false);
			driver_name.setEnabled(false);
			mCommitButton.setEnabled(false);
			mCommitButton.setBackgroundResource(R.drawable.nav_button_bg_pressed);
		} else {
			my_portrait_layout.setOnClickListener(this);
			info_pic.setOnClickListener(this);
			driver_pic.setOnClickListener(this);
			vehicle_pic.setOnClickListener(this);

			resident_area_layout.setOnClickListener(this);
			carmode_layout.setOnClickListener(this);
			mCommitButton.setOnClickListener(this);
			driver_name.addTextChangedListener(new TextWatcher() {
				int afterTextChangedCount = 1;

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (afterTextChangedCount > 1) {
						mIsHaveChanged = true;
					}
					afterTextChangedCount++;
				}
			});
			driver_carnum.addTextChangedListener(new TextWatcher() {
				int afterTextChangedCount = 1;

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (afterTextChangedCount > 1) {
						mIsHaveChanged = true;
					}
					afterTextChangedCount++;
				}
			});
			createPopwindow();
			createLoadingDialog();
			initIntnet();
		}
	}

	private void createPopwindow() {
		mPopWindowcontentView = View.inflate(this, R.layout.select_pic, null);
		mPopupWindow = new PopupWindow(mPopWindowcontentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopWindowImage = (ImageView) mPopWindowcontentView.findViewById(R.id.imagedemo);
		takePhoto = (TextView) mPopWindowcontentView.findViewById(R.id.takephoto);
		selPhoto = (TextView) mPopWindowcontentView.findViewById(R.id.selphoto);
		cancel = (TextView) mPopWindowcontentView.findViewById(R.id.cancel);

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPopupWindow != null && mPopupWindow.isShowing())
					mPopupWindow.dismiss();
			}
		});
		mPopupWindow.setFocusable(true);
		mPopWindowcontentView.setFocusable(true);
		mPopWindowcontentView.setFocusableInTouchMode(true);
		mPopWindowcontentView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (mPopupWindow != null && mPopupWindow.isShowing()) {
						mPopupWindow.dismiss();
						return true;
					}
				}
				return false;
			}
		});
		mPopupWindow.update();
	}

	private void createLoadingDialog() {
		mLoadingDialog = new LoadingDialog(this, R.string.loadingdialog_save);
	}

	private void loadData() {
		String portraitPath = mDriverInfo.image;
		if (portraitPath.equals("")) {
			portraitPath = mErrorImageUrl;
		}
		my_protrait_pic.setImageUrl(portraitPath, mImageLoader);
		my_protrait_pic.setDefaultImageResId(R.drawable.camera);
		my_protrait_pic.setErrorImageResId(R.drawable.camera);

		// 真实姓名
		String infoName = mDriverInfo.Name;
		if (TextUtils.isEmpty(infoName)) {
			infoName = "";
		}
		driver_name.setText(infoName);

		// 手机号不能改变
		String infoPhone = mDriverInfo.Phone;
		if (TextUtils.isEmpty(infoPhone)) {
			infoPhone = "";
		}
		driver_phone.setText(infoPhone);

		// 车牌号
		String infoCarNum = mDriverInfo.CarNum;
		if (TextUtils.isEmpty(infoCarNum)) {
			infoCarNum = "";
		}
		driver_carnum.setText(infoCarNum);

		// 常驻区域+驾驶车型(detailname+cartype+detailid)
		city_info.setText(mOrignCity);
		carmode_info.setText(String.valueOf(mOrignCityDetailName));

		String idCardPath = mDriverInfo.IdCardPic;
		// 如果是第一次从服务器获取的是""
		if (idCardPath.equals("")) {
			idCardPath = mErrorImageUrl;
		}
		info_pic.setImageUrl(idCardPath, mImageLoader);
		info_pic.setDefaultImageResId(R.drawable.idcard_pic);
		info_pic.setErrorImageResId(R.drawable.idcard_pic);

		String driverCardPath = mDriverInfo.DriverCardPic;
		if (driverCardPath.equals("")) {
			driverCardPath = mErrorImageUrl;
		}
		driver_pic.setImageUrl(driverCardPath, mImageLoader);
		driver_pic.setDefaultImageResId(R.drawable.driver_pic);
		driver_pic.setErrorImageResId(R.drawable.driver_pic);

		String driImgPath = mDriverInfo.DriLicPic;
		if (driImgPath.equals("")) {
			driImgPath = mErrorImageUrl;
		}
		vehicle_pic.setImageUrl(driImgPath, mImageLoader);
		vehicle_pic.setDefaultImageResId(R.drawable.vehicle_pic);
		vehicle_pic.setErrorImageResId(R.drawable.vehicle_pic);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_img:
			ExitToSaveDriverInfo();
			break;
		case R.id.my_portrait_layout:
			mPopWindowImage.setImageResource(R.drawable.portrait_demo);
			mPopupWindow.showAtLocation(mPopWindowcontentView, Gravity.CENTER, 0, 0);
			takePhoto.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MyInfoActivity.this.startActivityForResult(CAPTURE_INTENT, TAKE_MY_PROTRAIT_PIC_OK);
					if (mPopupWindow != null && mPopupWindow.isShowing())
						mPopupWindow.dismiss();
				}
			});
			selPhoto.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					MyInfoActivity.this.startActivityForResult(SELPIC_INTENT, SEL_MY_PROTRAIT_PIC_OK);
					if (mPopupWindow != null && mPopupWindow.isShowing())
						mPopupWindow.dismiss();
				}
			});
			break;
		case R.id.info_pic:
			mPopWindowImage.setImageResource(R.drawable.idcard_demo);
			mPopupWindow.showAtLocation(mPopWindowcontentView, Gravity.CENTER, 0, 0);
			takePhoto.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MyInfoActivity.this.startActivityForResult(CAPTURE_INTENT, TAKE_MY_INFO_PIC_OK);
					if (mPopupWindow != null && mPopupWindow.isShowing())
						mPopupWindow.dismiss();
				}
			});
			selPhoto.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					MyInfoActivity.this.startActivityForResult(SELPIC_INTENT, SEL_MY_INFO_PIC_OK);
					if (mPopupWindow != null && mPopupWindow.isShowing())
						mPopupWindow.dismiss();
				}
			});
			break;
		case R.id.driver_pic:
			mPopWindowImage.setImageResource(R.drawable.driver_demo);
			mPopupWindow.showAtLocation(mPopWindowcontentView, Gravity.CENTER, 0, 0);
			takePhoto.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MyInfoActivity.this.startActivityForResult(CAPTURE_INTENT, TAKE_DRIVER_PIC_OK);
					if (mPopupWindow != null && mPopupWindow.isShowing())
						mPopupWindow.dismiss();
				}
			});
			selPhoto.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					MyInfoActivity.this.startActivityForResult(SELPIC_INTENT, SEL_DRIVER_INFO_PIC_OK);
					if (mPopupWindow != null && mPopupWindow.isShowing())
						mPopupWindow.dismiss();
				}
			});
			break;
		case R.id.vehicle_pic:
			mPopWindowImage.setImageResource(R.drawable.vehicle_demo);
			mPopupWindow.showAtLocation(mPopWindowcontentView, Gravity.CENTER, 0, 0);
			takePhoto.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MyInfoActivity.this.startActivityForResult(CAPTURE_INTENT, TAKE_VEHICLE_PIC_OK);
					if (mPopupWindow != null && mPopupWindow.isShowing())
						mPopupWindow.dismiss();
				}
			});
			selPhoto.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					MyInfoActivity.this.startActivityForResult(SELPIC_INTENT, SEL_VEHICLE_PIC_OK);
					if (mPopupWindow != null && mPopupWindow.isShowing())
						mPopupWindow.dismiss();
				}
			});
			break;
		// 点击的是常住区域
		case R.id.resident_area_layout:
			Intent intent = new Intent(this, SelectCurrentCityActivity.class);
			intent.putExtra("CURRENTCITY", city_info.getText().toString());
			startActivityForResult(intent, RESCODE_SELCITY);
			break;
		// 点击的是选择车型:必须是已开通的城市,而且必须唯一
		case R.id.carmode_layout:
			Intent intent2 = new Intent(this, SelectCurrentCarModel.class);
			intent2.putExtra("CURRENTCITY", city_info.getText().toString());
			startActivityForResult(intent2, RESCODE_SELCARMODE);
			break;
		case R.id.commit_driver_info:
			normalSaveDriverInfo();
			break;
		}
	}

	/**
	 * 正常保存已修改的信息
	 */
	private void normalSaveDriverInfo() {
		if (mIsHaveChanged) {
			// 姓名可修改
			String needUploadDriverName = driver_name.getText().toString();
			// 常住区域修改
			String need_city_info = city_info.getText().toString();
			// 车牌号修改
			String need_car_num = driver_carnum.getText().toString();

			if (needUploadDriverName.trim().isEmpty()) {
				ToastUtil.showToast(mActivity, R.string.myinfo_modify_empty_name);
				return;
			}
			if (need_car_num.trim().isEmpty()) {
				ToastUtil.showToast(mActivity, R.string.myinfo_modify_empty_carnum);
				return;
			}
			mLoadingDialog.show();
			// //四张照片修改
			// //头像中的bitmap
			// my_protrait_pic.setDrawingCacheEnabled(true);
			// Bitmap newBitmap =
			// Bitmap.createBitmap(my_protrait_pic.getDrawingCache());
			// my_protrait_pic.setDrawingCacheEnabled(false);

			final BRJsonResponse updateInfoSucceedListener = new BRJsonResponse() {
				@Override
				public void onResponse(JSONObject response) {
					if (!isFinishing()) {
						mLoadingDialog.dismiss();
					}
					mIsHaveChanged = false;
					Toast.makeText(MyInfoActivity.this, "信息保存成功,可以退出了", Toast.LENGTH_SHORT).show();
					LogUtil.d(TAG, "---信息保存成功,可以退出了---");
				}
			};

			final ErrorListener updateErrorListener = new VolleyErrorListener(mActivity) {
				@Override
				public void onErrorResponse(VolleyError error) {
					super.onErrorResponse(error);
					if (!isFinishing()) {
						mLoadingDialog.dismiss();
					}
					mIsHaveChanged = true;
				}
			};

			mParams.put("Name", needUploadDriverName);
			// params.put("Phone", needUploadDriverPhone);手机号不用上传
			mParams.put("city", need_city_info);
			mParams.put("CarNum", need_car_num);
			// 车型要与城市绑定(一下三个字段要一起使用)
			mParams.put("CarType", mCarType);
			mParams.put("detailType", mDetailType);
			mParams.put("detailName", mOrignCityDetailName);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// 获得url后最终上传
					if (!mImageURL.equals(PIC_STATUS_NONE)) {
						mParams.put("image", mImageURL);
					}
					if (!mIdCardPic.equals(PIC_STATUS_NONE)) {
						mParams.put("IdCardPic", mIdCardPic);
					}
					if (!mDriverCardPic.equals(PIC_STATUS_NONE)) {
						mParams.put("DriverCardPic", mDriverCardPic);
					}
					if (!mDriLicPic.equals(PIC_STATUS_NONE)) {
						mParams.put("DriLicPic", mDriLicPic);
					}

					mRequestQueue
							.add(new UpdateDriverInfoRequest.Builder().setSucceedListener(updateInfoSucceedListener)
									.setErrorListener(updateErrorListener).setParams(mParams).build());
				}
			}, 1200);

		}

		else {
			ToastUtil.showToast(getApplicationContext(), R.string.myinfo_modify_notneed_save);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	/**
	 * @author sunliang
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		// 选择城市返回
		switch (requestCode) {

		case RESCODE_SELCITY:
			String currentCity = data.getStringExtra("currentCityString");
			LogUtil.d(TAG, "---currentCity----" + currentCity);
			// 选择城市不一致
			if (!TextUtils.isEmpty(currentCity) && !mOrignCity.equals(currentCity)) {
				mOrignCity = currentCity;
				city_info.setText(mOrignCity);
				// 重置车型信息
				mCarType = "0";
				mDetailType = "0";
				mOrignCityDetailName = mRes.getString(R.string.myinfo_modify_upload_field);
				carmode_info.setText(R.string.myinfo_modify_sel_cartype);
				mIsHaveChanged = true;
			}
			break;
		case RESCODE_SELCARMODE:
			String newCarMode = data.getStringExtra("carmode_infoString");
			LogUtil.d(TAG, "---newCarMode----" + newCarMode);
			String carType = data.getStringExtra("carType");
			String detailType = data.getStringExtra("detailType");
			if (!StringUtils.isEmpty(newCarMode) && !mOrignCityDetailName.equals(newCarMode)) {
				mOrignCityDetailName = newCarMode;
				mCarType = carType;
				mDetailType = detailType;
				carmode_info.setText(mOrignCityDetailName);
				mIsHaveChanged = true;
			}
			break;

		}

		// 判断是拍照
		if (requestCode == 0 || requestCode == 2 || requestCode == 4 || requestCode == 6) {
			if (mPictureFile == null || !mPictureFile.exists()) {
				ToastUtil.showToast(mContext, R.string.myinfo_modify_take_photo_error);
				return;
			}
			// 获取bitmap,将图片压缩，旋转（有的手机）
			bitmap = PictureHelper.getBitmap(mPictureFile.getAbsolutePath(), PICTURE_QUALIFIED_WIDTH,
					PICTURE_QUALIFIED_HEIGHT);
			// 将bitmap转换成文件
			File myFile = PictureHelper.convertBitMapToFile(bitmap, "lanxiniu_myinfo.jpg");
			// 内存回收
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
			bitmap = null;
			// 判断上传的是那张图片(头像、身份证、驾驶证、行驶证)
			if (requestCode == TAKE_MY_PROTRAIT_PIC_OK) {
				uploadDriverImage(myFile, DRIVER_PHOTO);
			} else if (requestCode == TAKE_MY_INFO_PIC_OK) {
				uploadDriverImage(myFile, DRIVER_IDCARD);
			} else if (requestCode == TAKE_DRIVER_PIC_OK) {
				uploadDriverImage(myFile, DRIVER_DRIVING_LICENSE);
			} else if (requestCode == TAKE_VEHICLE_PIC_OK) {
				uploadDriverImage(myFile, DRIVER_VEHICLE_LICENSE);
			}

		}
		// 判断是选择照片
		else if (requestCode == 1 || requestCode == 3 || requestCode == 5 || requestCode == 7) {
			Uri uri = data.getData();
			String path = PictureHelper.getAbsloutePath(this, uri);
			LogUtil.d(TAG, "---------" + path);
			// 获取bitmap,将图片压缩，旋转（有的手机）
			bitmap = PictureHelper.getBitmap(path, PICTURE_QUALIFIED_WIDTH, PICTURE_QUALIFIED_HEIGHT);
			// 将bitmap转换成文件
			File myFile = PictureHelper.convertBitMapToFile(bitmap, "lanxiniu_myinfo.jpg");
			// 内存回收
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
			bitmap = null;
			// 判断上传的是那张图片(头像、身份证、驾驶证、行驶证)
			switch (requestCode) {
			case SEL_MY_PROTRAIT_PIC_OK:
				uploadDriverImage(myFile, DRIVER_PHOTO);
				break;
			case SEL_MY_INFO_PIC_OK:
				uploadDriverImage(myFile, DRIVER_IDCARD);
				break;
			case SEL_DRIVER_INFO_PIC_OK:
				uploadDriverImage(myFile, DRIVER_DRIVING_LICENSE);
				break;
			case SEL_VEHICLE_PIC_OK:
				uploadDriverImage(myFile, DRIVER_VEHICLE_LICENSE);
				break;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 10081:// 处理上传的是哪张图片
				Bundle picTag = msg.getData();
				int myTag = (Integer) picTag.get("myTag");
				String imgUrl = (String) picTag.get("imgUrl");
				Log.d(TAG, imgUrl);
				switch (myTag) {
				// 头像
				case DRIVER_PHOTO:
					mImageURL = imgUrl;
					/*
					 * if (bitmap != null) {
					 * my_protrait_pic.setImageBitmap(bitmap); }
					 */
					my_protrait_pic.setImageUrl(mImageURL, mImageLoader);
					break;
				// 身份证件
				case DRIVER_IDCARD:
					mIdCardPic = imgUrl;
					/*
					 * if (bitmap != null) { info_pic.setImageBitmap(bitmap); }
					 */
					info_pic.setImageUrl(mIdCardPic, mImageLoader);
					break;
				// 驾驶证
				case DRIVER_DRIVING_LICENSE:
					mDriverCardPic = imgUrl;
					/*
					 * if (bitmap != null) { driver_pic.setImageBitmap(bitmap);
					 * }
					 */
					driver_pic.setImageUrl(mDriverCardPic, mImageLoader);
					break;
				// 行驶证
				case DRIVER_VEHICLE_LICENSE:
					mDriLicPic = imgUrl;
					/*
					 * if (bitmap != null) { vehicle_pic.setImageBitmap(bitmap);
					 * }
					 */
					vehicle_pic.setImageUrl(mDriLicPic, mImageLoader);
					break;
				}
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		if (handler != null) {
			handler.removeMessages(10081);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			ExitToSaveDriverInfo();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 点击退出时提醒保存已修改信息
	 */
	private void ExitToSaveDriverInfo() {
		if (mIsHaveChanged && mIsCanModified) {
			showIsNeedSaveDialog();
		} else {
			finish();
		}
	}

	/**
	 * 弹框提示如果是否需要保存已更新的数据,是则回去保存,否则退出.
	 */
	private void showIsNeedSaveDialog() {
		if (mAlertBuilder == null) {
			mAlertBuilder = new AlertDialog.Builder(this);
			mAlertBuilder.setMessage(R.string.myinfo_modify_confrim_save)
					.setPositiveButton(R.string.myinfo_modify_confrim_exit, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MyInfoActivity.this.finish();
						}
					}).setNegativeButton(R.string.myinfo_modify_confrim_tosave, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							normalSaveDriverInfo();
						}
					});
		}
		mAlertBuilder.show();
	}

	/**
	 * 上传司机相关图片，获取图片的url地址
	 * 
	 * @param file
	 *            需要上传的图片
	 * @param myTag
	 *            上传的tag标记
	 */
	private void uploadDriverImage(File file, final int myTag) {
		if (file == null || !file.exists()) {
			ToastUtil.showToast(mContext, R.string.myinfo_modify_take_photo_error);
			return;
		}
		// 判断是否更改数据
		mIsHaveChanged = true;
		Map<String, File> storeUpdateFile = new HashMap<String, File>();
		storeUpdateFile.put(file.getName(), file);
		String uploadUrl = BRURL.UPLOAD_IMAGE_POST_URL;
		UploadFilesPost mUploadFilesPost = new UploadFilesPost(this, uploadUrl, storeUpdateFile,
				new RequestListener<String>() {
					@Override
					public void onResponse(String result) {
						// 将json解析后，传递给handler
						try {
							JSONObject object = new JSONObject(result);
							int code = object.getInt("code");
							if (code == 0) {
								String imgUrl = object.getJSONObject("data").optString(RESPONSE_FIELD_FILEPATH);
								Message msg = Message.obtain();
								Bundle bundle = new Bundle();
								bundle.putInt("myTag", myTag);
								bundle.putString("imgUrl", imgUrl);
								msg.what = 10081;
								msg.setData(bundle);
								handler.sendMessage(msg);
							} else {
								ToastUtil.showToast(mContext, R.string.myinfo_modify_take_photo_error);
							}
						} catch (JSONException e) {
							LogUtil.d(TAG, "数据解析失败");
							e.printStackTrace();
						}
					}
				});
		mUploadFilesPost.execute();
	}

}