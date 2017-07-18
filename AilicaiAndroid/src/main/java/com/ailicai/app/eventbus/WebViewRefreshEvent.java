package com.ailicai.app.eventbus;

/**
 * name: WebViewRefreshEvent <BR>
 * description: 通知webview刷新 <BR>
 * create date: 2017/2/16
 *
 * @author: IWJW Zhou Xuan
 */
public class WebViewRefreshEvent {

    private String pageNameAlias;// 页面名称别名

    public String getPageNameAlias() {
        return pageNameAlias;
    }

    public void setPageNameAlias(String pageNameAlias) {
        this.pageNameAlias = pageNameAlias;
    }
}
