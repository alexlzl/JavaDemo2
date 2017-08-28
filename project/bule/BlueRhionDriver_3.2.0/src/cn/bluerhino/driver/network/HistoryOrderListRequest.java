package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

public class HistoryOrderListRequest extends OrderInfoListRequest {

	@SuppressWarnings("deprecation")
    private HistoryOrderListRequest(Builder builder) {
		super(builder);
	}

	public static class Builder extends OrderInfoListRequest.Builder {

		public Builder() {
			super();
			setUrl(BRURL.HISTORY_ORDERLIST_POST_URL);
		}

	}

}
