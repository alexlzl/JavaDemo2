package cn.bluerhino.driver.model;

/**
 * 签到状态
 * 
 * @author likai
 */
public enum CheckInStat {

	/**
	 * 0 未知，请签到
	 */
	UnknownStat(true),
	/**
	 * 1 签到
	 */
	CheckInStat(false),
	/**
	 * 2签出
	 */
	CheckOutStat(true);
	
	/**
	 * 是否是签退状态
	 */
	private boolean isLoginOff;
	
	private CheckInStat(boolean isLoginOff){
		this.isLoginOff = isLoginOff;
	}

	public static CheckInStat valueOfInt(int ordinal) {
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal");
		}
		return values()[ordinal];
	}
	
	/**
	 * 获得签退状态
	 * @return 签退状态
	 */
	public boolean getIsLogin(){
		return this.isLoginOff;
	}

}
