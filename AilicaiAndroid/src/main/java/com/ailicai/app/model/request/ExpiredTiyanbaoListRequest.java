package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Administrator on 2016/8/16.
 */
@RequestPath("/tiyanbao/getDuedateList.rest")
public class ExpiredTiyanbaoListRequest extends Request {
    private int offset;
    private int pageSize;

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
