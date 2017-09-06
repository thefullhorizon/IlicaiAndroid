package com.ailicai.app.model.bean;

public class Reward {

    private int id; // 主键
    private String iconUrl; // 图标url
    private String name; // 名称(生日送券、大额充值、每月送券)
    private String memo; // 特说说明
    private String additionalInfo; // 额外信息(json形式存在)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
