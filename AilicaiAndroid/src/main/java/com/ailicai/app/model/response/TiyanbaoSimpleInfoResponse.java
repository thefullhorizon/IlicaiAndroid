package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Administrator on 2016/8/17.
 */
public class TiyanbaoSimpleInfoResponse extends Response {

    private String productId=""; // 活动Id
    private String productName = ""; // 活动名称
    private String yearInterestRateStr = ""; // 预计年化利率字符串
    private String horizonStr = ""; // 投资期限，单位是天
    private int horizon; // 投资期限
    private int passDay;//距申购日天数

    private String bidAmount = "";// 体验金额度
    private String backAmount;// 预计回款、收益
    private String backTime = "";// 回款日期
    private String subDateMMDD = "";// 申购日
    private String interestDateMMDD = "";// 起息日
    private String expireDateMMDD = "";// 到期日
    private int status;//状态 0未发放 1已发放 2发放失败 3已过期
    private String bottomTips = "";// 底部提示
    private int hasValidLimit;//是否有有效天数限制

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

    public int getHorizon() {
        return horizon;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }

    public int getPassDay() {
        return passDay;
    }

    public void setPassDay(int passDay) {
        this.passDay = passDay;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(String backAmount) {
        this.backAmount = backAmount;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    public String getSubDateMMDD() {
        return subDateMMDD;
    }

    public void setSubDateMMDD(String subDateMMDD) {
        this.subDateMMDD = subDateMMDD;
    }

    public String getInterestDateMMDD() {
        return interestDateMMDD;
    }

    public void setInterestDateMMDD(String interestDateMMDD) {
        this.interestDateMMDD = interestDateMMDD;
    }

    public String getExpireDateMMDD() {
        return expireDateMMDD;
    }

    public void setExpireDateMMDD(String expireDateMMDD) {
        this.expireDateMMDD = expireDateMMDD;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBottomTips() {
        return bottomTips;
    }

    public void setBottomTips(String bottomTips) {
        this.bottomTips = bottomTips;
    }

    public int getHasValidLimit() {
        return hasValidLimit;
    }

    public void setHasValidLimit(int hasValidLimit) {
        this.hasValidLimit = hasValidLimit;
    }
}
