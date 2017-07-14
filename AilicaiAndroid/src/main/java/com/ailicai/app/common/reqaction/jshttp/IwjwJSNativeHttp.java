package com.ailicai.app.common.reqaction.jshttp;

import com.ailicai.app.common.reqaction.HttpConfig;
import com.ailicai.app.common.reqaction.IwjwHttpReq;
import com.ailicai.app.common.reqaction.ssl.RequestQueueFactory;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.huoqiu.framework.commhttp.JsonHttpResponseListener;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.rest.Response;

import java.util.Map;

/**
 * Created by duo,chen on 15/7/22.
 */
public class IwjwJSNativeHttp {

    public final static String CENTER_URL = "/ihouse";
    public static String ROOT_URL = Configuration.DEFAULT.protocol + "://" + Configuration.DEFAULT.hostname + ":" + Configuration.DEFAULT.port;// 生产环境
    private static RequestQueue mQueue;
    private static IwjwJSNativeHttp instance;
    private boolean shouldCache;
    private String path;
    private int method = com.android.volley.Request.Method.POST;
    private boolean isFullUrl = false;

    public static synchronized IwjwJSNativeHttp instance(String path) {
        if (instance == null) {
            instance = new IwjwJSNativeHttp();
        }
        instance.setPath(path);
        return instance;
    }

    public static synchronized RequestQueue getQueue() {
        mQueue = mQueue == null ? RequestQueueFactory.newRequestQueue() : mQueue;
        return mQueue;
    }

    private static String reqUrlForRest(String path) {
        return ROOT_URL + CENTER_URL + path;
    }

    public static void cancel(Object tag) {
        getQueue().cancelAll(tag);
    }

    public void configRequest(HttpConfig config) {

    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setShouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
    }

    public void setFullUrl(boolean fullUrl) {
        isFullUrl = fullUrl;
    }

    public void exec(Map param, JsonHttpResponseListener<? extends Response> listener) {
        String url = isFullUrl ? path : reqUrlForRest(path);
        IwjwHttpReq fastRequest = new IwjwHttpReq(method, url, param, listener);
        fastRequest.setShouldCache(shouldCache);
        fastRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (listener.getTag() != null) fastRequest.setTag(listener.getTag());
        listener.getReportRequest().setRestUrl(url.substring(Configuration.DEFAULT.getDomain().length()));
        listener.setStartTime(System.currentTimeMillis());
        listener.onStart();
        getQueue().add(fastRequest);
    }
}