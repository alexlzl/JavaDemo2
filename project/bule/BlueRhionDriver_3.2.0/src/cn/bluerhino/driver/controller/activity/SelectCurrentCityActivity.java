package cn.bluerhino.driver.controller.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.bluerhino.library.network.VolleyErrorListener;
import com.bluerhino.library.network.framework.BRRequestParams;
import com.bluerhino.library.network.framework.BRModelListRequest.BRModelListResponse;
import com.bluerhino.library.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.server.LocationServer;
import cn.bluerhino.driver.model.BRLocation;
import cn.bluerhino.driver.model.CityCarDetail;
import cn.bluerhino.driver.network.CityCarDetailRequest;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.view.LoadingDialog;

public class SelectCurrentCityActivity extends BaseParentActivity {

	private static final String TAG = SelectCurrentCityActivity.class.getSimpleName();

	@InjectView(R.id.myactionbar)
	RelativeLayout myactionbar;
	@InjectView(R.id.left_img)
	ImageView left_img;

	@InjectView(R.id.center_title)
	TextView mCommonTitle;
	/**
	 * 定位的城市
	 */
	@InjectView(R.id.current_city_textview)
	TextView current_city_textview;

	@InjectView(R.id.city_gridview)
	GridView mCityGridView;

	private CityListAdapter mCityListAdapter;

	private static List<String> mCitysList = new ArrayList<String>();;

	private Resources mRes;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.current_city_location);
		ButterKnife.inject(this);
		mRes = getResources();
		mCityListAdapter = new CityListAdapter(this);
		mCityGridView.setAdapter(mCityListAdapter);
		getCityCarData();
		initTitle();
		initGridViewListener();

	}

	private void initTitle() {
		myactionbar.setBackgroundColor(mRes.getColor(R.color.commonly_actionbar_gray));
		left_img.setVisibility(View.VISIBLE);
		left_img.setImageResource(R.drawable.comment_left_back_button);
		left_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		String s = getIntent().getExtras().getString("CURRENTCITY");
		String title = (s == null) ? "" : s;
		mCommonTitle.setTextColor(mRes.getColor(R.color.main_text_color));
		mCommonTitle.setText(mRes.getString(R.string.city_current_selected) + title);
	}

	private void initLocationCity() {
		current_city_textview.setText(R.string.city_get_loc);
		final LocationServer locationServer = new LocationServer(getApplicationContext());
		locationServer.startLocation();
		locationServer.registerLocationListener(new LocationServer.BRLocationListener() {
			@Override
			public void onReceiveLocationComplete(BRLocation location) {
				locationServer.stopLocation();
				if (location != null && location.getCity() != null) {
					String city = location.getCity();
					// 去掉'市'
					city = city.substring(0, city.length() - 1);
					if (mCitysList != null && !mCitysList.contains(city)) {
						current_city_textview.setText(city + mRes.getString(R.string.city_not_yet_open));
					} else {
						current_city_textview.setText(city);
						current_city_textview.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								SelectCurrentCityActivity.this.setResult(RESULT_OK,
										new Intent().putExtra("currentCityString", current_city_textview.getText()));
								SelectCurrentCityActivity.this.finish();
							}
						});
					}
				} else {
					current_city_textview.setText(R.string.city_getdata_err);
				}
			}
		});
	}

	private void initGridViewListener() {

		mCityGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String currentCityString = mCitysList.get(position);
				setResult(RESULT_OK, new Intent().putExtra("currentCityString", currentCityString));
				finish();

			}
		});

	}

	private static class CityListAdapter extends BaseAdapter {

		private Context mContext;

		public CityListAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			if (mCitysList != null) {
				return mCitysList.size();
			} else {
				return 0;
			}

		}

		@Override
		public Object getItem(int position) {
			if (mCitysList != null) {
				return mCitysList.get(position);
			} else {
				return null;
			}

		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = View.inflate(mContext, R.layout.select_current_city_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			holder.cityItem.setText(mCitysList.get(position));
			return convertView;
		}

	}

	static class ViewHolder {
		@InjectView(R.id.current_city_item_textview)
		TextView cityItem;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// if(mCitysList != null){
		// mCitysList.clear();
		// mCitysList = null;
		// }
		// if(mCityListAdapter != null){
		// mCityListAdapter = null;
		// }
		// if(mCityGridView != null){
		// mCityGridView = null;
		// }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private LoadingDialog mLoadingDialog;

	private void getCityCarData() {
		mLoadingDialog = new LoadingDialog(this);
		if (mLoadingDialog != null && !mLoadingDialog.isShowing() && !isFinishing()) {
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
					mCitysList.clear();
					for (CityCarDetail carDetail : model) {
						mCitysList.add(carDetail.getCityname());
					}
					mCityListAdapter.notifyDataSetChanged();
					initLocationCity();
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
				if (mLoadingDialog != null && !isFinishing()) {
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
