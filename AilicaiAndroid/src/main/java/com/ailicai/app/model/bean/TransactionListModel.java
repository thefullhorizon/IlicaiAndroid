package com.ailicai.app.model.bean;

import java.io.Serializable;

/**
 * Created by wulianghuan on 2016/1/4.
 */
public class TransactionListModel implements Serializable {
    private int tradeType; // 交易类型 0：全部，1：转入，2：转出，3：购买，4：回款, 5:支付, 6: 转让
    private String tradeTypeStr;
    private String tradeContent = ""; // 交易内容
    private long tradeTime;
    private String tradeTimeYmdHm = ""; // 交易时间 yyyy-MM-dd HH:mm
    private String tradeTimeYmdHms = ""; // 交易时间 yyyy-MM-dd HH:mm:ss
    private double tradeAmt; // 交易金额
    private String tradeAmtStr; // 格式化的交易金额
    private int status; // 交易状态 1：成功，2：失败，3：进行中，4：已退款
    private String statusStr;
    private String errorMsg = ""; // 交易错误提示信息
    private String tradeNo = ""; // 交易号
    private String payAccount = ""; // 付款账户 如：招商银行储蓄卡（7896）
    private boolean isExpand = false; // listview 该item是否已展开
    public int userRole;// 1-借款人
    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeContent() {
        return tradeContent;
    }

    public void setTradeContent(String tradeContent) {
        this.tradeContent = tradeContent;
    }

    public long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTradeTimeYmdHm() {
        return tradeTimeYmdHm;
    }

    public void setTradeTimeYmdHm(String tradeTimeYmdHm) {
        this.tradeTimeYmdHm = tradeTimeYmdHm;
    }

    public String getTradeTimeYmdHms() {
        return tradeTimeYmdHms;
    }

    public void setTradeTimeYmdHms(String tradeTimeYmdHms) {
        this.tradeTimeYmdHms = tradeTimeYmdHms;
    }

    public double getTradeAmt() {
        return tradeAmt;
    }

    public void setTradeAmt(double tradeAmt) {
        this.tradeAmt = tradeAmt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public String getTradeTypeStr() {
        return tradeTypeStr;
    }

    public void setTradeTypeStr(String tradeTypeStr) {
        this.tradeTypeStr = tradeTypeStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getTradeAmtStr() {
        return tradeAmtStr;
    }

    public void setTradeAmtStr(String tradeAmtStr) {
        this.tradeAmtStr = tradeAmtStr;
    }
}
