package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by duo.chen on 2016/6/3.
 */

public class Iwjwh5UrlResponse extends Response {

    private String helpCenterUrl = ""; // 帮助中心URL
    private String loanHelpUrl = ""; // 贷款帮助URL
    private String safecardExplainUrl = ""; // 安全卡说明页URL
    private String supportcardsByAllUrl = ""; // 显示所支持银行卡(银行卡和信用卡)URL
    private String supportcardsByBankUrl = ""; // 显示所支持银行卡页URL
    private String supportcardsByCreditUrl = ""; // 显示所支持信用卡页URL
    private String accountProtocol = "";//爱屋吉屋电子账户服务协议
    private String ailicaiProtocol = "";//爱理财平台服务协议
    private String defaultNewHouseUrl = "";//新房h5默认地址
    private String cardUrl = ""; // 卡券h5 URL
    private String tradeEnsureCardUrl = ""; //交易保障服务卡详情页url
    private String ailicaiUrl = "";//理财首页地址
    private String orderH5Url = "";//订单html5入口 eg.http://m.iwjwtest.com/myorder
    private String orderDetailH5Url = "";//订单详情html5入口 eg.http://m.iwjwtest.com/myorder/detail
    private String aboutUrl; //关于爱屋吉屋页Url
    private String alicaiType; //爱理财开启方式 0-关闭 1-理财首页 2-资产页
    private String rentHouseCommissionUrl; //出租房源委托页面极爱宅介绍页
    private String rentOrderDetailH5Url = "";//租房订单详情html5入口
    private String rentBillDetailH5Url = "";//租房账单详情html5入口

    private String payRentH5Url = ""; // 交房租列表h5Url地址
    private String payRentBillDetailH5Url = ""; // 交房租账单详情h5Url地址

    private String tiyanbaoDetailUrl; // 体验宝详情URL
    private String productDetailUrl; //房产宝详情ur

    private String rebateUrl = ""; // 返利券详情url
    private String brandShareUrl = ""; // 品牌公寓分享地址，由客户端判断，为空时表示无分享 6.6新增

    public String getHelpCenterUrl() {
        return helpCenterUrl;
    }

    public void setHelpCenterUrl(String helpCenterUrl) {
        this.helpCenterUrl = helpCenterUrl;
    }

    public String getLoanHelpUrl() {
        return loanHelpUrl;
    }

    public void setLoanHelpUrl(String loanHelpUrl) {
        this.loanHelpUrl = loanHelpUrl;
    }

    public String getSafecardExplainUrl() {
        return safecardExplainUrl;
    }

    public void setSafecardExplainUrl(String safecardExplainUrl) {
        this.safecardExplainUrl = safecardExplainUrl;
    }

    public String getSupportcardsByAllUrl() {
        return supportcardsByAllUrl;
    }

    public void setSupportcardsByAllUrl(String supportcardsByAllUrl) {
        this.supportcardsByAllUrl = supportcardsByAllUrl;
    }

    public String getSupportcardsByBankUrl() {
        return supportcardsByBankUrl;
    }

    public void setSupportcardsByBankUrl(String supportcardsByBankUrl) {
        this.supportcardsByBankUrl = supportcardsByBankUrl;
    }

    public String getSupportcardsByCreditUrl() {
        return supportcardsByCreditUrl;
    }

    public void setSupportcardsByCreditUrl(String supportcardsByCreditUrl) {
        this.supportcardsByCreditUrl = supportcardsByCreditUrl;
    }

    public String getAccountProtocol() {
        return accountProtocol;
    }

    public void setAccountProtocol(String accountProtocol) {
        this.accountProtocol = accountProtocol;
    }

    public String getAilicaiProtocol() {
        return ailicaiProtocol;
    }

    public void setAilicaiProtocol(String ailicaiProtocol) {
        this.ailicaiProtocol = ailicaiProtocol;
    }

    public String getDefaultNewHouseUrl() {
        return defaultNewHouseUrl;
    }

    public void setDefaultNewHouseUrl(String defaultNewHouseUrl) {
        this.defaultNewHouseUrl = defaultNewHouseUrl;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    public String getTradeEnsureCardUrl() {
        return tradeEnsureCardUrl;
    }

    public void setTradeEnsureCardUrl(String tradeEnsureCardUrl) {
        this.tradeEnsureCardUrl = tradeEnsureCardUrl;
    }

    public String getAilicaiUrl() {
        return ailicaiUrl;
    }

    public void setAilicaiUrl(String ailicaiUrl) {
        this.ailicaiUrl = ailicaiUrl;
    }

    public String getRentHouseCommissionUrl() {
        return rentHouseCommissionUrl;
    }

    public void setRentHouseCommissionUrl(String rentHouseCommissionUrl) {
        this.rentHouseCommissionUrl = rentHouseCommissionUrl;
    }

    public String getOrderH5Url() {
        return orderH5Url;
    }

    public void setOrderH5Url(String orderH5Url) {
        this.orderH5Url = orderH5Url;
    }

    public String getOrderDetailH5Url() {
        return orderDetailH5Url;
    }

    public void setOrderDetailH5Url(String orderDetailH5Url) {
        this.orderDetailH5Url = orderDetailH5Url;
    }

    public String getRentOrderDetailH5Url() {
        return rentOrderDetailH5Url;
    }

    public void setRentOrderDetailH5Url(String rentOrderDetailH5Url) {
        this.rentOrderDetailH5Url = rentOrderDetailH5Url;
    }

    public String getRentBillDetailH5Url() {
        return rentBillDetailH5Url;
    }

    public void setRentBillDetailH5Url(String rentBillDetailH5Url) {
        this.rentBillDetailH5Url = rentBillDetailH5Url;
    }

    public String getAboutUrl() {
        return aboutUrl;
    }

    public void setAboutUrl(String aboutUrl) {
        this.aboutUrl = aboutUrl;
    }

    public String getAlicaiType() {
        return alicaiType;
    }

    public void setAlicaiType(String alicaiType) {
        this.alicaiType = alicaiType;
    }

    public String getPayRentH5Url() {
        return payRentH5Url;
    }

    public void setPayRentH5Url(String payRentH5Url) {
        this.payRentH5Url = payRentH5Url;
    }

    public String getPayRentBillDetailH5Url() {
        return payRentBillDetailH5Url;
    }

    public void setPayRentBillDetailH5Url(String payRentBillDetailH5Url) {
        this.payRentBillDetailH5Url = payRentBillDetailH5Url;
    }

    public String getTiyanbaoDetailUrl() {
        return tiyanbaoDetailUrl;
    }

    public void setTiyanbaoDetailUrl(String tiyanbaoDetailUrl) {
        this.tiyanbaoDetailUrl = tiyanbaoDetailUrl;
    }

    public String getProductDetailUrl() {
        return productDetailUrl;
    }

    public void setProductDetailUrl(String productDetailUrl) {
        this.productDetailUrl = productDetailUrl;
    }

    public String getRebateUrl() {
        return rebateUrl;
    }

    public void setRebateUrl(String rebateUrl) {
        this.rebateUrl = rebateUrl;
    }

    public String getBrandShareUrl() {
        return brandShareUrl;
    }

    public void setBrandShareUrl(String brandShareUrl) {
        this.brandShareUrl = brandShareUrl;
    }
}
