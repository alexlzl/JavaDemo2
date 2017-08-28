package com.minsheng.app.module.appointmentorder;

import java.util.List;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.entity.CompleteOrderListEntity.Infor.CategoryListInfor;
import com.minsheng.app.util.LogUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:完善订单父分类数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-3下午8:24:48
 * 
 */
public class CompleteOrderParentAdapter extends
		BaseListAdapter<CategoryListInfor> {
	private static final String TAG = "ParentAdapter";
	private SparseBooleanArray stateArray = new SparseBooleanArray();
	private CategoryChange checkChange;
	private ListView lvParent;

	public CompleteOrderParentAdapter(List<CategoryListInfor> data, Object obj,
			Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
		/**
		 * 初始化分类默认状态,默认第一个选中
		 */
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				if (i == 0) {
					stateArray.append(i, true);
				} else {
					stateArray.append(i, false);
				}

			}

		}
		this.lvParent = (ListView) obj;
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.complete_order_parent_item, parent, false);
		}
		final TextView tvClothesCategory = (TextView) ViewHolderUtil
				.getItemView(convertView, R.id.complete_order_parent_item_type);
		final View line = ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_parent_item_line);
		tvClothesCategory.setText(baseListData.get(position).getCategoryName());
		LogUtil.d(TAG, "状态值" + stateArray.toString());
		if (!stateArray.get(position)) {
			/**
			 * 未选中状态
			 */
			tvClothesCategory.setBackgroundResource(R.color.colore9fff4);
			line.setVisibility(View.VISIBLE);
		} else {
			/**
			 * 选中状态
			 */
			tvClothesCategory.setBackgroundResource(R.color.white);
			line.setVisibility(View.GONE);
		}
		/**
		 * 文本点击事件
		 */
		tvClothesCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (stateArray.get(position)) {
					/**
					 * 处于选中状态
					 */
					LogUtil.d(TAG, "选中" + position);
				} else {
					/**
					 * 非选中状态
					 */
					LogUtil.d(TAG, "未选中" + position);
					int length = stateArray.size();
					for (int i = 0; i < length; i++) {
						stateArray.append(i, false);
						View item = lvParent.getChildAt(i);
						if (item != null) {
							TextView tv = (TextView) item
									.findViewById(R.id.complete_order_parent_item_type);
							tv.setBackgroundResource(R.color.colore9fff4);
							View line = item
									.findViewById(R.id.complete_order_parent_item_line);
							line.setVisibility(View.VISIBLE);
						}

					}

					stateArray.append(position, true);
					tvClothesCategory.setBackgroundResource(R.color.white);
					line.setVisibility(View.GONE);
					/**
					 * 回调刷新数据
					 */
					checkChange.checkChange(position);
				}

			}
		});
		return convertView;
	}

	public interface CategoryChange {
		public void checkChange(int position);
	}

	public void setCheckListener(CategoryChange listener) {
		checkChange = listener;
	}

}
