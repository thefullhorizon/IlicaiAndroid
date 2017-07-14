package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Gerry on 2015/7/7.
 */
@RequestPath("/user/sendVerifyCode.rest")
public class GetVerifyCodeRequest extends Request {
    private String mobile; //手机号
    private int type = 0; // 发送验证码功能类型 0：登录注册 1：修改手机账号验当前手机号 2：修改手机账号验新手机号

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
