package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by nanshan on 7/19/2017.
 */

public class AssetInfoNewResponse extends Response {

    private String totalAsset;                                                     // 总资产
    private String yestodayIncome;                                                 // 昨日收益
    private String totalIncome;                                                    // 累计收益
    private String currentDepositBalance = "";                                     // 房产宝余额
    private String timeDepositBalance = "";                                     // 货币基金
    private String smallPurleBalance = "";                                        //小钱袋余额
    private String netLoanBalance = "";                                     //网贷资产
    private String accountBalance = "";                                           //账户可用余额

    private int voucherRedPoint;                                          //卡券小红点：1有红点 0没有红点
    private String holdTotalBalance = "";                 //持有总额

    private int purchaseCount; // 申购数量，如果0就不显示
    private String purchaseAmount = ""; // 申购金额

    private String depositIncome; //货币基金收益

    private String netLoanIncome;//网贷收益

    private String experienceIncome;//体验宝收益

    public String getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(String totalAsset) {
        this.totalAsset = totalAsset;
    }

    public String getYestodayIncome() {
        return yestodayIncome;
    }

    public void setYestodayIncome(String yestodayIncome) {
        this.yestodayIncome = yestodayIncome;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getCurrentDepositBalance() {
        return currentDepositBalance;
    }

    public void setCurrentDepositBalance(String currentDepositBalance) {
        this.currentDepositBalance = currentDepositBalance;
    }

    public String getTimeDepositBalance() {
        return timeDepositBalance;
    }

    public void setTimeDepositBalance(String timeDepositBalance) {
        this.timeDepositBalance = timeDepositBalance;
    }

    public String getSmallPurleBalance() {
        return smallPurleBalance;
    }

    public void setSmallPurleBalance(String smallPurleBalance) {
        this.smallPurleBalance = smallPurleBalance;
    }

    public String getNetLoanBalance() {
        return netLoanBalance;
    }

    public void setNetLoanBalance(String netLoanBalance) {
        this.netLoanBalance = netLoanBalance;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getVoucherRedPoint() {
        return voucherRedPoint;
    }

    public void setVoucherRedPoint(int voucherRedPoint) {
        this.voucherRedPoint = voucherRedPoint;
    }

    public String getHoldTotalBalance() {
        return holdTotalBalance;
    }

    public void setHoldTotalBalance(String holdTotalBalance) {
        this.holdTotalBalance = holdTotalBalance;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getDepositIncome() {
        return depositIncome;
    }

    public void setDepositIncome(String depositIncome) {
        this.depositIncome = depositIncome;
    }

    public String getNetLoanIncome() {
        return netLoanIncome;
    }

    public void setNetLoanIncome(String netLoanIncome) {
        this.netLoanIncome = netLoanIncome;
    }

    public String getExperienceIncome() {
        return experienceIncome;
    }

    public void setExperienceIncome(String experienceIncome) {
        this.experienceIncome = experienceIncome;
    }
}
