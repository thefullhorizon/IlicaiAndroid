package com.ailicai.app.common.support;

import java.io.Serializable;

/**
 * Created by Gerry on 2016/1/26.
 */
public class SupportFinance implements Serializable {

    // 爱理财界面使用
    private String safecardExplainUrl = "http://m.iwjw.com/licai/safecard"; // 安全卡说明页URL
    private String accountProtocol = "http://m.iwjw.com/licai/protocal/eaccount";//爱屋吉屋电子账户服务协议
    private String ailicaiProtocol = "http://m.iwjw.com/licai/protocal/service";//爱理财平台服务协议
    private String openAccountDebitMsg = "开户需从该卡扣除1.00元，用于验卡，扣款将充入钱包"; // 开户扣款提示文案
    private String setSafeCardMsg = "设值安全卡需从该卡扣除0.01元，用于验卡，扣款将转入钱包"; // 设值安全卡扣款提示文案
    private String alicaiType; //爱理财开启方式 0-关闭 1-理财首页 2-资产页
    private String tiyanbaoDetailUrl = ""; // 体验宝详情URL
    private String productDetailUrl = ""; //房产宝详情ur

/*    private String brandRentUrl = ""; //品牌公寓banner跳转url(列表 、详情、地图一致)
    private String brandRentPic4List = ""; //品牌公寓banner图片地址 列表
    private String brandRentPic4Detail = ""; //品牌公寓banner图片地址 详情
    private String brandRentPic4Map = ""; //品牌公寓banner图片地址 4地图*/

    public String getSafecardExplainUrl() {
        return safecardExplainUrl;
    }

    public void setSafecardExplainUrl(String safecardExplainUrl) {
        this.safecardExplainUrl = safecardExplainUrl;
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

    public String getOpenAccountDebitMsg() {
        return openAccountDebitMsg;
    }

    public void setOpenAccountDebitMsg(String openAccountDebitMsg) {
        this.openAccountDebitMsg = openAccountDebitMsg;
    }

    public String getSetSafeCardMsg() {
        return setSafeCardMsg;
    }

    public void setSetSafeCardMsg(String setSafeCardMsg) {
        this.setSafeCardMsg = setSafeCardMsg;
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
/*
    public String getBrandRentUrl() {
        return brandRentUrl;
    }

    public void setBrandRentUrl(String brandRentUrl) {
        this.brandRentUrl = brandRentUrl;
    }

    public String getBrandRentPic4List() {
        return brandRentPic4List;
    }

    public void setBrandRentPic4List(String brandRentPic4List) {
        this.brandRentPic4List = brandRentPic4List;
    }

    public String getBrandRentPic4Detail() {
        return brandRentPic4Detail;
    }

    public void setBrandRentPic4Detail(String brandRentPic4Detail) {
        this.brandRentPic4Detail = brandRentPic4Detail;
    }

    public String getBrandRentPic4Map() {
        return brandRentPic4Map;
    }

    public void setBrandRentPic4Map(String brandRentPic4Map) {
        this.brandRentPic4Map = brandRentPic4Map;
    }*/
}
