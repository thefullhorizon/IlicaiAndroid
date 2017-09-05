package com.ailicai.app.model.response;

import android.text.TextUtils;

import com.huoqiu.framework.rest.Response;

/**
 * Created by liyanan on 16/9/7.
 */
public class AilicaiPartResponse extends Response {
    private String currentDepositBalance = ""; // 钱包总额
    private String timeDepositBalance = "预计年化超7%"; // 吉爱财总额 or 未开户文案
    private String totalBalance = "";//总额

    public String getCurrentDepositBalance() {
        return currentDepositBalance;
    }

    public String getCurrentDepositBalanceDesc() {
        return TextUtils.isEmpty(currentDepositBalance)?"":currentDepositBalance+"元";
    }

    public void setCurrentDepositBalance(String currentDepositBalance) {
        this.currentDepositBalance = currentDepositBalance;
    }

    public String getTimeDepositBalance() {
        return timeDepositBalance;
    }

    public String getTimeDepositBalanceDesc() {
        return TextUtils.isEmpty(timeDepositBalance)?"":timeDepositBalance+"元";
    }

    public void setTimeDepositBalance(String timeDepositBalance) {
        this.timeDepositBalance = timeDepositBalance;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }
}
