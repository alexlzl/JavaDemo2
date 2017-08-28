package cn.bluerhino.driver.network;

import java.lang.reflect.Type;

import cn.bluerhino.driver.model.OrderInfo;

import com.bluerhino.library.network.framework.BRModelRequest;
import com.google.gson.reflect.TypeToken;

public class OrderInfoRequest extends BRModelRequest<OrderInfo> {

	@Deprecated
	protected OrderInfoRequest(Builder builder) {
		super(builder);
	}

	@Override
	protected Type getModelType() {
		return new TypeToken<OrderInfo>(){}.getType();
	}

	public static class Builder extends BRModelRequest.BRModelBuilder<OrderInfoRequest>{

		@Override
		public OrderInfoRequest build() {
			return new OrderInfoRequest(this);
		}
	}
}
