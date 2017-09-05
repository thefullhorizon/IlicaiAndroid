package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

@RequestPath("/ailicai/getScoreDetail.rest")
public class IntegralRecordRequest extends Request {
    private int pageSize = 10;// 页面大小
    private int offset = 0;// 偏移

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
