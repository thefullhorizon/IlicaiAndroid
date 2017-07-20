package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Product;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by duo.chen on 2016/1/7.
 */
public class RegularProductDetailResponse extends Response {

    private Product product;
    private String calculationDate; //计息日期
    private String endBuyTimeStr; //购买截止时间
    private String payBackTimeStr; //汇款时间
    private String buyMemo;//购买说明
    private String productMemo;// 产品详情
    private String productSpecMemo;//产品特别说明
    private String moreDetailUrl = "";
    private double availableBalance = 0;//账户可用余额
    private String reserveFund = ""; //准备金文案
    private String reserveFundMemoUrl;//准备金介绍页URL
    private List<String> buyMemoList;//购买说明（5.0版以后，list方式返回）
    private double lat;// 房产纬度
    private double lon;// 房产经度
    private String houseAddress = "";//房产地址
    private String interestVoucher = "";//加息券文案，为空字符串时标示无可用加息券
    private int isOpen;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(String calculationDate) {
        this.calculationDate = calculationDate;
    }

    public String getEndBuyTimeStr() {
        return endBuyTimeStr;
    }

    public void setEndBuyTimeStr(String endBuyTimeStr) {
        this.endBuyTimeStr = endBuyTimeStr;
    }

    public String getPayBackTimeStr() {
        return payBackTimeStr;
    }

    public void setPayBackTimeStr(String payBackTimeStr) {
        this.payBackTimeStr = payBackTimeStr;
    }

    public String getBuyMemo() {
        return buyMemo;
    }

    public void setBuyMemo(String buyMemo) {
        this.buyMemo = buyMemo;
    }

    public String getProductMemo() {
        return productMemo;
    }

    public void setProductMemo(String productMemo) {
        this.productMemo = productMemo;
    }

    public String getProductSpecMemo() {
        return productSpecMemo;
    }

    public void setProductSpecMemo(String productSpecMemo) {
        this.productSpecMemo = productSpecMemo;
    }

    public String getMoreDetailUrl() {
        return moreDetailUrl;
    }

    public void setMoreDetailUrl(String moreDetailUrl) {
        this.moreDetailUrl = moreDetailUrl;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getReserveFund() {
        return reserveFund;
    }

    public void setReserveFund(String reserveFund) {
        this.reserveFund = reserveFund;
    }

    public String getReserveFundMemoUrl() {
        return reserveFundMemoUrl;
    }

    public void setReserveFundMemoUrl(String reserveFundMemoUrl) {
        this.reserveFundMemoUrl = reserveFundMemoUrl;
    }

    public List<String> getBuyMemoList() {
        return buyMemoList;
    }

    public void setBuyMemoList(List<String> buyMemoList) {
        this.buyMemoList = buyMemoList;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getInterestVoucher() {
        return interestVoucher;
    }

    public void setInterestVoucher(String interestVoucher) {
        this.interestVoucher = interestVoucher;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }
}

