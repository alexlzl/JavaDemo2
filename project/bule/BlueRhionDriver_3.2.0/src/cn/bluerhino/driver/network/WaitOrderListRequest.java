package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

public class WaitOrderListRequest extends OrderInfoListRequest {

	@SuppressWarnings("deprecation")
    private WaitOrderListRequest(Builder builder) {
		super(builder);
	}

	public static class Builder extends OrderInfoListRequest.Builder {

		public Builder() {
			super();
			setUrl(BRURL.WAIT_ORDER_LIST_POST_URL);
		}

	}

}
