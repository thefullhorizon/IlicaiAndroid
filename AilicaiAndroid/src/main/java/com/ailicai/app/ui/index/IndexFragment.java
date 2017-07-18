package com.ailicai.app.ui.index;

import android.os.Bundle;

import com.ailicai.app.ui.base.webview.BaseWebViewFragment;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;

/**
 * name: IndexFragment <BR>
 * description: 首页 <BR>
 * create date: 2017/7/12
 *
 * @author: IWJW Zhou Xuan
 */
public class IndexFragment extends BaseWebViewFragment{


    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        setNoTitle();
        setLoadingStyle(BaseWebViewLayout.LoadingStyle.WHEEL);
        shouldShowLoading(true);

        setIWebListener(new BaseWebViewLayout.IWebListener() {
            @Override
            public void onWebLoadStart(BaseWebViewLayout webViewLayout) {

            }

            @Override
            public void onProgressChanged(int newProgress, BaseWebViewLayout webViewLayout) {
                if (newProgress > 50) {
                    webViewLayout.shouldShowLoading(false);
                }
            }

            @Override
            public void onReceivedTitle(String title, BaseWebViewLayout webViewLayout) {
            }
        });

//        loadUrl("http://mtest.iwlicai.com/alcapp/licai");
//        loadUrl("http://10.7.249.203:6088/licai/list ");
        loadUrl("http://192.168.1.44:2323/licai");
    }

    private void startOrStopAutoRefresh(boolean should) {
        loadJs("javascript:callJs('setautorefreshstate'," + should + ")");
    }

    private void addAction() {
    }
}
