package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.IncomeDetail;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by David on 16/1/11.
 */
public class IncomeDetailResponse extends Response {
    private int errorCode = 0; //返回代码 0-正常 其他参考对应的errorCode定义
    private String message = "";//返回消息
    private double totalIncome; //累计收益
    private int total;// 数据总笔数
    private List<IncomeDetail> incomeDetailList; // 收益列表


    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<IncomeDetail> getIncomeDetailList() {
        return incomeDetailList;
    }

    public void setIncomeDetailList(List<IncomeDetail> incomeDetailList) {
        this.incomeDetailList = incomeDetailList;
    }
}
