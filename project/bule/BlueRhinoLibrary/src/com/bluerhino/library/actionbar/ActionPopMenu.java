package com.bluerhino.library.actionbar;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.bluerhino.R;

public class ActionPopMenu implements OnItemClickListener {

	// private static final String TAG = ActionMenuItem.class.getSimpleName();

	private OnItemClickListener mListener;

	public interface OnItemClickListener {
		public void onItemClick(int position);
	}

	// private Context context;
	private ListPopupWindow mListPopupWindow;
	private LayoutInflater inflater;

	public ActionPopMenu(Context context, View parentView, ArrayList<ActionMenuItem> menuList) {
		// this.context = context;
		inflater = LayoutInflater.from(context);

		mListPopupWindow = new ListPopupWindow(context);
		mListPopupWindow.setAdapter(new PopAdapter(menuList));
		mListPopupWindow.setAnchorView(parentView);
		mListPopupWindow.setHorizontalOffset(40);
		mListPopupWindow.setVerticalOffset(1);
		mListPopupWindow.setListSelector(context.getResources().getDrawable(R.drawable.actionbar_popmenu_item_selector));
		mListPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.white));
		mListPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		mListPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		mListPopupWindow.setModal(true);
		mListPopupWindow.setOnItemClickListener(this);
	}

	public void setWidth(int width) {
		mListPopupWindow.setWidth(width);
	}

	public void setHeight(int height) {
		mListPopupWindow.setHeight(height);
	}

	public void show() {
		mListPopupWindow.show();
	}

	public void dismiss() {
		mListPopupWindow.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mListener != null) {
			mListener.onItemClick(position);
		}
		mListPopupWindow.dismiss();
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mListener = listener;
	}

	private final class PopAdapter extends BaseAdapter {
		private ArrayList<ActionMenuItem> mActionMenuList;

		public PopAdapter(ArrayList<ActionMenuItem> actionMenuList) {
			super();
			mActionMenuList = actionMenuList;
		}

		@Override
		public int getCount() {
			return mActionMenuList.size();
		}

		@Override
		public Object getItem(int position) {
			return mActionMenuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.action_pomenu_item, null);
				holder = new ViewHolder();
				holder.titleView = (TextView) convertView;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ActionMenuItem menuItme = (ActionMenuItem) getItem(position);
			if (ActionMenuItem.RES_ID_NONE != menuItme.iconResId) {
				holder.titleView.setCompoundDrawablesWithIntrinsicBounds(menuItme.iconResId, 0, 0,
				        0);
			}

			if (ActionMenuItem.RES_ID_NONE != menuItme.textResId) {
				holder.titleView.setText(menuItme.textResId);
			}

			return convertView;
		}

		private final class ViewHolder {
			TextView titleView;
		}
	}

	public static class ActionMenuItem {
		public static final int RES_ID_NONE = -1;
		public int iconResId = RES_ID_NONE;
		public int textResId = RES_ID_NONE;

		public ActionMenuItem(int iconResId, int textResId) {
			this.iconResId = iconResId;
			this.textResId = textResId;
		}
	}
}
