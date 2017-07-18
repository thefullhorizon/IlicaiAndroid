package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * 购买体验宝response
 * Created by liyanan on 16/8/15.
 */
public class BuyTiyanbaoResponse extends Response {
    private int remainingCnt;//密码失败剩余
    private int isOpen;//是否开户 1是 0否
    private String backDateStr = "";//回款日期
    private String dueDateStr = "";//清零日期
    private int hasValidLimit;//是否有有效天数限制
    private int couponId;//卡券id   用于跳转到详情

    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public String getBackDateStr() {
        return backDateStr;
    }

    public void setBackDateStr(String backDateStr) {
        this.backDateStr = backDateStr;
    }

    public String getDueDateStr() {
        return dueDateStr;
    }

    public void setDueDateStr(String dueDateStr) {
        this.dueDateStr = dueDateStr;
    }

    public int getHasValidLimit() {
        return hasValidLimit;
    }

    public void setHasValidLimit(int hasValidLimit) {
        this.hasValidLimit = hasValidLimit;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }
}
