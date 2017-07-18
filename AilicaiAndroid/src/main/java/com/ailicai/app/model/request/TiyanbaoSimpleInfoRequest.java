package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Administrator on 2016/8/17.
 */
@RequestPath("/tiyanbao/getTiyanbaoHoldDetail.rest")
public class TiyanbaoSimpleInfoRequest extends Request {

    private long couponId;//卡券id

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }
}
