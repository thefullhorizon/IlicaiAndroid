package com.ailicai.app.model.bean;

/**
 * Created by David on 16/1/11.
 */
public class IncomeDetail {
    private String time = "";// 日期  yyyy-MM-dd
    private String incomeTypeMemo = ""; // 收益类型描述 如：钱包，产品名称
    private double income; // 单日收益

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIncomeTypeMemo() {
        return incomeTypeMemo;
    }

    public void setIncomeTypeMemo(String incomeTypeMemo) {
        this.incomeTypeMemo = incomeTypeMemo;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
