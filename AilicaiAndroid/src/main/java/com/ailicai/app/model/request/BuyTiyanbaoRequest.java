package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 购买体验宝request
 * Created by liyanan on 16/8/15.
 */
@RequestPath("/tiyanbao/buyTiyanbao.rest")
public class BuyTiyanbaoRequest extends Request {
    private long activityId;//活动id
    private int couponId;//卡券id
    private String paypwd; //交易密码
    private int checkPwd;//是否验证密码 0否 1是

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public int getCheckPwd() {
        return checkPwd;
    }

    public void setCheckPwd(int checkPwd) {
        this.checkPwd = checkPwd;
    }
}
