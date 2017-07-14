package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by David on 15/9/14.
 */

@RequestPath("/order/orderList.rest")
public class OrderListRequest extends Request {
    private int pageSize = 20; //每次加载20条数据
    private int offSet = 0; //数据偏移量

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
