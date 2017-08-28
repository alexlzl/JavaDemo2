package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRJsonRequest;

public class UploadPoiRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
	private UploadPoiRequest(Builder builder) {
		super(builder);
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.UPLOAD_POI_POST_URL);
		}

		@Override
		public UploadPoiRequest build() {
			return new UploadPoiRequest(this);
		}

	}

}
