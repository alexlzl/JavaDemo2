package cn.bluerhino.driver.db;

import android.content.Context;

public class HistoryDeliverHelper extends DeliverSQLiteHelper {

	private static HistoryDeliverHelper INSTANCE;
	
	public synchronized static HistoryDeliverHelper getInstance(Context context) {
		if (null == INSTANCE) {
			INSTANCE = new HistoryDeliverHelper(context);
		}
		return INSTANCE;
	}
	
	private HistoryDeliverHelper(Context context) {
		super(context);
	}

	@Override
	protected String getPath() {
		return SQLiteHelperConstant.HISTORY_DELIVER_PATH;
	}
}
