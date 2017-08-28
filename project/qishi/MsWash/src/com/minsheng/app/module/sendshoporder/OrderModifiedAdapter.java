package com.minsheng.app.module.sendshoporder;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.minsheng.app.application.MsApplication;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.entity.OrderDetail.Infor.Detail.OrderList;
import com.minsheng.app.module.appointmentorder.ConfirmOrderCheckObject;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.StringUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:修改订单信息页面数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-4下午5:46:19
 * 
 */
public class OrderModifiedAdapter extends BaseListAdapter<OrderList> {
	private static final String TAG = "OrderModifiedAdapter";
	// 标记一维码内容
	public ArrayList<String> oneCodeList = new ArrayList<String>();
	// 标记隐藏区域是否显示
	public ArrayList<Boolean> isHide = new ArrayList<Boolean>();
	// 存储衣服备注数据
	public ArrayList<String> remarkInfor = new ArrayList<String>();
	// 存储衣服备注选择内容
	public ArrayList<ConfirmOrderCheckObject> checkContent = MsApplication.checkContent;

	public OrderModifiedAdapter(List<OrderList> data, Object obj,
			Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub

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
				String tagString = data.get(i).getProductTag();
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

	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.order_modified_list_item, parent, false);
		}
		TextView tvNmae = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_type);
		TextView tvOneCode = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.complete_order_confirm_item_num);
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

}
