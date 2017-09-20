package com.ailicai.app.model.response;

import com.ailicai.app.common.reqaction.RequestPath;
import com.ailicai.app.model.request.Request;

/**
 * Created by Jer on 2016/1/8.
 */
@RequestPath("/ailicai/saleHuoqibaoStatusQuery.rest")
public class SaleStatusQueryRequest extends Request {
    private String outTradeNo;//外部订单号
    private String accountType; //收银台类型：101-活期宝；106-用户账户
    private String payMethod; //支付到的账户类型 1-银行卡；2-账户余额 说明：活期宝收银台需指定

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}
