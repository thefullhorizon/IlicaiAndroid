package com.ailicai.app.ui.account;

import android.view.View;

import com.ailicai.app.ui.html5.SupportUrl;


public class OpenAccountFeature {
    // 开户中状态,主要为交易保障服务卡的一次弹框使用
    public static boolean isOpeningAccount = false;

    /**
     * 卡bin类型 0：安全卡，1：普通绑卡
     */
    public int cardBinType;
    public boolean isDark = false;
    // 第一个界面
    public int tvCancelVisible = View.GONE;
    public String tvCancelText = "";
    public String titleText = "";
    /**
     * 支持的银行卡URL地址：0.支持所有的储蓄卡 1.支持的所有银行卡列表
     */
    public String supportCardsUrl = "";
    public String boundTitleText = "";
    /**
     * 为空表示该行不显示
     */
    public String nameTitleTv = "";
    public int safeCardHelpVisible = View.VISIBLE;
    public boolean bindNewCard = false;

    // 第二个界面
    public String toast2Text = "";
    // 第三个界面
    public String title3Text = "";
    public String toastSuccessText = "";
    public String toastFaileText = "";
    public Class activity;

    public static OpenAccountFeature openAccount() {
        OpenAccountFeature feature = new OpenAccountFeature();

        // Start Setting
        // 第一个界面
        feature.cardBinType = 0;
        feature.isDark = true;
        feature.tvCancelVisible = View.VISIBLE;
        feature.tvCancelText = "是否放弃开户";
        feature.titleText = "开户";
        feature.supportCardsUrl = SupportUrl.getSupportUrlsResponse().getSupportcardsByAllUrl() + "?channel=2";
        feature.boundTitleText = "请绑定账户本人的卡，作为";
        feature.nameTitleTv = "开户人";
        feature.safeCardHelpVisible = View.VISIBLE;
        feature.bindNewCard = false;
        feature.title3Text = "绑定银行卡需要短信确认，验证码已发送以下手机";
        feature.toastSuccessText = "";
        feature.toastFaileText = "";
        // 第二个界面
//        feature.activity = OpenAccountResultActivity.class;
        // End Setting

        return feature;
    }

    public static OpenAccountFeature openAccountUseNewBankCard() {
        OpenAccountFeature feature = openAccount();
        feature.bindNewCard = true;
        return feature;

    }
}