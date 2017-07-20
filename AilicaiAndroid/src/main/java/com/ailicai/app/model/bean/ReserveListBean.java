package com.ailicai.app.model.bean;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Owen on 2016/5/25
 */
public class ReserveListBean extends Response {

    private String productId = ""; //理财 产品Id
    private String yearInterestRateStr; // 预计年化利率字符串
    private String horizonStr; // 投资期限，单位是天
    private String bidAmountStr; // 投资金额字符串
    private String customerName;//借款方

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getBidAmountStr() {
        return bidAmountStr;
    }

    public void setBidAmountStr(String bidAmountStr) {
        this.bidAmountStr = bidAmountStr;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
