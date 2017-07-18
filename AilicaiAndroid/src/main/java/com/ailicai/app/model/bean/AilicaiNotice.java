package com.ailicai.app.model.bean;

/**
 * Created by nanshan on 6/16/2017.
 */

public class AilicaiNotice {

    private String noticeTitle; // 公告标题

    private String noticeUrl; // 公告地址

    private long noticeId; // 公告编号

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }
}
