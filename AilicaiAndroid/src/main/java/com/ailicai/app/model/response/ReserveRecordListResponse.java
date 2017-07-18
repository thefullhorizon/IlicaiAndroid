package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Product;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by Owen on 2016/3/10
 */
public class ReserveRecordListResponse extends Response {

    private List<Product> reserveList;

    public List<Product> getReserveList() {
        return reserveList;
    }

    public void setReserveList(List<Product> reserveList) {
        this.reserveList = reserveList;
    }
}
