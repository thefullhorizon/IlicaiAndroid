package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Gerry on 2015/7/7.
 */
@RequestPath("/user/userMobileCheck.rest")
public class UserMobileCheckRequest extends Request {
    private String mobile; //手机号
    private String verifyCode; //验证码

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
