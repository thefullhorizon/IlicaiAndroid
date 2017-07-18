package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Gerry on 2016/1/7.
 * 转入房产宝页面初始数据
 */

@RequestPath("/ailicai/buyDingqibaoPageInit.rest")
public class RegularPayBaseInfoRequest extends Request {
    private long userId; //用户Id,通过请求Header里的uticket获取
    private String productId; //产品编号

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
