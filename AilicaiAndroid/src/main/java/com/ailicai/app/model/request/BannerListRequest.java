package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by nanshan on 2017/5/22.
 * V6.8新增 Banner
 */
@RequestPath("/cms/bannerList.rest")
public class BannerListRequest extends Request {

    private long userId ; //
    private int btype = 0; //banner类型 0-普通banner，1-爱理财bannner，3-头条，4-贷总管，6-交房租

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }
}
