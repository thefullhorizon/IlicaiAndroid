package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by nanshan on 8/7/2017.
 */

public class GetAppropriateCouponResponse extends Response {

    private int voucherId;//卡券Id
    private int voucherType;//卡券类型(73-吉爱财加息券,74-吉爱财返金券)
    private int amountCent;//金额//单位：分
    private double addRate;//利率
    private int addRateDay;//加息天数
    private int minAmountCent;//最低投资金额限制

    private int availableVoucherNumber;

    private String amountCentString="";// 金额字符串(返金券使用)

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public int getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(int voucherType) {
        this.voucherType = voucherType;
    }

    public int getAmountCent() {
        return amountCent;
    }

    public void setAmountCent(int amountCent) {
        this.amountCent = amountCent;
    }

    public double getAddRate() {
        return addRate;
    }

    public void setAddRate(double addRate) {
        this.addRate = addRate;
    }

    public int getAddRateDay() {
        return addRateDay;
    }

    public void setAddRateDay(int addRateDay) {
        this.addRateDay = addRateDay;
    }

    public int getMinAmountCent() {
        return minAmountCent;
    }

    public void setMinAmountCent(int minAmountCent) {
        this.minAmountCent = minAmountCent;
    }

    public int getAvailableVoucherNumber() {
        return availableVoucherNumber;
    }

    public void setAvailableVoucherNumber(int availableVoucherNumber) {
        this.availableVoucherNumber = availableVoucherNumber;
    }

    public String getAmountCentString() {
        return amountCentString;
    }

    public void setAmountCentString(String amountCentString) {
        this.amountCentString = amountCentString;
    }
}
