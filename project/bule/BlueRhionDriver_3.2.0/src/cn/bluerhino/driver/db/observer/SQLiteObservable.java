package cn.bluerhino.driver.db.observer;

import java.lang.ref.WeakReference;

import android.database.Observable;
import android.os.Handler;

public class SQLiteObservable extends Observable<SQLiteObserver<?>> {

//	private final Map<SQLiteObserver, String> mTagMap = new IdentityHashMap<SQLiteObserver, String>();

	private Handler mHanlder = new Handler();

	private static class DispatchRunnable implements Runnable {
		private final WeakReference<SQLiteObservable> mWeakOwner;

		public DispatchRunnable(SQLiteObservable observable) {
			mWeakOwner = new WeakReference<SQLiteObservable>(observable);
		}

		@Override
		public void run() {
			SQLiteObservable weakOwner = mWeakOwner.get();
			if (null == weakOwner) {
				return;
			}

//			synchronized (weakOwner.mObservers) {
//				for (SQLiteObserver observer : weakOwner.mObservers) {
//					String dispatchTag = weakOwner.mTagMap.get(observer);
//					if (!TextUtils.equals(observer.tag, dispatchTag)) {
//					}
//					observer.onChange();
//				}
//			}

			synchronized (weakOwner.mObservers) {
				for (SQLiteObserver<?> observer : weakOwner.mObservers) {
					observer.onChange();
				}
			}
		}
	}

//	public void registerObserver(SQLiteObserver observer) {
//		super.registerObserver(observer);
//	}

//	public void registerObserver(SQLiteObserver observer, String dispatchTag) {
//		if (TextUtils.isEmpty(dispatchTag)) {
//			throw new IllegalArgumentException("The dispatchTag is null.");
//		}
//
//		synchronized (mTagMap) {
//			if (mTagMap.containsKey(observer)) {
//				throw new IllegalStateException("observer " + observer + " is already registered.");
//			}
//			mTagMap.put(observer, dispatchTag);
//		}
//		super.registerObserver(observer);
//	}

	public void dispatchChange() {
		mHanlder.post(new DispatchRunnable(this));
	}
}
