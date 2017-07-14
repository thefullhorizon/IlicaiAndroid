package com.huoqiu.framework.commhttp;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

public class BaseRequest extends Request<byte[]> {

    public static final String ENCODING_GZIP = "gzip";

    private BaseHttpResponseListener httpResponseListener;

    public BaseRequest(int method, String url, BaseHttpResponseListener listener) {
        super(method, url, new ErrorListenerWrapper(listener));
        this.httpResponseListener = listener;
    }

    /**
     * Run in Dispatcher thread
     */
    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        if (httpResponseListener != null) {
            httpResponseListener.parseNetworkResponse(this);
        }
        byte[] parsedData = parseGzipIfIsNeeded(response.data, response.headers);
        //  Log.d("shejianTime","performRequest耗时:"+response.networkTimeMs);
        return Response.success(parsedData, HttpHeaderParser.parseCacheHeaders(response));
    }

    private byte[] parseGzipIfIsNeeded(byte[] data, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            if (headers.get(key).equalsIgnoreCase(ENCODING_GZIP)) {
                return GzipParser.parse(data);
            }
        }
        return data;
    }

    /**
     * Run in UI Main Thread
     */
    @Override
    protected void deliverResponse(byte[] response) {
        if (httpResponseListener != null) {
            httpResponseListener.onSuccess(response);
            httpResponseListener.onFinish();
        }
    }

    public static class ErrorListenerWrapper implements ErrorListener {

        private BaseHttpResponseListener httpListener;

        public ErrorListenerWrapper(BaseHttpResponseListener httpListener) {
            super();
            this.httpListener = httpListener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (httpListener != null) {
                httpListener.onErrorResponse(error);
                httpListener.onFinish();
            }
        }

    }

}
