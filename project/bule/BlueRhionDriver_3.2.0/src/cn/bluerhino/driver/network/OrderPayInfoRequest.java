package cn.bluerhino.driver.network;

import java.lang.reflect.Type;

import cn.bluerhino.driver.BRURL;
import cn.bluerhino.driver.model.PayInfo;

import com.bluerhino.library.network.framework.BRModelRequest;
import com.google.gson.reflect.TypeToken;

public class OrderPayInfoRequest extends BRModelRequest<PayInfo> {

	private OrderPayInfoRequest(Builder builder) {
		super(builder);
	}

	@Override
	protected Type getModelType() {
		return new TypeToken<PayInfo>(){}.getType();
	}
	
	public static class Builder extends BRModelBuilder<OrderPayInfoRequest>{

		public Builder() {
	        super();
			setUrl(BRURL.ORDERPAY_INFO_POST_URL);
        }

		@Override
        public OrderPayInfoRequest build() {
	        return new OrderPayInfoRequest(this);
        }
		
	}

}
