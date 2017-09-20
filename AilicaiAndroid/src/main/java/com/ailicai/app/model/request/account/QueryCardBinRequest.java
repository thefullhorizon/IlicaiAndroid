package com.ailicai.app.model.request.account;


import com.ailicai.app.common.reqaction.RequestPath;
import com.ailicai.app.model.request.Request;

@RequestPath("/ailicai/queryCardBin.rest")
public class QueryCardBinRequest extends Request {
    private String cardNo;//卡号，RSA加密
    private int cardBinType = 0; //卡bin类型 0：开户绑卡（银行卡），1：普通绑卡
    private String payAmt; // 支付金额（银行卡支付时传）

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getCardBinType() {
        return cardBinType;
    }

    public void setCardBinType(int cardBinType) {
        this.cardBinType = cardBinType;
    }

    public String getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(String payAmt) {
        this.payAmt = payAmt;
    }
}
