package cn.bluerhino.driver.model;

import java.math.BigDecimal;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import cn.bluerhino.driver.ApplicationController;
import com.baidu.location.BDLocation;
import com.bluerhino.library.model.BaseContainer.BaseColumn;

public class LocationInfo implements Parcelable {

	public static final int NONE = 0;

	public static final int UPLOAD = 1;

	/*
	 * city=北京市, address=北京市朝阳区西大望路15号1号楼, latitude=39.908743,
	 * longitude=116.485436, time=1414464859, locationStatus=0, uploadFlag=0
	 */
	private int id;

	/**
	 * 城市
	 */
	private String city;
	/**
	 * 所在地
	 */
	private String address;

	/**
	 * 所在地纬度
	 */
	private double latitude;
	/**
	 * 所在地经度
	 */
	private double longitude;

	private String time;

	private int locationStatus;
	private int uploadFlag;
	private float speed;
	private double altitude;
	private float verticalAccuracy;
	private float course;

	// private int orderNum;
	// private int orderFlag;

	public LocationInfo() {
	}

	public LocationInfo(BDLocation location) {
		city = location.getCity();
		address = location.getAddrStr();
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		locationStatus = location.getLocType() == BDLocation.TypeGpsLocation ? 1
				: 0;
		// time = ApplicationController.getInstance().generateServerTime() /
		// 1000
		// + "";
		time = System.currentTimeMillis() / 1000 + "";
		uploadFlag = NONE;
		speed = location.getSpeed();
		altitude = getData(location.getAltitude());
		verticalAccuracy = location.getRadius();
		course = location.getDirection();

	}

	private double getData(double data) {
		BigDecimal db = new BigDecimal(data);
		String strData = db.toPlainString();
		double newData = Double.parseDouble(strData);
		return newData;

	}

	public LocationInfo(Cursor cursor) {
		id = cursor.getInt(BaseColumn.ID_INDEX);
		city = cursor.getString(Column.CITY_INDEX);
		address = cursor.getString(Column.ADDRESS_INDEX);
		latitude = cursor.getDouble(Column.LATITUDE_INDEX);
		longitude = cursor.getDouble(Column.LONGITUDE_INDEX);
		locationStatus = cursor.getInt(Column.LOCATIONSTATUS_INDEX);
		time = cursor.getString(Column.TIEM_INDEX);
		uploadFlag = cursor.getInt(Column.UPLOADFLAG_INDEX);

		// orderNum = cursor.getInt(Column.ORDERNUM_INDEX);
		// orderFlag = cursor.getInt(Column.ORDERFLAG_INDEX);
	}

	public LocationInfo(Parcel in) {
		id = in.readInt();
		city = in.readString();
		address = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		time = in.readString();
		locationStatus = in.readInt();
		uploadFlag = in.readInt();
		speed = in.readFloat();
		altitude = in.readDouble();
		verticalAccuracy = in.readFloat();
		course = in.readFloat();
		// orderNum = in.readInt();
		// orderFlag = in.readInt();
	}

	public float getCourse() {
		return course;
	}

	public void setCourse(float course) {
		this.course = course;
	}

	public float getVerticalAccuracy() {
		return verticalAccuracy;
	}

	public void setVerticalAccuracy(float verticalAccuracy) {
		this.verticalAccuracy = verticalAccuracy;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public final int getId() {
		return id;
	}

	public final String getCity() {
		return city;
	}

	public final String getAddress() {
		return address;
	}

	public final double getLatitude() {
		return latitude;
	}

	public final double getLongitude() {
		return longitude;
	}

	public final String getTime() {
		return time;
	}

	public final int getLocationStatus() {
		return locationStatus;
	}

	public final int getUploadFlag() {
		return uploadFlag;
	}

	// public final int getOrderNum() {
	// return orderNum;
	// }
	//
	// public final int getOrderFlag() {
	// return orderFlag;
	// }

	public final void setCity(String city) {
		this.city = city;
	}

	public final void setAddress(String address) {
		this.address = address;
	}

	public final void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public final void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public final void setTime(String time) {
		this.time = time;
	}

	public final void setLoccationStatus(int locationStatus) {
		this.locationStatus = locationStatus;
	}

	public final void setUploadFlag(int flag) {
		this.uploadFlag = flag;
	}

	public ContentValues createContentValues() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Column.CITY, city);
		contentValues.put(Column.ADDRESS, address);
		contentValues.put(Column.LATITUDE, latitude);
		contentValues.put(Column.LONGITUDE, longitude);
		contentValues.put(Column.TIME, time);
		contentValues.put(Column.LOCATIONSTATUS, locationStatus);
		contentValues.put(Column.UPLOADFLAG, uploadFlag);
		return contentValues;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(city);
		dest.writeString(address);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(time);
		dest.writeInt(locationStatus);
		dest.writeInt(uploadFlag);
		dest.writeFloat(speed);
		dest.writeDouble(altitude);
		dest.writeFloat(verticalAccuracy);
		dest.writeFloat(course);
	}

	public static final Parcelable.Creator<LocationInfo> CREATOR = new Parcelable.Creator<LocationInfo>() {
		@Override
		public LocationInfo createFromParcel(Parcel source) {
			return new LocationInfo(source);
		}

		@Override
		public LocationInfo[] newArray(int size) {
			return new LocationInfo[size];
		}
	};

	@Override
	public String toString() {
		return "LocationInfo [id=" + id + ", city=" + city + ", address="
				+ address + ", latitude=" + latitude + ", longitude="
				+ longitude + ", time=" + time + ", locationStatus="
				+ locationStatus + ", uploadFlag=" + uploadFlag + ", speed="
				+ speed + ", altitude=" + altitude + ", verticalAccuracy="
				+ verticalAccuracy + ", course=" + course + "]";
	}

	public class Column implements BaseColumn {
		public static final String CITY = "city";
		public static final String ADDRESS = "address";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String TIME = "time";
		public static final String LOCATIONSTATUS = "locationstatus";
		public static final String UPLOADFLAG = "uploadFlag";

		public static final int CITY_INDEX = 1;
		public static final int ADDRESS_INDEX = 2;
		public static final int LATITUDE_INDEX = 3;
		public static final int LONGITUDE_INDEX = 4;
		public static final int TIEM_INDEX = 5;
		public static final int LOCATIONSTATUS_INDEX = 6;
		public static final int UPLOADFLAG_INDEX = 7;
	}
}
