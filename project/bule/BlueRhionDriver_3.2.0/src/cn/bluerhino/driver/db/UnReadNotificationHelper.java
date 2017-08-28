package cn.bluerhino.driver.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.bluerhino.driver.model.UnReadNotification;

public class UnReadNotificationHelper extends DriverSQLiteHelper<UnReadNotification> {
	
	public static final String WHERE = "OrderId=%d";
	
	private static final String UNREAD_WHERE = "Read=1";
	
	private static UnReadNotificationHelper INSTANCE;
	
	public synchronized static UnReadNotificationHelper getInstance(Context context){
		if(null == INSTANCE){
			INSTANCE = new UnReadNotificationHelper(context);
		}
		return INSTANCE;
	}

	private UnReadNotificationHelper(Context context) {
	    super(context);
    }

	@Override
    public List<UnReadNotification> query(String where) {
		Cursor cursor = query(null, where, null, null);
		if(null != cursor){
			List<UnReadNotification> notificationList = new ArrayList<UnReadNotification>();
			try{
				while(cursor.moveToNext()){
					notificationList.add(new UnReadNotification(cursor));
				}
			}finally{
				cursor.close();
			}
			return notificationList;
		}
	    return null;
    }

	@Override
    public List<UnReadNotification> query(UnReadNotification t) {
		String where = String.format(WHERE, t.getOrderNum());
		return query(where);
    }
	
	@Override
	public List<UnReadNotification> query(){
		return query(UNREAD_WHERE);
	}

	@Override
    public boolean insert(UnReadNotification t) {
		boolean succeed = insert(t.createContentValues());
		if (succeed) {
			return true;
		}else{
			return false;
		}
    }

	@Override
    public boolean insert(List<UnReadNotification> t) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try{
			for (UnReadNotification orderInfo : t) {
				boolean succeed = insert(orderInfo.createContentValues());
				if (succeed) {
					count++;
				}
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		if(count == t.size()){
			return true;
		}else{
			return false;
		}
    }
	
	@Override
    public boolean delete() {
		return delete("");
    }

	@Override
    public boolean delete(String where) {
		int count = delete(where, null);
		if (count < 0){
			return false;
		}else{
			return true;
		}
    }

	@Override
    public boolean delete(UnReadNotification t) {
		String where = String.format(WHERE, t.getOrderNum());
		return delete(where);
    }

	@Override
    public boolean update(UnReadNotification t) {
		String where = String.format(WHERE, t.getOrderNum());
		int count = update(t.createContentValues(), where, null);
		if (count < 0) {
			return false;
		} else {
			return true;
		}
    }

	@Override
    public boolean update(List<UnReadNotification> t) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		try{
			db.beginTransaction();
			for (UnReadNotification orderInfo : t) {
				boolean succeed = update(orderInfo);
				if (succeed) {
					count++;
				}
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		if(count == t.size()){
			return true;
		}else{
			return false;
		}
    }

	@Override
    protected String getPath() {
	    return SQLiteHelperConstant.UNREADNOTIFICATION_PATH;
    }
}
