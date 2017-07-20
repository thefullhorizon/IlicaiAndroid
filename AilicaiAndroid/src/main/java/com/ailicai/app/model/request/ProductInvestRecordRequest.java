package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 投资记录request
 * Created by liyanan on 16/4/8.
 */
@RequestPath("/ailicai/productInvestRecord.rest")
public class ProductInvestRecordRequest extends Request {
    private int offset;// 已看多少条
    private int pageSize;// 每页显示多少行
    private String productId; //产品编号

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
