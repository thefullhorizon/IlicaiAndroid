package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * 房产宝项目详情response
 * Created by liyanan on 16/4/11.
 */
public class FinanceProductDetailResponse extends Response {
    private int status; //1-成功
    private String productId = ""; //产品id
    private String userName = ""; // 用户姓名
    private String gender = ""; //性别
    private String maritaltatus = ""; //婚姻状况
    private String education = "";//教育程度
    private String companyType = "";// 单位性质
    private String houseAddress = "";//房产地址
    private String space = "";// 面积
    private String price = "";// 总价
    private String tradeStatus = "";// 交易状态

    private String productType = "0";// 标的类型 0-p2p标的  1-债券转让
    private String protocolUrl = "";//借款协议URL

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritaltatus() {
        return maritaltatus;
    }

    public void setMaritaltatus(String maritaltatus) {
        this.maritaltatus = maritaltatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProtocolUrl() {
        return protocolUrl;
    }

    public void setProtocolUrl(String protocolUrl) {
        this.protocolUrl = protocolUrl;
    }
}
