package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;
import com.bluerhino.library.network.framework.BRJsonRequest;

/**
 * Describe:获取首页TAB信息
 * 
 * Date:2015-8-31
 * 
 * Author:liuzhouliang
 */
public class TabInforRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
	protected TabInforRequest(JsonBuilder builder) {
		super(builder);
		// TODO Auto-generated constructor stub
	}

	public static class Builder extends JsonBuilder {

		public Builder() {
			super();
			setUrl(BRURL.GET_TABINFOR);
		}

		@Override
		public TabInforRequest build() {
			return new TabInforRequest(this);
		}

	}
}
