package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.FundYeildRateModel;
import com.ailicai.app.model.bean.IncomeModel;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by duo.chen on 2016/7/4.11:48
 */

public class WalletInfoResponse extends Response {
    private double totalAsset; // 总资产
    private double yestodayIncome; // 昨日收益
    private String yearInterestRateStr; // 预计年化利率带百分号
    private String productInfoUrl = ""; //产品信息页Url H5页
    private double availableBalance = 0;//活期宝可用余额
    private double lockedBalance = 0;//冻结的余额
    private int recentNum;//最近交易笔数

    private double totalIncome; // 累计收益
    private double yearInterestRate; // 预计年化利率
    private double bankInterestRate; // 银行活期利率
    private List<IncomeModel> incomeList; //累计收益列表
    private String yestodayIncomeStr=""; // 昨日收益字符串格式 新浪渠道增加
    private String yearInterestRateDesc;//预计年化利率,包含文案 Eg：近7日年化收益2.2300%；新浪渠道增加

    private String unitIncome=""; //万份收益  例如：unitIncome=0.8525
    private List<FundYeildRateModel> fundYeildRateList; // 近七日年化率列表

    public String getYearInterestRateStr() {
        return yearInterestRateStr;
    }

    public void setYearInterestRateStr(String yearInterestRateStr) {
        this.yearInterestRateStr = yearInterestRateStr;
    }

    public double getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(double totalAsset) {
        this.totalAsset = totalAsset;
    }

    public double getYestodayIncome() {
        return yestodayIncome;
    }

    public void setYestodayIncome(double yestodayIncome) {
        this.yestodayIncome = yestodayIncome;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getLockedBalance() {
        return lockedBalance;
    }

    public void setLockedBalance(double lockedBalance) {
        this.lockedBalance = lockedBalance;
    }

    public int getRecentNum() {
        return recentNum;
    }

    public void setRecentNum(int recentNum) {
        this.recentNum = recentNum;
    }

    public String getProductInfoUrl() {
        return productInfoUrl;
    }

    public void setProductInfoUrl(String productInfoUrl) {
        this.productInfoUrl = productInfoUrl;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getYearInterestRate() {
        return yearInterestRate;
    }

    public void setYearInterestRate(double yearInterestRate) {
        this.yearInterestRate = yearInterestRate;
    }

    public double getBankInterestRate() {
        return bankInterestRate;
    }

    public void setBankInterestRate(double bankInterestRate) {
        this.bankInterestRate = bankInterestRate;
    }

    public List<IncomeModel> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<IncomeModel> incomeList) {
        this.incomeList = incomeList;
    }

    public String getYestodayIncomeStr() {
        return yestodayIncomeStr;
    }

    public void setYestodayIncomeStr(String yestodayIncomeStr) {
        this.yestodayIncomeStr = yestodayIncomeStr;
    }

    public String getYearInterestRateDesc() {
        return yearInterestRateDesc;
    }

    public void setYearInterestRateDesc(String yearInterestRateDesc) {
        this.yearInterestRateDesc = yearInterestRateDesc;
    }

    public String getUnitIncome() {
        return unitIncome;
    }

    public void setUnitIncome(String unitIncome) {
        this.unitIncome = unitIncome;
    }

    public List<FundYeildRateModel> getFundYeildRateList() {
        return fundYeildRateList;
    }

    public void setFundYeildRateList(List<FundYeildRateModel> fundYeildRateList) {
        this.fundYeildRateList = fundYeildRateList;
    }
}
