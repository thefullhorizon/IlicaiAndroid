package com.ailicai.app.eventbus;

/**
 * Created by Gerry on 2014/10/11.
 */
public class EditUserInfoEvent {
    private long userId; //用户ID
    private String realName; //用户称呼
    private int gender; //性别 1：男，2：女
    private String mobile; //手机号码

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
