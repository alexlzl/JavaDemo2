package cn.bluerhino.driver.model.datasource;

import java.util.List;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.model.OrderInfo;
import cn.bluerhino.driver.utils.DateUtils;
import cn.bluerhino.driver.utils.ReDrawGridView;
import cn.bluerhino.driver.utils.TimeUtil;
import cn.bluerhino.driver.utils.ViewHolder;

public abstract class ShowTimeOrderInfoListAdapter extends OrderListAdapter {
	protected String mStrToday;
	protected String mStrTomorrow;
	protected String mStrAfterTomorrow;
	
	public ShowTimeOrderInfoListAdapter(Context context, List<OrderInfo> list){
		super(context, list);
		initResString();
	}
	
	private void initResString() {
		mStrToday = mRes.getString(R.string.wait_adapter_today);
		mStrTomorrow = mRes.getString(R.string.wait_adapter_tomorrow);
		mStrAfterTomorrow = mRes.getString(R.string.wait_adapter_after_tomorrow);
	}

	/**
	 * 时间的展示样式
	 */
	@Override
	protected CharSequence getServerTime(long mills) {
		SpannableString spannableString = null;
		String result = mDateUtils.getDates(mills / 1000L);
		if(result.equals("今天")){
			spannableString = new SpannableString(String.format(mStrToday, TimeUtil.formatHourMin(mills)));
			setSpanColor(spannableString);
			return spannableString;
		}else if(result.equals("明天")){
			spannableString = new SpannableString(String.format(mStrTomorrow, TimeUtil.formatHourMin(mills)));
			setSpanColor(spannableString);
			return spannableString;
		}else if(result.equals("后天")){
			spannableString = new SpannableString(String.format(mStrAfterTomorrow, TimeUtil.formatHourMin(mills)));
			setSpanColor(spannableString);
			return spannableString;
		}else{
			return super.getServerTime(mills);
		}
	}	
	
	private ForegroundColorSpan mColorSpan = new ForegroundColorSpan(mRes.getColor(R.color.warn_text_color));
	
	private void setSpanColor(SpannableString span){
		span.setSpan(mColorSpan, 1, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}	
	
	/**
	 * 抢单页 和 当前订单 修改“需要搬运”为红色字体，且如存在该选项始终置于第一位置
	 */
	@Override
	protected void setRemarkTag(OrderInfo orderInfo, GridView remark_gridview,
			View convertView) {
		if (mTagList != null) {
			mTagList.clear();
		}
		if (orderInfo.getCarringType() > 0) {
			mTagList.add(mTagStrArr[0]);
		}
		if (orderInfo.getCollectCharges() > 0) {
			mTagList.add(mTagStrArr[1]);
		}
		if (orderInfo.getReceiptType() > 0) {
			mTagList.add(mTagStrArr[2]);
		}
		if (orderInfo.getIsFollowCar() > 0) {
			mTagList.add(mTagStrArr[3]);
		}
		if (orderInfo.getTrolleyNum() > 0) {
			mTagList.add(mTagStrArr[4]);
		}
		if (mTagList.size() == 0) {
			ViewHolder.get(convertView, R.id.seperate_line).setVisibility(
					View.GONE);
			remark_gridview.setVisibility(View.GONE);
		} else {
			ViewHolder.get(convertView, R.id.seperate_line).setVisibility(
					View.VISIBLE);
			remark_gridview.setVisibility(View.VISIBLE);
			remark_gridview.setAdapter(new OrderTagAdapter(mContext, mTagList) {
				@Override
				public void setSpecificItemColor(TextView remarkItem) {
					if (remarkItem.getText().equals(mTagStrArr[0])) {
						remarkItem.setTextColor(mRes
								.getColor(R.color.warn_text_color));
					}
				}
			});
			ReDrawGridView.setGridViewHeightBasedOnChildren(remark_gridview);
		}
	}
	
	@Override
	protected boolean isOrderItemSpecShow() {
		return true;
	}
	
	private static final DateUtils mDateUtils = new DateUtils();
	

}
