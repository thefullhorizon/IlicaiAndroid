package com.ailicai.app.model.bean;

import java.io.Serializable;

public class IntegralModel implements Serializable {

    private String integralTime = "";//时间
    private String integralTitle = "";//明细
    private String integralNum = "";//积分

    public String getIntegralTime() {
        return integralTime;
    }

    public void setIntegralTime(String integralTime) {
        this.integralTime = integralTime;
    }

    public String getIntegralTitle() {
        return integralTitle;
    }

    public void setIntegralTitle(String integralTitle) {
        this.integralTitle = integralTitle;
    }

    public String getIntegralNum() {
        return integralNum;
    }

    public void setIntegralNum(String integralNum) {
        this.integralNum = integralNum;
    }
}
