package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * name: SplashScreenResponse <BR>
 * description: 获取闪屏弹窗信息 <BR>
 * create date: 2017/9/19
 *
 * @author: IWJW Zhou Xuan
 */
public class SplashScreenResponse extends Response {

    private String moduleName; //模块名称
    private String imgUrl; //模块图标图片地址
    private String jumpUrl; //跳转链接
    private Integer showTime; //展示时长
    private long validTill; //有效截止期  0 表示永久有效

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public Integer getShowTime() {
        return showTime==null?new Integer(0):showTime;
    }

    public void setShowTime(Integer showTime) {
        this.showTime = showTime;
    }

    public long getValidTill() {
        return validTill;
    }

    public void setValidTill(long validTill) {
        this.validTill = validTill;
    }
}
