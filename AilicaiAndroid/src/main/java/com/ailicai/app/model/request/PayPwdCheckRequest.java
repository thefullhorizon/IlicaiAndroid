package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * @author owen
 * 2016/1/7
 */
@RequestPath("/ailicai/checkPayPwd.rest")
public class PayPwdCheckRequest extends Request {

    private long userId; //用户Id
    private String paypwd; //支付密码,SHA-256(支付密码明文)

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }
}
