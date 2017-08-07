package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by nanshan on 8/7/2017.
 */

public class GetAppropriateCouponResponse extends Response {

    private String voucherId;//卡券Id
    private int voucherType;//卡券类型(73-爱理财加息券,74-爱理财返金券)
    private String amountCent;//金额//单位：分
    private String addRate;//利率
    private String addRateDay;//加息天数

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public int getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(int voucherType) {
        this.voucherType = voucherType;
    }

    public String getAmountCent() {
        return amountCent;
    }

    public void setAmountCent(String amountCent) {
        this.amountCent = amountCent;
    }

    public String getAddRate() {
        return addRate;
    }

    public void setAddRate(String addRate) {
        this.addRate = addRate;
    }

    public String getAddRateDay() {
        return addRateDay;
    }

    public void setAddRateDay(String addRateDay) {
        this.addRateDay = addRateDay;
    }
}
