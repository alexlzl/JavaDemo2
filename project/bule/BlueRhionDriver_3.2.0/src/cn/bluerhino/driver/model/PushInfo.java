package cn.bluerhino.driver.model;

public class PushInfo {

	private String action;
	private String orderNum;
	private String msg;
	private String protocol;
	private String phone;
	private String button_name;
	
	public PushInfo() {
		action = "";
		orderNum = "";
		msg = "";
		protocol = "";
		phone = "";
	}

	public String getButton_name() {
		return button_name;
	}

	public void setButton_name(String button_name) {
		this.button_name = button_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public final String getMsg() {
		return msg;
	}

	public final void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "PushInfo [action=" + action + ", orderNum=" + orderNum
				+ ", msg=" + msg + ", protocol=" + protocol + ", phone="
				+ phone + ", button_name=" + button_name + "]";
	}

}
