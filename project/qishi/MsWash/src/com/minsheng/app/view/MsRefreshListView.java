package com.minsheng.app.view;

import com.minsheng.app.util.LogUtil;
import com.minsheng.wash.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ListView下拉刷新
 * 
 * 
 */
public class MsRefreshListView extends ListView implements OnScrollListener {
	private String TAG = "自定义刷新控件";
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;
	private LayoutInflater inflater;
	private LinearLayout headView;
	// private TextView tipsTextview;
	// private TextView lastUpdatedTextView;
	// private ImageView arrowImageView;
	// private RotateAnimation animation;
	// private RotateAnimation reverseAnimation;
	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;
	private int headContentWidth;
	private int headContentHeight;
	private int startY;
	private int firstItemIndex;
	private int state;
	private boolean isBack;
	private OnRefreshListener refreshListener;
	private OnMoreListener loadMoreListener;
	private boolean isRefreshable;
	private ProgressBar moreProgressBar;
	private TextView loadMoreView;
	private View moreView;
	private GestureDetector gestureDetector;
	private boolean isRefresh = true;
	private ToTopCallBack listener;
	// 拉到底部的开始默认图片
	private ImageView ivDefault;
	// 拉到底部的动画控件
	private ImageView ivAnimaton;
	// 下来刷新提示语
	private TextView tvNote;
	// 加载进度框
	private ProgressBar loadingPb;

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}

	public MsRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public MsRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 
	 * 
	 * @describe:初始化
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午2:34:34
	 * 
	 */
	private void init(Context context) {
		// 加载头部视图==================================
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(R.layout.list_header, null);
		ivDefault = (ImageView) headView.findViewById(R.id.list_header_default);
		loadingPb = (ProgressBar) headView.findViewById(R.id.list_header_pb);
		ivAnimaton=(ImageView) headView.findViewById(R.id.list_header_animation);
		// arrowImageView.setMinimumWidth(70);
		// arrowImageView.setMinimumHeight(50);
		tvNote = (TextView) headView.findViewById(R.id.list_header_note_tv);
		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();
		// 设置间距隐藏头部视图
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();
		addHeaderView(headView, null, false);
		setOnScrollListener(this);
		// animation = new RotateAnimation(0, -180,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// animation.setInterpolator(new LinearInterpolator());
		// animation.setDuration(250);
		// animation.setFillAfter(true);

		// reverseAnimation = new RotateAnimation(-180, 0,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// reverseAnimation.setInterpolator(new LinearInterpolator());
		// reverseAnimation.setDuration(200);
		// reverseAnimation.setFillAfter(true);
		state = DONE;
		isRefreshable = false;
		// 加载底部更多视图========================
		moreView = LayoutInflater.from(context).inflate(R.layout.list_foot,
				null);
		moreProgressBar = (ProgressBar) moreView
				.findViewById(R.id.list_foot_pb);
		loadMoreView = (TextView) moreView.findViewById(R.id.load_more);
		addFooterView(moreView);
	}

	@Override
	public void onScroll(AbsListView arg0, int firstVisiableItem,
			int visibleItemCount, int totalItemCount) {
		if (visibleItemCount == totalItemCount) {
			/**
			 * 数据未到一页
			 */
			isRefresh = false;
			if (listener != null) {
				listener.hide();
			}

		} else {
			if (visibleItemCount < totalItemCount) {
				/**
				 * 数据超过一页
				 */
				isRefresh = true;
				if (listener != null) {
					listener.show();
				}

			}
		}
		firstItemIndex = firstVisiableItem;
		if (visibleItemCount < totalItemCount
				&& (firstItemIndex + visibleItemCount) == totalItemCount
				&& loadMoreView != null) {
			if (getResources().getString(R.string.app_list_footer_more).equals(
					loadMoreView.getText())) {
				onLoad();
			}
		}
	}

	/**
	 * 滑动监听
	 */
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		switch (scrollState) {

		// 当不滚动时
		case OnScrollListener.SCROLL_STATE_IDLE:
			// 判断滚动到底部
			if (this.getLastVisiblePosition() == (this.getCount() - 1)) {
				if (loadMoreListener != null) {
					if (isRefresh) {
						LogUtil.d(TAG, "SCROLL_STATE_IDLE");
						loadMoreListener.onLoadMore();
						moreView.setVisibility(View.VISIBLE);
						moreProgressBar.setVisibility(View.VISIBLE);
						loadMoreView.setText("努力加载中");

					}
				}
			}
			// 判断滚动到顶部

			if (this.getFirstVisiblePosition() == 0) {
			}

			break;
		}
	}

	/**
	 * 滑动事件监听
	 */
	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				LogUtil.d(TAG, "ACTION_DOWN");
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:
				LogUtil.d(TAG, "ACTION_UP");
				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {

					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				LogUtil.d(TAG, "ACTION_MOVE");
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}

				if (state != REFRESHING && isRecored && state != LOADING) {

					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {

						setSelection(0);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 
	 * 
	 * @describe:当状态改变时候，调用该方法，以更新界面
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午3:07:20
	 * 
	 */
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			/**
			 * 松开刷新状态，显示动画
			 */
			tvNote.setText("松开刷新");
			ivDefault.setVisibility(View.GONE);
			ivAnimaton.setVisibility(View.VISIBLE);
			loadingPb.setVisibility(View.GONE);
			break;
		case PULL_To_REFRESH:
			/**
			 * 下拉刷新状态，显示默认图片
			 */
			ivDefault.setVisibility(View.VISIBLE);
			loadingPb.setVisibility(View.GONE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				ivDefault.setVisibility(View.GONE);
				isBack = false;
				tvNote.setText("下拉刷新");
			} else {
				tvNote.setText("下拉刷新");
			}
			break;

		case REFRESHING:
			/**
			 * 正在刷新状态，显示圆形进度框
			 */
			headView.setPadding(0, 0, 0, 0);
			loadingPb.setVisibility(View.VISIBLE);
			tvNote.setText("正在刷新...");
			break;
		case DONE:
			/**
			 * 刷新完成状态，隐藏头部视图
			 */
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			ivAnimaton.setVisibility(View.GONE);
			ivDefault.setVisibility(View.VISIBLE);
			tvNote.setText("下拉刷新");
			loadingPb.setVisibility(View.GONE);
			break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public void setonLoadListener(OnMoreListener loadListener) {
		this.loadMoreListener = loadListener;
	}

	/**
	 * 
	 * 
	 * @describe:刷新监听
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午3:34:52
	 * 
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * 
	 * 
	 * @describe:加载更多监听
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午3:35:10
	 * 
	 */
	public interface OnMoreListener {
		public void onLoadMore();
	}

	/**
	 * 
	 * 
	 * @describe:刷新完成
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午3:34:30
	 * 
	 */
	public void onRefreshComplete() {
		state = DONE;
		changeHeaderViewByState();
	}

	/**
	 * 
	 * 
	 * @describe:加载更多
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午3:34:19
	 * 
	 */
	private void onLoad() {
		if (loadMoreListener != null) {
			moreProgressBar.setVisibility(View.VISIBLE);
			loadMoreView.setText(R.string.app_list_footer_loading);
			loadMoreListener.onLoadMore();
		}
	}

	/**
	 * 
	 * 
	 * @describe:加载更多完成
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午3:33:09
	 * 
	 */
	public void onLoadComplete() {
		moreView.setVisibility(View.GONE);
		moreProgressBar.setVisibility(View.GONE);
	}

	/**
	 * 
	 * 
	 * @describe:刷新回调
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午3:32:59
	 * 
	 */
	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	/**
	 * 
	 * 
	 * @describe:没有更多数据回调
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午3:32:38
	 * 
	 */
	public void onNoData() {
		moreProgressBar.setVisibility(View.GONE);
		loadMoreView.setText("亲，已经到底了~~");
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 
	 * 
	 * @describe:设置到达顶部回调
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-15下午3:31:10
	 * 
	 */
	public void setTopCallBack(ToTopCallBack listener) {
		this.listener = listener;
	}

	public interface ToTopCallBack {
		public void show();

		public void hide();
	}
}
