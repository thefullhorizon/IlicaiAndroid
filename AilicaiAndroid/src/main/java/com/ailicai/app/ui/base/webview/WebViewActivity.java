package com.ailicai.app.ui.base.webview;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;


import com.ailicai.app.common.utils.HashMapUtil;
import com.ailicai.app.common.utils.MyIntent;

import java.util.Map;

public class WebViewActivity extends BaseWebViewActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, String> dataMap = MyIntent.getData(getIntent());
        if (null != dataMap) {
            int needRefresh = HashMapUtil.getInt(dataMap, NEED_REFRESH);
            WebSettings settings = getWebSetting();
            if (needRefresh == 0) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else if (needRefresh == 1) {
                settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            }
            boolean isDarkTheme = HashMapUtil.getBoolean(dataMap, TOPVIEWTHEME);
            setTopTheme(isDarkTheme);
            String s = HashMapUtil.getString(dataMap, TITLE);
            if (!TextUtils.isEmpty(s)) {
                setTitle(s);
            }
            String url = HashMapUtil.getString(dataMap, URL);
            if (!TextUtils.isEmpty(url)) {
                loadUrl(url);
            }
            boolean useWebTitle = HashMapUtil.getBoolean(dataMap, USEWEBTITLE);
            if (useWebTitle) {
                setIWebListener(new BaseWebViewLayout.IWebListener() {
                    @Override
                    public void onWebLoadStart(BaseWebViewLayout webViewLayout) {

                    }

                    @Override
                    public void onProgressChanged(int newProgress, BaseWebViewLayout webViewLayout) {

                    }

                    @Override
                    public void onReceivedTitle(String title, BaseWebViewLayout webViewLayout) {
                        if (!TextUtils.isEmpty(title)) {
                            webViewLayout.setTitle(title);
                        }
                    }
                });
            }
        }
    }
}
