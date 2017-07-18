package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Owen on 2016/3/10
 */
@RequestPath("/ailicai/queryReserveHistoryList.rest")
public class ReserveRecordListRequest extends Request {

    private long userId; //用户Id,通过请求Header里的uticket获取
    private int offset;// 已看多少条
    private int pageSize;// 每页显示多少行

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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
}
