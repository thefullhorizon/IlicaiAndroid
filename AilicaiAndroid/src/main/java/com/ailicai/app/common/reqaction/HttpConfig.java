package com.ailicai.app.common.reqaction;

import com.android.volley.Request;

/**
 * Created by David on 15/7/22.
 */
public class HttpConfig {

    boolean needCache;
    String tag;
    int method = Request.Method.POST;


    public static void build() {

    }

    public HttpConfig setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public HttpConfig setMethod(int method) {
        this.method = method;
        return this;
    }

    public HttpConfig setCache(boolean needCache) {
        this.needCache = needCache;
        return this;
    }
}
