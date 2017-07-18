package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Jer on 2016/1/8.
 */
@RequestPath("/ailicai/saleHuoqibao.rest")
public class SaleHuoqibaoRequest extends Request {
    private double amount;//转出金额
    private String paypwd; //交易密码，RSA加密
    private String outTradeNo;//外部订单号 （通过接口22、0获取外部交易号获取）
    //7.2
    private String accountType; //收银台类型：101-活期宝；106-用户账户
    private String payMethod; //支付到的账户类型 1-安全卡；2-账户余额 说明：活期宝收银台需指定

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

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
