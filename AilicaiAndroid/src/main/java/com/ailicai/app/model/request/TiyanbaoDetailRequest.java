package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 体验宝购买页初始数据请求
 * Created by liyanan on 16/8/15.
 */
@RequestPath("/tiyanbao/initBuyTiyanbao.rest")
public class TiyanbaoDetailRequest extends Request {
    private long activiId;//体验宝id

    public long getActiviId() {
        return activiId;
    }

    public void setActiviId(long activiId) {
        this.activiId = activiId;
    }
}
