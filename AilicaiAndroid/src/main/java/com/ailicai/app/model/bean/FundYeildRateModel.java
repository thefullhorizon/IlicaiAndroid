package com.ailicai.app.model.bean;

import java.io.Serializable;

/**
 * Created by duo.chen on 2017/1/4
 */
public class FundYeildRateModel implements Serializable {

    private String date = ""; //日期 例如：9/12
    private String yeildRate = "";//7日年化率 例如：3.1650
    private String unitIncome=""; //万份收益  例如：unitIncome=0.8525

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYeildRate() {
        return yeildRate;
    }

    public void setYeildRate(String yeildRate) {
        this.yeildRate = yeildRate;
    }

    public String getUnitIncome() {
        return unitIncome;
    }

    public void setUnitIncome(String unitIncome) {
        this.unitIncome = unitIncome;
    }
}
