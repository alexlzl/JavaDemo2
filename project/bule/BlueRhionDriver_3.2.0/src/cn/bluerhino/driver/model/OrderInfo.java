package cn.bluerhino.driver.model;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.bluerhino.library.model.BRModel;

public class OrderInfo extends BRModel {

    // "ArriveCount": 4,
    // "OrderId": 406014,
    // "OrderType": "跑一趟",
    // "PaymentMode": 5,
    // "ServeType": 100,
    // "PickupMan": "Sara",
    // "CashPayed": null,
    // "Price": 111,
    // "OrderBill": "111元",
    // "PayFlag": 0,
    // "PickupPhone": "15210832515",
    // "PickupAddress": "北京市朝阳区西大望路16号（北京市朝阳区西大望路16号）",
    // "ShippingContent": "不详件",
    // "TransTime": 1417006800,
    // "noPay": "25",
    // "startKilometer": "19公里",
    // "Kilometer": "19公里",
    // "trolleyNum": "0",
    // "isFollowCar": "0",
    // "carringType": "0",
    // "collectCharges": "0.00",
    // "receiptType": "0",
    // "Remark": "",
    // "OrderFlag": 4600,
    // "Reason": "",
    // "RobDistance": "",
    // "PickupAddressLng": "116.484583",
    // "PickupAddressLat": "39.910277",

    private String CashPayed;

    private float Price;

    private String startKilometer;

    private int trolleyNum;

    private int isFollowCar;

    private int carringType;

    private float collectCharges;

    private int receiptType;

    private String Reason;

    private String RobDistance;

    private int CountState;

    private int CountBegin;

    private int CountTimeDifference;

    private int OrderId;

    private int OrderType;

    private int PaymentMode;

    private int ServeType;

    private String PickupMan;

    private int CashPay;

    private int PayFlag;

    private String PickupPhone;

    private String PickupAddress;

    private String ShippingContent;

    private String Kilometer;

    private long TransTime;

    private String noPay;

    private String Remark;

    private int OrderFlag;

    private String PickupAddressLng;

    private String PickupAddressLat;

    private int ArriveCount;

    private String OrderBill;
    

    private int Confirmed;
    // 收货地数据信息
    private List<DeliverInfo> Deliver = new ArrayList<DeliverInfo>();

    /**
     * 含补贴的金额
     */
    private String billMoney;
    
    public OrderInfo(Parcel source) {
        OrderFlag = source.readInt();
        OrderId = source.readInt();
        OrderType = source.readInt();
        PaymentMode = source.readInt();
        ServeType = source.readInt();
        CountState = source.readInt();
        CountBegin = source.readInt();
        CountTimeDifference = source.readInt();
        Kilometer = source.readString();
        CashPay = source.readInt();
        PayFlag = source.readInt();
        noPay = source.readString();
        ShippingContent = source.readString();
        Remark = source.readString();
        PickupMan = source.readString();
        PickupPhone = source.readString();
        PickupAddress = source.readString();
        PickupAddressLng = source.readString();
        PickupAddressLat = source.readString();
        TransTime = source.readLong();
        ArriveCount = source.readInt();
        OrderBill = source.readString();
        source.readTypedList(Deliver, DeliverInfo.CREATOR);
        Confirmed = source.readInt();
        CashPayed = source.readString();
        Price = source.readFloat();
        startKilometer = source.readString();
        trolleyNum = source.readInt();
        isFollowCar = source.readInt();
        carringType = source.readInt();
        collectCharges = source.readFloat();
        receiptType = source.readInt();
        Reason = source.readString();
        RobDistance = source.readString();
        billMoney = source.readString();
    }

    public OrderInfo(Cursor cursor) {
        OrderFlag = cursor.getInt(Column.ORDERFLAG_INDEX);
        OrderId = cursor.getInt(Column.ORDERID_INDEX);
        OrderType = cursor.getInt(Column.ORDERTYPE_INDEX);
        PaymentMode = cursor.getInt(Column.PAYMENTMODE_INDEX);
        ServeType = cursor.getInt(Column.SERVETYPE_INDEX);
        CountState = cursor.getInt(Column.COUNTSTATE_INDEX);
        CountBegin = cursor.getInt(Column.COUNTBEGIN_INDEX);
        CountTimeDifference = cursor.getInt(Column.COUNTTIMEDIFFERENCE_INDEX);
        Kilometer = cursor.getString(Column.KILOMETER_INDEX);
        CashPay = cursor.getInt(Column.CASHPAY_INDEX);
        PayFlag = cursor.getInt(Column.PAYFLAG_INDEX);
        noPay = cursor.getString(Column.NOPAY_INDEX);
        ShippingContent = cursor.getString(Column.SHIPPINGCONTENT_INDEX);
        Remark = cursor.getString(Column.REMARK_INDEX);
        PickupMan = cursor.getString(Column.PICKUPMAN_INDEX);
        PickupPhone = cursor.getString(Column.PICKUPPHONE_INDEX);
        PickupAddress = cursor.getString(Column.PICKUPADDRESS_INDEX);
        PickupAddressLng = cursor.getString(Column.PICKUPADDRESSLNG_INDEX);
        PickupAddressLat = cursor.getString(Column.PICKUPADDRESSLAT_INDEX);
        TransTime = cursor.getLong(Column.TRANSTIME_INDEX);
        ArriveCount = cursor.getInt(Column.ARRIVECOUNT_INDEX);
        OrderBill = cursor.getString(Column.ORDERBILL_INDEX);
    }

