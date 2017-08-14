package com.ailicai.app.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.eventbus.FinancePayEvent;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.MoneyChangeEvent;
import com.ailicai.app.eventbus.RegularPayEvent;
import com.ailicai.app.eventbus.RegularPayH5ActivityFinishEvent;
import com.ailicai.app.model.request.TiyanbaoDetailRequest;
import com.ailicai.app.model.response.BuyTiyanbaoInitResponse;
import com.ailicai.app.ui.asset.FinanceUpgradePresenter;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebJumpUiAction;
import com.ailicai.app.ui.base.webview.WebMethodCallAction;
import com.ailicai.app.ui.buy.NoSetSafeCardHint;
import com.ailicai.app.ui.buy.ProcessActivity;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;


/**
 * 房产宝详情页H5页面
 * Created by liyanan on 16/7/11.
 */
public class RegularFinanceDetailH5Activity extends BaseWebViewActivity {
    public static final String EXTRA_URL = "url";
    private static final int REQUEST_FOR_PROCESS_BUY = 10001;//处理购买
    private String prodId;
    private String isTransfer;//是否是转让房产宝  1 是   0否
    private String activityId;
    private String type = "";
    private boolean isSmallCoin = false;//绑卡处理逻辑返回是需要

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
        EventBus.getDefault().register(this);
    }

    private void addMethodCallAction() {
        //立即购买
        addMethodCallAction(new WebMethodCallAction("fcbdetailbuy") {
            @Override
            public Boolean call(HashMap params) {
                if (params.containsKey("type")) {
                    type = (String) params.get("type");
                }
                if ("1".equals(type)) {
                    //体验宝
                    buyTiYanBao(params);
                } else if ("0".equals(type)) {
                    //之前版本的逻辑(普通和转让)
                    buyRegularFinance(params);
                } else if ("2".equals(type)) {
                    //小钱袋
                    isSmallCoin = true;
                    //走跟房产宝相同的逻辑
                    buyRegularFinance(params);
                }
                return false;
            }
        });

        /**
         * 再次打开房产宝详情
         */
        addJumpUiActions(new WebJumpUiAction("regulardetail") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                if (params.containsKey("url")) {
                    String url = params.get("url");
                    Intent intent = new Intent(RegularFinanceDetailH5Activity.this, RegularFinanceDetailH5Activity.class);
                    intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, url);
                    startActivity(intent);
                }
            }
        });
    }

    private void buyTiYanBao(HashMap params) {
        activityId = (String) params.get("prodId");
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            //去登录
            LoginManager.goLogin(this, LoginManager.LOGIN_FROM_AILICAI);
        } else {
            //获取是否有体验金
            getTiYanBaoData(Long.parseLong(activityId));
        }
    }

    /**
     * 获取体验金数据
     *
     * @param activityId
     */
    public void getTiYanBaoData(final long activityId) {
        TiyanbaoDetailRequest request = new TiyanbaoDetailRequest();
        request.setActiviId(activityId);
        ServiceSender.exec(this, request, new IwjwRespListener<BuyTiyanbaoInitResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(BuyTiyanbaoInitResponse jsonObject) {
                showContentView();
                if (jsonObject != null && jsonObject.getHasCoupon() == 1) {
                    //有体验金
                    Intent intent = new Intent(RegularFinanceDetailH5Activity.this, BuyTiYanBaoActivity.class);
                    intent.putExtra(BuyTiYanBaoActivity.EXTRA_KEY, jsonObject);
                    startActivity(intent);
                } else {
                    //无体验金
                    callJsRefresh();
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                showContentView();
                showMyToast(errorInfo);
            }
        });
    }

    /**
     * 购买房产宝(普通和转让)
     *
     * @param params
     */
    private void buyRegularFinance(HashMap params) {
        prodId = (String) params.get("prodId");
        if (params.containsKey("isTransfer")) {
            isTransfer = (String) params.get("isTransfer");
        }
        LogUtil.e("lyn", "prodId=" + prodId);
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            //去登录页面
            LoginManager.goLogin(this, LoginManager.LOGIN_FROM_AILICAI);
        } else {
            //进入统一处理页面
            if (!NoSetSafeCardHint.isShowHintDialog(RegularFinanceDetailH5Activity.this)) {
                Intent intent = new Intent(RegularFinanceDetailH5Activity.this, ProcessActivity.class);
                if (params.containsKey("openAccountUrl")) {
                    String openAccountUrl = (String) params.get("openAccountUrl");
                    intent.putExtra("openAccountUrl", openAccountUrl);
                }
                startActivityForResult(intent, REQUEST_FOR_PROCESS_BUY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_FOR_PROCESS_BUY:
                //统一处理
                callJsRefresh();
                if (resultCode == RESULT_OK) {
                    if (isSmallCoin) {
                        Intent smallCoinIntent = new Intent(RegularFinanceDetailH5Activity.this, RegularPayActivity.class);
                        smallCoinIntent.putExtra(RegularPayActivity.PRODUCT_ID_KEY, prodId);
                        smallCoinIntent.putExtra(RegularPayActivity.IS_FROM_SMALL_COIN, true);
                        startActivityForResult(smallCoinIntent, REQUEST_FOR_PROCESS_BUY);
                    } else {
                        if ("1".equals(isTransfer)) {
                            //是转让的房产宝
                            MyIntent.startActivity(RegularFinanceDetailH5Activity.this, BuyTransferPayActivity.class, prodId);
                        } else {
                            //非转让的房产宝
                            MyIntent.startActivity(RegularFinanceDetailH5Activity.this, RegularPayActivity.class, prodId);
                        }
                    }

                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent loginEvent) {
        if (loginEvent.isCancelLogin()) {
            return;
        }
        if (loginEvent.isLoginSuccess() && "1".equals(type)) {
            //获取是否有体验金
            if (loginEvent.isContinueNext()) {
                getTiYanBaoData(Long.parseLong(activityId));
            }
        } else {
            if (loginEvent.isLoginSuccess()) {
                //进入统一处理页面
                if (!NoSetSafeCardHint.isShowHintDialogWithLoginInfo(RegularFinanceDetailH5Activity.this, loginEvent.getJsonObject())) {
                    if (loginEvent.isContinueNext()) {
                        Intent intent = new Intent(RegularFinanceDetailH5Activity.this, ProcessActivity.class);
                        startActivityForResult(intent, REQUEST_FOR_PROCESS_BUY);
                    }
                }
            }
        }


        if (loginEvent.isLoginSuccess()) {
            // 在当前页面登陆成功上报的埋点
            EventLog.upEventLog("2017050801", "y", "alc_detail");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRegularPayEvent(RegularPayEvent regularPayEvent) {
        callJsRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFinancePayEvent(FinancePayEvent payEvent) {
        callJsRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMoneyChangeEvent(MoneyChangeEvent payEvent) {
        callJsRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRegularPayH5ActivityFinishEvent(RegularPayH5ActivityFinishEvent event) {
        finish();
    }

    private void startOrStopAutoRefresh(boolean should) {
        loadJs("javascript:callJs('setautorefreshstate'," + should + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startOrStopAutoRefresh(true);
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
}
