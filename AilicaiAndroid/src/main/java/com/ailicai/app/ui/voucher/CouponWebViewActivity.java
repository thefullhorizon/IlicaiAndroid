package com.ailicai.app.ui.voucher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.eventbus.WebViewRefreshEvent;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebJumpUiAction;
import com.ailicai.app.ui.base.webview.WebMethodCallAction;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.view.MyWalletActivity;
import com.huoqiu.framework.util.CheckDoubleClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by duo.chen on 2016/5/20.
 */

public class CouponWebViewActivity extends BaseWebViewActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        loadUrl(SupportUrl.getSupportUrlsResponse().getCardUrl());


        addMethodCallAction(new WebMethodCallAction("setnavigation") {
            @Override
            public Boolean call(HashMap params) {

                try {
                    String showleft = String.valueOf(params.get("showleft"));
                    String showright = String.valueOf(params.get("showright"));
                    String rightTxt = String.valueOf(params.get("righttxt"));
                    final String rightUrl = String.valueOf(params.get("righturl"));
                    String righticon = String.valueOf(params.get("righticon"));

                    String toShowRightStr = "";
                    if("1".equals(righticon)) {
                        toShowRightStr = getResources().getString(R.string.information);
                    } else {
                        toShowRightStr = rightTxt;
                    }

                    if (!TextUtils.isEmpty(toShowRightStr)) {
                        addTitleRightText(toShowRightStr, R.style.text_14_54000000, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!CheckDoubleClick.isFastDoubleClick()) {

                                    if (TextUtils.isEmpty(rightUrl)) {
                                        //为空代表是完成按钮
                                        loadJs("javascript:callJs('cardFinish',{})");
                                    } else {
                                        Map<String, String> dataMap = ObjectUtil.newHashMap();
                                        dataMap.put(WebViewActivity.URL, rightUrl);
                                        dataMap.put(WebViewActivity.NEED_REFRESH, "0");
                                        dataMap.put(WebViewActivity.USEWEBTITLE, "true");
                                        MyIntent.startActivity(CouponWebViewActivity.this,
                                                WebViewActivity.class, dataMap);
                                    }
                                }
                            }
                        });
                    }

                    showBack("yes".equals(showleft));
                    showRightTextIcon("yes".equals(showright));

                } catch (Exception ignored) {

                }
                return false;
            }
        });

        addMethodCallAction(new WebMethodCallAction("setnavigation") {
            @Override
            public Boolean call(HashMap params) {
                try {
                    String page = String.valueOf(params.get("page"));
                    String url = String.valueOf(params.get("url"));
                    if (!TextUtils.isEmpty(url)) {
                        // 跳转卡券详情页
                        Intent intent = new Intent(CouponWebViewActivity.this, CouponDetailWebViewActivity.class);
                        intent.putExtra(CouponDetailWebViewActivity.PARAM_DETAIL_URL, url);
                        startActivity(intent);
                    }
                } catch (Exception ignored) {

                }
                return false;
            }
        });

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
                MyWalletActivity.goMywallet(CouponWebViewActivity.this);
            }
        });

        addJumpUiActions(new WebJumpUiAction("rebatesdetail") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                String url = String.valueOf(params.get("url"));
                if (!TextUtils.isEmpty(url)) {
                    // 跳转卡券详情页
                    Intent intent = new Intent(CouponWebViewActivity.this, CouponDetailWebViewActivity.class);
                    intent.putExtra(CouponDetailWebViewActivity.PARAM_DETAIL_URL, url);
                    startActivity(intent);
                }
            }
        });

//        addJumpUiActions(new WebJumpUiAction("applyrebates") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                String voucherid = params.get("voucherid");
//                String refundurl = params.get("refundurl");
//                String voucherurl = params.get("voucherurl");
//                if (!TextUtils.isEmpty(voucherid)) {
//                    // 申请返利 (审核中, 审核失败)
//                    Intent intent = new Intent(CouponWebViewActivity.this, FlatApplyForBackProfitActivity.class);
//                    intent.putExtra("couponId", voucherid);
//                    intent.putExtra("refundUrl", refundurl);
//                    intent.putExtra("couponDetailUrl",voucherurl);
//
//                    startActivity(intent);
//                }
//            }
//        });

        addJumpUiActions(new WebJumpUiAction("refund") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                String url = String.valueOf(params.get("url"));
                if (!TextUtils.isEmpty(url)) {
                    // 跳转卡券详情页
                    Intent intent = new Intent(CouponWebViewActivity.this, CouponDetailWebViewActivity.class);
                    intent.putExtra(CouponDetailWebViewActivity.PARAM_DETAIL_URL, url);
                    startActivity(intent);
                }
            }
        });
//        addJumpUiActions(new WebJumpUiAction("myproperty") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                String url = String.valueOf(params.get("url"));
//                if (!TextUtils.isEmpty(url)) {
//                    // 跳转我的资产页面
//                    Intent intent = new Intent(CouponWebViewActivity.this, MyAssetsActivity.class);
//                    intent.putExtra(CouponDetailWebViewActivity.PARAM_DETAIL_URL, url);
//                    startActivity(intent);
//                }
//            }
//        });

//        addJumpUiActions(new WebJumpUiAction("rebatesuccess") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                String voucherfee = String.valueOf(params.get("voucherfee"));
//                if (!TextUtils.isEmpty(voucherfee)) {
//                    // 跳转卡券详情页
//                    Intent intent = new Intent(CouponWebViewActivity.this, FlatBackProfitSuccessActivity.class);
//                    intent.putExtra("voucherfee", voucherfee);
//                    startActivity(intent);
//                }
//            }
//        });

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 接收切换tab的事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRefreshTabEvent(WebViewRefreshEvent event) {
        callJsRefresh();
    }

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
            Intent detail = new Intent(context, CouponWebViewActivity.class);
            context.startActivity(detail);
            return true;
        }
    }

    @Override
    public void logout(Activity activity, String msg) {
        super.logout(activity, msg);
        finish();
    }
}
