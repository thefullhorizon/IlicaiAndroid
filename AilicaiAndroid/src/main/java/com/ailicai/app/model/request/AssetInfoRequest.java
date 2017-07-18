package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by David on 16/1/6.
 */
@RequestPath("/ailicai/getAssetInfo.rest")
public class AssetInfoRequest extends Request{
    private long userId; //用户Id,通过请求Header里的uticket获取

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

