package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.BankcardModel;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * @author Tangxiaolong
 */
public class PageResponse extends Response {
    private int pageSize; //每页数据量
    private int total;// 数据总笔数
    private List<BankcardModel> rows;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BankcardModel> getRows() {
        return rows;
    }

    public void setRows(List<BankcardModel> rows) {
        this.rows = rows;
    }
}