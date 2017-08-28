package cn.bluerhino.driver.model;

import android.content.ContentValues;
import android.os.Parcel;

import com.bluerhino.library.model.BRModel;

public class WaitInfo extends BRModel {

//	‘WaitTime’:,
//	‘showWaitTime’:,
//	‘waitCost’:,
//	‘freeWaitTime’:,
//	‘waitUnit’:,
//	‘waitPrice’:

	public int WaitTime;
	public int showWaitTime;
	public int waitCost;   //等待费用
	public int freeWaitTime; //免费等待时间
	public int waitUnit;  //等待费用收取单位
	public int waitPrice; //等待费用单价

	public WaitInfo() {
		super();
	}

	public WaitInfo(Parcel source) {
		super(source);
		WaitTime = source.readInt();
		showWaitTime = source.readInt();
		waitCost = source.readInt();
		freeWaitTime = source.readInt();
		waitUnit = source.readInt();
		waitPrice = source.readInt();
	}

	@Deprecated
	@Override
	public ContentValues createContentValues() {
		return null;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(WaitTime);
		dest.writeInt(showWaitTime);
		dest.writeInt(waitCost);
		dest.writeInt(freeWaitTime);
		dest.writeInt(waitUnit);
		dest.writeInt(waitPrice);
	}

	@Override
    public String toString() {
	    return "WaitInfo [WaitTime=" + WaitTime + ", showWaitTime=" + showWaitTime + ", waitCost="
	            + waitCost + ", freeWaitTime=" + freeWaitTime + ", waitUnit=" + waitUnit
	            + ", waitPrice=" + waitPrice + "]";
    }

	public int getWaitTime() {
		return WaitTime;
	}

	public int getShowWaitTime() {
		return showWaitTime;
	}

	public int getWaitCost() {
		return waitCost;
	}

	public int getFreeWaitTime() {
		return freeWaitTime;
	}

	public int getWaitUnit() {
		return waitUnit;
	}

	public int getWaitPrice() {
		return waitPrice;
	}
	
	

}
