package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * 为当前用户开启或者关闭自动投标
 * Created by jeme on 2017/8/21.
 */

public class AutoBidSwitchResponse extends Response {

    private int remainingCnt = 0;                   // 密码错误剩余次数

    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
    }
}
