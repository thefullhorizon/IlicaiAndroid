package com.ailicai.app.model.response;

import java.io.Serializable;

public class ScoreDetailResponse implements Serializable {
    private int increment; // 分数变动情况
    private int reason; // 积分变动缘由(1:续期消耗, 2:升级消耗,3:投资获得)
    private String changeDate; // 改变日期(格式:yyyy-MM-dd)

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }
}
