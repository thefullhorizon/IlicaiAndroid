package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.ActivityModel;
import com.ailicai.app.model.bean.Protocol;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by Gerry on 2016/1/7.
 * 转入房产宝页面初始数据
 */
public class RegularPayBaseInfoResponse extends Response {
    private String productId = ""; // 产品Id
    private String productName; //产品名称
    private double minAmount; //起购金额
    private String minAmountStr; //起购金额文案
    private double bidUnit; //投标单位 递增金额
    private String bidUnitStr; //投标单位 递增金额文案
    private double currentDepositBalance;//钱包余额
    private double buyLimit; //单笔限额
    private String buyLimitStr; //单笔限额文案
    private double maxPerProductLimit; //产品单人累计限额
    private String maxPerProductLimitStr; //产品单人累计限额文案
    private double biddableAmount;//可投标金额
    private String biddableAmountStr;//可投标金额
    private String requestNo; //请求交易号
    private String dingqibaoBrokerProtocol = "";//房产宝居间协议地址
    private double availableBalance;//可用余额
    private String limitString = ""; //单笔限额、累计限额文案 为空时不显示
    private String hint = "";//购买钱包提示文案
    private double yearInterestRate; // 预计年化利率
    private int loanTerm; // 房产宝投资期限
    private int interestVoucherNum = 0;//加息券数量
    private int bankLimit;//充值限额
    private String bankLimitStr="";//银行限额文案
    private int voucherId; //卡券ID
    private int addRateDay; //可加息天数
    private double addRate; //加息比例 小数表示，比如1.5表示1.5%

    private ActivityModel activity; //活动信息
    private List<Protocol> protocolList;//协议list

    /****** 购买转让相关字段  5.4新增  edit by zhouxuan *********************/
    private int isTransfer;//是否是转让房产宝 1 是 0否
    private long interestDate;//起息日
    private int isOwnTransfer;//是否是自己的转让 1是 0 否
    private int oriLoanTerm;//原房产宝期限
    private String transferProtocol ="";//债权转让协议地址
    private String actualPayTipsString="";//实际支付 提示
    private int timeDiff = 0; //转让日-起息日
    private String creditId; //债券转让ID
    /****** 购买转让相关字段 5.4新增  edit by zhouxuan *********************/




    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public double getCurrentDepositBalance() {
        return currentDepositBalance;
    }

    public void setCurrentDepositBalance(double currentDepositBalance) {
        this.currentDepositBalance = currentDepositBalance;
    }

    public double getBuyLimit() {
        return buyLimit;
    }

    public void setBuyLimit(double buyLimit) {
        this.buyLimit = buyLimit;
    }

    public double getMaxPerProductLimit() {
        return maxPerProductLimit;
    }

    public void setMaxPerProductLimit(double maxPerProductLimit) {
        this.maxPerProductLimit = maxPerProductLimit;
    }

    public double getBidUnit() {
        return bidUnit;
    }

    public void setBidUnit(double bidUnit) {
        this.bidUnit = bidUnit;
    }

    public double getBiddableAmount() {
        return biddableAmount;
    }

    public void setBiddableAmount(double biddableAmount) {
        this.biddableAmount = biddableAmount;
    }

    public String getBiddableAmountStr() {
        return biddableAmountStr;
    }

    public void setBiddableAmountStr(String biddableAmountStr) {
        this.biddableAmountStr = biddableAmountStr;
    }

    public String getMinAmountStr() {
        return minAmountStr;
    }

    public void setMinAmountStr(String minAmountStr) {
        this.minAmountStr = minAmountStr;
    }

    public String getBidUnitStr() {
        return bidUnitStr;
    }

    public void setBidUnitStr(String bidUnitStr) {
        this.bidUnitStr = bidUnitStr;
    }

    public String getBuyLimitStr() {
        return buyLimitStr;
    }

    public void setBuyLimitStr(String buyLimitStr) {
        this.buyLimitStr = buyLimitStr;
    }

    public String getMaxPerProductLimitStr() {
        return maxPerProductLimitStr;
    }

    public void setMaxPerProductLimitStr(String maxPerProductLimitStr) {
        this.maxPerProductLimitStr = maxPerProductLimitStr;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getDingqibaoBrokerProtocol() {
        return dingqibaoBrokerProtocol;
    }

    public void setDingqibaoBrokerProtocol(String dingqibaoBrokerProtocol) {
        this.dingqibaoBrokerProtocol = dingqibaoBrokerProtocol;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getLimitString() {
        return limitString;
    }

    public void setLimitString(String limitString) {
        this.limitString = limitString;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public double getYearInterestRate() {
        return yearInterestRate;
    }

    public void setYearInterestRate(double yearInterestRate) {
        this.yearInterestRate = yearInterestRate;
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public int getInterestVoucherNum() {
        return interestVoucherNum;
    }

    public void setInterestVoucherNum(int interestVoucherNum) {
        this.interestVoucherNum = interestVoucherNum;
    }

    public ActivityModel getActivity() {
        return activity;
    }

    public void setActivity(ActivityModel activity) {
        this.activity = activity;
    }

    public int getBankLimit() {
        return bankLimit;
    }

    public void setBankLimit(int bankLimit) {
        this.bankLimit = bankLimit;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public int getAddRateDay() {
        return addRateDay;
    }

    public void setAddRateDay(int addRateDay) {
        this.addRateDay = addRateDay;
    }

    public double getAddRate() {
        return addRate;
    }

    public void setAddRate(double addRate) {
        this.addRate = addRate;
    }

    public int getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(int isTransfer) {
        this.isTransfer = isTransfer;
    }

    public long getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(long interestDate) {
        this.interestDate = interestDate;
    }

    public int getIsOwnTransfer() {
        return isOwnTransfer;
    }

    public void setIsOwnTransfer(int isOwnTransfer) {
        this.isOwnTransfer = isOwnTransfer;
    }

    public int getOriLoanTerm() {
        return oriLoanTerm;
    }

    public void setOriLoanTerm(int oriLoanTerm) {
        this.oriLoanTerm = oriLoanTerm;
    }

    public String getTransferProtocol() {
        return transferProtocol;
    }

    public void setTransferProtocol(String transferProtocol) {
        this.transferProtocol = transferProtocol;
    }

    public String getActualPayTipsString() {
        return actualPayTipsString;
    }

    public void setActualPayTipsString(String actualPayTipsString) {
        this.actualPayTipsString = actualPayTipsString;
    }

    public int getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(int timeDiff) {
        this.timeDiff = timeDiff;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public List<Protocol> getProtocolList() {
        return protocolList;
    }

    public void setProtocolList(List<Protocol> protocolList) {
        this.protocolList = protocolList;
    }

    public String getBankLimitStr() {
        return bankLimitStr;
    }

    public void setBankLimitStr(String bankLimitStr) {
        this.bankLimitStr = bankLimitStr;
    }
}
