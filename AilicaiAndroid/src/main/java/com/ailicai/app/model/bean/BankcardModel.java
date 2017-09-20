package com.ailicai.app.model.bean;

import android.text.TextUtils;

/**
 * Created by Tangxiaolong on 2015/12/30.
 */
public class BankcardModel {
    private String bankAccountId; //银行卡标识
    private String cardNo; //银行卡号(银行卡号后四位，其余为*号)
    private String bankCode; // 银行编号
    private String bankName; //银行名称
    private int cardType; //卡类型【1：储蓄卡 、2：信用卡 、3：存折 、4：其它 】
    private int isSafeCard; // 是否银行卡 0-否 1-是
    private String mobile; // 银行预留手机号
    private int hasMobile; // 是否有银行预留手机号 0：无，1：有
    private String cardLastNo; //银行卡尾号(卡号后四位)
    private int maxPayAmt; // 最高支付额度
    private int firstTimeLimit; //首次限额
    private int secondTimeLimit; //二次限额

    private String timeLimitStr;//单次限制（包含：万）
    private String dayLimitStr; //单日限制（包含：万）
    private String remark; // 银行备注
    private String bankMobile; //银行手机号码

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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

    public int getIsSafeCard() {
        return isSafeCard;
    }

    public void setIsSafeCard(int isSafeCard) {
        this.isSafeCard = isSafeCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getHasMobile() {
        return hasMobile;
    }

    public void setHasMobile(int hasMobile) {
        this.hasMobile = hasMobile;
    }

    public String getCardLastNo() {
        return cardLastNo;
    }

    public void setCardLastNo(String cardLastNo) {
        this.cardLastNo = cardLastNo;
    }

    public int getMaxPayAmt() {
        return maxPayAmt;
    }

    public void setMaxPayAmt(int maxPayAmt) {
        this.maxPayAmt = maxPayAmt;
    }

    public int getFirstTimeLimit() {
        return firstTimeLimit;
    }

    public void setFirstTimeLimit(int firstTimeLimit) {
        this.firstTimeLimit = firstTimeLimit;
    }

    public int getSecondTimeLimit() {
        return secondTimeLimit;
    }

    public void setSecondTimeLimit(int secondTimeLimit) {
        this.secondTimeLimit = secondTimeLimit;
    }

    public String getTimeLimitStr() {
        return timeLimitStr;
    }

    public void setTimeLimitStr(String timeLimitStr) {
        this.timeLimitStr = timeLimitStr;
    }

    public String getDayLimitStr() {
        return dayLimitStr;
    }

    public void setDayLimitStr(String dayLimitStr) {
        this.dayLimitStr = dayLimitStr;
    }

    public String getRemark() {
        return TextUtils.isEmpty(remark)?"":remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBankMobile() {
        return TextUtils.isEmpty(bankMobile)?"":bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

    public String getPayMax() {
        return "银行限额："+getEachPayMax() + " " + getEachDayPayMax();
    }

    public String getEachPayMax() {
        if(TextUtils.isEmpty(timeLimitStr)|| "0".equals(timeLimitStr)) {
            return "单笔不限";
        } else {
            return "单笔"+timeLimitStr;
        }
    }

    public String getEachDayPayMax() {
        if(TextUtils.isEmpty(dayLimitStr)|| "0".equals(dayLimitStr)) {
            return "单日不限";
        } else {
            return "单日"+dayLimitStr;
        }
    }
}
