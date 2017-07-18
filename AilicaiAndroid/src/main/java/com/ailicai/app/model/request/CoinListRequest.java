package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by nanshan on 2017/5/8.
 * V6.7新增 小钱列表
 */
@RequestPath("/ailicai/queryInvestPennyList.rest")
public class CoinListRequest extends Request {

    private String productId; //产品编号
    private int offset = 0; // 当前页
    private int pageSize = 10; // 每页显示多少行
    private long userId ; // 2017-5-15 新加

    private String bidOrderNo;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }

}
