package com.minsheng.wash;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.util.ViewUtil;
import com.minsheng.app.view.MsToast;

/**
 * 
 * @describe:
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-2-10下午2:59:09
 * 
 */
public class ListAdapter extends BaseListAdapter<String> {
	RemoveCallback mcallback;

	public ListAdapter(List<String> data, Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(R.layout.list_item, parent,
					false);
		}

		TextView tv = (TextView) ViewHolderUtil.getItemView(convertView,
				R.id.text);
		tv.setText(baseListData.get(position) + "");
		Button bt = (Button) ViewHolderUtil.getItemView(convertView,
				R.id.remove);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MsToast.makeText(basecontext, "TEST" + position).show();
				mcallback.remove(position);
				// dataList.remove(position);
				// ListAdapter.this.notifyDataSetChanged();
			}
		});
		return convertView;
	}

	public interface RemoveCallback {
		public void remove(int position);
	}

	public void setListener(RemoveCallback callback) {
		mcallback = callback;
	}
}
