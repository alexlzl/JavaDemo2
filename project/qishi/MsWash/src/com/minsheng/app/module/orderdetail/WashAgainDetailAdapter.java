package com.minsheng.app.module.orderdetail;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:返厂重洗订单详情页数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @2015-3-15下午10:10:59
 * 
 */
public class WashAgainDetailAdapter extends BaseListAdapter<String> {
	private static final String TAG = "OrderDetailAfreshAdapter";
	private SparseBooleanArray checkState = new SparseBooleanArray();
	private ListView mLv;
	public WashAgainDetailAdapter(List<String> data,Object obj, Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
		/**
		 * checkbox 默认未选中
		 */
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				checkState.append(i, false);
			}
		}
		mLv = (ListView) obj;
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.wash_again_orderdetail_item, parent, false);
		}
		final CheckBox cb = (CheckBox) ViewHolderUtil.getItemView(convertView,
				R.id.wash_again_orderdetail_item_cb);
		/**
		 * 此处监听必须在checkbox初始化状态前
		 * 添加监听器的代码在初始化checkBox属性的代码之后,也就是说当初始化checkBox属性时,由于可能改变其状态,
		 * 导致调用了onCheckedChange
		 * ()方法,而这个监听器是在上一次初始化的时候添加的,那么当然其index就是上一次的positon值
		 * ,而不是本次的,所以每次保存checkBox属性状态的时候
		 * ，都把值赋到的list集合里其它对象上去了,而不是与本次index相关的对象上,这才是发生莫名其妙错乱的真正原因.
		 */
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					checkState.append(position, true);
				} else {
					checkState.append(position, false);
				}
			}
		});
		if (checkState.get(position)) {
			cb.setChecked(true);
		} else {
			cb.setChecked(false);
		}
		// cb.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// /**
		// * 选择前更新所有视图状态
		// */
		//
		// // for (int i = 0; i < baseListData.size(); i++) {
		// // checkState.append(i, false);
		// // }
		//
		// // int size = mLv.getChildCount() - 1;
		// // for (int i = 0; i < size; i++) {
		// // View view = mLv.getChildAt(i);
		// // CheckBox cb = (CheckBox) view
		// // .findViewById(R.id.order_detail_item_afresh_check);
		// // if (cb != null) {
		// // cb.setChecked(false);
		// // }
		// //
		// // }
		// LogUtil.d(TAG, "选中状态" + cb.isChecked());
		// if (cb.isChecked()) {
		// cb.setChecked(true);
		// checkState.append(position, true);
		//
		// } else {
		// cb.setChecked(false);
		// checkState.append(position, false);
		//
		// }
		//
		// }
		// });

		return convertView;
	}

}
