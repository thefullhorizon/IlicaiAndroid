package com.ailicai.app.model.bean;

import android.text.TextUtils;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Jer on 2016/1/7.
 */
public class BuyHuoqibaoResponse extends Response {
    private double amount;//申购金额
    private String calculationDate; //计息日期
    private String giveDate; //到账日期
    private String annualizedYield; //年化收益率
    private String bizStatus;//S成功;P处理中;F失败
    private String outTradeNo;//外部订单号
    private String cardNo; //银行卡号（尾号）
    private String bankName; //银行名称
    private String activityMsg = "";//活动发券结果文案
    private long activityDealId; //活动推进id

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAnnualizedYield() {
        return annualizedYield;
    }

    public void setAnnualizedYield(String annualizedYield) {
        this.annualizedYield = annualizedYield;
    }

    public String getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(String calculationDate) {
        this.calculationDate = calculationDate;
    }

    public String getGiveDate() {
        return giveDate;
    }

    public void setGiveDate(String giveDate) {
        this.giveDate = giveDate;
    }

    public String getBizStatus() {
        return bizStatus = TextUtils.isEmpty(bizStatus) ? "" : bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getActivityMsg() {
        return activityMsg;
    }

    public void setActivityMsg(String activityMsg) {
        this.activityMsg = activityMsg;
    }

    public long getActivityDealId() {
        return activityDealId;
    }

    public void setActivityDealId(long activityDealId) {
        this.activityDealId = activityDealId;
    }

    @Override
    public String toString() {
        return "BuyHuoqibaoResponse{" +
                "amount=" + amount +
                ", calculationDate='" + calculationDate + '\'' +
                ", giveDate='" + giveDate + '\'' +
                ", annualizedYield='" + annualizedYield + '\'' +
                ", bizStatus='" + bizStatus + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
