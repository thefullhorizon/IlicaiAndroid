package com.ailicai.app.common.reqaction.jshttp;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.ailicai.app.common.reqaction.HttpConfig;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.huoqiu.framework.rest.Response;

import java.util.Map;

/**
 * Created by duo.chen on 2016/1/13.
 */
public class ServiceJsCallSender {

    /**
     * 发送请求.
     *
     * @param map
     * @param listener
     */
    private static void exec(String url, boolean isFullApiname, Map map, IwjwRespListener<? extends
                Response> listener) {
        exec(url, isFullApiname, map, listener, null);
    }

    public static void exec(Context mC, boolean isFullApiname, String url, Map map,
                            IwjwRespListener<? extends Response>
                                    listener) {
        if (null != mC) {
            listener.setContext(mC);
            exec(url, isFullApiname, map, listener);
        }
    }

    public static void exec(Fragment mf, boolean isFullApiname, String url, Map map,
                            IwjwRespListener<? extends Response>
                                    listener) {
        if (null != mf) {
            listener.setFragment(mf);
            exec(url, isFullApiname, map, listener);
        }
    }

    /**
     * 发送请求.
     *
     * @param map
     * @param listener
     * @param config   请求定制化.
     */
    public static void exec(String path, boolean isFullApiname, Map map, IwjwRespListener<? extends
            Response> listener,
                            HttpConfig config) {
        if (listener.isDetached()) {
            return;
        }
        if (path == null) throw new IllegalArgumentException("request should be set request path!");
        IwjwJSNativeHttp http = IwjwJSNativeHttp.instance(path);
        if (config != null) http.configRequest(config);
        http.setFullUrl(isFullApiname);
        http.exec(map, listener);
    }
}
