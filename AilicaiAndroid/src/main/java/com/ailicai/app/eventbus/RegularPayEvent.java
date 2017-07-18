package com.ailicai.app.eventbus;

/**
 * Created by gerry on 2015/05/25.
 */
public class RegularPayEvent {

    public static final int PAY_RESULT_SUCCESS = 1;

    private int resultCode = 0;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
