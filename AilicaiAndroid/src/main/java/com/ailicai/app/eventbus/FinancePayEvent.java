package com.ailicai.app.eventbus;

public class FinancePayEvent {
    public static final int PAY_IN = 1;//转入
    public static final int PAY_OUT = 2;//转出
    //处理结果，S成功;P处理中;F失败
    private String payState = "S";
    //类型:转入，转出
    private int payType = PAY_IN;

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }
}