    public OrderInfo() {
        OrderFlag = 0;
        OrderId = 0;
        OrderType = 0;
        PaymentMode = 0;
        ServeType = 0;
        CountState = 0;
        CountBegin = 0;
        CountTimeDifference = 0;
        Kilometer = "";
        CashPay = 0;
        PayFlag = 0;
        noPay = "";
        ShippingContent = "";
        Remark = "";
        PickupMan = "";
        PickupPhone = "";
        PickupAddress = "";
        PickupAddressLng = "";
        PickupAddressLat = "";
        TransTime = 0L;
        ArriveCount = 0;
        OrderBill = "0";
        Deliver = new ArrayList<DeliverInfo>();
        Confirmed = 0;
        billMoney = "0";
    }
    
    public OrderInfo(OrderInfo info){
    	 OrderFlag = info.OrderFlag;
         OrderId = info.OrderId;
         OrderType = info.OrderType;
         PaymentMode = info.PaymentMode;
         ServeType = info.ServeType;
         CountState = info.CountState;
         CountBegin = info.CountBegin;
         CountTimeDifference = info.CountTimeDifference;
         Kilometer = info.Kilometer;
         CashPay = info.CashPay;
         
         PayFlag = info.PayFlag;
         noPay = info.noPay;
         ShippingContent = info.ShippingContent;
         Remark = info.Remark;
         PickupMan = info.Remark;
         PickupPhone = info.PickupPhone;
         PickupAddress = info.PickupAddress;
         PickupAddressLng = info.PickupAddressLng;
         PickupAddressLat = info.PickupAddressLat;
         TransTime = info.TransTime;
         
         ArriveCount = info.ArriveCount;
         OrderBill = info.OrderBill;
         Deliver = info.Deliver;
         Confirmed = info.Confirmed;
         CashPayed = info.CashPayed;
         Price = info.Price;
         startKilometer = info.startKilometer;
         trolleyNum = info.trolleyNum;
         isFollowCar = info.isFollowCar;
         carringType = info.carringType;
         
         collectCharges = info.collectCharges;
         receiptType = info.receiptType;
         Reason = info.Reason;
         RobDistance = info.RobDistance;
         billMoney = info.billMoney;
    	
    }

    public final int getFlag() {
        return OrderFlag;
    }

    public final int getOrderNum() {
        return OrderId;
    }

    public final int getOrderType() {
        return OrderType;
    }

    public final int getPaymentMode() {
        return PaymentMode;
    }

    public final int getServeType() {
        return ServeType;
    }

    public final int getCountState() {
        return CountState;
    }

    public final int getCountBegin() {
        return CountBegin;
    }

    public final int getCountTimeDifference() {
        return CountTimeDifference;
    }

    public final String getKilometer() {
        return Kilometer;
    }

    public final int getCashPay() {
        return CashPay;
    }

    public final int getPayFlag() {
        return PayFlag;
    }

    public final String getNoPay() {
        return noPay;
    }

    public final String getShippingContent() {
        return ShippingContent;
    }

    public final String getRemark() {
        return Remark;
    }

    public final String getPickupMan() {
        return PickupMan;
    }

    public final String getPickupPhone() {
        return PickupPhone;
    }

    public final String getPickupAddress() {
        return PickupAddress;
    }

    public final String getPickupAddressLng() {
        return PickupAddressLng;
    }

    public final String getPickupAddressLat() {
        return PickupAddressLat;
    }

    public final long getTransTime() {
        return TransTime;
    }

    public final int getArriveCount() {
        return ArriveCount;
    }

    public final List<DeliverInfo> getDeliver() {
        return Deliver;
    }

    public final void setFlag(int flag) {
        this.OrderFlag = flag;
    }

