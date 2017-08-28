package com.bluerhino.library.network.framework;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

public abstract class BRErrorListener implements ErrorListener {

	@Override
	public abstract void onErrorResponse(VolleyError error);

}
