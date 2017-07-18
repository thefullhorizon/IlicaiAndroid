package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Owen on 2016/3/10
 */
@RequestPath("/ailicai/cancelReserve.rest")
public class ReserveCancelRequest extends Request {

    private long userId; //用户Id,通过请求Header里的uticket获取

    private String bidOrderNo; // 预约订单编号 （从预约列表中获取）
    private String paypwd; //交易密码


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }
}
