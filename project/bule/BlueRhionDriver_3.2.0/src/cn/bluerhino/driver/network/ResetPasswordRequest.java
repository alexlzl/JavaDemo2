package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRJsonRequest;

public class ResetPasswordRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
	private ResetPasswordRequest(Builder builder) {
		super(builder);
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.RESET_PASSWORD_POST_URL);
		}

		@Override
		public ResetPasswordRequest build() {
			return new ResetPasswordRequest(this);
		}
	}

}
