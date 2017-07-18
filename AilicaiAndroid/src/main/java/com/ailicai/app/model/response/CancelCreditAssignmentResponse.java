package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * 取消转让response
 * Created by liyanan on 16/8/3.
 */
public class CancelCreditAssignmentResponse extends Response {
    private int remainingCnt = 0; // 剩余次数

    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
    }
}
