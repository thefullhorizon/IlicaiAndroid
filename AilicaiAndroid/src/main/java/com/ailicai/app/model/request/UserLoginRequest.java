package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 登陆验证request模型
 *
 * @author hubin
 */
@RequestPath("/user/userLogin.rest")
public class UserLoginRequest extends Request {
    private String mobile; //手机号

    private String verifyCode; //验证码

    private String mobileSn; //手机设备唯一标识(或GUID)

    private String[] houseIds; //离线收藏的房源id

    private String[] rentOrSales; // 出租或出售

    private String idfa; //IDFA 广告id （暂时IOS用）

    private int fromPage; //登录来源页面（1-我的；2-房源详情(租房)；3-房源详情（二手房）；4-约看清单；5-看房日程；6-业主委托（2.5版本补上）； 7-委托列表）

    private String systemVer; //系统版本(登录日志用)
    private String netType; //网络类型 如：WIFI,mobile(登录日志用)
    private String support; //运营商(登录日志用)

    private String clientId;//客户端id，用于长连接 （5.1更新）

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getMobileSn() {
        return mobileSn;
    }

    public void setMobileSn(String mobileSn) {
        this.mobileSn = mobileSn;
    }

    public String[] getHouseIds() {
        return houseIds;
    }

    public void setHouseIds(String[] houseIds) {
        this.houseIds = houseIds;
    }

    public String[] getRentOrSales() {
        return rentOrSales;
    }

    public void setRentOrSales(String[] rentOrSales) {
        this.rentOrSales = rentOrSales;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public int getFromPage() {
        return fromPage;
    }

    public void setFromPage(int fromPage) {
        this.fromPage = fromPage;
    }

    public String getSystemVer() {
        return systemVer;
    }

    public void setSystemVer(String systemVer) {
        this.systemVer = systemVer;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
