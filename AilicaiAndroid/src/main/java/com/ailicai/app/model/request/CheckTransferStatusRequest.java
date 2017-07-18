package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Administrator on 2016/8/1.
 */
@RequestPath("/ailicai/checkAssignmentStatus.rest")
public class CheckTransferStatusRequest extends Request {

    private String orderNo =""; //订单编号

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
