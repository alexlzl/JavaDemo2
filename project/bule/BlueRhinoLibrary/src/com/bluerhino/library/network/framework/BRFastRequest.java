package com.bluerhino.library.network.framework;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

public abstract class BRFastRequest extends Request<String> implements Cloneable {

	private RequestQueue mRequestQueue;

	private BRFastBuilder<?> mBuilder;

	@Deprecated
	protected BRFastRequest(BRFastBuilder<?> builder) {
		super(Method.POST, builder.mUrl, builder.mErrorListener);

		init(builder);
	}

	private void init(BRFastBuilder<?> builder) {
		mBuilder = builder;
		// mHeaders = builder.mHeaders;
		// mParams = builder.mParams;

		setRetryPolicy(builder.mDefaultRetryPolicy);

		if (!TextUtils.isEmpty(builder.mTag)) {
			setTag(builder.mTag);
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		if (mBuilder.mHeaders != null) {
			return mBuilder.mHeaders;
		} else {
			return super.getHeaders();
		}
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mBuilder.mParams;
	}

	@Override
	public Object clone() {
		BRFastRequest request = null;
		try {
			request = (BRFastRequest) super.clone();
			request.mBuilder = (BRFastBuilder<?>) mBuilder.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}

		return request;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
			        HttpHeaderParser.parseCharset(response.headers));
			JSONObject responseJson = new JSONObject(jsonString);

			if ((!responseJson.has("code") && !responseJson.has("ret"))
			        || !responseJson.has("data")) {
				return Response.error(new ParseError());
			}

			int errorCode = 0;
			if (responseJson.has("data")) {
				if (responseJson.has("code")) {
					errorCode = responseJson.getInt("code");
					if (errorCode != 0) {
						String msg = responseJson.getString("msg");
						return Response.error(new BRResponseError(errorCode, msg));
					}
				} else if (responseJson.has("ret")) {
					// String ret = responseJson.getString("ret");
					errorCode = responseJson.getInt("ret");
					if (errorCode != 1) {
						String msg = responseJson.getString("msg");
						return Response.error(new BRResponseError(errorCode, msg));
					}
				} else {
					return Response.error(new ParseError());
				}

				String jsonData = parserDataJson(responseJson);
				
				if (null == jsonData) {
					return Response.error(new BRResponseError(errorCode, "data is null"));
				} else {
					return Response.success(jsonData, HttpHeaderParser.parseCacheHeaders(response));
				}

			} else {
				return Response.error(new BRResponseError(errorCode, "data is null"));
			}

		} catch (UnsupportedEncodingException e) {
			return Response.error(new BRResponseError(BRResponseError.SERVER_UNKNOWN_ERROR, e
			        .getMessage()));
		} catch (JSONException je) {
			return Response
			        .error(new BRResponseError(BRResponseError.PARSER_ERROR, je.getMessage()));
		}
	}

	protected String parserDataJson(JSONObject responseJson) {
		try {
			JSONObject dataJson = responseJson.getJSONObject("data");
			return dataJson.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return responseJson.toString();
	}

	@Override
	public void deliverError(VolleyError error) {
		super.deliverError(error);
	}

	@Override
	protected abstract void deliverResponse(String responseJson);

	@Deprecated
	public void addToRequestQueue(RequestQueue queue) {
		if (null == queue) {
			return;
		}
		mRequestQueue = queue;
		queue.add(this);
	}

	public void cancelPendingRequests() {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(getTag());
		}
	}

	public void cancelPendingRequests(RequestQueue queue, String tag) {
		if (null == queue || !TextUtils.isEmpty(tag)) {
			return;
		}
		queue.cancelAll(tag);
	}

	public static abstract class BRFastBuilder<T extends BRFastRequest> implements Cloneable {

		private static final int TIMEOUT_MS = 10000;

		protected final DefaultRetryPolicy DEFAULTRETRYPOLICY = new DefaultRetryPolicy(TIMEOUT_MS,
		        0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

		private String mUrl;

		private String mTag;

		private BRRequestHead mHeaders;

		private BRRequestParams mParams;

		private ErrorListener mErrorListener;

		private DefaultRetryPolicy mDefaultRetryPolicy;

		public BRFastBuilder() {
			mDefaultRetryPolicy = DEFAULTRETRYPOLICY;
		}

		public BRFastBuilder<T> setUrl(String url) {
			mUrl = url;
			return this;
		}

		public BRFastBuilder<T> setRequestTag(String tag) {
			mTag = tag;
			return this;
		}

		public BRFastBuilder<T> setHeaders(BRRequestHead headers) {
			this.mHeaders = headers;
			return this;
		}

		public BRFastBuilder<T> setParams(BRRequestParams params) {
			mParams = params;
			return this;
		}

		public BRFastBuilder<T> setErrorListener(ErrorListener errorListener) {
			mErrorListener = errorListener;
			return this;
		}

		public BRFastBuilder<T> setRetryPolicy(DefaultRetryPolicy defaultretrypolicy) {
			mDefaultRetryPolicy = defaultretrypolicy;
			return this;
		}

		@Override
		public Object clone() {
			BRFastBuilder<?> builder = null;

			try {
				builder = (BRFastBuilder<?>) super.clone();
				builder.mUrl = mUrl;
				builder.mTag = mTag;
				builder.mHeaders = (BRRequestHead) mHeaders.clone();
				builder.mParams = (BRRequestParams) mParams.clone();
			} catch (CloneNotSupportedException e) {
				throw new AssertionError(e);
			}

			return builder;
		}

		public abstract BRFastBuilder<T> setSucceedListener(Listener<?> succeedListener);

		public abstract T build();
	}
}
