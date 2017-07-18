package com.ailicai.app.model.response;

import android.text.TextUtils;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Jer on 2016/1/8.
 */
public class SaleHuoqibaoResponse extends Response {
    private double amount;//转出金额
    private String giveDate; //到账日期
    private String outTradeNo;//外部订单号
    private String bizStatus;//S成功;P处理中;F失败
    private String tips="";//大于5w文案提示

    private int remainingCnt=-1;

    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
    }

    public String getBizStatus() {
        return bizStatus = TextUtils.isEmpty(bizStatus) ? "" : bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGiveDate() {
        return giveDate;
    }

    public void setGiveDate(String giveDate) {
        this.giveDate = giveDate;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "SaleHuoqibaoResponse{" +
                "amount=" + amount +
                ", giveDate='" + giveDate + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", bizStatus='" + bizStatus + '\'' +
                '}';
    }
}
