package cn.bluerhino.driver.network;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bluerhino.driver.model.OrderInfo;

import com.bluerhino.library.network.framework.BRModelListRequest;
import com.google.gson.reflect.TypeToken;

public class OrderInfoListRequest extends BRModelListRequest<List<OrderInfo>> {

	@Deprecated
	protected OrderInfoListRequest(Builder builder) {
		super(builder);
	}
	
	@Override
    protected String parserDataJson(JSONObject responseJson) {
		try {
			JSONArray jsonArray = responseJson.getJSONObject("data").getJSONArray("orderInfo");
			return jsonArray.toString();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
	    return super.parserDataJson(responseJson);
    }

	@Override
	protected Type getModelType() {
		return new TypeToken<List<OrderInfo>>(){}.getType();
	}

	public static class Builder extends BRModelListBuilder<OrderInfoListRequest> {
		public Builder() {
	        super();
        }

		@Override
		public OrderInfoListRequest build() {
			return new OrderInfoListRequest(this);
		}
	}
}
