package com.huoqiu.framework.rest;

import java.io.Serializable;

public class UpdateInfo implements Serializable {

    /**
     * 最新版本号
     */
    private String version;

    /**
     * 是否需要强制更新 0：不需要 1：强制更新
     */
    private int forceUpdate;

    /**
     * 更新下载地址
     */
    private String downloadUrl;

    /**
     * 安装文件大小
     */
    private String fileSize;

    private long fileSizeByte; // 安装文件大小

    /**
     * 版本信息
     */
    private String versionInfo;
    private int needPopup;

    private String patchUrl = "";// 补丁下载地址
    private String patchSize = "";// 补丁大小
    private long patchSizeByte;// 补丁大小
    private int forcePatch; //补丁是否需要强制更新 0-不需要 1-强制更新
    private int popupPatch; //补丁在进入应用时是否主动提醒 0-不提醒；1-提醒

    private String hotFixUrl = "";//热修复jar下载地址


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public long getFileSizeByte() {
        return fileSizeByte;
    }

    public void setFileSizeByte(long fileSizeByte) {
        this.fileSizeByte = fileSizeByte;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }


    public int getNeedPopup() {
        return needPopup;
    }

    public void setNeedPopup(int needPopup) {
        this.needPopup = needPopup;
    }

    public String getPatchUrl() {
        return patchUrl;
    }

    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
    }

    public String getPatchSize() {
        return patchSize;
    }

    public void setPatchSize(String patchSize) {
        this.patchSize = patchSize;
    }

    public long getPatchSizeByte() {
        return patchSizeByte;
    }

    public void setPatchSizeByte(long patchSizeByte) {
        this.patchSizeByte = patchSizeByte;
    }

    public int getForcePatch() {
        return forcePatch;
    }

    public void setForcePatch(int forcePatch) {
        this.forcePatch = forcePatch;
    }

    public int getPopupPatch() {
        return popupPatch;
    }

    public void setPopupPatch(int popupPatch) {
        this.popupPatch = popupPatch;
    }

    public String getHotFixUrl() {
        return hotFixUrl;
    }

    public void setHotFixUrl(String hotFixUrl) {
        this.hotFixUrl = hotFixUrl;
    }
}
