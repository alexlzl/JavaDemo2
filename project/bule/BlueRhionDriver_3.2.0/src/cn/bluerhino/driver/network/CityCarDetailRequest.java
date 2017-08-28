package cn.bluerhino.driver.network;

import java.lang.reflect.Type;
import java.util.List;

import cn.bluerhino.driver.BRURL;
import cn.bluerhino.driver.model.CityCarDetail;

import com.bluerhino.library.network.framework.BRModelListRequest;
import com.google.gson.reflect.TypeToken;

public class CityCarDetailRequest extends BRModelListRequest<List<CityCarDetail>> {
	
	private CityCarDetailRequest(Builder builder) {
		super(builder);
	}

	@Override
	protected Type getModelType() {
	    return new TypeToken<List<CityCarDetail>>() {}.getType();
    }

	public static class Builder extends BRModelListBuilder<CityCarDetailRequest> {
		
		public Builder() {
			setUrl(BRURL.CITYCAR_DETAIL_POST_URL);
        }

		@Override
		public CityCarDetailRequest build() {
			return new CityCarDetailRequest(this);
		}
	}

}
