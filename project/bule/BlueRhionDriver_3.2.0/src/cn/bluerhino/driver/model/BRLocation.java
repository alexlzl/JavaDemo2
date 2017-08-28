package cn.bluerhino.driver.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.baidu.location.BDLocation;
import com.bluerhino.library.model.BRModel;

public class BRLocation extends BRModel {

	private String time;
	private double latitude;
	private double longitude;
	private float radius;
	private String addrStr;
	private String street;
	private String city;
	private String cityCode;
	private boolean hasAddr;

	public BRLocation() {
	}

	public BRLocation(BDLocation location) {
		this.time = location.getTime();
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		this.radius = location.getRadius();
		this.addrStr = location.getAddrStr();
		this.street = location.getStreet();
		this.city = location.getCity();
		this.cityCode = location.getCityCode();
		this.hasAddr = location.hasAddr();
	}

	public BRLocation(Parcel source) {
		super(source);
		this.time = source.readString();
		this.latitude = source.readDouble();
		this.longitude = source.readDouble();
		this.radius = source.readFloat();
		this.addrStr = source.readString();
		this.street = source.readString();
		this.city = source.readString();
		this.cityCode = source.readString();
		this.hasAddr = source.readInt() == 1 ? true : false;
	}

	public BRLocation(Cursor cursor) {
		super(cursor);
		this.city = cursor.getString(Column.CITY_INDEX);
		this.addrStr = cursor.getString(Column.ADDRSTR_INDEX);
		this.street = cursor.getString(Column.STREET_INDEX);
		this.cityCode = cursor.getString(Column.CITYCODE_INDEX);
		this.time = cursor.getString(Column.TIME_INDEX);
		this.latitude = cursor.getDouble(Column.LATITUDE_INDEX);
		this.longitude = cursor.getDouble(Column.LONGITUDE_INDEX);
		this.radius = cursor.getFloat(Column.RADIUS_INDEX);
		this.hasAddr = cursor.getInt(Column.HASADDR_INDEX) == 1 ? true : false;
	}

	public final String getTime() {
		return time;
	}

	public final void setTime(String time) {
		this.time = time;
	}

	public final double getLatitude() {
		return latitude;
	}

	public final void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public final double getLongitude() {
		return longitude;
	}

	public final void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public final float getRadius() {
		return radius;
	}

	public final void setRadius(float radius) {
		this.radius = radius;
	}

	public final String getAddrStr() {
		return addrStr;
	}

	public final void setAddrStr(String addrStr) {
		this.addrStr = addrStr;
	}

	public final String getStreet() {
		return street;
	}

	public final void setStreet(String street) {
		this.street = street;
	}

	public final String getCity() {
		return city;
	}

	public final void setCity(String city) {
		this.city = city;
	}

	public final String getCityCode() {
		return cityCode;
	}

	public final void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public final boolean isHasAddr() {
		return hasAddr;
	}

	public final void setHasAddr(boolean hasAddr) {
		this.hasAddr = hasAddr;
	}

	@Override
	public ContentValues createContentValues() {

		ContentValues contentValues = new ContentValues();
		contentValues.put(Column.CITY, city);
		contentValues.put(Column.ADDRSTR, addrStr);
		contentValues.put(Column.STREET, street);
		contentValues.put(Column.CITYCODE, cityCode);
		contentValues.put(Column.TIME, time);
		contentValues.put(Column.LATITUDE, latitude);
		contentValues.put(Column.LONGITUDE, longitude);
		contentValues.put(Column.RADIUS, radius);
		contentValues.put(Column.HASADDR, hasAddr ? 1 : 0);

		return contentValues;
	}

	public static final Creator<BRLocation> CREATOR = new Creator<BRLocation>() {

		@Override
		public BRLocation[] newArray(int size) {
			return new BRLocation[size];
		}

		@Override
		public BRLocation createFromParcel(Parcel source) {
			return new BRLocation(source);
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(city);
		dest.writeString(addrStr);
		dest.writeString(street);
		dest.writeString(cityCode);
		dest.writeString(time);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeFloat(radius);
		dest.writeInt(hasAddr ? 1 : 0);
	}

	@Override
    public String toString() {
	    return "BRLocation [time=" + time + ", latitude=" + latitude + ", longitude=" + longitude
	            + ", radius=" + radius + ", addrStr=" + addrStr + ", street=" + street + ", city="
	            + city + ", cityCode=" + cityCode + ", hasAddr=" + hasAddr + "]";
    }
	
	public static class Column implements BaseColumn{

		public static final String CITY = "city";
		public static final int CITY_INDEX = 1;
		
		public static final String ADDRSTR = "addrStr";
		public static final int ADDRSTR_INDEX = 2;
		
		public static final String STREET = "street";
		public static final int STREET_INDEX = 3;
		
		public static final String CITYCODE = "cityCode";
		public static final int CITYCODE_INDEX = 4;
		
		public static final String TIME = "time";
		public static final int TIME_INDEX = 5;
		
		public static final String LATITUDE = "latitude";
		public static final int LATITUDE_INDEX = 6;
		
		public static final String LONGITUDE = "longitude";
		public static final int LONGITUDE_INDEX = 7;
		
		public static final String RADIUS = "radius";
		public static final int RADIUS_INDEX = 8;
		
		public static final String HASADDR = "hasAddr";
		public static final int HASADDR_INDEX = 9;
	}
}
