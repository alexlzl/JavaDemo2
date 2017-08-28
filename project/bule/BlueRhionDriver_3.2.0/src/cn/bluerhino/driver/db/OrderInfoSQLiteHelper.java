package cn.bluerhino.driver.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.bluerhino.driver.model.OrderInfo;

public abstract class OrderInfoSQLiteHelper extends DriverSQLiteHelper<OrderInfo> {
	
	protected static final String WHERE = "OrderId=%d";
	
	OrderInfoSQLiteHelper(Context context) {
		super(context);
	}
	
	@Override
	public List<OrderInfo> query() {
		return this.query("");
	}
	
	@Override
	public List<OrderInfo> query(String where) {
		Cursor cursor = query(null, where, null, null);
		if (null != cursor) {
			List<OrderInfo> orderList = new ArrayList<OrderInfo>();
			try {
				while (cursor.moveToNext()) {
					orderList.add(new OrderInfo(cursor));
				}
			} finally {
				cursor.close();
			}
			return orderList;
		}
		return null;
	}

	@Override
	public List<OrderInfo> query(OrderInfo t) {
		String where = String.format(WHERE, t.getOrderNum());
		return query(where);
	}

	@Override
    public boolean insert(OrderInfo t) {
		boolean succeed = insert(t.createContentValues());
		if (succeed) {
			return true;
		}else{
			return false;
		}
    }

	@Override
    public boolean insert(List<OrderInfo> t) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try{
			for (OrderInfo orderInfo : t) {
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
    public boolean delete(OrderInfo t) {
		String where = String.format(WHERE, t.getOrderNum());
		return delete(where);
    }

	@Override
	public boolean update(OrderInfo t) {
		String where = String.format(WHERE, t.getOrderNum());
		int count = update(t.createContentValues(), where, null);
		if (count < 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
    public boolean update(List<OrderInfo> t) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		try{
			db.beginTransaction();
			for (OrderInfo orderInfo : t) {
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
