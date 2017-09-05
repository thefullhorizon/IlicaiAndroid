package com.ailicai.app.ui.login;

import java.io.Serializable;

/**
 * Created by Gerry on 2015/7/3.
 * 用户基本信息
 */
public class UserInfoBase implements Serializable {

    private long userId; //用户ID
    private String realName; //用户称呼
    private int gender; //性别 1：男，2：女
    private String mobile; //手机号码
    private int isOpenAccount = 0; // 判断吉爱财是否是否开户 0:否，1:是
    private int isSetPayPwd = 0; // 判断是否设置支付密码 0:否，1:是
    private int isRealNameVerify = 0; // 吉爱财是否通过实名认证 0:否，1:是
    private int isBinDebitCard = 0; // 吉爱财是否已绑定借记卡 0:否，1:是
    private int isAilicaiAllowUser = 0; // 判断是否吉爱财白名单用户 0:否，1:是
    private String key1 = ""; // app端加密用公钥
    private String key2 = ""; // server端加密的对应解密私钥
    private int rsaClose = 0; // RSA加密是否关闭，1：关闭，0：打开
    private int hasSafeCard = 0; // 是否已绑定安全卡  0:否，1:是
    private String bankName; //安全卡银行名称
    private String bankcardTailNo; //安全卡末尾4位
    private String imUserId = ""; // 环信用户Id
    private String imPasswd = ""; // 环信用户密码
    private String idCardNo = ""; // 身份证号
    private int collectionNum; //关注总数量
    public int isTestUser; //是否新浪测试用户

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public int getIsAilicaiAllowUser() {
        return isAilicaiAllowUser;
    }

    public void setIsAilicaiAllowUser(int isAilicaiAllowUser) {
        this.isAilicaiAllowUser = isAilicaiAllowUser;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public int getRsaClose() {
        return rsaClose;
    }

    public void setRsaClose(int rsaClose) {
        this.rsaClose = rsaClose;
    }

    public int getHasSafeCard() {
        return hasSafeCard;
    }

    public void setHasSafeCard(int hasSafeCard) {
        this.hasSafeCard = hasSafeCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankcardTailNo() {
        return bankcardTailNo;
    }

    public void setBankcardTailNo(String bankcardTailNo) {
        this.bankcardTailNo = bankcardTailNo;
    }

    public int getIsTestUser() {
        return isTestUser;
    }

    public String getImUserId() {
        return imUserId;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public String getImPasswd() {
        return imPasswd;
    }

    public void setImPasswd(String imPasswd) {
        this.imPasswd = imPasswd;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }
    public void setIsTestUser(int isTestUser) {
        this.isTestUser = isTestUser;
    }

    @Override
    public String toString() {
        return "UserInfoBase{" +
                "userId=" + userId +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", mobile='" + mobile + '\'' +
                ", isOpenAccount=" + isOpenAccount +
                ", isSetPayPwd=" + isSetPayPwd +
                ", isRealNameVerify=" + isRealNameVerify +
                ", isBinDebitCard=" + isBinDebitCard +
                ", isAilicaiAllowUser=" + isAilicaiAllowUser +
                ", key1='" + key1 + '\'' +
                ", key2='" + key2 + '\'' +
                ", rsaClose=" + rsaClose +
                ", hasSafeCard=" + hasSafeCard +
                ", imUserId='" + imUserId + '\'' +
                ", imPasswd='" + imPasswd + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", collectionNum=" + collectionNum +
                '}';
    }
}
