package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 用户信息
 */
@RequestPath("/user/getUserInfo.rest")
public class UserInfoRequest extends Request{

    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
