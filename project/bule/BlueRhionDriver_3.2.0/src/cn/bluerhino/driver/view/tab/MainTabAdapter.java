package cn.bluerhino.driver.view.tab;

import com.baidu.navisdk.util.common.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.fragment.ActivityFragment;
import cn.bluerhino.driver.controller.fragment.CurrentOrderFragment;
import cn.bluerhino.driver.controller.fragment.UserInfoFragment;
import cn.bluerhino.driver.controller.fragment.WaitListFragment;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.bluerhino.driver.view.framework.FragmentTabAdapter;
import cn.bluerhino.driver.view.framework.FragmentTabInfo;

public class MainTabAdapter implements FragmentTabAdapter {

	public static final String TAG = FragmentTabAdapter.class.getSimpleName();
	public static final String FRAGMENT_ID_WAITLIST = "waitlist";
	public static final String FRAGMENT_ID_CURRENT = "current";
	public static final String FRAGMENT_ID_MYINFO = "userinfo";
	public static final String FRAGMENT_ID_ACTIVITY = "activity";
	private Context mContext;
	private LayoutInflater mInflater;
	private static final FragmentTabInfo[] FRAGMENT_TAB_ITEMS = {
			new FragmentTabInfo.Builder().setTabId(FRAGMENT_ID_WAITLIST).setFragmentClass(WaitListFragment.class)
					.setTitleResID(R.string.main_tab_item_wait).setIconResID(R.drawable.waitlist_icon_sel).build(),

			new FragmentTabInfo.Builder().setTabId(FRAGMENT_ID_CURRENT).setFragmentClass(CurrentOrderFragment.class)
					.setTitleResID(R.string.main_tab_item_cur).setIconResID(R.drawable.curlist_icon_sel).build(),
			new FragmentTabInfo.Builder().setTabId(FRAGMENT_ID_ACTIVITY).setFragmentClass(ActivityFragment.class)
					.setTitleResID(R.string.main_tab_item_activity).setIconResID(R.drawable.activity_fragment_click)
					.build(),

			new FragmentTabInfo.Builder().setTabId(FRAGMENT_ID_MYINFO).setFragmentClass(UserInfoFragment.class)
					.setTitleResID(R.string.main_tab_item_my).setIconResID(R.drawable.main_tab_my_icon_sel).build(), };

	public MainTabAdapter(Context context) {
		super();
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return FRAGMENT_TAB_ITEMS.length;
	}

	@Override
	public FragmentTabInfo getItem(int position) {
		return FRAGMENT_TAB_ITEMS[position];
	}

	@Override
	public View getView(int position) {
		FragmentTabInfo info = getItem(position);
		if (info.getIconResID() == 0 && info.getTitleResID() == 0) {
			return null;
		}
		View view = mInflater.inflate(R.layout.main_tab_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.main_tab_item_image);
		imageView.setImageResource(info.getIconResID());
		TextView textView = (TextView) view.findViewById(R.id.main_tab_item_text);
		if (position == 2) {
			if (StringUtils.isEmpty(ApplicationController.getInstance().getTabTitle())) {
				textView.setText("最新活动");
			} else {
				textView.setText(ApplicationController.getInstance().getTabTitle());
			}
		} else {
			textView.setText(info.getTitleResID());
		}
		return view;
	}
}
