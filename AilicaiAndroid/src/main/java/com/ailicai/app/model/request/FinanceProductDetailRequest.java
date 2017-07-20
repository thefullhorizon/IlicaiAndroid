package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 房产宝项目详情接口
 * Created by liyanan on 16/4/11.
 */
@RequestPath("/ailicai/financeProductMoreDetail.rest")
public class FinanceProductDetailRequest extends Request {
    private String productId; //产品编号

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
