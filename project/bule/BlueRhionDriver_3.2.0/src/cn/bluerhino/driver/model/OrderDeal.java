package cn.bluerhino.driver.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.bluerhino.library.model.BaseContainer.BaseColumn;

public class OrderDeal implements BaseColumn {

	public static final String ORDERID = "OrderId";
	public static final String OrderFLAG = "OrderFlag";
	public static final String TIME = "Time";

	private static final int ORDERID_INDEX = 1;
	private static final int ORDERFALG_INDEX = 2;
	private static final int TIME_INDEX = 3;

	private int mOrderId;
	private int mOrderFlag;
	private String mTime;

	public OrderDeal(int orderNum, int flag, String time) {
		this.mOrderId = orderNum;
		this.mOrderFlag = flag;
		this.mTime = time;
	}

	public OrderDeal(Cursor cursor) {
		this.mOrderId = cursor.getInt(ORDERID_INDEX);
		this.mOrderFlag = cursor.getInt(ORDERFALG_INDEX);
		this.mTime = cursor.getString(TIME_INDEX);
	}

	public final int getOrderId() {
		return mOrderId;
	}

	public final int getFlag() {
		return mOrderFlag;
	}

	public final String getTime() {
		return mTime;
	}

	public final void setOrderNum(int orderNum) {
		this.mOrderId = orderNum;
	}

	public final void setFlag(int flag) {
		this.mOrderFlag = flag;
	}

	public final void setTime(String time) {
		this.mTime = time;
	}

	public final ContentValues createContentValues() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ORDERID, mOrderId);
		contentValues.put(OrderFLAG, mOrderFlag);
		contentValues.put(TIME, mTime);
		return contentValues;
	}

	@Override
    public String toString() {
	    return "OrderDeal [mOrderId=" + mOrderId + ", mOrderFlag=" + mOrderFlag + ", mTime="
	            + mTime + "]";
    }
}
