package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by liyanan on 16/7/12.
 */
@RequestPath("/ailicai/depositAndApplyInvest.rest")
public class DepositAndApplyInvestRequest extends Request {
    private String paypwd; //交易密码
    private double amount;//房产宝申购金额
    private String requestNo;//请求号
    private String outTradeNo;//外部订单号
    private String productId = ""; // 标的编号(预约编号)
    private double depositAmount;//钱包充值金额
    private int voucherId; // 卡券id
    private int term; //预约期限 （仅预约时传值）
    private String reservePwd; // 预约口令（仅预约时传值）
    private int isTransfer; //是否是转让标 0-普通投标；1-债权购买
    private String creditId; //债权转让ID
    private String yearInterestRateStr;//购买预约标使用，预购房产宝，年化率

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getReservePwd() {
        return reservePwd;
    }

    public void setReservePwd(String reservePwd) {
        this.reservePwd = reservePwd;
    }

    public int getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(int isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getYearInterestRateStr() {
        return yearInterestRateStr;
    }

    public void setYearInterestRateStr(String yearInterestRateStr) {
        this.yearInterestRateStr = yearInterestRateStr;
    }
}
