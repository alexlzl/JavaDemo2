package cn.bluerhino.driver.model;

import android.content.ContentValues;
import android.os.Parcel;

import com.bluerhino.library.model.BRModel;

public class OrderExecuteResponse extends BRModel {

//	‘OrderFlag’:,
//	‘ArriveCount’:,
//	‘WaitTime’:,
//	‘showWaitTime’:,
//	‘waitCost’:, //等待费用
//	‘freeWaitTime’:, //免费等待时间
//	‘waitUnit’:, //等待费收取单位
//	‘waitPrice’:, //等待费单价

	public int OrderFlag;
	public int ArriveCount;
	public int totalSeriveOrder;
	public int WaitTime;
	public int showWaitTime;
	public int waitCost;
	public int freeWaitTime;
	public int waitUnit;
	public int waitPrice;

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
		return "OrderExecuteResponse [OrderFlag=" + OrderFlag + ", ArriveCount=" + ArriveCount
		        + ", WaitTime=" + WaitTime + ", showWaitTime=" + showWaitTime + ", waitCost="
		        + waitCost + ", freeWaitTime=" + freeWaitTime + ", waitUnit=" + waitUnit
		        + ", waitPrice=" + waitPrice + "]";
	}

}
