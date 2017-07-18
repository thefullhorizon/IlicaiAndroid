package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Administrator on 2015/7/23.
 */

@RequestPath("/activity/optionReport.rest")
public class OptionReportRequest extends Request {
    private long bannerId; //bannerId
    private int type; //业务类型：0-一般业务（app大首页banner，爱理财banner，开屏弹窗） 1-分享弹窗业务 （微信分享）
    private int status; //0-banner点击统计pv/uv     2-弹出弹窗的次数 3-点击分享分享朋友圈 4-点击分享给好友

    public long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
