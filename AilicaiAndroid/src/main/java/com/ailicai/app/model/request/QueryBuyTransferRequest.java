package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Gerry on 16/11/22.
 * 钱包余额直接购买5次轮询接口
 */
@RequestPath("/ailicai/queryBuyTransfer.rest")
public class QueryBuyTransferRequest extends Request {
    private String bidOrderNo; //订单编号

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }
}