    public final void setOrderNum(int orderNum) {
        OrderId = orderNum;
    }

    public final void setOrderType(int orderType) {
        OrderType = orderType;
    }

    public final void setPaymentMode(int paymentMode) {
        PaymentMode = paymentMode;
    }

    public final void setServeType(int serveType) {
        ServeType = serveType;
    }

    public final void setCountState(int countState) {
        CountState = countState;
    }

    public final void setCountBegin(int countBegin) {
        CountBegin = countBegin;
    }

    public final void setCountTimeDifference(int countTimeDifference) {
        CountTimeDifference = countTimeDifference;
    }

    public final void setKilometer(String kilometer) {
        Kilometer = kilometer;
    }

    public final void setPayFlag(int payFlag) {
        PayFlag = payFlag;
    }

    public final void setCashPay(int cashPay) {
        CashPay = cashPay;
    }

    public final void setNoPay(String noPay) {
        this.noPay = noPay;
    }

    public final void setShippingContent(String shippingContent) {
        ShippingContent = shippingContent;
    }

    public final void setRemark(String remark) {
        this.Remark = remark;
    }

    public final void setPickupMan(String pickupMan) {
        PickupMan = pickupMan;
    }

    public final void setPickupPhone(String pickupPhone) {
        PickupPhone = pickupPhone;
    }

    public final void setPickupAddress(String pickupAddress) {
        PickupAddress = pickupAddress;
    }

    public final void setPickupAddressX(String pickupAddressX) {
        PickupAddressLng = pickupAddressX;
    }

    public final void setPickupAddressy(String pickupAddressy) {
        PickupAddressLat = pickupAddressy;
    }

    public final void setTransTime(long transTime) {
        TransTime = transTime;
    }

    public final void setPickupAddressLng(String pickupAddressLng) {
        PickupAddressLng = pickupAddressLng;
    }

    public final void setPickupAddressLat(String pickupAddressLat) {
        PickupAddressLat = pickupAddressLat;
    }

    public final void setArriveCount(int arriveCount) {
        ArriveCount = arriveCount;
    }

    public final void setDeliver(List<DeliverInfo> deliver) {
        Deliver = deliver;
    }

    public String getOrderBill() {
        return OrderBill;
    }

    public void setOrderBill(String orderBill) {
        OrderBill = orderBill;
    }

    public final int getConfirmed() {
        return Confirmed;
    }

    public final void setConfirmed(int confirmed) {
        this.Confirmed = confirmed;
    }

    public final String getCashPayed() {
        return CashPayed;
    }

    public final float getPrice() {
        return Price;
    }

    public final String getStartKilometer() {
        return startKilometer;
    }

    public final int getTrolleyNum() {
        return trolleyNum;
    }

    public final int getIsFollowCar() {
        return isFollowCar;
    }

    public final int getCarringType() {
        return carringType;
    }

    public final float getCollectCharges() {
        return collectCharges;
    }

    public final int getReceiptType() {
        return receiptType;
    }

    public final String getReason() {
        return Reason;
    }

    public final String getRobDistance() {
        return RobDistance;
    }

    public final int getOrderId() {
        return OrderId;
    }

    public final int getOrderFlag() {
        return OrderFlag;
    }

    public final void setCashPayed(String cashPayed) {
        CashPayed = cashPayed;
    }

    public final void setPrice(float price) {
        Price = price;
    }

    public final void setStartKilometer(String startKilometer) {
        this.startKilometer = startKilometer;
    }

    public final void setTrolleyNum(int trolleyNum) {
        this.trolleyNum = trolleyNum;
    }

    public final void setIsFollowCar(int isFollowCar) {
        this.isFollowCar = isFollowCar;
    }

    public final void setCarringType(int carringType) {
        this.carringType = carringType;
    }

    public final void setCollectCharges(float collectCharges) {
        this.collectCharges = collectCharges;
    }

