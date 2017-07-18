package com.ailicai.app.ui.view.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.MoneyChangeEvent;
import com.ailicai.app.ui.asset.CapitalListProductDetailActivity;
import com.ailicai.app.ui.asset.FinanceUpgradePresenter;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebJumpUiAction;
import com.ailicai.app.ui.base.webview.WebMethodCallAction;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.buy.ProcessActivity;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.view.CapitalActivity;
import com.ailicai.app.ui.view.RegularFinanceDetailH5Activity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;


public class FinanceHomeActivity extends BaseWebViewActivity {

    private final static int REQUEST_CODE_OPENACCOUNT = 10001;
    private final static int CLICK_STATUE_OPEN_ACCOUNT = 1;
    private int clickState = -1;

    // 浏览器打开房产宝详情和体验宝详情，shceme传过来的链接
    String urlFromBroswer = "";

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        getDataFromIntentOrSavedInstance(savedInstanceState);
        checkIfBroswerToRegularOrTiyanBao();

        setTopTheme(true);
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

        addBaseActions();

        EventBus.getDefault().register(this);

        loadUrl(/*"https://pctest.iwjw.com/licai/index"*/SupportUrl.getAilicaiUrl());
        //TODO nanshan
//        OpenScreenFragmentDialog.showByPosition(this,OpenScreenPopModel.POS_LICAI);
    }

    private void getDataFromIntentOrSavedInstance(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            urlFromBroswer = getIntent().getStringExtra(RegularFinanceDetailH5Activity.EXTRA_URL);
        } else {
            urlFromBroswer = savedInstanceState.getString(RegularFinanceDetailH5Activity.EXTRA_URL);
        }
    }

    // 检测是否从浏览器直接打开房产宝或者体验宝详情
    private void checkIfBroswerToRegularOrTiyanBao() {
        if(!TextUtils.isEmpty(urlFromBroswer)) {
            Intent intent = new Intent(FinanceHomeActivity.this, RegularFinanceDetailH5Activity.class);
            intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, urlFromBroswer);
            startActivity(intent);
        }
    }

    private void startOrStopAutoRefresh(boolean should) {
        loadJs("javascript:callJs('setautorefreshstate'," + should + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startOrStopAutoRefresh(true);

        // 请求是否弹框提醒升级协议 是否维护中
        FinanceUpgradePresenter presenter = new FinanceUpgradePresenter();
        presenter.httpForProtocalUpgradeState(this);
        presenter.httpForSystemIsFix(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        startOrStopAutoRefresh(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void addBaseActions() {
        addJumpUiActions(new WebJumpUiAction("holdtiyanbao") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                String couponId = params.get("couponId");
                Intent intent = new Intent(FinanceHomeActivity.this, CapitalListProductDetailActivity.class);
                intent.putExtra(CapitalListProductDetailActivity.IS_TIYANBAO, true);
                intent.putExtra(CapitalListProductDetailActivity.TIYANBAO_ID, Long.parseLong(couponId));
                intent.putExtra(CapitalListProductDetailActivity.TYPE, 2);
                startActivity(intent);
            }
        });
        addJumpUiActions(new WebJumpUiAction("reserve") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                goReserve();
            }
        });

        addJumpUiActions(new WebJumpUiAction("regulardetail") {
            @Override
            public void jumpUi(HashMap<String, String> params) {

                if (params.containsKey("url")) {
                    String url = params.get("url");
                    Intent intent = new Intent(FinanceHomeActivity.this, RegularFinanceDetailH5Activity.class);
                    intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, url);
                    startActivity(intent);
                }
            }
        });

        addJumpUiActions(new WebJumpUiAction("noticedetail") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                if (params.containsKey("url")) {
                    String url = params.get("url");
                    Map<String, String> dataMap = ObjectUtil.newHashMap();
                    dataMap.put(WebViewActivity.URL, url);
                    dataMap.put(WebViewActivity.USEWEBTITLE, "true");
                    dataMap.put(WebViewActivity.TOPVIEWTHEME, "true");
                    MyIntent.startActivity(FinanceHomeActivity.this, WebViewActivity.class, dataMap);
                }
            }
        });

        addJumpUiActions(new WebJumpUiAction("capital") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                //资产页面
                Intent intent = new Intent(FinanceHomeActivity.this, CapitalActivity.class);
                startActivity(intent);
            }
        });

        addJumpUiActions(new WebJumpUiAction("wallet") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                //TODO nanshan
                //我的钱包
//                MyWalletActivity.goMywallet(FinanceHomeActivity.this);
            }
        });


        addJumpUiActions(new WebJumpUiAction("myproperty") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                //我的资产
                //TODO nanshan
//                Intent intent = new Intent(FinanceHomeActivity.this, MyAssetsActivity.class);
//                startActivity(intent);
            }
        });

        addMethodCallAction(new WebMethodCallAction("openaccount") {
            @Override
            public Boolean call(HashMap params) {
                openAccount();
                return false;
            }
        });

        addMethodCallAction(new WebMethodCallAction("systemupgradealert") {
            @Override
            public Object call(HashMap params) {
                // 请求是否是否维护中
                FinanceUpgradePresenter presenter = new FinanceUpgradePresenter();
                presenter.httpForSystemIsFix(FinanceHomeActivity.this);
                return false;
            }
        });

        addMethodCallAction(new WebMethodCallAction("protcolupgradealert") {
            @Override
            public Object call(HashMap params) {
                // 请求是否弹框提醒升级协议
                FinanceUpgradePresenter presenter = new FinanceUpgradePresenter();
                presenter.httpForProtocalUpgradeState(FinanceHomeActivity.this);
                return false;
            }
        });
    }

    private void goReserve() {
        //TODO nanshan
//        Intent intent = new Intent(this, ReserveActivity.class);
//        startActivity(intent);
    }

    private void openAccount() {
        clickState = CLICK_STATUE_OPEN_ACCOUNT;
        EventLog.upEventLog("201", "purchase_homepage");
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            //未登录
            LoginManager.goLogin(this, LoginManager.LOGIN_FROM_ENTRUST);
        } else {
            //已登录,去开户
            Intent intent = new Intent(this, ProcessActivity.class);
            startActivityForResult(intent, REQUEST_CODE_OPENACCOUNT);
            //TODO nanshan
//            OpenAccountFeature.isOpeningAccount = true;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMoneyChangeEvent(MoneyChangeEvent payEvent) {
        callJsRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent loginEvent) {
        if (loginEvent.isLoginSuccess()) {
            if (clickState == CLICK_STATUE_OPEN_ACCOUNT) {
                //去开户
                Intent intent = new Intent(this, ProcessActivity.class);
                startActivityForResult(intent, REQUEST_CODE_OPENACCOUNT);
                //TODO nanshan
//                OpenAccountFeature.isOpeningAccount = true;
            }
        }
        callJsRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_OPENACCOUNT:
                    //TODO nanshan
//                    OpenAccountFeature.isOpeningAccount = false;
                    callJsRefresh();
                    break;
            }
        }
    }

    @Override
    public void logout(Activity activity, String msg) {
        super.logout(activity, msg);
        finish();
    }
}
