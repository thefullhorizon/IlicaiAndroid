package com.ailicai.app.ui.mine;

import com.ailicai.app.common.reqaction.RequestPath;
import com.ailicai.app.model.request.Request;

/**
 * Created by Gerry on 2015/7/7.
 */
@RequestPath("/user/userMobileChange.rest")
public class UserMobileChangeRequest extends Request {
    private long userId; // 用户ID
    private String mobile; //手机号
    private String verifyCode; //验证码
    private String mobileSn; //手机设备唯一标识(或GUID)

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

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

    public String getMobileSn() {
        return mobileSn;
    }

    public void setMobileSn(String mobileSn) {
        this.mobileSn = mobileSn;
    }
}
