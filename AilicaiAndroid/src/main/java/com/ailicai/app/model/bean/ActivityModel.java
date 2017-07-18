package com.ailicai.app.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gerry on 2016/7/7.
 */
public class ActivityModel implements Serializable {
    private long activityId; //活动id
    private long configId; //业务id
    private int relationId; //活动关联表id
    private List<ActivityRuleModel> ruleList; //活动规则列表

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

    public List<ActivityRuleModel> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<ActivityRuleModel> ruleList) {
        this.ruleList = ruleList;
    }
}
