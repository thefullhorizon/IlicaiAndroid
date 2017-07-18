package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by zhouxuan on 16/8/2.
 *
 * 购买转让房产宝请求实际支付
 */
@RequestPath("/ailicai/calActualPay.rest")
public class CalActualPayRequest extends Request {

    private double amount;//支付本金
    private long beginTime;//计息日
    private String rateStr;//年化利率 16% 传16
    private String term;//原房产宝借款期限  数字 不带天数

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public String getRateStr() {
        return rateStr;
    }

    public void setRateStr(String rateStr) {
        this.rateStr = rateStr;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
