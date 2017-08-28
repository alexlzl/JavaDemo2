package cn.bluerhino.driver.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import cn.bluerhino.driver.R;

public abstract class DriverSQLiteHelper<T> extends FastSQLHelper<T> {

	private static final String DATABASE_NAME = "BlueRhinoDriver";

	protected Resources mRes;
	
	protected DriverSQLiteHelper(Context context) {
		super(context, DATABASE_NAME);
		
		mRes = context.getResources();
	}

	private void createTable(SQLiteDatabase db) {
		String[] local_database = mRes.getStringArray(R.array.all_local_database);
		for (String sql : local_database) {
			db.execSQL(sql);
		}
	}

	private void dropTable(SQLiteDatabase db) {
		String[] local_database = mRes.getStringArray(R.array.all_drop_database);
		for (String sql : local_database) {
			db.execSQL(sql);
		}
	}

	/**
	 * 先删除在重建
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		dropTable(db);
		createTable(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
		// db.execSQL(String.format("DROP TABLE IF EXISTS %s", mDataBaseName));
		onCreate(db);
	}

}
