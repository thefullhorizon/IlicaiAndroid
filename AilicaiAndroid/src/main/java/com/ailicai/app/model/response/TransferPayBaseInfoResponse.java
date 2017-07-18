package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

public class TransferPayBaseInfoResponse extends Response {
    private String productId = ""; // 产品Id
    private String productName = ""; // 产品名称
    private double yearInterestRate; // 预计年化利率 小数表示，比如0.15表示15%
    private String yearInterestRateStr; // 预计年化利率字符串
    private String totalDays; // 投资总期限，单位是天
    private String remainDays; // 剩余期限，单位是天
    private int status; // 理财状态 1-正在募集；2-即将开售；3-已售罄 4-已结束
    private double applyAmount; // 可转额度
    /**
     * 持有天数<=30天 转让手续费=转让价格*0.6%；其中30天和0.6%为服务端下发值
     * 持有天数>30天 转让手续费=0
     */
    private int limitDays = 30;//手续费收取天数
    private double gt30CostRate = 0.003;//超过30天手续费比例
    private String gt30CostRateStr = "0.3%";//超过30天手续费比例Str
    private int holdDays;//持有天数
    private String tips = "";//顶部提示 不足1000 全部转让
    private long beginTime;//起息日期

    private String assignRule;// Settings.Global.HTML5_URL+"licai/assign/explain";//转让规则h5 URL
    private String hint = "";//输入金额提示 eg请输入1000的整数倍

    private double unit;//购买的整数倍值   100 1000 10000

    private String creditId = "";//债权id
    private double minAmount;//最小起投
    private String chargeTips = "";//手续费提示

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

    public double getYearInterestRate() {
        return yearInterestRate;
    }

    public void setYearInterestRate(double yearInterestRate) {
        this.yearInterestRate = yearInterestRate;
    }

    public String getYearInterestRateStr() {
        return yearInterestRateStr;
    }

    public void setYearInterestRateStr(String yearInterestRateStr) {
        this.yearInterestRateStr = yearInterestRateStr;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(String remainDays) {
        this.remainDays = remainDays;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(double applyAmount) {
        this.applyAmount = applyAmount;
    }

    public int getLimitDays() {
        return limitDays;
    }

    public void setLimitDays(int limitDays) {
        this.limitDays = limitDays;
    }

    public double getGt30CostRate() {
        return gt30CostRate;
    }

    public void setGt30CostRate(double gt30CostRate) {
        this.gt30CostRate = gt30CostRate;
    }

    public String getGt30CostRateStr() {
        return gt30CostRateStr;
    }

    public void setGt30CostRateStr(String gt30CostRateStr) {
        this.gt30CostRateStr = gt30CostRateStr;
    }

    public int getHoldDays() {
        return holdDays;
    }

    public void setHoldDays(int holdDays) {
        this.holdDays = holdDays;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public String getAssignRule() {
        return assignRule;
    }

    public void setAssignRule(String assignRule) {
        this.assignRule = assignRule;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public double getUnit() {
        return unit;
    }

    public void setUnit(double unit) {
        this.unit = unit;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public String getChargeTips() {
        return chargeTips;
    }

    public void setChargeTips(String chargeTips) {
        this.chargeTips = chargeTips;
    }
}
