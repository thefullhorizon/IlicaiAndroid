package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by nanshan on 6/16/2017.
 */
@RequestPath("/ailicai/getBankNoticeList.rest")
public class AilicaiNoticeListOnRollRequest extends Request {
    private long userId; //用户Id,通过请求Header里的uticket获取

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
