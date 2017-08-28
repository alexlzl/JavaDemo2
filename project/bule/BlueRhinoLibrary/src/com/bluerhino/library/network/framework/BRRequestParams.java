package com.bluerhino.library.network.framework;

public class BRRequestParams extends BRRequestHead {

	private static final long serialVersionUID = -270201029094252464L;

	private String mToken;
	private String mVerCode;
	private String mDeviceId;
	
	public BRRequestParams() {
		super();
	}
	
	public BRRequestParams(BRRequestParams params) {
		super(params);
	}
	
	public BRRequestParams(String token) {
		mToken = token;
		put(Constant.TOKEN, mToken);
	}
	
	public BRRequestParams(String token,String verCode, String deviceId) {
		setToken(token);
		setVerCode(verCode);
		setDeviceId(deviceId);
	}

	public final void setToken(String token) {
		this.mToken = token;
		put(Constant.TOKEN, mToken);
	}

	public final String getToken() {
		mToken = get(Constant.TOKEN);
		return mToken;
	}
	
	public final void setVerCode(String verCode){
		mVerCode = verCode;
		put(Constant.VER_CODE, mVerCode);
	}
	
	public final void setDeviceId(String deviceId){
		mDeviceId = deviceId;
		put(Constant.DEVICE_ID, mDeviceId);
	}

	public static class Constant {
		public static final String TOKEN = "session_id";
		public static final String DEVICE_ID = "deviceid";
		public static final String VER_CODE = "fr";
	}
}
