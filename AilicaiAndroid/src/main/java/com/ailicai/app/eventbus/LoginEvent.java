package com.ailicai.app.eventbus;


import com.ailicai.app.model.response.UserLoginResponse;
import com.ailicai.app.ui.login.LoginManager;

import java.io.Serializable;

/**
 * Created by Ted on 2014/10/11.
 */
public class LoginEvent implements Serializable{
    /**
     * 登录或者退出
     */
    private boolean isLoginSuccess = false;
    //登录成功是否弹出大礼包
    private boolean continueNext = true;

    private boolean cancelLogin = false;
    private UserLoginResponse jsonObject = null;
    /**
     * 发起登录或者退出的页面的标记，默认是来自我的页面
     */
    private int fromPageCode = LoginManager.LOGIN_FROM_MINE;

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }

    public boolean isContinueNext() {
        return continueNext;
    }

    public void setContinueNext(boolean continueNext) {
        this.continueNext = continueNext;
    }

    public boolean isCancelLogin() {
        return cancelLogin;
    }

    public void setCancelLogin(boolean cancelLogin) {
        this.cancelLogin = cancelLogin;
    }

    public int getFromPageCode() {
        return fromPageCode;
    }

    public void setFromPageCode(int fromPageCode) {
        this.fromPageCode = fromPageCode;
    }

    public UserLoginResponse getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(UserLoginResponse jsonObject) {
        this.jsonObject = jsonObject;
    }
}
