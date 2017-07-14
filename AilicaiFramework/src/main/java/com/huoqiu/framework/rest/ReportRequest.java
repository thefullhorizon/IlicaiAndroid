package com.huoqiu.framework.rest;

import java.io.Serializable;

public class ReportRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String restUrl; //接口URL（见对应接口）
    private String result = "1"; //调用结果 1:正常；2：timeout 3：服务端报错
    private String responseTime = "0"; //响应时间（正常调用时）,单位毫秒
    private String errorMessage; //服务端报错信息
    private String errorCode; //http错误代码
    private String logLevel; //Log级别


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    /**

     * error : 运行期错误；
     * warn : 警告信息；
     * info: 有意义的事件信息，如程序启动，关闭事件，收到请求事件等；
     * debug: 调试信息，可记录详细的业务处理到哪一步了，以及当前的变量状态；
     * trace: 更详细的跟踪信息；
     * @param logLevel
     */
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getRestUrl() {
        return restUrl;
    }

    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ReportRequest{" +
                "restUrl='" + restUrl + '\'' +
                ", result=" + result +
                ", responseTime=" + responseTime +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}