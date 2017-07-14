package com.ailicai.app.message;


import com.ailicai.app.common.reqaction.RequestPath;
import com.ailicai.app.model.request.Request;

/**
 * Created by duo.chen on 2015/8/11.
 */
@RequestPath("/notice/getNoticeNums.rest")
public class NoticeRequest extends Request {
    private long userId; // 用户ID
    private int dataStatus = 0; // 1：获取免打扰的经纪人列表，0不获取

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }
}
