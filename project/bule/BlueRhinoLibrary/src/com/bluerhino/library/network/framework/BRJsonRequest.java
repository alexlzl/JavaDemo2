package com.bluerhino.library.network.framework;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;

public class BRJsonRequest extends BRFastRequest {

	private BRJsonResponse mSucceedResponse;

	@Deprecated
	protected BRJsonRequest(JsonBuilder builder) {
		super(builder);
		mSucceedResponse = builder.mSucceedResponse;
	}

	@Override
	protected void deliverResponse(String responseJson) {
		try {
			JSONObject jsonObject = new JSONObject(responseJson);
			mSucceedResponse.onResponse(jsonObject);
        } catch (JSONException e) {
	        e.printStackTrace();
        }
	}

	@Override
	public void addToRequestQueue(RequestQueue queue) {
		super.addToRequestQueue(queue);
		queue.add(this);
	}

	public static class JsonBuilder extends BRFastBuilder<BRJsonRequest> {

		private BRJsonResponse mSucceedResponse;

		@Deprecated
		@Override
		public JsonBuilder setSucceedListener(Listener<?> succeedListener) {
			mSucceedResponse = (BRJsonResponse) succeedListener;
			return this;
		}
		
		public JsonBuilder setSucceedListener(BRJsonResponse succeedListener) {
			mSucceedResponse = succeedListener;
			return this;
		}

		@Override
		public BRJsonRequest build() {
			return new BRJsonRequest(this);
		}
	}

	public static abstract class BRJsonResponse implements Listener<JSONObject> {
		public abstract void onResponse(JSONObject response);
	}
}
