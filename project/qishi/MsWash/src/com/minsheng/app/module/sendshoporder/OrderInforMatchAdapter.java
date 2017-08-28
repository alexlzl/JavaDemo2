package com.minsheng.app.module.sendshoporder;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.entity.OrderDetail.Infor.Detail.OrderList;
import com.minsheng.app.module.sendshoporder.OrderInforMatch.ConfirmInforListener;
import com.minsheng.app.scan_one.CameraActivity;
import com.minsheng.app.scan_one.CameraActivity.ScanCallbackOne;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;

/**
 * 
 * @describe:订单信息匹配数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-14下午2:40:01
 * 
 */
public class OrderInforMatchAdapter extends BaseListAdapter<OrderList>
		implements ConfirmInforListener, ScanCallbackOne {
	private static final String TAG = "OrderInforMatchAdapter";
	public ArrayList<String> inputContent = new ArrayList<String>();
	private EditText scanET;
	private int scanPosition;
	private ListView lv;
	private boolean isScroll;

	public OrderInforMatchAdapter(List<OrderList> data, Object obj,
			Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
		OrderInforMatch.setConfirmListener(this);
		lv = (ListView) obj;

		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				inputContent.add(i, "");
			}
		}
		LogUtil.d(TAG, "初始化输入编码数据" + inputContent.toString());
		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					// 手指触屏拉动准备滚动，只触发一次 顺序: 1
					LogUtil.d(TAG, "滚动前");
					isScroll = true;
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
					// 持续滚动开始，只触发一次 顺序: 2
					LogUtil.d(TAG, "滚动开始");
					isScroll = true;
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					// 整个滚动事件结束，只触发一次 顺序: 4
					LogUtil.d(TAG, "滚动结束");
					isScroll = false;
					break;
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				// 一直在滚动中，多次触发 顺序: 3
				LogUtil.d(TAG, "滚动中");
				// isScroll = true;
			}
		});
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// if (convertView == null) {
		// convertView = baseInflater.inflate(R.layout.orderinfor_match_item,
		// parent, false);
		// }
		convertView = baseInflater.inflate(R.layout.orderinfor_match_item,
				parent, false);
		TextView tvSn = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.orderinfor_match_item_num);
		TextView tvName = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.orderinfor_match_item_type);
		final EditText etInput = (EditText) ViewHolderUtil.getItemView(
				convertView, R.id.orderinfor_match_item_input);
		ImageView ivScan = (ImageView) ViewHolderUtil.getItemView(convertView,
				R.id.orderinfor_match_item_scan);
		etInput.setText(inputContent.get(position));
		etInput.setTag(position);
		tvSn.setText(baseListData.get(position).getOneCode());
		tvName.setText(baseListData.get(position).getProductName());
		etInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				LogUtil.d(TAG, "输入中==" + arg0 + "==position==" + position);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				LogUtil.d(TAG, "输入前==" + arg0 + "==position==" + position);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

				// if (!isScroll) {
				//
				// inputContent.set(position, arg0.toString());
				// LogUtil.d(TAG, "输入完成==" + arg0.toString() + "position=="
				// + position + "==" + inputContent.toString());
				// } else {
				// LogUtil.d(TAG, "滚动中");
				// }
				inputContent.set(position, arg0.toString());
				LogUtil.d(TAG, "输入完成==" + arg0.toString() + "position=="
						+ position + "==" + inputContent.toString());

			}
		});
		// etInput.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View arg0, boolean arg1) {
		// // TODO Auto-generated method stub
		// if (arg1) {
		//
		// } else {
		// inputContent.set(position, arg0.toString());
		// }
		// }
		// });
		ivScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CameraActivity.setScanOneListener(OrderInforMatchAdapter.this);
				scanET = etInput;
				scanPosition = position;
				Intent intentScan = new Intent(basecontext,
						CameraActivity.class);
				intentScan.putExtra(MsConfiguration.FROM_WHERE_TO_SCAN,
						MsConfiguration.FROM_MODIFY_ORDER_INFOR);
				MsStartActivity.startActivity((Activity) basecontext,
						intentScan);
			}
		});
		return convertView;
	}

	@Override
	public void confirmListener() {
		// TODO Auto-generated method stub
		/**
		 * 提交数据
		 */
		int size = inputContent.size();
		for (int i = 0; i < size; i++) {
			String content = inputContent.get(i);
			if (StringUtil.isEmpty(content)) {
				MsToast.makeText(basecontext, "请输入完整衣服标识码").show();
				return;
			}
		}
		LogUtil.d(TAG, inputContent.toString());
	}

	@Override
	public void setScanResult(String result) {
		// TODO Auto-generated method stub
		scanET.setText(result);
		inputContent.set(scanPosition, result);
		LogUtil.d(TAG, "scanPosition=" + scanPosition + "更新后数据=="
				+ inputContent.toString());
	}

}
