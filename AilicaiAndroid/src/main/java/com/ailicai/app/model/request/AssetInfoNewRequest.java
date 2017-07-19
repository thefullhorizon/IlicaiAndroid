package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by nanshan on 7/19/2017.
 */
@RequestPath("/ailicai/myAssetsInfoNew.rest")
public class AssetInfoNewRequest extends Request{

    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
