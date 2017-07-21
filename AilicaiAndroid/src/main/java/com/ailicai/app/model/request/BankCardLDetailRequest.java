package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * name: BankCardLDetailRequest <BR>
 * description: 银行卡详情请求 <BR>
 * create date: 2016/1/12
 *
 * @author: IWJW Zhou Xuan
 */

@RequestPath("/ailicai/bankcardDetail.rest")
public class BankCardLDetailRequest extends Request {

    private long userId;
    private String bankAccountId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
}
