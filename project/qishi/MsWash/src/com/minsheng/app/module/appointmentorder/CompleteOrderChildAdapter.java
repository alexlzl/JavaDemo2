package com.minsheng.app.module.appointmentorder;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.entity.CompleteOrderListEntity.Infor.CategoryListInfor.ClothesListInfor;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:子分类衣服列表数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-3下午8:25:04
 * 
 */
public class CompleteOrderChildAdapter extends
		BaseListAdapter<ClothesListInfor> {
	private static final String TAG = "ChildAdapter";
	private ListView lvChild;
	// 记录是否隐藏减少按钮
	private SparseBooleanArray isHideReduce = new SparseBooleanArray();
	// 记录衣服数量状态值
	private SparseArray<String> numData = new SparseArray<String>();
	private ChildCheckChange childCheckChage;
	private Handler handler;

	public CompleteOrderChildAdapter(List<ClothesListInfor> data, Object obj,
			Context context, Handler handler) {
		super(data, context);
		// TODO Auto-generated constructor stub
		this.lvChild = (ListView) obj;

		// 历史选择商品ID集合
		List<Integer> productIdlList = new ArrayList<Integer>();
		if (MsApplication.selectList != null
				&& MsApplication.selectList.size() > 0) {
			/**
			 * 存在选择历史数据
			 */
			for (int i = 0; i < MsApplication.selectList.size(); i++) {
				productIdlList.add(MsApplication.selectList.get(i)
						.getProductId());
			}
		}
		/**
		 * 初始化，数量默认显示空
		 */
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				int id = data.get(i).getProductId();
				if (productIdlList.contains(id)) {
					/**
					 * 匹配到衣服有选择历史记录
					 */
					int index = productIdlList.indexOf(id);
					numData.append(i, MsApplication.selectList.get(index)
							.getProductAmount() + "");
					LogUtil.d(TAG, "有历史数量"
							+ MsApplication.selectList.get(index)
									.getProductAmount());
					isHideReduce.append(i, false);
				} else {
					isHideReduce.append(i, true);
					numData.append(i, "");
					LogUtil.d(TAG, "没有历史数量");
				}

			}
		}
		this.handler = handler;
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.complete_order_child_item, parent, false);
		}
		TextView tvClothesInfor = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_child_item_category);
		TextView tvClothesPrice = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_child_item_price);
		LogUtil.d(TAG, "position==" + position);
		LogUtil.d(TAG, "lvChild child==" + lvChild.getCount());
		final Button btReduce = (Button) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_child_item_reduce);
		Button btAdd = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_child_item_add);
		final TextView tvNum = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_child_item_num);
		tvClothesInfor.setText(baseListData.get(position).getProductName());
		ViewUtil.setActivityPrice(tvClothesPrice, baseListData.get(position)
				.getSalePriceD());
		/**
		 * 是否隐藏减少按钮
		 */
		if (isHideReduce.get(position)) {
			btReduce.setVisibility(View.GONE);
		} else {
			btReduce.setVisibility(View.VISIBLE);
		}
		tvNum.setText(numData.get(position));
		btAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/**
				 * 添加衣服
				 */
				boolean isEmptyAdd = StringUtil.isEmpty(tvNum.getText() + "");
				int numAdd = 0;
				if (!isEmptyAdd) {
					numAdd = Integer.parseInt(tvNum.getText() + "");
				}
				LogUtil.d(TAG, "增加前数量==" + numAdd);
				int newAddNum = ++numAdd;
				tvNum.setText(newAddNum + "");
				isHideReduce.append(position, false);
				numData.append(position, newAddNum + "");
				if (!btReduce.isShown()) {
					btReduce.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(
							basecontext, R.anim.show_in);
					btReduce.startAnimation(animation);
				}

				LogUtil.d(TAG, "newAddNum==" + newAddNum);
				/**
				 * 更新缓存
				 */
				if (MsApplication.selectList != null
						&& MsApplication.selectList.size() == 0) {
					/**
					 * 数据集合为空的情况，存储新的数据
					 */
					SelectObject obj = new SelectObject();
					obj.setProductAmount(newAddNum);
					obj.setProductId(baseListData.get(position).getProductId());
					obj.setProductName(baseListData.get(position)
							.getProductName());
					MsApplication.selectList.add(obj);
					LogUtil.d(TAG,
							"第一次添加新产品" + MsApplication.selectList.toString());
				} else {
					/**
					 * 数据集合不为空
					 */

					int size = MsApplication.selectList.size();
					int productId = baseListData.get(position).getProductId();
					boolean isHasProduct = false;
					for (int i = 0; i < size; i++) {
						int id = MsApplication.selectList.get(i).getProductId();
						if (id == productId) {
							/**
							 * 集合中存在该产品，更新数量=====================1
							 */
							isHasProduct = true;
							int oldAmount = MsApplication.selectList.get(i)
									.getProductAmount();
							int newAmount = oldAmount + 1;
							MsApplication.selectList.get(i).setProductAmount(
									newAmount);
							LogUtil.d(TAG, "存在历史产品，增加更新数量"
									+ MsApplication.selectList.toString());
							break;
						}

					}
					if (!isHasProduct) {
						/**
						 * 不存在历史产品，添加到集合中================================2
						 */
						SelectObject obj = new SelectObject();
						obj.setProductAmount(newAddNum);
						obj.setProductId(baseListData.get(position)
								.getProductId());
						obj.setProductName(baseListData.get(position)
								.getProductName());
						MsApplication.selectList.add(obj);
						LogUtil.d(TAG, "不存在历史产品，添加新产品"
								+ MsApplication.selectList.toString());
					}
				}
				Message message = Message.obtain();
				handler.sendMessage(message);
			}
		});
		/**
		 * 减少数量操作
		 */
		btReduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				/**
				 * 减少衣服
				 */
				boolean isEmpty = StringUtil.isEmpty(tvNum.getText() + "");

				int numReduce = 0;
				if (!isEmpty) {
					numReduce = Integer.parseInt(tvNum.getText() + "");
				}
				LogUtil.d(TAG, "减少前数量==" + numReduce);
				if (numReduce > 1) {
					int newReduceNum = numReduce - 1;
					tvNum.setText(newReduceNum + "");
					numData.append(position, newReduceNum + "");
				}
				if (numReduce == 1) {
					btReduce.setVisibility(View.GONE);
					isHideReduce.append(position, true);
					numData.append(position, "");
					Animation animation = AnimationUtils.loadAnimation(
							basecontext, R.anim.show_out);
					btReduce.startAnimation(animation);
					tvNum.setText("");

				}
				/**
				 * 更新缓存======================
				 */

				int size = MsApplication.selectList.size();
				// 当前操作产品ID
				int productId = baseListData.get(position).getProductId();
				for (int i = 0; i < size; i++) {
					int id = MsApplication.selectList.get(i).getProductId();
					if (id == productId) {
						/**
						 * 集合中存在该产品
						 */
						// 获取历史产品数量
						int oldAmount = MsApplication.selectList.get(i)
								.getProductAmount();
						if (oldAmount == 1) {
							/**
							 * 删除该条数据
							 */
							MsApplication.selectList.remove(i);
							LogUtil.d(TAG, "删除数据"+i);
						} else {
							/**
							 * 减少数量
							 */
							int newAmount = oldAmount - 1;
							MsApplication.selectList.get(i).setProductAmount(
									newAmount);
						}

						LogUtil.d(TAG, "存在历史产品，减少更新数量"
								+ MsApplication.selectList.toString());
						break;
					}

				}
				Message message = Message.obtain();
				handler.sendMessage(message);
			}
		});
		return convertView;
	}

	public interface ChildCheckChange {
		public void childCheckChange(int position);
	}

	public void setCheckListener(ChildCheckChange listener) {
		childCheckChage = listener;
	}
}
