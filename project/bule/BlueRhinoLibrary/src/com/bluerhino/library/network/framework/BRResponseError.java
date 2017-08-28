package com.bluerhino.library.network.framework;

import com.android.volley.VolleyError;

public class BRResponseError extends VolleyError {

    private static final long serialVersionUID = -3793272771528738924L;

    public static final int PARSER_ERROR = 11;

    public static final int SERVER_UNKNOWN_ERROR = 12;
    
    public static final int HAS_ARREARAGE_ORDER_ERROR = 600;

    private final int code;
    private final String msg;

    public BRResponseError(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public final int getCode() {
        return code;
    }

    @Override
    public final String getMessage() {
        return msg;
    }

}
