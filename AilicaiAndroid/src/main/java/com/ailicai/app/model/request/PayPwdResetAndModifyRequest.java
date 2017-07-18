package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * @author owen
 *         2016/1/7
 */
@RequestPath("/ailicai/resetPayPassword.rest")
public class PayPwdResetAndModifyRequest extends Request {

    private long userId; //用户Id
    private String paypwd;//新密码 SHA-256(支付密码明文)
    private String requestNo;//请求号

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

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }
}
