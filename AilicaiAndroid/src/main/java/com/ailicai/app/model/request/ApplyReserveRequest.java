package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by David on 16/3/11.
 */
@RequestPath("/ailicai/applyReserve.rest")
public class ApplyReserveRequest extends Request {

    private long userId; //用户Id,通过请求Header里的uticket获取
    private String productId = ""; // 产品Id
    private String amount;//申购金额
    private int term; //预约期限
    private String paypwd; //交易密码 RSA加密
    private String reservePwd; // 预约口令
    private String yearInterestRateStr;//年化收益率或收益区间

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public String getReservePwd() {
        return reservePwd;
    }

    public void setReservePwd(String reservePwd) {
        this.reservePwd = reservePwd;
    }

    public String getYearInterestRateStr() {
        return yearInterestRateStr;
    }

    public void setYearInterestRateStr(String yearInterestRateStr) {
        this.yearInterestRateStr = yearInterestRateStr;
    }
}
