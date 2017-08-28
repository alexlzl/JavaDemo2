package com.minsheng.app.module.usercenter;

import com.minsheng.app.module.usercenter.PinnedHeaderListView.PinnedHeaderAdapter;
import com.minsheng.app.module.usercenter.MyUserEvaluation.StandardArrayAdapter;
import com.minsheng.wash.R;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MyUserEvaluationAdapter implements ListAdapter,
		OnItemClickListener, PinnedHeaderAdapter, SectionIndexer,
		OnScrollListener {
	private SectionIndexer mIndexer;
	private String[] mSections;
	private int[] mCounts;
	// section的数目
	private int mSectionCounts = 0;
	private final StandardArrayAdapter linkedAdapter;
	// private final Map<String, View> currentViewSections = new HashMap<String,
	// View>();
	private int viewTypeCount;
	protected final LayoutInflater inflater;
	private View transparentSectionView;
	private OnItemClickListener linkedListener;

	// private final DataSetObserver dataSetObserver = new DataSetObserver() {
	// @Override
	// public void onChanged() {
	// super.onChanged();
	// updateTotalCount();
	// }
	//
	// @Override
	// public void onInvalidated() {
	// super.onInvalidated();
	// updateTotalCount();
	// };
	// };

	public MyUserEvaluationAdapter(final LayoutInflater inflater,
			final StandardArrayAdapter linkedAdapter) {
		this.linkedAdapter = linkedAdapter;
		this.inflater = inflater;
		// linkedAdapter.registerDataSetObserver(dataSetObserver);
		updateTotalCount();
		mIndexer = new MySectionIndexer(mSections, mCounts);
	}

	/**
	 * 
	 * 
	 * @describe:计算一共多少个分类，相同分类的元素在数组中相邻排列，比较前后两个item的section
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-13下午4:23:35
	 * 
	 */
	private synchronized void updateTotalCount() {
		String currentSection = null;
		viewTypeCount = linkedAdapter.getViewTypeCount() + 1;
		final int count = linkedAdapter.getCount();
		for (int i = 0; i < count; i++) {
			final MyUserEvaluationBean item = (MyUserEvaluationBean) linkedAdapter
					.getItem(i);
			if (!isTheSame(currentSection, item.section)) {
				mSectionCounts++;
				currentSection = item.section;
			}
		}
		fillSections();
	}

	/**
	 * 
	 * 
	 * @describe:判断section是否相同
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-13下午4:26:31
	 * 
	 */
	private boolean isTheSame(final String previousSection,
			final String newSection) {
		if (previousSection == null) {
			return newSection == null;
		} else {
			return previousSection.equals(newSection);
		}
	}

	/**
	 * 
	 * 
	 * @describe:填充seciton
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2015-3-13下午4:28:24
	 * 
	 */
	private void fillSections() {
		mSections = new String[mSectionCounts];
		mCounts = new int[mSectionCounts];
		final int count = linkedAdapter.getCount();
		String currentSection = null;
		int sectionIndex = 0;
		int sectionCounts = 0;
		String previousSection = null;
		for (int i = 0; i < count; i++) {
			sectionCounts++;
			currentSection = linkedAdapter.items[i].section;
			if (!isTheSame(previousSection, currentSection)) {
				mSections[sectionIndex] = currentSection;
				previousSection = currentSection;
				if (sectionIndex == 0) {
					mCounts[0] = 1;
				} else if (sectionIndex != 0) {
					mCounts[sectionIndex - 1] = sectionCounts - 1;
				}
				if (i > 0) {
					sectionCounts = 1;
				}
				sectionIndex++;
			}
			if (i == count - 1) {
				mCounts[sectionIndex - 1] = sectionCounts;
			}

		}
	}

	@Override
	public synchronized int getCount() {
		return linkedAdapter.getCount();
	}

	@Override
	public synchronized Object getItem(final int position) {
		final int linkedItemPosition = getLinkedPosition(position);
		return linkedAdapter.getItem(linkedItemPosition);
	}

	public synchronized String getSectionName(final int position) {
		return null;
	}

	@Override
	public long getItemId(final int position) {
		return linkedAdapter.getItemId(getLinkedPosition(position));
	}

	protected Integer getLinkedPosition(final int position) {
		return position;
	}

	@Override
	public int getItemViewType(final int position) {
		return linkedAdapter.getItemViewType(getLinkedPosition(position));
	}

	// protected void setSectionText(final String section, final View
	// sectionView) {
	// final TextView textView = (TextView) sectionView
	// .findViewById(R.id.my_userevaluation_listitem_date);
	// textView.setText(section);
	// }

	// protected synchronized void replaceSectionViewsInMaps(final String
	// section,
	// final View theView) {
	// if (currentViewSections.containsKey(theView)) {
	// currentViewSections.remove(theView);
	// }
	// currentViewSections.put(section, theView);
	// }

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.my_userevaluation_listitem, null);
		}
		final MyUserEvaluationBean currentItem = linkedAdapter.items[position];
		if (currentItem != null) {
			// final RelativeLayout itemHead = (RelativeLayout) view
			// .findViewById(R.id.my_userevaluation_listitem_head);
			// final TextView tvDate = (TextView) view
			// .findViewById(R.id.my_userevaluation_listitem_date);
			//
			// final TextView tvOrderNum = (TextView) view
			// .findViewById(R.id.my_userevaluation_listitem_ordercontent);
			final TextView tvCommentContent = (TextView) view
					.findViewById(R.id.my_userevaluation_listitem_content);
			final View tvLine = view
					.findViewById(R.id.my_userevaluation_listitem_line);
			// if (tvOrderNum != null) {
			// tvOrderNum.setText(currentItem.orderNum);
			// }
			// if (tvCommentContent != null) {
			// tvCommentContent.setText(currentItem.comments);
			// }
			// if (tvDate != null) {
			// tvDate.setText(currentItem.section);
			// }
			//
			// int section = getSectionForPosition(position);
			// if (getPositionForSection(section) == position) {
			// view.findViewById(R.id.my_userevaluation_listitem_head)
			// .setVisibility(View.VISIBLE);
			// itemHead.setVisibility(View.VISIBLE);
			// } else {
			// view.findViewById(R.id.my_userevaluation_listitem_head)
			// .setVisibility(View.GONE);
			// itemHead.setVisibility(View.GONE);
			// }

		}
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return viewTypeCount;
	}

	@Override
	public boolean hasStableIds() {
		return linkedAdapter.hasStableIds();
	}

	@Override
	public boolean isEmpty() {
		return linkedAdapter.isEmpty();
	}

	@Override
	public void registerDataSetObserver(final DataSetObserver observer) {
		linkedAdapter.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(final DataSetObserver observer) {
		linkedAdapter.unregisterDataSetObserver(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return linkedAdapter.areAllItemsEnabled();
	}

	@Override
	public boolean isEnabled(final int position) {
		return linkedAdapter.isEnabled(getLinkedPosition(position));
	}

	public int getRealPosition(int pos) {
		return pos - 1;
	}

	public synchronized View getTransparentSectionView() {
		if (transparentSectionView == null) {
		}
		return transparentSectionView;
	}

	protected void sectionClicked(final String section) {
		// do nothing
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view,
			final int position, final long id) {
		if (linkedListener != null) {
			linkedListener.onItemClick(parent, view,
					getLinkedPosition(position), id);
		}

	}

	public void setOnItemClickListener(final OnItemClickListener linkedListener) {
		this.linkedListener = linkedListener;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;// ����ûʲô�����˼����Ҫ��ͨѶ¼�У�listview�е�һ����ʾ�������е���ϵ�ˣ�
									// ������ʵ����ݣ����Ի�-1,����͵����ֱ�ӰѺ����ȥ�����������������Ƶĵط���ԭ��һ��
		if (mIndexer == null) {
			return PINNED_HEADER_GONE;
		}
		if (realPosition < 0) {
			return PINNED_HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);// �õ���item���ڵķ���λ��
		int nextSectionPosition = getPositionForSection(section + 1);// �õ���һ�������λ��
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		int realPosition = position;
		int section = getSectionForPosition(realPosition);

		String title = (String) mIndexer.getSections()[section];
		((TextView) header.findViewById(R.id.header_text)).setText(title);

	}

	@Override
	public Object[] getSections() {
		if (mIndexer == null) {
			return new String[] { "" };
		} else {
			return mIndexer.getSections();
		}
	}

	@Override
	public int getPositionForSection(int section) {
		if (mIndexer == null) {
			return -1;
		}
		return mIndexer.getPositionForSection(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		if (mIndexer == null) {
			return -1;
		}
		return mIndexer.getSectionForPosition(position);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}

	}

}
