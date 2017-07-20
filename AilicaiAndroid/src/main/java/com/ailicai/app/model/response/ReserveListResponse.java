package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.ReserveListBean;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by Owen on 2016/5/25
 */
public class ReserveListResponse extends Response {

    private int total;// 数据总笔数
    private String interestTimeStr = ""; //预计成交时间：预计{房产宝产品日期} 10:30成交
    private List<ReserveListBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ReserveListBean> getRows() {
        return rows;
    }

    public void setRows(List<ReserveListBean> rows) {
        this.rows = rows;
    }

    public String getInterestTimeStr() {
        return interestTimeStr;
    }

    public void setInterestTimeStr(String interestTimeStr) {
        this.interestTimeStr = interestTimeStr;
    }
}
