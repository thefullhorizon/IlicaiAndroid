package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Gerry on 2017/8/7.
 * 转入房产宝页面初始数据
 */

@RequestPath("/ailicai/getAppropriateCoupon.rest")
public class GetAppropriateCouponRequest extends Request {
    private String amount; //用户输入
    private String productId; //产品编号

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
