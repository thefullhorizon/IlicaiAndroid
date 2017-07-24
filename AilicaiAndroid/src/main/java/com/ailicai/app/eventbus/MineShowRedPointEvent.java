package com.ailicai.app.eventbus;

/**
 * 主页面我的红点提示通知
 */
public class MineShowRedPointEvent {
    public boolean showRedPoint = false;

    public boolean isShowRedPoint() {
        return showRedPoint;
    }

    public void setShowRedPoint(boolean showRedPoint) {
        this.showRedPoint = showRedPoint;
    }
}
