package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Gerry on 2016/1/7.
 * 转出钱包页面初始数据
 */
@RequestPath("/ailicai/saleHuoqibaoPageInit.rest")
public class CurrentRollOutBaseInfoRequest extends Request {
    private long userId; //用户Id,通过请求Header里的uticket获取
    private String accountType; //转入类型：101-存钱罐；106-用户账户
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
