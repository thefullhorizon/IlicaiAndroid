package com.ailicai.app.model.bean;

import java.io.Serializable;

public class RewardRecord implements Serializable {

    private String rewardTime = "";//奖励时间

    private int status;         //奖励状态 0：待发放，1：已发放

    private String reward = "";//返利金额

    public String getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(String rewardTime) {
        this.rewardTime = rewardTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
