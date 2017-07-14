package com.ailicai.app.message;


import com.ailicai.app.common.reqaction.RequestPath;
import com.ailicai.app.model.request.Request;

/**
 * Created by duo.chen on 2015/8/10.
 */
@RequestPath("/notice/getNoticeList.rest")
public class NoticeListRequest extends Request {
    private long userId; // 用户ID
    private int type; //1：提醒 2：资讯
    private int pageSize = 20; //每次加载20条数据
    private int offSet = 0; //数据偏移量

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }
}
