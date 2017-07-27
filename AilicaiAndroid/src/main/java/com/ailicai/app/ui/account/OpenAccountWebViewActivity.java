package com.ailicai.app.ui.account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.WindowManager;
import android.webkit.WebSettings;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.HashMapUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.eventbus.OpenAccountFinishEvent;
import com.ailicai.app.ui.bankcard.BankCardListActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebJumpUiAction;
import com.ailicai.app.ui.base.webview.WebMethodCallAction;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.paypassword.PayPwdResetActivity;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.SystemBarTintManager;
import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class OpenAccountWebViewActivity extends BaseWebViewActivity {

    boolean isBackShowQuit = false;

    private static final int RC_TO_SCAN_BANK_CARD = 1;
    private static final int RC_TO_DATA_BACK = 2;
    private static final int RC_TO_SCAN_ID_CARD = 3;

    // 扫完卡，结果页点下一步，需要打开的url，这个url是去填写银行信息的
    private String cardBinNextUrl = "";

    public static void goToOpenAccount(Context context) {
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(BaseWebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getOpenAccountUrl());
//        dataMap.put(BaseWebViewActivity.URL, "http://192.168.1.44:2323/account/password-all");
        dataMap.put(BaseWebViewActivity.USEWEBTITLE, "true");
        dataMap.put(BaseWebViewActivity.TOPVIEWTHEME, "false");
        MyIntent.startActivity(context, OpenAccountWebViewActivity.class, dataMap);
    }

    public static void goToBindNewSafeCard(Context context) {
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(BaseWebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getBindNewOpenAccountUrl());
//        dataMap.put(BaseWebViewActivity.URL, "http://192.168.1.44:2323/account/id-binded");
        dataMap.put(BaseWebViewActivity.USEWEBTITLE, "true");
        dataMap.put(BaseWebViewActivity.TOPVIEWTHEME, "false");
        MyIntent.startActivity(context, OpenAccountWebViewActivity.class, dataMap);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setShowSystemBarTint(false);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.white);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);

        rootView.setFitsSystemWindows(true);
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
            setTopBackListener();
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

                if (params.containsKey("url")) {
                    cardBinNextUrl = params.get("url");
                    Intent intent = new Intent(OpenAccountWebViewActivity.this, BankCardScanActivity.class);
                    startActivityForResult(intent, RC_TO_SCAN_BANK_CARD);
                }
            }
        });

        addJumpUiActions(new WebJumpUiAction("scanid") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                Intent intent = new Intent(OpenAccountWebViewActivity.this, IDCardScanActivity.class);
                startActivityForResult(intent, RC_TO_SCAN_ID_CARD);
            }
        });

        addMethodCallAction(new WebMethodCallAction("openaccountfinish") {
            @Override
            public Boolean call(HashMap params) {
                EventBus.getDefault().post(new OpenAccountFinishEvent());
                LoginManager.updateUserInfoData();
                BankCardListActivity.NEED_MANUAL_REFRESH_LIST = true;
                return false;
            }
        });

        addMethodCallAction(new WebMethodCallAction("isbackshowquit") {
            @Override
            public Boolean call(HashMap params) {
                try {
                    isBackShowQuit = Boolean.parseBoolean(String.valueOf(params.get("isbackshowquit"))) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void callJSToTellIdCardInfo(String name, String idNo) {
        HashMap map = new HashMap();
        map.put("name", name);
        map.put("idNo",idNo);
        loadJs("javascript:callJs('getidentity'," + JSON.toJSONString(map)  + ")");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == RC_TO_SCAN_BANK_CARD) {
            Intent intent = new Intent(this, BankCardResultActivity.class);
            if (data != null) {
                Bundle extras = data.getExtras();
                extras.putString("url", cardBinNextUrl);
                intent.putExtras(extras);
            }
            startActivityForResult(intent, RC_TO_DATA_BACK);
        } else if (requestCode == RC_TO_DATA_BACK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                callJSToTellCardNumber(extras.getString("bankCardNumber"));
            }
        } else if (requestCode == RC_TO_SCAN_ID_CARD) {
            if (data != null) {
                Bundle extras = data.getExtras();
                String name = extras.getString("cardName");
                String idCardNo = extras.getString("idCardNumber");
                callJSToTellIdCardInfo(name, idCardNo);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleOpenAccountFinshEvent(OpenAccountFinishEvent finishEvent) {
        finish();
    }

    @Override
    public void onBackPressed() {
        if(isBackShowQuit) {
            showCancelText();
        } else {
            super.onBackPressed();
        }
    }

    private void setTopBackListener() {
        IWTopTitleView topTitleView = (IWTopTitleView) findViewById(R.id.webview_title);
        topTitleView.setTitleOnClickListener(new IWTopTitleView.TopTitleOnClickListener() {
            @Override
            public boolean onBackClick() {
                if(isBackShowQuit) {
                    showCancelText();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void showCancelText() {

        // 刷新开户状态
        if (UserInfo.isLogin()) {
            LoginManager.updateUserInfoData();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatDialog);
        builder.setMessage("是否放弃开户");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AccountInfo.isRealNameVerify()) {
                    EventLog.upEventLog("683", "abandon", "setcard");
                } else {
                    EventLog.upEventLog("682", "abandon", "setcard");
                }
                finish();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AccountInfo.isRealNameVerify()) {
                    EventLog.upEventLog("683", "unabandon", "setcard");
                } else {
                    EventLog.upEventLog("682", "unabandon", "setcard");
                }
            }
        });
        builder.create().show();
    }
}
