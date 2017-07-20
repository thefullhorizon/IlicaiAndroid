package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Product;
import com.huoqiu.framework.rest.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by David on 16/3/11.
 */
public class ReserveDetailResponse extends Response {
    private int errorCode = 0; //返回代码 0-正常 其他参考对应的errorCode定义
    private String message = "";//返回消息
    private double availableBalance;//账户可用余额
    private String availableBalanceMemo = ""; // 钱包余额展示文案
    private String moreDetailUrl = "";// 更多详情URL
    private String interestTimeStr = ""; //预计成交时间：预计{房产宝产品日期} 10:30成交。（预约中用）
    private long beginReserveTime; //开始预约时间，单位毫秒；即将开始预约时用
    private int status; //状态：1-即将开始预约 2-预约中 3-额度已满 4-预约已过期
    private String reserveAmtMemo = "";// 预约金额提示文案
    private int productNum; //可预约的产品数量（最大天数以内）
    private int productNum60; //60天以内可预约产品数量
    private String protocolUrl;//http://m.iwjwtest.com/licai/protocal/deduct
    private Product product; //产品详情
    private int bankLimit;// 银行卡支付限额

    private int productNum90; //90天以内可预约产品数量
    private int maxDays; //房产宝最大投资天数,大于90天时展示
    private List<ProductRate> rateList; //可预约房产宝利率列表

    private int reservedNum;//预约记录数量

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

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getMoreDetailUrl() {
        return moreDetailUrl;
    }

    public void setMoreDetailUrl(String moreDetailUrl) {
        this.moreDetailUrl = moreDetailUrl;
    }

    public String getInterestTimeStr() {
        return interestTimeStr;
    }

    public void setInterestTimeStr(String interestTimeStr) {
        this.interestTimeStr = interestTimeStr;
    }

    public long getBeginReserveTime() {
        return beginReserveTime;
    }

    public void setBeginReserveTime(long beginReserveTime) {
        this.beginReserveTime = beginReserveTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getAvailableBalanceMemo() {
        return availableBalanceMemo;
    }

    public void setAvailableBalanceMemo(String availableBalanceMemo) {
        this.availableBalanceMemo = availableBalanceMemo;
    }

    public String getReserveAmtMemo() {
        return reserveAmtMemo;
    }

    public void setReserveAmtMemo(String reserveAmtMemo) {
        this.reserveAmtMemo = reserveAmtMemo;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public int getProductNum60() {
        return productNum60;
    }

    public void setProductNum60(int productNum60) {
        this.productNum60 = productNum60;
    }

    public String getProtocolUrl() {
        return protocolUrl;
    }

    public void setProtocolUrl(String protocolUrl) {
        this.protocolUrl = protocolUrl;
    }

    public int getBankLimit() {
        return bankLimit;
    }

    public void setBankLimit(int bankLimit) {
        this.bankLimit = bankLimit;
    }

    public int getProductNum90() {
        return productNum90;
    }

    public void setProductNum90(int productNum90) {
        this.productNum90 = productNum90;
    }

    public int getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    public List<ProductRate> getRateList() {
        return rateList;
    }

    public void setRateList(List<ProductRate> rateList) {
        this.rateList = rateList;
    }

    public int getReservedNum() {
        return reservedNum;
    }

    public void setReservedNum(int reservedNum) {
        this.reservedNum = reservedNum;
    }

    public static class ProductRate implements Serializable {
        private int term; //投资期限
        private String rate; //利率
        private String productDetailUrl;//房产包详情页地址

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getProductDetailUrl() {
            return productDetailUrl;
        }

        public void setProductDetailUrl(String productDetailUrl) {
            this.productDetailUrl = productDetailUrl;
        }
    }

}
