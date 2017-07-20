package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 校验预约口令request
 * Created by liyanan on 16/4/14.
 */
@RequestPath("/ailicai/checkReservePasswd.rest")
public class CheckReservePasswdRequest extends Request {
    private long userId; //用户Id,通过请求Header里的uticket获取
    private String reservePwd; // 预约口令

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getReservePwd() {
        return reservePwd;
    }

    public void setReservePwd(String reservePwd) {
        this.reservePwd = reservePwd;
    }
}
