package cn.bluerhino.driver.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.bluerhino.driver.model.OrderDeal;

public class OrderDealSQLiteHelper extends DriverSQLiteHelper<OrderDeal> {

	public static final String WHERE = "OrderId=%d";
	
	private static OrderDealSQLiteHelper INSTANCE;

	public synchronized static final OrderDealSQLiteHelper getInstance(Context context) {
		if (null == INSTANCE) {
			INSTANCE = new OrderDealSQLiteHelper(context);
		}

		return INSTANCE;
	}

	public OrderDealSQLiteHelper(Context context) {
		super(context);
	}
	
	@Override
    public List<OrderDeal> query() {
		return query("");
    }

	@Override
	public List<OrderDeal> query(String where) {
		Cursor cursor = query(null, where, null, null);
		if(null != cursor){
			List<OrderDeal> orderdealList = new ArrayList<OrderDeal>();
			try{
				while(cursor.moveToNext()){
					orderdealList.add(new OrderDeal(cursor));
				}
			}finally{
				cursor.close();
			}
			return orderdealList;
		}
	    return null;
	}

	@Override
	public List<OrderDeal> query(OrderDeal orderdeal) {
		String where = String.format(WHERE, orderdeal.getOrderId());
		return query(where);
	}

	@Override
	public boolean insert(OrderDeal orderdeal) {
		boolean succeed = insert(orderdeal.createContentValues());
		if (succeed) {
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean insert(List<OrderDeal> orderdealList) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			for (OrderDeal orderdeal : orderdealList) {
				boolean succeed = insert(orderdeal.createContentValues());
				if (succeed) {
					count++;
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		if (count == orderdealList.size()) {
			return true;
		} else {
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
	public boolean delete(OrderDeal orderdeal) {
		String where = String.format(WHERE, orderdeal.getOrderId());
		return delete(where);
	}

	@Override
	public boolean update(OrderDeal orderdeal) {
		String where = String.format(WHERE, orderdeal.getOrderId());
		int count = update(orderdeal.createContentValues(), where, null);
		if (count < 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean update(List<OrderDeal> orderdealList) {
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		try{
			db.beginTransaction();
			for (OrderDeal orderdeal : orderdealList) {
				boolean succeed = update(orderdeal);
				if (succeed) {
					count++;
				}
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		if(count == orderdealList.size()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected String getPath() {
		return SQLiteHelperConstant.ORDERDEAL_PATH;
	}

}
