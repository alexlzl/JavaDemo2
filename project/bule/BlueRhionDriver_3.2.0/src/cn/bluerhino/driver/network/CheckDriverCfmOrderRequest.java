package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;
import com.bluerhino.library.network.framework.BRJsonRequest;

/**
 * Describe: 判断司机抢单情况 1抢单成功 2抢单失败 3未抢单
 */
public class CheckDriverCfmOrderRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
	protected CheckDriverCfmOrderRequest(JsonBuilder builder) {
		super(builder);
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.CHECK_DRIVER_CFMORDER_URL);
		}

		@Override
		public CheckDriverCfmOrderRequest build() {
			return new CheckDriverCfmOrderRequest(this);
		}

	}
}
