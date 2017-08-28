package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRJsonRequest;
import com.bluerhino.library.network.framework.BRRequestParams;

public class UpdateDriverInfoRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
    private UpdateDriverInfoRequest(Builder builder) {
		super(builder);
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.UPDATE_DRIVERINFO_POST_URL);
		}
		
		@Override
        public BRFastBuilder<BRJsonRequest> setParams(BRRequestParams params) {
	        return super.setParams(params);
        }

		@Override
		public UpdateDriverInfoRequest build() {
			return new UpdateDriverInfoRequest(this);
		}
	}
}
