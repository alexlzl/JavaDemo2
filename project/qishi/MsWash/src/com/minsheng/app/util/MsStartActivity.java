package com.minsheng.app.util;

import com.minsheng.wash.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 
 * 
 * @describe:启动Activity管理
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:25:50
 * 
 */
public class MsStartActivity {

	/**
	 * activity跳转，回调menu事件，从右至左滑入动画效果
	 * 
	 * @param to
	 *            跳转到的Activity
	 * @param iMenuListener
	 *            标题菜单监听器, 传MainActivity
	 */
	public static void startActivity1(Activity context, Class<?> to) {
		Intent intent = new Intent(context, to);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.activity_in_from_right,
				R.anim.activity_out_to_left);
	}

	/**
	 * activity跳转，回调menu事件，从右至左滑入动画效果
	 * 
	 * @param intent
	 *            自定义intent，可以putExtra
	 * @param iMenuListener
	 *            标题菜单监听器, 传MainActivity
	 */
	// public static void startActivity(Activity context, Intent intent,
	// IMenuListener iMenuListener){
	// SfTabChangeService.getInstance().setIMenuListener(iMenuListener);
	// context.startActivity(intent);
	// context.overridePendingTransition(R.anim.activity_in_from_right,
	// R.anim.activity_out_to_left);
	// }

	/**
	 * activity跳转，从右至左滑入动画效果
	 * 
	 * @param intent
	 *            自定义intent，可以putExtra
	 */
	public static void startActivity(Activity context, Intent intent) {
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.activity_in_from_right,
				R.anim.activity_out_to_left);
	}

	/**
	 * activity跳转，从下至上滑入动画效果
	 * 
	 * @param intent
	 *            自定义intent，可以putExtra
	 */
	public static void startActivityFromBottom(Activity context, Intent intent) {
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.activity_in_from_bottom,
				R.anim.activity_out_to_top);
	}

	/**
	 * activity跳转，从右至左滑入动画效果
	 */
	public static void startActivity(Activity context, Class<?> to) {
		Intent intent = new Intent(context, to);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.activity_in_from_right,
				R.anim.activity_out_to_left);
	}

	/**
	 * activity跳转，从右至左滑入动画效果
	 * 
	 * @param context
	 * @param to
	 * @param bundle
	 * @param requestCode
	 */
	public static void startActivity(Activity context, Class<?> to,
			Bundle bundle) {
		Intent intent = new Intent(context, to);
		intent.putExtras(bundle);
		context.overridePendingTransition(R.anim.activity_in_from_right,
				R.anim.activity_out_to_left);
	}

	/**
	 * activity跳转，从右至左滑入动画效果
	 * 
	 * @param context
	 * @param to
	 * @param bundle
	 * @param requestCode
	 */
	public static void startActivityForResult(Activity context, Class<?> to,
			Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, to);
		intent.putExtras(bundle);
		context.startActivityForResult(intent, requestCode);
		context.overridePendingTransition(R.anim.activity_in_from_right,
				R.anim.activity_out_to_left);
	}

	/**
	 * activity跳转，从右至左滑入动画效果
	 * 
	 * @param context
	 * @param to
	 * @param bundle
	 * @param requestCode
	 */
	public static void startActivityForResult(Activity context, Intent intent,
			int requestCode) {
		context.startActivityForResult(intent, requestCode);
		context.overridePendingTransition(R.anim.activity_in_from_right,
				R.anim.activity_out_to_left);
	}

	/**
	 * activity跳转，从下至上滑入动画效果
	 * 
	 * @param context
	 * @param to
	 * @param bundle
	 * @param requestCode
	 */
	public static void startActivityForResultFromBottom(Activity context,
			Intent intent, int requestCode) {
		context.startActivityForResult(intent, requestCode);
		context.overridePendingTransition(R.anim.activity_in_from_bottom,
				R.anim.activity_out_to_top);
	}

	/**
	 * activity跳转，从右至左滑入动画效果
	 * 
	 * @param context
	 * @param to
	 * @param bundle
	 * @param requestCode
	 */
	public static void startActivityForResult(Activity context, Class<?> to,
			int requestCode) {
		Intent intent = new Intent(context, to);
		context.startActivityForResult(intent, requestCode);
		context.overridePendingTransition(R.anim.activity_in_from_right,
				R.anim.activity_out_to_left);
	}

	/**
	 * activity关闭，从左至右滑出动画效果
	 */
	public static void finishActivity(Activity context) {
		context.finish();
		context.overridePendingTransition(R.anim.activity_in_from_left,
				R.anim.activity_out_to_right);
	}

	/**
	 * activity关闭，从上至下滑出动画效果
	 */
	public static void finishActivityToBottom(Activity context) {
		context.finish();
		context.overridePendingTransition(R.anim.activity_in_from_top,
				R.anim.activity_out_to_bottom);
	}

	/**
	 * activity跳转，从右至左滑入动画效果
	 * 
	 * @param intent
	 *            自定义intent，可以putExtra
	 */
	// public static void startActivityForAddressChanged(Activity context,
	// Intent intent, IAddressChangedListener iAddressChangedListener){
	// AddressChangedUtil.addressChangedListener = iAddressChangedListener;
	// context.startActivity(intent);
	// context.overridePendingTransition(R.anim.activity_in_from_right,
	// R.anim.activity_out_to_left);
	// }

}
