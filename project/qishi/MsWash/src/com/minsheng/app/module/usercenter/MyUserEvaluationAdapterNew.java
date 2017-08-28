package com.minsheng.app.module.usercenter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.entity.CourierEvaluate.Infor.EvaluateDetail;
import com.minsheng.app.util.StringUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:我的评价页面数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-23下午3:29:42
 * 
 */
public class MyUserEvaluationAdapterNew extends BaseListAdapter<EvaluateDetail> {

	public MyUserEvaluationAdapterNew(List<EvaluateDetail> data, Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.my_userevaluation_listitem, parent, false);
		}
		TextView tvOrderNum = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.my_userevaluation_listitem_order_num);
		TextView tvOrderRemark = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.my_userevaluation_listitem_content);
		ImageView qualityOne = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.quality_one);
		ImageView qualityTwo = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.quality_two);
		ImageView qualityThree = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.quality_three);
		ImageView qualityFour = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.quality_four);
		ImageView qualityFive = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.quality_five);
		ImageView serviceOne = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.service_one);
		ImageView serviceTwo = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.service_two);
		ImageView serviceThree = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.service_three);
		ImageView serviceFour = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.service_four);
		ImageView serviceFive = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.service_five);
		ImageView washTimeOne = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.wash_time_one);
		ImageView washTimeTwo = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.wash_time_two);
		ImageView washTimeThree = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.wash_time_three);
		ImageView washTimeFour = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.wash_time_four);
		ImageView washTimeFive = (ImageView) ViewHolderUtil.getItemView(
				convertView, R.id.wash_time_five);
		tvOrderNum.setText(baseListData.get(position).getOrderSn());
		if (!StringUtil
				.isEmpty(baseListData.get(position).getEvaluateContent())) {
			tvOrderRemark.setText(baseListData.get(position)
					.getEvaluateContent());
			tvOrderRemark.setVisibility(View.VISIBLE);
		} else {
			tvOrderRemark.setVisibility(View.GONE);
		}

		int washQuality = baseListData.get(position).getWashQuality();
		int serviceQuality = baseListData.get(position).getServiceQuality();
		int timeQuality = baseListData.get(position).getTimeQuality();
		switch (washQuality) {
		/**
		 * 洗衣质量
		 */
		case 1:
			qualityOne.setImageResource(R.drawable.star_full);
			qualityTwo.setImageResource(R.drawable.star_empty);
			qualityThree.setImageResource(R.drawable.star_empty);
			qualityFour.setImageResource(R.drawable.star_empty);
			qualityFive.setImageResource(R.drawable.star_empty);
			break;
		case 2:
			qualityOne.setImageResource(R.drawable.star_full);
			qualityTwo.setImageResource(R.drawable.star_full);
			qualityThree.setImageResource(R.drawable.star_empty);
			qualityFour.setImageResource(R.drawable.star_empty);
			qualityFive.setImageResource(R.drawable.star_empty);
			break;
		case 3:
			qualityOne.setImageResource(R.drawable.star_full);
			qualityTwo.setImageResource(R.drawable.star_full);
			qualityThree.setImageResource(R.drawable.star_full);
			qualityFour.setImageResource(R.drawable.star_empty);
			qualityFive.setImageResource(R.drawable.star_empty);
			break;
		case 4:
			qualityOne.setImageResource(R.drawable.star_full);
			qualityTwo.setImageResource(R.drawable.star_full);
			qualityThree.setImageResource(R.drawable.star_full);
			qualityFour.setImageResource(R.drawable.star_full);
			qualityFive.setImageResource(R.drawable.star_empty);
			break;
		case 5:
			qualityOne.setImageResource(R.drawable.star_full);
			qualityTwo.setImageResource(R.drawable.star_full);
			qualityThree.setImageResource(R.drawable.star_full);
			qualityFour.setImageResource(R.drawable.star_full);
			qualityFive.setImageResource(R.drawable.star_full);
			break;

		}
		switch (serviceQuality) {
		/**
		 * 服务态度
		 */
		case 1:
			serviceOne.setImageResource(R.drawable.star_full);
			serviceTwo.setImageResource(R.drawable.star_empty);
			serviceThree.setImageResource(R.drawable.star_empty);
			serviceFour.setImageResource(R.drawable.star_empty);
			serviceFive.setImageResource(R.drawable.star_empty);
			break;
		case 2:
			serviceOne.setImageResource(R.drawable.star_full);
			serviceTwo.setImageResource(R.drawable.star_full);
			serviceThree.setImageResource(R.drawable.star_empty);
			serviceFour.setImageResource(R.drawable.star_empty);
			serviceFive.setImageResource(R.drawable.star_empty);
			break;
		case 3:
			serviceOne.setImageResource(R.drawable.star_full);
			serviceTwo.setImageResource(R.drawable.star_full);
			serviceThree.setImageResource(R.drawable.star_full);
			serviceFour.setImageResource(R.drawable.star_empty);
			serviceFive.setImageResource(R.drawable.star_empty);
			break;
		case 4:
			serviceOne.setImageResource(R.drawable.star_full);
			serviceTwo.setImageResource(R.drawable.star_full);
			serviceThree.setImageResource(R.drawable.star_full);
			serviceFour.setImageResource(R.drawable.star_full);
			serviceFive.setImageResource(R.drawable.star_empty);
			break;
		case 5:
			serviceOne.setImageResource(R.drawable.star_full);
			serviceTwo.setImageResource(R.drawable.star_full);
			serviceThree.setImageResource(R.drawable.star_full);
			serviceFour.setImageResource(R.drawable.star_full);
			serviceFive.setImageResource(R.drawable.star_full);
			break;

		}
		switch (timeQuality) {
		/**
		 * 上门时间
		 */
		case 1:
			washTimeOne.setImageResource(R.drawable.star_full);
			washTimeTwo.setImageResource(R.drawable.star_empty);
			washTimeThree.setImageResource(R.drawable.star_empty);
			washTimeFour.setImageResource(R.drawable.star_empty);
			washTimeFive.setImageResource(R.drawable.star_empty);
			break;
		case 2:
			washTimeOne.setImageResource(R.drawable.star_full);
			washTimeTwo.setImageResource(R.drawable.star_full);
			washTimeThree.setImageResource(R.drawable.star_empty);
			washTimeFour.setImageResource(R.drawable.star_empty);
			washTimeFive.setImageResource(R.drawable.star_empty);
			break;
		case 3:
			washTimeOne.setImageResource(R.drawable.star_full);
			washTimeTwo.setImageResource(R.drawable.star_full);
			washTimeThree.setImageResource(R.drawable.star_full);
			washTimeFour.setImageResource(R.drawable.star_empty);
			washTimeFive.setImageResource(R.drawable.star_empty);
			break;
		case 4:
			washTimeOne.setImageResource(R.drawable.star_full);
			washTimeTwo.setImageResource(R.drawable.star_full);
			washTimeThree.setImageResource(R.drawable.star_full);
			washTimeFour.setImageResource(R.drawable.star_full);
			washTimeFive.setImageResource(R.drawable.star_empty);
			break;
		case 5:
			washTimeOne.setImageResource(R.drawable.star_full);
			washTimeTwo.setImageResource(R.drawable.star_full);
			washTimeThree.setImageResource(R.drawable.star_full);
			washTimeFour.setImageResource(R.drawable.star_full);
			washTimeFive.setImageResource(R.drawable.star_full);
			break;

		}
		return convertView;
	}

}
