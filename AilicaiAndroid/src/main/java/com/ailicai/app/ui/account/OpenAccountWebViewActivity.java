package com.ailicai.app.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;

import com.ailicai.app.common.utils.HashMapUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.eventbus.OpenAccountFinishEvent;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebJumpUiAction;
import com.ailicai.app.ui.base.webview.WebMethodCallAction;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.paypassword.PayPwdResetActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class OpenAccountWebViewActivity extends BaseWebViewActivity {

    private static final int RC_TO_SCAN_PAGE = 1;
    private static final int RC_TO_DATA_BACK = 2;

    // 扫完卡，结果页点下一步，需要打开的url，这个url是去填写银行信息的
    private String cardBinNextUrl = "";

    public static void goToOpenAccount(Context context) {
        Map<String, String> dataMap = ObjectUtil.newHashMap();
//        dataMap.put(BaseWebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getOpenAccountUrl());
        dataMap.put(BaseWebViewActivity.URL, "http://192.168.1.44:2323/account/password-all");
//        dataMap.put(BaseWebViewActivity.URL, "http://10.7.249.203:6088/account/password-all");
        dataMap.put(BaseWebViewActivity.USEWEBTITLE, "true");
        dataMap.put(BaseWebViewActivity.TOPVIEWTHEME, "false");
        MyIntent.startActivity(context, OpenAccountWebViewActivity.class, dataMap);
    }

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

            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void addAction() {
        addJumpUiActions(new WebJumpUiAction("scancard") {
            @Override
            public void jumpUi(HashMap<String, String> params) {

                if(params.containsKey("url")) {
                    cardBinNextUrl = params.get("url");
                    Intent intent = new Intent(OpenAccountWebViewActivity.this, BankCardScanActivity.class);
                    startActivityForResult(intent, RC_TO_SCAN_PAGE);
                }
            }
        });

        addMethodCallAction(new WebMethodCallAction("openaccountfinish") {
            @Override
            public Boolean call(HashMap params) {
                EventBus.getDefault().post(new OpenAccountFinishEvent());
                LoginManager.updateUserInfoData();
                return false;
            }
        });

        addJumpUiActions(new WebJumpUiAction("forgotpassword") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                Intent intent = new Intent(OpenAccountWebViewActivity.this, PayPwdResetActivity.class);
                startActivityForResult(intent, 22);
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
                extras.putString("url",cardBinNextUrl);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleOpenAccountFinshEvent(OpenAccountFinishEvent finishEvent) {
        finish();
    }
}
