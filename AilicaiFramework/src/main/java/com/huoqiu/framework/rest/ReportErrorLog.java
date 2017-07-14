package com.huoqiu.framework.rest;

import java.io.Serializable;

public class ReportErrorLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 时区：东八区，格式为yyyy-MM-dd’T’HH:mm:ss
     */
    private String logTime;
    /**
     * error : 运行期错误；
     * warn : 警告信息；
     * info: 有意义的事件信息，如程序启动，关闭事件，收到请求事件等；
     * debug: 调试信息，可记录详细的业务处理到哪一步了，以及当前的变量状态；
     * trace: 更详细的跟踪信息；
     */
    private String logLevel;
    private String projectName;
    private String IMEI;
    private String OS;

    ReportRequest reportRequest;

    public ReportRequest getReportRequest() {
        return reportRequest;
    }

    public void setReportRequest(ReportRequest reportRequest) {
        this.reportRequest = reportRequest;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}