package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * 体验宝购买页初始化数据
 * Created by liyanan on 16/8/15.
 */
public class BuyTiyanbaoInitResponse extends Response {
    private long activityId; // 活动Id
    private String activityName = ""; // 活动名称
    private double yearInterestRate; // 预计年化利率 小数表示，比如0.15表示15%
    private String yearInterestRateStr = ""; // 预计年化利率字符串
    private int horizon; // 投资期限，单位是天
    private String horizonStr = ""; // 投资期限，单位是天
    private int status = 1; // 1在售 2即将开始 3 已结束
    private int hasCoupon;// 是否持有体验金 1有 0 无
    private int couponId;//体验券Id
    private double couponAmount;// 体验券金额
    private String couponTitle = "";// 卡券名称 eg体验金XXX元
    private String dueDateStr = "";// 清零日期
    private int isOpen;//是否开户 1是 0否
    private int expiredDays;//收益有效天数
    private int hasPwd;//是否有密码 1是 0 否
    private String couponUrl = "";//选择卡券h5Url
    private long backDate;//收益回款时间       收益回款时间+收益有效天数 =过期时间

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public int getHorizon() {
        return horizon;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }

    public String getHorizonStr() {
        return horizonStr;
    }

    public void setHorizonStr(String horizonStr) {
        this.horizonStr = horizonStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHasCoupon() {
        return hasCoupon;
    }

    public void setHasCoupon(int hasCoupon) {
        this.hasCoupon = hasCoupon;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getDueDateStr() {
        return dueDateStr;
    }

    public void setDueDateStr(String dueDateStr) {
        this.dueDateStr = dueDateStr;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getExpiredDays() {
        return expiredDays;
    }

    public void setExpiredDays(int expiredDays) {
        this.expiredDays = expiredDays;
    }

    public int getHasPwd() {
        return hasPwd;
    }

    public void setHasPwd(int hasPwd) {
        this.hasPwd = hasPwd;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public long getBackDate() {
        return backDate;
    }

    public void setBackDate(long backDate) {
        this.backDate = backDate;
    }
}
