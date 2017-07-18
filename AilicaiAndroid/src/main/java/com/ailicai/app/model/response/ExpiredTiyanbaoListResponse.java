package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.TiyanbaoDetailModel;
import com.huoqiu.framework.rest.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public class ExpiredTiyanbaoListResponse extends Response {
    private List<TiyanbaoDetailModel> tiyanbaoList = new ArrayList<>();//到期体验宝列表
    private String income="";//累计收益
    private String amount="";//到期体验金
    private int total;

    public List<TiyanbaoDetailModel> getTiyanbaoList() {
        return tiyanbaoList;
    }

    public void setTiyanbaoList(List<TiyanbaoDetailModel> tiyanbaoList) {
        this.tiyanbaoList = tiyanbaoList;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
