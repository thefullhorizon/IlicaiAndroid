package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.TransactionListModel;
import com.huoqiu.framework.rest.Response;

/**
 * Created by wulianghuan on 2016/02/15.
 */
public class TransactionDetailResponse extends Response {
    private TransactionListModel tradeInfo; // 交易item

    public TransactionListModel getTradeInfo() {
        return tradeInfo;
    }

    public void setTradeInfo(TransactionListModel tradeInfo) {
        this.tradeInfo = tradeInfo;
    }
}
