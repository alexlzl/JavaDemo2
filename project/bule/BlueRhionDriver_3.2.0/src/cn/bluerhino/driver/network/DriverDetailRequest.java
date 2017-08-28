package cn.bluerhino.driver.network;

import java.lang.reflect.Type;

import cn.bluerhino.driver.BRURL;
import cn.bluerhino.driver.model.DriverIDetail;

import com.bluerhino.library.network.framework.BRModelRequest;
import com.google.gson.reflect.TypeToken;

public class DriverDetailRequest extends BRModelRequest<DriverIDetail> {

	private DriverDetailRequest(Builder builder) {
		super(builder);
	}

	@Override
	protected Type getModelType() {
		return new TypeToken<DriverIDetail>() {}.getType();
	}

	public static class Builder extends BRModelBuilder<DriverDetailRequest> {
		
		public Builder() {
	        super();
			setUrl(BRURL.DRIVER_DETAIL_POST_URL);
        }

		@Override
		public DriverDetailRequest build() {
			return new DriverDetailRequest(this);
		}
	}

}
