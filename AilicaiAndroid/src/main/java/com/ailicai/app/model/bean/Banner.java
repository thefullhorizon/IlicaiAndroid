package com.ailicai.app.model.bean;

/**
 * Created by nanshan on 5/22/2017.
 */

public class Banner {

    private long bannerId; // 用于统计ID
    private String imageUrl; // url
    private String detailUrl; // 详情url
    private int urlType; // 0-外部跳转(H5) 1-二手房 2-新房 3-租房 4-吉爱财 5-业主委托 6-购房百科 7-财产说 8-房贷计算器 9卖房委托 10出租委托 11钱包页 12资产页

    public long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public int getUrlType() {
        return urlType;
    }

    public void setUrlType(int urlType) {
        this.urlType = urlType;
    }
}

