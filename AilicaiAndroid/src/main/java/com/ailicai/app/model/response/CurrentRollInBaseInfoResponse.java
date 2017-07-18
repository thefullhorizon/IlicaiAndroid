package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.ActivityModel;
import com.ailicai.app.model.bean.Protocol;
import com.huoqiu.framework.rest.Response;


import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Gerry on 2016/1/7.
 * 转入钱包页面初始数据
 */
public class CurrentRollInBaseInfoResponse extends Response {
    private String cardNo; //银行卡号（尾号）
    private String bankName; //银行名称
    private int cardType; //卡类型【1：储蓄卡 、2：信用卡 、3：存折 、4：其它 】
    private double buyLimit; //最大转入限额 0为不限
    private String buyLimitStr; //最大转入限额
    private double buyMinLimit; //最小转入限额
    private String buyMinLimitStr; //最小转入限额Str
    private String bankAccountId;//银行卡标识
    private String giveDate; //到账日期
    private String huoqibaoEntrustProtocol; //定向委托投资管理（钱包）服务协议
    private String hint = "";//购买钱包提示文案
    private double withdrawBalance;//账户可用余额
    private ActivityModel activity; //活动信息
    private List<Protocol> protocolList;//协议list

    //7.0
    private String openEBankTitle=""; // 开通网银大额支付
    private String openEBankDetailUrl = ""; //开通网银详情地址
    private double dayRemain; // 当日剩余限额字符串
    private String dayRemainStr; // 当日剩余限额处理后的字符串：万元的整数倍显示X万，千元的整数倍显示X千，其他情况显示具体金额XXX元

    //7.2
    private double depositoryBalance; //存管账户可用余额 （转入活期宝初始页用）

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

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public double getBuyLimit() {
        return buyLimit;
    }

    public String getFormatBuyLimitStr() {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(buyLimit);
    }

    public void setBuyLimit(double buyLimit) {
        this.buyLimit = buyLimit;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBuyLimitStr() {
        return buyLimitStr;
    }

    public void setBuyLimitStr(String buyLimitStr) {
        this.buyLimitStr = buyLimitStr;
    }

    public String getGiveDate() {
        return giveDate;
    }

    public void setGiveDate(String giveDate) {
        this.giveDate = giveDate;
    }

    public String getHuoqibaoEntrustProtocol() {
        return huoqibaoEntrustProtocol;
    }

    public void setHuoqibaoEntrustProtocol(String huoqibaoEntrustProtocol) {
        this.huoqibaoEntrustProtocol = huoqibaoEntrustProtocol;
    }

    public double getBuyMinLimit() {
        return buyMinLimit;
    }

    public String getFormatBuyMinLimitStr() {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(buyMinLimit);
    }

    public void setBuyMinLimit(double buyMinLimit) {
        this.buyMinLimit = buyMinLimit;
    }

    public String getBuyMinLimitStr() {
        return buyMinLimitStr;
    }

    public void setBuyMinLimitStr(String buyMinLimitStr) {
        this.buyMinLimitStr = buyMinLimitStr;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public double getWithdrawBalance() {
        return withdrawBalance;
    }

    public void setWithdrawBalance(double withdrawBalance) {
        this.withdrawBalance = withdrawBalance;
    }

    public ActivityModel getActivity() {
        return activity;
    }

    public void setActivity(ActivityModel activity) {
        this.activity = activity;
    }

    public List<Protocol> getProtocolList() {
        return protocolList;
    }

    public void setProtocolList(List<Protocol> protocolList) {
        this.protocolList = protocolList;
    }

    public String getOpenEBankTitle() {
        return openEBankTitle;
    }

    public void setOpenEBankTitle(String openEBankTitle) {
        this.openEBankTitle = openEBankTitle;
    }

    public String getOpenEBankDetailUrl() {
        return openEBankDetailUrl;
    }

    public void setOpenEBankDetailUrl(String openEBankDetailUrl) {
        this.openEBankDetailUrl = openEBankDetailUrl;
    }

    public double getDayRemain() {
        return dayRemain;
    }

    public void setDayRemain(double dayRemain) {
        this.dayRemain = dayRemain;
    }

    public String getDayRemainStr() {
        return dayRemainStr;
    }

    public void setDayRemainStr(String dayRemainStr) {
        this.dayRemainStr = dayRemainStr;
    }

    public double getDepositoryBalance() {
        return depositoryBalance;
    }

    public void setDepositoryBalance(double depositoryBalance) {
        this.depositoryBalance = depositoryBalance;
    }
}
