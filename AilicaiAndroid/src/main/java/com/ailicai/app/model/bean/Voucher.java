package com.ailicai.app.model.bean;

import java.util.List;

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
    private int voucherType; //卡券类型 67-爱理财现金券；68-爱理财抵扣券；73-爱理财加息券; 74-爱理财返金券 83-权益卡
    private String voucherTypeStr = ""; //卡券类型文案
    private int status; //状态 0-不可用；1-正常；2-已使用；3-已过期；4-已作废; 5-未到使用时间
    private double addRate; //加息比例 小数表示，比如1.5表示1.5%
    private String simpleDesc;//类型下描述
    private String bottomDesc;//加息券底部描述
    private int addRateDay;//加息天数//-1代表不限

    private List<Integer> productTypes;// 可使用的标的类型限制73-房产宝,74-小钱袋
    private int minAmountCent;// 最低投资金额限制
    private int leftValidDays;// 剩余天数剩余
    private String leftValidDayString=""; //剩余天数描述
    private String useRange="";//投资标的限制描述
    private String minAmountCentString="";//最低投资金额限制文案

    private String amountCentString="";// 金额字符串(返金券使用)

    private boolean simpleDescRed ; // 类型描述是否标红
    private boolean useRangeRed ; // 卡券所使用的标的限制描述是否标红
    private boolean minAmountCentStringRed ; // 最低投资金额是否标红

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


    public List<Integer> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<Integer> productTypes) {
        this.productTypes = productTypes;
    }

    public int getMinAmountCent() {
        return minAmountCent;
    }

    public void setMinAmountCent(int minAmountCent) {
        this.minAmountCent = minAmountCent;
    }

    public int getLeftValidDays() {
        return leftValidDays;
    }

    public void setLeftValidDays(int leftValidDays) {
        this.leftValidDays = leftValidDays;
    }

    public String getLeftValidDayString() {
        return leftValidDayString;
    }

    public void setLeftValidDayString(String leftValidDayString) {
        this.leftValidDayString = leftValidDayString;
    }

    public String getUseRange() {
        return useRange;
    }

    public void setUseRange(String useRange) {
        this.useRange = useRange;
    }

    public String getMinAmountCentString() {
        return minAmountCentString;
    }

    public void setMinAmountCentString(String minAmountCentString) {
        this.minAmountCentString = minAmountCentString;
    }

    public String getAmountCentString() {
        return amountCentString;
    }

    public void setAmountCentString(String amountCentString) {
        this.amountCentString = amountCentString;
    }

    public boolean isSimpleDescRed() {
        return simpleDescRed;
    }

    public void setSimpleDescRed(boolean simpleDescRed) {
        this.simpleDescRed = simpleDescRed;
    }

    public boolean isUseRangeRed() {
        return useRangeRed;
    }

    public void setUseRangeRed(boolean useRangeRed) {
        this.useRangeRed = useRangeRed;
    }

    public boolean isMinAmountCentStringRed() {
        return minAmountCentStringRed;
    }

    public void setMinAmountCentStringRed(boolean minAmountCentStringRed) {
        this.minAmountCentStringRed = minAmountCentStringRed;
    }
}
