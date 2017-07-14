package com.ailicai.app.setting;

import android.text.TextUtils;

import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.model.response.UpdateInfoResponse;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.rest.UpdateInfo;


public class AppInfo {
    private static AppInfo appInfo = new AppInfo();
    /**
     * mServerVersion:服务端下发的服务器最新版本号
     */
    private String mServerVersion = "0";
    private String mUpdateURL = "";// 最新版本的URL
    private String mUpdateRemark = "";// 更新提示
    private boolean isForceUpdate = false;// 是否强制升级
    private boolean isHaveUpdate = false;// 是否有新版本
    private String fileSize = ""; // 安装文件大小
    private String versionInfo = ""; // 版本信息
    private int needPopup; //在进入应用时是否主动提醒升级 0-不提醒；1-提醒
    private String patchDownloadURL = ""; // 最新patch下载URL
    private String patchSize; // 补丁文件大小

    public static AppInfo getInstance() {
        if (null == appInfo) {
            appInfo = new AppInfo();
        }
        return appInfo;
    }

    public boolean isHaveUpdate() {
        return isHaveUpdate;
    }

    public void setHaveUpdate(boolean isHaveUpdate) {
        this.isHaveUpdate = isHaveUpdate;
    }

    public String getmServerVersion() {
        return mServerVersion;
    }

    public void setmServerVersion(String mServerVersion) {
        this.mServerVersion = mServerVersion;
    }

    public String getmUpdateURL() {
        return mUpdateURL;
    }

    public void setmUpdateURL(String mUpdateURL) {
        this.mUpdateURL = mUpdateURL;
    }

    public String getmUpdateRemark() {
        return mUpdateRemark;
    }

    public void setmUpdateRemark(String mUpdateRemark) {
        this.mUpdateRemark = mUpdateRemark;
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public void setForceUpdate(boolean isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getNeedPopup() {
        return needPopup;
    }

    public void setNeedPopup(int needPopup) {
        this.needPopup = needPopup;
    }

    public String getAPP_VERSION() {
        return AppConfig.versionName;
    }

    public String getPatchDownloadURL() {
        return patchDownloadURL;
    }

    public void setPatchDownloadURL(String patchDownloadURL) {
        this.patchDownloadURL = patchDownloadURL;
    }

    public String getPatchSize() {
        return patchSize;
    }

    public void setPatchSize(String patchSize) {
        this.patchSize = patchSize;
    }

    public void setAppInfo(UpdateInfoResponse res) {
        if (res.getErrorCode() == 0) {
            setForceUpdate(res.getForceUpdate() == 1);
            setmServerVersion(res.getVersion());
            setmUpdateRemark("");
            setmUpdateURL(res.getDownloadUrl());

            setFileSize(res.getFileSize());
            setVersionInfo(res.getVersionInfo());
            setNeedPopup(res.getNeedPopup());

            setPatchDownloadURL(res.getPatchUrl());
            setPatchSize(res.getPatchSize());

            AppConfig.hotfix_url = res.getHotFixUrl();

            if (!TextUtils.isEmpty(AppConfig.hotfix_url)) {
                int startIndex = AppConfig.hotfix_url.lastIndexOf("/") + 1;
                int endIndex = AppConfig.hotfix_url.length();
                if (startIndex > 0 && startIndex < endIndex) {
                    AppConfig.hotfixPatchName = AppConfig.hotfix_url.substring(startIndex, endIndex);
                }
            }

            if (StringUtil.isBigThan(mServerVersion, AppConfig.versionName) && !TextUtils.isEmpty(mUpdateURL)) {
                setHaveUpdate(true);
            } else {
                setHaveUpdate(false);
            }
        }
    }


    public void setAppInfo(UpdateInfo res) {
        //setForceUpdate(res.getForceUpdate() == 1);
        setForceUpdate(true);
        setmServerVersion(res.getVersion());
        setmUpdateRemark("");
        setmUpdateURL(res.getDownloadUrl());

        setFileSize(res.getFileSize());
        setVersionInfo(res.getVersionInfo());
        setNeedPopup(res.getNeedPopup());

        setPatchDownloadURL(res.getPatchUrl());
        setPatchSize(res.getPatchSize());

        AppConfig.hotfix_url = res.getHotFixUrl();

        if (!TextUtils.isEmpty(AppConfig.hotfix_url)) {
            int startIndex = AppConfig.hotfix_url.lastIndexOf("/") + 1;
            int endIndex = AppConfig.hotfix_url.length();
            if (startIndex > 0 && startIndex < endIndex) {
                AppConfig.hotfixPatchName = AppConfig.hotfix_url.substring(startIndex, endIndex);
            }
        }

        if (StringUtil.isBigThan(mServerVersion, AppConfig.versionName) && !TextUtils.isEmpty(mUpdateURL)) {
            setHaveUpdate(true);
        } else {
            setHaveUpdate(false);
        }
    }
}
