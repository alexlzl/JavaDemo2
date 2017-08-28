package cn.bluerhino.driver.db;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import cn.bluerhino.driver.db.observer.SQLiteObservable;
import cn.bluerhino.driver.db.observer.SQLiteObserver;
import cn.bluerhino.driver.utils.LogUtil;

public abstract class FastSQLHelper<T> extends SQLiteOpenHelper {

	protected static final String TAG = FastSQLHelper.class.getSimpleName();
	private static final boolean DEBUG = false;
	private static final AtomicInteger sKnownMutationsInFlight = new AtomicInteger(
			0);
	private SQLiteObservable mObservable = new SQLiteObservable();

	protected FastSQLHelper(Context context, String name) {
		super(context, name, null, SQLiteHelperConstant.DATABASE_VERSION);

		if (TextUtils.isEmpty(name)) {
			throw new SQLException("The mDataBaseName cannot be empty");
		}
	}

	public <Observer extends SQLiteObserver<T>> void registerObserver(
			Observer observer) {
		mObservable.registerObserver(observer);
	}

	public <Observer extends SQLiteObserver<T>> void unregisterObserver(
			Observer observer) {
		mObservable.unregisterObserver(observer);
	}

	public void notifyChanged() {
		dispatchChange();
	}

	private void dispatchChange() {
		mObservable.dispatchChange();
	}

	public Cursor query(String[] projectionIn, String where,
			String[] whereArgs, String sort) {
		return query(projectionIn, where, whereArgs, sort, null);
	}

	public final Cursor query(String[] projectionIn, String where,
			String[] whereArgs, String sort, String limit) {
		return query(projectionIn, where, whereArgs, sort, limit, null);
	}

	public final Cursor query(String[] projectionIn, String where,
			String[] whereArgs, String sort, String limit, String offset) {

		String path = getPath();
		boolean isTableExist = tableIsExist(path);
		LogUtil.d(TAG, "判断表====" + path + "===是否存在===" + isTableExist);

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		Cursor cursor = null;
		if (!TextUtils.isEmpty(path) && isTableExist) {
			qb.setTables(path);
			SQLiteDatabase db = getReadableDatabase();

			String buildLimit = null;

			if (!TextUtils.isEmpty(limit)) {
				buildLimit = limit;

				if (!TextUtils.isEmpty(offset)) {
					buildLimit += " OFFSET " + offset;
				}
			}

			cursor = qb.query(db, projectionIn, where, whereArgs, null, null,
					sort, buildLimit);
		}
		return cursor;
	}

	/**
	 * 
	 * Describe:判断某张表是否存在
	 * 
	 * Date:2015-8-6
	 * 
	 * Author:liuzhouliang
	 */
	public boolean tableIsExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
					+ tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public int update(ContentValues values, String where, String[] whereArgs) {
		String path = getPath();

		int count = -1;
		if (!TextUtils.isEmpty(path) && tableIsExist(path)) {
			SQLiteDatabase db = getWritableDatabase();
			count = db.update(path, values, where, whereArgs);
		}
		if (count < 0) {
			throw new SQLException("Failed to update row into " + values);
		} else {
			notifyChanged();
		}
		return count;
	}

	public boolean insert(ContentValues initialValues) {
		if (null == initialValues) {
			throw new IllegalArgumentException(
					"initialValues cannot be null : " + initialValues);
		}

		String path = getPath();

		boolean isSucceed = false;
		if (!TextUtils.isEmpty(path) && tableIsExist(path)) {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues(initialValues);
			long rowId = db.insert(path, BaseColumns._ID, values);

			if (rowId < 0) {
				// throw new SQLException("Failed to insert row into "
				// + initialValues);
			} else {
				notifyChanged();
				isSucceed = true;
			}

			if (DEBUG) {
				Log.v(TAG, "Added content rowId = " + rowId);
			}
		}
		return isSucceed;
	}

	public int bulkInsert(List<ContentValues> values) {
		String path = getPath();

		sKnownMutationsInFlight.incrementAndGet();
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			for (ContentValues value : values) {
				if (db.insert(path, null, value) < 0) {
					throw new SQLException("Failed to bulkInsert row into "
							+ value);
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			sKnownMutationsInFlight.decrementAndGet();
		}

		int count = values.size();

		if (count < 0) {
			throw new SQLException("Failed to insert row into " + values);
		} else {
			notifyChanged();
		}
		return count;
	}

	public int delete(String where, String[] whereArgs) {
		String path = getPath();

		int count = -1;
		if (!TextUtils.isEmpty(path) && tableIsExist(path)) {
			SQLiteDatabase db = getWritableDatabase();
			count = db.delete(path, where, whereArgs);
		}
		if (count < 0) {
			throw new SQLException("Failed to delete row into " + where);
		} else {
			notifyChanged();
		}
		return count;
	}

	public abstract List<T> query();

	public abstract List<T> query(String where);

	public abstract List<T> query(T t);

	public abstract boolean insert(T t);

	public abstract boolean insert(List<T> t);

	public abstract boolean delete();

	public abstract boolean delete(String where);

	public abstract boolean delete(T t);

	public abstract boolean update(T t);

	public abstract boolean update(List<T> t);

	protected abstract String getPath();

	String getSQLWhere(Uri uri, String where) {
		String whereClause = "";
		List<String> segments = uri.getPathSegments();
		if (1 < segments.size()) {
			String segment = uri.getPathSegments().get(1);
			whereClause = "_id= " + segment
					+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : "");
		} else {
			whereClause = where;
		}
		return whereClause;
	}
}
