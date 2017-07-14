package com.ailicai.app.common.reqaction;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.ailicai.app.model.request.Request;
import com.ailicai.app.ui.base.BaseActivity;
import com.ailicai.app.ui.base.BaseFragment;
import com.huoqiu.framework.rest.Response;

import java.util.Map;

/**
 * Created by David on 15/7/22.
 */
public class ServiceSender {

    /**
     * 发送请求.
     *
     * @param request
     * @param listener
     */
    private static void exec(Request request, IwjwRespListener<? extends Response> listener) {
        exec(request, listener, null);
    }

    public static void exec(Context mC, Request request, IwjwRespListener<? extends Response> listener) {
        if (null != mC) {
            listener.setContext(mC);
            exec(request, listener);
        }
    }

    public static void exec(Fragment mf, Request request, IwjwRespListener<? extends Response> listener) {
        if (null != mf) {
            listener.setFragment(mf);
            exec(request, listener);
        }
    }

    /**
     * 发送请求.
     *
     * @param request
     * @param listener
     * @param config   请求定制化.
     */
    public static void exec(Request request, IwjwRespListener<? extends Response> listener, HttpConfig config) {
        if (listener.isDetached()) {
            return;
        }
        RequestPath path = request.getClass().getAnnotation(RequestPath.class);
        if (path == null) throw new IllegalArgumentException("request should be set request path!");
        IwjwHttp http = IwjwHttp.instance(path.value());
        if (config != null) http.configRequest(config);
        if (listener.getWRContext() != null) {
            if (listener.getWRContext() instanceof BaseActivity) {
                listener.setTag(((BaseActivity) listener.getWRContext()).getPageRequestTag().toString());
            }
        } else if (listener.getWRFragment() != null) {
            if (listener.getWRFragment() instanceof BaseFragment) {
                if (listener.getTag() == null || (listener.getTag() != null && TextUtils.isEmpty
                        (listener.getTag().toString()))) {
                    listener.setTag(((BaseFragment) listener.getWRFragment()).getPageRequestTag()
                            .toString());
                }
            }
        }
        http.exec(request, listener);
    }


    /**
     * 支持适用直接传URL
     * Mock是可以使用这个
     * @param request
     * @param listener
     */
    public static void exec(Request request, String url, IwjwRespListener<? extends Response> listener) {
        if (listener.isDetached()) {
            return;
        }
        IwjwHttp http = IwjwHttp.instance(url);
        if (listener.getWRContext() != null) {
            if (listener.getWRContext() instanceof BaseActivity) {
                listener.setTag(((BaseActivity) listener.getWRContext()).getPageRequestTag().toString());
            }
        } else if (listener.getWRFragment() != null) {
            if (listener.getWRFragment() instanceof BaseFragment) {
                listener.setTag(((BaseFragment) listener.getWRFragment()).getPageRequestTag().toString());
            }
        }
        http.exec(request, listener);
    }

    /**
     * @param paramsObject
     * @param url
     * @param listener
     */
    public static void exec(Map paramsObject, String url, IwjwRespListener listener) {
        if (listener.isDetached()) {
            return;
        }
        IwjwHttp http = IwjwHttp.instance(url);
        if (listener.getWRContext() != null) {
            if (listener.getWRContext() instanceof BaseActivity) {
                listener.setTag(((BaseActivity) listener.getWRContext()).getPageRequestTag().toString());
            }
        } else if (listener.getWRFragment() != null) {
            if (listener.getWRFragment() instanceof BaseFragment) {
                listener.setTag(((BaseFragment) listener.getWRFragment()).getPageRequestTag().toString());
            }
        }
        http.exec(paramsObject, listener);
    }
}