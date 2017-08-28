package com.minsheng.app.baseadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * 
 * @describe:基础数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-3上午10:41:28
 * 
 */
public abstract class BaseListAdapter<E> extends BaseAdapter {
	protected List<E> baseListData;
	protected Context basecontext;
	protected LayoutInflater baseInflater;

	public BaseListAdapter(List<E> data, Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.basecontext = context;
		this.baseListData = data;
		baseInflater = LayoutInflater.from(basecontext);
	}

	public BaseListAdapter(List<E> data, Object obj, Context context) {
		super();
		this.basecontext = context;
		this.baseListData = data;
		baseInflater = LayoutInflater.from(basecontext);
	}

	/**
	 * 
	 * @describe:用于数据刷新
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014年8月1日上午11:00:56
	 * 
	 */
	public void setNewData(List<E> newData) {
		this.baseListData = newData;
		notifyDataSetChanged();

	}

	/**
	 * 
	 * @describe:用于数据源中加入集合
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014年8月1日上午11:05:37
	 * 
	 */
	public void addListData(List<E> addList) {
		this.baseListData.addAll(addList);
		notifyDataSetChanged();
	}

	/**
	 * 
	 * @describe:插入单个数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014年8月1日上午11:09:45
	 * 
	 */
	public void addItemData(E itemData) {
		this.baseListData.add(itemData);
		notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		return baseListData == null ? 0 : baseListData.size();
	}

	@Override
	public Object getItem(int position) {
		return baseListData == null ? null : baseListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = bindView(position, convertView, parent);

		return convertView;
	}

	/**
	 * 
	 * @describe:用于子类实现各自数据的绑定
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014年8月1日上午10:48:33
	 * 
	 */
	public abstract View bindView(int position, View convertView,
			ViewGroup parent);
}
