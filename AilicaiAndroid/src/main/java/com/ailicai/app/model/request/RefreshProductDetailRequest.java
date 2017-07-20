package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 房产宝自动刷新request
 * Created by liyanan on 16/4/13.
 */
@RequestPath("/ailicai/refreshProductDetail.rest")
public class RefreshProductDetailRequest extends Request {
    private String productId; //产品编号

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
