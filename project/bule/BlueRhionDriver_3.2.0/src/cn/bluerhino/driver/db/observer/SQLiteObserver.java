package cn.bluerhino.driver.db.observer;

import java.util.List;

import android.content.Context;

public abstract class SQLiteObserver<T> {

	protected Context mContext;
	protected List<T> tList;
	
	public SQLiteObserver(Context context, List<T> tList) {
		this.mContext = context;
		this.tList = tList;
	}

	public abstract List<T> onChange();

}
