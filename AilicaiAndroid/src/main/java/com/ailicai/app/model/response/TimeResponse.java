package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * 请求服务器时间
 *
 * @author zhangshuxin
 */
public class TimeResponse extends Response {

    private Long sysDate;

    public Long getSysDate() {
        return sysDate;
    }

    public void setSysDate(Long sysDate) {
        this.sysDate = sysDate;
    }

}
