package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * @author owen
 *         2016/1/7
 */
@RequestPath("/ailicai/checkVerifyCode.rest")
public class PayPwdCheckVerifyCodeRequest extends Request {

    private long userId; //用户Id
    private String verifyCode; // 验证码

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
