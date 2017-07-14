package com.ailicai.app.ui.base.webview;

/**
 * Created by duo.chen on 2016/8/17
 */

public abstract class WebPathAction {

    private String path;

    public WebPathAction(String path) {
        this.path = path;
    }

    public abstract void action(String url);

    public boolean match(String path){
        return this.path.equals(path);
    }

}
