package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * @author owen
 *         2016/1/7
 */
public class PayPwdCheckVerifyCodeResponse extends Response {
    private String requestNo;//请求号

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }
}