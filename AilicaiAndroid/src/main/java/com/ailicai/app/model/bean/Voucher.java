package com.ailicai.app.model.bean;

/**
 * 现金券
 * Created by liyanan on 16/3/8.
 */
public class Voucher {
    private int voucherId; //卡券ID
    private String voucherName; //卡券名称
    private String userTimeFrom; //卡券有效期开始时间 eg.2016-03-09 10:25
    private String userTimeTo; //卡券有效期结束时间 eg.2016-03-10 10:24
    private int amountCent; //金额，单位分
    private int voucherType; //卡券类型 67-爱理财现金券；68-爱理财抵扣券；73-爱理财加息券; 83-权益卡
    private String voucherTypeStr = ""; //卡券类型文案
    private int status; //状态 0-不可用；1-正常；2-已使用；3-已过期；4-已作废; 5-未到使用时间
    private double addRate; //加息比例 小数表示，比如1.5表示1.5%
    private String simpleDesc;//类型下描述
    private String bottomDesc;//加息券底部描述
    private int addRateDay;//加息天数//-1代表不限

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getUserTimeFrom() {
        return userTimeFrom;
    }

    public void setUserTimeFrom(String userTimeFrom) {
        this.userTimeFrom = userTimeFrom;
    }

    public String getUserTimeTo() {
        return userTimeTo;
    }

    public void setUserTimeTo(String userTimeTo) {
        this.userTimeTo = userTimeTo;
    }

    public int getAmountCent() {
        return amountCent;
    }

    public void setAmountCent(int amountCent) {
        this.amountCent = amountCent;
    }

    public int getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(int voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherTypeStr() {
        return voucherTypeStr;
    }

    public void setVoucherTypeStr(String voucherTypeStr) {
        this.voucherTypeStr = voucherTypeStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getAddRate() {
        return addRate;
    }

    public void setAddRate(double addRate) {
        this.addRate = addRate;
    }

    public String getSimpleDesc() {
        return simpleDesc;
    }

    public void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc;
    }

    public String getBottomDesc() {
        return bottomDesc;
    }

    public void setBottomDesc(String bottomDesc) {
        this.bottomDesc = bottomDesc;
    }

    public int getAddRateDay() {
        return addRateDay;
    }

    public void setAddRateDay(int addRateDay) {
        this.addRateDay = addRateDay;
    }
}
