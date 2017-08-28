package com.bluerhino.library.network.framework;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.ParseError;
import com.bluerhino.library.model.BRModel;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public abstract class BRModelListRequest<T extends List<? extends BRModel>> extends
        BRModelRequest<BRModel> {
	
	private BRModelListResponse<T> mSucceedListene;

	@SuppressWarnings("unchecked")
	protected BRModelListRequest(BRModelListBuilder<?> builder) {
		super(builder);
		mSucceedListene = (BRModelListResponse<T>) builder.mSucceedListene;
	}
	
	@Override
    protected String parserDataJson(JSONObject responseJson) {
		try {
			JSONArray jsonArray = responseJson.getJSONArray("data");
			return jsonArray.toString();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		return super.parserDataJson(responseJson);
    }

	@Override
	protected void deliverResponse(String response) {
		try {
			T model = GSON.fromJson(response.toString(), getModelType());
			mSucceedListene.onResponse(model);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			deliverError(new ParseError(e));
		} catch (JsonParseException e) {
			e.printStackTrace();
			deliverError(new ParseError(e));
		}
	}

	public static abstract class BRModelListBuilder<C extends BRModelListRequest<?>> extends
	        BRModelBuilder<C> {

		private BRModelListResponse<? extends List<? extends BRModel>> mSucceedListene;

		public BRModelListBuilder<C> setSucceedListener(
		        BRModelListResponse<? extends List<? extends BRModel>> succeedListListener) {
			mSucceedListene = succeedListListener;
			return this;
		}
	}

	public static abstract class BRModelListResponse<J extends List<? extends BRModel>> extends
	        BRModelResponse<BRModel> {
		
		@Deprecated
		@Override
		public void onResponse(BRModel model) {
			throw new IllegalArgumentException("Method invocation error!");
		}

		public abstract void onResponse(J modelList);
	}

}
