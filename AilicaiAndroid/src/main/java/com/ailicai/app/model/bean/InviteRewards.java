package com.ailicai.app.model.bean;

import java.io.Serializable;

/**
 * 邀请记录
 */
public class InviteRewards implements Serializable {
    private String mobile = "";//手机号 隐藏中间七位
    private String inviteTime = "";//邀请时间
    private int status = 0;//0:未投资，1：投资中 ， 2：失效

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(String inviteTime) {
        this.inviteTime = inviteTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
