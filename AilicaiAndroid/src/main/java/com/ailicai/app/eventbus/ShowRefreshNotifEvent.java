package com.ailicai.app.eventbus;

import com.ailicai.app.model.response.RreshDataResponse;

/**
 * Tab小红点相关数据通知
 */
public class ShowRefreshNotifEvent {

    RreshDataResponse rreshDataResponse;

    public void setRreshDataResponse(RreshDataResponse rreshDataResponse) {
        this.rreshDataResponse = rreshDataResponse;
    }

    public RreshDataResponse getRreshDataResponse() {
        return rreshDataResponse;
    }

    @Override
    public String toString() {
        return "ShowRefreshNotifEvent{" +
                "rreshDataResponse=" + rreshDataResponse +
                '}';
    }
}
