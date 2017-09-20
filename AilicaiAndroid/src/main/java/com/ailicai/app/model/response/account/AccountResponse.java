package com.ailicai.app.model.response.account;

import com.huoqiu.framework.rest.Response;

import java.io.Serializable;

/**
 * @author Tangxiaolong
 */
public class AccountResponse extends Response implements Serializable {

    private int isOpenAccount = 0; // 判断是否是否开户 0:否，1:是，2：开户中
    private int isSetPayPwd = 0; // 判断是否设置支付密码 0:否，1:是
    private int isRealNameVerify = 0; // 是否通过实名认证 0:否，1:是
    private int isBinDebitCard = 0; // 是否已绑定借记卡 0:否，1:
    private int hasSafeCard = 0; // 是否已绑定银行卡  0:否，1:是
    private String name; // 用户名
    private String idCardNo; // 身份证号码
    private String bankcardTailNo; //银行卡尾号
    private String bankName; //银行名称
    private int cardType; //卡类型【1：储蓄卡 、2：信用卡 、3：存折 、4：其它 】
    private int isAdult = 1;//判断是否成年 0  否  1是

    // v5.7 add
    private int showDeductMoney = 1;//是否提示扣1分钱
    private int useH5;//开户结果页是否使用H5
    private String openResultUrl = "";//开户结果h5 Url

    // v7.0  开户成功后的奖励
    private String activityMemo = "";
    private String openAccountNotice="开户成功";   //开户成功页通知，文案1：开户成功   文案2：上海华瑞银行存管账户开通成功
    private String openAccountTitle="开户成功";         //流程开户成功或者开通存管账户

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

    public int getHasSafeCard() {
        return hasSafeCard;
    }

    public void setHasSafeCard(int hasSafeCard) {
        this.hasSafeCard = hasSafeCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getBankcardTailNo() {
        return bankcardTailNo;
    }

    public void setBankcardTailNo(String bankcardTailNo) {
        this.bankcardTailNo = bankcardTailNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(int isAdult) {
        this.isAdult = isAdult;
    }

    public int getShowDeductMoney() {
        return showDeductMoney;
    }

    public void setShowDeductMoney(int showDeductMoney) {
        this.showDeductMoney = showDeductMoney;
    }

    public int getUseH5() {
        return useH5;
    }

    public void setUseH5(int useH5) {
        this.useH5 = useH5;
    }

    public String getOpenResultUrl() {
        return openResultUrl;
    }

    public void setOpenResultUrl(String openResultUrl) {
        this.openResultUrl = openResultUrl;
    }

    public boolean isShowDeductMoney() {
        return showDeductMoney == 1;
    }

    public boolean isUseH5() {
        return useH5 == 1;
    }

    public String getActivityMemo() {
        return activityMemo;
    }

    public void setActivityMemo(String activityMemo) {
        this.activityMemo = activityMemo;
    }

    public String getOpenAccountNotice() {
        return openAccountNotice;
    }

    public void setOpenAccountNotice(String openAccountNotice) {
        this.openAccountNotice = openAccountNotice;
    }

    public String getOpenAccountTitle() {
        return openAccountTitle;
    }

    public void setOpenAccountTitle(String openAccountTitle) {
        this.openAccountTitle = openAccountTitle;
    }
}