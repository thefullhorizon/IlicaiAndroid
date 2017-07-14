package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 15/9/14.
 */
public class OrderListResponse extends Response {
    private boolean hasNextPage; // 是否有分页
    private long total; // 总记录

    private List<Order> orderList = new ArrayList<>();


    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
