package com.bluerhino.library.network.framework;

import java.lang.reflect.Type;

import com.android.volley.ParseError;
import com.android.volley.Response.Listener;
import com.bluerhino.library.model.BRModel;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public abstract class BRModelRequest<T extends BRModel> extends BRFastRequest {

	protected final Gson GSON = new Gson();
	
	protected BRModelResponse<T> mSucceedListene;
	
	@SuppressWarnings({"unchecked", "deprecation" })
    protected BRModelRequest(BRModelBuilder<?> builder) {
		super(builder);
		mSucceedListene = (BRModelResponse<T>) builder.mSucceedListene;
	}

	@Override
	protected void deliverResponse(String response){
		try {
			T model = GSON.fromJson(response, getModelType());
			mSucceedListene.onResponse(model);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			deliverError(new ParseError(e));
		} catch (JsonParseException e) {
			e.printStackTrace();
			deliverError(new ParseError(e));
		}
	}

	protected abstract Type getModelType();

	public static abstract class BRModelBuilder<C extends BRModelRequest<? extends BRModel>>
	        extends BRFastBuilder<C> {
		
		private BRModelResponse<? extends BRModel> mSucceedListene;
		
		@Deprecated
		@Override
		public final BRModelBuilder<C> setSucceedListener(Listener<?> succeedListener) {
			throw new IllegalArgumentException("Method invocation error!");
		}

		public BRModelBuilder<C> setSucceedListener(
		        BRModelResponse<? extends BRModel> succeedListener) {
			mSucceedListene = succeedListener;
			return this;
		}

		@Override
		public abstract C build();
	}
	
	public static abstract class BRModelResponse<J extends BRModel> implements Listener<J> {
		public abstract void onResponse(J model);
	}
}
