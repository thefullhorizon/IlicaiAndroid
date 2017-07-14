package com.ailicai.app.message;

import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by duo.chen on 2015/8/10.
 */
public class NoticeListResponse extends Response {
    private int totalNum; //总件数
    private boolean hasNextPage; // 是否有分页
    private List<Notice> noticeList; // 通知列表

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<Notice> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<Notice> noticeList) {
        this.noticeList = noticeList;
    }
}
