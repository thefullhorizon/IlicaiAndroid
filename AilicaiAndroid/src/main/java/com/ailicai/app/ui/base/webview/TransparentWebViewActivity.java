package com.ailicai.app.ui.base.webview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;


import com.ailicai.app.R;
import com.ailicai.app.widget.IWTopTitleView;

import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by duo.chen on 2015/10/29.
 * 全透明webview activity
 */
@SuppressWarnings("unchecked")
public class TransparentWebViewActivity extends WebViewActivity {

    @Bind(R.id.webview_root)
    LinearLayout webview_root;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        IWTopTitleView topTitleView = (IWTopTitleView) findViewById(R.id.webview_title);
        topTitleView.setVisibility(View.GONE);
        webview_root.setBackgroundColor(getResources().getColor(R.color.transparent));
        getWebViewLayout().setWebviewAlpha(0);

        setIWebListener(new BaseWebViewLayout.IWebListener() {
            @Override
            public void onWebLoadStart(BaseWebViewLayout webViewLayout) {

            }

            @Override
            public void onProgressChanged(int newProgress, BaseWebViewLayout webViewLayout) {
                if(newProgress >= 100) {
                    getWebViewLayout().setWebviewAlpha(255);
                    getWebViewLayout().setWebviewTransparent();
                }
            }

            @Override
            public void onReceivedTitle(String title, BaseWebViewLayout webViewLayout) {
            }
        });

        addAction();
    }

    private void addAction() {
        addMethodCallAction(new WebMethodCallAction("lotterysuccess") {
            @Override
            public Object call(HashMap params) {
                setResult(RESULT_OK);
                return false;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.fade_out);
    }
}
