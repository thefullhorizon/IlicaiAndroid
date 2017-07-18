package com.ailicai.app.model.bean;

import java.io.Serializable;

/**
 * Created by Gerry on 2016/7/7.
 */
public class ActivityRuleModel implements Serializable {
    private long ruleId; //规则id
    private String name; //规则名称
    private int reachRule;//规则满金额
    private int awardMoney;//返金额
    private String memo;//文案

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReachRule() {
        return reachRule;
    }

    public void setReachRule(int reachRule) {
        this.reachRule = reachRule;
    }

    public int getAwardMoney() {
        return awardMoney;
    }

    public void setAwardMoney(int awardMoney) {
        this.awardMoney = awardMoney;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
