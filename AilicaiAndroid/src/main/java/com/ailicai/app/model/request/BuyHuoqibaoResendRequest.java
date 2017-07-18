package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Jer on 2016/1/7.
 */
@RequestPath("/ailicai/buyHuoqibaoResend.rest")
public class BuyHuoqibaoResendRequest extends Request {
    private String paypwd; //交易密码，RSA加密
    private double amount;//申购金额
    private String accountType; //转入类型：101-存钱罐；106-用户账户
    private String payMethod; //支付方式 1-安全卡；2-账户余额

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
