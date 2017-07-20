package com.ailicai.app.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebMethodCallAction;

import java.util.HashMap;


/**
 * 选择体验金卡券列表
 * Created by liyanan on 16/8/17.
 */
public class TiYanBaoCouponH5Activity extends BaseWebViewActivity {
    public static final String EXTRA_URL = "url";
    private boolean noData;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTopTheme(false);
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
                if (!TextUtils.isEmpty(title)) {
                    webViewLayout.setTitle(title);
                }
            }
        });
        addMethodCallAction();
        loadUrl(getIntent().getStringExtra(EXTRA_URL));
    }

    private void addMethodCallAction() {
        addMethodCallAction(new WebMethodCallAction("voucherselect") {
            @Override
            public Boolean call(HashMap params) {
                double amount = Double.parseDouble((String) params.get("amountCent")) / 100;
                int voucherId = Integer.parseInt((String) params.get("voucherId"));
                int expiredDays = Integer.parseInt((String) params.get("expiredDays"));
                Intent intent = new Intent();
                intent.putExtra("couponId", voucherId);
                intent.putExtra("couponAmount", amount);
                intent.putExtra("expiredDays", expiredDays);
                setResult(RESULT_OK, intent);
                finish();
                return false;
            }
        });
        addMethodCallAction(new WebMethodCallAction("vouchernodata") {
            @Override
            public Boolean call(HashMap params) {
                noData = true;
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (noData) {
            Intent intent = new Intent();
            intent.putExtra("noData", noData);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }
}
