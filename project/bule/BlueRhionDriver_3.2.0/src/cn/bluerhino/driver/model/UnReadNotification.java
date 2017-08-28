package cn.bluerhino.driver.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.bluerhino.library.model.BaseContainer.BaseColumn;

public class UnReadNotification implements BaseColumn {
	public static final String ORDERID = "OrderId";
	public static final String ORDERFLAG = "OrderFlag";
	public static final String READ = "Read";

	static final int ORDERID_INDEX = 1;
	static final int ORDERFLAG_INDEX = 2;
	static final int READ_INDEX = 3;

	private int mOrderId;
	private int mOrderFlag;
	private int mRead = 1;
	
	public UnReadNotification() {
	}

	public UnReadNotification(int orderId, int flag, int read) {
		this.mOrderId = orderId;
		this.mOrderFlag = flag;
		this.mRead = read;
	}

	public UnReadNotification(Cursor cursor) {
		mOrderId = cursor.getInt(ORDERID_INDEX);
		mOrderFlag = cursor.getInt(ORDERFLAG_INDEX);
		mRead = cursor.getInt(READ_INDEX);
	}

	public final int getOrderNum() {
		return mOrderId;
	}

	public final int getFlag() {
		return mOrderFlag;
	}
	
	public final int getRead() {
		return mRead;
	}

	public final void setOrderNum(int orderNum) {
		mOrderId = orderNum;
	}
	
	public final void setFlag(int flag) {
		this.mOrderFlag = flag;
	}

	public final void setRead(int read) {
		mRead = read;
	}

	public final ContentValues createContentValues() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ORDERID, mOrderId);
		contentValues.put(ORDERFLAG, mOrderFlag);
		contentValues.put(READ, mRead);
		return contentValues;
	}

	@Override
    public String toString() {
	    return "UnReadNotification [mOrderId=" + mOrderId + ", mOrderFlag=" + mOrderFlag
	            + ", mRead=" + mRead + "]";
    }
	
}
