package com.ailicai.app.ui.base.webview;

/**
 * Created by duo.chen on 2016/1/14.
 */
public abstract class WebSchemeFilter {

    private String[] actions;

    public WebSchemeFilter(String... actions) {
        this.actions = actions;
    }

    public abstract boolean action(String url);

    protected boolean filter(String url){
        for (String action : actions) {
            if (url.startsWith(action)) {
                return true;
            }
        }
        return false;
    }
}
