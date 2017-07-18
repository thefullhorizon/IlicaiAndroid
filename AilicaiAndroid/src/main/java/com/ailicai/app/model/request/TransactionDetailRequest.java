package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by wulianghuan on 2016/02/15.
 */
@RequestPath("/ailicai/tradeDetail.rest")
public class TransactionDetailRequest extends Request {

    private long userId; //用户Id,通过请求Header里的uticket获取
    private String tradeNo = ""; // 交易号
    private int tradeType; // 交易类型 0：全部，1：转入，2：转出，3：购买，4：回款, 5:支付, 6: 转让

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

}

