package cn.bluerhino.driver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.os.Parcel;

import com.bluerhino.library.model.BRModel;

public class DriverIDetail extends BRModel {

//	"Name": "闫瑞",
//    "Image": "http://192.168.10.199:9999//driver/1420426326_7484606.jpg",
//    "CarNum": "京E785001",
//    "CarType": "2",
//    "totalOrders": "49",
//    "totalEarns": "0",
//    "commentLevel": "4.000",
//    "checkInStat": "2",
//    "accountStat": "待完善",

//    "weekSort": "1",
//    "WapList": [
//        {
//            "title": "资费说明",
//            "url": "http://www.lanxiniu.com/Wap/question.shtml"
//        },
//        {
//            "title": "常见问题",
//            "url": "http://www.lanxiniu.com/Wap/question.shtml"
//        },
//        {
//            "title": "合作协议",
//            "url": "http://www.lanxiniu.com/Wap/question.shtml"
//        }
//    ]
	
	public String Name;
	public String Image;
	public String CarNum;
	public String CarType;
	public String commentLevel;
	public String accountStat;
	public int totalOrders;
	public double totalEarns;
	public int checkInStat;
	public int weekSort;
	public String Id;
	public DriverInfo driverInfo;
	public List<Map<String,String>> WapList = new ArrayList<Map<String, String>>();
	public double todayEarns;//本日收入
	public int todayOrders;//本日单数	
	
	@Deprecated
	@Override
	public ContentValues createContentValues() {
		return null;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public String toString() {
		return "DriverIDetail [Name=" + Name + ", Image=" + Image + ", CarNum="
				+ CarNum + ", CarType=" + CarType + ", commentLevel="
				+ commentLevel + ", accountStat=" + accountStat
				+ ", totalOrders=" + totalOrders + ", totalEarns=" + totalEarns
				+ ", checkInStat=" + checkInStat + ", weekSort=" + weekSort
				+ ", Id=" + Id + ", driverInfo=" + driverInfo + ", WapList="
				+ WapList + ", todayEarns=" + todayEarns + ", todayOrders="
				+ todayOrders + "]";
	}	
}
