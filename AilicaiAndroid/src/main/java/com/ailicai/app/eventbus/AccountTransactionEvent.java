package com.ailicai.app.eventbus;

/**
 * Created by nanshan on 7/12/2017.
 */

public class AccountTransactionEvent {
    public static final int TOPUP = 1;//
    public static final int WITHDRAW = 2;//转出
    //处理结果，S成功;P处理中;F失败
    private String payState = "S";
    //类型:转入，转出
    private int payType = TOPUP;

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
