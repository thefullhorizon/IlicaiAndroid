package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by David on 16/3/15.
 */
@RequestPath("/ailicai/hasBuyProductList.rest")
public class HasBuyProductListRequest extends Request {
    private long userId; //用户Id,通过请求Header里的uticket获取
    private int type; //1-申购;2-持有;3-到期
    private int offset = 0; // 已看多少条
    private int pageSize = 10; // 每页显示多少行
    private int reserveOffset; //预约已加载数量 （申购tab传）

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getReserveOffset() {
        return reserveOffset;
    }

    public void setReserveOffset(int reserveOffset) {
        this.reserveOffset = reserveOffset;
    }
}
