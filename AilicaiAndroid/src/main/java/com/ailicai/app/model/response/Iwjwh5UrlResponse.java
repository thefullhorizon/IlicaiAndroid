package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by duo.chen on 2016/6/3.
 */

public class Iwjwh5UrlResponse extends Response {

    private String bankCardUrl; //银行卡
    private String cardDescribeUrl = ""; //卡券使用说明
    private String rebateUrl = ""; // 返利券详情url

    private String brandShareUrl = ""; //分享链接，由客户端判断是否使用，为空时表示分享无活动
    private String h5AccountUrl = ""; // 开户h5地址
    private String commerceRevealUrl = ""; //平台工商信息披露Url
    private String operationReportUrl = ""; //运营信息披露Url
    private String dzgEntryUrl = ""; //贷总管入口Url

    private String helpCenterUrl = ""; // 帮助中心URL
    private String loanHelpUrl = ""; // 贷款帮助URL
    private String safecardExplainUrl = ""; // 银行卡说明页URL
    private String supportcardsByAllUrl = ""; // 显示所支持银行卡(银行卡和信用卡)URL
    private String supportcardsByBankUrl = ""; // 显示所支持银行卡页URL
    private String supportcardsByCreditUrl = ""; // 显示所支持信用卡页URL
    private String accountProtocol = "";//爱屋吉屋电子账户服务协议
    private String ailicaiProtocol = "";//吉爱财平台服务协议
    private String cardUrl = ""; // 卡券h5 URL
    private String ailicaiUrl = "";//理财首页地址
    private String aboutUrl; //关于爱屋吉屋页Url
    private String alicaiType; //吉爱财开启方式 0-关闭 1-理财首页 2-资产页
    private String tiyanbaoDetailUrl; // 体验宝详情URL
    private String productDetailUrl; //房产宝详情ur

    // 独立app新增 首页(porosWebUrl)，银行卡(bankCardUrl) ，推荐(recommondUrl)，货币基金（monetaryFundUrl）  网贷(netLoanUrl)，转让(transferUrl) 关于吉爱财（aboutAiLiCaiUrl）,开户（openAccountUrl）
    private String porosWebUrl; // 首页
    private String recommondUrl; // 首页 投资中 四个tab之一推荐
    private String monetaryFundUrl; // 首页 投资中 四个tab之一货基
    private String netLoanUrl; // 首页 投资中 四个tab之一网贷
    private String transferUrl; // 首页 投资中 四个tab之一转让
    private String aboutAiLiCaiUrl;// "我的"里面 关于吉爱财
    private String inviteUrl;
    private String openAccountUrl;// 开户
    private String bindNewOpenAccountUrl; // 重新绑定银行卡

    private String safeBankSupport = "";//银行卡限额列表地址
    private String autoBidNoteH5Url          = ""; // 自动投标说明Url
    private String memberLevleH5Url       = ""; // 会员等级说明url

    public String getBankCardUrl() {
        return bankCardUrl;
    }

    public void setBankCardUrl(String bankCardUrl) {
        this.bankCardUrl = bankCardUrl;
    }

    public String getCardDescribeUrl() {
        return cardDescribeUrl;
    }

    public void setCardDescribeUrl(String cardDescribeUrl) {
        this.cardDescribeUrl = cardDescribeUrl;
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

    public String getH5AccountUrl() {
        return h5AccountUrl;
    }

    public void setH5AccountUrl(String h5AccountUrl) {
        this.h5AccountUrl = h5AccountUrl;
    }

    public String getCommerceRevealUrl() {
        return commerceRevealUrl;
    }

    public void setCommerceRevealUrl(String commerceRevealUrl) {
        this.commerceRevealUrl = commerceRevealUrl;
    }

    public String getOperationReportUrl() {
        return operationReportUrl;
    }

    public void setOperationReportUrl(String operationReportUrl) {
        this.operationReportUrl = operationReportUrl;
    }

    public String getDzgEntryUrl() {
        return dzgEntryUrl;
    }

    public void setDzgEntryUrl(String dzgEntryUrl) {
        this.dzgEntryUrl = dzgEntryUrl;
    }

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

    public String getPorosWebUrl() {
        return porosWebUrl;
    }

    public void setPorosWebUrl(String porosWebUrl) {
        this.porosWebUrl = porosWebUrl;
    }

    public String getRecommondUrl() {
        return recommondUrl;
    }

    public void setRecommondUrl(String recommondUrl) {
        this.recommondUrl = recommondUrl;
    }

    public String getMonetaryFundUrl() {
        return monetaryFundUrl;
    }

    public void setMonetaryFundUrl(String monetaryFundUrl) {
        this.monetaryFundUrl = monetaryFundUrl;
    }

    public String getNetLoanUrl() {
        return netLoanUrl;
    }

    public void setNetLoanUrl(String netLoanUrl) {
        this.netLoanUrl = netLoanUrl;
    }

    public String getTransferUrl() {
        return transferUrl;
    }

    public void setTransferUrl(String transferUrl) {
        this.transferUrl = transferUrl;
    }

    public String getAboutAiLiCaiUrl() {
        return aboutAiLiCaiUrl;
    }

    public void setAboutAiLiCaiUrl(String aboutAiLiCaiUrl) {
        this.aboutAiLiCaiUrl = aboutAiLiCaiUrl;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }

    public String getOpenAccountUrl() {
        return openAccountUrl;
    }

    public void setOpenAccountUrl(String openAccountUrl) {
        this.openAccountUrl = openAccountUrl;
    }

    public String getBindNewOpenAccountUrl() {
        return bindNewOpenAccountUrl;
    }

    public void setBindNewOpenAccountUrl(String bindNewOpenAccountUrl) {
        this.bindNewOpenAccountUrl = bindNewOpenAccountUrl;
    }

    public String getSafeBankSupport() {
        return safeBankSupport;
    }

    public void setSafeBankSupport(String safeBankSupport) {
        this.safeBankSupport = safeBankSupport;
    }

    public String getAutoBidNoteH5Url() {
        return autoBidNoteH5Url;
    }

    public void setAutoBidNoteH5Url(String autoBidNoteH5Url) {
        this.autoBidNoteH5Url = autoBidNoteH5Url;
    }

    public String getMemberLevleH5Url() {
        return memberLevleH5Url;
    }

    public void setMemberLevleH5Url(String memberLevleH5Url) {
        this.memberLevleH5Url = memberLevleH5Url;
    }
}
