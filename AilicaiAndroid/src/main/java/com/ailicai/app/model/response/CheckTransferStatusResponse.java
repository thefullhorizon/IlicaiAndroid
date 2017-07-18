package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CheckTransferStatusResponse extends Response {
    private String orderNo =""; //订单编号

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
