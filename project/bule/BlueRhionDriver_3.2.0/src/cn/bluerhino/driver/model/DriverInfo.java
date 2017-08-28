package cn.bluerhino.driver.model;

import android.content.ContentValues;
import android.os.Parcel;

import com.bluerhino.library.model.BRModel;
import com.bluerhino.library.network.framework.BRRequestParams;

public class DriverInfo extends BRModel {
//	    "driverInfo": {
//      "image": "http://192.168.10.199:9999//driver/1420426326_7484606.jpg",
//      "Name": "闫瑞",
//      "Phone": "18610968829",
//      "city": "北京",
//      "CarType": "2",
//      "detailType": "15",
//      "detailName": "其他",
//      "CarNum": "京E785001",
//      "IdCardPic": "",
//      "DriverCardPic": "",
//      "DriLicPic": ""
//  },
	public String image;
	public String Name;
	public String Phone;
	public String city;
	public int CarType;
	public int detailType;
	public String detailName;
	public String CarNum;
	public String IdCardPic;
	public String DriverCardPic;
	public String DriLicPic;

	public DriverInfo(Parcel source) {
		image = source.readString();
		Name = source.readString();
		Phone = source.readString();
		city = source.readString();
		CarType = source.readInt();
		detailType = source.readInt();
		detailName = source.readString();
		CarNum = source.readString();
		IdCardPic = source.readString();
		DriverCardPic = source.readString();
		DriLicPic = source.readString();
	}

	@Override
	public String toString() {
		return "DriverInfo [image=" + image + ", Name=" + Name + ", Phone=" + Phone + ", city="
		        + city + ", CarType=" + CarType + ", detailType=" + detailType + ", detailName="
		        + detailName + ", CarNum=" + CarNum + ", IdCardPic=" + IdCardPic
		        + ", DriverCardPic=" + DriverCardPic + ", DriLicPic=" + DriLicPic + "]";
	}

	@Override
	public ContentValues createContentValues() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static final Creator<DriverInfo> CREATOR = new Creator<DriverInfo>() {
		
		@Override
		public DriverInfo[] newArray(int size) {
			return new DriverInfo[size];
		}
		
		@Override
		public DriverInfo createFromParcel(Parcel source) {
			return new DriverInfo(source);
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(image);
		dest.writeString(Name);
		dest.writeString(Phone);
		dest.writeString(city);
		dest.writeInt(CarType);
		dest.writeInt(detailType);
		dest.writeString(detailName);
		dest.writeString(CarNum);
		dest.writeString(IdCardPic);
		dest.writeString(DriverCardPic);
		dest.writeString(DriLicPic);

	}

	public BRRequestParams createRequestParams() {
		BRRequestParams params = new BRRequestParams();
		params.put(Column.IMAGE, image);
		params.put(Column.NAME, Name);
		params.put(Column.PHONE, Phone);
		params.put(Column.CITY, city);
		params.put(Column.CARTYPE, CarType);
		params.put(Column.DETAILTYPE, detailType);
		params.put(Column.DETAILNAME, detailName);
		params.put(Column.CARNUM, CarNum);
		params.put(Column.IDCARDPIC, IdCardPic);
		params.put(Column.DRIVERCARDPIC, DriverCardPic);
		params.put(Column.DRILICPIC, DriLicPic);
		return params;
	}

	public static class Column implements BaseColumn {
		public static final String IMAGE = "image";
		public static final String NAME = "Name";
		public static final String PHONE = "Phone";
		public static final String CITY = "city";
		public static final String CARTYPE = "CarType";
		public static final String DETAILTYPE = "detailType";
		public static final String DETAILNAME = "detailName";
		public static final String CARNUM = "CarNum";
		public static final String IDCARDPIC = "IdCardPic";
		public static final String DRIVERCARDPIC = "DriverCardPic";
		public static final String DRILICPIC = "DriLicPic";
	}

}