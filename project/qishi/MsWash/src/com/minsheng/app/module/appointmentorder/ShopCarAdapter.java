package com.minsheng.app.module.appointmentorder;

import java.util.List;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.minsheng.app.application.MsApplication;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.StringUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:购物车数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-4下午2:14:13
 * 
 */
public class ShopCarAdapter extends BaseListAdapter<SelectObject> {
	private static final String TAG = "ShopCarAdapter";
	private SparseBooleanArray isHideReduce = new SparseBooleanArray();
	private SparseArray<String> numData = new SparseArray<String>();
	private SelectChangeListener selectChangeListener;
	private Handler handler;

	public ShopCarAdapter(List<SelectObject> data, Context context,
			Handler handler) {
		super(data, context);
		// TODO Auto-generated constructor stub
		/**
		 * 初始化，默认减少按钮状态显示
		 */
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				isHideReduce.append(i, false);
			}
		}
		/**
		 * 初始化，数量默认显示空
		 */
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				numData.append(i, data.get(i).getProductAmount() + "");
			}
		}
		this.handler = handler;
		LogUtil.d(TAG, "按钮隐藏状态" + isHideReduce.toString());
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(R.layout.shopcar_item, parent,
					false);
		}
		final Button btReduce = (Button) ViewHolderUtil.getItemView(
				convertView, R.id.shopcar_item_type_reduce);
		Button btAdd = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.shopcar_item_type_add);
		final TextView tvNum = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.shopcar_item_type_num);
		TextView tvCategory = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.shopcar_item_type);
		if (isHideReduce.get(position)) {
			btReduce.setVisibility(View.GONE);
		} else {
			btReduce.setVisibility(View.VISIBLE);
		}
		tvNum.setText(baseListData.get(position).getProductAmount() + "");
		tvCategory.setText(baseListData.get(position).getProductName());
		btAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/**
				 * 更新缓存
				 */
				boolean isEmptyAdd = StringUtil.isEmpty(tvNum.getText() + "");
				int numAdd = 0;
				if (!isEmptyAdd) {
					numAdd = Integer.parseInt(tvNum.getText() + "");
				}
				baseListData.get(position).setProductAmount(numAdd + 1);
				int newAddNum = ++numAdd;
				tvNum.setText(newAddNum + "");
				isHideReduce.append(position, false);
				numData.append(position, newAddNum + "");
				LogUtil.d(TAG, "newAddNum==" + newAddNum);
				if (!btReduce.isShown()) {
					btReduce.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(
							basecontext, R.anim.show_in);
					btReduce.startAnimation(animation);
				}
				selectChangeListener.shopcarAmountChage();
				Message message = Message.obtain();
				handler.sendMessage(message);
			}
		});
		btReduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				boolean isEmpty = StringUtil.isEmpty(tvNum.getText() + "");
				int numReduce = 0;
				if (!isEmpty) {
					numReduce = Integer.parseInt(tvNum.getText() + "");
				}
				int newReduceNum = 0;
				if (numReduce > 1) {
					newReduceNum = numReduce - 1;
					tvNum.setText(newReduceNum + "");
					numData.append(position, newReduceNum + "");
					baseListData.get(position).setProductAmount(numReduce - 1);

				}
				if (numReduce == 1) {
					tvNum.setText("");
					btReduce.setVisibility(View.GONE);
					isHideReduce.append(position, true);
					numData.append(position, "");
					Animation animation = AnimationUtils.loadAnimation(
							basecontext, R.anim.show_out);
					btReduce.startAnimation(animation);
					baseListData.remove(position);
					// notifyDataSetChanged();
					if (baseListData != null & baseListData.size() == 0) {
						/**
						 * 如果数据集合为空，隐藏购物车
						 */
						Animation animationOUt = AnimationUtils.loadAnimation(
								basecontext, R.anim.scroll_out);
						CompleteOrder.rlShopcar.setVisibility(View.GONE);
						CompleteOrder.rlShopcar.setTag(false);
						CompleteOrder.rlShopcar.startAnimation(animationOUt);
					}

				}
				LogUtil.d(TAG, "newReduceNum==" + newReduceNum);
				selectChangeListener.shopcarAmountChage();
				Message message = Message.obtain();
				handler.sendMessage(message);
			}
		});
		return convertView;
	}

	public interface SelectChangeListener {
		public void shopcarAmountChage();
	}

	public void setSelectListerner(SelectChangeListener listener) {
		selectChangeListener = listener;
	}

}
