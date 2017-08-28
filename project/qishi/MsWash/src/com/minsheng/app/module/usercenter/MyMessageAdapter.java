package com.minsheng.app.module.usercenter;

import java.util.List;
import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.entity.MyMessageEntity.Infor.MessageObj;
import com.minsheng.app.util.LogUtil;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.app.util.TimeUtil;
import com.minsheng.wash.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @describe:消息中心数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-2下午4:04:01
 * 
 */
public class MyMessageAdapter extends BaseListAdapter<MessageObj> {
	private static String TAG = "MyMessageAdapter";
	private SparseBooleanArray messageStateList = new SparseBooleanArray();

	// private static final int VIEW_ID = 1001;

	public MyMessageAdapter(List<MessageObj> data, Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
		bindViewData(data);
	}

	@Override
	public void setNewData(List<MessageObj> newData) {
		// TODO Auto-generated method stub
		super.setNewData(newData);
		bindViewData(newData);
	}

	private void bindViewData(List<MessageObj> data) {
		if (data != null) {
			int length = data.size();
			for (int i = 0; i < length; i++) {
				switch (data.get(i).getIsRead()) {
				case 0:
					messageStateList.append(i, false);
					break;
				case 1:
					messageStateList.append(i, true);
					break;
				}

			}
		}
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.usercenter_mymessage_item, parent, false);
		}
		final TextView tvState = (TextView) ViewHolderUtil.getItemView(
				convertView, R.id.usercenter_mymessage_item_state);
		TextView tvTitle = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.usercenter_mymessage_item_title);
		TextView tvTime = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.usercenter_mymessage_item_time);

		if (messageStateList.get(position)) {
			tvState.setBackgroundResource(R.drawable.messagecenter_hasread_bg);
			tvState.setText("已读");
		} else {
			tvState.setBackgroundResource(R.drawable.messagecenter_notread_bg);
			tvState.setText("未读");
		}
		tvTime.setText(TimeUtil.changeTimeStempToString(baseListData.get(
				position).getAddTime()));
		tvTitle.setText(baseListData.get(position).getMsgTitle());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvState.setBackgroundResource(R.drawable.messagecenter_hasread_bg);
				Intent intent = new Intent(basecontext, MyMessageDetail.class);
				intent.putExtra("messagedetail", baseListData.get(position));
				intent.putExtra("messageid", baseListData.get(position)
						.getMsgId());
				intent.putExtra("isRead", messageStateList.get(position));
				LogUtil.d(TAG, "参数" + baseListData.get(position));
				MsStartActivity.startActivity((Activity) basecontext, intent);
			}
		});
		// convertView.setId(VIEW_ID);
		// convertView.setTag(R.id.item_id, position);
		return convertView;
	}

	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// switch (arg0.getId()) {
	// case R.id.usercenter_mymessage_item_state:
	// // int position = (Integer) arg0.getTag(R.id.item_id);
	// // array.append(position, true);
	// // ((TextView)
	// // arg0).setBackgroundResource(R.drawable.messagecenter_hasread_bg);
	// break;
	// case VIEW_ID:
	// int position = (Integer) arg0.getTag(R.id.item_id);
	// messageStateList.append(position, true);
	// TextView tv = (TextView) arg0
	// .findViewById(R.id.usercenter_mymessage_item_state);
	// tv.setBackgroundResource(R.drawable.messagecenter_hasread_bg);
	// Intent intent = new Intent(basecontext, MyMessageDetail.class);
	// intent.putExtra("messagedetail", baseListData.get(position));
	// intent.putExtra("messageid", baseListData.get(position).getMsgId());
	// intent.putExtra("isRead", messageStateList.get(position));
	// LogUtil.d(TAG, "参数" + baseListData.get(position));
	// MsStartActivity.startActivity((Activity) basecontext, intent);
	// break;
	// }
	//
	// }

}
