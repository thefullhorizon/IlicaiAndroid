package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by jrvair on 16/1/14.
 */
public class GetOutTradeNoResponse extends Response {


    private String requestNo;//请求号
    private String outTradeNo;//外部订单号


    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}
