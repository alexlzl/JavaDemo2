package com.minsheng.app.module.usercenter;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.entity.LoginBean;
import com.minsheng.app.module.login.Login;
import com.minsheng.app.util.AppManager;
import com.minsheng.app.util.FileManager;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.view.CustomScrollTextView;
import com.minsheng.app.view.ScrollTextView;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @describe:个人信息页面
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-13下午5:23:09
 * 
 */
public class UserInfor extends BaseActivity {
	private static final String TAG = "UserInfor";
	private TextView tvJobNum, tvPhoneNum, tvName;
	private CustomScrollTextView tvServiceLocation;
	private ImageView ivHeadIcon;
	private LoginBean dataObject;
	private Button btExit;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.usercenter_userinfor);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		tvJobNum = (TextView) findViewById(R.id.usercenter_userinfor_jobnum);
		tvPhoneNum = (TextView) findViewById(R.id.usercenter_userinfor_phonenum);
		tvServiceLocation = (CustomScrollTextView) findViewById(R.id.usercenter_userinfor_service_location);
		ivHeadIcon = (ImageView) findViewById(R.id.usercenter_userinfor_headicon);
		tvName = (TextView) findViewById(R.id.usercenter_userinfor_name);
		btExit = (Button) findViewById(R.id.usercenter_userinfor_bt);
//		tvServiceLocation.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		dataObject = (LoginBean) FileManager.getObject(baseContext,
				MsConfiguration.LOGIN_INFOR_FILE);

		if (dataObject != null) {
			LogUtil.d(TAG, "dataObject==" + dataObject.toString());
			String name = dataObject.getInfo().getDeliveryUser().getWdName();
			String headUrl = dataObject.getInfo().getDeliveryUser()
					.getWdPicUrl();
			String jobNum = dataObject.getInfo().getDeliveryUser()
					.getWdNumber();
			String phoneNum = dataObject.getInfo().getDeliveryUser()
					.getWdMobile();
			List<String> serviceLocation = dataObject.getInfo()
					.getDeliveryUser().getServiceCyList();
			tvJobNum.setText(jobNum);
			tvPhoneNum.setText(phoneNum);
			StringBuilder sb = null;
			if (serviceLocation != null && serviceLocation.size() > 0) {
				sb = new StringBuilder();
				int size = serviceLocation.size();
				for (int i = 0; i < size; i++) {
					if (i == (size-1)) {
						sb.append(serviceLocation.get(i) + "");
					} else {
						sb.append(serviceLocation.get(i) + ",");
					}

				}
			}
			tvServiceLocation.setText(sb.toString());
			tvName.setText(name);
			MsApplication.imageLoader.displayImage(headUrl, ivHeadIcon,
					MsApplication.options, new ImageLoadingListener() {

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
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub

						}
					});
		} else {
			LogUtil.d(TAG, "dataObject==null");
		}
	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		btExit.setOnClickListener(this);
	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "个人信息";
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
		case R.id.usercenter_userinfor_bt:
			MsApplication.clearToken();
			AppManager.finishAllActivity();
			Intent intent = new Intent(baseActivity, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			MsStartActivity.startActivity(baseActivity, intent);
			break;
		}
	}

}
