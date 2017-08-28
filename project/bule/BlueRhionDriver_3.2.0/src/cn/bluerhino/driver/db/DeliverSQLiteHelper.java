package cn.bluerhino.driver.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.bluerhino.driver.model.DeliverInfo;

public abstract class DeliverSQLiteHelper extends DriverSQLiteHelper<DeliverInfo> {
	
	private static final String WHERE = "OrderId=%d";

	DeliverSQLiteHelper(Context context) {
		super(context);
	}

	@Override
    public List<DeliverInfo> query() {
		return query("");
    }

	@Override
	public List<DeliverInfo> query(String where) {
		Cursor cursor = query(null, where, null, null);
		if(null != cursor){
			List<DeliverInfo> deliverList = new ArrayList<DeliverInfo>();
			try{
				while(cursor.moveToNext()){
					deliverList.add(new DeliverInfo(cursor));
				}
			}finally{
				cursor.close();
			}
			return deliverList;
		}
		
		return null;
	}

	@Override
	public List<DeliverInfo> query(DeliverInfo t) {
		String where = String.format(WHERE, t.getOrderId());
		return query(where);
	}

	@Override
	public boolean insert(DeliverInfo t) {
		boolean succeed = insert(t.createContentValues());
		if (succeed) {
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean insert(List<DeliverInfo> t) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try{
			for (DeliverInfo orderInfo : t) {
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
	public boolean delete(){
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
    public boolean delete(DeliverInfo t) {
		String where = String.format(WHERE, t.getOrderId());
		return delete(where);
    }

	@Override
	public boolean update(DeliverInfo t) {
		String where = String.format(WHERE, t.getOrderId());
		int count = update(t.createContentValues(), where, null);
		if (count < 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
    public boolean update(List<DeliverInfo> t) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		try{
			db.beginTransaction();
			for (DeliverInfo orderInfo : t) {
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
}
