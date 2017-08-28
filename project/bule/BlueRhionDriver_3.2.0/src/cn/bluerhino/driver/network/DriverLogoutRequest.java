package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRJsonRequest;

public class DriverLogoutRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
    private DriverLogoutRequest(JsonBuilder builder) {
		super(builder);
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.DRIVER_LOGOUT_REQUEST_POST_URL);
		}

		@Override
		public DriverLogoutRequest build() {
			return new DriverLogoutRequest(this);
		}

	}
}
