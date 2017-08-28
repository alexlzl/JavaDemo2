package cn.bluerhino.driver.model.datasource;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.utils.ViewHolder;

public class OrderTagAdapter extends BaseAdapter {
	
	private List<String> mList;
	private LayoutInflater mLayoutInflater;
	
	
	public OrderTagAdapter(Context context, List<String> list){
		mList = list;
		mLayoutInflater =  LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mLayoutInflater.inflate(R.layout.orderdeal_remark_item, null);
		}
		TextView remarkItem = ViewHolder.get(convertView, R.id.remark_textview);
		setSpecificItemColor(remarkItem);
		remarkItem.setText(mList.get(position).toString());
		return convertView;
	}
	
	public void setSpecificItemColor(TextView remarkItem){
		
	}

}