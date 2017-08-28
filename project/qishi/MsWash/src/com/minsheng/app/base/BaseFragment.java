package com.minsheng.app.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.minsheng.app.application.MsApplication;
import com.minsheng.wash.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * 
 * @describe:基础Fragment
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-3下午2:44:11
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
	/**
	 * 异步加载图片相关
	 */
	protected ImageLoader imageLoader = MsApplication.imageLoader;
	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;
	protected OnScrollListener customListener = null;
	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";
	protected String uMActivityNameString = "";
	protected ListView listView = null;
	protected DialogFragment dialogFragment = MyDialogFragment.newInstance();
	protected LayoutInflater baseInflate;
	protected Activity baseActivity;
	protected EditText searchBoxEt = null;
	protected ImageView mLoading = null;
	protected AnimationDrawable animLoading = null;

	/**
	 * 
	 * 
	 * @describe:初始化页面视图
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午2:47:12
	 * 
	 */
	protected abstract View initView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	/**
	 * 
	 * 
	 * @describe:加载数据
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午2:47:34
	 * 
	 */
	protected abstract void getNetData();

	/**
	 * 
	 * 
	 * @describe:设置控件监听
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午2:47:53
	 * 
	 */
	protected abstract void setViewListener();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		baseInflate = getActivity().getLayoutInflater();
		container = (ViewGroup) inflater.inflate(
				R.layout.base_layout_without_titlebar, container, false);
		return initView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setViewListener();
		getNetData();

	}

	/**
	 * 
	 * 
	 * @describe:显示加载进度框
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午2:57:11
	 * 
	 */
	public void showProgressBar() {
		if (dialogFragment == null) {
			dialogFragment = MyDialogFragment.newInstance();
		}
		dialogFragment.setStyle(android.R.style.Theme_Translucent_NoTitleBar,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialogFragment.show(getActivity().getSupportFragmentManager(),
				"progressbar");
	}

	/**
	 * 
	 * 
	 * @describe:隐藏加载进度框
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-3下午2:57:52
	 * 
	 */
	public void dismissProgressBar() {
		if (dialogFragment != null && !dialogFragment.isHidden()) {
			try {
				dialogFragment.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @describe:进度框Fragment
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-9-2下午5:45:31
	 * 
	 */
	public static class MyDialogFragment extends DialogFragment {
		static MyDialogFragment newInstance() {
			return new MyDialogFragment();
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.progress_bar_layout, container,
					false);
			return v;
		}
	}

}
