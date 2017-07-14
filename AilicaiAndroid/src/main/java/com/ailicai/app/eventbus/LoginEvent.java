package com.ailicai.app.eventbus;


import com.ailicai.app.ui.login.LoginManager;

/**
 * Created by Ted on 2014/10/11.
 */
public class LoginEvent {
    /**
     * 登录或者退出
     */
    private boolean isLoginSuccess = false;

    private boolean cancelLogin = false;
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
}
