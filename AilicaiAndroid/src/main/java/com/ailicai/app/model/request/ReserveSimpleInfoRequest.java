package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by yeyong.zhang on 2016/5/25.
 * 资产列表预约的产品详情
 */
@RequestPath("/ailicai/reserveSimpleInfo.rest")
public class ReserveSimpleInfoRequest extends Request {
    private String productId; //产品编号

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
