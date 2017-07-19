package com.ailicai.app.ui.index;

import android.content.Intent;
import android.os.Bundle;

import com.ailicai.app.ui.base.webview.BaseWebViewFragment;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebJumpUiAction;
import com.ailicai.app.ui.view.RegularFinanceDetailH5Activity;

import java.util.HashMap;

/**
 * name: IndexFragment <BR>
 * description: 首页 <BR>
 * create date: 2017/7/12
 *
 * @author: IWJW Zhou Xuan
 */
public class IndexFragment extends BaseWebViewFragment {


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
        addAction();
        loadUrl("http://192.168.1.44:2323/licai");
    }

    private void startOrStopAutoRefresh(boolean should) {
        loadJs("javascript:callJs('setautorefreshstate'," + should + ")");
    }

    private void addAction() {
        addJumpUiActions(new WebJumpUiAction("regulardetail") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                if (params.containsKey("url")) {
                    String url = params.get("url");
                    Intent intent = new Intent(getActivity(), RegularFinanceDetailH5Activity.class);
                    intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, url);
                    startActivity(intent);
                }
            }
        });
    }
}
