package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.RewardRecord;
import com.huoqiu.framework.rest.Response;

import java.util.List;

public class RewardRecordResponse extends Response {

    List<RewardRecord> rewardRecordList;//最多十条奖励记录

    public List<RewardRecord> getRewardRecordList() {
        return rewardRecordList;
    }

    public void setRewardRecordList(List<RewardRecord> rewardRecordList) {
        this.rewardRecordList = rewardRecordList;
    }
}
