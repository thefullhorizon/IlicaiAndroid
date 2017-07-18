package com.ailicai.app.ui.message;

import android.os.Bundle;
import android.text.TextUtils;

import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;

/**
 * Created by duo.chen on 2015/8/6.
 * 消息列表里使用的WebView加载的页面
 */

public class MessageDetailWebViewActivity extends BaseWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUrl(getIntent().getStringExtra(URL));

        setIWebListener(new BaseWebViewLayout.IWebListener() {
            @Override
            public void onWebLoadStart(BaseWebViewLayout webViewLayout) {
            }

            @Override
            public void onProgressChanged(int newProgress, BaseWebViewLayout webViewLayout) {

            }

            @Override
            public void onReceivedTitle(String title, BaseWebViewLayout webViewLayout) {
                if (!TextUtils.isEmpty(title))
                    webViewLayout.setTitle(title);
            }
        });
    }

}
