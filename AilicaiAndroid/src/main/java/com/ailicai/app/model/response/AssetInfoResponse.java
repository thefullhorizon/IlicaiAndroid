package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Product;
import com.huoqiu.framework.rest.Response;

/**
 * Created by David on 16/1/6.
 */
public class AssetInfoResponse extends Response {

    private int errorCode = 0; //返回代码 0-正常 其他参考对应的errorCode定义
    private String message = "";//返回消息
    private double totalAsset; // 总资产
    private double yestodayIncome; // 昨日收益
    private double totalIncome; // 累计收益
    private String preProfit=""; //房产宝待收收益,包含加息

    private String currentDepositBalance = ""; // 钱包余额，余额没有则下发空串
    private String timeDepositBalance = ""; // 房产宝余额，余额没有则下发空串
    private int hasSale; // 有无可售产品 0：无，1：1小时内有可售，2：1小时外有可售
    private String assureMemo = ""; // 担保说明文案
    private Product product; // 第一个可售产品的信息

    private String noSaleHorizonStr = ""; // 无可售产品时投资期限带单位
    private String noSaleMinAmtStr = ""; // 无可售产品时默认模版的起购金额带单位
    private String noSaleYearRate = ""; // 无可售产品时默认模版的预计年化利率 如：7.25%-10%

    private int applyNum; //申购数量
    private int holdNum; //持有数量
    private int overdueNum; //到期数量



    private String yearInterestRate=""; // 钱包预计年化利率文案
    private String productInterestRate=""; // 房产宝宝预计年化利率文案
    private String hisAvgIncomeRate=""; //房产宝历史年化率 eg:12.8%

    private String reserveFund=""; //准备金文案
    private String principalTitle ="持有本金说明";//持有本金的解释文案标题
    private String principalDes = "持有本金=持有列表下的项目本金+购买转让房产宝的本金部分(不含垫付利息)";//持有本金的解释文案

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
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

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
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

    public int getHasSale() {
        return hasSale;
    }

    public void setHasSale(int hasSale) {
        this.hasSale = hasSale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getAssureMemo() {
        return assureMemo;
    }

    public void setAssureMemo(String assureMemo) {
        this.assureMemo = assureMemo;
    }

    public String getNoSaleMinAmtStr() {
        return noSaleMinAmtStr;
    }

    public void setNoSaleMinAmtStr(String noSaleMinAmtStr) {
        this.noSaleMinAmtStr = noSaleMinAmtStr;
    }

    public String getNoSaleYearRate() {
        return noSaleYearRate;
    }

    public void setNoSaleYearRate(String noSaleYearRate) {
        this.noSaleYearRate = noSaleYearRate;
    }

    public String getNoSaleHorizonStr() {
        return noSaleHorizonStr;
    }

    public void setNoSaleHorizonStr(String noSaleHorizonStr) {
        this.noSaleHorizonStr = noSaleHorizonStr;
    }

    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }

    public int getHoldNum() {
        return holdNum;
    }

    public void setHoldNum(int holdNum) {
        this.holdNum = holdNum;
    }

    public int getOverdueNum() {
        return overdueNum;
    }

    public void setOverdueNum(int overdueNum) {
        this.overdueNum = overdueNum;
    }

    public String getYearInterestRate() {
        return yearInterestRate;
    }

    public void setYearInterestRate(String yearInterestRate) {
        this.yearInterestRate = yearInterestRate;
    }

    public String getProductInterestRate() {
        return productInterestRate;
    }

    public void setProductInterestRate(String productInterestRate) {
        this.productInterestRate = productInterestRate;
    }

    public String getHisAvgIncomeRate() {
        return hisAvgIncomeRate;
    }

    public void setHisAvgIncomeRate(String hisAvgIncomeRate) {
        this.hisAvgIncomeRate = hisAvgIncomeRate;
    }

    public String getReserveFund() {
        return reserveFund;
    }

    public void setReserveFund(String reserveFund) {
        this.reserveFund = reserveFund;
    }

    public String getPrincipalTitle() {
        return principalTitle;
    }

    public void setPrincipalTitle(String principalTitle) {
        this.principalTitle = principalTitle;
    }

    public String getPrincipalDes() {
        return principalDes;
    }

    public void setPrincipalDes(String principalDes) {
        this.principalDes = principalDes;
    }

    public String getPreProfit() {
        return preProfit;
    }

    public void setPreProfit(String preProfit) {
        this.preProfit = preProfit;
    }

    public void reset() {
        setTotalAsset(0);
        setYestodayIncome(0);
        setTotalIncome(0);
        setCurrentDepositBalance("");
        setTimeDepositBalance("");
        setHasSale(0);
        setProduct(new Product());
    }
}
