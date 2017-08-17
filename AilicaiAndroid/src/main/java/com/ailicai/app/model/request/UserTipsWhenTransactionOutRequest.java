package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by nanshan on 8/17/2017.
 */

@RequestPath("/ailicai/saleHuoqibaoPreCalc.rest")
public class UserTipsWhenTransactionOutRequest extends Request {

    private String accountType; // "101":活期宝，"106":存管账号
    private String payMethod; // "1":存管账号，"2":安全卡

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}
