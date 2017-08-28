package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

@SuppressWarnings("deprecation")
public class WaitOrderRequest extends OrderInfoListRequest {
	
	private WaitOrderRequest(Builder builder) {
		super(builder);
	}
	
	public static class Builder extends OrderInfoListRequest.Builder {
		
		public Builder() {
			super();
			setUrl(BRURL.WAIT_ORDER_POST_URL);
		}
	}
	
}
