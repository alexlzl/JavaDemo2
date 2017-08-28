package cn.bluerhino.driver.db;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.bluerhino.driver.model.LocationInfo;

public class LocationSQLiteHelper extends DriverSQLiteHelper<LocationInfo> {

	private static final String WHERE = "_id=%d";

	private static final String NONE_WHERE_FORMAT = "uploadflag = 0 and time >= strftime('%s','now')-1800";

	private static LocationSQLiteHelper INSTANCE;

	public synchronized static LocationSQLiteHelper getInstance(Context context) {
		if (null == INSTANCE) {
			INSTANCE = new LocationSQLiteHelper(context);
		}
		return INSTANCE;
	}

	private LocationSQLiteHelper(Context context) {
		super(context);
	}

	@Override
	public List<LocationInfo> query() {
		return query("");
	}

	@Override
	public List<LocationInfo> query(String where) {
		Cursor cursor = query(null, where, null, null);
		if (null != cursor) {
			List<LocationInfo> locationList = new ArrayList<LocationInfo>();
			try {
				while (cursor.moveToNext()) {
					locationList.add(new LocationInfo(cursor));
				}
			} finally {
				cursor.close();
			}
			return locationList;
		}
		return null;
	}

	@Override
	public List<LocationInfo> query(LocationInfo t) {
		String where = String.format(WHERE, t.getId());
		return query(where);
	}

	public List<LocationInfo> quereyUnUpLoad() {
		return query(NONE_WHERE_FORMAT);
	}

	public List<LocationInfo> quereyUnUpLoad(int limit) {
		return quereyUnUpLoad(limit, -1);
	}

	public List<LocationInfo> quereyUnUpLoad(int limit, int offset) {
		String limitStr = null;
		if (-1 != limit) {
			limitStr = String.valueOf(limit);
		}

		String offsetStr = null;
		if (-1 != offset) {
			offsetStr = String.valueOf(offset);
		}

		Cursor cursor = query(null, NONE_WHERE_FORMAT, null, null, limitStr,
				offsetStr);
		if (null != cursor) {
			List<LocationInfo> locationList = new ArrayList<LocationInfo>();
			try {
				while (cursor.moveToNext()) {
					locationList.add(new LocationInfo(cursor));
				}
			} finally {
				cursor.close();
			}
			return locationList;
		}
		return null;
	}

	@Override
	public boolean insert(LocationInfo t) {
		boolean succeed = insert(t.createContentValues());
		if (succeed) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean insert(List<LocationInfo> t) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.beginTransaction();
			for (LocationInfo orderInfo : t) {
				boolean succeed = insert(orderInfo.createContentValues());
				if (succeed) {
					count++;
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		if (count == t.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean delete() {
		return delete();
	}

	@Override
	public boolean delete(String where) {
		int count = delete(where, null);
		if (count < 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean delete(LocationInfo t) {
		String where = String.format(WHERE, t.getId());
		return delete(where);
	}

	@Override
	public boolean update(LocationInfo t) {
		String where = String.format(WHERE, t.getId());
		int count = update(t.createContentValues(), where, null);
		if (count < 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean update(List<LocationInfo> t) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.beginTransaction();
			for (LocationInfo orderInfo : t) {
				boolean succeed = update(orderInfo);
				if (succeed) {
					count++;
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		if (count == t.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected String getPath() {
		return SQLiteHelperConstant.LOCATIONINFO_PATH;
	}
}
