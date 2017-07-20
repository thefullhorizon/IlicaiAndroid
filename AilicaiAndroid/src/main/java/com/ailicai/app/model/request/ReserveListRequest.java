package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Owen on 2016/5/25
 */
@RequestPath("/ailicai/canReserveProductList.rest")
public class ReserveListRequest extends Request {

    private String productId; //预约产品编号
    private int horizon;//投资期限
    private int offset = 0; // 已看多少条
    private int pageSize = 10; // 每页显示多少行


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getHorizon() {
        return horizon;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }
}