    public final void setReceiptType(int receiptType) {
        this.receiptType = receiptType;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public final void setRobDistance(String robDistance) {
        RobDistance = robDistance;
    }

    public final void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public final void setOrderFlag(int orderFlag) {
        OrderFlag = orderFlag;
    }
    
    public void setBillMoney(String billMoney){
    	this.billMoney = billMoney;
    }
    
    public String getBillMoney(){
    	return this.billMoney;
    }

    public static final Parcelable.Creator<OrderInfo> CREATOR = new Parcelable.Creator<OrderInfo>() {
        public OrderInfo createFromParcel(Parcel source) {
            return new OrderInfo(source);
        }

        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(OrderFlag);
        dest.writeInt(OrderId);
        dest.writeInt(OrderType);
        dest.writeInt(PaymentMode);
        dest.writeInt(ServeType);
        dest.writeInt(CountState);
        dest.writeInt(CountBegin);
        dest.writeInt(CountTimeDifference);
        dest.writeString(Kilometer);
        dest.writeInt(CashPay);
        dest.writeInt(PayFlag);
        dest.writeString(noPay);
        dest.writeString(ShippingContent);
        dest.writeString(Remark);
        dest.writeString(PickupMan);
        dest.writeString(PickupPhone);
        dest.writeString(PickupAddress);
        dest.writeString(PickupAddressLng);
        dest.writeString(PickupAddressLat);
        dest.writeLong(TransTime);
        dest.writeInt(ArriveCount);
        dest.writeString(OrderBill);
        dest.writeTypedList(Deliver);
        dest.writeInt(Confirmed);
        dest.writeString(CashPayed);
        dest.writeFloat(Price);
        dest.writeString(startKilometer);
        dest.writeInt(trolleyNum);
        dest.writeInt(isFollowCar);
        dest.writeInt(carringType);
        dest.writeFloat(collectCharges);
        dest.writeInt(receiptType);
        dest.writeString(Reason);
        dest.writeString(RobDistance);
        dest.writeString(billMoney);
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues orderValues = new ContentValues();
        orderValues.put(Column.ORDERFLAG, OrderFlag);
        orderValues.put(Column.ORDERID, OrderId);
        orderValues.put(Column.ORDERTYPE, OrderType);
        orderValues.put(Column.PAYMENTMODE, PaymentMode);
        orderValues.put(Column.SERVETYPE, ServeType);
        orderValues.put(Column.COUNTSTATE, CountState);
        orderValues.put(Column.COUNTBEGIN, CountBegin);
        orderValues.put(Column.COUNTTIMEDIFFERENCE, CountTimeDifference);
        orderValues.put(Column.KILOMETER, Kilometer);
        orderValues.put(Column.CASHPAY, CashPay);
        orderValues.put(Column.PAYFLAG, PayFlag);
        orderValues.put(Column.NOPAY, noPay);
        orderValues.put(Column.SHIPPINGCONTENT, ShippingContent);
        orderValues.put(Column.REMARK, Remark);
        orderValues.put(Column.PICKUPMAN, PickupMan);
        orderValues.put(Column.PICKUPPHONE, PickupPhone);
        orderValues.put(Column.PICKUPADDRESS, PickupAddress);
        orderValues.put(Column.PICKUPADDRESSLNG, PickupAddressLng);
        orderValues.put(Column.PICKUPADDRESSLAT, PickupAddressLat);
        orderValues.put(Column.TRANSTIME, TransTime);
        orderValues.put(Column.ARRIVECOUNT, ArriveCount);
        orderValues.put(Column.ORDERBILL, OrderBill);
        return orderValues;
    }

    @Override
    public String toString() {
        return "OrderInfo [CashPayed=" + CashPayed + ", Price=" + Price
                + ", startKilometer=" + startKilometer + ", trolleyNum="
                + trolleyNum + ", isFollowCar=" + isFollowCar
                + ", carringType=" + carringType + ", collectCharges="
                + collectCharges + ", receiptType=" + receiptType + ", Reason="
                + Reason + ", RobDistance=" + RobDistance + ", CountState="
                + CountState + ", CountBegin=" + CountBegin
                + ", CountTimeDifference=" + CountTimeDifference + ", OrderId="
                + OrderId + ", OrderType=" + OrderType + ", PaymentMode="
                + PaymentMode + ", ServeType=" + ServeType + ", PickupMan="
                + PickupMan + ", CashPay=" + CashPay + ", PayFlag=" + PayFlag
                + ", PickupPhone=" + PickupPhone + ", PickupAddress="
                + PickupAddress + ", ShippingContent=" + ShippingContent
                + ", Kilometer=" + Kilometer + ", TransTime=" + TransTime
                + ", noPay=" + noPay + ", Remark=" + Remark + ", OrderFlag="
                + OrderFlag + ", PickupAddressLng=" + PickupAddressLng
                + ", PickupAddressLat=" + PickupAddressLat + ", ArriveCount="
                + ArriveCount + ", OrderBill=" + OrderBill + ", Confirmed="
                + Confirmed + ", Deliver=" + Deliver +  ", billMoney=" + billMoney + "]";
    }

    /**
     * 订单状态
     * 
     * @author chowjee
     * @date 2014-8-18
     */
    public static class OrderState {

        /** 等待司机抢单 */
        public static final int NEEDDRIVER1 = 1200;
        /** 等待用户确认 */
        public static final int USERAFFIRM = 1500;
        // 待审核
        public static final int AUDITING = 2000;
        /** 等待司机抢单 */
        public static final int NEEDDRIVER2 = 2100;
        /** 抢单提交成功 */
        public static final int NEEDDRIVER3 = 2200;
        /*人工分配订单*/
        public static final int  ALLOCATION=2300;
        /** 等待响应 */
        public static final int WAITRESPONSE = 2600;
        /** 等待服务 */
        public static final int WAITSERVICE = 3000;
        /** 司机出发 */
        public static final int DELIVERDEPARTURE = 3300;
        /** 到达发货地 */
        public static final int REACHSHIPADDRESS = 4000;
        /** 从发货地出发 */
        public static final int DEPARTURESHIPADDRESS = 4150;
        /** 到达收货地 */
        public static final int REACHPLACEOFRECEIPT = 4600;
        /** 从收货地出发 */
        public static final int DEPARTURERECEIPT = 4800;
        /** 服务结束 */
        public static final int SERVICEFINISH = 5000;
        /** 订单取消 */
        public static final int ORDERCANCEL = 10000;
        /** 订单结束 */
        public static final int ORDERFINISH = 20000;
    }

    /***
     * 支付状态
     * 
     * @author chowjee
     * @date 2014-8-18
     */
    public static class CashPayType {
        /** 向发货人收款 */
        public static final int CHARGECONSIGNOR = 31;
        /** 向收货人收款 */
        public static final int CHARGECONSIGNEE = 32;
    }

    public static class PayMode {
        // 余额支付
        public static final int BALANCE = 1;
        // 月结
        public static final int MONTH = 2;
        // 现金支付
        public static final int CASH = 3;
        // 车载pos机支付
        public static final int POSONCAR = 4;
        // 在线支付
        public static final int ONLINE = 5;
        // 优惠券
        public static final int DISCOUNTVOUCHER = 6;

    }

    public static class Column implements BaseColumn {

        public static final String ORDERFLAG = "OrderFlag";

        public static final String ORDERID = "OrderId";

        public static final String ORDERTYPE = "OrderType";

        public static final String PAYMENTMODE = "PaymentMode";

        public static final String SERVETYPE = "ServeType";

        public static final String PAYFLAG = "PayFlag";

        public static final String CASHPAY = "CashPay";

        public static final String NOPAY = "noPay";

        public static final String COUNTSTATE = "CountState";

        public static final String COUNTBEGIN = "CountBegin";

        public static final String COUNTTIMEDIFFERENCE = "CountTimeDifference";

        public static final String SHIPPINGCONTENT = "ShippingContent";

        public static final String KILOMETER = "Kilometer";

        public static final String TRANSTIME = "TransTime";

        public static final String PICKUPMAN = "PickupMan";

        public static final String PICKUPPHONE = "PickupPhone";

        public static final String PICKUPADDRESS = "PickupAddress";

        public static final String PICKUPADDRESSLNG = "PickupAddressLng";

        public static final String PICKUPADDRESSLAT = "PickupAddressLat";

        public static final String REMARK = "Remark";

        public static final String ARRIVECOUNT = "ArriveCount";

        public static final String ORDERBILL = "OrderBill";

        public static final String WHERE_ORDERNUM = "OrderNum=%d";

        public static final int ORDERFLAG_INDEX = 1;

        public static final int ORDERID_INDEX = 2;

        public static final int ORDERTYPE_INDEX = 3;

        public static final int PAYMENTMODE_INDEX = 4;

        public static final int SERVETYPE_INDEX = 5;

        public static final int COUNTSTATE_INDEX = 6;

        public static final int COUNTBEGIN_INDEX = 7;

        public static final int COUNTTIMEDIFFERENCE_INDEX = 8;

        public static final int KILOMETER_INDEX = 9;

        public static final int CASHPAY_INDEX = 10;

        public static final int PAYFLAG_INDEX = 11;

        public static final int NOPAY_INDEX = 12;

        public static final int SHIPPINGCONTENT_INDEX = 13;

        public static final int REMARK_INDEX = 14;

        public static final int PICKUPMAN_INDEX = 15;

        public static final int PICKUPPHONE_INDEX = 16;

        public static final int PICKUPADDRESS_INDEX = 17;

        public static final int PICKUPADDRESSLNG_INDEX = 18;

        public static final int PICKUPADDRESSLAT_INDEX = 19;

        public static final int TRANSTIME_INDEX = 20;

        public static final int ARRIVECOUNT_INDEX = 21;

        public static final int ORDERBILL_INDEX = 22;
    }
}
