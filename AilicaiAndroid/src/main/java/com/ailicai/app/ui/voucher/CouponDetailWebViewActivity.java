package com.ailicai.app.ui.voucher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.WebViewRefreshEvent;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebJumpUiAction;
import com.ailicai.app.ui.base.webview.WebMethodCallAction;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.view.MyWalletActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

/**
 * 卡券详情页  和申请退款页
 * Created by wulianghuan on 2017/02/10.
 */

public class CouponDetailWebViewActivity extends BaseWebViewActivity {
    public static final String PARAM_DETAIL_URL = "PARAM_DETAIL_URL";
    public static final int APPLY_FOR_BACK_PROFIT_REQUEST_CODE = 168;
    public static int BUYREBATEVOUCHER_REQCODE = 169;

    /**
     * 跳转到红包列表
     *
     * @param context
     * @return
     */
    public static boolean goCoupon(Activity context) {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            LoginManager.goLogin(context, LoginManager.LOGIN_FROM_SMS_VOUCHER);
            return false;
        } else {
            Intent detail = new Intent(context, CouponDetailWebViewActivity.class);
            context.startActivity(detail);
            return true;
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);
        final String detailUrl = getIntent().getStringExtra(PARAM_DETAIL_URL);
        loadUrl(detailUrl);


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

        addJumpUiActions(new WebJumpUiAction("wallet") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                //我的钱包
                MyWalletActivity.goMywallet(CouponDetailWebViewActivity.this);
            }
        });

//        addJumpUiActions(new WebJumpUiAction("applyrebates") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                // 申请返利
//                String couponId = params.get("voucherid");
//                if (!TextUtils.isEmpty(couponId)) {
//                    Intent intent = new Intent(CouponDetailWebViewActivity.this, FlatApplyForBackProfitActivity.class);
//                    intent.putExtra("couponId", couponId);
//                    intent.putExtra("couponDetailUrl", detailUrl);
//                    startActivityForResult(intent, APPLY_FOR_BACK_PROFIT_REQUEST_CODE);
//                }
//            }
//        });


//        addJumpUiActions(new WebJumpUiAction("pay") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                // 立即购买
//                String ordertype = params.get("ordertype");
//                String billtitle = params.get("billtitle");//返利券标题
//                String billfee = params.get("billfee");//返利券金额"
//                if (!TextUtils.isEmpty(ordertype)) {
//                    Intent intent = new Intent(CouponDetailWebViewActivity.this, BuyRebateVoucherActivity.class);
//                    intent.putExtra(BuyRebateVoucherActivity.PARAM_PAY_TITLE, billtitle);
//                    intent.putExtra(BuyRebateVoucherActivity.PARAM_PAY_AMOUNT, billfee);
//                    startActivityForResult(intent, BUYREBATEVOUCHER_REQCODE);
//                }
//            }
//        });

        addJumpUiActions(new WebJumpUiAction("refund") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                String url = String.valueOf(params.get("url"));
                if (!TextUtils.isEmpty(url)) {
                    // 跳转到退款页
                    loadUrl(url);
                }
            }
        });


        addJumpUiActions(new WebJumpUiAction("refund") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                String url = String.valueOf(params.get("url"));
                if (!TextUtils.isEmpty(url)) {
                    // 跳转到退款页
                    loadUrl(url);
                }
            }
        });

        // 退款成功-->发送刷新事件
        addMethodCallAction(new WebMethodCallAction("refundsuccess") {
            @Override
            public Boolean call(HashMap params) {
                setResult(RESULT_OK);

                // 内有pageNameAlias 扩展用
                WebViewRefreshEvent event = new WebViewRefreshEvent();
                EventBus.getDefault().post(event);
                return false;
            }
        });

        // 发送刷新事件
        addMethodCallAction(new WebMethodCallAction("refreshrequest") {
            @Override
            public Boolean call(HashMap params) {
                setResult(RESULT_OK);

                // 内有pageNameAlias 扩展用
                WebViewRefreshEvent event = new WebViewRefreshEvent();
                EventBus.getDefault().post(event);

                return false;
            }
        });
    }

    @Override
    public void logout(Activity activity, String msg) {
        super.logout(activity, msg);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (BUYREBATEVOUCHER_REQCODE == requestCode && resultCode == RESULT_OK) {
            finish();
        }

        if (APPLY_FOR_BACK_PROFIT_REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent event) {
        if (event.isLoginSuccess()) {
            final String detailUrl = getIntent().getStringExtra(PARAM_DETAIL_URL);
            loadUrl(detailUrl);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
