package cn.bluerhino.driver.controller.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRModelListRequest.BRModelListResponse;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.CityCarDetail;
import cn.bluerhino.driver.model.CityCarDetail.Detail;
import cn.bluerhino.driver.network.CityCarDetailRequest;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.view.LoadingDialog;

public class SelectCurrentCarModel extends BaseParentActivity {

	private static final String TAG = SelectCurrentCarModel.class.getSimpleName();

	@InjectView(R.id.listview_carmode)
	ListView listview_carmode;
	@InjectView(R.id.myactionbar)
	RelativeLayout myactionbar;
	@InjectView(R.id.left_img)
	ImageView left_img;
	@InjectView(R.id.center_title)
	TextView center_title;
	private Activity mActivity;
	private static List<Detail> mCarDetailList = new ArrayList<Detail>();
	private String mOrignCity;
	private SelectCurrentCarModelAdapter adapter;
	private LoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.current_car_mode);
		ButterKnife.inject(this);
		mActivity = this;
		initTitle();
		parseString();
		requestIntentAndGetData();

	}

	private void initTitle() {
		myactionbar.setBackgroundColor(getResources().getColor(R.color.commonly_actionbar_gray));
		left_img.setVisibility(View.VISIBLE);
		left_img.setImageResource(R.drawable.comment_left_back_button);
		left_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SelectCurrentCarModel.this.finish();
			}
		});
		center_title.setText("选择车型");
		center_title.setTextColor(getResources().getColor(R.color.main_text_color));
	}

	private void parseString() {
		// 解析传递来的唯一城市
		mOrignCity = getIntent().getStringExtra("CURRENTCITY");
	}

	private void requestIntentAndGetData() {
		initView();
		checkData();
	}

	private void checkData() {
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog(this);
		}
		getCityCarData();
	}

	private void initView() {
		adapter = new SelectCurrentCarModelAdapter();
		listview_carmode.setAdapter(adapter);
		listview_carmode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mCarDetailList != null && mCarDetailList.size() > 0) {
					String selectedItemString = mCarDetailList.get(position).getDetailName();
					String carType = String.valueOf(mCarDetailList.get(position).getCartType());
					String detailType = String.valueOf(mCarDetailList.get(position).getDetailId());

					Intent intent = new Intent();
					intent.putExtra("carmode_infoString", selectedItemString);
					intent.putExtra("carType", carType);
					intent.putExtra("detailType", detailType);
					// 返回detailName,CarType,detailType
					SelectCurrentCarModel.this.setResult(RESULT_OK, intent);
					mActivity.finish();
				}

			}
		});

	}

	class SelectCurrentCarModelAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = View.inflate(SelectCurrentCarModel.this, R.layout.select_current_carmode_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			if (mCarDetailList != null && mCarDetailList.size() > 0) {
				holder.carItem.setText(mCarDetailList.get(position).getDetailName());
			}

			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return mCarDetailList.get(position).getDetailName();
		}

		@Override
		public int getCount() {
			return mCarDetailList.size();
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

	static class ViewHolder {
		@InjectView(R.id.current_carmode_item_textview)
		TextView carItem;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			SelectCurrentCarModel.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 * @Description: 获取城市列表
	 * @author: sunliang
	 * @date 2015年9月14日 下午2:01:43
	 */
	private void getCityCarData() {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing() && !this.isFinishing()) {
			mLoadingDialog.show();
		}
		BRRequestParams params = new BRRequestParams();
		RequestQueue mRequestQueue = ApplicationController.getInstance().getRequestQueue();
		params.setDeviceId(ApplicationController.getInstance().getDeviceId());
		params.setVerCode(ApplicationController.getInstance().getVerCode());
		BRModelListResponse<List<CityCarDetail>> succeedListener = new BRModelListResponse<List<CityCarDetail>>() {
			@Override
			public void onResponse(List<CityCarDetail> model) {
				if (model != null) {
					for (CityCarDetail carDetail : model) {
						if (carDetail.getCityname().equals(mOrignCity)) {
							mCarDetailList.clear();
							mCarDetailList.addAll(carDetail.getCarDetail());
							break;
						}
					}
					adapter.notifyDataSetChanged();
				}
				if (mLoadingDialog != null && !isFinishing()) {
					mLoadingDialog.dismiss();
				}
			}
		};

		ErrorListener errorListener = new VolleyErrorListener(this) {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.d(TAG, "---车型列表获取失败---");
				ToastUtil.showToast(mContext, "车型列表获取失败");
				if (mLoadingDialog != null && isFinishing()) {
					mLoadingDialog.dismiss();
				}
			}
		};

		CityCarDetailRequest mDetailRequest = new CityCarDetailRequest.Builder().setSucceedListener(succeedListener)
				.setErrorListener(errorListener).setParams(params).build();
		if (mRequestQueue != null) {
			mRequestQueue.add(mDetailRequest);
		}
	}
}
