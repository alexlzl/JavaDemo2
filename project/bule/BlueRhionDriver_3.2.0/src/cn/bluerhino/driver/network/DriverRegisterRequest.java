package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRJsonRequest;

public class DriverRegisterRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
	protected DriverRegisterRequest(Builder builder) {
		super(builder);
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.DRIVER_REGISTER_POST_URL);
		}

	}

}
