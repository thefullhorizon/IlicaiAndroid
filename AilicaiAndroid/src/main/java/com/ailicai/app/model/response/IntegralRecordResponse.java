package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

import java.util.List;

public class IntegralRecordResponse extends Response {

    private int total;// 数据总笔数
    private List<ScoreDetailResponse> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ScoreDetailResponse> getRows() {
        return rows;
    }

    public void setRows(List<ScoreDetailResponse> rows) {
        this.rows = rows;
    }
}
