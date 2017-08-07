package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 红包列表Request
 * Created by liyanan on 16/3/4.
 */
@RequestPath("/ailicai/voucherListByProduct.rest")
public class VoucherListByProductRequest extends Request {
    private String userId;//用户userId
    private int offset;// 已看多少条
    private int pageSize;// 每页显示多少行
    private String productId; //房产宝id

    private int amount; // 金额

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
