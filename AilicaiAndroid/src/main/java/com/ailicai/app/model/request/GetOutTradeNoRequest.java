package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by jrvair on 16/1/14.
 */
@RequestPath("/ailicai/getOutTradeNo.rest")
public class GetOutTradeNoRequest extends Request {

    private double amount;//申购金额
    private int bizType; //业务类型 1-买入钱包 2-钱包提现


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }
}
