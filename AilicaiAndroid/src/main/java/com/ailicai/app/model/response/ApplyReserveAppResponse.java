package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by David on 16/3/11.
 */
public class ApplyReserveAppResponse extends Response {

    private int errorCode = 0; //返回代码 0-正常 其他参考对应的errorCode定义
    private String message = "";//返回消息
    private String productId = ""; // 产品Id
    private double amount;//实际申购金额
    private String productName = ""; // 产品名称
    private String bidOrderNo = "";//购买预约订单编号

    private int remainingCnt=-1;

    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }
}
