package cn.bluerhino.driver.model;

public class LocInfo {

	private int id;
	private Double latitude;
	private Double longitude;
	private long time;
	private int isGps;
	private String ordernum;
	private int orderflag;
	private int isupload;
	
	public LocInfo()
	{
		id = -1;
		latitude = 0.0;
		longitude = 0.0;
		time = 0;
		isGps = 0;
		ordernum = "";
		orderflag= 0;
		isupload = 0;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public int getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(int orderflag) {
		this.orderflag = orderflag;
	}

	public int getIsupload() {
		return isupload;
	}

	public void setIsupload(int isupload) {
		this.isupload = isupload;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getIsGps() {
		return isGps;
	}

	public void setIsGps(int isGps) {
		this.isGps = isGps;
	}



}
