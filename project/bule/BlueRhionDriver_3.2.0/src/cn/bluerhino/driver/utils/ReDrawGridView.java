package cn.bluerhino.driver.utils;

import java.lang.reflect.Field;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

public class ReDrawGridView {

	public static void setGridViewHeightBasedOnChildren(GridView gridView) {
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int rows = 0;
		int columns = 0;
		int horizontalBorderHeight = 0; // 分割线的高度
		Class<?> clazz = gridView.getClass();
		try {
			Field column = clazz.getDeclaredField("mRequestedNumColumns");
			column.setAccessible(true);
			columns = (Integer) column.get(gridView);
			
			Field horizontalSpacing = clazz
					.getDeclaredField("mRequestedHorizontalSpacing");
			horizontalSpacing.setAccessible(true);
			horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (listAdapter.getCount() % columns > 0) {
			rows = listAdapter.getCount() / columns + 1;
		} else {
			rows = listAdapter.getCount() / columns;
		}
		int totalHeight = 0;
		for (int i = 0; i < rows; i++) { 
			View listItem = listAdapter.getView(i, null, gridView);
			listItem.measure(0, 0); 
			totalHeight += listItem.getMeasuredHeight(); 
		}
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight + horizontalBorderHeight * (rows - 1);
		gridView.setLayoutParams(params);
	}
}
