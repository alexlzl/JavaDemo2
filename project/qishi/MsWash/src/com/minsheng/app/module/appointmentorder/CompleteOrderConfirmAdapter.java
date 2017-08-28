package com.minsheng.app.module.appointmentorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.configuration.MsRequestConfiguration;
import com.minsheng.app.entity.AddShopCarEntity.Infor.ShopCarList;
import com.minsheng.app.entity.ModifyState;
import com.minsheng.app.http.BasicHttpClient;
import com.minsheng.app.scan_one.CameraActivity;
import com.minsheng.app.scan_one.CameraActivity.ScanCallbackOne;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.app.view.MsToast;
import com.minsheng.wash.R;

/**
 * 
 * @describe:完善订单确认页面数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-4下午5:46:19
 * 
 */
public class CompleteOrderConfirmAdapter extends BaseListAdapter<ShopCarList>
		implements ScanCallbackOne {
	private static final String TAG = "CompleteOrderConfirmAdapter";
	private RemoveCallback mcallback;
	private Button currentBt;
	// 扫描结果单号文本
	private TextView orderNumTv;
	// 扫描控件文本
	private Button mbtScan;
	private int currentPosition;
	// 标记扫描控件是否显示
	public ArrayList<Boolean> isHideScan = new ArrayList<Boolean>();
	// 标记一维码内容
	public ArrayList<String> oneCodeList = new ArrayList<String>();
	// 标记隐藏区域是否显示
	public ArrayList<Boolean> isHide = new ArrayList<Boolean>();
	// 存储衣服备注数据
	public ArrayList<String> remarkInfor = new ArrayList<String>();
	// 存储衣服备注选择内容
	public ArrayList<ConfirmOrderCheckObject> checkContent = MsApplication.checkContent;
	// 是否是首单价格
	public ArrayList<Boolean> isFirstOrderList = new ArrayList<Boolean>();
	private ModifyState deleteProductBean;
	private int deletePosition;
	private SwipeListView mListView;
	static final int ANIMATION_DURATION = 200;
	private View itemView;

	public CompleteOrderConfirmAdapter(List<ShopCarList> data, Object obj,
			Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
		mListView = (SwipeListView) obj;
		/**
		 * 通过判断一维码是否为空，存储扫描控件是否隐藏
		 */
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				if (StringUtil.isEmpty(data.get(i).getOneCode())) {
					/**
					 * 一维码为空，显示扫描控件
					 */
					isHideScan.add(i, false);
				} else {
					/**
					 * 一维码不为空，隐藏扫描控件
					 */
					isHideScan.add(i, true);
				}

			}
		}

		/**
		 * 存储一维码值
		 */
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				String onecode = data.get(i).getOneCode();
				oneCodeList.add(i, onecode);
			}
		}

		/**
		 * 存储标记备注区域是否显示状态，初始化默认都隐藏
		 */
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				isHide.add(i, false);
			}
		}
		/**
		 * 初始化存储衣服备注选择内容
		 */
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				ConfirmOrderCheckObject checkObj = new ConfirmOrderCheckObject();
				// 获取返回标记
				String tagString = data.get(i).getRemarkTag();
				String remakString = data.get(i).getRemark();
				if (!StringUtil.isEmpty(tagString)) {
					/**
					 * 标签内容不为空==================
					 */
					if (tagString.contains("发黑")) {
						checkObj.setCheckOne("发黑");
						checkObj.setChcekStateOne(true);
					} else {
						checkObj.setCheckOne("");
						checkObj.setChcekStateOne(false);
					}
					if (tagString.contains("油渍")) {
						checkObj.setCheckTwo("油渍");
						checkObj.setChcekStateTwo(true);
					} else {
						checkObj.setCheckTwo("");
						checkObj.setChcekStateTwo(false);
					}
					if (tagString.contains("磨损")) {
						checkObj.setCheckThree("磨损");
						checkObj.setChcekStateThree(true);
					} else {
						checkObj.setCheckThree("");
						checkObj.setChcekStateThree(false);
					}
					if (tagString.contains("破损")) {
						checkObj.setCheckFour("破损");
						checkObj.setChcekStateFour(true);
					} else {
						checkObj.setCheckFour("");
						checkObj.setChcekStateFour(false);
					}
					if (tagString.contains("少扣")) {
						checkObj.setCheckFive("少扣");
						checkObj.setChcekStateFive(true);
					} else {
						checkObj.setChcekStateFive(false);
						checkObj.setCheckFive("");
					}

				} else {
					/**
					 * 标签内容为空==================
					 */
					checkObj.setCheckOne("");
					checkObj.setCheckTwo("");
					checkObj.setCheckThree("");
					checkObj.setCheckFour("");
					checkObj.setCheckFive("");
					checkObj.setChcekStateOne(false);
					checkObj.setChcekStateTwo(false);
					checkObj.setChcekStateThree(false);
					checkObj.setChcekStateFour(false);
					checkObj.setChcekStateFive(false);
				}
				/**
				 * 缓存备注内容
				 */
				if (StringUtil.isEmpty(remakString)) {
					checkObj.setCheckSix("");

				} else {
					checkObj.setCheckSix(remakString);

				}

				if (!StringUtil.isEmpty(tagString)
						&& !StringUtil.isEmpty(remakString)) {
					remarkInfor.add(i, tagString + ", (" + remakString + ")");
				}
				if (StringUtil.isEmpty(tagString)
						&& !StringUtil.isEmpty(remakString)) {
					remarkInfor.add(i, remakString);
				}
				if (!StringUtil.isEmpty(tagString)
						&& StringUtil.isEmpty(remakString)) {
					remarkInfor.add(i, tagString);
				}
				if (StringUtil.isEmpty(tagString)
						&& StringUtil.isEmpty(remakString)) {
					remarkInfor.add(i, "");
				}
				checkContent.add(i, checkObj);
			}
		}

		/**
		 * 记录是否选择一元洗衣状态==========================================
		 */
		if (data != null && data.size() > 0) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				int isFirshOrder = data.get(i).getIsFirst();
				switch (isFirshOrder) {
				case 0:
					/**
					 * 非首单价格
					 */
					isFirstOrderList.add(i, false);
					break;
				case 1:
					/**
					 * 首单价格
					 */
					isFirstOrderList.add(i, true);
					break;
				}

			}
		}
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final View item;
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.complete_order_confirm_item, parent, false);
			item = convertView;
		} else {
			item = convertView;
		}
		Button btDelete = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_remove);
		Button btScanRepeat = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_scanrepeat);
		final Button btCheck = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_check);
		final TextView tvNum = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_num);
		final Button btscan = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_scan);
		TextView tvNmae = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_type);
		TextView tvOneCode = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_num);
		final CheckBox cb = (CheckBox) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_check_one);

		final EditText etRemarkOther = (EditText) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_checkt_et);
		RelativeLayout rlItem = (RelativeLayout) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_topparent);
		final RelativeLayout rlIsHide = (RelativeLayout) ViewHolderUtil
				.getItemView(convertView,
						R.id.complete_order_confirm_item_hideparent);
		final ImageView ivArrow = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_arrow);
		final TextView tvCheckOne = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_checkone);
		final TextView tvCheckTwo = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_checktwo);
		final TextView tvCheckThree = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_checkthree);
		final TextView tvCheckFour = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_checkfour);
		final TextView tvCheckFive = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_checkfive);
		Button btCheckOver = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_checkt_over_bt);
		final TextView tvRemark = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.complete_order_confirm_item_remark);
		/**
		 * 绑定数据
		 */
		tvNmae.setText(baseListData.get(position).getProductName());
		tvOneCode.setText(baseListData.get(position).getOneCode());
		if (isFirstOrderList.get(position)) {
			cb.setChecked(true);
		} else {
			cb.setChecked(false);
		}
		cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/**
				 * 选择前将所有的一元选择状态置为非选中
				 */
				if (isFirstOrderList.get(position)) {
					/**
					 * 已经选中==================
					 */
					for (int i = 0; i < isFirstOrderList.size(); i++) {
						isFirstOrderList.set(i, false);
						LogUtil.d(TAG, "遍历状态" + false);
					}

					LogUtil.d(TAG, "遍历完" + isFirstOrderList.toString());
					int length = mListView.getChildCount();
					for (int i = 0; i < length; i++) {
						View child = mListView.getChildAt(i);
						CheckBox cbChild = (CheckBox) child
								.findViewById(R.id.complete_order_confirm_item_check_one);
						if (cbChild != null) {
							cbChild.setChecked(false);
						}
						LogUtil.d(TAG, "cb状态" + false);
					}
					cb.setChecked(false);
					isFirstOrderList.set(position, false);
					LogUtil.d(TAG, "更新后所有状态" + isFirstOrderList.toString());
					LogUtil.d(
							TAG,
							"选中状态" + "位置" + position
									+ isFirstOrderList.get(position));
				} else {
					/**
					 * 未选中======================
					 */
					for (int i = 0; i < isFirstOrderList.size(); i++) {
						isFirstOrderList.set(i, false);
						LogUtil.d(TAG, "遍历状态" + false);
					}

					LogUtil.d(TAG, "遍历完" + isFirstOrderList.toString());
					int length = mListView.getChildCount();
					for (int i = 0; i < length; i++) {
						View child = mListView.getChildAt(i);
						CheckBox cbChild = (CheckBox) child
								.findViewById(R.id.complete_order_confirm_item_check_one);
						if (cbChild != null) {
							cbChild.setChecked(false);
						}
						LogUtil.d(TAG, "cb状态" + false);
					}
					cb.setChecked(true);
					isFirstOrderList.set(position, true);
					LogUtil.d(TAG, "更新后所有状态" + isFirstOrderList.toString());
					LogUtil.d(
							TAG,
							"选中状态" + "位置" + position
									+ isFirstOrderList.get(position));

				}

			}

		});

		/**
		 * 设置备注数据
		 */
		if (!StringUtil.isEmpty(remarkInfor.get(position))) {
			tvRemark.setVisibility(View.VISIBLE);
			tvRemark.setText(remarkInfor.get(position));
		} else {
			tvRemark.setVisibility(View.GONE);
		}

		/**
		 * 判断是否是隐藏区域
		 */
		if (isHide.get(position)) {
			/**
			 * 显示备注区域
			 */
			rlIsHide.setVisibility(View.VISIBLE);
			ivArrow.setImageResource(R.drawable.arrow_down);
			LayoutParams lp = new LayoutParams(
					ViewUtil.dip2px(basecontext, 14), ViewUtil.dip2px(
							basecontext, 7));
			lp.leftMargin = ViewUtil.dip2px(basecontext, 30);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			ivArrow.setLayoutParams(lp);
		} else {
			/**
			 * 隐藏备注区域
			 */
			rlIsHide.setVisibility(View.GONE);
			ivArrow.setImageResource(R.drawable.home_orderlist_arrow);
			LayoutParams lp = new LayoutParams(ViewUtil.dip2px(basecontext, 7),
					ViewUtil.dip2px(basecontext, 14));
			lp.leftMargin = ViewUtil.dip2px(basecontext, 30);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			ivArrow.setLayoutParams(lp);
		}
		/**
		 * 判断是否显示扫描控件
		 */
		if (!isHideScan.get(position)) {
			btscan.setVisibility(View.VISIBLE);
		} else {
			btscan.setVisibility(View.GONE);
		}
		/**
		 * 判断是否显示扫描完成控件
		 */
		if (!isHideScan.get(position)) {
			btCheck.setVisibility(View.GONE);
		} else {
			btCheck.setVisibility(View.VISIBLE);
		}
		// 绑定一维码数据
		tvNum.setText(oneCodeList.get(position));
		/**
		 * 删除数据事件
		 */
		btDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// MsToast.makeText(basecontext, "TEST" + position).show();
				/**
				 * 更新移除位置的状态记录
				 */
				deleteProduct(position, item);

			}
		});
		/**
		 * 重复扫描二维码事件
		 */
		btScanRepeat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				currentBt = btCheck;
				orderNumTv = tvNum;
				mbtScan = btscan;
				currentPosition = position;
				Intent intent = new Intent((Activity) basecontext,
						CameraActivity.class);
				intent.putExtra(MsConfiguration.FROM_WHERE_TO_SCAN,
						MsConfiguration.FROM_CONFIRM_ORDER);
				MsStartActivity.startActivity((Activity) basecontext, intent);
			}
		});
		// 二维码扫描监听
		CameraActivity.setScanOneListener(this);
		btscan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				currentBt = btCheck;
				orderNumTv = tvNum;
				mbtScan = btscan;
				currentPosition = position;
				Intent intent = new Intent((Activity) basecontext,
						CameraActivity.class);
				intent.putExtra(MsConfiguration.FROM_WHERE_TO_SCAN,
						MsConfiguration.FROM_CONFIRM_ORDER);
				MsStartActivity.startActivity((Activity) basecontext, intent);
			}
		});
		/**
		 * 隐藏区域显示控制
		 */
		rlItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isHide.get(position)) {
					/**
					 * 显示备注区域=======================
					 */
					rlIsHide.setVisibility(View.VISIBLE);
					ivArrow.setImageResource(R.drawable.arrow_down);
					isHide.set(position, true);
					LayoutParams lp = new LayoutParams(ViewUtil.dip2px(
							basecontext, 14), ViewUtil.dip2px(basecontext, 7));
					lp.leftMargin = ViewUtil.dip2px(basecontext, 30);
					lp.addRule(RelativeLayout.CENTER_VERTICAL);
					ivArrow.setLayoutParams(lp);
					Animation animation = AnimationUtils.loadAnimation(
							basecontext, R.anim.hide_in);
					rlIsHide.startAnimation(animation);
				} else {
					/**
					 * 隐藏备注区域========================
					 */
					rlIsHide.setVisibility(View.GONE);
					isHide.set(position, false);
					/**
					 * 重置箭头布局
					 */
					ivArrow.setImageResource(R.drawable.home_orderlist_arrow);
					LayoutParams lp = new LayoutParams(ViewUtil.dip2px(
							basecontext, 7), ViewUtil.dip2px(basecontext, 14));
					lp.leftMargin = ViewUtil.dip2px(basecontext, 30);
					lp.addRule(RelativeLayout.CENTER_VERTICAL);
					ivArrow.setLayoutParams(lp);
					/**
					 * 刷新备注文本框内容
					 */
					LogUtil.d(TAG, "备注区内容"
							+ checkContent.get(position).toString());
					StringBuilder sb = new StringBuilder();
					if (!StringUtil.isEmpty(checkContent.get(position)
							.getCheckOne())) {
						if (!StringUtil.isEmpty(checkContent.get(position)
								.getCheckTwo())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckThree())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckFour())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckFive())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckSix())) {
							sb.append(checkContent.get(position).getCheckOne()
									+ ", ");
						} else {
							sb.append(checkContent.get(position).getCheckOne()
									+ "");
						}

					}
					if (!StringUtil.isEmpty(checkContent.get(position)
							.getCheckTwo())) {
						if (!StringUtil.isEmpty(checkContent.get(position)
								.getCheckThree())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckFour())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckFive())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckSix())) {
							sb.append(checkContent.get(position).getCheckTwo()
									+ ", ");
						} else {
							sb.append(checkContent.get(position).getCheckTwo()
									+ "");
						}

					}
					if (!StringUtil.isEmpty(checkContent.get(position)
							.getCheckThree())) {
						if (!StringUtil.isEmpty(checkContent.get(position)
								.getCheckFour())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckFive())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckSix())) {
							sb.append(checkContent.get(position)
									.getCheckThree() + ",  ");
						} else {
							sb.append(checkContent.get(position)
									.getCheckThree() + "");
						}

					}
					if (!StringUtil.isEmpty(checkContent.get(position)
							.getCheckFour())) {
						if (!StringUtil.isEmpty(checkContent.get(position)
								.getCheckFive())
								|| !StringUtil.isEmpty(checkContent.get(
										position).getCheckSix())) {
							sb.append(checkContent.get(position).getCheckFour()
									+ ", ");
						} else {
							sb.append(checkContent.get(position).getCheckFour()
									+ "");
						}

					}
					if (!StringUtil.isEmpty(checkContent.get(position)
							.getCheckFive())) {
						if (!StringUtil.isEmpty(etRemarkOther.getText()
								.toString())) {
							sb.append(checkContent.get(position).getCheckFive()
									+ ", ");
						} else {
							sb.append(checkContent.get(position).getCheckFive()
									+ "");
						}

					}
					if (!StringUtil.isEmpty(etRemarkOther.getText().toString())) {
						sb.append("(" + etRemarkOther.getText().toString()
								+ ")");
					}
					// 存储当前选择备注
					remarkInfor.set(position, sb.toString());
					if (StringUtil.isEmpty(sb.toString())) {
						tvRemark.setText("");
						tvRemark.setVisibility(View.GONE);
					} else {
						tvRemark.setText(sb.toString());
						tvRemark.setVisibility(View.VISIBLE);
					}
				}

			}
		});
		/**
		 * 绑定显示
		 */
		if (checkContent.get(position).isChcekStateOne) {
			tvCheckOne.setBackgroundResource(R.drawable.bg_green_conrner);
			tvCheckOne.setTextColor(basecontext.getResources().getColor(
					R.color.white));
		} else {
			tvCheckOne
					.setBackgroundResource(R.drawable.edittext_bg_solid_white);
			tvCheckOne.setTextColor(basecontext.getResources().getColor(
					R.color.color26cbb2));
		}
		if (checkContent.get(position).isChcekStateTwo) {
			tvCheckTwo.setBackgroundResource(R.drawable.bg_green_conrner);
			tvCheckTwo.setTextColor(basecontext.getResources().getColor(
					R.color.white));
		} else {

			tvCheckTwo
					.setBackgroundResource(R.drawable.edittext_bg_solid_white);
			tvCheckTwo.setTextColor(basecontext.getResources().getColor(
					R.color.color26cbb2));
		}
		if (checkContent.get(position).isChcekStateThree) {

			tvCheckThree.setBackgroundResource(R.drawable.bg_green_conrner);
			tvCheckThree.setTextColor(basecontext.getResources().getColor(
					R.color.white));
		} else {

			tvCheckThree
					.setBackgroundResource(R.drawable.edittext_bg_solid_white);
			tvCheckThree.setTextColor(basecontext.getResources().getColor(
					R.color.color26cbb2));
		}
		if (checkContent.get(position).isChcekStateFour) {

			tvCheckFour.setBackgroundResource(R.drawable.bg_green_conrner);
			tvCheckFour.setTextColor(basecontext.getResources().getColor(
					R.color.white));
		} else {

			tvCheckFour
					.setBackgroundResource(R.drawable.edittext_bg_solid_white);
			tvCheckFour.setTextColor(basecontext.getResources().getColor(
					R.color.color26cbb2));
		}
		if (checkContent.get(position).isChcekStateFive) {

			tvCheckFive.setBackgroundResource(R.drawable.bg_green_conrner);
			tvCheckFive.setTextColor(basecontext.getResources().getColor(
					R.color.white));
		} else {

			tvCheckFive
					.setBackgroundResource(R.drawable.edittext_bg_solid_white);
			tvCheckFive.setTextColor(basecontext.getResources().getColor(
					R.color.color26cbb2));
		}

		etRemarkOther.setText(checkContent.get(position).getCheckSix());
		// LogUtil.d(TAG, "输入框的值" + position
		// + checkContent.get(position).getCheckSix());
		/**
		 * 处理选择衣服备注事件
		 */
		// final ConfirmOrderCheckObject checkObj = checkContent.get(position);
		etRemarkOther
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							// 此处为得到焦点时的处理内容
							if (checkContent != null && checkContent.size() > 0) {
								checkContent.get(position).setCheckSix(
										etRemarkOther.getText().toString());
							}

						} else {
							// 此处为失去焦点时的处理内容
							if (checkContent != null && checkContent.size() > 0) {
								checkContent.get(position).setCheckSix(
										etRemarkOther.getText().toString());
							}

						}
					}
				});
		// etRemarkOther.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence arg0, int arg1, int arg2,
		// int arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence arg0, int arg1,
		// int arg2, int arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable arg0) {
		// // TODO Auto-generated method stub
		// // checkContent.get(position).setCheckSix(arg0.toString());
		// // checkObj.setCheckSix(arg0.toString());
		// checkObj.setCheckSix(etRemarkOther.getText().toString());
		// LogUtil.d(TAG, "输入完成" + position + arg0.toString()
		// + checkContent.toString());
		// }
		// });
		tvCheckOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!checkContent.get(position).isChcekStateOne) {
					/**
					 * 未选择
					 */

					tvCheckOne
							.setBackgroundResource(R.drawable.bg_green_corner_2);
					tvCheckOne.setTextColor(basecontext.getResources()
							.getColor(R.color.white));
					checkContent.get(position).setChcekStateOne(true);
					checkContent.get(position).setCheckOne(
							tvCheckOne.getText().toString());
					LogUtil.d(TAG, "存储标签1" + tvCheckOne.getText().toString());
				} else {
					/**
					 * 已经选择
					 */
					tvCheckOne
							.setBackgroundResource(R.drawable.edittext_bg_solid_white);
					tvCheckOne.setTextColor(basecontext.getResources()
							.getColor(R.color.color26cbb2));

					checkContent.get(position).setChcekStateOne(false);
					checkContent.get(position).setCheckOne("");
					LogUtil.d(TAG, "存储标签1" + tvCheckOne.getText().toString());
				}

			}
		});

		tvCheckTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!checkContent.get(position).isChcekStateTwo) {
					/**
					 * 未选择
					 */

					tvCheckTwo
							.setBackgroundResource(R.drawable.bg_green_corner_2);
					tvCheckTwo.setTextColor(basecontext.getResources()
							.getColor(R.color.white));
					checkContent.get(position).setChcekStateTwo(true);
					checkContent.get(position).setCheckTwo(
							tvCheckTwo.getText().toString());
					LogUtil.d(TAG, "存储标签2" + tvCheckTwo.getText().toString());
				} else {
					/**
					 * 已经选择
					 */

					tvCheckTwo
							.setBackgroundResource(R.drawable.edittext_bg_solid_white);
					tvCheckTwo.setTextColor(basecontext.getResources()
							.getColor(R.color.color26cbb2));
					checkContent.get(position).setChcekStateTwo(false);
					checkContent.get(position).setCheckTwo("");
					LogUtil.d(TAG, "存储标签2"
							+ checkContent.get(position).getCheckTwo());
				}
			}
		});
		tvCheckThree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!checkContent.get(position).isChcekStateThree) {
					/**
					 * 未选择
					 */
					tvCheckThree
							.setBackgroundResource(R.drawable.bg_green_corner_2);
					tvCheckThree.setTextColor(basecontext.getResources()
							.getColor(R.color.white));

					checkContent.get(position).setChcekStateThree(true);
					checkContent.get(position).setCheckThree(
							tvCheckThree.getText().toString());
					LogUtil.d(TAG, "存储标签3" + tvCheckThree.getText().toString());
				} else {
					/**
					 * 已经选择
					 */
					tvCheckThree
							.setBackgroundResource(R.drawable.edittext_bg_solid_white);
					tvCheckThree.setTextColor(basecontext.getResources()
							.getColor(R.color.color26cbb2));

					checkContent.get(position).setChcekStateThree(false);
					checkContent.get(position).setCheckThree("");
					LogUtil.d(TAG, "存储标签3"
							+ checkContent.get(position).getCheckThree());
				}
			}
		});
		tvCheckFour.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!checkContent.get(position).isChcekStateFour) {
					/**
					 * 未选择
					 */
					tvCheckFour
							.setBackgroundResource(R.drawable.bg_green_corner_2);
					tvCheckFour.setTextColor(basecontext.getResources()
							.getColor(R.color.white));

					checkContent.get(position).setChcekStateFour(true);
					checkContent.get(position).setCheckFour(
							tvCheckFour.getText().toString());
					LogUtil.d(TAG, "存储标签4" + tvCheckFour.getText().toString());
				} else {
					/**
					 * 已经选择
					 */
					tvCheckFour
							.setBackgroundResource(R.drawable.edittext_bg_solid_white);
					tvCheckFour.setTextColor(basecontext.getResources()
							.getColor(R.color.color26cbb2));

					checkContent.get(position).setChcekStateFour(false);
					checkContent.get(position).setCheckFour("");
					LogUtil.d(TAG, "存储标签4"
							+ checkContent.get(position).getCheckFour());
				}
			}
		});
		tvCheckFive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!checkContent.get(position).isChcekStateFive) {
					/**
					 * 未选择
					 */
					tvCheckFive
							.setBackgroundResource(R.drawable.bg_green_corner_2);
					tvCheckFive.setTextColor(basecontext.getResources()
							.getColor(R.color.white));

					checkContent.get(position).setChcekStateFive(true);
					checkContent.get(position).setCheckFive(
							tvCheckFive.getText().toString());
					LogUtil.d(TAG, "存储标签5" + tvCheckFive.getText().toString());
				} else {
					/**
					 * 已经选择
					 */
					tvCheckFive
							.setBackgroundResource(R.drawable.edittext_bg_solid_white);
					tvCheckFive.setTextColor(basecontext.getResources()
							.getColor(R.color.color26cbb2));

					checkContent.get(position).setChcekStateFive(false);
					checkContent.get(position).setCheckFive("");
					LogUtil.d(TAG, "存储标签5"
							+ checkContent.get(position).getCheckFive());
				}
			}
		});
		/**
		 * 确认选择完成衣服备注信息
		 */
		btCheckOver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				rlIsHide.setVisibility(View.GONE);
				isHide.add(position, false);
				ivArrow.setImageResource(R.drawable.home_orderlist_arrow);
				LayoutParams lp = new LayoutParams(ViewUtil.dip2px(basecontext,
						7), ViewUtil.dip2px(basecontext, 14));
				lp.leftMargin = ViewUtil.dip2px(basecontext, 30);
				lp.addRule(RelativeLayout.CENTER_VERTICAL);
				ivArrow.setLayoutParams(lp);
				StringBuilder sb = new StringBuilder();
				if (!StringUtil.isEmpty(checkContent.get(position)
						.getCheckOne())) {

					if (!StringUtil.isEmpty(checkContent.get(position)
							.getCheckTwo())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckThree())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckFour())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckFive())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckSix())) {
						sb.append(checkContent.get(position).getCheckOne()
								+ ", ");
					} else {
						sb.append(checkContent.get(position).getCheckOne() + "");
					}
				}
				if (!StringUtil.isEmpty(checkContent.get(position)
						.getCheckTwo())) {
					if (!StringUtil.isEmpty(checkContent.get(position)
							.getCheckThree())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckFour())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckFive())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckSix())) {
						sb.append(checkContent.get(position).getCheckTwo()
								+ ", ");
					} else {
						sb.append(checkContent.get(position).getCheckTwo() + "");
					}

				}
				if (!StringUtil.isEmpty(checkContent.get(position)
						.getCheckThree())) {
					if (!StringUtil.isEmpty(checkContent.get(position)
							.getCheckFour())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckFive())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckSix())) {
						sb.append(checkContent.get(position).getCheckThree()
								+ ", ");
					} else {
						sb.append(checkContent.get(position).getCheckThree()
								+ "");
					}

				}
				if (!StringUtil.isEmpty(checkContent.get(position)
						.getCheckFour())) {
					if (!StringUtil.isEmpty(checkContent.get(position)
							.getCheckFive())
							|| !StringUtil.isEmpty(checkContent.get(position)
									.getCheckSix())) {
						sb.append(checkContent.get(position).getCheckFour()
								+ ", ");
					} else {
						sb.append(checkContent.get(position).getCheckFour()
								+ "");
					}

				}
				if (!StringUtil.isEmpty(checkContent.get(position)
						.getCheckFive())) {
					if (!StringUtil.isEmpty(etRemarkOther.getText().toString())) {
						sb.append(checkContent.get(position).getCheckFive()
								+ ", ");
					} else {
						sb.append(checkContent.get(position).getCheckFive()
								+ "");
					}

				}
				if (!StringUtil.isEmpty(etRemarkOther.getText().toString())) {
					sb.append("(" + etRemarkOther.getText().toString() + ")");
				}
				String content = checkContent.get(position).getCheckOne() + " "
						+ checkContent.get(position).getCheckTwo() + " "
						+ checkContent.get(position).getCheckThree() + " "
						+ checkContent.get(position).getCheckFour() + " "
						+ checkContent.get(position).getCheckFive();
				// MsToast.makeText(basecontext, content).show();
				remarkInfor.set(position, sb.toString());
				if (StringUtil.isEmpty(sb.toString())) {
					tvRemark.setText("");
					tvRemark.setVisibility(View.GONE);
				} else {
					tvRemark.setText(sb.toString());
					tvRemark.setVisibility(View.VISIBLE);
				}

			}
		});

		return convertView;
	}

	/**
	 * 
	 * 
	 * @describe:删除监听接口
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-5下午4:22:50
	 * 
	 */
	public interface RemoveCallback {
		public void remove(int position);
	}

	/**
	 * 
	 * 
	 * @describe:设置删除监听
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-5下午4:23:08
	 * 
	 */
	public void setListener(RemoveCallback callback) {
		mcallback = callback;
	}

	/**
	 * 扫描回调
	 */
	@Override
	public void setScanResult(String result) {
		// TODO Auto-generated method stub
		currentBt.setVisibility(View.VISIBLE);
		mbtScan.setVisibility(View.GONE);
		orderNumTv.setText(result);
		isHideScan.set(currentPosition, true);
		oneCodeList.set(currentPosition, result);
		LogUtil.d(TAG, "删除前==扫描状态" + currentPosition + isHideScan.toString());
		LogUtil.d(TAG, "删除前==订单号状态" + currentPosition + oneCodeList.toString());
	}

	/**
	 * 
	 * 
	 * @describe:删除商品
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-28下午7:48:48
	 * 
	 */
	private void deleteProduct(int position, View convertView) {
		itemView = convertView;
		deletePosition = position;
		ViewUtil.showRoundProcessDialog(basecontext);
		RequestParams paramsIn = new RequestParams();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("loginToken", MsApplication.getLoginToken());
		map.put("orderCartId", baseListData.get(position).getOrdeCartId());
		paramsIn = MsApplication.getRequestParams(map, paramsIn,
				MsConfiguration.HOME_PAGE_INTERFACE_NAME);
		BasicHttpClient.getInstance().post(basecontext, paramsIn,
				MsRequestConfiguration.DELETE_SHOPCAR_PRODUCT,
				new BaseJsonHttpResponseHandler<ModifyState>() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, String arg3, ModifyState arg4) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onFailure==" + arg2.toString());
						ViewUtil.dismissRoundProcessDialog();
						Message message = Message.obtain();
						message.what = MsConfiguration.FAIL;
						handlerDeleteProduct.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2,
							ModifyState arg3) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "onSuccess==" + arg2);
						ViewUtil.dismissRoundProcessDialog();
					}

					@Override
					protected ModifyState parseResponse(String arg0,
							boolean arg1) throws Throwable {
						// TODO Auto-generated method stub
						ViewUtil.dismissRoundProcessDialog();
						LogUtil.d(TAG, "parseResponse==" + arg0);
						if (MsApplication.isEqualKey(arg0)) {
							LogUtil.d(TAG, "认证通过");
							Gson gson = new Gson();
							deleteProductBean = gson.fromJson(
									MsApplication.getBeanResult(arg0),
									ModifyState.class);
							Message message = Message.obtain();
							message.what = MsConfiguration.SUCCESS;
							handlerDeleteProduct.sendMessage(message);
						} else {
							Message message = Message.obtain();
							message.what = MsConfiguration.FAIL;
							handlerDeleteProduct.sendMessage(message);
							LogUtil.d(TAG, "认证不通过");
						}
						return deleteProductBean;
					}
				});
	}

	/**
	 * 删除商品消息返回
	 */
	Handler handlerDeleteProduct = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case MsConfiguration.SUCCESS:

				if (deleteProductBean != null
						&& deleteProductBean.getCode() == 0) {
					// MsToast.makeText(basecontext, "删除成功").show();
					/**
					 * 添加删除动画
					 */
					deleteItem(itemView, deletePosition);
					// isHideScan.remove(deletePosition);
					// oneCodeList.remove(deletePosition);
					// isHide.remove(deletePosition);
					// remarkInfor.remove(deletePosition);
					// checkContent.remove(deletePosition);
					// isFirstOrderList.remove(deletePosition);
					// LogUtil.d(TAG, "删除后==扫描状态" + isHideScan.toString());
					// LogUtil.d(TAG, "删除后==订单号状态" + oneCodeList.toString());
					// mcallback.remove(deletePosition);
				} else {
					if (deleteProductBean != null) {
						MsToast.makeText(basecontext,
								deleteProductBean.getMsg()).show();
					} else {
						MsToast.makeText(basecontext, "删除失败").show();
					}

				}

				break;
			case MsConfiguration.FAIL:
				MsToast.makeText(basecontext, "删除失败").show();
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @describe:删除ITEM
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-5-12下午4:37:45
	 * 
	 */
	private void deleteItem(final View v, final int index) {
		AnimationListener al = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				// mAnimList.remove(index);
				// ViewHolder vh = (ViewHolder) v.getTag();
				// vh.needInflate = true;
				isHideScan.remove(deletePosition);
				oneCodeList.remove(deletePosition);
				isHide.remove(deletePosition);
				remarkInfor.remove(deletePosition);
				checkContent.remove(deletePosition);
				isFirstOrderList.remove(deletePosition);
				LogUtil.d(TAG, "删除后==扫描状态" + isHideScan.toString());
				LogUtil.d(TAG, "删除后==订单号状态" + oneCodeList.toString());
				mcallback.remove(deletePosition);
				// notifyDataSetChanged();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		};

		sestyAimation(v, al);
	}

	/**
	 * 
	 * 
	 * @describe:设置删除动画
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-5-12下午4:37:20
	 * 
	 */
	private void sestyAimation(final View v, AnimationListener al) {
		final int initialHeight = v.getMeasuredHeight();

		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		if (al != null) {
			anim.setAnimationListener(al);
		}
		anim.setDuration(ANIMATION_DURATION);
		v.startAnimation(anim);
	}
}
