package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

@RequestPath("/ailicai/unbindBankcardVerify.rest")
public class BankcardUnbindVerifyBySinaRequest extends Request {
    public String advanceVoucherNo; //推进号
    public String verifyCode; //验证码
}
