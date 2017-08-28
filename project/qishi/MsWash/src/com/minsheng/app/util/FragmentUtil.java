package com.minsheng.app.util;

import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.listener.OnBackListener;
import com.minsheng.wash.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager.BackStackEntry;

/**
 * 
 * 
 * @describe:Fragment管理
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:22:00
 * 
 */

public class FragmentUtil {
	/**
	 * 显示Fragment容器
	 */
	public static int MAIN_ACTIVITY = 0;

	/**
	 * fragment跳转
	 * 
	 * @param context
	 * @param fragment
	 * @param id
	 */
	public static void stratFragment(FragmentActivity context,
			Fragment fragment, int id, boolean isAddOrReplace) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.addToBackStack(fragment.getClass().getName());

		if (isAddOrReplace) {
			fragmentTransaction
					.add(id, fragment, fragment.getClass().getName())
					.commitAllowingStateLoss();
		} else {
			fragmentTransaction.replace(id, fragment,
					fragment.getClass().getName()).commitAllowingStateLoss();
		}

	}

	/**
	 * Fragment跳转
	 * 
	 * @param context
	 * @param fragment
	 */
	public static void stratFragment(FragmentActivity context,
			Fragment fragment, boolean isAddOrReplace) {
		stratFragment(context, fragment, MAIN_ACTIVITY, isAddOrReplace);
	}

	/**
	 * 从右向左划入切换fragment
	 * 
	 * @param context
	 * @param fragment
	 * @param id
	 */
	public static void stratFragmentFromRight(FragmentActivity context,
			Fragment fragment, int id, BaseActivity baseActivity,
			OnBackListener onBackListener, boolean isAddOrReplace) {
		if (null != baseActivity.onBackListenerList) {
			baseActivity.onBackListenerList.add(onBackListener);
		}

		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragmentTransaction
				.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out,
						R.anim.push_left_in, R.anim.push_left_out);

		fragmentTransaction.addToBackStack(fragment.getClass().getName());
		if (isAddOrReplace) {
			fragmentTransaction
					.add(id, fragment, fragment.getClass().getName())
					.commitAllowingStateLoss();
		} else {
			fragmentTransaction.replace(id, fragment,
					fragment.getClass().getName()).commitAllowingStateLoss();
		}

	}

	/**
	 * 从右向左划入切换fragment
	 * 
	 * @param context
	 * @param fragment
	 * @param id
	 */
	public static void stratFragmentFromRight(FragmentActivity context,
			Fragment fragment, BaseActivity baseActivity,
			OnBackListener onBackListener, boolean isAddOrReplace) {

		if (null != baseActivity.onBackListenerList) {
			baseActivity.onBackListenerList.add(onBackListener);
		}

		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragmentTransaction
				.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out,
						R.anim.push_left_in, R.anim.push_left_out);

		fragmentTransaction.addToBackStack(fragment.getClass().getName());

		if (isAddOrReplace) {
			fragmentTransaction.add(MAIN_ACTIVITY, fragment,
					fragment.getClass().getName()).commitAllowingStateLoss();
		} else {
			fragmentTransaction.replace(MAIN_ACTIVITY, fragment,
					fragment.getClass().getName()).commitAllowingStateLoss();
		}

	}

	/**
	 * 从右向左划入切换fragment
	 * 
	 * @param context
	 * @param fragment
	 * @param id
	 */
	public static void stratFragmentFromRightNotBack(FragmentActivity context,
			Fragment fragment, BaseActivity baseActivity,
			OnBackListener onBackListener, boolean isAddOrReplace) {
		if (null != baseActivity.onBackListenerList) {
			baseActivity.onBackListenerList.add(onBackListener);
		}

		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragmentTransaction
				.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out,
						R.anim.push_left_in, R.anim.push_left_out);

		if (isAddOrReplace) {
			fragmentTransaction.add(MAIN_ACTIVITY, fragment,
					fragment.getClass().getName()).commitAllowingStateLoss();
		} else {
			fragmentTransaction.replace(MAIN_ACTIVITY, fragment,
					fragment.getClass().getName()).commitAllowingStateLoss();
		}

		fragmentManager.popBackStackImmediate(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);

	}

	/**
	 * 从下向上划入切换fragment
	 * 
	 * @param context
	 * @param fragment
	 * @param id
	 */
	public static void stratFragmentFromBottom(FragmentActivity context,
			Fragment fragment, int id, boolean isAddOrReplace) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in,
				R.anim.push_bottom_out, R.anim.push_bottom_in,
				R.anim.push_bottom_out);

		fragmentTransaction.addToBackStack(fragment.getClass().getName());
		if (isAddOrReplace) {
			fragmentTransaction
					.add(id, fragment, fragment.getClass().getName())
					.commitAllowingStateLoss();
		} else {
			fragmentTransaction.replace(id, fragment,
					fragment.getClass().getName()).commitAllowingStateLoss();
		}

	}

	/**
	 * Fragment跳转后不可返回
	 * 
	 * @param context
	 * @param fragment
	 * @param id
	 */
	public static void stratFragmentNotBack(FragmentActivity context,
			Fragment fragment, int id, boolean isAddOrReplace) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		if (isAddOrReplace) {
			fragmentTransaction
					.add(id, fragment, fragment.getClass().getName())
					.commitAllowingStateLoss();
		} else {
			fragmentTransaction.replace(id, fragment,
					fragment.getClass().getName()).commitAllowingStateLoss();
		}

		fragmentManager.popBackStackImmediate(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	/**
	 * Fragment出栈
	 * 
	 * @param context
	 */
	public static void popBackStack(BaseActivity context) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		fragmentManager.popBackStackImmediate();
	}

	@SuppressWarnings("unused")
	private static void logBackStack(final String Tag, final String method,
			final FragmentManager fragmentManager) {
		fragmentManager
				.addOnBackStackChangedListener(new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged() {
						LogUtil.d(
								Tag,
								method
										+ "************onBackStackChanged************backStackEntryCount = "
										+ fragmentManager
												.getBackStackEntryCount());
						for (int i = 0; i < fragmentManager
								.getBackStackEntryCount(); i++) {
							BackStackEntry entry = fragmentManager
									.getBackStackEntryAt(i);
							if (null != entry) {
								LogUtil.d(Tag, "---------------------" + i
										+ "--------------------------------");
								LogUtil.d(
										Tag,
										"backStackEntryName = "
												+ entry.getName());
								LogUtil.d(Tag, "---------------------" + i
										+ "--------------------------------");
							}
						}
					}
				});

	}
}
