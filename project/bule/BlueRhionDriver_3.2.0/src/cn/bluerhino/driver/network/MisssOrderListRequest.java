package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

public class MisssOrderListRequest extends OrderInfoListRequest {

	@SuppressWarnings("deprecation")
    private MisssOrderListRequest(Builder builder) {
		super(builder);
	}

	public static class Builder extends OrderInfoListRequest.Builder {

		public Builder() {
			super();
			setUrl(BRURL.MISS_ORDERLIST_POST_URL);
		}

	}

}
