package com.ailicai.app.model.bean;

import java.io.Serializable;

/**
 * 开屏弹窗
 * Created by jeme on 2017/5/24.
 */

public class OpenScreenPopModel implements Serializable {

    public static final String OPENSCREENPOP_TAG = "openScreenPopTag_";

    public static final int POS_OPEN_SCREEN = 0;//开屏
    public static final int POS_INVEST = 6;//投资
    public static final int POS_MINE = 7;//我的

    public static final int POP_TYPE_H5 = 0;//外部跳转(H5)
    public static final int POP_TYPE_LICAI = 4;//吉爱财
    public static final int POP_TYPE_NONE = 31;//无跳转

    private long adPopupId; //推广弹出Id
    private String adPopupUrl = ""; //推广弹出url，根据类型来判断是否使用adPopupUrl
    private String adPopupImgUrl = ""; //广告弹窗的图片Url
    private int adPopupUrlType; ////0-外部跳转(H5) 4-爱理财 31-无跳转

    private int popPosition = -1; // 开屏弹窗展示的位置 0-开屏弹窗 6-吉爱财【投资】 7-【我的】


    public long getAdPopupId() {
        return adPopupId;
    }

    public void setAdPopupId(long adPopupId) {
        this.adPopupId = adPopupId;
    }

    public String getAdPopupUrl() {
        return adPopupUrl;
    }

    public void setAdPopupUrl(String adPopupUrl) {
        this.adPopupUrl = adPopupUrl;
    }

    public String getAdPopupImgUrl() {
        return adPopupImgUrl;
    }

    public void setAdPopupImgUrl(String adPopupImgUrl) {
        this.adPopupImgUrl = adPopupImgUrl;
    }

    public int getAdPopupUrlType() {
        return adPopupUrlType;
    }

    public void setAdPopupUrlType(int adPopupUrlType) {
        this.adPopupUrlType = adPopupUrlType;
    }

    public int getPopPosition() {
        return popPosition;
    }

    public void setPopPosition(int popPosition) {
        this.popPosition = popPosition;
    }

    @Override
    public String toString() {
        return "OpenScreenPopModel{" +
                "adPopupId=" + adPopupId +
                ", adPopupUrl='" + adPopupUrl + '\'' +
                ", adPopupImgUrl='" + adPopupImgUrl + '\'' +
                ", adPopupUrlType=" + adPopupUrlType +
                ", popPosition=" + popPosition +
                '}';
    }
}
