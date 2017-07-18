package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

public class ApplyAssignmentResponse extends Response {
    private int remainingCnt = 0; // 密码错误剩余次数
    private String productName = "";//转让产品名称
    private String productPrice ="";//转让价格
    private String beginDate ="";//转让提交日期
    private String endDate ="";//转让截止日期 提交日期+5天
    private String creditId ="";//债权id
    private String productDetailUrl ="";


    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getProductDetailUrl() {
        return productDetailUrl;
    }

    public void setProductDetailUrl(String productDetailUrl) {
        this.productDetailUrl = productDetailUrl;
    }
}
