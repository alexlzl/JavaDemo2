package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;
import com.bluerhino.library.network.framework.BRJsonRequest;

/**
 * Describe:
 * 
 * Date:2015-7-21
 * 
 * Author:liuzhouliang
 */
public class LoadingURLRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
	protected LoadingURLRequest(JsonBuilder builder) {
		super(builder);
		// TODO Auto-generated constructor stub
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.GET_LOADING_URL);
		}

		@Override
		public LoadingURLRequest build() {
			return new LoadingURLRequest(this);
		}

	}
}
