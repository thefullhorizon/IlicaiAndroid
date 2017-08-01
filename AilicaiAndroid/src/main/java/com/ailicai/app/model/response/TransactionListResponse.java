package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.TransactionListModel;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by wulianghuan on 2016/1/6.
 */
public class TransactionListResponse extends Response {
    private int total;// 数据总笔数
    private List<TransactionListModel> rows; // 交易列表

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TransactionListModel> getRows() {
        return rows;
    }

    public void setRows(List<TransactionListModel> rows) {
        this.rows = rows;
    }
}
