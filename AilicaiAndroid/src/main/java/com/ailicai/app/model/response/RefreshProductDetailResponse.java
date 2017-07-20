package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * 房产宝详情页刷新response
 * Created by liyanan on 16/4/13.
 */
public class RefreshProductDetailResponse extends Response {
    private String productId = ""; //产品编号
    private int status; // 理财状态 1-正在募集；2-即将开售；3-已售罄
    private double biddableAmount; // 可投标金额
    private String biddableAmountStr = ""; // 可投标金额字符串
    private double hasBuyPrecent;//融资进度，小数表示，比如0.15表示15%

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getBiddableAmount() {
        return biddableAmount;
    }

    public void setBiddableAmount(double biddableAmount) {
        this.biddableAmount = biddableAmount;
    }

    public double getHasBuyPrecent() {
        return hasBuyPrecent;
    }

    public void setHasBuyPrecent(double hasBuyPrecent) {
        this.hasBuyPrecent = hasBuyPrecent;
    }

    public String getBiddableAmountStr() {
        return biddableAmountStr;
    }

    public void setBiddableAmountStr(String biddableAmountStr) {
        this.biddableAmountStr = biddableAmountStr;
    }
}
