package com.ailicai.app.model.bean;

/**
 * Created by David on 16/3/14.
 */
public class IncomeModel {
    private String dayStr = "";//日期
    private double income; //收益

    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
