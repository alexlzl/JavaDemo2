package cn.bluerhino.driver.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.bluerhino.library.model.BRModel;

/**
 * 
 * @author chowjee
 * @date 2014-7-7
 */
public class DeliverInfo extends BRModel {

	private int Id;
	
	private int OrderId;

	private long ArriveTime;

	private long LeaveTime;
	
	private String TakeMan;

	private String TakePhone;
	
	private int DeliverOrder;
	
	private String DeliverAddress;

	private String DeliverAddressLng;

	private String DeliverAddressLat;

	public DeliverInfo() {
		Id = 0;
		OrderId = 0;
		TakeMan = "";
		TakePhone = "";
		ArriveTime = 0;
		LeaveTime = 0;
		DeliverOrder = 0;
		DeliverAddress = "";
		DeliverAddressLng = "";
		DeliverAddressLat = "";
	}

	public DeliverInfo(Parcel in) {
		Id = in.readInt();
		OrderId = in.readInt();
		TakeMan = in.readString();
		TakePhone = in.readString();
		ArriveTime = in.readLong();
		LeaveTime = in.readLong();
		DeliverOrder = in.readInt();
		DeliverAddress = in.readString();
		DeliverAddressLng = in.readString();
		DeliverAddressLat = in.readString();
	}
	
	public DeliverInfo(Cursor cursor) {
		Id = cursor.getInt(Column.ID_INDEX);
		OrderId = cursor.getInt(Column.ORDERNUM_INDEX);
		TakeMan = cursor.getString(Column.TAKEMAN_INDEX);
		TakePhone = cursor.getString(Column.TAKEPHONE_INDEX);
		ArriveTime = cursor.getLong(Column.ARRIVETIME_INDEX);
		LeaveTime = cursor.getLong(Column.LEAVETIME_INDEX);
		DeliverOrder = cursor.getInt(Column.DELIVERORDER_INDEX);
		DeliverAddress = cursor.getString(Column.DELIVERADDRESS_INDEX);
		DeliverAddressLng = cursor.getString(Column.DELIVERADDRESSLNG_INDEX);
		DeliverAddressLat = cursor.getString(Column.DELIVERADDRESSLAT_INDEX);
	}
	
	public final int getOrderId() {
		return OrderId;
	}

	public final int getId() {
		return Id;
	}

	public final long getArriveTime() {
		return ArriveTime;
	}

	public final long getLeaveTime() {
		return LeaveTime;
	}
	
	public final String getTakePhone() {
		return TakePhone;
	}

	public final String getTakeMan() {
		return TakeMan;
	}
	
	public final int getDeliverOrder(){
		return DeliverOrder;
	}
	
	public final String getDeliverAddress() {
		return DeliverAddress;
	}

	public final String getDeliverAddressLng() {
		return DeliverAddressLng;
	}

	public final String getDeliverAddressLat() {
		return DeliverAddressLat;
	}

	public final void setOrderId(int orderId) {
		OrderId = orderId;
	}

	public final void setId(int id) {
		Id = id;
	}

	public final void setArriveTime(long arriveTime) {
		ArriveTime = arriveTime;
	}

	public final void setLeaveTime(long leaveTime) {
		LeaveTime = leaveTime;
	}

	public final void setTakeMan(String takeMan) {
		TakeMan = takeMan;
	}
	
	public final void setTakePhone(String takePhone) {
		TakePhone = takePhone;
	}
	
	public final void setDeliverOrder(int deliverOrder){
		DeliverOrder = deliverOrder;
	}
	
	public final void setDeliverAddress(String deliverAddress) {
		DeliverAddress = deliverAddress;
	}

	public final void setDeliverAddressLng(String deliverAddressLng) {
		DeliverAddressLng = deliverAddressLng;
	}

	public final void setDeliverAddressLat(String deliverAddressLat) {
		DeliverAddressLat = deliverAddressLat;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Id);
		dest.writeInt(OrderId);
		dest.writeString(TakeMan);
		dest.writeString(TakePhone);
		dest.writeLong(ArriveTime);
		dest.writeLong(LeaveTime);
		dest.writeInt(DeliverOrder);
		dest.writeString(DeliverAddress);
		dest.writeString(DeliverAddressLng);
		dest.writeString(DeliverAddressLat);
	}

	public static final Parcelable.Creator<DeliverInfo> CREATOR = new Parcelable.Creator<DeliverInfo>() {
		public DeliverInfo createFromParcel(Parcel source) {
			return new DeliverInfo(source);
		}

		public DeliverInfo[] newArray(int size) {
			return new DeliverInfo[size];
		}
	};

	@Override
	public ContentValues createContentValues() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Column.ORDERID, OrderId);
		contentValues.put(Column.ID, Id);
		contentValues.put(Column.TAKEMAN, TakeMan);
		contentValues.put(Column.TAKEPHONE, TakePhone);
		contentValues.put(Column.ARRIVETIME, ArriveTime);
		contentValues.put(Column.LEAVETIME, LeaveTime);
		contentValues.put(Column.DELIVERORDER, DeliverOrder);
		contentValues.put(Column.DELIVERADDRESS, DeliverAddress);
		contentValues.put(Column.DELIVERADDRESSLNG, DeliverAddressLng);
		contentValues.put(Column.DELIVERADDRESSLAT, DeliverAddressLat);
		return contentValues;
	}

	@Override
    public String toString() {
	    return "DeliverInfo [Id=" + Id + ", OrderId=" + OrderId + ", ArriveTime=" + ArriveTime
	            + ", LeaveTime=" + LeaveTime + ", TakeMan=" + TakeMan + ", TakePhone=" + TakePhone
	            + ", DeliverOrder=" + DeliverOrder + ", DeliverAddress=" + DeliverAddress
	            + ", DeliverAddressLng=" + DeliverAddressLng + ", DeliverAddressLat="
	            + DeliverAddressLat + "]";
    }

	public static class Column implements BaseColumn {
		
		public static final String ID = "Id";

		public static final String ORDERID = "OrderId";
		
		public static final String TAKEMAN = "TakeMan";

		public static final String TAKEPHONE = "TakePhone";

		public static final String ARRIVETIME = "ArriveTime";
		
		public static final String LEAVETIME = "LeaveTime";
		
		public static final String DELIVERORDER = "DeliverOrder";
		
		public static final String DELIVERADDRESS = "DeliverAddress";

		public static final String DELIVERADDRESSLNG = "DeliverAddressLng";

		public static final String DELIVERADDRESSLAT = "DeliverAddressLat";
		
		public static final int ID_INDEX = 1;
		
		public static final int ORDERNUM_INDEX = 2;
		
		public static final int TAKEMAN_INDEX = 3;
		
		public static final int TAKEPHONE_INDEX = 4;
		
		public static final int ARRIVETIME_INDEX = 5;
		
		public static final int LEAVETIME_INDEX = 6;
		
		public static final int DELIVERORDER_INDEX = 7;
		
		public static final int DELIVERADDRESS_INDEX = 8;
		
		public static final int DELIVERADDRESSLNG_INDEX = 9;
		
		public static final int DELIVERADDRESSLAT_INDEX = 10;
	}
}