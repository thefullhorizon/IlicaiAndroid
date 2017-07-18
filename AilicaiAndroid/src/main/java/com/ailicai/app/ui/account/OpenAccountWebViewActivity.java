package com.ailicai.app.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;


import com.ailicai.app.common.utils.HashMapUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebJumpUiAction;

import java.util.HashMap;
import java.util.Map;

public class OpenAccountWebViewActivity extends BaseWebViewActivity {

    private static final int RC_TO_SCAN_PAGE = 1;
    private static final int RC_TO_DATA_BACK = 2;

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

            addAction();
        }
    }

    private void addAction() {
        addJumpUiActions(new WebJumpUiAction("scancard") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                Intent intent = new Intent(OpenAccountWebViewActivity.this, BankCardScanActivity.class);
                startActivityForResult(intent, RC_TO_SCAN_PAGE);
            }
        });
    }

    private void callJSToTellCardNumber(String cardNumber) {
        loadJs("javascript:callJs('getcardno'," + cardNumber + ")");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) return;

        if(requestCode == RC_TO_SCAN_PAGE) {
            Intent intent = new Intent(this, BankCardResultActivity.class);
            if (data != null) {
                Bundle extras = data.getExtras();
                intent.putExtras(extras);
            }
            startActivityForResult(intent, RC_TO_DATA_BACK);
        } else if(requestCode == RC_TO_DATA_BACK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                callJSToTellCardNumber(extras.getString("bankCardNumber"));
            }
        }
    }
}
