package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Jer on 2016/1/7.
 */
public class BuyHuoqibaoResendResponse extends Response {

    private String advanceVoucherNo; //推进号
    private String outTradeNo;//外部订单号
    private String mobile; //手机号码

    public String getAdvanceVoucherNo() {
        return advanceVoucherNo;
    }

    public void setAdvanceVoucherNo(String advanceVoucherNo) {
        this.advanceVoucherNo = advanceVoucherNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
