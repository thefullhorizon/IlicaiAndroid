package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by duo.chen on 2016/5/17.
 */
@RequestPath("/About/htmlResourceVersion.rest")
public class HybridVersionUpdateRequest extends Request {
    private String version;//app现有资源版本号，没有版本号请传"0"
    private int netType;// 1-移动网络 2-wifi

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getNetType() {
        return netType;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }
}
