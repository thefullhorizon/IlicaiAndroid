package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Jer on 2016/1/7.
 */
@RequestPath("/ailicai/buyHuoqibao.rest")
public class BuyHuoqibaoRequest extends Request {
    private String paypwd; //交易密码，RSA加密
    private String amount;//申购金额
    private String requestNo;//请求号
    private String outTradeNo;//外部订单号

    //7.2
    private String accountType; //转入类型：101-存钱罐；106-用户账户
    private String payMethod; //支付方式 1-银行卡；2-账户余额

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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
