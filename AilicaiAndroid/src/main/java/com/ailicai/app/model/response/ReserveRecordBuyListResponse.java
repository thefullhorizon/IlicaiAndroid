package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.ReserveRecordBuyListBean;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by Owen on 2016/4/18
 */
public class ReserveRecordBuyListResponse extends Response {

    private List<ReserveRecordBuyListBean> rows;

    public List<ReserveRecordBuyListBean> getRows() {
        return rows;
    }

    public void setRows(List<ReserveRecordBuyListBean> rows) {
        this.rows = rows;
    }
}
