package com.ailicai.app.model.bean;

/**
 * Created by nanshan on 2017/5/8.
 */

public class Coin {

    private String pennyCode;//小钱编号
    private String pennyName;//小钱名称
    private String applyUser;//借款人
    private double applyAmount;//融资金额
    private String bidAmount;//借款人
    private double biddableAmount;//剩余(可投资)金额
    private String userBidInterest; //用户投资利息收入
    private String protocolUrl ; //原小钱信息跳转地址
    private String infoUrl ; //借款合同跳转地址


    public String getPennyCode() {
        return pennyCode;
    }

    public void setPennyCode(String pennyCode) {
        this.pennyCode = pennyCode;
    }

    public String getPennyName() {
        return pennyName;
    }

    public void setPennyName(String pennyName) {
        this.pennyName = pennyName;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public double getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(double applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }
    public double getBiddableAmount() {
        return biddableAmount;
    }

    public void setBiddableAmount(double biddableAmount) {
        this.biddableAmount = biddableAmount;
    }

    public String getUserBidInterest() {
        return userBidInterest;
    }

    public void setUserBidInterest(String userBidInterest) {
        this.userBidInterest = userBidInterest;
    }

    public String getProtocolUrl() {
        return protocolUrl;
    }

    public void setProtocolUrl(String protocolUrl) {
        this.protocolUrl = protocolUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }
}
