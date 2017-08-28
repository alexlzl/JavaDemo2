package cn.bluerhino.driver.db;

import android.content.Context;

public class CurrentDeliverHelper extends DeliverSQLiteHelper {
	
	private static CurrentDeliverHelper INSTANCE;
	
	public synchronized static CurrentDeliverHelper getInstance(Context context) {
		if(null == INSTANCE){
			INSTANCE = new CurrentDeliverHelper(context);
		}
		return INSTANCE;
	}
	
	private CurrentDeliverHelper(Context context) {
		super(context);
	}

	@Override
	protected String getPath() {
		return SQLiteHelperConstant.CURRENT_DELIVER_PATH;
	}

}
