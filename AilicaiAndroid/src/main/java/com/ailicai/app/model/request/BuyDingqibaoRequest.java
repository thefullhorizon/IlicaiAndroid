package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Jer on 2016/1/7.
 */
@RequestPath("/ailicai/buyDingqibao.rest")
public class BuyDingqibaoRequest extends Request {
    private String productId; //产品编号
    private double amount;//申购金额
    private String paypwd; //交易密码，RSA加密
    private int voucherId; // 卡券id 5.2增加
    //参加活动的参数start 不参加活动则不需要传
    private long activityId; //活动id
    private long configId; //业务id
    private int relationId; //活动关联表id
    private long ruleId; //规则id
    //参加活动的参数end


    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }
}
