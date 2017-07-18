package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Protocol;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by Gerry on 2016/1/7.
 * 转出钱包页面初始数据
 */
public class CurrentRollOutBaseInfoResponse extends Response {
    private double accountBalance;//余额
    private double withdrawBalance;//可提现余额
    private String giveDate; //到账日期
    private String bankName; //银行名称
    private String cardNo; //银行卡号（尾号）
    private String hint = "";
    private double limit;//单笔限额

    private double depositoryBalance; //存管账户可用余额,存钱罐转出时使用
    private String toDepositoryDate; //转出到存管账户的到账日期
    private List<Protocol> protocolList;//协议list

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getGiveDate() {
        return giveDate;
    }

    public void setGiveDate(String giveDate) {
        this.giveDate = giveDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public double getWithdrawBalance() {
        return withdrawBalance;
    }

    public void setWithdrawBalance(double withdrawBalance) {
        this.withdrawBalance = withdrawBalance;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getDepositoryBalance() {
        return depositoryBalance;
    }

    public void setDepositoryBalance(double depositoryBalance) {
        this.depositoryBalance = depositoryBalance;
    }

    public String getToDepositoryDate() {
        return toDepositoryDate;
    }

    public void setToDepositoryDate(String toDepositoryDate) {
        this.toDepositoryDate = toDepositoryDate;
    }

    public List<Protocol> getProtocolList() {
        return protocolList;
    }

    public void setProtocolList(List<Protocol> protocolList) {
        this.protocolList = protocolList;
    }
}
