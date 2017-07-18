package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

import java.math.BigDecimal;

/**
 * Created by zhouxuan on 16/8/2.
 *
 * 计算实际收益 服务器返回
 */
public class CalActualPayResponse extends Response {

    private BigDecimal actualPay = BigDecimal.ZERO;//实际支付金额
    private BigDecimal income = BigDecimal.ZERO;//预计收益

    public BigDecimal getActualPay() {
        if(actualPay == null) {
            return  new BigDecimal(0.0d);
        }
        return actualPay;
    }

    public void setActualPay(BigDecimal actualPay) {
        this.actualPay = actualPay;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
