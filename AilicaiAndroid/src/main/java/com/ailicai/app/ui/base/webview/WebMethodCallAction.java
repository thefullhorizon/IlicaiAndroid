package com.ailicai.app.ui.base.webview;

import java.util.HashMap;

/**
 * Created by duo.chen on 2016/4/1.
 */
public abstract class WebMethodCallAction<T> {

    private String method;

    public WebMethodCallAction(String method) {
        this.method = method;
    }

    public abstract T call(HashMap params);

    public boolean match(String url){
        return url.equals(method);
    }
}
