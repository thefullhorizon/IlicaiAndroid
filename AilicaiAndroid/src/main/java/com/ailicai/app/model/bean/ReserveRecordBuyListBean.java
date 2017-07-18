package com.ailicai.app.model.bean;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Owen on 2016/4/18
 */
public class ReserveRecordBuyListBean extends Response {

    private String productId = ""; // 产品Id
    private String productName = ""; // 产品名称
    private String yearInterestRateStr; // 预计年化利率字符串
    private String horizonStr; // 投资期限，单位是天
    private String bidAmountStr; // 投资金额字符串
    private String bidTimeStr;//标的申购时间
    private int status;//投资状态 1-投标支付成功 2-成功购买 3-流标

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

    public String getBidAmountStr() {
        return bidAmountStr;
    }

    public void setBidAmountStr(String bidAmountStr) {
        this.bidAmountStr = bidAmountStr;
    }

    public String getBidTimeStr() {
        return bidTimeStr;
    }

    public void setBidTimeStr(String bidTimeStr) {
        this.bidTimeStr = bidTimeStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
