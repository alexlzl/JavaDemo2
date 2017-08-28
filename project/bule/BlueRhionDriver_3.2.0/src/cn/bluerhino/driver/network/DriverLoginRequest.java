package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRJsonRequest;

public class DriverLoginRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
    private DriverLoginRequest(JsonBuilder builder) {
		super(builder);
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.DRIVER_LOGIN_REQUEST_POST_URL);
		}

		@Override
		public DriverLoginRequest build() {
			return new DriverLoginRequest(this);
		}

	}
}
