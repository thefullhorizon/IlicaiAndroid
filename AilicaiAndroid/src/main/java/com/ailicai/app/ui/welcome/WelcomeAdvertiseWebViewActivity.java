package com.ailicai.app.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.HashMapUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.index.IndexActivity;
import com.ailicai.app.widget.IWTopTitleView;

import java.util.Map;

public class WelcomeAdvertiseWebViewActivity extends WebViewActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTopBackListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toHomePage();
    }

    public void toHomePage() {
        startActivity(new Intent(this,IndexActivity.class));
    }

    private void setTopBackListener() {
        IWTopTitleView topTitleView = (IWTopTitleView) findViewById(R.id.webview_title);
        topTitleView.setTitleOnClickListener(new IWTopTitleView.TopTitleOnClickListener() {
            @Override
            public boolean onBackClick() {
                toHomePage();
                return false;
            }
        });
    }
}
