package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.ProductInvestRecord;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * 投资记录ListResponse
 * Created by liyanan on 16/4/8.
 */
public class ProductInvestRecordListResponse extends Response {
    private int total;// 数据总笔数
    private List<ProductInvestRecord> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ProductInvestRecord> getRows() {
        return rows;
    }

    public void setRows(List<ProductInvestRecord> rows) {
        this.rows = rows;
    }
}
