package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

@RequestPath("/ailicai/creditAssignmentInit.rest")
public class TransferPayBaseInfoRequest extends Request {
    private long userId; //用户Id,通过请求Header里的uticket获取
    private String orderNo; //产品编号

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
