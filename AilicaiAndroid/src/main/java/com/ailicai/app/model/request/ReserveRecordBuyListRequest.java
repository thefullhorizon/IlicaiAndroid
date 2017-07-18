package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Owen on 2016/4/18
 */
@RequestPath("/ailicai/buyOkReserveProducts.rest")
public class ReserveRecordBuyListRequest extends Request {

    private String bidOrderNo = "";

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }
}
