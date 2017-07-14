package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by TangXiaolong on 2016/05/27.
 */
public class VoiceVerifyCodeInitResponse extends Response {
    private String message = "";//返回消息
    private String account = ""; // 语音帐号

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
