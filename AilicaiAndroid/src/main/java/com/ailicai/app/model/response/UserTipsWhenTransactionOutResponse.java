package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by nanshan on 8/17/2017.
 */

public class UserTipsWhenTransactionOutResponse extends Response {

    private int beforeFifteen; //当前时间是否是15点前
    private String messageLine1; //下发文案 1
    private String messageLine2; //下发文案 2

    public int getBeforeFifteen() {
        return beforeFifteen;
    }

    public void setBeforeFifteen(int beforeFifteen) {
        this.beforeFifteen = beforeFifteen;
    }

    public String getMessageLine1() {
        return messageLine1;
    }

    public void setMessageLine1(String messageLine1) {
        this.messageLine1 = messageLine1;
    }

    public String getMessageLine2() {
        return messageLine2;
    }

    public void setMessageLine2(String messageLine2) {
        this.messageLine2 = messageLine2;
    }
}
