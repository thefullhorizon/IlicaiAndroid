package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Function: 更新检查需传递终端类型
 *
 * @author hu-bin
 * @Date 2014年6月19日        下午3:43:01
 * @see
 */
@RequestPath("/user/updateInfo.rest")
public class UpdateInfoRequest extends Request {
    /**
     * 终端类型 1：android 2：iphone
     */
    private int type;

    /**
     * 渠道号
     */
    private String channel;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


}
