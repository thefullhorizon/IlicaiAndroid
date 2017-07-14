package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by TangXiaolong on 2016/05/27.
 */
@RequestPath("/user/sendVoiceVerifyCode.rest")
public class SendVoiceVerifyCodeRequest extends Request {
    private String mobile; //手机号
    private String account = ""; // 语音帐号

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
