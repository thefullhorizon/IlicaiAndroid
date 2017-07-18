package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * @author owen
 *         2016/1/7
 */
@RequestPath("/ailicai/sendUserCheckVerifyCode.rest")
public class PayPwdGetVerifyCodeRequest extends Request {
    private long userId; //用户Id

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
