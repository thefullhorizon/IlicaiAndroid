package com.ailicai.app.eventbus;

/**
 * 设置中强制更新版本时，强制退出
 */
public class ExitEvent {
    private boolean isForceExit = false;

    public boolean isForceExit() {
        return isForceExit;
    }

    public void setForceExit(boolean forceExit) {
        isForceExit = forceExit;
    }
}
