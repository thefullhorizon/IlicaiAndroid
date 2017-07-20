package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

@RequestPath("/ailicai/rewardRecord.rest")
public class RewardRecordRequest extends Request {
    public long userid;

    private int startIndex = 0;//起始行

    private int pageSize = 10;//页面显示行数

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
