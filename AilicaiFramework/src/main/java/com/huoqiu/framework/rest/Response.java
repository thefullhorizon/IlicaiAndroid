package com.huoqiu.framework.rest;


import java.io.Serializable;
import java.util.Observable;


/**
 * @author leo.li
 */
public class Response extends Observable implements Serializable {

    private int errorCode = 0;
    private String message = "";

    /**
     * 业务代码
     * 0:default
     * 1:非工作时间
     */
    private int bizCode;

    private UpdateInfo updateInfo;

    /**
     *
     */
    public Response() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getBizCode() {
        return bizCode;
    }

    public void setBizCode(int bizCode) {
        this.bizCode = bizCode;
    }

    public boolean isOutWork() {
        return this.bizCode == 1;
    }

    public UpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    @Override
    public String toString() {
        return "Response{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", bizCode=" + bizCode +
                ", updateInfo=" + updateInfo +
                '}';
    }
}
