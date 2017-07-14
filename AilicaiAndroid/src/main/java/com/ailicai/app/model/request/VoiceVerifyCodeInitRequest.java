package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by TangXiaolong on 2016/05/27.
 */
@RequestPath("/user/voiceVerifyCodeInit.rest")
public class VoiceVerifyCodeInitRequest extends Request {
    private String mobile; //手机号

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
