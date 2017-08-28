package cn.bluerhino.driver.network;

import com.bluerhino.library.network.framework.BRJsonRequest;

import cn.bluerhino.driver.BRURL;

public class SaveDriverErrorRequest extends BRJsonRequest {

	protected SaveDriverErrorRequest(JsonBuilder builder) {
		super(builder);
		// TODO Auto-generated constructor stub
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.SAVEDRIVERERROR);
		}
	}

}
