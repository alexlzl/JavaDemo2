package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRJsonRequest;

public class DriverCheckInRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
    private DriverCheckInRequest(Builder builder) {
		super(builder);
	}
	
	public static class Builder extends JsonBuilder {
		
		public Builder() {
	        super();
			setUrl(BRURL.DRIVER_CHECKIN_POST_URL);
        }
	}

}
