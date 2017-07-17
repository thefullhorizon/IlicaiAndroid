package com.ailicai.app.common.imageloader;

import java.io.Serializable;

/**
 * Created by zhouxuan on 2016/11/14.
 */

public class ImageDataReportBean implements Serializable {

//    pf=android-picfilt/ios-picfilt
//    url={图片url}
//    resptime={响应时间}（毫秒）

    public String url;
    public long resptime;
    public String pf = "android-picfilt";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getResptime() {
        return resptime;
    }

    public void setResptime(long resptime) {
        this.resptime = resptime;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }
}
