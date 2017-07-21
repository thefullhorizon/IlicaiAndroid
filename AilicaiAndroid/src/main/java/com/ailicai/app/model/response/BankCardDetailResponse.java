package com.ailicai.app.model.response;

import android.text.TextUtils;

import com.huoqiu.framework.rest.Response;

/**
 * name: BankCardDetailResponse <BR>
 * description: 银行卡详情返回数据 <BR>
 * create date: 2016/1/12
 *
 * @author: IWJW Zhou Xuan
 */
public class BankCardDetailResponse extends Response {

    private String bankAccountId;//银行卡标识
    private String cardNo; //银行卡号
    private String bankName; //银行名称
    private int cardType; //卡类型【1：储蓄卡 、2：信用卡 、3：存折 、4：其它 】
    private int isSafeCard; // 是否安全卡 0-否 1-是
    private int singleLimit; //单笔限额
    private int dayLimit; //单日限额
    private int monthLimit; //单月限额
    private double totalAsset; // 总资产(仅安全卡有值)
    private String mobile; // 银行预留手机号
    private String changeSafeCardUrl; // 原安全卡不可用时更换安全卡的说明URL
    private int hasMobile; // 是否有银行预留手机号 0：无，1：有
    private int changeLimit;//换卡限额
    private String dayLimitStr; // 单日限额字符串
    private String singleLimitStr;// 单笔限额字符串

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

    public int getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(int singleLimit) {
        this.singleLimit = singleLimit;
    }

    public int getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(int dayLimit) {
        this.dayLimit = dayLimit;
    }

    public int getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(int monthLimit) {
        this.monthLimit = monthLimit;
    }

    public int getChangeLimit() {
        return changeLimit;
    }

    public void setChangeLimit(int changeLimit) {
        this.changeLimit = changeLimit;
    }

    public String getDayLimitStr() {
        return dayLimitStr;
    }

    public void setDayLimitStr(String dayLimitStr) {
        this.dayLimitStr = dayLimitStr;
    }

    public String getSingleLimitStr() {
        return singleLimitStr;
    }

    public void setSingleLimitStr(String singleLimitStr) {
        this.singleLimitStr = singleLimitStr;
    }

    public String getPayMax() {
        return getEachPayMax() + "，" + getEachDayPayMax();
    }

    public String getEachPayMax() {
       if (singleLimit == 0 || TextUtils.isEmpty(singleLimitStr) || "0".equals(singleLimitStr)) {
            return "单笔不限";
        } else {
            return "单笔限额"+singleLimitStr;
        }
    }

    public String getEachDayPayMax() {
         if (dayLimit == 0 || TextUtils.isEmpty(dayLimitStr)|| "0".equals(dayLimitStr)) {
            return "单日不限";
        } else {
            return "单日"+dayLimitStr;
        }
    }

    public String getEachMonthPayMax() {
        if (monthLimit >= 10000) {
            String moneyShow = ((double) monthLimit) / 10000d + "";
            if (moneyShow.endsWith(".0")) {
                moneyShow = moneyShow.substring(0, moneyShow.length() - 2);
            }
            return "单月" + moneyShow + "万";
        } else if (monthLimit == 0) {
            return "单月不限";
        } else {
            return "单月" + monthLimit;
        }
    }

    public String getCardTypeAndNoDesc() {
        return getCardTypeDesc() + getCardNoDesc();
    }

    public String getCardNoDesc() {
        if (cardNo != null && cardNo.length() > 4) {
            return cardNo.substring(cardNo.length() - 4, cardNo.length());
        }
        return cardNo == null ? "" : cardNo;
    }

    public String getCardTypeDesc() {
        String desc = "";
        switch (cardType) {
            case 1:
                desc = "储蓄卡尾号";
                break;
            case 2:
                desc = "信用卡尾号";
                break;
            case 3:
                desc = "存折";
                break;
            case 4:
                desc = "其它";
                break;
        }
        return desc;
    }

    public double getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(double totalAsset) {
        this.totalAsset = totalAsset;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChangeSafeCardUrl() {
        return changeSafeCardUrl;
    }

    public void setChangeSafeCardUrl(String changeSafeCardUrl) {
        this.changeSafeCardUrl = changeSafeCardUrl;
    }

    public int getHasMobile() {
        return hasMobile;
    }

    public void setHasMobile(int hasMobile) {
        this.hasMobile = hasMobile;
    }
}
