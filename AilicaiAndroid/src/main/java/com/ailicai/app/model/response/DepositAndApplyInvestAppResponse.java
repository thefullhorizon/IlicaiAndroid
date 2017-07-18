package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by liyanan on 16/7/12.
 */
public class DepositAndApplyInvestAppResponse extends Response {
    private String advanceVoucherNo; //推进号
    private String outTradeNo;//外部订单号
    private String mobile; //手机号
    private int remainingCnt = 0; // 密码错误剩余次数

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

    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
    }
}
