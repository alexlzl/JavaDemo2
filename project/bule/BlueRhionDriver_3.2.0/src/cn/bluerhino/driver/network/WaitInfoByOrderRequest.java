package cn.bluerhino.driver.network;

import java.lang.reflect.Type;

import cn.bluerhino.driver.BRURL;
import cn.bluerhino.driver.model.WaitInfo;

import com.bluerhino.library.network.framework.BRModelRequest;
import com.google.gson.reflect.TypeToken;

public class WaitInfoByOrderRequest extends BRModelRequest<WaitInfo> {
	
	private WaitInfoByOrderRequest(
			com.bluerhino.library.network.framework.BRModelRequest.BRModelBuilder<?> builder) {
		super(builder);
	}

	@Override
    protected Type getModelType() {
	    return new TypeToken<WaitInfo>(){}.getType();
    }
	
	public static class Builder extends BRModelBuilder<WaitInfoByOrderRequest>{
		
		public Builder() {
			super();
			setUrl(BRURL.WAITINFO_BY_ORDER_POST_URL);
		}

		@Override
        public WaitInfoByOrderRequest build() {
	        return new WaitInfoByOrderRequest(this);
        }
		
	}
}
