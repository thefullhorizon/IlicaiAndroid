package com.huoqiu.framework.commhttp;

import com.android.volley.Response.ErrorListener;

public interface BaseHttpResponseListener extends ErrorListener {

    void onStart();

    void onSuccess(byte[] response);

    void onFinish();

    void onGetReqUrl(String requrl);

    void parseNetworkResponse(BaseRequest request);

}
