package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

@RequestPath("/user/freshData.rest")
public class FreshMsgDataRequest extends Request {
    private long userId;

    private int bizType; // 0 出租， 1 出售

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    @Override
    public String toString() {
        return "FreshMsgDataRequest{" +
                "userId=" + userId +
                ", bizType=" + bizType +
                '}';
    }
}
