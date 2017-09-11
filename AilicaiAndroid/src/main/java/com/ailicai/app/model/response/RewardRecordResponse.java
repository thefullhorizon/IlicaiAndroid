package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.RewardRecord;
import com.huoqiu.framework.rest.Response;

import java.util.List;

public class RewardRecordResponse extends Response {
    //返回代码 0-正常 其他参考对应的errorCode定义，2104表示查询数据超过90天

    List<RewardRecord> rewardRecordList;//最多十条奖励记录

    public List<RewardRecord> getRewardRecordList() {
        return rewardRecordList;
    }

    public void setRewardRecordList(List<RewardRecord> rewardRecordList) {
        this.rewardRecordList = rewardRecordList;
    }
}
