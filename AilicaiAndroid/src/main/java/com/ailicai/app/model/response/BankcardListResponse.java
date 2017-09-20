package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.ActivityModel;
import com.ailicai.app.model.bean.BankcardModel;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by Tangxiaolong on 2016/2/17.
 */
public class BankcardListResponse extends Response {
    private boolean showBkCd; // 银行卡支付时是否显示银行卡
    private boolean showWallet; // 是否显示钱包 true-显示 false-不显示
    private String cardLimitSolutionUrl; // 银行卡支付时卡限额表-解决方案URL
    private int hasSafeCard = 0; // 是否已绑定银行卡  0:否，1:是
    private int isOpenAccount = 0; // 判断是否是否开户 0:否，1:已开户，2：开户中
    private int isSetPayPwd = 0; // 判断是否设置支付密码 0:否，1:是
    private int isRealNameVerify = 0; // 是否通过实名认证 0:否，1:是
    private int isBinDebitCard = 0; // 是否已绑定借记卡 0:否，1:是
    private double availableBalance;//账户可用余额 字符串格式，单位元
    private String yearInterestRate = ""; // 钱包预计年化利率文案
    private ActivityModel activity; //活动信息
    private int userType;//用户类型 1:业主，2：客户

    // V6.7增加 服务器配置是否支付时候显示微信和支付宝
    private boolean showWechat; // 是否显示微信支付true-显示 false-不显示
    private boolean showAlipay; // 是否显示支付宝支付true-显示 false-不显示

    private List<BankcardModel> bankcardList;//银行卡列表

    public boolean isShowBkCd() {
        return showBkCd;
    }

    public void setShowBkCd(boolean showBkCd) {
        this.showBkCd = showBkCd;
    }

    public boolean isShowWallet() {
        return showWallet;
    }

    public void setShowWallet(boolean showWallet) {
        this.showWallet = showWallet;
    }

    public String getCardLimitSolutionUrl() {
        return cardLimitSolutionUrl;
    }

    public void setCardLimitSolutionUrl(String cardLimitSolutionUrl) {
        this.cardLimitSolutionUrl = cardLimitSolutionUrl;
    }

    public int getHasSafeCard() {
        return hasSafeCard;
    }

    public void setHasSafeCard(int hasSafeCard) {
        this.hasSafeCard = hasSafeCard;
    }

    public int getIsOpenAccount() {
        return isOpenAccount;
    }

    public void setIsOpenAccount(int isOpenAccount) {
        this.isOpenAccount = isOpenAccount;
    }

    public int getIsSetPayPwd() {
        return isSetPayPwd;
    }

    public void setIsSetPayPwd(int isSetPayPwd) {
        this.isSetPayPwd = isSetPayPwd;
    }

    public int getIsRealNameVerify() {
        return isRealNameVerify;
    }

    public void setIsRealNameVerify(int isRealNameVerify) {
        this.isRealNameVerify = isRealNameVerify;
    }

    public int getIsBinDebitCard() {
        return isBinDebitCard;
    }

    public void setIsBinDebitCard(int isBinDebitCard) {
        this.isBinDebitCard = isBinDebitCard;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getYearInterestRate() {
        return yearInterestRate;
    }

    public void setYearInterestRate(String yearInterestRate) {
        this.yearInterestRate = yearInterestRate;
    }

    public ActivityModel getActivity() {
        return activity;
    }

    public void setActivity(ActivityModel activity) {
        this.activity = activity;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public List<BankcardModel> getBankcardList() {
        return bankcardList;
    }

    public void setBankcardList(List<BankcardModel> bankcardList) {
        this.bankcardList = bankcardList;
    }

    public boolean isShowWechat() {
        return showWechat;
    }

    public void setShowWechat(boolean showWechat) {
        this.showWechat = showWechat;
    }

    public boolean isShowAlipay() {
        return showAlipay;
    }

    public void setShowAlipay(boolean showAlipay) {
        this.showAlipay = showAlipay;
    }
}