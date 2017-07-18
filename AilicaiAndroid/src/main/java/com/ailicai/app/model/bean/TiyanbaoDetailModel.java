package com.ailicai.app.model.bean;

/**
 * Created by Administrator on 2016/8/16.
 */
public class TiyanbaoDetailModel {
    private String productId=""; // 活动Id
    private String productName = ""; // 活动名称
    private String yearInterestRateStr = ""; // 预计年化利率字符串
    private String horizonStr = ""; // 投资期限，单位是天
    private String backAmount="";// 预计回款、收益
    private String backDateStr = "";// 回款日期
    private long couponId;//卡券id

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

    public String getYearInterestRateStr() {
        return yearInterestRateStr;
    }

    public void setYearInterestRateStr(String yearInterestRateStr) {
        this.yearInterestRateStr = yearInterestRateStr;
    }

    public String getHorizonStr() {
        return horizonStr;
    }

    public void setHorizonStr(String horizonStr) {
        this.horizonStr = horizonStr;
    }

    public String getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(String backAmount) {
        this.backAmount = backAmount;
    }

    public String getBackDateStr() {
        return backDateStr;
    }

    public void setBackDateStr(String backDateStr) {
        this.backDateStr = backDateStr;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }
}
