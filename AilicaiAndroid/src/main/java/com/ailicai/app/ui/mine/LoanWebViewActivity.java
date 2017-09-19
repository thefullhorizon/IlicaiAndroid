package com.ailicai.app.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.push.MqttManager;
import com.ailicai.app.common.push.PushUtil;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.model.request.UserLoginRequest;
import com.ailicai.app.model.response.UserLoginResponse;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebMethodCallAction;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.NetworkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by duo.chen on 2016/8/16
 */

public class LoanWebViewActivity extends BaseWebViewActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        setTopTheme(true);

        addMethodCallAction(new WebMethodCallAction("h5login") {
            @Override
            public Object call(HashMap params) {
                if (params.containsKey("callback")) {

                    getWebViewLayout().setLoginCallBack(String.valueOf(params.get("callback")));

                    if (params.containsKey("phoneNumber") && params.containsKey("verifyCode")) {
                        login(String.valueOf(params.get("phoneNumber")),
                                String.valueOf(params.get("verifyCode")),
                                LoginManager.LOGIN_FROM_H5_WEB);
                    }
                }
                return null;
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
                webViewLayout.setTitle(title);
            }
        });

        addMethodCallAction(new WebMethodCallAction("setnavigation") {
            @Override
            public Object call(HashMap params) {
                try {
                    String showleft = String.valueOf(params.get("showleft"));
                    String showright = String.valueOf(params.get("showright"));
                    final String rightTxt = String.valueOf(params.get("righttxt"));
                    final String rightUrl = String.valueOf(params.get("righturl"));

                    showBack("yes".equals(showleft));
                    showRightTextIcon("yes".equals(showright));

                    if (!TextUtils.isEmpty(rightTxt)) {
                        switch (rightUrl) {
                            case "contactservice":
                                addTitleRightText(rightTxt, R.style.text_14_ffffff, new View
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        if (!CheckDoubleClick.isFastDoubleClick()) {
                                            showCallPhoneDialog(UserInfo.getInstance().getServicePhoneNumber());
                                        }
                                    }
                                });
                                break;
                        }
                    }

                }catch (Exception ignore){

                }
                return null;
            }
        });

        //贷总管
        loadUrl(SupportUrl.getSupportUrlsResponse().getDzgEntryUrl());

        //getWebViewLayout().setFitsSystemWindows(true);
        //setShowSystemBarTint(false);

    }

    public void login(final String phoneNumber, String verifyCode, final int fromPage){
        UserLoginRequest loginRequest = new UserLoginRequest();
        List<String> list = new ArrayList<>();
        String[] houseInfos = new String[list.size()];
        houseInfos = list.toArray(houseInfos);
        loginRequest.setHouseIds(houseInfos);
        loginRequest.setMobile(phoneNumber);
        loginRequest.setVerifyCode(StringUtil.allSpaceFormat(verifyCode));
        loginRequest.setMobileSn(DeviceUtil.getMobileUUID(MyApplication.getInstance()));
        loginRequest.setFromPage(fromPage);
        loginRequest.setSystemVer(DeviceUtil.getSystemVersion());
        loginRequest.setNetType(NetworkUtil.getNetWorkTypeStr(MyApplication.getInstance()));
        loginRequest.setSupport(DeviceUtil.getOperators(MyApplication.getInstance()));
        loginRequest.setClientId(MqttManager.getClientIdByMobile(phoneNumber));

        ServiceSender.exec(this, loginRequest, new IwjwRespListener<UserLoginResponse>() {
            @Override
            public void onStart() {
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(UserLoginResponse jsonObject) {
                showContentView();

                int code = jsonObject.getErrorCode();
                if (code == 0) {
                    if (jsonObject.getUserId() > 0) {
                        UserInfo.getInstance().setUserInfoData(jsonObject, phoneNumber);
                        //处理登录成功相关事件
                        boolean showPackage = !TextUtils.isEmpty(jsonObject.getActivityName());
                        LoginManager.loginSuccess(LoanWebViewActivity.this, fromPage, jsonObject, showPackage);
                        PushUtil.resetMqttService(LoanWebViewActivity.this);
                    } else {
                        showMyToast("认证失败，请重试！");
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                showMyToast(errorInfo);
                loadJs("javascript:" + getWebViewLayout().getLoginCallBack() + "(false)");
            }
        });
    }

}
