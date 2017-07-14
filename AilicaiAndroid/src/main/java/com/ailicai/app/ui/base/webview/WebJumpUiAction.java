package com.ailicai.app.ui.base.webview;

import java.util.HashMap;

/**
 * Created by duo.chen on 2016/3/30.
 */
public abstract class WebJumpUiAction {
    private String[] pages;

    public WebJumpUiAction(String... pages) {
        this.pages = pages;
    }

    public abstract void jumpUi(HashMap<String, String> params);

    public boolean match(String pageName) {
        for (String page : pages) {
            if (page.equals(pageName)) {
                return true;
            }
        }
        return false;
    }
}
