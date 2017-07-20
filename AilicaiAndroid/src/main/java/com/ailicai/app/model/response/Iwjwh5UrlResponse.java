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
    private String cardUrl = ""; // 卡券h5 URL
    private String ailicaiUrl = "";//理财首页地址
    private String aboutUrl; //关于爱屋吉屋页Url
    private String alicaiType; //爱理财开启方式 0-关闭 1-理财首页 2-资产页
    private String tiyanbaoDetailUrl; // 体验宝详情URL
    private String productDetailUrl; //房产宝详情ur

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

    public String getCardUrl() {
        return cardUrl;
    }

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    public String getAilicaiUrl() {
        return ailicaiUrl;
    }

    public void setAilicaiUrl(String ailicaiUrl) {
        this.ailicaiUrl = ailicaiUrl;
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
}
