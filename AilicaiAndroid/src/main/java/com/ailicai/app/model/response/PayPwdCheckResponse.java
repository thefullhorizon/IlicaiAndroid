package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * @author owen
 *         2016/1/7
 */
public class PayPwdCheckResponse extends Response {

    private int checkFlag = 0;  // 0：未通过，1：通过
    private int remainingCnt = 0; // 剩余次数

    public int getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(int checkFlag) {
        this.checkFlag = checkFlag;
    }

    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
    }
}