package com.minsheng.app.module.orderdetail;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.entity.OrderDetail.Infor.Detail.OrderList;
import com.minsheng.app.entity.WashAgainParam;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:订单详情数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-6下午4:47:42
 * 
 */
public class UniversalOrderDetailAdapter extends BaseListAdapter<OrderList> {
	private static final String TAG = "UniversalOrderDetailAdapter";
	private SparseBooleanArray checkState = new SparseBooleanArray();
	public List<WashAgainParam> washAgainList = new ArrayList<WashAgainParam>();

	public UniversalOrderDetailAdapter(List<OrderList> data, Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
		if (MsConfiguration.PAGE_TYPE_WASH_AGAIN
				.equals(UniversalOrderDetail.pageType)) {
			/**
			 * 返厂重新页面
			 */
			/**
			 * checkbox 默认未选中
			 */

			if (data != null) {
				int length = data.size();
				for (int i = 0; i < length; i++) {
					checkState.append(i, false);

				}
			}
		}

		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				washAgainList.add(i, null);

			}
		}

	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.universal_order_detail_item, parent, false);
		}
		TextView tvClothesName = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.universal_order_detail_item_name);
		Button btAgainIcon = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.universal_order_detail_item_again_icon);
		TextView tvRemark = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.universal_order_detail_item_remark);
		TextView tvOneCode = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.universal_order_detail_item_sn);
		TextView tvPrice = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.universal_order_detail_item_price);
		int isWash = baseListData.get(position).getIsToWash();
		switch (isWash) {
		case 0:
			/**
			 * 未重洗
			 */
			btAgainIcon.setVisibility(View.GONE);
			break;
		case 1:
			/**
			 * 重洗过
			 */
			if (MsConfiguration.PAGE_TYPE_WASH_AGAIN
					.equals(UniversalOrderDetail.pageType)) {
				/**
				 * 第二次重洗
				 */
				btAgainIcon.setVisibility(View.GONE);
			} else {
				/**
				 * 第一次重洗
				 */
				btAgainIcon.setVisibility(View.VISIBLE);
			}

			break;
		}
		/**
		 * 返厂重洗========================================
		 */

		if (MsConfiguration.PAGE_TYPE_WASH_AGAIN
				.equals(UniversalOrderDetail.pageType)) {
			final CheckBox cb = (CheckBox) ViewHolderUtil.getItemView(
					convertView, R.id.universal_order_detail_item_ck);
			cb.setVisibility(View.VISIBLE);
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
						/**
						 * 选中
						 */
						checkState.append(position, true);
						WashAgainParam param = new WashAgainParam();
						param.setOrderProductId(baseListData.get(position)
								.getOrderProductId());
						param.setProductName(baseListData.get(position).getProductName());
						washAgainList.add(position, param);

					} else {
						/**
						 * 未选中
						 */
						checkState.append(position, false);
						washAgainList.set(position, null);
					}
				}
			});
			if (checkState.get(position)) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}
		}

		ViewUtil.setActivityPrice(tvPrice, baseListData.get(position)
				.getSellPriceD() + "");
		if (!StringUtil.isEmpty(baseListData.get(position).getProductTag())
				&& !StringUtil.isEmpty(baseListData.get(position).getRemark())) {
			tvRemark.setText(baseListData.get(position).getProductTag() + "("
					+ baseListData.get(position).getRemark() + ")");
		}
		if (StringUtil.isEmpty(baseListData.get(position).getProductTag())
				&& StringUtil.isEmpty(baseListData.get(position).getRemark())) {
			tvRemark.setText("");
			tvRemark.setVisibility(View.GONE);
		}
		if (StringUtil.isEmpty(baseListData.get(position).getProductTag())
				&& !StringUtil.isEmpty(baseListData.get(position).getRemark())) {
			tvRemark.setText(baseListData.get(position).getRemark());
		}
		if (!StringUtil.isEmpty(baseListData.get(position).getProductTag())
				&& StringUtil.isEmpty(baseListData.get(position).getRemark())) {
			tvRemark.setText(baseListData.get(position).getProductTag());
		}
		tvClothesName.setText(baseListData.get(position).getProductName());
		tvOneCode.setText(baseListData.get(position).getOneCode());
		return convertView;
	}

}
