package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

public class CurrentOrderListRequest extends OrderInfoListRequest {

	@SuppressWarnings("deprecation")
    private CurrentOrderListRequest(Builder builder) {
		super(builder);
	}

	public static class Builder extends OrderInfoListRequest.Builder {

		public Builder() {
			super();
			setUrl(BRURL.CURRENT_ORDER_LIST_POST_URL);
		}

	}
}
