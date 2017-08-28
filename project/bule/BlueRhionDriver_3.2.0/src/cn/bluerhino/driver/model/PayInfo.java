package cn.bluerhino.driver.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.bluerhino.library.model.BRModel;

public class PayInfo extends BRModel {

//	"Kilometer": "11",
//    "WaitTime": "0",
//    "IsJourneyChange": "1",
//    "BillOfNight": "35",
//    "BillOfTime": "0",
//    "BillOfMile": "75",
//    "TotalFree": "0",
//    "NeedPay": "110",
//    "Pay": "0",
//    "NoPay": "110",
//    "NeedCashPay": "0",
//    "needLaKaLa": "1"
	
	private String Kilometer;
	private String WaitTime;
	private int IsJourneyChange;
	private int IsNightOrder;
	private String TotalFee;
	private String NeedPay;
	private String Pay;
	private String NoPay;
	private String NeedCashPay;
	private String BillOfMile;
	private String BillOfTime;
	private String BillOfNight;
	private int needLaKaLa;

	public PayInfo() {
		super();
	}

	public PayInfo(Parcel source) {
		Kilometer = source.readString();
		WaitTime = source.readString();
		IsJourneyChange = source.readInt();
		IsNightOrder = source.readInt();
		TotalFee = source.readString();
		NeedPay = source.readString();
		Pay = source.readString();
		NoPay = source.readString();
		NeedCashPay = source.readString();

		BillOfMile = source.readString();
		BillOfTime = source.readString();
		BillOfNight = source.readString();
		needLaKaLa = source.readInt();
	}

	public final String getKilometer() {
		return Kilometer;
	}

	public final String getWaitTime() {
		return WaitTime;
	}

	public final int getIsJourneyChange() {
		return IsJourneyChange;
	}

	public final int getIsNightOrder() {
		return IsNightOrder;
	}

	public final String getTotalFee() {
		return TotalFee;
	}

	public final String getNeedPay() {
		return NeedPay;
	}

	public final String getPay() {
		return Pay;
	}

	public final String getNoPay() {
		return NoPay;
	}

	public String getNeedCashPay() {
		return NeedCashPay;
	}

	public void setNeedCashPay(String needCashPay) {
		NeedCashPay = needCashPay;
	}

	public final void setKilometer(String kilometer) {
		Kilometer = kilometer;
	}

	public final void setWaitTime(String waitTime) {
		WaitTime = waitTime;
	}

	public final void setIsJourneyChange(int isJourneyChange) {
		IsJourneyChange = isJourneyChange;
	}

	public final void setIsNightOrder(int isNightOrder) {
		IsNightOrder = isNightOrder;
	}

	public final void setTotalFee(String totalFee) {
		TotalFee = totalFee;
	}

	public final void setNeedPay(String needPay) {
		NeedPay = needPay;
	}

	public final void setPay(String pay) {
		Pay = pay;
	}

	public final void setNoPay(String noPay) {
		NoPay = noPay;
	}
	
	public String getBillOfMile() {
		return BillOfMile;
	}

	public void setBillOfMile(String billOfMile) {
		BillOfMile = billOfMile;
	}

	public String getBillOfTime() {
		return BillOfTime;
	}

	public void setBillOfTime(String billOfTime) {
		BillOfTime = billOfTime;
	}

	public String getBillOfNight() {
		return BillOfNight;
	}

	public void setBillOfNight(String billOfNight) {
		BillOfNight = billOfNight;
	}

	public final int getNeedLaKaLa() {
		return needLaKaLa;
	}

	public final void setNeedLaKaLa(int needLaKaLa) {
		this.needLaKaLa = needLaKaLa;
	}

	@Override
    public String toString() {
	    return "PayInfo [Kilometer=" + Kilometer + ", WaitTime=" + WaitTime + ", IsJourneyChange="
	            + IsJourneyChange + ", IsNightOrder=" + IsNightOrder + ", TotalFee=" + TotalFee
	            + ", NeedPay=" + NeedPay + ", Pay=" + Pay + ", NoPay=" + NoPay + ", NeedCashPay="
	            + NeedCashPay + ", BillOfMile=" + BillOfMile + ", BillOfTime=" + BillOfTime
	            + ", BillOfNight=" + BillOfNight + ", needLaKaLa=" + needLaKaLa + "]";
    }

	public static final Parcelable.Creator<PayInfo> CREATOR = new Parcelable.Creator<PayInfo>() {
		@Override
		public PayInfo createFromParcel(Parcel source) {
			return new PayInfo(source);
		}

		@Override
		public PayInfo[] newArray(int size) {
			return new PayInfo[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

		
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(Kilometer);
		dest.writeString(WaitTime);
		dest.writeInt(IsJourneyChange);
		dest.writeInt(IsNightOrder);
		dest.writeString(TotalFee);
		dest.writeString(NeedPay);
		dest.writeString(Pay);
		dest.writeString(NoPay);
		dest.writeString(NeedCashPay);
		dest.writeString(this.BillOfMile);
		dest.writeString(this.BillOfTime);
		dest.writeString(this.BillOfNight);
		dest.writeInt(needLaKaLa);
	}

	@Override
	public ContentValues createContentValues() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Coloumn.KILOMETER, Kilometer);
		contentValues.put(Coloumn.WAITTIME, WaitTime);
		contentValues.put(Coloumn.ISJOURNEYCHANGE, IsJourneyChange);
		contentValues.put(Coloumn.ISNIGHTORDER, IsNightOrder);
		contentValues.put(Coloumn.TOTALFEE, TotalFee);
		contentValues.put(Coloumn.NEEDPAY, NeedPay);
		contentValues.put(Coloumn.PAY, Pay);
		contentValues.put(Coloumn.NOPAY, NoPay);
		contentValues.put(Coloumn.NEEDCASHPAY, NeedCashPay);
		contentValues.put(Coloumn.BILLOFMILE, BillOfMile);
		contentValues.put(Coloumn.BILLOFTIME, BillOfTime);
		contentValues.put(Coloumn.BILLOFNIGHT, BillOfNight);
		
		return contentValues;
	}

	public static class Coloumn implements BaseColumn {
		public static final String KILOMETER = "Kilometer";
		public static final String WAITTIME = "WaitTime";
		public static final String ISJOURNEYCHANGE = "IsJourneyChange";
		public static final String ISNIGHTORDER = "IsNightOrder";
		public static final String TOTALFEE = "TotalFee";
		public static final String NEEDPAY = "NeedPay";
		public static final String PAY = "Pay";
		public static final String NOPAY = "NoPay";
		public static final String NEEDCASHPAY = "NeedCashPay";
		public static final String BILLOFMILE = "BillOfMile";
		public static final String BILLOFTIME = "BillOfTime";
		public static final String BILLOFNIGHT = "BillOfHight";
	}
}