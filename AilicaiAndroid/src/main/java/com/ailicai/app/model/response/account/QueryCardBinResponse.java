package com.ailicai.app.model.response.account;

import android.text.TextUtils;

import com.huoqiu.framework.rest.Response;

/**
 * @author Tangxiaolong
 */
public class QueryCardBinResponse extends Response {
    private String bankCode; // 银行编号
    private String bankName; // 银行名称
    private int valid = 1; // 银行卡是否可用 0：不可用，1可用，开户绑安全卡用
    private int normalValid = 1; // 银行卡是否可用 0：不可用，1可用，绑普通卡用
    private int cardType; //卡类型【1：储蓄卡 、2：信用卡 、3：存折 、4：其它 】0则卡bin失败

    private String timeLimitStr;  // 单次限制（包含：万）
    private String dayLimitStr; //单日限制（包含：万）
    private String remark; //银行备注
    private String bankMobile; //银行号码

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

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getNormalValid() {
        return normalValid;
    }

    public void setNormalValid(int normalValid) {
        this.normalValid = normalValid;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
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
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

    public String getPayMax() {
        return "银行限额："+getEachPayMax() + "，" + getEachDayPayMax();
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