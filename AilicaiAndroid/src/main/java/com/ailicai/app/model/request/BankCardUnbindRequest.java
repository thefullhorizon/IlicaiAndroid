package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * name: BankCardUnbindRequest <BR>
 * description: 银行卡解绑请求 <BR>
 * create date: 2016/1/19
 *
 * @author: IWJW Zhou Xuan
 */

@RequestPath("/ailicai/unbindBankcard.rest")
public class BankCardUnbindRequest extends Request {

    private String bankAccountId;//银行卡标识
    private String paypwd;//支付密码，RSA加密

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }
}
