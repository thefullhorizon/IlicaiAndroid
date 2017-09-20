package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Jer on 2016/1/7.
 */
@RequestPath("/ailicai/buyStatusQuery.rest")
public class BuyHuoqibaoStateRequest extends Request {
    private String outTradeNo;//外部订单号
    private long activityDealId; //活动推进id
    private String accountType; //收银台类型：101-活期宝；106-用户账户
    private String payMethod; //支付到的账户类型 1-银行卡；2-账户余额 说明：活期宝收银台需指定

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public long getActivityDealId() {
        return activityDealId;
    }

    public void setActivityDealId(long activityDealId) {
        this.activityDealId = activityDealId;
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
