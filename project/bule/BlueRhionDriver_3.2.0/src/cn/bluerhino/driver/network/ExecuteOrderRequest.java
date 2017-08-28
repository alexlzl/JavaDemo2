package cn.bluerhino.driver.network;

import java.lang.reflect.Type;

import cn.bluerhino.driver.BRURL;
import cn.bluerhino.driver.model.OrderExecuteResponse;

import com.bluerhino.library.network.framework.BRModelRequest;
import com.google.gson.reflect.TypeToken;

public class ExecuteOrderRequest extends BRModelRequest<OrderExecuteResponse> {

	private ExecuteOrderRequest(Builder builder) {
		super(builder);
	}

	@Override
	protected Type getModelType() {
		return new TypeToken<OrderExecuteResponse>() {}.getType();
	}

	public static class Builder extends BRModelBuilder<ExecuteOrderRequest> {
		
		public Builder() {
	        super();
			setUrl(BRURL.EXECUTE_ORDER_POST_URL);
        }

		@Override
		public ExecuteOrderRequest build() {
			return new ExecuteOrderRequest(this);
		}
	}
}
