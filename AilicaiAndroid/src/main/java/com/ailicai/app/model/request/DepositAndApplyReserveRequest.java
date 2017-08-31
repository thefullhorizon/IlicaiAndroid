package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Owen on 16/7/12
 */
@RequestPath("/ailicai/depositAndApplyInvest.rest")
public class DepositAndApplyReserveRequest extends Request {

    private long userId; //用户Id,通过请求Header里的uticket获取
    private String paypwd; //交易密码
    private String amount;//房产宝申购金额
    private String requestNo;//请求号
    private String outTradeNo;//外部订单号
    private String productId = ""; // 标的编号(预约编号)
    private String depositAmount;//钱包充值金额
    private int voucherId; // 卡券id

    private int term; //预约期限 （仅预约时传值）
    private String reservePwd; // 预约口令（仅预约时传值）
    private String yearInterestRateStr;//购买预约标使用，预购房产宝，年化率


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public String getReservePwd() {
        return reservePwd;
    }

    public void setReservePwd(String reservePwd) {
        this.reservePwd = reservePwd;
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

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getYearInterestRateStr() {
        return yearInterestRateStr;
    }

    public void setYearInterestRateStr(String yearInterestRateStr) {
        this.yearInterestRateStr = yearInterestRateStr;
    }
}
