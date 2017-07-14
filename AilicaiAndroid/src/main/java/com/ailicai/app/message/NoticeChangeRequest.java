package com.ailicai.app.message;


import com.ailicai.app.common.reqaction.RequestPath;
import com.ailicai.app.model.request.Request;

/**
 * Created by duo.chen on 2015/8/10.
 */
@RequestPath("/notice/changeNoticeStatus.rest")
public class NoticeChangeRequest extends Request {

    private final static int INITTYPE = 0;
    public final static int READ = INITTYPE + 1;
    public final static int DEL = INITTYPE + 2;
    private long userId; // 用户ID
    private long id; //消息Id
    private int type = 2; //1：阅读 2：删除
    private int noticeType; //通知类型 1：提醒，2：资讯 3：动态 (背景：消息服务表拆分成三张表,删除的时候需要知道类型)

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }
}
