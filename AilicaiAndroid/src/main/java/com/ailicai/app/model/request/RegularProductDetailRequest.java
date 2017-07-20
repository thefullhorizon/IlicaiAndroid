package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by duo.chen on 2016/1/7.
 */
@RequestPath("/ailicai/financeProductDetail.rest")
public class RegularProductDetailRequest extends Request{

    private String ProductId; //产品编号

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }
}
