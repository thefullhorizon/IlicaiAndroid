package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by duo.chen on 2016/5/17.
 */
public class HybridVersionResponse extends Response {
    private String downloadUrl; //下载地址
    private long fileSize; //文件大小
    private String version; //版本
    private int updateMethod;//更新方式（0-增量 ；1-全量）
    private String fileMd5; //文件的md5值

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getUpdateMethod() {
        return updateMethod;
    }

    public void setUpdateMethod(int updateMethod) {
        this.updateMethod = updateMethod;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }
}
